(function () {
    angular.module('occIDEASApp.InterviewResults')
        .service('InterviewResultsService', InterviewResultsService);

    InterviewResultsService.$inject = ['$http', '$q'];
    function InterviewResultsService($http, $q) {
    	function listByInterview(idInterview) {
			var restUrl = 'web/rest/interviewquestionanswer/getbyinterview?id=' + idInterview;
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess1,handleError);
		}
    	function listAllInterviews() {
			var restUrl = 'web/rest/interview/getlistwithanswers';
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess1,handleError);
		}
    	function getInterviews() {
			var restUrl = 'web/rest/interviewquestionanswer/getlist';
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
        	listAllInterviews: listAllInterviews,
        	listByInterview: listByInterview,
        	getInterviews: getInterviews         
        };
    }
})();