(function () {
    var app = angular
        .module("surveyResultsApp", []);
    app.config(['$locationProvider', function ($locationProvider) {
        $locationProvider.html5Mode({
            enabled: true,
            requireBase: false
        })
    }]);
    app.controller('resultCtrl', ($scope, $http, $location) => {
        const params = $location.search();
        const sid = params.SID;
        const rid = params.RID;
        $http({
            url: "/view-results",
            method: "GET",
            params: {SID: sid, RID: rid}
        }).then(response => $scope.results = JSON.parse(response.data));
    });
})();