(function(){

angular
  .module("occIDEASApp", [
    "ui.router",
    "ngMaterial",
    "ui.tree",
    "ngTable",
    "ngSanitize",
    "occIDEASApp.Tabs",
    "occIDEASApp.Questions",
    "occIDEASApp.Fragments",
    "occIDEASApp.Modules"
  ], function($rootScopeProvider){
	  $rootScopeProvider.digestTtl(100);
  })
  .config(['$urlRouterProvider', '$stateProvider', function($urlRouterProvider, $stateProvider) {
    $urlRouterProvider.otherwise('/');
    $stateProvider
    .state('allmodules', {
        url: '/allmodules',
        controller: 'TabsCtrl as vm',
        templateUrl: 'scripts/modules/view/modules.html'
      })
      .state('module', {
        url: '/module',
        templateUrl: 'scripts/modules/view/modules.html'
      })
      .state('allfragments', {
        url: '/allfragments',
        controller: 'TabsCtrl as vm',
        templateUrl: 'scripts/modules/view/modules.html'
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