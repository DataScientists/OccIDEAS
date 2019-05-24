(function() {

    angular.module('occIDEASApp.NodeLanguage').controller('LanguageBreakdownCtrl',
    		LanguageBreakdownCtrl);

    LanguageBreakdownCtrl.$inject = ['$state','ngToast','$timeout',
                         '$scope','$http','$rootScope','$window','$sessionStorage',
                         '$mdDialog','$translate','NodeLanguageService',
                         'NgTableParams','$q','$filter','flag','type','ModulesService','FragmentsService'];
    function LanguageBreakdownCtrl($state, ngToast, $timeout, 
    		$scope, $http, $rootScope,$window, 
    		$sessionStorage,$mdDialog,
    		$translate,NodeLanguageService,NgTableParams,$q,$filter,flag,type,ModulesService,FragmentsService) {
        var vm = this;
        $scope.discriminatorType = type;
        $scope.flagUsed = 'flag-icon-'+flag.split(/[- ]+/).pop().toLowerCase();
        if($sessionStorage.languages){
        	var lang = _.find($sessionStorage.languages, function(o) { 
				return o.flag == flag; 
			});
        	$scope.flagDescription = lang.description; 
        	$scope.lang = lang;
        }
        
        $scope.openModuleLanguageByFlagIcon = function(idNode){
			var cloneLanguage = _.cloneDeep($scope.lang);
				cloneLanguage.idNode = idNode;
			$scope.openModuleLanguageTab(cloneLanguage.id,cloneLanguage);
		}
        
        $scope.openFragmentLanguageByFlagIcon = function(idNode){
			var cloneLanguage = _.cloneDeep($scope.lang);
				cloneLanguage.idNode = idNode;
			$scope.openFragmentLanguageTab(cloneLanguage.id,cloneLanguage);
		}
        
        $scope.$storage = $sessionStorage; 
        $scope.isEnabled = $sessionStorage.langEnabled;
        
		vm.languageSummaryTableParams = new NgTableParams(
				{
				},
			{
	        getData: function(params) {
	        	if(params.filter().name || params.filter().idNode){	
		        	return $filter('filter')(vm.languageSummaryTableParams.settings().dataset, params.filter());
	        	}
	        	if(type == 'M'){
	        		return ModulesService.getModuleLanguageBreakdown($scope.lang.id)
	        		.then(function(response){
		        		if(response.status == '200'){
		        			vm.languageSummaryTableParams.settings().dataset = response.data;
		        			var modulestats = response.data; 
		        			vm.totalModuleCurrentCount = _.sumBy(modulestats, function(o) { return o.current; });
		        			vm.totalModuleCount = _.sumBy(modulestats, function(o) { return o.total; });
		        			return modulestats;
		        		}
		        	});
	            }
	            
	            if(type == 'F'){
	            	return FragmentsService.getFragmentLanguageBreakdown($scope.lang.id)
	        		.then(function(response){
		        		if(response.status == '200'){
		        			vm.languageSummaryTableParams.settings().dataset = response.data;
		        			var fragmentstats = response.data; 
		        			vm.totalFragmentCurrentCount = _.sumBy(fragmentstats, function(o) { return o.current; });
		        			vm.totalFragmentCount = _.sumBy(fragmentstats, function(o) { return o.total; });
		        			return response.data;
		        		}
		        	});
	            }
	        	  
	        }
	      });
		
		
    }
})();