(function(){
	angular.module('occIDEASApp.Interviews')
	.service('InterviewsService',InterviewsService);
	
	InterviewsService.$inject = ['$http','$q'];
	function InterviewsService($http,$q){
		function findQuestions(idNode) {
			var restUrl = 'rest/module/get?id=' + idNode;
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		
		function save(data){
			var restSaveUrl = 'rest/question/update';
			var request =  $http({
				  method: 'POST',
				  url: restSaveUrl,
				  data:data
				})
			return request.then(handleSuccess,handleError);
		}

        function getNextQuestion(idnode){
            var restNextQuestionUrl = 'rest/question/getNextQuestion?idNode=' + idnode;
            var request =  $http({
                method: 'GET',
                url: restNextQuestionUrl
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
			findQuestions: findQuestions,
			save:save,
			
            getNextQuestion:getNextQuestion
		};
	}
})();