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
		function save(data){
			var restSaveUrl = 'rest/fragment/update';
			var request =  $http({
				  method: 'POST',
				  url: restSaveUrl,
				  data:data
				})
			return request.then(handleSuccess1,handleError);
		}
		var findFragmentChildNodes = function(idNode) {
			  return $http.get('rest/fragment/get?id=' + idNode).then(function(response) {
			    var data = response.data;
			    var filteredData = [];
			    for(var i=0;i < data.length;i++){
					var node = data[i];
					if(node.nodes!=null){					
						filteredData=node.nodes;
					}
				}
			    return filteredData;
			  });
			};
		var getFragmentsByType = function(type) {
			  return $http.get(modulesUrl+'/getlist').then(function(response) {
			    var data = response.data;
			    var filteredData = [];
			    for(var i=0;i < data.length;i++){
					var node = data[i];
					if(node.type==type){
						
						filteredData.push(node);
					}
				}
			    return filteredData;
			  });
			};
		function findFragment(idNode) {
			var restUrl = 'rest/fragment/get?id=' + idNode;
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		function checkExists(idNode) {
			var restUrl = 'rest/fragment/checkexists?id=' + idNode;
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}

		var deleteFragment = function(moduleObj) {
			var request = $http({
				method:'POST',
				url: modulesUrl+'/delete',
				data:moduleObj
			  });
			  return request.then(handleSuccess,handleError);
		}
			
		var createFragment = function(moduleObj){
			var request =  $http({
				  method: 'POST',
				  url: modulesUrl+'/create',
				  data:moduleObj
				})
		    return request.then(handleSuccess1,handleError);
		};

		return {		  
			checkExists: checkExists,
			getByType: getFragmentsByType,
			get: getFragments,
		    save: save, 	
		    findFragment: findFragment,
		    findFragmentChildNodes: findFragmentChildNodes,
		    deleteFragment: deleteFragment,
		    createFragment:createFragment
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
            return( response.data );
        }
		function handleSuccess1( response ) {
            return( response );
        }
	}
	
})();