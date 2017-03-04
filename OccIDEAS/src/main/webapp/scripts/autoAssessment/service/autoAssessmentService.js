(function() {
	angular.module('occIDEASApp.AutoAssessment').service('AutoAssessmentService',
			AutoAssessmentService);

	AutoAssessmentService.$inject = [ '$http', '$q' ];
	function AutoAssessmentService($http, $q) {
		function save(data) {
			var restURL = 'web/rest/interviewAutoAssessment/addAutoAssessment';
			var request = $http({
				method : 'POST',
				url : restURL,
				data : data
			})
			return request.then(handleSuccess, handleError);
		}
		function saveAutoAssessments(data) {
			var restURL = 'web/rest/interviewAutoAssessment/saveAutoAssessments';
			var request = $http({
				method : 'POST',
				url : restURL,
				data : data
			})
			return request.then(handleSuccess, handleError);
		}
		
		
		function getByInterviewId(id){
			var restURL = 'web/rest/interviewAutoAssessment/getByInterviewId?id=' + id;
			var request = $http({
				method : 'GET',
				url : restURL
			})
			return request.then(handleSuccess, handleError);
		}
		
		function findNodeById(id){
			var restURL = 'web/rest/interviewAutoAssessment/findNodeById?id=' + id;
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
			saveAutoAssessments : saveAutoAssessments,
			getByInterviewId:getByInterviewId,
			findNodeById:findNodeById
		};
	}
})();