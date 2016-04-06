(function () {
    angular.module('occIDEASApp.Interviews')
        .controller('InterviewsCtrl', InterviewsCtrl);

    InterviewsCtrl.$inject = ['data', '$scope', '$mdDialog', 'FragmentsService',
        '$q', 'QuestionsService', 'ModulesService', 'InterviewsService', 'ParticipantsService',
        '$anchorScroll', '$location', '$mdMedia', '$window', '$state', '$rootScope','$compile','$timeout','$log'];
    function InterviewsCtrl(data, $scope, $mdDialog, FragmentsService,
                            $q, QuestionsService, ModulesService, InterviewsService, ParticipantsService,
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
       /* $window.setInterval(function() {
        	  var elem = document.getElementById('interview-question-list');
        	  elem.scrollTop = elem.scrollHeight;
        	}, 5000);*/
        self.editQuestion = function(question){
        	var interview = $scope.activeInterview;
        	if(!interview){
        		return null;
        	}
        	refreshInterview();
        	$scope.inProgress = true;
        	$scope.updateEnable = true;
        	safeDigest($scope.inProgress);
        	var number = (question.number.substr(question.number.length - 1) - 1);
        	var newNum;
        	if(number == 0 && question.number.length > 1){
        		newNum = question.number.substring(0, question.number.length - 1);
        	}
        	
        	var actualQuestion =
        	{
				topNodeId:question.topNodeId,
				questionId:question.questionId,
    	        parentId:question.number.length > 1?question.parentAnswerId:question.parentId,
    	        number:newNum?newNum:question.number.slice(0, -1) +number
        	}
			var status = showNextQuestion(actualQuestion,false,true);
        	if(status){
        		status.then(function(data){
        			if(data == 200){
        				var answerList = [];
        				$scope.updateEnable = false;
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
        				}
        				deleteQuestion([question]);
        			}
        		});
        	}
        	
        };

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
        				var question = mod.questionsAsked[j];
        				if(question.questionId == answer.parentQuestionId){
        					answer.deleted = 1;
        					deleteModuleWithParentAnswer(answer);
        					question.deleted = 1;
            				InterviewsService.saveQuestion(mod.questionsAsked[j]).then(function (response) {
                    			if (response.status === 200) {
                    				console.log("Delete question successful...");
                    				_.remove(mod.questionsAsked,function(val){
                    	        		return val.questionId === question.questionId;
                    	        	});
                    				return deleteQuestionWithParentAnswer(question.answers);
                    			}
                    		});
        				}
        			}
        		}
        }
        
        function deleteModuleWithParentAnswer(answer){
        	_.find($scope.activeInterview.modules,function(mod){
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
        
        function deleteQuestion(questions){
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
        					return mod.idNode == $scope.data.showedQuestion.topNodeId;
        				});
        				_.remove(mod.questionsAsked,function(val){
        	        		return val.questionId === question.questionId;
        	        	});
        			}
        		});
        	})
        }
        
        
        self.showRulesMenu = function(scope){
			return self.rulesMenuOptions;
		}
        
        $scope.nodePopover = {
    		    templateUrl: 'scripts/interviews/partials/nodePopover.html',
    		    open: function(x,idRule) {
    		    	var nodeclass = 'P';
    		    	if(angular.isUndefined(x.info)){
  		    		  x.info = [];
  		    	  	}
    		    	 x.info["Node"+x.idNode+idRule] = {
							    				  idNode:x.idNode,
							    				  nodeclass:nodeclass,
							    				  nodePopover:{
							    					  isOpen: false
							    				  },
							    				  nodePopoverInProgress : false
		    		  							};
    		    	 var nodeInPopup = x.info["Node"+x.idNode+idRule];
    		    	 nodeInPopup.nodePopoverInProgress = true;
    		         var deffered = $q.defer();
    		         QuestionsService.findPossibleAnswer(nodeInPopup.idNode).then(function(data) {	
    		    		nodeInPopup.data = data.data[0];
    		    		nodeInPopup.idRule =idRule;
   						nodeInPopup.nodePopoverInProgress = false;
   						deffered.resolve();
 					 });
    		         deffered.promise.then(function(){
    		        	 nodeInPopup.nodePopover.isOpen = true;
    		    	 })
    		    },   		    
  		        close: function close(x,idRule) {
  		        	x.info["Node"+x.idNode+idRule].nodePopover.isOpen = false;
  		        }
    	};
        
        self.rulesMenuOptions =
			[
			  [ 'Show Rules', function($itemScope, $event, model) {
				  	var ruleArray =_.filter(model.firedRules, function(r){
  						return $itemScope.agent.idAgent === r.agent.idAgent; 
				  	});
				  	 var uniqueArray = _.map(_.groupBy(ruleArray,function(item){
       				  return item.idRule;
       				}),function(grouped){
       				  return grouped[0];
       				});
				  	for(var i=0;i<uniqueArray.length;i++){
					  	var scope = $itemScope.$new();
				  		scope.model = model;
				  		scope.rule = uniqueArray[i];
				  		scope.agentName = $itemScope.agent.name;
				  		newInterviewNote($event.currentTarget.parentElement,scope,$compile);
				  	}
			  	}			  
			  ]
			];
        
        $scope.closeIntDialog = function(elem,$event) {
        	$($event.target).closest('.int-note').remove();
        	$scope.activeIntRuleDialog = '';
        	$scope.activeIntRuleCell = '';
        	safeDigest($scope.activeIntRuleDialog);
        	safeDigest($scope.activeIntRuleCell);
        };
        
        $scope.setActiveIntRule = function(model,el){
        	$scope.activeIntRuleDialog = el.$id;
        	$scope.activeIntRuleCell = model.agentId;
        	safeDigest($scope.activeIntRuleDialog);
        	safeDigest($scope.activeIntRuleCell);
        }
        
        var safeDigest = function (obj){
        	if (!obj.$$phase) {
		        try {
		        	obj.$digest();
		        }
		        catch (e) { }
        	}
        }

        function findActiveInterview(){
        	var interview = _.find($scope.interviews, function(o) { return o.active; });
        	if(!interview){
        		return null;
        	}
        	
            if (!interview.questionsAsked) {
                interview.questionsAsked = [];
            }
            return interview;
        }
        function findActiveInterviewInParticipant(interviewId){
        	ParticipantsService.findInterviewParticipant($scope.participant.idParticipant).then(function (response) {
                if (response.status === 200) {
                	$scope.participant = response.data[0];
                	var interview = cascadeFindInterview($scope.participant.interviews, interviewId);      	     	
                	if(!interview){
                		return null;
                	}
                    if (!interview.questionsAsked) {
                        interview.questionsAsked = [];
                    }
                    return interview;
                }
        	});
        }
        function cascadeFindInterview(interviews, interviewId){
        	//var retValue = null;
			_.each(interviews, function(data) {
				if(data.interviewId == interviewId){
					$scope.activeInterview = data;
				}else{
					if(data.interviews){
						if(data.interviews.length>0){
							 cascadeFindInterview(data.interviews,interviewId);
						}
					}
				}	
						 
			});
			//return retValue;
		}
        function verifyIfUpdate(interview,node){
        	 if($scope.updateAnswers){
            	 _.find(interview.questionsAsked,function(val,ind){
                 	if(val.question.idNode === node.idNode){
                 		var iqa = interview.questionsAsked[ind];
                  		iqa.deleted = 1;
                 		deleteQuestions(interview,iqa);
                 		safeDigest(interview.questionsAsked);
                 	}
                 });
            }
        }
        
        function processQuestionsWithMultipleAnswers(interview,node){
        	var selectedEl = $scope.multiSelected;
            $scope.multiSelected = [];
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
              	  answers: []
             };
            
            _.each(selectedEl,function(value,i){
            	var newQuestionAsked = {
                        possibleAnswer: value,
                        idInterview: interview.interviewId,
                        question: node,
                        deleted:0,
                        interviewQuestionAnswerFreetext: value.name
                }
            	interview.questionsAsked.push(newQuestionAsked);
            });
            $scope.multiAnswers = false;
            showNextQuestion();
        }
        
        function populateNewQuestionAskedJsonByNode(interview,node){
        	return {
              	  idInterview:$scope.interviewId,
              	  topNodeId:node.topNodeId,
              	  questionId:node.idNode,
              	  parentId:$scope.parentQId?$scope.parentQId:node.parentId,
              	  parentAnswerId:$scope.parentQId?node.parentId:null,
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
              	return val.idNode === node.topNodeId;
            });
        	
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
      				showNextQuestion(actualQuestion,true);
      			}
      		});
        }
        
        function processFrequency(interview,node){
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
            		  type:answer.type,
            		  answerFreetext:answerValue,
            		  link: answer.link,
            		  deleted:0
            	  }]
            }
            var mod = _.find(interview.modules,function(val,ind){
            	return val.idNode === node.topNodeId;
            });
            mod.questionsAsked.push(newQuestionAsked);
            InterviewsService.saveQuestion(newQuestionAsked).then(function (response) {
    			if (response.status === 200) {
    				var actualQuestion =
                	{
    					topNodeId:node.topNodeId,
    					questionId:node.idNode,
            	        parentId:answer.idNode,
            	        number:answer.number,
            	        link: node.link,
                	}
    				showNextQuestion(actualQuestion,true);
    			}
    		});
        }
        
        function processQuestion(interview,node){
        	var answer = node.selectedAnswer;
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
            		  link: answer.link,
            		  type:answer.type,
            		  deleted:0
            	  }]
            }
            var mod = _.find(interview.modules,function(val,ind){
            	return val.idNode === node.topNodeId;
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
    				var actualQuestion =
                	{
    					topNodeId:node.topNodeId,
    					questionId:node.idNode,
            	        parentId:answer.idNode,
            	        number:answer.number,
            	        link: node.link
                	}
    				showNextQuestion(actualQuestion,true);
    			}
    		});
        }
        
        $scope.saveAnswerQuestion = function (node) {
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
        
        function mergeInterviewWithQaView(){
        	for (var i = 0; i < $scope.interviews.length; i++) {
            	_.find($scope.qaView,function(val,ind){
                 	if(val.interviewId === $scope.interviews[i].interviewId){
                 			$scope.interviews[i].questionsAsked = angular.copy(val.questionsAsked);
                 			safeDigest($scope.interviews);
                 	}
                });
            }
        }
        
        function checkUpdateAnswersFlag(){
        	if($scope.updateAnswers){
                $scope.updateAnswers = false;
                safeDigest($scope.updateAnswers);
            }
        }
        
        function getChildInterviews(){
        	var interview = findActiveInterviewInParticipant();
        	if(interview != null){
        		InterviewsService.get(interview.interviewId).then(function (response) {
        			if (response.status === 200) {
        				interview = response.data[0];
        				interview.active = true;
        				showNextQuestion();
        			}
        		});
        	}
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
        function updateQaView(interview,newQuestionAsked){
           _.find($scope.qaView,function(val,ind){
              	if(val.interviewId === interview.interviewId){
              		if(!val.questionsAsked){
              			val.questionsAsked = [];
              		}
              		val.questionsAsked.push(newQuestionAsked);
              		safeDigest($scope.qaView);
              	}
           });
        }
        function deleteQaView(interview,idNode){
            _.find($scope.qaView,function(val,ind){
               	if(val.interviewId === interview.interviewId){
               		_.find(val.questionsAsked,function(qa,counter){
               			if(qa.question.idNode === idNode){
               				qa.deleted = 1;
               				deleteQuestions(val,qa);
               			}
               		})
               	}
            });
         }
        function deleteQuestions(interview,parentiqa){
        	var questionsAsked = interview.questionsAsked;
        	_.find(questionsAsked,function(val,ind){
        		if(!(val.deleted)){
        			if(val.question.parentId == parentiqa.possibleAnswer.idNode){
        				var iqa = questionsAsked[ind];
                  		iqa.deleted = 1;
                  		deleteQuestions(questionsAsked,iqa);
                  		safeDigest(interview.questionsAsked);
                  	}
              	}
              	
              });
        }
        $scope.showRule = function (scope) {
            //Todo implement show dialog on
            /*var x = scope.rule.conditions;
             x.idRule = scope.rule.idRule;
             addPopoverInfo(x,scope.rule.idRule);
             newNote($event.currentTarget.parentElement,scope,$compile);*/
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
                                        showNextQuestion(actualQuestion);
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
        function showLinkingQuestion(newInterview,question){
        	newInterview.referenceNumber = $scope.interviews[0].referenceNumber;
            newInterview.active = true;
            newInterview.participant = $scope.participant;
            var runningInterview;
            for (var i = 0; i < $scope.interviews.length; i++) {
            	runningInterview = $scope.interviews[i];
                if ($scope.interviews[i].active) {
                	runningInterview.active = false;
                }
                if (!runningInterview.questionsAsked) {
                	runningInterview.questionsAsked = [];
                }
                var linkedQuestionAsked = {
                    question: question.linkingQuestion,
                    idInterview: runningInterview.interviewId,
                    interviewQuestionAnswerFreetext: 'Q_linked'
                }
                var isExistInQa = _.find(runningInterview.questionsAsked,function(val,ind){
                 	var retValue = false;
                 	if(val.idInterview == runningInterview.interviewId){
                 		if(val.question.idNode == linkedQuestionAsked.question.idNode){
                 			if(val.interviewQuestionAnswerFreetext == 'Q_linked'){
                 				retValue = true;
                         	}
                     	}
                 	}
                	return retValue;
                 });
                if(!isExistInQa){
                	runningInterview.questionsAsked.push(linkedQuestionAsked);
                }
                var deffered = $q.defer();
                getChildInterviews();
				deffered.resolve();
				deffered.promise.then(function(){
					$scope.interviews.push(newInterview);
                    InterviewsService.startInterview(newInterview).then(function (response) {
                        if (response.status === 200) {
                            for (var i = 0; i < $scope.interviews.length; i++) {
                                if ($scope.interviews[i].active) {
                                    $scope.interviews[i].interviewId = response.data.interviewId;
                                }
                            }
                        }
                    });
		    	 })
            }
            
        }
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

        function showNextQuestion(actualQuestion,isAnswer,statusRequired){
        	var defer;
        	if(statusRequired){
        		defer = $q.defer();
        	}
        	var actualQuestionTemp = actualQuestion;
            return InterviewsService.getNextQuestion(actualQuestion).then(function (response) {
                    if (response.status === 200) {
                       var question = response.data;
                       resetSelectedIndex();
                       if(question.link == 0){
                    	   $scope.inProgress = false;
                    	   safeDigest($scope.inProgress);
                    	   $scope.data.showedQuestion = question;
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
                         	return val.idNode == actualQuestionTemp.topNodeId;
                         });
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
              				showNextQuestion(actualQuestion,true);
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
                    		
              			   showNextQuestion(actualQuestion);
                    	}
                    	else if(mod.parentNode){
                    	   verifyQuestionInParentModule(mod);
                    	}
                    	else {
                    	   $scope.inProgress = false;
                     	   safeDigest($scope.inProgress);
                    	   endInterview();
                    	}
                    } else {
                       console.log('ERROR on Get!');
                    }
                       angular.element('#numId').focus();
                    });
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
                       	deleted:0,
                       	questionsAsked:[]
                       };
        	  		 var mdIndex = _.indexOf($scope.activeInterview.modules, 
        			  _.find($scope.activeInterview.modules,function(val){
               		   	  return val.idNode == question.link ||  val.idNode == actualQuestionTemp.questionId;
               		   })
               		  );
                      if(mdIndex == -1){
                    	  $scope.activeInterview.modules.push(modDetail);
                      }else{
                    	  $scope.activeInterview.modules.splice(mdIndex, 1, modDetail);
                      } 
        	   
        	   var num = 0;
        	   _.find($scope.activeInterview.modules,function(val){
        		   if(val.idNode == question.link){
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
        	   showNextQuestion(actualQuestion);
        }
        
        function verifyQuestionInParentModule(mod){
        	var tempMod = angular.copy(mod);
     	   var mod = _.find($scope.activeInterview.modules,function(val,ind){
               return val.idNode === tempMod.topNode;
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
			   var status = showNextQuestion(actualQuestion,null,true);
     		 if(status){
     		   status.then(function(data){
     		   if(data == 204){
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
     			  return showNextQuestion(actualQuestion,null,false);
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
  			   return showNextQuestion(actualQuestion,true);
     	   }else{
     		 var actualQuestion =
       		 {
  				   topNodeId:question.topNodeId,
    				   questionId:question.questionId,	  
     				   parentId:question.parentId,
     				   number:question.number
       		 }
  		     return showNextQuestion(actualQuestion);
     	   }
        }
        
        function endInterview(){
        	$scope.data.interviewStarted = false;
    		$scope.data.interviewEnded = true;
        }
         }
})();