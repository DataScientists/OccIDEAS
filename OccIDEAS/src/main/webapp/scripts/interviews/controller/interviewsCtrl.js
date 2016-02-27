(function () {
    angular.module('occIDEASApp.Interviews')
        .controller('InterviewsCtrl', InterviewsCtrl);

    InterviewsCtrl.$inject = ['data', '$scope', '$mdDialog', 'FragmentsService',
        '$q', 'QuestionsService', 'ModulesService', 'InterviewsService',
        '$anchorScroll', '$location', '$mdMedia', '$window', '$state','$rootScope'];
    function InterviewsCtrl(data, $scope, $mdDialog, FragmentsService,
                            $q, QuestionsService, ModulesService, InterviewsService,
                            $anchorScroll, $location, $mdMedia, $window, $state, $rootScope) {
        var self = this;
        $scope.data = data;

        $scope.saveAnswerQuestion = function (node) {       	
            var seletectedEl = node.selectedAnswer;
            if (node.type == 'Q_multiple') {
            	seletectedEl = $scope.multiSelected;
            }
            if (!seletectedEl && $scope.data.interviewStarted) {
                alert("Please select an answer!");
                return false;
            }
            var interview;
            for(var i=0;i<$scope.interviews.length;i++){ 
            	if($scope.interviews[i].active){
            		interview = $scope.interviews[i];
            	}
            }
            if(!interview.questionsAsked){
        		interview.questionsAsked = [];
        	}
            if (node.type == 'Q_multiple') {
                var answers = seletectedEl;
                var answerIds = [];
                for (var i = 0; i < answers.length; i++) {
                	var newQuestionAsked = {possibleAnswer:answers[i],
                			question:node,
    						interviewQuestionAnswerFreetext:answers[i].name}
                	
                	interview.questionsAsked.push(newQuestionAsked);
                }
            } else {
            	var newQuestionAsked = {possibleAnswer:seletectedEl,
            			question:node,
						interviewQuestionAnswerFreetext:seletectedEl.name}
            	interview.questionsAsked.push(newQuestionAsked);               
            }
            InterviewsService.save(interview).then(function (response) {
            	if (response.status === 200) {
                	InterviewsService.getNextQuestion($scope.interviews).then(function (response) {
                        if (response.status === 200) {
                        	var question = response.data;
                            $scope.data.showedQuestion = question;
                            resetSelectedIndex();
                            if(question.linkingQuestion){                             	
                            	var linkingQuestion = question.linkingQuestion;
                            	var mtype;
                            	var newInterview = {};
                            	if(linkingQuestion.type=='Q_linkedajsm'){
                            		mtype = 'F_ajsm';
                            		newInterview.fragment = {idNode:linkingQuestion.link,type:mtype};
                            		newInterview.type = 'ajsm';
                            	}else if (linkingQuestion.type=='Q_linkedajsm'){
                            		mtype = 'M_Module';
                            		newInterview.module = {idNode:linkingQuestion.link,type:mtype};
                            		newInterview.type = 'module';
                            	}
                            	newInterview.referenceNumber = $scope.interviews[0].referenceNumber;
                            	newInterview.active = true;
                                var activeInterview;
                                for(var i=0;i<$scope.interviews.length;i++){ 
                                	if($scope.interviews[i].active){
                                		activeInterview = $scope.interviews[i];
                                		activeInterview.active=false;
                                	}
                                }
                                $scope.interviews.push(newInterview);
                                InterviewsService.startInterview(newInterview).then(function (response) {
                                	if (response.status === 200) {
                                		for(var i=0;i<$scope.interviews.length;i++){ 
                                        	if($scope.interviews[i].active){
                                        		$scope.interviews[i].interviewId = response.data.interviewId;
                                        	}
                                        }
                                	}
                                });                                                                 
                                if(!activeInterview.questionsAsked){
                                	activeInterview.questionsAsked = [];
                            	}
                            	var newQuestionAsked = {question:question.linkingQuestion,
                						interviewQuestionAnswerFreetext:'Q_linked'}
                            	activeInterview.questionsAsked.push(newQuestionAsked);
                            	
                            	InterviewsService.save(activeInterview).then(function (response) {
                            		if (response.status === 200) {
                            			console.log('Added liking question');
                            		}
                            	});
                            } else if(question.activeInterviewId){
                            	for(var i=0;i<$scope.interviews.length;i++){ 
                                	if($scope.interviews[i].interviewId==question.activeInterviewId){
                                		$scope.interviews[i].active = true;
                                	}else{
                                		$scope.interviews[i].active = false;
                                	}
                                }
                            }                                                            
                            if(response.data.idNode){
                                var elId = "node-" + response.data.idNode;
                                $scope.scrollTo(elId);   
                            }else{
                            	$scope.data.interviewStarted = false;
                            	$scope.data.interviewEnded = true;
                            }                           
                        } else if (response.status == 204) {
                            //alert("End interview!");
                            $scope.data.interviewStarted = false;
                            $scope.data.interviewEnded = true;
                            return false;
                        } else {
                            console.log('ERROR on Get!');
                        }
                    });
            	}          	
            });                  
        }
        $scope.startInterview = function (data) {
        	QuestionsService.findQuestions($scope.data[0].idNode,'M')
            .then(function(response){
                console.log("Data getting from questions AJAX ...");
                $scope.data = response.data;
                var interview = {};
                interview.module = $scope.data[0];
                interview.referenceNumber = "H"+Math.floor((Math.random() * 100) + 1);
                interview.type = 'intromodule';
                $scope.interviews = [];
                $scope.interviews.push(interview);
                
                InterviewsService.startInterview(interview).then(function (response) {
                	if (response.status === 200) {
                		$scope.interviewId = response.data.interviewId;
                		$scope.interviews[0].interviewId = response.data.interviewId;
                		$scope.interviews[0].active = true;
                		$scope.data.interviewStarted = true;
                		$scope.data.interviewEnded = false;
                		InterviewsService.getNextQuestion($scope.interviews).then(function (response){
                			$scope.data.showedQuestion = response.data;

                			var elId = "node-" + response.data.idNode;
                			$scope.scrollTo(elId);
                			angular.element(document.querySelector("#tree-root-interviewing #" + elId)).addClass('highlight');
                        })                          
                     } else {
                            console.log('ERROR on Start Interview!');
                     }
                    
                });             
            });
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
            var scrollPane = $('#interivew-module-tree');
			var scrollTarget = $('#' + target);
			if(scrollTarget){
				if(scrollTarget.offset()){
					var scrollY = scrollTarget.offset().top - 150;
					scrollPane.animate({scrollTop : scrollY }, 2000, 'swing');
					angular.element(document.querySelector("#tree-root-interviewing #" + target)).addClass('highlight-interview');
					angular.element(document.querySelector("#tree-root-interviewing #" + target)).addClass('highlight');	
				}	
			}
		};
        $scope.multiSelected = [];
        $scope.multiToggle = function (item, list) {
          var idx = list.indexOf(item);
          if (idx > -1) list.splice(idx, 1);
          else list.push(item);
        };
        $scope.multiExists = function (item, list) {
          return list.indexOf(item) > -1;
        };
    }
})();