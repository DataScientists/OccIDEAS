(function(){
	angular.module('occIDEASApp.Rule')
	.service('RulesService',QuestionsService);
	
	RulesService.$inject = ['$http','$q'];
	function RulesService($http,$q){
		function findRules(idNode) {
			var restUrl = 'rest/rules/get?id=' + idNode;
			var request =  $http({
				  method: 'GET',
				  url: restUrl
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
			findRules: findRules
			
		};
	}
})();