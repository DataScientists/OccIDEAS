angular
  .module('occIDEASApp')
  .factory('Modules', ['$http', function($http) {
	var apiUrl = 'http://localhost:8080/occideas/rest/';
	var modulesUrl = apiUrl + 'module';
	var apiKey = '';
	
	var getModules = function() {
	  return $http.get(modulesUrl+'/getAll').then(function(response) {
	    return response.data;
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

	// factory definition
	return {
	      get: getModules,
	      post: postNewModule, 
	      deleteModule: deleteModule
	};

  }]);

