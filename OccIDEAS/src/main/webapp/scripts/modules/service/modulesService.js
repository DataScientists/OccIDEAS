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
		      get: getModules,
		      post: postNewModule, 
		      deleteModule: deleteModule
		};
	}
	
})();