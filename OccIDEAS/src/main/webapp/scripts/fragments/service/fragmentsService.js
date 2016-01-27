(function(){
	angular.module('occIDEASApp.Fragments')
	.service('FragmentsService',FragmentsService);

	FragmentsService.$inject = ['$http'];
	function FragmentsService($http){
		var apiUrl = '/occideas/rest/';
		var modulesUrl = apiUrl + 'fragment';
		var apiKey = '';
		
		var getFragments = function() {
		  return $http.get(modulesUrl+'/getlist').then(function(response) {
		    var data = response.data;
		    //FragmentsCache.put("all",data);
		    return data;
		  });
		};
		
		/*function findFragment(idNode) {
			var restUrl = 'rest/fragment/get?id=' + idNode;
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
			return $http.get(restUrl).then(function(response) {
			    var data = response.data;
			    return data;
			  });
		}
*/
		var postNewFragment = function(moduleObj) {
		  return $http.post(modulesUrl + '?apiKey='+apiKey).then(function(response) {
			console.log(response.data.id);
		  });
		};
		
		var deleteFragment = function(moduleObj) {
			  return $http.post(modulesUrl+'/delete' + '?id='+moduleObj.id).then(function(response) {
				console.log(response.data.id);
			  });
			}; 

		return {		  
		      get: getFragments,
		      post: postNewFragment, 	    
		      deleteFragment: deleteFragment
		};
		/*function handleError( response ) {
            if (
                ! angular.isObject( response.data ) ||
                ! response.data.message
                ) {
                return( $q.reject( "An unknown error occurred." ) );
            }
            return( $q.reject( response.data.message ) );
        }

		function handleSuccess( response ) {
            return( response.data );
        }*/
	}
	
})();