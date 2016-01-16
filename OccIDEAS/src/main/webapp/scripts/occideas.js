angular
  .module("occIDEASApp", [
    "ui.router",
    "ngMaterial",
    "ui.tree",
    "ngTable"
  ])
  .config(['$urlRouterProvider', '$stateProvider','QuestionsService', function($urlRouterProvider, $stateProvider,QuestionsService) {
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
      .state('questionView', {
		        url: '/questionView',
		        templateUrl: 'scripts/questions/view/questions.html',
		        controller: 'QuestionsCtrl',
		        onEnter: function(QuestionsService, $stateParams) {
		        	QuestionsService.findQuestions($stateParams.row);
		        }
      })
      .state('agent', {
        url: '/agent',
        templateUrl: 'agent.html',
        controller: 'agentCtrl'
      });
  }]);