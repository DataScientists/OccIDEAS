(function () {
    angular.module('occIDEASApp.Interviews')
        .controller('InterviewsCtrl', InterviewsCtrl);

    InterviewsCtrl.$inject = ['data', '$scope', '$mdDialog', 'FragmentsService',
        '$q', 'QuestionsService', 'ModulesService', 'InterviewsService', 'ParticipantsService', 'AssessmentsService',
        '$anchorScroll', '$location', '$mdMedia', '$window', '$state', '$rootScope','$compile','$timeout','$log'];
    function InterviewsCtrl(data, $scope, $mdDialog, FragmentsService,
                            $q, QuestionsService, ModulesService, InterviewsService, ParticipantsService, AssessmentsService,
                            $anchorScroll, $location, $mdMedia, $window, $state, $rootScope,$compile,$timeout,$log) {
        var self = this;
        $scope.data = data;
        
        $scope.showIntroModule = true;
        $scope.showModule = false;
        $scope.showAjsm = false;
        $scope.refNoPattern = "H([a-zA-Z0-9]){3}(-)([a-zA-Z0-9]){3}";
        $scope.multiSelected = [];
        $scope.questionHistory = [];
        $scope.updateAnswers = false;
        
        $rootScope.$on('InterviewCtrl:update', function (event, elId) {
        	$scope.addClassWithTimeout('IntResult'+elId);
        	safeDigest($scope.interviews);
    	});
        
        $scope.addClassWithTimeout = function(elId){
        	$timeout(function() {
        		if(!$('#'+elId).hasClass("highlight")){
            	    $('#'+elId).addClass("highlight");
            	}
        	},1000);
        };

        self.editQuestion = function(question){
        	var interview = $scope.activeInterview;
        	if(!interview){
        		return null;
        	}
        	refreshInterview();
        	$scope.inProgress = true;
        	$scope.updateEnable = true;
        	$scope.updateFirst = true;
        	safeDigest($scope.inProgress);
        	var number = (question.number.substr(question.number.length - 1) - 1);
        	var newNum;
        	if(number == 0 && question.number.length > 1){
        		newNum = question.number.substring(0, question.number.length - 1);
        	}
        	var parentId = question.number.length > 1?question.parentAnswerId:question.parentId;
        	var actualQuestion = {};
        	actualQuestion.topNodeId = question.topNodeId;
        	actualQuestion.questionId = question.questionId;
        	actualQuestion.parentId = parentId,
        	actualQuestion.number = newNum?newNum:question.number.slice(0, -1) +number;		

			var status = showNextQuestion(actualQuestion,false,true,question.count);
        	if(status){
        		status.then(function(data){
        			if(data == 200){
        				var answerList = [];
        				var mod = _.find($scope.activeInterview.modules,function(mod){
        					return mod.idNode == $scope.data.showedQuestion.topNodeId;
        				});
        				var qs = _.find(mod.questionsAsked,function(qs){
        					return $scope.data.showedQuestion.idNode == qs.questionId;
        				});
        				if(qs.answers.length > 1 ){
        					_.each($scope.data.showedQuestion.nodes,function(node){
        						_.find(qs.answers,function(ans){
        							if(ans.answerId == node.idNode){
        								$scope.multiToggle(node,$scope.multiSelected);
        								node.isSelected = true;
        								answerList.push(ans);
        							}
        						});
        					});
        				}else{
        					var qs = _.find(mod.questionsAsked,function(qs){
            					return $scope.data.showedQuestion.idNode == qs.questionId;
            				});
        				   var ansNode = _.find($scope.data.showedQuestion.nodes,function(ans){
        					   return ans.idNode == qs.answers[0].answerId;
        				   });
        				   _.find(qs.answers,function(ans){
   								if(ans.answerId == ansNode.idNode){
   									answerList.push(ans);
   								}
        				   });
        				   $scope.data.showedQuestion.selectedAnswer = ansNode;
        				   $scope.previousAnswer =ansNode;
        				}
//        				deleteQuestion([question]);
        			}
        		});
        	}
        	
        };
        
        
        function determineNextUnansweredQuestion(question){
        	var questionAsked = hasQuestionBeenAsked(question);
        	if(questionAsked && !$scope.updateFirst){
        		if(questionAsked.answers.length == 1){
        		var actualQuestion =
            	{
					topNodeId:questionAsked.topNodeId,
					questionId:questionAsked.questionId,
        	        parentId:questionAsked.answers[0].answerId,
        	        number:questionAsked.answers[0].number,
        	        link: questionAsked.link
            	}
				return showNextQuestion(actualQuestion,true,false,question.count);
        		}
        	}
        	$scope.updateFirst = false;
        	$scope.data.showedQuestion = question;
        	return false;
        }

        function refreshInterview(){
        	$scope.data.interviewStarted = true;
			$scope.data.interviewEnded = false;
			safeDigest($scope.data.interviewStarted);
        }
        
        function deleteQuestionWithParentAnswer(answerList){
        	var mod = _.find($scope.activeInterview.modules,function(mod){
				return mod.idNode == $scope.data.showedQuestion.topNodeId;
			});
        		for(var i=0;i < answerList.length;i++){
        			var answer = answerList[i];
        			for(var j=0;j < mod.questionsAsked.length;j++){
        				if(mod.questionsAsked[j].questionId == answer.parentQuestionId
        						|| mod.questionsAsked[j].parentAnswerId == answer.answerId){
        					var question = mod.questionsAsked[j];
        					deleteModuleWithParentAnswer(answer);
        					question.deleted = 1;
        					answer.deleted = 1;
        					if(mod.questionsAsked[j].parentAnswerId == answer.answerId){
        						deleteQuestionWithParentAnswer(question.answers);
        					}
            				InterviewsService.saveQuestion(question).then(function (response) {
                    			if (response.status === 200) {
                    				console.log("Delete question successful...");
                    				_.remove(mod.questionsAsked,function(val){
                    	        		return val.deleted == 1;
                    	        	});
                    			}
                    		});
        				}
        			}
        		}
        }
        
        function deleteModuleWithParentAnswer(answer){
        	_.find($scope.activeInterview.modules,function(mod,index){
        		if(index == 0){
        			return false;
        		}
        		if(!mod){
        			return false;
        		}
	        	if(mod.parentAnswerId == answer.answerId){
	        		_.each(mod.questionsAsked,function(qs){
	        			mod.deleted = 1;
    	        		qs.deleted = 1;
    	        		_.each(qs.answers,function(ans){
    	        			ans.deleted = 1;
    	        			return deleteModuleWithParentAnswer(ans);
    	        		});
    	        	});
	        	}
	        	_.remove($scope.activeInterview.modules,function(mod){
	        		if(mod.answerNode && mod.answerNode == answer.answerId){
	        			deleteModuleWithParentModule(mod);
	        			return true;
	        		}
	        		return false;
	        	});
        	});
        }
        
        function deleteModuleWithParentModule(module){
        	_.remove($scope.activeInterview.modules,function(mod){
	        	if(mod.parentAnswerId && mod.parentAnswerId == module.idNode){
	        		mod.deleted = 1;
	        		deleteModuleWithParentModule(mod);
	        		deleteQuestion(mod.questionsAsked);
	        		return true;
	        	}
	        	return false;
        	});
        }
        
        function deleteQuestion(questions,defer){
        	_.each(questions,function(question){
        		question.deleted = 1;
        		_.each(question.answers,function(ans){
        			ans.deleted = 1;
        			deleteQuestionWithParentAnswer(question.answers);
        		});
        		InterviewsService.saveQuestion(question).then(function (response) {
        			if (response.status === 200) {
        				console.log("Delete question successful...");
        				var mod = _.find($scope.activeInterview.modules,function(mod){
        					return mod.idNode == $scope.data.showedQuestion.topNodeId
        						&& mod.count == $scope.data.showedQuestion.count;
        				});
        				if(!mod){
        					mod = _.find($scope.activeInterview.modules,function(mod){
            					return mod.idNode == $scope.data.showedQuestion.topNodeId
        					});
        				}
        				_.remove(mod.questionsAsked,function(val){
        	        		return val.questionId === question.questionId;
        	        	});
        				defer.resolve();
        			}else{
        				defer.reject();
        			}
        		});
        	})
        	return defer.promise;
        }
        
        var safeDigest = function (obj){
        	if (!obj.$$phase) {
		        try {
		        	obj.$digest();
		        }
		        catch (e) { }
        	}
        }

        
        function populateNewQuestionAskedJsonByNode(interview,node){
        	return {
              	  idInterview:$scope.interviewId,
              	  topNodeId:node.topNodeId,
              	  questionId:node.idNode,
              	  parentId:$scope.parentQId?$scope.parentQId:node.parentId,
              	  parentAnswerId:node.parentId,
              	  name:node.name,
              	  description:node.description,
              	  nodeClass:node.nodeclass,
              	  number: node.number,
              	  type: node.type,
              	  link: node.link,
              	  deleted: 0,
              	  answers: []
            };
        }
        
        function populateAnswerJsonByNode(value,node){
        	return {
      		  idInterview:$scope.interviewId,
       		  topQuestionId:node.topNodeId,
       		  parentQuestionId:node.idNode,
       		  answerId:value.idNode,
       		  name:value.name,
       		  answerFreetext:value.name,
     		  nodeClass:value.nodeclass,
       		  number:value.number,
       		  type:value.type,
       		  link: node.link,
       		  deleted:0,
       		  isProcessed:false
        	};
        }
        
        function processInterviewQuestionsWithMultipleAnswers(interview,node){
        	var mod = _.find(interview.modules,function(val,ind){
              	return val.idNode === node.topNodeId && node.count == val.count;
            });
        	if(!mod){
        		mod = _.find(interview.modules,function(val,ind){
                  	return val.idNode === node.topNodeId;
                });
        	}
        	
        	var newQuestionAsked = populateNewQuestionAskedJsonByNode(interview,node);
        	
        	  var selectedEl = $scope.multiSelected;
              $scope.multiSelected = [];
              _.each(selectedEl,function(value,i){
            	 var actualAnswer = populateAnswerJsonByNode(value,node);
            	 newQuestionAsked.answers.push(actualAnswer);
              });
              
              //check if question already exist, do not push, just update the
          	  // question in database
              var qsIndex = _.indexOf(mod.questionsAsked, 
            	_.find(mod.questionsAsked, function(qs){
            	  return qs.questionId == node.idNode;
            	})
              );
              if(qsIndex == -1){
            	  mod.questionsAsked.push(newQuestionAsked);
              }else{
            	  mod.questionsAsked.splice(qsIndex, 1, newQuestionAsked);
              }
              
              InterviewsService.saveQuestion(newQuestionAsked).then(function (response) {
      			if (response.status === 200) {
      				newQuestionAsked.answers[0].isProcessed = true;
      				var answer = newQuestionAsked.answers[0];
      				var actualQuestion =
                  	{
      						topNodeId:node.topNodeId,
        					questionId:node.idNode,
                	        parentId:answer.answerId,
                	        number:answer.number,
                	        link: node.link
                  	}
      				showNextQuestion(actualQuestion,true,false,mod.count);
      			}
      		});
        }
        
        function processFrequency(interview,node){
        	var deffered = undefined;
        	if($scope.updateEnable && $scope.previousAnswer != $scope.data.showedQuestion.selectedAnswer){
        		var qs = hasQuestionBeenAsked(node);
        		deffered = $q.defer();
        		if(qs){
        			deleteQuestion([qs],deffered);
        		}else{
        			deffered.resolve();
        		}
        	}
        	if(deffered){
        	deffered.promise.then(function(){
        		buildAndSaveFrequency(interview,node);
        	})
        	}else{
        		buildAndSaveFrequency(interview,node);
        	}
        }
        
        function buildAndSaveFrequency(interview,node){
        	var hours = 0;
            if(node.hours){
            	hours = node.hours;
            }
            var minutes = 0;
            if(node.minutes){
            	minutes = node.minutes;
            }
            var answerValue = Number(hours) + (Number(minutes)/60);
            var answer = node.nodes[0];
            
            var newQuestionAsked = {
              	  idInterview:$scope.interviewId,
              	  topNodeId:node.topNodeId,
              	  questionId:node.idNode,
              	  parentId:$scope.parentQId?$scope.parentQId:node.parentId,
              	  name:node.name,
              	  description:node.description,
              	  nodeClass:node.nodeclass,
              	  number: node.number,
              	  type: node.type,
              	  link: node.link,
              	  deleted: 0,
              	  answers: [{
              		  idInterview:$scope.interviewId,
              		  topQuestionId:node.topNodeId,
              		  parentQuestionId:node.idNode,
              		  answerId:answer.idNode,
              		  name:answer.name,
              		  description:answer.description,
              		  nodeClass:answer.nodeclass,
              		  number:answer.number,
              		  answerFreetext:answerValue,
              		  link: answer.link,
              		  type:answer.type,
              		  deleted:0
              	  }]
              }
              var mod = _.find(interview.modules,function(val,ind){
              	return val.idNode === node.topNodeId && node.count == val.count;
              });
              if(!mod){
            	  mod = _.find(interview.modules,function(val,ind){
                    	return val.idNode === node.topNodeId;
                  });
              }
              //check if question already exist, do not push, just update the
          	// question in database
              var qsIndex = _.indexOf(mod.questionsAsked, 
            	_.find(mod.questionsAsked, function(qs){
            	  return qs.questionId == node.idNode;
            	})
              );
              if(qsIndex == -1){
            	  mod.questionsAsked.push(newQuestionAsked);
              }else{
            	  mod.questionsAsked.splice(qsIndex, 1, newQuestionAsked);
              }
              InterviewsService.saveQuestion(newQuestionAsked).then(function (response) {
      			if (response.status === 200) {
      				var actualQuestion =
                  	{
      					topNodeId:node.topNodeId,
      					questionId:node.idNode,
              	        parentId:answer.idNode,
              	        number:answer.number,
              	        link: node.link
                  	}
      				showNextQuestion(actualQuestion,true,false,mod.count);
      			}
      		});
        }
        
        function processQuestion(interview,node){
        	var deffered = undefined;
        	if($scope.updateEnable && $scope.previousAnswer != $scope.data.showedQuestion.selectedAnswer){
        		var qs = hasQuestionBeenAsked(node);
        		deffered = $q.defer();
        		if(qs){
        			deleteQuestion([qs],deffered);
        		}else{
        			deffered.resolve();
        		}
        	}
        	if(deffered){
        	deffered.promise.then(function(){
        		buildAndSaveQuestion(interview,node);
        	})
        	}else{
        		buildAndSaveQuestion(interview,node);
        	}
        }
        
        function buildAndSaveQuestion(interview,node){
        	var answer = node.selectedAnswer;
        	
            var newQuestionAsked = {
            	  idInterview:$scope.interviewId,
            	  topNodeId:node.topNodeId,
            	  questionId:node.idNode,
            	  parentId:$scope.parentQId?$scope.parentQId:node.parentId,
            	  parentAnswerId:node.parentId,
            	  name:node.name,
            	  description:node.description,
            	  nodeClass:node.nodeclass,
            	  number: node.number,
            	  type: node.type,
            	  link: node.link,
            	  deleted: 0,
            	  answers: [{
            		  idInterview:$scope.interviewId,
            		  topQuestionId:node.topNodeId,
            		  parentQuestionId:node.idNode,
            		  answerId:answer.idNode,
            		  name:answer.name,
            		  description:answer.description,
            		  nodeClass:answer.nodeclass,
            		  number:answer.number,
            		  link: answer.link,
            		  type:answer.type,
            		  deleted:0
            	  }]
            }
            var mod = _.find(interview.modules,function(val,ind){
            	return val.idNode === node.topNodeId && node.count == val.count;
            });
            if(!mod){
            	mod = _.find(interview.modules,function(val,ind){
                	return val.idNode === node.topNodeId;
                });
            }
            //check if question already exist, do not push, just update the
        	// question in database
            var qsIndex = _.indexOf(mod.questionsAsked, 
          	_.find(mod.questionsAsked, function(qs){
          	  return qs.questionId == node.idNode && node.count == mod.count;
          	})
            );
            if(qsIndex == -1){
          	  mod.questionsAsked.push(newQuestionAsked);
            }else{
          	  mod.questionsAsked.splice(qsIndex, 1, newQuestionAsked);
            }
            InterviewsService.saveQuestion(newQuestionAsked).then(function (response) {
    			if (response.status === 200) {
    				var actualQuestion =
                	{
    					topNodeId:node.topNodeId,
    					questionId:node.idNode,
            	        parentId:answer.idNode,
            	        number:answer.number,
            	        link: node.link
                	}
    				showNextQuestion(actualQuestion,true,false,mod.count);
    			}
    		});
        }
        
        function validateIfAnswerSelected(node){
        	if(node.type == 'Q_multiple'){
        		var results = _.find($scope.data.showedQuestion.nodes,function(val,ind){
        			return val.isSelected;
        		});
        		return results < 1;
        	}else if(node.type == 'Q_frequency'){
        		_.each(node.childNodes,function(val,ind){
        			if(val.type == 'P_frequencyweeks'){
        				return !node.name;
        			}
        				
        			if(val.type == 'P_frequencyshifthours'){
        				return !$scope.data.showedQuestion.hours || !$scope.data.showedQuestion.minutes;
        			}
        			if(val.type == 'P_frequencyhoursminute'){
        				return !$scope.data.showedQuestion.hours || !$scope.data.showedQuestion.minutes;
        			}
        		})
        	}else if(node.type == 'Q_single' || node.type == 'Q_simple'){
        		return !$scope.data.showedQuestion.selectedAnswer;
        	}
        	
        	return false;
        }
        
        $scope.saveAnswerQuestion = function (node) {
        	if(validateIfAnswerSelected(node)){
        		alert("Please select an answer.");
        		return;
        	}
        	
        	$scope.inProgress = true;
        	ParticipantsService.findInterviewParticipant($scope.participant.idParticipant).then(function (response) {
                if (response.status === 200) {
                	$scope.participant = response.data[0];
                	
                	var interview = $scope.activeInterview;
                	if(!interview){
                		return null;
                	}
                    if (node.type == 'Q_multiple') {
                    	processInterviewQuestionsWithMultipleAnswers(interview,node);
                    } else if (node.type == 'Q_frequency') {
                        processFrequency(interview,node);
                    } else {
                    	processQuestion(interview,node);
                    }
                }
        	}); 
        }
        
        function saveInterview(interview){
           	InterviewsService.save(interview).then(function (response) {
        		if (response.status === 200) {
        			$log.info("Saving interview with id:" + interview.interviewId +" successful");
        		}
        	});
        }
        function saveParticipant(participant){       	
        	ParticipantsService.save(participant).then(function (response) {
        		if (response.status === 200) {
        			$log.info("Saving Participant " + participant.idParticipant +" was successful");
        		}
        	});     	
        }
        
        function createReferenceNumber(data){
        	 $scope.referenceNumber = data.referenceNumber;
             if (!$scope.referenceNumber) {
                 $scope.referenceNumber = 'TEST' + Math.floor((Math.random() * 100) + 1);
             }
        }
        
        function createParticipant(data){
        	var participant = {reference:$scope.referenceNumber,
            		interviews:[]}
            ParticipantsService.createParticipant(participant).then(function (response){
            	if (response.status === 200) {
            		$scope.participant = response.data;
            		InterviewsService.findModule($scope.data[0].idNode)
                    .then(function (response) {
                        console.log("Data getting from questions AJAX ...");
                        $scope.data = response.data;
                        var interview = {};
                        interview.module = $scope.data[0];
                        interview.referenceNumber = $scope.referenceNumber;
                        interview.modules = [];
                        interview.modules.push({
                        	name:interview.module.name,
                        	idNode:interview.module.idNode,
                        	deleted:0,
                        	count:1,
                        	questionsAsked:[]
                        });
                        var copyParticipant = angular.copy($scope.participant);
                        interview.participant = copyParticipant;
                        $scope.interview = {};
                        $scope.interview = interview;
                        $scope.activeInterview = interview;
                        interview.active = true;
                        InterviewsService.startInterview(interview).then(function (response) {
                            if (response.status === 200) {
                            	$scope.interviewId = response.data.interviewId;
                            	ParticipantsService.save($scope.participant).then(function (response) {
                                    if (response.status === 200) {
                                    	$scope.data.interviewStarted = true;
                                        $scope.data.interviewEnded = false;
                                        var actualQuestion =
                                        	{
                                    	        parentId:interview.module.idNode,
                                    	        number:'0'
                                        	}
                                        showNextQuestion(actualQuestion,false,false,1);
                                    } else {
                                        console.log('ERROR on Start Interview!');
                                    }
                                });
                            }
                        });
                    });
            	}	
            });
        }
        
        $scope.startInterview = function (data) {
           createReferenceNumber(data);
           createParticipant(data);
        }
        
        $scope.scrollWithTimeout = function(elId){
        	$timeout(function() {
        		$scope.scrollTo(elId);
            }, 500);
        }
        
        resetSelectedIndex();

        $scope.singleChoiceHandler = function ($index) {
            $scope.selectedIndex = $index;
        }
        $scope.multipleChoiceHandler = function (parentId, id) {
            var el = angular.element(document.querySelector("#interviewing-" + parentId + " #answer-" + id));
            if (el.hasClass('selected')) {
                el.removeClass('selected');
            } else {
                el.addClass('selected');
            }
        }
        function resetSelectedIndex() {
            $scope.selectedIndex = -1;
        }

        $scope.scrollTo = function (target) {
            var scrollPane = null;
            if ($scope.showIntroModule) {
                scrollPane = $('#interview-intromodule-tree');
            } else if ($scope.showModule) {
                scrollPane = $('#interview-module-tree');
            } else if ($scope.showAjsm) {
                scrollPane = $('#interview-ajsm-tree');
            }
            var scrollTarget = $('#' + target);
            if (scrollTarget) {
                if (scrollTarget.offset()) {
                	var currentScroll = 0;
                	if(scrollPane.scrollTop()){
                		currentScroll = scrollPane.scrollTop();
                	}
                	var offset = 150;
                	var top = scrollTarget.offset().top;
                	//alert(top);
                	var currentScroll = scrollPane.scrollTop();
                	//alert(currentScroll);
                    var scrollY =  top - offset + currentScroll;
                    scrollPane.animate({scrollTop: scrollY}, 1000, 'swing');
                    angular.element(document.querySelector("#tree-root-interviewing #" + target)).addClass('highlight-interview');
                    angular.element(document.querySelector("#tree-root-interviewing #" + target)).addClass('highlight');
                }
            }
        };

        $scope.multiToggle = function (item, list) {
            var idx = list.indexOf(item);
            if (idx > -1) list.splice(idx, 1);
            else list.push(item);
        };
        $scope.multiExists = function (item, list) {
            return list.indexOf(item) > -1;
        };
        $scope.debug = function(element){
        	var scrollPane = $('#interview-intromodule-tree');    	
        	//var top = element.$element.context.offsetTop;     	    	
        	//alert(top);
        	//scrollPane.animate({scrollTop: (top-150)}, 2000, 'swing');
        	var jQObject = $('#interviewnode-'+element.$modelValue.idNode);
        	var top = jQObject.offset().top;
        	alert(top);
        	var currentScroll = scrollPane.scrollTop();
        	alert(currentScroll);
        	scrollPane.animate({scrollTop: (top-150+currentScroll)}, 2000, 'swing');
        	
        }
        $scope.getWeeksArray = function(){
        	var weeks = [];
        	for(var i=0;i<53;i++){
        		weeks.push(i);
        	}
        	return weeks;
        };
        $scope.getShiftHoursArray = function(){
        	var hours = [];
        	for(var i=0;i<25;i++){
        		hours.push(i);
        	}
        	return hours;
        };
        $scope.getShiftMinutesArray= function(){
        	var minutes = [0,1,2,5,10,15,20,25,30,35,40,45,50,55];
        	
        	return minutes;
        };
        $scope.questionheader = {};
        function validateActiveInterview(question){
        	var interviewModuleId = question.topNodeId;
        	 QuestionsService.findQuestions(question.topNodeId, 'M')
             .then(function (response) {
                 console.log("Data getting from questions AJAX ...");
                 if(response.data[0]){
                 $scope.questionheader.name = response.data[0].name;
                 }
             });
        }

        function showNextQuestion(actualQuestion,isAnswer,statusRequired,count){
        	var defer;
        	if(statusRequired){
        		defer = $q.defer();
        	}
        	var actualQuestionTemp = actualQuestion;
        	var tempCount = count;
            return InterviewsService.getNextQuestion(actualQuestion).then(function (response) {
                    if (response.status === 200) {
                       var question = response.data;
                       question.count = tempCount;
                       resetSelectedIndex();
                       if(question.link == 0){
                    	   $scope.inProgress = false;
                    	   safeDigest($scope.inProgress);
                    	   if($scope.updateEnable){
                    		   if(determineNextUnansweredQuestion(question)){
                    			   return;
                    		   }
                    	   }
                    	   else{
                    		   $scope.data.showedQuestion = question;
                    	   }
                    	   safeDigest($scope.data.showedQuestion);
                       }
                       if(isAnswer){
                    	   $scope.parentQId = actualQuestionTemp.questionId;
                       }else{
                    	   $scope.parentQId = undefined;
                       }
                       if(question.link !== 0){
                    	   return processLinkingQuestion(question,actualQuestionTemp);
                       }
                       if(question.type=='Q_frequency'){
                          	$scope.hoursArray = $scope.getShiftHoursArray();
                          	$scope.minutesArray = $scope.getShiftMinutesArray();
                           	$scope.weeks = $scope.getWeeksArray();
                       }
                       if(statusRequired){
                      	   defer.resolve(response.status);
                      	   return defer.promise;
              	   	   }
                    } else if (response.status == 204) {
                    	 if(statusRequired){
                          	   defer.resolve(response.status);
                          	   return defer.promise;
                  	   	 }
                    	 var mod = _.find($scope.activeInterview.modules,function(val,ind){
                         	return val.idNode == actualQuestionTemp.topNodeId && val.count == tempCount;
                         });
                    	 if(!mod){
                    		 mod = _.find($scope.activeInterview.modules,function(val,ind){
                              	return val.idNode == actualQuestionTemp.topNodeId;
                              });
                    	 }
                    	var results =_.find(mod.questionsAsked,function(val,index){
                  		   return val.questionId == actualQuestionTemp.questionId;
                  	    });
                    	
                    	var multiAnswer = results?_.find(results.answers,function(val,ind){
                    		return val.isProcessed == false;
                    	}):undefined;
                    	
                    	if(multiAnswer){
                    		multiAnswer.isProcessed = true;
              				var answer = multiAnswer;
              				
              				var actualQuestion =
                          	{
              						topNodeId:results.topNodeId,
                					questionId:results.questionId,
                        	        parentId:answer.answerId,
                        	        number:answer.number
                          	}
              				showNextQuestion(actualQuestion,true,false,mod.count);
                    	}
                    	else if(results){
                    		var actualQuestion =
                      		{
                    		   topNodeId:results.topNodeId,
              				   questionId:results.parentId,	  
               				   parentId:results.link !=0?results.link:results.parentId,
               				   number:results.number,
               				   link: results.link
                      		}
              			   showNextQuestion(actualQuestion,false,false,mod.count);
                    	}
                    	else if(mod.parentNode){
                    	   verifyQuestionInParentModule(mod);
                    	}
                    	else {
                    		AssessmentsService.updateFiredRules($scope.interviewId).then(function (response) {
                                if (response.status === 200) {
                                	console.log('Updated Fired Rules');
                                }
                    		});
                    	   endInterview();
                    	}
                    } else {
                       console.log('ERROR on Get!');
                    }
                       angular.element('#numId').focus();
                    });
              }
        
        function hasQuestionBeenAsked(node){
        	var mod = _.find($scope.activeInterview.modules,function(val,ind){
            	return val.idNode == node.topNodeId && node.count == val.count;
            });
        	if(!mod){
        		mod = _.find($scope.activeInterview.modules,function(val,ind){
                	return val.idNode == node.topNodeId;
                });
        	}
       		var results =_.find(mod.questionsAsked,function(val,index){
     		   return val.questionId == node.idNode;
     	    });
       		return results;
        }
        
        function processLinkingQuestion(question,actualQuestionTemp){
        	 var modDetail = {
                  	name:question.name,
                    	idNode:question.link,
                    	topNode:question.topNodeId,
                    	parentNode:actualQuestionTemp.link?actualQuestionTemp.link:actualQuestionTemp.questionId,
                    	answerNode:actualQuestionTemp.parentId,
                    	parentAnswerId:question.parentId,
                    	link:actualQuestionTemp.link,
                    	number:question.number,
                    	count:1,
                    	deleted:0,
                    	questionsAsked:[]
                    };
        	 		 var mdIndex = _.indexOf($scope.activeInterview.modules, 
        	     			  _.find($scope.activeInterview.modules,function(val){
        	            		   	  return (val.answerNode == question.parentId && val.idNode == question.link);
        	            		   })
        	            		  );
        	                   if(mdIndex == -1){
        	                	   var modules = _.filter($scope.activeInterview.modules,function(val){
        	                		   return val.idNode == modDetail.idNode;
        	                	   });
        	                	   if(modules){
        	                		   modDetail.count = modDetail.count + modules.length;
        	                	   }
        	                	   $scope.activeInterview.modules.push(modDetail);
        	                   }
     	   var num = 0;
     	   _.find($scope.activeInterview.modules,function(val){
     		   if(val.idNode == question.link && val.parentAnswerId == question.parentId){
     			   if(val.questionsAsked.length > 0){
     				   num = val.questionsAsked.slice(-1)[0].number;
     			   }
     		   }
     	   });
     	   
     	   var actualQuestion =
     	   {
     		   topNodeId:question.topNodeId,
				   questionId:question.idNode,
     		   parentId:question.link,
     		   link:actualQuestionTemp.link,
     		   number:num
    			}
     	   showNextQuestion(actualQuestion,false,false,modDetail.count);
        }
        
        function verifyQuestionInParentModule(mod){
        	var tempMod = angular.copy(mod);
     	   var mod = _.find($scope.activeInterview.modules,function(val,ind){
               return (val.idNode === tempMod.topNode || val.topNode == tempMod.topNode) 
               && val.count == tempMod.count;
            });
     	   var question =_.find(mod.questionsAsked,function(val,index){
     		   return val.questionId === tempMod.parentNode;
     	   });
     	  
     	   var results;
     	   if(question){
     		  results = _.find(question.answers,function(val,index){
        		   return val.answerId === tempMod.answerNode;
        	  });
     	   }
     	  if(!question){
     		 var actualQuestion =
       			{
     		       topNodeId:mod.topNode,
				   questionId:mod.answerNode,	  
				   parentId:mod.idNode,
				   number:tempMod.number
       			}
			   var status = showNextQuestion(actualQuestion,null,true,mod.count);
     		 if(status){
     		   status.then(function(data){
     		   if(data == 204){
     			  if(mod.idNode == $scope.activeInterview.modules[0].idNode){
     				  return endInterview();
     			  } 
     			   
     			  var modParent = _.find($scope.activeInterview.modules,function(val,ind){
     	               return val.idNode === mod.topNode;
     	          }); 
     			  var modQuestion = modParent.questionsAsked.slice(-1)[0];
     			  var actualQuestion =
         		   {
       		       topNodeId:modQuestion.topNodeId,
  				   questionId:modQuestion.questionId,	  
  				   parentId:modQuestion.link > 0?modQuestion.link:modQuestion.parentId,
  				   number:modQuestion.number
         		   }
     			  return showNextQuestion(actualQuestion,null,false,mod.count);
     		   }
     		  });
     		 }
     		 return;
     	  }
     	   if(results){
        		var actualQuestion =
          		{
        		   topNodeId:results.topQuestionId,
  				   questionId:results.parentQuestionId,	  
   				   parentId:results.answerId,
   				   number:tempMod.number
          		}
  			   return showNextQuestion(actualQuestion,true,false,mod.count);
     	   }else{
     		 var actualQuestion =
       		 {
  				   topNodeId:question.topNodeId,
    				   questionId:question.questionId,	  
     				   parentId:question.parentId,
     				   number:question.number
       		 }
  		     return showNextQuestion(actualQuestion,false,false,mod.count);
     	   }
        }
        
        function endInterview(){
        	$scope.inProgress = false;
       	   	safeDigest($scope.inProgress);
        	$scope.data.interviewStarted = false;
    		$scope.data.interviewEnded = true;
    		$scope.updateEnable = false;
        }
         }
})();