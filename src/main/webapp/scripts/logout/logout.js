(function() {
  angular.module('occIDEASApp.Logout', ['ui.router', 'ngStorage']).config(Config);

  Config.$inject = ['$stateProvider'];

  function Config($stateProvider) {
    $stateProvider
      .state('logout', {
        controller: 'LogoutCtrl'
      });
  }
})();