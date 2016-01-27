(function(){
	angular.module('occIDEASApp.Questions')
	.service('QuestionsService',QuestionsService);
	
	QuestionsService.$inject = ['$http','$q'];
	function QuestionsService($http,$q){
		function findQuestions(idNode) {
			var restUrl = 'rest/module/get?id=' + idNode;
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		function findFragment(idNode) {
			var restUrl = 'rest/fragment/get?id=' + idNode;
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
            return( response.data );
        }
		
		return {
			findQuestions: findQuestions,
			findFragment: findFragment
		};
	}
})();