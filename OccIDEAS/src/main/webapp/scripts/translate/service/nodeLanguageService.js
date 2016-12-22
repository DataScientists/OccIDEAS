(function(){
	angular.module('occIDEASApp.NodeLanguage')
	.service('NodeLanguageService',NodeLanguageService);
	
	NodeLanguageService.$inject = ['$http','$q'];
	function NodeLanguageService($http,$q){
		var url = 'web/rest/nodelanguage';
		function getNodeLanguageById(id) {
			var restUrl = url+'/getNodeLanguageById?id=' + id;
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		function getAllLanguage(){
			var restUrl = url+'/getAllLanguage';
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		
		function getDistinctLanguage(){
			var restUrl = url+'/getDistinctLanguage';
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		
		function addLanguage(data){
			var restUrl = url+'/addLanguage';
			var request =  $http({
				  method: 'POST',
				  url: restUrl,
				  data:data
				})
			return request.then(handleSuccess,handleError);
		}
		
		function getNodesByLanguageAndWord(data){
			var restUrl = url+'/getNodesByLanguageAndWord';
			var request =  $http({
				  method: 'POST',
				  url: restUrl,
				  data:data
				})
			return request.then(handleSuccess,handleError);
		}
		
		function save(data){
			var restSaveUrl = url+'/save';
			var request =  $http({
				  method: 'POST',
				  url: restSaveUrl,
				  data:data
				})
			return request.then(handleSuccess,handleError);
		}
		
		function deleteNodeLanguage(data){
			var restSaveUrl = url+'/delete';
			var request =  $http({
				  method: 'POST',
				  url: restSaveUrl,
				  data:data
				})
			return request.then(handleSuccess,handleError);
		}
		
		function getLanguageById(id){
			var restUrl = url+'/getLanguageById?id=' + id;
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		
		function getNodeNodeLanguageList(){
			var restUrl = url+'/getNodeNodeLanguageList';
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
		function handleSuccess1( response ) {
            return( response.data );
        }
		return {
			getNodeLanguageById:getNodeLanguageById,
			save:save,
			getAllLanguage:getAllLanguage,
			addLanguage:addLanguage,
			getNodesByLanguageAndWord:getNodesByLanguageAndWord,
			deleteNodeLanguage:deleteNodeLanguage,
			getDistinctLanguage:getDistinctLanguage,
			getLanguageById:getLanguageById,
			getNodeNodeLanguageList:getNodeNodeLanguageList
		};
	}
})();