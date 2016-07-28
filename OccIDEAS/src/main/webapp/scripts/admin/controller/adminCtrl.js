(function(){
	angular
	  .module('occIDEASApp.Admin')
	  .controller('AdminCtrl',AdminCtrl);
	
	AdminCtrl.$inject = ['$log','NgTableParams','$scope','$filter','AdminService','$mdDialog'];
	function AdminCtrl($log,NgTableParams,$scope,$filter,AdminService,$mdDialog){
		var self = this;
		self.newUser = {};
		self.showAddUserDialog = function(){
			$mdDialog.show({
				scope: $scope,  
				preserveScope: true,
				templateUrl : 'scripts/admin/partials/addNewUser.html',
				clickOutsideToClose:false
			});
		}
		
		$scope.cancel = function() {
			$mdDialog.cancel();
		};
		
		self.tableParams = new NgTableParams(
				{
				}, 
				{	
	        getData: function(params) {
	          if(params.filter().ssoId || params.filter().firstName || 
	        		  params.filter().lastName || params.filter().email
	        		  || params.filter().state || params.filter().roles){	
	        	return $filter('filter')(self.tableParams.settings().dataset, params.filter());
	          }
	          if(!self.tableParams.shouldGetData){
	        	  return self.tableParams.settings().dataset;
	          }
	          $log.info("Data getting from admin ajax ..."); 
	          return  AdminService.getUserRoles().then(function(data){
	        	  _.each(data,function(user){
	        		  user.roles = _.map(user.userProfiles, 'type').join(', ');
	        	  })
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