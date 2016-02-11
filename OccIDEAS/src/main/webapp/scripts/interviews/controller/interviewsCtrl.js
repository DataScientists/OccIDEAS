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
            var seletectedEl = angular.element(document.getElementsByClassName("selected"));
            if (!seletectedEl.length && $scope.interviewStarted) {
                alert("Please select an answer!");
                return false;
            }

            var idNode;
            if (!$scope.interviewStarted
            // && node.type != "Q_simple" && node.type != 'Q_single'
            ) {
                idNode = node.idNode;
            } else {
                idNode = seletectedEl[0].id;
            }
            InterviewsService.getNextQuestion(idNode).then(function (response) {
                console.log(response);
                if (response.status === 200) {
                    $scope.data.showedQuestion = response.data;
                    resetSelectedIndex();
                    $scope.interviewStarted = true;

                    var elId = "node-" + response.data.idNode;
                    $scope.scrollTo(elId);

                    angular.element(document.getElementById(elId)).addClass('highlight');
                } else if (response.status == 400) {
                    alert("End interview!");
                    return false;
                } else {
                    console.log('ERROR on Get!');
                }
            });
            ;
        }
        resetSelectedIndex();
        $scope.interviewStarted = false;


        $scope.singleChoiceHandler = function ($index) {
            $scope.selectedIndex = $index;
        }

        $scope.multipleChoiceHandler = function (id) {
            angular.element('#' + id).addClass('selected');
        }

        function resetSelectedIndex() {
            $scope.selectedIndex = -1;
        }

        $scope.scrollTo = function (target) {
            var scrollTarget = $('#' + target);
            scrollTarget.get(0).scrollIntoView();
        };
    }
})();