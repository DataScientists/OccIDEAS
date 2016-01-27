(function(){
	angular.module('occIDEASApp.Agents')
	.service('AgentsService',AgentsService);

	AgentsService.$inject = ['$http','AgentsCache'];
	function AgentsService($http,AgentsCache){
		var apiUrl = '/occideas/rest/';
		var modulesUrl = apiUrl + 'agent';
		var apiKey = '';
		
		var getAgents = function() {
		  return $http.get(modulesUrl+'/getlist').then(function(response) {
		    var data = response.data;
		    AgentsCache.put("all",data);
		    return data;
		  });
		};

		var postNewAgent = function(moduleObj) {
		  return $http.post(modulesUrl + '?apiKey='+apiKey).then(function(response) {
			console.log(response.data.id);
		  });
		};
		
		var deleteAgent = function(moduleObj) {
			  return $http.post(modulesUrl+'/delete' + '?id='+moduleObj.id).then(function(response) {
				console.log(response.data.id);
			  });
			}; 

		return {
		      get: getAgents,
		      post: postNewAgent, 
		      deleteAgent: deleteAgent
		};
	}
	
})();