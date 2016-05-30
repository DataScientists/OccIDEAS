(function(){
	angular.module('occIDEASApp.Modules')
	.service('ModulesService',ModulesService);

	ModulesService.$inject = ['$http','ModulesCache'];
	function ModulesService($http,ModulesCache){
		var apiUrl = '/occideas/web/rest/';
		var modulesUrl = apiUrl + 'module';
		var apiKey = '';
		
		var getModules = function() {
		  return $http.get(modulesUrl+'/getlist').then(function(response) {
		    var data = response.data;
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
		function save(data){
			var restSaveUrl = 'web/rest/module/update';
			var request =  $http({
				  method: 'POST',
				  url: restSaveUrl,
				  data:data
				})
			return request.then(handleSuccess,handleError);
		}
		
		var deleteModule = function(moduleObj) {
			  var request = $http({
				method:'POST',
				url: modulesUrl+'/delete',
				data:moduleObj
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
			getActiveModules: getActiveModules,
			get: getModules,
			save:save,
		    post: postNewModule, 
		    deleteModule: deleteModule
		};
	}
	
})();