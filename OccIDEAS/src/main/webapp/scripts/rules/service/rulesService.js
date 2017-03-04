(function(){
	angular.module('occIDEASApp.Rules')
	.service('RulesService',RulesService);
	
	RulesService.$inject = ['$http','$q'];
	function RulesService($http,$q){
		function findRules(idNode) {
			var restUrl = 'web/rest/rules/get?id=' + idNode;
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		function listAllRules() {
			var restUrl = 'web/rest/modulerule/getlist';
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess1,handleError);
		}
		function listByModule(idNode) {
			var restUrl = 'web/rest/modulerule/getbymodule?id=' + idNode;
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess1,handleError);
		}
		function listByAgent(idAgent) {
			var restUrl = 'web/rest/modulerule/getbyagent?id=' + idAgent;
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess1,handleError);
		}
		function getRule(idRule) {
			var restUrl = 'web/rest/rule/get?id=' + idRule;
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		function save(data){
			var restSaveUrl = 'web/rest/rule/saveOrUpdate';
			var request =  $http({
				  method: 'POST',
				  url: restSaveUrl,
				  data:data
				})
			return request.then(handleSuccess,handleError);
		}
		function saveList(data){
			var restSaveUrl = 'web/rest/rule/saveList';
			var request =  $http({
				  method: 'POST',
				  url: restSaveUrl,
				  data:data
				})
			return request.then(handleSuccess,handleError);
		}
		function update(data){
			var restSaveUrl = 'web/rest/rule/update';
			var request =  $http({
				  method: 'POST',
				  url: restSaveUrl,
				  data:data
				})
			return request.then(handleSuccess,handleError);
		}
		function create(data){
			var restSaveUrl = 'web/rest/rule/create';
			var request =  $http({
				  method: 'POST',
				  url: restSaveUrl,
				  data:data
				})
			return request.then(handleSuccess,handleError);
		}
		function remove(data){
			var restDeleteUrl = 'web/rest/rule/delete';
			var request =  $http({
				  method: 'POST',
				  url: restDeleteUrl,
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
			listByModule: listByModule,
			listByAgent: listByAgent,
			listAllRules: listAllRules,
			create: create,
			save: save,
			saveList: saveList,
			getRule: getRule,
			findRules: findRules,
			update:update,
			remove:remove
		};
	}
})();