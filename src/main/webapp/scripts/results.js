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
    app.controller('resultCtrl', ['$scope', '$http', '$location', '$timeout', ($scope, $http, $location, $timeout) => {
        $scope.results = null;
        $scope.progress = true;
        $scope.error = null;
        const getResults = () => {
            const params = $location.search();
            const rid = params.RID;
            $http({
                url: "./view-results",
                method: "GET",
                params: {RID: rid}
            }).then(response => {
                const derivedResponseData = {
                    ...response.data,
                    results: response.data?.results?.map(result => {
                        return {
                            ...result,
                            partialExposure: derivePercentage(result.partialExposure, response.data.totalPartialExposure)
                        }
                    })
                }
                $scope.results = derivedResponseData;
                if ($scope.results) {
                    let autoExposureLevel = 10 * (Math.log10($scope.results.totalPartialExposure / (3.2 * (Math.pow(10, -9)))));
                    $scope.autoExposureLevel = autoExposureLevel.toFixed(2);
                    $scope.progress = false;
                }
            }, response => {
                console.error(response);
                $scope.error = 'Something went wrong';
                $scope.progress = false;
            });
        }

        $timeout(getResults, 10000);
    }]);

    function derivePercentage(value, total) {
        if (!value || !total) {
            return 'N/A';
        }
        const val = parseFloat(value).toFixed(2);
        const tot = parseFloat(total).toFixed(2);
        return Math.round((val / tot) * 100) + '%';
    }
})();