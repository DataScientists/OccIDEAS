(function () {
    var app = angular
        .module("surveyIntroApp", ["ngMaterial"], function ($mdThemingProvider) {
            $mdThemingProvider.theme('docs-dark', 'default')
                .primaryPalette('yellow')
                .dark();
        });
    app.controller('resultCtrl', ['$scope', '$http', ($scope, $http) => {
        $scope.error = null;
        $scope.results = null;
        const getResults = () => {
            $http({
                url: "./survey-link",
                method: "GET",
            }).then(response => {
                $scope.results = response.data;
            }, response => {
                console.error(response);
                $scope.error = 'Something went wrong';
            });
        }
        getResults();
    }]);
})();