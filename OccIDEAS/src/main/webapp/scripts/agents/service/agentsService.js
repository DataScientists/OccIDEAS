(function(){
	angular.module('occIDEASApp.Agents')
	.service('AgentsService',AgentsService);

	AgentsService.$inject = ['$http'];
	function AgentsService($http){
		var apiUrl = '/occideas/web/rest/';
		var modulesUrl = apiUrl + 'agent';
		var apiKey = '';
		
		var getAgents = function() {
		  return $http.get(modulesUrl+'/getlist',{ cache: false}).then(function(response) {
		    var data = response.data;
		    return data;
		  });
		};
		
		var postNewAgent = function(moduleObj) {
		  return $http.post(modulesUrl + '?apiKey='+apiKey).then(function(response) {
			console.log(response.data.id);
		  });
		};
		function save(data){
			var restSaveUrl = 'web/rest/agent/update';
			var request =  $http({
				  method: 'POST',
				  url: restSaveUrl,
				  data:data
				})
			return request.then(handleSuccess,handleError);
		}
		var deleteAgent = function(moduleObj) {
			  return $http.post(modulesUrl+'/delete' + '?id='+moduleObj.id).then(function(response) {
				console.log(response.data.id);
			  });
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
		      get: getAgents,
		      save: save, 
		      post: postNewAgent, 
		      deleteAgent: deleteAgent
		};
	}
	
})();