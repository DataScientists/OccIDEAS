angular
  .module('occIDEASApp', [
    'ui.router'
  ])
  .config(['$urlRouterProvider', '$stateProvider', function($urlRouterProvider, $stateProvider) {
    $urlRouterProvider.otherwise('/');
    $stateProvider
    .state('jerseymodule', {
        url: '/jerseymodules',
        templateUrl: 'modules.html',
        controller: 'jerseymoduleCtrl'
      })
      .state('module', {
        url: '/modules',
        templateUrl: 'modules.html',
        controller: 'moduleCtrl'
      })
      .state('fragment', {
        url: '/fragment',
        templateUrl: 'fragment.html',
        controller: 'fragmentCtrl'
      })
      .state('agent', {
        url: '/agent',
        templateUrl: 'agent.html'
      });
  }]);