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
					$scope.cancel();
				}
			})
		};
//		self.isDeleting = false;
//		var dirtyCellsByRow = [];
//	    var invalidCellsByRow = [];
//	    $scope.$root.tabsLoading = false;
	    
	    
	    
	}
})();

