(function(){
	angular.module('occIDEASApp.Rules')
	.service('RulesService',RulesService);
	
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
		function listAllRules() {
			var restUrl = 'rest/modulerule/getlist';
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess1,handleError);
		}
		function listByModule(idNode) {
			var restUrl = 'rest/modulerule/getbymodule?id=' + idNode;
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess1,handleError);
		}
		function listByAgent(idAgent) {
			var restUrl = 'rest/modulerule/getbyagent?id=' + idAgent;
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess1,handleError);
		}
		function getRule(idRule) {
			var restUrl = 'rest/rule/get?id=' + idRule;
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		function save(data){
			var restSaveUrl = 'rest/rule/saveOrUpdate';
			var request =  $http({
				  method: 'POST',
				  url: restSaveUrl,
				  data:data
				})
			return request.then(handleSuccess,handleError);
		}
		function update(data){
			var restSaveUrl = 'rest/rule/update';
			var request =  $http({
				  method: 'POST',
				  url: restSaveUrl,
				  data:data
				})
			return request.then(handleSuccess,handleError);
		}
		function create(data){
			var restSaveUrl = 'rest/rule/create';
			var request =  $http({
				  method: 'POST',
				  url: restSaveUrl,
				  data:data
				})
			return request.then(handleSuccess,handleError);
		}
		//modulerule/getlist
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
			getRule: getRule,
			findRules: findRules,
			update:update
		};
	}
})();