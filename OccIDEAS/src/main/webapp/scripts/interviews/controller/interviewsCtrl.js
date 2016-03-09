(function () {
    angular.module('occIDEASApp.Interviews')
        .controller('InterviewsCtrl', InterviewsCtrl);

    InterviewsCtrl.$inject = ['data', '$scope', '$mdDialog', 'FragmentsService',
        '$q', 'QuestionsService', 'ModulesService', 'InterviewsService',
        '$anchorScroll', '$location', '$mdMedia', '$window', '$state', '$rootScope','$compile'];
    function InterviewsCtrl(data, $scope, $mdDialog, FragmentsService,
                            $q, QuestionsService, ModulesService, InterviewsService,
                            $anchorScroll, $location, $mdMedia, $window, $state, $rootScope,$compile) {
        var self = this;
        $scope.data = data;
        $scope.showIntroModule = true;
        $scope.showModule = false;
        $scope.showAjsm = false;
        $scope.refNoPattern = "H([a-zA-Z0-9]){3}(-)([a-zA-Z0-9]){3}";
        $scope.multiSelected = [];
        
        self.showRulesMenu = function(scope){
			return self.rulesMenuOptions;
		}
        
        self.rulesMenuOptions =
			[
			  [ 'Show Rules', function($itemScope, $event, model) {
				  	var ruleArray =_.filter(model.module.moduleRule, function(r){
  						return $itemScope.agent.idAgent === r.idAgent; 
				  	});
				  	 var uniqueArray = _.map(_.groupBy(ruleArray,function(item){
       				  return item.rule.idRule;
       				}),function(grouped){
       				  return grouped[0];
       				});
				  	var scope = $itemScope.$new();
			  		scope.model = model;
			  		scope.rule = uniqueArray[0];
			  		scope.agentName = $itemScope.agent.name;
			  		newInterviewNote($event.currentTarget.parentElement,scope,$compile);
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
        	$scope.activeIntRuleCell = model.idAgent;
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
        
        $scope.saveAnswerQuestion = function (node) {
            var seletectedEl = node.selectedAnswer;
            if (node.type == 'Q_multiple') {
                seletectedEl = $scope.multiSelected;
                $scope.multiSelected = [];
            }
            if (!seletectedEl && $scope.data.interviewStarted) {
                alert("Please select an answer!");
                return false;
            }
            var interview;
            for (var i = 0; i < $scope.interviews.length; i++) {
                if ($scope.interviews[i].active) {
                    interview = $scope.interviews[i];
                }
            }
            if (!interview.questionsAsked) {
                interview.questionsAsked = [];
            }
            if (node.type == 'Q_multiple') {
                var answers = seletectedEl;
                var answerIds = [];
                for (var i = 0; i < answers.length; i++) {
                    var newQuestionAsked = {
                        possibleAnswer: answers[i],
                        idInterview: interview.interviewId,
                        question: node,
                        interviewQuestionAnswerFreetext: answers[i].name
                    }

                    interview.questionsAsked.push(newQuestionAsked);
                }
            } else {
                var newQuestionAsked = {
                    possibleAnswer: seletectedEl,
                    idInterview: interview.interviewId,
                    question: node,
                    interviewQuestionAnswerFreetext: seletectedEl.name
                }
                interview.questionsAsked.push(newQuestionAsked);
            }
            InterviewsService.save(interview).then(function (response) {
                if (response.status === 200) {
                    var interviewId = 0;
                    for (var i = 0; i < $scope.interviews.length; i++) {
                        if ($scope.interviews[i].active) {
                            interviewId = $scope.interviews[i].interviewId;

                        }
                    }
                    InterviewsService.get(interviewId).then(function (response) {
                        if (response.status === 200) {
                            for (var i = 0; i < $scope.interviews.length; i++) {
                                if ($scope.interviews[i].active) {
                                    var interview = response.data[0];
                                    interview.active = true;
                                    $scope.interviews[i] = interview;
                                }
                            }
                            InterviewsService.getNextQuestion($scope.interviews).then(function (response) {
                                if (response.status === 200) {
                                    var question = response.data;
                                    $scope.data.showedQuestion = question;
                                    /*if (question.description.indexOf('[] FA_freq') > -1) {
                                        debugger;
                                        var ans = question.nodes[0].name.split("-");
                                        var startP = parseInt(ans[0].split(" ")[1]);
                                        var endP = parseInt(ans[1].replace("]", ""));
                                        var freqAns = [];
                                        for (var i = startP; i <= endP; i++) {
                                            freqAns.push(i);
                                        }

                                        $scope.data.showedQuestion.freqAns = freqAns;
                                    }*/
                                    resetSelectedIndex();
                                    if (question.linkingQuestion) {
                                        var linkingQuestion = question.linkingQuestion;
                                        var newInterview = {};
                                        if (linkingQuestion.type == 'Q_linkedajsm') {
                                            newInterview.fragment = {
                                                idNode: linkingQuestion.link,
                                                name: linkingQuestion.name
                                            };
                                            QuestionsService.findQuestions(linkingQuestion.link, 'F')
                                                .then(function (response) {
                                                    console.log("Data getting from questions AJAX ...");
                                                    $scope.linkedAjsm = response.data;
                                                    $scope.showIntroModule = false;
                                                    $scope.showModule = false;
                                                    $scope.showAjsm = true;

                                                });
                                        } else if (linkingQuestion.type == 'Q_linkedmodule') {
                                            newInterview.module = {idNode: linkingQuestion.link,
                                                    name: linkingQuestion.name};
                                            QuestionsService.findQuestions(linkingQuestion.link, 'M')
                                                .then(function (response) {
                                                    console.log("Data getting from questions AJAX ...");
                                                    $scope.linkedModule = response.data;
                                                    $scope.showIntroModule = false;
                                                    $scope.showModule = true;
                                                    $scope.showAjsm = false;
                                                });
                                        }
                                        newInterview.referenceNumber = $scope.interviews[0].referenceNumber;
                                        newInterview.active = true;
                                        var activeInterview;
                                        for (var i = 0; i < $scope.interviews.length; i++) {
                                            if ($scope.interviews[i].active) {
                                                activeInterview = $scope.interviews[i];
                                                activeInterview.active = false;
                                            }
                                        }
                                        $scope.interviews.push(newInterview);
                                        InterviewsService.startInterview(newInterview).then(function (response) {
                                            if (response.status === 200) {
                                                for (var i = 0; i < $scope.interviews.length; i++) {
                                                    if ($scope.interviews[i].active) {
                                                        $scope.interviews[i].interviewId = response.data.interviewId;
                                                    }
                                                }
                                                var elId = "interviewnode-" +  $scope.data.showedQuestion.idNode;
                                                $scope.scrollTo(elId);
                                            }
                                        });
                                        if (!activeInterview.questionsAsked) {
                                            activeInterview.questionsAsked = [];
                                        }
                                        var newQuestionAsked = {
                                            question: question.linkingQuestion,
                                            idInterview: activeInterview.interviewId,
                                            interviewQuestionAnswerFreetext: 'Q_linked'
                                        }
                                        activeInterview.questionsAsked.push(newQuestionAsked);

                                        InterviewsService.save(activeInterview).then(function (response) {
                                            if (response.status === 200) {
                                                console.log('Added liking question');
                                            }
                                        });
                                    } else if (question.activeInterviewId) {
                                        for (var i = 0; i < $scope.interviews.length; i++) {
                                            if ($scope.interviews[i].interviewId == question.activeInterviewId) {
                                                $scope.interviews[i].active = true;
                                                if ($scope.interviews[i].module) {
                                                    if ($scope.interviews[i].module.type == 'M_IntroModule') {
                                                        $scope.showIntroModule = true;
                                                        $scope.showModule = false;
                                                        $scope.showAjsm = false;
                                                    } else {
                                                        $scope.showIntroModule = false;
                                                        $scope.showModule = true;
                                                        $scope.showAjsm = false;
                                                    }
                                                }
                                            } else {
                                                $scope.interviews[i].active = false;
                                            }
                                        }
                                    }
                                } else if (response.status == 204) {
                                    for (var i = 0; i < $scope.interviews.length; i++) {
                                        InterviewsService.get($scope.interviews[i].interviewId).then(function (response) {
                                            if (response.status === 200) {
                                                for (var i = 0; i < $scope.interviews.length; i++) {
                                                    if ($scope.interviews[i].interviewId == response.data[0].interviewId) {
                                                        $scope.interviews[i] = response.data[0];
                                                    }
                                                }
                                            }
                                        });
                                    }
                                    $scope.data.interviewStarted = false;
                                    $scope.data.interviewEnded = true;

                                } else {
                                    console.log('ERROR on Get!');
                                }
                                angular.element('#numId').focus();
                                var elId = "interviewnode-" +  $scope.data.showedQuestion.idNode;
                                $scope.scrollTo(elId);
                            });
                        }
                    });

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
        $scope.startInterview = function (data) {
            $scope.referenceNumber = data.referenceNumber;
            if (!$scope.referenceNumber) {
                $scope.referenceNumber = 'TEST' + Math.floor((Math.random() * 100) + 1);
            }
            QuestionsService.findQuestions($scope.data[0].idNode, 'M')
                .then(function (response) {
                    console.log("Data getting from questions AJAX ...");
                    $scope.data = response.data;
                    var interview = {};
                    interview.module = $scope.data[0];
                    interview.referenceNumber = $scope.referenceNumber;
                    $scope.interviews = [];
                    $scope.interviews.push(interview);

                    InterviewsService.startInterview(interview).then(function (response) {
                        if (response.status === 200) {
                            $scope.interviewId = response.data.interviewId;
                            $scope.interviews[0].interviewId = response.data.interviewId;
                            $scope.interviews[0].active = true;
                            $scope.data.interviewStarted = true;
                            $scope.data.interviewEnded = false;
                            InterviewsService.getNextQuestion($scope.interviews).then(function (response) {
                                $scope.data.showedQuestion = response.data;

                                var elId = "interviewnode-" + response.data.idNode;
                                $scope.scrollTo(elId);
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
                    scrollPane.animate({scrollTop: scrollY}, 2000, 'swing');
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
    }
})();