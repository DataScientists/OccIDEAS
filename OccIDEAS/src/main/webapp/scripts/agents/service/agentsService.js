(function(){
	angular.module('occIDEASApp.Agents')
	.service('AgentsService',AgentsService);

	AgentsService.$inject = ['$http'];
	function AgentsService($http){
		var apiUrl = 'web/rest/';
		var agentEndpoint = apiUrl + 'agent';
		var apiKey = '';
		
		var getAgents = function() {
		  return $http.get(agentEndpoint+'/getlist',{ cache: false}).then(function(response) {
		    var data = response.data;
		    return data;
		  });
		};
		var getStudyAgents = function() {
			  return $http.get(agentEndpoint+'/getstudyagents',{ cache: false}).then(function(response) {
			    var data = response.data;
			    return data;
			  });
			};
        var findAgent = function(agentId) {
            var request = $http({
                method:'GET',
                url: agentEndpoint+'/find?agentId='+agentId
            });
            return request.then(handleSuccess,handleError);
        };
		var postNewAgent = function(moduleObj) {
		  return $http.post(agentEndpoint + '?apiKey='+apiKey).then(function(response) {
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
		var deleteAgent = function(agentObj) {
            var request = $http({
                method:'POST',
                url: agentEndpoint+'/delete',
                data:agentObj
            });
            return request.then(handleSuccess,handleError);
        };
        var hasRules = function(agentId) {
            var request = $http({
                method:'GET',
                url: agentEndpoint+'/hasrules?agentId='+ agentId
            });
            return request.then(handleSuccess,handleError);
        };
        var getRules = function(agentId) {
            var request = $http({
                method:'GET',
                url: agentEndpoint+'/getrules?agentId='+ agentId
            });
            return request.then(handleSuccess,handleError);
        };
        var loadStudyAgents = function() {
            var request = $http({
                method:'GET',
                url: agentEndpoint+'/loadStudyAgents'
            });
            return request.then(handleSuccess,handleError);
        };
        
        var updateStudyAgents = function(agent){
        	var request = $http({
                method:'POST',
                url: agentEndpoint+'/updateStudyAgents',
                data:agent
            });
            return request.then(handleSuccess,handleError);
        };
        
        var deleteStudyAgents = function(agent){
        	var request = $http({
                method:'POST',
                url: agentEndpoint+'/deleteStudyAgents',
                data:agent
            });
            return request.then(handleSuccess,handleError);
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
			  getStudyAgents: getStudyAgents,
		      save: save, 
		      post: postNewAgent, 
		      deleteAgent: deleteAgent,
              hasRules: hasRules,
              findAgent:findAgent,
              getRules:getRules,
              loadStudyAgents:loadStudyAgents,
              updateStudyAgents:updateStudyAgents,
              deleteStudyAgents:deleteStudyAgents
		};
	}
	
})();