(function () {
    angular.module('occIDEASApp.Assessments')
        .service('AssessmentsService', AssessmentsService);

    AssessmentsService.$inject = ['$http', '$q'];
    function AssessmentsService($http, $q) {
    	//Unused
    	function listByInterview(idInterview) {
			var restUrl = 'web/rest/interviewquestionanswer/getbyinterview?id=' + idInterview;
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess1,handleError);
		}
    	
    	function getAnswerSummaryByName(answerSummaryFilter) {
			var restUrl = 'web/rest/assessment/getAnswerSummaryByName';
			var request =  $http({
				  method: 'POST',
				  url: restUrl,
				  data:answerSummaryFilter
				})
			return request.then(handleSuccess,handleError);
		}
    	
    	function getInterviews() {
			var restUrl = 'web/rest/interview/getlist';
			var request =  $http({
				  method: 'GET',
				  url: restUrl,
				  ignoreLoadingBar: true
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
    	function getFiredRules(idInterview) {
			var restUrl = 'web/rest/interview/getfiredrules?id=' + idInterview;
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
    	function showAnswers(idInterview) {
			var restUrl = 'web/rest/interview/getAnswers?id=' + idInterview;
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
    	function saveManualAssessments(manualAssessments) {
			var restURL = 'web/rest/interview/saveManualAssessments';
			var request = $http({
				method : 'POST',
				url : restURL,
				data : data
			})
			return request.then(handleSuccess, handleError);
		}
    	function updateAutoAssessments(type) {
    		
			var restUrl = 'web/rest/interview/updateAutoAssessments?type=' 
				+ type;
			var request =  $http({
				  method: 'GET',
				  url: restUrl,
				  ignoreLoadingBar: true
				})
			return request.then(handleSuccess1,handleError);
		}
    	//Unused
    	function getAssessments() {
			var restUrl = 'web/rest/interview/getassessments';
			var request =  $http({
				  method: 'GET',
				  url: restUrl,
				  ignoreLoadingBar: true
				})
			return request.then(handleSuccess1,handleError);
		}
    	//Unused
    	function getAssessments() {
			var restUrl = 'web/rest/interview/getassessments';
			var request =  $http({
				  method: 'GET',
				  url: restUrl,
				  ignoreLoadingBar: true
				})
			return request.then(handleSuccess1,handleError);
		}
    	
    	function getAssessmentSize(status) {
			var restUrl = 'web/rest/interview/getAssessmentSize?status=' +status;
			var request =  $http({
				  method: 'GET',
				  url: restUrl,
				  ignoreLoadingBar: true
				})
			return request.then(handleSuccess,handleError);
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
        	updateAutoAssessments:updateAutoAssessments,
        	listByInterview: listByInterview,
        	getInterview: getInterview,
        	getInterviews: getInterviews,
        	getAssessments: getAssessments,
        	showAnswers:showAnswers,
        	getFiredRules:getFiredRules,
        	getAssessmentSize:getAssessmentSize,
        	getAnswerSummaryByName:getAnswerSummaryByName
        	
        };
    }
})();