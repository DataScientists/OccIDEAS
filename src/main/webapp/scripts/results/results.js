(function () {

    angular.module('occIDEASApp.Results', ['ui.router', 'ngStorage']).config(Config);

    Config.$inject = ['$stateProvider'];

    function Config($stateProvider) {
        $stateProvider
            .state('results', {
                url: '/results',
                templateUrl: "scripts/results/view/view-results.html",
                controller: 'ResultCtrl',
                controllerAs: 'vm',
                authenticate: false
            });
    }

})();