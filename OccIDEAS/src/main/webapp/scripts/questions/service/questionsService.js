(function(){
	angular.module('occIDEASApp.Questions')
	.service('QuestionsService',QuestionsService);
	
	QuestionsService.$inject = ['$http'];
	function QuestionsService($http){
		console.log("inside service");
		
		function findQuestions(row) {
			var restUrl = 'rest/module/get?id=' + row.idNode;
			$http({
				  method: 'GET',
				  url: restUrl
				}).then(function successCallback(response) {
				    console.log("Success1");
				    return response;
				  }, function errorCallback(response) {
					  console.log("error retrieving questions:"+response);
				  });
		};
		return {
			findQuestions: findQuestions
		};
	}
})();