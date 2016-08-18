(function(){
	angular
	  .module('occIDEASApp.Admin')
	  .controller('AdminCtrl',AdminCtrl);
	
	AdminCtrl.$inject = ['$log','NgTableParams','$scope','$filter','AdminService','$mdDialog','SystemPropertyService'];
	function AdminCtrl($log,NgTableParams,$scope,$filter,AdminService,$mdDialog,SystemPropertyService){
		var self = this;
		self.sysprop = {};
		self.states = [{
			name:'Active'	
		},{name:'Inactive'}];
		
		AdminService.getRoles().then(function(data){
			self.roleList = data;
		});
		
		SystemPropertyService.getById('activeIntro').then(function(data){
			self.sysprop.activemodule = data;
		});
		
		self.saveSysPropBtn = function(){
			alert("attempt to save system property");
		}
		
		self.showAddUserDialog = function(){
			self.newUser = {roles:[]};
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
		
		$scope.addUserBtn = function(newUser){
			if(!newUser.state){
				alert("State is a required field.");
				return;
			}
			if(newUser.roles.length == 0){
				alert("Please select a role for the user.");
				return;
			}
			
			AdminService.addUser(newUser).then(function(response){
				if(response.status == 200){
					console.log('User was successfully added');
					if(response.status == '200'){				
						// time to add the roles
						var profiles = [];
						_.each(newUser.roles,function(role){
						var profile = {
								userId:response.data.id,
								userProfileId:role.id
						};
						profiles.push(profile);
						});
						AdminService.saveUserProfileList(profiles).then(function(response){
							if(response.status == 200){
								self.tableParams.reload();
								$mdDialog.cancel();
							}
						});
						}else{
							$mdDialog.cancel();
						}
				}else{
					$mdDialog.cancel();
				}
			});
		};
		
		self.showChangePasswordDialog = function(existingUser){
			$scope.existingUser = existingUser;
			$scope.existingUser.id = existingUser.id;
			$scope.existingUser.roles = existingUser.userProfiles;
			$scope.existingUser.state = existingUser.state;
			$scope.existingUser.password = existingUser.password;
			$scope.existingUser.previousPassword = existingUser.password;
			$mdDialog.show({
				scope: $scope,  
				preserveScope: true,
				templateUrl : 'scripts/admin/partials/changePasswordDialog.html',
				clickOutsideToClose:false
			});
		}
		
		$scope.changePasswordBtn = function(existingUser){
			if($scope.existingUser.previousPassword == existingUser.newPassword){
				alert("No changes on the password.");
				return;
			}
			$scope.existingUser.password = existingUser.newPassword;
			AdminService.updatePassword(existingUser).then(function(response){
				if(response.status == 200){
					console.log('User was successfully updated');
					self.tableParams.reload();
				}
				$mdDialog.cancel();
			});
		}
		
		self.showEditUserDialog = function(existingUser){
			$scope.existingUser = existingUser;
			$scope.existingUser.id = existingUser.id
			$scope.existingUser.roles = existingUser.userProfiles
			$mdDialog.show({
				scope: $scope,  
				preserveScope: true,
				templateUrl : 'scripts/admin/partials/editUser.html',
				clickOutsideToClose:false
			});
		}
		
		$scope.editUserBtn = function(existingUser){
			if(!existingUser.state){
				alert("State is a required field.");
				return;
			}
			if(existingUser.roles.length == 0){
				alert("Please select a role for the user.");
				return;
			}
			
			AdminService.updateUser(existingUser).then(function(response){
				if(response.status == 200){
					var userId = response.data.id;
					console.log('User was successfully added');
					// delete all roles for user id
					AdminService.deleteUserProfile(userId)
								.then(function(response){
					
					if(response.status == '200'){				
					console.log("Delete user in role map was successful");	
					// time to add the roles
					var profiles = [];
					_.each(existingUser.roles,function(role){
					var profile = {
							userId:userId,
							userProfileId:role.id
					};
					profiles.push(profile);
					});
					AdminService.saveUserProfileList(profiles).then(function(response){
						if(response.status == 200){
							self.tableParams.reload();
							$mdDialog.cancel();
						}
					});
					}else{
						$mdDialog.cancel();
					}
					});
				}else{
					$mdDialog.cancel();
				}
				$mdDialog.cancel();
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
	        	  data = _.uniqBy(data,'ssoId');
	        	  _.each(data,function(user){
	        		  user.rolesStr = _.map(user.userProfiles, 'type').join(', ');
	        	  })
	        	  self.tableParams.settings().dataset = data;
	        	  self.tableParams.shouldGetData = true;
                  return data;
              });
	         }
	      });
		self.tableParams.shouldGetData = true;
		
	}
	
})();