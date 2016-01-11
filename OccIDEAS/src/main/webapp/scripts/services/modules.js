angular
  .module('occIDEASApp')
  .factory('Modules', ['$http', function($http) {
	var apiUrl = '';
	var modulesUrl = apiUrl + '/modules';
	var apiKey = '';

	modulesUrl = 'ListServlet';
	    
	var getModules = function() {
	  return $http.get(modulesUrl + '?deleted=false').then(function(response) {
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

