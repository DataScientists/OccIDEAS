(function(){
	angular.module('occIDEASApp.Agents')
	.service('AgentsService',AgentsService);

	AgentsService.$inject = ['$http'];
	function AgentsService($http){
		var apiUrl = '/occideas/rest/';
		var modulesUrl = apiUrl + 'agent';
		var apiKey = '';
		
		var getAgents = function() {
		  return $http.get(modulesUrl+'/getlist',{ cache: true}).then(function(response) {
		    var data = response.data;
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