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
            if (!seletectedEl && $scope.data.interviewStarted) {
                alert("Please select an answer!");
                return false;
            }

            var interview = $scope.interview;
            //interview.questionId = node.idNode;
            if (node.type == 'Q_multiple') {
                var answers = angular.element(document.querySelectorAll("#interviewing-" + node.idNode + " .selected"));
                var answerIds = [];
                for (var i = 0; i < answers.length; i++) {
                    answerIds.push(parseInt(answers[i].id.replace("answer-", "")));
                    if ($("#interviewing-" + node.idNode + " #" + answers[i].id).is(":input")) {
                        interview.freeText = answers[i].value;
                    }
                }
                interview.multipleAnswerId = answerIds;
                interview.type = "multiple";
            } else {
            	var newQuestionAsked = {possibleAnswer:seletectedEl,
            			question:node,
						interviewQuestionAnswerFreetext:seletectedEl.name}
            	if(!interview.questionsAsked){
            		interview.questionsAsked = [];
            	}
            	interview.questionsAsked.push(newQuestionAsked);
                
            }
            InterviewsService.save(interview).then(function (response) {
            	if (response.status === 200) {
            		InterviewsService.getNextQuestion(interview).then(function (response) {
                        if (response.status === 200) {
                            $scope.data.showedQuestion = response.data;
                            resetSelectedIndex();

                            if(response.data.idNode){
                                var elId = "node-" + response.data.idNode;
                                $scope.scrollTo(elId);   
                            }else{
                            	$scope.data.interviewStarted = false;
                            	$scope.data.interviewEnded = true;
                            }
                            
                        } else if (response.status == 400) {
                            alert("End interview!");
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
                $scope.interview = interview;
                InterviewsService.startInterview(interview).then(function (response) {
                	if (response.status === 200) {
                		$scope.interviewId = response.data.interviewId;
                		$scope.interview.interviewId = response.data.interviewId;
                		$scope.data.interviewStarted = true;
                		$scope.data.interviewEnded = false;
                		InterviewsService.getNextQuestion($scope.interview).then(function (response){
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
        /*Handler click on answers*/
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
			var scrollY = scrollTarget.offset().top - 150;
			scrollPane.animate({scrollTop : scrollY }, 2000, 'swing');
			angular.element(document.querySelector("#tree-root-interviewing #" + target)).addClass('highlight-interview');
			angular.element(document.querySelector("#tree-root-interviewing #" + target)).addClass('highlight');
        };
    }
})();