(function(){
	angular
	  .module('occIDEASApp.Questions',['ui.router'])
	  .config(Config);
	
	Config.$inject = ['$stateProvider'];
	function Config($stateProvider){
		 $stateProvider
		    .state('questionView', {
		        url: '/questionView',
		        templateUrl: 'scripts/questions/view/questions.html',
		        controller: 'QuestionsCtrl',
		        params:{row: null},
		        onEnter: function(QuestionsService, $stateParams) {
		        	QuestionsService.findQuestions($stateParams.row);
		        }
		 });
	}
})();