(function () {
    angular.module('occIDEASApp.Assessments')
        .service('AssessmentsService', AssessmentsService);

    AssessmentsService.$inject = ['$http', '$q'];
    function AssessmentsService($http, $q) {
    	function listByInterview(idInterview) {
			var restUrl = 'rest/interviewquestionanswer/getbyinterview?id=' + idInterview;
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess1,handleError);
		}
    	function getInterviews() {
			var restUrl = 'rest/interview/getlist';
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess1,handleError);
		}
    	function getAssessments() {
			var restUrl = 'rest/interviews/getassessments';
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
        	listByInterview: listByInterview,
        	getInterviews: getInterviews,
        	getAssessments: getAssessments 
        };
    }
})();