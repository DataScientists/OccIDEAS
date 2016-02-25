(function(){
	angular.module('occIDEASApp.Questions')
	.service('QuestionsService',QuestionsService);
	
	QuestionsService.$inject = ['$http','$q'];
	function QuestionsService($http,$q){
		function findQuestions(idNode,type) {
			var restUrl = "";
			if(type=='M'){
				restUrl = 'rest/module/get?id=' + idNode;
				
			}else if(type=='F'){
				restUrl = 'rest/fragment/get?id=' + idNode;
				
			}else if(type=='P'){
				restUrl = 'rest/answer/getById?id='+idNode;
			}else{
				restUrl = 'rest/question/get?id=' + idNode;
			}
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		function findQuestion(idNode) {
			var restUrl = 'rest/question/get?id=' + idNode;
			
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		function findPossibleAnswer(idNode) {
			var restUrl = 'rest/answer/getById?id='+idNode;
			
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
		function saveNode(data){
			if(data.nodeclass=='M'){
				var restSaveUrl = 'rest/module/update';
				var request =  $http({
					  method: 'POST',
					  url: restSaveUrl,
					  data:data
					})
			}else if(data.nodeclass=='F'){
				var restSaveUrl = 'rest/fragment/update';
				var request =  $http({
					  method: 'POST',
					  url: restSaveUrl,
					  data:data
					})
			}else{
				var restSaveUrl = 'rest/question/update';
				var request =  $http({
					  method: 'POST',
					  url: restSaveUrl,
					  data:data
					})
			}
			
			return request.then(handleSuccess,handleError);
		}
		
		function getMaxId(){
			var restGetMaxIdUrl = 'rest/question/getMaxId';
			var request =  $http({
				  method: 'GET',
				  url: restGetMaxIdUrl
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
			findQuestion: findQuestion,
			findPossibleAnswer: findPossibleAnswer,
			save:save,
			saveNode:saveNode,
			getMaxId:getMaxId,
            getNextQuestion:getNextQuestion
		};
	}
})();