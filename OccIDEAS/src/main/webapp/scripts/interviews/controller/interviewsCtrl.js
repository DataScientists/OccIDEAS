(function () {
    angular.module('occIDEASApp.Interviews')
        .controller('InterviewsCtrl', InterviewsCtrl);

    InterviewsCtrl.$inject = ['data', '$scope', '$mdDialog', 'FragmentsService',
        '$q', 'QuestionsService', 'ModulesService', 'InterviewsService',
        '$anchorScroll', '$location', '$mdMedia', '$window', '$state'];
    function InterviewsCtrl(data, $scope, $mdDialog, FragmentsService,
                            $q, QuestionsService, ModulesService, InterviewsService,
                            $anchorScroll, $location, $mdMedia, $window, $state) {
        var self = this;
        $scope.data = data;

        $scope.saveAnswerQuestion = function (node) {
            var seletectedEl = angular.element(document.querySelector("#interviewing-" + node.idNode + " .selected"));
            if (!seletectedEl.length && $scope.data.interviewStarted) {
                alert("Please select an answer!");
                return false;
            }

            var interview = {};
            interview.interviewId = $scope.interviewId;
            interview.questionId = node.idNode;
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
                interview.singleAnswerId = seletectedEl[0].id.replace("answer-", "");
                interview.type = "single";
                interview.freeText = seletectedEl[0].value;
            }

            InterviewsService.getNextQuestion(interview).then(function (response) {
                if (response.status === 200) {
                    $scope.data.showedQuestion = response.data;
                    resetSelectedIndex();

                    var elId = "node-" + response.data.idNode;
                    $scope.scrollTo(elId);

                    angular.element(document.querySelector("#tree-root-interviewing #" + elId)).addClass('highlight');
                } else if (response.status == 400) {
                    alert("End interview!");
                    return false;
                } else {
                    console.log('ERROR on Get!');
                }
            });
            ;
        }

        $scope.startInterview = function (data) {
            var interview = {};
            interview.moduleId = data[0].idNode;
            InterviewsService.startInterview(interview).then(function (response) {
                $scope.interviewId = response.data.interviewId;
                $scope.data.interviewStarted = true;

                InterviewsService.getNextQuestion(JSON.stringify({
                    "interviewId": response.data.interviewId,
                    "moduleId": response.data.moduleId
                })).then(function (qResponse) {
                    if (qResponse.status === 200) {
                        $scope.data.showedQuestion = qResponse.data;

                        var elId = "node-" + qResponse.data.idNode;
                        $scope.scrollTo(elId);
                        angular.element(document.querySelector("#tree-root-interviewing #" + elId)).addClass('highlight');
                    } else {
                        console.log('ERROR on Get!');
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

        /*Handler click on answers*/

        $scope.scrollTo = function (target) {
            var container = $('#interivew-module-tree'),
                scrollTo = $('#' + target);
            container.animate({
                scrollTop: scrollTo.offset().top - container.offset().top + container.scrollTop() - 200
            });
        };
    }
})();