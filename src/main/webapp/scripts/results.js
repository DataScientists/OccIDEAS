(function () {
    var app = angular
        .module("surveyResultsApp", ["ngMaterial"], function ($mdThemingProvider) {
            $mdThemingProvider.theme('docs-dark', 'default')
                .primaryPalette('yellow')
                .dark();
        });
    app.config(['$locationProvider', function ($locationProvider) {
        $locationProvider.html5Mode({
            enabled: true,
            requireBase: false
        })
    }]);
    app.controller('resultCtrl', ['$scope', '$http', '$location', '$interval', ($scope, $http, $location, $interval) => {
        $scope.results = null;
        $scope.progress = true;

        $scope.interval;
        const getResults = () => {
            const params = $location.search();
            const sid = params.SID;
            const rid = params.RID;
            $http({
                url: "/view-results",
                method: "GET",
                params: {SID: sid, RID: rid}
            }).then(response => {
                $scope.results = response.data
                if ($scope.results) {
                    $interval.cancel($scope.interval);
                    $scope.progress = false;
                }
            });
        }

        $scope.interval = $interval(
            getResults, 5000, 10);
    }]);
})();