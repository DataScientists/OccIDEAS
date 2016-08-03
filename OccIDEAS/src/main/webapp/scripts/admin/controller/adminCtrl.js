(function(){
	angular
	  .module('occIDEASApp.Admin')
	  .controller('AdminCtrl',AdminCtrl);
	
	AdminCtrl.$inject = ['$log','NgTableParams','$scope','$filter','AdminService','$mdDialog'];
	function AdminCtrl($log,NgTableParams,$scope,$filter,AdminService,$mdDialog){
		var self = this;
		self.newUser = {};
		self.states = [{
			name:'Active'	
		},{name:'Inactive'}];
		
		AdminService.getRoles().then(function(data){
			self.roleList = data;
		});
		
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
		
		$scope.addUser = function(newUser){
			if(angular.element("#newUserForm").input){
				if(angular.element("#newUserForm").input.$error){
					return;
				}
			}else if(angular.element("#newUserForm").roles){
				if(angular.element("#newUserForm").roles.$error){
					return;
				}
			}else if(angular.element("#newUserForm").state){
				if(angular.element("#newUserForm").state.$error){
					return;
				}
			}
			var selectedState = newUser.state.name;
			newUser.state = selectedState;
			AdminService.addUser(newUser).then(function(response){
				if(response.status == 200){
					console.log('User was successfully added');
					var profile = {
							userId:response.data.id,
							userProfileId:newUser.role
					};
					AdminService.saveUserProfile(profile).then(function(response){
						if(response.status == 200){
							self.tableParams.reload();
							$mdDialog.cancel();
						}
					});
				}else{
					$mdDialog.cancel();
				}
			});
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