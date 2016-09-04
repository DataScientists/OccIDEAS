(function(){
	angular.module('occIDEASApp.Fragments')
	.service('FragmentsService',FragmentsService);

	FragmentsService.$inject = ['$http'];
	function FragmentsService($http){
		var apiUrl = 'web/rest/';
		var modulesUrl = apiUrl + 'fragment';
		var apiKey = '';
		
		function findInterviewByFragmentId(id) {
			var restUrl = 'web/rest/interviewmodulefragment/findInterviewByFragmentId?id=' + id;
			
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess1,handleError);
		}
		
		var getFragments = function() {
		  return $http.get(modulesUrl+'/getlist').then(function(response) {
		    var data = response.data;
		    //FragmentsCache.put("all",data);
		    return data;
		  });
		};
		function save(data){
			var restSaveUrl = 'web/rest/fragment/update';
			var request =  $http({
				  method: 'POST',
				  url: restSaveUrl,
				  data:data
				})
			return request.then(handleSuccess1,handleError);
		}
		var findFragmentChildNodes = function(idNode) {
			  return $http.get('web/rest/fragment/get?id=' + idNode).then(function(response) {
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
			var restUrl = 'web/rest/fragment/get?id=' + idNode;
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		function checkExists(idNode) {
			var restUrl = 'web/rest/fragment/checkexists?id=' + idNode;
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
		
		var copyModule = function(vo){
			var request = $http({
				method:'POST',
				url:'web/rest/fragment/saveAs',
				data:vo
			  });
			  return request.then(handleSuccess1,handleError);
		}

		return {		  
			checkExists: checkExists,
			getByType: getFragmentsByType,
			get: getFragments,
		    save: save, 	
		    findFragment: findFragment,
		    findFragmentChildNodes: findFragmentChildNodes,
		    deleteFragment: deleteFragment,
		    createFragment:createFragment,
		    findInterviewByFragmentId:findInterviewByFragmentId,
		    copyModule:copyModule
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