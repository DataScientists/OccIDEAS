(function() {
	angular.module('occIDEASApp.FiredRules').service('FiredRulesService',
			FiredRulesService);

	FiredRulesService.$inject = [ '$http', '$q' ];
	function FiredRulesService($http, $q) {
		function save(data) {
			var restURL = 'web/rest/interviewfiredrules/addFiredRules';
			var request = $http({
				method : 'POST',
				url : restURL,
				data : data
			})
			return request.then(handleSuccess, handleError);
		}
		
		function getAnswerWithModuleRule(id){
			var restURL = 'web/rest/answer/getAnswerWithModuleRule?id=' + id;
			var request = $http({
				method : 'GET',
				url : restURL
			})
			return request.then(handleSuccess, handleError);
		}
		
		function getByInterviewId(id){
			var restURL = 'web/rest/interviewfiredrules/getByInterviewId?id=' + id;
			var request = $http({
				method : 'GET',
				url : restURL
			})
			return request.then(handleSuccess, handleError);
		}
		
		
		
		function handleError(response) {
			if (!angular.isObject(response.data) || !response.data.message) {
				return ($q.reject("An unknown error occurred."));
			}
			return ($q.reject(response.data.message));
		}

		function handleSuccess(response) {
			return (response);
		}

		return {
			save : save,
			getAnswerWithModuleRule:getAnswerWithModuleRule,
			getByInterviewId:getByInterviewId
		};
	}
})();