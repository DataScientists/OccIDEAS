(function(){
	angular.module('occIDEASApp.Questions')
	.service('QuestionsService',QuestionsService);
	
	QuestionsService.$inject = ['$http','$q'];
	function QuestionsService($http,$q){
		function findQuestions(idNode,type) {
			var restUrl = "";
			if(type=='M'){
				restUrl = 'web/rest/module/get?id=' + idNode;
				
			}else if(type=='F'){
				restUrl = 'web/rest/fragment/get?id=' + idNode;
				
			}else if(type=='P'){
				restUrl = 'web/rest/answer/getById?id='+idNode;
			}else{
				restUrl = 'web/rest/question/get?id=' + idNode;
			}
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		function findQuestion(idNode) {
			var restUrl = 'web/rest/question/get?id=' + idNode;
			
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		function findQuestionSingleChildLevel(idNode) {
			var restUrl = 'web/rest/question/getquestion?id=' + idNode;
			
			var request =  $http({
				  method: 'GET',
				  url: restUrl,
				  ignoreLoadingBar: true
				})
			return request.then(handleSuccess,handleError);
		}

		function getAllMultipleQuestion() {
			var restUrl = 'web/rest/question/getAllMultipleQuestion';
			
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		
		function findPossibleAnswer(idNode) {
			var restUrl = 'web/rest/answer/getById?id='+idNode;
			
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		
		function save(data){
			var restSaveUrl = 'web/rest/question/update';
			var request =  $http({
				  method: 'POST',
				  url: restSaveUrl,
				  data:data
				})
			return request.then(handleSuccess,handleError);
		}
		
		function exportToWord(data){
			var restSaveUrl = 'web/rest/module/exportToWord';
			var request =  $http({
				  method: 'POST',
				  url: restSaveUrl,
				  data:data
				})
			return request.then(handleSuccess,handleError);
		}
		
		function saveNode(data){
			if(data.nodeclass=='M'){
				var restSaveUrl = 'web/rest/module/update';
				var request =  $http({
					  method: 'POST',
					  url: restSaveUrl,
					  data:data
					})
			}else if(data.nodeclass=='F'){
				var restSaveUrl = 'web/rest/fragment/update';
				var request =  $http({
					  method: 'POST',
					  url: restSaveUrl,
					  data:data
					})
			}else{
				var restSaveUrl = 'web/rest/question/update';
				var request =  $http({
					  method: 'POST',
					  url: restSaveUrl,
					  data:data
					})
			}
			
			return request.then(handleSuccess,handleError);
		}
		
		function getMaxId(){
			var restGetMaxIdUrl = 'web/rest/question/getMaxId';
			var request =  $http({
				  method: 'GET',
				  url: restGetMaxIdUrl
				})
			return request.then(handleSuccess,handleError);
		}

        function getNextQuestion(idnode){
            var restNextQuestionUrl = 'web/rest/question/getNextQuestion?idNode=' + idnode;
            var request =  $http({
                method: 'GET',
                url: restNextQuestionUrl
            })
            return request.then(handleSuccess,handleError);
        }
        
        function getNodesWithAgent(agentId) {
			var restUrl = 'web/rest/question/getNodesWithAgent?agentId=' + agentId;
			
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
			findQuestions: findQuestions,
			findQuestionSingleChildLevel: findQuestionSingleChildLevel,
			findQuestion: findQuestion,
			findPossibleAnswer: findPossibleAnswer,
			save:save,
			saveNode:saveNode,
			getMaxId:getMaxId,
            getNextQuestion:getNextQuestion,
            getAllMultipleQuestion:getAllMultipleQuestion,
            getNodesWithAgent:getNodesWithAgent,
            exportToWord:exportToWord
		};
	}
})();