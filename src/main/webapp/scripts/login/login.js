(function() {

  angular.module('occIDEASApp.Login', ['ui.router', 'ngStorage']).config(Config);

  Config.$inject = ['$stateProvider'];

  function Config($stateProvider) {
    $stateProvider
      .state('login', {
        url: '/login',
        controller: 'LoginCtrl',
        controllerAs: 'vm',
        resolve: {
          message: function() {
            return '';
          }
        },
        authenticate: false
      })
      .state('loginHome', {
        url: '/loginHome/:message',
        templateUrl: "scripts/login/view/login.html",
        controller: 'LoginCtrl',
        controllerAs: 'vm',
        authenticate: false
      });
  }

})();