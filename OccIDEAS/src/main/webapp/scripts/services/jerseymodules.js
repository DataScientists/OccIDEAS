angular
  .module('occIDEASApp')
  .factory('jerseyModules', ['$http', function($http) {
	var apiUrl = 'http://localhost:8080/occideas/rest/';
	var modulesUrl = apiUrl + 'modules';
	var apiKey = '';
	
	var getModules = function() {
	  return $http.get(modulesUrl).then(function(response) {
	    return response.data;
	  });
	};

	var postNewModule = function(moduleObj) {
	  return $http.post(modulesUrl + '?apiKey='+apiKey).then(function(response) {
		console.log(response.data.id);
	  });
	};

	// factory definition
	return {
	      get: getModules,
	      post: postNewModule 
	};

  }]);

