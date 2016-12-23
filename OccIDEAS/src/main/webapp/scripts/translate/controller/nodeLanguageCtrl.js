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
	                            'ngToast',
	                            '$translate'];
	function NodeLanguageCtrl(NodeLanguageService,NgTableParams,$state,$scope,$filter,
			$log,$mdDialog,$timeout,$q,
			$ngToast,$translate){
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
//					var english = {
//						id: -1,
//						language: 'EN'
//					}
//					$scope.languages.unshift(english);
					$scope.selectLanguage = $scope.languages[0];
					safeDigest($scope.selectLanguage);
				}
			})
		};
		$scope.getAllLanguage();
		
		vm.changeNodeLanguage = function(data) {
        	$translate.refresh();
       		if(data.language == 'EN'){
       			vm.translateNode = false;
       		}else{
       			vm.translateNode = true;
       			$translate.use(data.language);
       			vm.tableParams.shouldGetData = true;
    	        vm.tableParams.reload();
       		}
       		$scope.selectLanguage = data;
        };
        
		vm.tableParams = new NgTableParams(
				{
				}, 
				{	
	        getData: function(params) {
//	          if(params.filter().name || params.filter().description || params.filter().isDuplicate){	
//	        	return $filter('filter')(vm.tableParams.settings().dataset, params.filter());
//	          }
//	          if(!vm.tableParams.shouldGetData){
//	        	  return vm.tableParams.settings().dataset;
//	          }
	          $log.info("Data getting from modules ajax ..."); 
	          if($scope.selectLanguage){
	          return  NodeLanguageService.getNodeLanguageById($scope.selectLanguage.id).then(function(response) {
	        	  $log.info("Data received from modules ajax ...");  
	        	  var data = response.data;
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
		
		vm.add = function() {
			if(!$scope.selectLanguage){
				alert("No language available.");
				return;
			}
			
	        vm.isEditing = true;
	        vm.isAdding = true;
	        
	        vm.tableParams.settings().dataset.unshift({
	        	word: "English Question/Answer",
	        	translation: "Traslated Version"
	        });
	        vm.originalData = angular.copy(vm.tableParams.settings().dataset);
	        vm.tableParams.sorting({});
	        vm.tableParams.page(1);
	        vm.tableParams.shouldGetData = false;
	        vm.tableParams.reload();
	        vm.isAdding = false;
	    }
		
		vm.save = function(row, rowForm) {
			vm.isEditing = false;
			$scope.row = row;
			$scope.rowForm = rowForm;
			row.languageId = $scope.selectLanguage.id;
			NodeLanguageService.save(row).then(function(response){
				if(response.status === 200){
					vm.tableParams.shouldGetData = true;
					alert("save was successful");
					vm.tableParams.reload();
				}
			});


		}
		
		vm.del = function(row){
	    	var data =  NodeLanguageService.deleteNodeLanguage(row).then(function(response) {
	    		if(response.status === 200){
					console.log('translation was deleted!');
					vm.tableParams.shouldGetData = true;
					vm.tableParams.reload().then(function (data) {
			            if (data.length === 0 && vm.tableParams.total() > 0) {
			            	vm.tableParams.page(vm.tableParams.page() - 1);
			            	vm.tableParams.reload();
			            }
			        });
				}
	        });
	    	_.remove(vm.tableParams.settings().dataset, function (item) {
	            return row === item;
	        });
	        self.tableParams.shouldGetData = false;
	        self.tableParams.reload().then(function (data) {
	            if (data.length === 0 && self.tableParams.total() > 0) {
	                self.tableParams.page(self.tableParams.page() - 1);
	                self.tableParams.reload();
	                if(isValidationFilterEnabled == true){
	                	$scope.validateBtn(true);
	                }
	            }
	        });
		}
		
	}
	
	
})();

