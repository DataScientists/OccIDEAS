(function(){
	angular.module('occIDEASApp.ModuleRule')
			.service('ModuleRuleService',ModuleRuleService);
	
	ModuleRuleService.$inject = ['$http','$q'];
	function ModuleRuleService($http,$q){
		var apiUrl = '/occideas/rest/';
		var moduleRuleUrl = apiUrl + 'modulerule';
		
		var getModuleRule = function(idNode) {
			  var request = $http({
				method:'GET',
				url: moduleRuleUrl+'/getbyidnode?id='+idNode
			  });
			  return request.then(handleSuccess,handleError);
		}; 

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
			getModuleRule: getModuleRule
		};
	}
})();