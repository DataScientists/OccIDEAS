(function() {

    angular.module('occIDEASApp.NodeLanguage').controller('LanguageSummaryCtrl',
    		LanguageSummaryCtrl);

    LanguageSummaryCtrl.$inject = ['$state','ngToast','$timeout',
                         '$scope','$http','$rootScope','$window','$sessionStorage',
                         '$mdDialog','$translate','NodeLanguageService',
                         'NgTableParams'];
    function LanguageSummaryCtrl($state, ngToast, $timeout, 
    		$scope, $http, $rootScope,$window, 
    		$sessionStorage,$mdDialog,
    		$translate,NodeLanguageService,NgTableParams) {
        var vm = this;

        $scope.$storage = $sessionStorage; 
        $scope.isEnabled = $sessionStorage.langEnabled;
        
        $scope.getAllLanguage = function(){
        	 NodeLanguageService.getNodeNodeLanguageList().then(function(response){
 		    	if(response.status == '200'){
 		    		$scope.nodeNodeLanguageMap = response.data;
 		    		$scope.flags = _.uniqBy($scope.nodeNodeLanguageMap,'flag');
				}
			})
		};
		$scope.getAllLanguage();
		
		$scope.selectLanguage = {};
		$scope.selectLanguage.selected ='';
		vm.languageTableParams =  new NgTableParams(
				{
				}, 
			{	
	        getData: function() {
	        	  var data = _.filter($scope.nodeNodeLanguageMap,function(nodeMap){
	        		  return nodeMap.languageId == $scope.selectLanguage.selected.languageId;
	        	  });
	        	  $scope.totalCurrent =_.sumBy(data, function(o) { return o.current; }); 
	        	  
	        	  vm.originalData = angular.copy(data);
	        	  vm.languageTableParams.settings().dataset = data;
	        	  vm.languageTableParams.shouldGetData = true;
	        	  
	        	  return data;
	        }
	      });
		vm.languageTableParams.shouldGetData = true;
        
		NodeLanguageService.getTotalUntranslatedModule().then(function(response){
		    	if(response.status == '200'){
		    		$scope.grandTotal = response.data;
				}
			  });
		
		vm.changeLanguage = function(){
			NodeLanguageService.getUntranslatedModules($scope.selectLanguage.selected.flag).then(function(response){
	   		    if(response.status == '200'){
	   		    	$scope.untranslatedModules = response.data;
	  			}
	  		});
			vm.languageTableParams.reload();
		}
    }
})();