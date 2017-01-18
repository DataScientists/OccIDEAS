(function() {

    angular.module('occIDEASApp.NodeLanguage').controller('LanguageSummaryCtrl',
    		LanguageSummaryCtrl);

    LanguageSummaryCtrl.$inject = ['$state','ngToast','$timeout',
                         '$scope','$http','$rootScope','$window','$sessionStorage',
                         '$mdDialog','$translate','NodeLanguageService',
                         'NgTableParams','$q'];
    function LanguageSummaryCtrl($state, ngToast, $timeout, 
    		$scope, $http, $rootScope,$window, 
    		$sessionStorage,$mdDialog,
    		$translate,NodeLanguageService,NgTableParams,$q) {
        var vm = this;

        $scope.$storage = $sessionStorage; 
        $scope.isEnabled = $sessionStorage.langEnabled;
        
        $scope.openModuleBreakdown = function(flag){
        	$rootScope.addLanguageBreakdownTab(flag,'M');
        }
        
        $scope.openFragmentBreakdown = function(flag){
        	$rootScope.addLanguageBreakdownTab(flag,'F');
        }
        
        // For Modules
        $scope.getAllLanguage = function(){
        	 NodeLanguageService.getNodeNodeLanguageList().then(function(response){
 		    	if(response.status == '200'){
 		    		$scope.nodeNodeLanguageMap = response.data;
 		    		$scope.flags = _.uniqBy($scope.nodeNodeLanguageMap,'flag');
				}
			})
		};
		$scope.getAllLanguage();
		
		// For Fragments
		$scope.getAllFragmentLanguage = function(){
        	 NodeLanguageService.getNodeNodeLanguageFragmentList().then(function(response){
 		    	if(response.status == '200'){
 		    		$scope.nodeNodeLanguageFragmentMap = response.data;
 		    		$scope.flags = _.uniqBy($scope.nodeNodeLanguageFragmentMap,'flag');
				}
			})
		};
		$scope.getAllFragmentLanguage();
		
		$scope.selectLanguage = {};
		$scope.selectLanguage.selected ='';
		vm.languageTableParams =  new NgTableParams(
				{
				}, 
			{	
	        getData: function() {
	        	  // For Modules
	        	  var data = _.filter($scope.nodeNodeLanguageMap,function(nodeMap){
	        		  return nodeMap.languageId == $scope.selectLanguage.selected.languageId;
	        	  });
	        	  $scope.totalCurrent =_.sumBy(data, function(o) { return o.current; }); 
	        	  
	        	  // For Fragments
	        	  var dataFrag = _.filter($scope.nodeNodeLanguageFragmentMap,function(nodeMap){
	        		  return nodeMap.languageId == $scope.selectLanguage.selected.languageId;
	        	  });
	        	  $scope.totalFragCurrent =_.sumBy(dataFrag, function(o) { return o.current; });
	        	  
	        	  vm.originalData = angular.copy(data);
	        	  vm.languageTableParams.settings().dataset = data;
	        	  vm.languageTableParams.shouldGetData = true;
	        	  
	        	  return data;
	        }
	      });
		vm.languageTableParams.shouldGetData = true;
        
		vm.languageSummaryTableParams = new NgTableParams(
				{
				}, 
			{	
	        getData: function() {
	        	return NodeLanguageService.getAllLanguage().then(function(response){
	        		if(response.status == '200'){
	        			var promises = [];
	        			$scope.lang = response.data;
	        			_.remove($scope.lang, function (l) {
	        				  return l.language == 'GB'; 
	        			});
	        			_.each($scope.lang,function(l){
	        				promises.push(NodeLanguageService.getUntranslatedModules(l.flag)
	        						.then(function(resp){
	        							if(resp.status == '200'){
	        								var data = _.filter($scope.nodeNodeLanguageMap,function(nodeMap){
	        					        		  return nodeMap.languageId == l.id;
	        					        	});
	        					        	l.totalCurrent =_.sumBy(data, function(o) { return o.current; });
	        								l.translatedModuleCount = resp.data;
	        							}
	        						}));
	        						promises.push(NodeLanguageService.getUntranslatedFragments(l.flag)
	        						.then(function(resp){
	        							if(resp.status == '200'){
	        								var data = _.filter($scope.nodeNodeLanguageFragmentMap,function(nodeMap){
	        					        		  return nodeMap.languageId == l.id;
	        					        	});
	        					        	l.totalFragCurrent =_.sumBy(data, function(o) { return o.current; });
	        								l.translatedFragCount = resp.data;
	        							}
	        						}));
	        			});
	        			$q.all(promises).then(function () {
	        				vm.languageSummaryTableParams.settings().dataset = $scope.lang;
	        				return $scope.lang;
	        			});
	        		}
	        	});  
	        }
	      });
		
		
		// get all untranslated nodes
		NodeLanguageService.getTotalUntranslatedModule().then(function(response){
			if(response.status == '200'){
				$scope.grandTotal = response.data;
			}
		});
		
		// get total module count
		$scope.totalModuleCount = 0;
		NodeLanguageService.getTotalModuleCount().then(function(response){
			if(response.status == '200'){
				$scope.totalModuleCount = response.data;
			}
		});
		
		// get all untranslated fragment nodes
		NodeLanguageService.getTotalUntranslatedFragment().then(function(response){
			if(response.status == '200'){
				$scope.grandFragTotal = response.data;
			}
		});
		
		// get total fragment count
		$scope.totalFragCount = 0;
		NodeLanguageService.getTotalFragmentCount().then(function(response){
			if(response.status == '200'){
				$scope.totalFragCount = response.data;
			}
		});
		
			
    }
})();