(function() {

    angular.module('cams.login',['ui.router']).config(Config);

    Config.$inject = ['$stateProvider'];

    function Config($stateProvider){
           $stateProvider
        .state('login', {
            url: '/',
            templateUrl: 'resources/views/login/view/login.html',
            controller: 'LoginCtrl',
            controllerAs: 'vm',
            authenticate: false
        });
    }

})();