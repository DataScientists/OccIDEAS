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
		        	data: function($stateParams,QuestionsService) {
		        		return QuestionsService.findQuestions($stateParams.row)
		        				.then(function(data){
		        					console.log("Data getting from questions AJAX ...");
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