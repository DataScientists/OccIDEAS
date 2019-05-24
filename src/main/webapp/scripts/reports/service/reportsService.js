(function(){
	angular.module('occIDEASApp.Reports')
	.service('ReportsService',ReportsService);
	
	ReportsService.$inject = ['$http','$q'];
	function ReportsService($http,$q){
		var url = 'web/rest/reportHistory/';
		
		function getAll() {
			var restUrl = url+'getAll';
			
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		
		function getByType(type) {
			var restUrl = url+'getByType?type=' + type;
			
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		
		function downloadReport(reportVo){
			var restDownloadReportUrl = url+'downloadReport';
			var request =  $http({
				  method: 'POST',
				  url: restDownloadReportUrl,
				  data:reportVo,
				  responseType:'arraybuffer'
				})
			return request.then(handleSuccess,handleError);
    	}
    	
    	function downloadLookup(reportVo){
			var restDownloadReportUrl = url+'downloadLookup';
			var request =  $http({
				  method: 'POST',
				  url: restDownloadReportUrl,
				  data:reportVo,
				  responseType:'arraybuffer'
				})
			return request.then(handleSuccess,handleError);
    	}
		
		function save(data){
			var restSaveUrl = url+'save';
			var request =  $http({
				  method: 'POST',
				  url: restSaveUrl,
				  data:data
				})
			return request.then(handleSuccess,handleError);
		}
		
		function deleteReport(data){
			var restSaveUrl = url+'delete';
			var request =  $http({
				  method: 'POST',
				  url: restSaveUrl,
				  data:data
				})
			return request.then(handleSuccess,handleError);
		}
		

		function handleError( response ) {
            if (
                ! angular.isObject( response.data ) ||
                ! response.data.message
                ) {
                return( $q.reject( "An unknown error occurred." ) );
            }
            return( $q.reject( response.data.message ) );
        }

		function handleSuccess( response ) {
            return(response);
        }
		
		return {
			getAll: getAll,
			getByType: getByType,
			save: save,
			deleteReport: deleteReport,
			downloadReport:downloadReport,
			downloadLookup:downloadLookup
		};
	}
	
})();