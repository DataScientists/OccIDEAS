angular
  .module('occIDEASApp', [
    'ui.router'
  ])
  .config(['$urlRouterProvider', '$stateProvider', function($urlRouterProvider, $stateProvider) {
    $urlRouterProvider.otherwise('/');
    $stateProvider
    .state('allmodules', {
        url: '/allmodules',
        templateUrl: 'modules.html',
        controller: 'moduleCtrl'
      })
      .state('module', {
        url: '/module',
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
        templateUrl: 'agent.html',
        controller: 'agentCtrl'
      });
  }]);