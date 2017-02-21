(function(){
	angular.module('occIDEASApp.Modules')
	.service('ModulesService',ModulesService);

	ModulesService.$inject = ['$http','ModulesCache'];
	function ModulesService($http,ModulesCache){
		var apiUrl = 'web/rest/';
		var modulesUrl = apiUrl + 'module';
		var apiKey = '';
		
		function getNodeNameById(idNode) {
			var restUrl = 'web/rest/module/getNodeNameById?id=' + idNode;
			
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		
		function findInterviewByModuleId(idModule) {
			var restUrl = 'web/rest/interviewintromodule/findInterviewByModuleId?id=' + idModule;
			
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		
		function getModuleFragmentByModuleId(idModule){
			var restUrl = 'web/rest/modulefragment/getModuleFragmentByModuleId?id=' + idModule;

			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		
		function getModuleIntroModuleByModuleId(idModule){
			var restUrl = 'web/rest/moduleintromodule/getModuleIntroModuleByModuleId?id=' + idModule;

			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		
		function get(idModule){
			var restUrl = 'web/rest/module/get?id=' + idModule;

			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		
		function getWithFragments(idModule){
			var restUrl = 'web/rest/moduleintromodule/getWithFragments?id=' + idModule;

			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		
		function getModuleFilterStudyAgent(idModule){
			var restUrl = 'web/rest/module/getModuleFilterStudyAgent?id=' + idModule;

			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		
		function getModuleFilterAgent(idModule,idAgent){
			var restUrl = 'web/rest/module/getModuleFilterAgent?id=' + idModule+"&idAgent="+idAgent;

			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
		
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
		
		function setActiveIntroModule(data){
			var restSaveUrl = 'web/rest/module/setActiveIntroModule';
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
		
		var copyModule = function(vo){
			var request = $http({
				method:'POST',
				url: modulesUrl+'/saveAs',
				data:vo
			  });
			  return request.then(handleSuccess,handleError);
		}
		
		var importJson = function(vo){
			var request = $http({
				method:'POST',
				url: modulesUrl+'/importJson',
				data:vo
			  });
			  return request.then(handleSuccess,handleError);
		}
		
		function getAllModulesReport(){
			var restUrl = 'web/rest/module/getAllModulesReport';

			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}

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
		    deleteModule: deleteModule,
		    copyModule:copyModule,
		    importJson:importJson,
            findInterviewByModuleId:findInterviewByModuleId,
            getModuleFragmentByModuleId:getModuleFragmentByModuleId,
            getModuleIntroModuleByModuleId:getModuleIntroModuleByModuleId,
            getWithFragments:getWithFragments,
            setActiveIntroModule:setActiveIntroModule,
            getModuleFilterStudyAgent:getModuleFilterStudyAgent,
            getModuleFilterAgent:getModuleFilterAgent,
            getAllModulesReport:getAllModulesReport,
            getNodeNameById:getNodeNameById
		};
	}
	
})();