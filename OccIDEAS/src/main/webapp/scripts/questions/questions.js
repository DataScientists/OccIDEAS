(function(){
	angular
	  .module('occIDEASApp')
	  .config(Config)
	  .controller('QuestionsCtrl',QuestionsCtrl)
	  .service('QuestionsService',QuestionsService);
	
	Config.$inject('$stateProvider');
	function Config($stateProvider){
		 $stateProvider
		    .state('questionView', {
		        url: '/questionView',
		        templateUrl: 'scripts/questions/view/questions.html',
		        controller: 'QuestionsCtrl',
		        onEnter: function(QuestionsService, $stateParams) {
		        	QuestionsService.findQuestions($stateParams.row);
		        }
		 });
	}
	
	QuestionsCtrl.$inject();
	function QuestionsCtrl(){
		var vm = this;
	}
	
	QuestionsService.$inject();
	function QuestionsService(){
		console.log("inside service");
		var services = {
				findQuestions: findQuestions
		}
		
		var findQuestions = function(row) {
			console.log("inside findQuestions "+row);
		};
		return services;
	}
});