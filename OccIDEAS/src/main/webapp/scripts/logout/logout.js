(function() {
    angular.module('cams.logout',['ui.router']).config(Config);

    Config.$inject = ['$stateProvider'];

    function Config($stateProvider){
           $stateProvider
        .state('logout', {
            controller: 'LogoutCtrl'
        });
    }
})();