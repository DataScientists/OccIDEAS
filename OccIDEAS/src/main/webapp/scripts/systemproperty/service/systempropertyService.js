(function(){
	angular.module('occIDEASApp.SystemProperty')
	.service('SystemPropertyService',SystemPropertyService);
	
	SystemPropertyService.$inject = ['$http','$q'];
	function SystemPropertyService($http,$q){
		function getById(id) {
			var restUrl = 'web/rest/systemproperty/getById?id=' + id;
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		
		function save(data) {
			var restSaveUrl = 'web/rest/systemproperty/save';
			var request =  $http({
				  method: 'POST',
				  url: restSaveUrl,
				  data:data
				})
			return request.then(handleSuccess,handleError);
		}
		
		function handleSuccess( response ) {
            return(response);
        }
		function handleSuccess1( response ) {
            return( response.data );
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
		return {
			getById: getById,
			save: save
		};
	}
})();