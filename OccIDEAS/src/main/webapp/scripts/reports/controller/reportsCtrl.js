(function(){
	angular.module('occIDEASApp.Reports')
	.controller('ReportsCtrl',ReportsCtrl);
	
	ReportsCtrl.$inject = ['ReportsService','NgTableParams','$state','data','$scope','$filter','$resource'];
	function ReportsCtrl(ReportsService,NgTableParams,$state,data,$scope,$filter,$resource){
		var self = this;
		$scope.data = data;
		$scope.$root.tabsLoading = false;
		
		$scope.getArray = function(jsonData){
			return JSON.parse(jsonData);
		}
		
	    self.tableParams = new NgTableParams(
			{	
				page: 1,            
                count: 10
            }, {	
            
			getData: function(params) {
	          if ((params.sorting().reference)||(params.sorting().idParticipant)||(params.sorting().statusDescription)){
				return $filter('orderBy')(self.tableParams.settings().dataset, params.orderBy());
		      }
	        	
	          if(params.filter().reference || params.filter().idParticipant){
	        	  if(awesIdIsValid(params.filter().reference)){
	        		  return $filter('filter')(self.tableParams.settings().dataset, params.filter()); 
	        	  }     	  
		      }
	          if(!self.tableParams.shouldGetData){
	        	  var last = params.page() * params.count();
		          return _.slice(self.tableParams.settings().dataset,last - params.count(),last);
	          }
	          return  ReportsService.getAll().then(function(response) {
	        	  if(response.status == '200'){
	        		  var data = response.data;
	        		  console.log("Data get list from get reports ajax ...");        	 
	        		  self.originalData = angular.copy(data);
		        	  self.tableParams.settings().dataset = data;
		        	  self.tableParams.shouldGetData = false;
		        	  self.tableParams.total(self.originalData.length);
		        	  var last = params.page() * params.count();
			          return _.slice(data,last - params.count(),last);
	        	  }
	          });
	        },
	      });
	    self.tableParams.shouldGetData = true;
	}
	
})();