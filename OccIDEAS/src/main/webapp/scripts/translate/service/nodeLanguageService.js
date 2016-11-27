(function(){
	angular.module('occIDEASApp.NodeLanguage')
	.service('NodeLanguageService',NodeLanguageService);
	
	NodeLanguageService.$inject = ['$http','$q'];
	function NodeLanguageService($http,$q){
		function getNodeByLanguage(language) {
			var restUrl = 'web/rest/nodelanguage/getNodeByLanguage?language=' + language;
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		function save(data){
			var restSaveUrl = 'web/rest/nodelanguage/save';
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
		function handleSuccess1( response ) {
            return( response.data );
        }
		return {
			getNodeByLanguage:getNodeByLanguage,
			save:save
		};
	}
})();