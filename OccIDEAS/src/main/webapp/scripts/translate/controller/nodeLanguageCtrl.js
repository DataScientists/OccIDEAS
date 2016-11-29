(function(){
	angular.module('occIDEASApp.NodeLanguage')
		   .controller('NodeLanguageCtrl',NodeLanguageCtrl);
	NodeLanguageCtrl.$inject = ['NodeLanguageService',
	                            'ngTableParams',
	                            '$state',
	                            '$scope',
	                            '$filter',
	                            '$log',
	                            '$mdDialog',
	                            '$timeout',
	                            '$q',
	                            'ngToast'];
	function NodeLanguageCtrl(NodeLanguageService,NgTableParams,$state,$scope,$filter,
			$log,$mdDialog,$timeout,$q,
			$ngToast){
		var vm = this;
		$scope.selectLanguage = undefined;
		
		vm.showNewLanguageDialog = function(){
			$mdDialog.show({
				scope: $scope, 
				preserveScope: true,
				templateUrl : 'scripts/translate/view/createLanguage.html',
				clickOutsideToClose:false
			});
		}
		
		$scope.cancel = function() {
			$mdDialog.cancel();
		};
		
		$scope.saveNewLanguageButton = function(language,description){
			var data = {
					language:language,
					description:description
			}
			NodeLanguageService.addLanguage(data).then(function(response){
				if(response.status == '200'){
					alert("Save was successful");
					$scope.getAllLanguage();
					$scope.cancel();
				}
			})
		};
		
		$scope.$watch('selectLanguage', function(current, old) {
			vm.tableParams.reload();
        });
		
		$scope.languages = undefined;
		$scope.getAllLanguage = function(){
			NodeLanguageService.getAllLanguage().then(function(response){
				if(response.status == '200'){
					$scope.languages = response.data;
					$scope.selectLanguage = $scope.languages[0];
					safeDigest($scope.selectLanguage);
				}
			})
		};
		$scope.getAllLanguage();

		vm.tableParams = new NgTableParams(
				{
				}, 
				{	
	        getData: function(params) {
//	          if(params.filter().name || params.filter().description || params.filter().isDuplicate){	
//	        	return $filter('filter')(vm.tableParams.settings().dataset, params.filter());
//	          }
	          if(!vm.tableParams.shouldGetData){
	        	  return vm.tableParams.settings().dataset;
	          }
	          $log.info("Data getting from modules ajax ..."); 
	          if($scope.selectLanguage){
	          return  NodeLanguageService.getNodeLanguageById($scope.selectLanguage.id).then(function(data) {
	        	  $log.info("Data received from modules ajax ...");        	 
	        	  vm.originalData = angular.copy(data);
	        	  vm.tableParams.settings().dataset = data;
	        	  vm.tableParams.shouldGetData = true;
	            return data;
	          });
	          }else{
	        	  return [];
	          }
	          }
	          
	      });
		vm.tableParams.shouldGetData = true;
	    
	}
})();

