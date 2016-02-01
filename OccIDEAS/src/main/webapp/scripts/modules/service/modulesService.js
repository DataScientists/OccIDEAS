(function(){
	angular.module('occIDEASApp.Modules')
	.service('ModulesService',ModulesService);

	ModulesService.$inject = ['$http','ModulesCache'];
	function ModulesService($http,ModulesCache){
		var apiUrl = '/occideas/rest/';
		var modulesUrl = apiUrl + 'module';
		var apiKey = '';
		
		var getModules = function() {
		  return $http.get(modulesUrl+'/getlist').then(function(response) {
		    var data = response.data;
		    ModulesCache.put("all",data);
		    return data;
		  });
		};
		
		var getActiveModules = function() {
			  return $http.get(modulesUrl+'/getlist').then(function(response) {
			    var data = response.data;
			    var filteredData = [];
			    for(var i=0;i < data.length;i++){
					var node = data[i];
					if(node.type=='M_Module'){					
						filteredData.push(node);
					}
				}
			    return filteredData;
			  });
			};

		var postNewModule = function(moduleObj) {
		  return $http.post(modulesUrl + '?apiKey='+apiKey).then(function(response) {
			console.log(response.data.id);
		  });
		};
		
		var deleteModule = function(moduleObj) {
			  return $http.post(modulesUrl+'/delete' + '?id='+moduleObj.id).then(function(response) {
				console.log(response.data.id);
			  });
			}; 

		return {
			getActiveModules: getActiveModules,
			get: getModules,
		      post: postNewModule, 
		      deleteModule: deleteModule
		};
	}
	
})();