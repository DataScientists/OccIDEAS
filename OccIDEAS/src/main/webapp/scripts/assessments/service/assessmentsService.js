(function () {
    angular.module('occIDEASApp.Assessments')
        .service('AssessmentsService', AssessmentsService);

    AssessmentsService.$inject = ['$http', '$q'];
    function AssessmentsService($http, $q) {
    	function listByInterview(idInterview) {
			var restUrl = 'web/rest/interviewquestionanswer/getbyinterview?id=' + idInterview;
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess1,handleError);
		}
    	function getInterviews() {
			var restUrl = 'web/rest/interview/getlist';
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess1,handleError);
		}
    	function getInterview(idInterview) {
			var restUrl = 'web/rest/interview/get?id=' + idInterview;
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
    	function updateFiredRules(idInterview) {
			var restUrl = 'web/rest/interview/updatefiredrules?id=' + idInterview;
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
    	function getAssessments() {
			var restUrl = 'web/rest/interview/getassessments';
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess1,handleError);
		}

        function handleError(response) {
            if (
                !angular.isObject(response.data) || !response.data.message
            ) {
                return ( $q.reject("An unknown error occurred.") );
            }
            return ( $q.reject(response.data.message) );
        }

        function handleSuccess(response) {
            return (response);
        }
        function handleSuccess1(response) {
            return (response.data);
        }

        return {
        	updateFiredRules: updateFiredRules,
        	listByInterview: listByInterview,
        	getInterview: getInterview,
        	getInterviews: getInterviews,
        	getAssessments: getAssessments 
        };
    }
})();