(function() {
	angular.module("occIDEASApp.Tabs", [ 'ui.router' ]).config(Config);

	Config.$inject = ['$stateProvider'];
	function Config($stateProvider) {
		$stateProvider.state('tabs', {
			abstract : true,
			templateUrl : "scripts/tabs/view/tabs.html"
		}).state('tabs.modules', {
			templateUrl : "scripts/modules/view/modulesTable.html",
			controller: 'ModuleCtrl'
		}).state('tabs.fragments', {
			templateUrl : "scripts/fragment/view/fragments.html"
		}).state('tabs.agents', {
			templateUrl : "scripts/agents/view/agents.html"
		}).state('tabs.questions', {
			templateUrl: 'scripts/questions/view/questions.html',
	        controller: 'QuestionsCtrl as vm',
	        params:{row: null},
	        resolve:{
	        	data: function($stateParams,QuestionsService,QuestionsCache) {
	        		if(QuestionsCache.get($stateParams.row.idNode)){
	        			return QuestionsCache.get($stateParams.row.idNode);
	        		}
	        		return QuestionsService.findQuestions($stateParams.row)
	        				.then(function(data){
	        					QuestionsCache.put($stateParams.row.idNode,data);
				        		return data;
    				})
	        	}
	        }
		});
	}
})();