(function() {
	angular.module('occIDEASApp.ManualAssessment').service('ManualAssessmentService',
			ManualAssessmentService);

	ManualAssessmentService.$inject = [ '$http', '$q' ];
	function ManualAssessmentService($http, $q) {
		function save(data) {
			var restURL = 'web/rest/interviewManualAssessment/addManualAssessment';
			var request = $http({
				method : 'POST',
				url : restURL,
				data : data
			})
			return request.then(handleSuccess, handleError);
		}
		function saveManualAssessments(data) {
			var restURL = 'web/rest/interviewManualAssessment/saveManualAssessments';
			var request = $http({
				method : 'POST',
				url : restURL,
				data : data
			})
			return request.then(handleSuccess, handleError);
		}
		
		
		function getByInterviewId(id){
			var restURL = 'web/rest/interviewManualAssessment/getByInterviewId?id=' + id;
			var request = $http({
				method : 'GET',
				url : restURL
			})
			return request.then(handleSuccess, handleError);
		}
		
		function findNodeById(id){
			var restURL = 'web/rest/interviewManualAssessment/findNodeById?id=' + id;
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
			saveManualAssessments : saveManualAssessments,
			getByInterviewId:getByInterviewId,
			findNodeById:findNodeById
		};
	}
})();