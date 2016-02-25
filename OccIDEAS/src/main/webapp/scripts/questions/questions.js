(function(){
	angular
	  .module('occIDEASApp.Questions',['ui.router'])
	  .config(Config)
	  .factory('QuestionsCache',QuestionsCache);
	
	Config.$inject = ['$stateProvider','treeConfig'];
	function Config($stateProvider,treeConfig){
		 treeConfig.defaultCollapsed = false;
	}
	
	QuestionsCache.$inject = ['$cacheFactory'];
	function QuestionsCache($cacheFactory){
		return $cacheFactory('questions-cache');
	}
})();