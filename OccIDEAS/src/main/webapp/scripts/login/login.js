(function() {

    angular.module('occIDEASApp.Login',['ui.router','ngStorage']).config(Config);

    Config.$inject = ['$stateProvider'];

    function Config($stateProvider){
           $stateProvider
        .state('login', {
            url: '/',
            templateUrl: 'scripts/login/view/login.html',
            controller: 'LoginCtrl',
            controllerAs: 'vm',
            authenticate: false
        });
    }

})();