(function(){
	angular.module('occIDEASApp.Questions')
	.service('QuestionsService',QuestionsService);
	
	QuestionsService.$inject = [];
	function QuestionsService(){
		console.log("inside service");
		
		function findQuestions(row) {
			console.log("inside findQuestions "+row);
		};
		return {
			findQuestions: findQuestions
		};
	}
})();