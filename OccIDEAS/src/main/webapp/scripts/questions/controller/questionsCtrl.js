(function(){
	angular.module('occIDEASApp.Questions')
	.controller('QuestionsCtrl',QuestionsCtrl);
	
	QuestionsCtrl.$inject = ['QuestionsService'];
	function QuestionsCtrl(QuestionsService){
		var vm = this;
		console.log("controller:");
//		
//		vm.findQuestions = function(row){
//			var response = QuestionsService.findQuestions(row);
//			console.log(response);
//			vm.list = response;
//		};
		
	}
})();