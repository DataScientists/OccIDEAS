(function() {
    angular.module('occIDEASApp.Logout',['ui.router']).config(Config);

    Config.$inject = ['$stateProvider'];

    function Config($stateProvider){
           $stateProvider
        .state('logout', {
            controller: 'LogoutCtrl'
        });
    }
})();