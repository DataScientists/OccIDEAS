(function(){
	angular.module('occIDEASApp.NodeLanguage')
	.service('NodeLanguageService',NodeLanguageService);
	
	NodeLanguageService.$inject = ['$http','$q'];
	function NodeLanguageService($http,$q){
		var url = 'web/rest/nodelanguage';
		
		function getUntranslatedModules(flag) {
			var restUrl = url+'/getUntranslatedModules?flag='+flag;
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		
		function getTotalUntranslatedModule() {
			var restUrl = url+'/getTotalUntranslatedModule';
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		
		function getTotalModuleCount(){
			var restUrl = url+'/getTotalModuleCount';
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		
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
		
		function batchSave(data){
			var restSaveUrl = url+'/batchSave';
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
		
		function getNodeNodeLanguageFragmentList(){
			var restUrl = url+'/getNodeNodeLanguageFragmentList';
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		
		function getUntranslatedFragments(flag){
			var restUrl = url+'/getUntranslatedFragments?flag='+flag;
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		
		function getTotalUntranslatedFragment(){
			var restUrl = url+'/getTotalUntranslatedFragment';
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		
		function getTotalFragmentCount(){
			var restUrl = url+'/getTotalFragmentCount';
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		
		function getLanguageModBreakdown(flag){
			var restUrl = url+'/getLanguageModBreakdown?flag='+flag;
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		
		function getLanguageFragBreakdown(flag){
			var restUrl = url+'/getLanguageFragBreakdown?flag='+flag;
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
			getNodeNodeLanguageList:getNodeNodeLanguageList,
			getUntranslatedModules:getUntranslatedModules,
			getTotalUntranslatedModule:getTotalUntranslatedModule,
			getTotalModuleCount:getTotalModuleCount,
			getNodeNodeLanguageFragmentList:getNodeNodeLanguageFragmentList,
			getUntranslatedFragments:getUntranslatedFragments,
			getTotalUntranslatedFragment:getTotalUntranslatedFragment,
			getTotalFragmentCount:getTotalFragmentCount,
			getLanguageModBreakdown:getLanguageModBreakdown,
			getLanguageFragBreakdown:getLanguageFragBreakdown,
			batchSave:batchSave
		};
	}
})();