(function(){

angular
  .module("occIDEASApp", [
    "ui.router",
    "ngMaterial",
    "ui.tree",
    "ngTable",
    
    "occIDEASApp.Questions",
    "occIDEASApp.Modules"
  ])
  .config(['$urlRouterProvider', '$stateProvider', function($urlRouterProvider, $stateProvider) {
    $urlRouterProvider.otherwise('/');
    $stateProvider
    .state('allmodules', {
        url: '/allmodules',
        templateUrl: 'scripts/modules/view/modules.html',
        controller: 'ModuleCtrl'
      })
      .state('module', {
        url: '/module',
        templateUrl: 'scripts/modules/view/modules.html',
        controller: 'ModuleCtrl'
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
  }])
  .run(configureDefaults);

   configureDefaults.$inject = ['ngTableDefaults'];
   function configureDefaults(ngTableDefaults) {
        ngTableDefaults.params.count = 5;
        ngTableDefaults.settings.counts = [];
   }
})();