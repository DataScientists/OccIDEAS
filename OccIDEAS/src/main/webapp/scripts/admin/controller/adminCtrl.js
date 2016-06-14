(function(){
	angular
	  .module('occIDEASApp.Admin')
	  .controller('AdminCtrl',AdminCtrl);
	
	AdminCtrl.$inject = ['$log','ngTableParams','$scope','$filter','AdminService'];
	function AdminCtrl($log,NgTableParams,$scope,$filter,AdminService){
		var self = this;
		
		self.tableParams = new NgTableParams(
				{
				}, 
				{	
	        getData: function(params) {
	          if(params.filter().name || params.filter().description){	
	        	return $filter('filter')(self.tableParams.settings().dataset, params.filter());
	          }
	          if(!self.tableParams.shouldGetData){
	        	  return self.tableParams.settings().dataset;
	          }
	          $log.info("Data getting from admin ajax ..."); 
	          return  AdminService.getUserRoles().then(function(data){
                  self.originalData = angular.copy(data);
	        	  self.tableParams.settings().dataset = data;
	        	  self.tableParams.shouldGetData = true;
                  return data;
              });
	         }
	      });
		self.tableParams.shouldGetData = true;
		
	}
	
})();