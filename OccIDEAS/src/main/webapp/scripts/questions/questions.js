(function(){
	angular
	  .module('occIDEASApp.Questions',['ui.router'])
	  .config(Config)
	  .factory('QuestionsCache',QuestionsCache);
	
	Config.$inject = ['$stateProvider','treeConfig'];
	function Config($stateProvider,treeConfig){
		 $stateProvider
		    .state('questionView', {
		        url: '/questionView',
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
		 
		 treeConfig.defaultCollapsed = false;
	}
	
	QuestionsCache.$inject = ['$cacheFactory'];
	function QuestionsCache($cacheFactory){
		return $cacheFactory('questions-cache');
	}
})();