(function(){
	angular
	  .module('occIDEASApp.Admin')
	  .controller('AdminCtrl',AdminCtrl);
	
	AdminCtrl.$inject = ['$log','NgTableParams','$scope','$filter','AdminService','$mdDialog','SystemPropertyService','ngToast'];
	function AdminCtrl($log,NgTableParams,$scope,$filter,AdminService,$mdDialog,SystemPropertyService,$ngToast){
		var self = this;
		self.isDeleting = false;
		var dirtyCellsByRow = [];
	    var invalidCellsByRow = [];
	    self.cancel = cancel;
	    self.del = del;
	    self.save = save;
	    self.add = add;
	    self.toggleIsDeleting = toggleIsDeleting;
	    self.addNewConfigBtn = addNewConfigBtn;
		
		self.states = [{
			name:'Active'	
		},{name:'Inactive'}];
		
		 function setInvalid(isInvalid) {
		        self.$invalid = isInvalid;
		        self.$valid = !isInvalid;
		      }
		
		function untrack(row) {
	        _.remove(invalidCellsByRow, function(item) {
	          return item.row === row;
	        });
	        _.remove(dirtyCellsByRow, function(item) {
	          return item.row === row;
	        });
	        setInvalid(invalidCellsByRow.length > 0);
	   }
		function del(row) {
	    	row.deleted = 1;
	    	var data =  SystemPropertyService.deleteProperty(row).then(function(response) {
	    		if(response.status === 200){
	    			$ngToast.create({
	      	    		  className: 'success',
	      	    		  content: 'System Property was deleted successfully.',
	      	    		  animation:'slide'
	      	    	 });
					self.tableConfig.shouldGetData = true;
			        self.tableConfig.reload().then(function (data) {
			            if (data.length === 0 && self.tableConfig.total() > 0) {
			                self.tableConfig.page(self.tableConfig.page() - 1);
			                self.tableConfig.reload();
			            }
			        });
				}
	        });
	    	_.remove(self.tableConfig.settings().dataset, function (item) {
	            return row === item;
	        });
	        self.tableConfig.shouldGetData = false;
	        self.tableConfig.reload().then(function (data) {
	            if (data.length === 0 && self.tableConfig.total() > 0) {
	                self.tableConfig.page(self.tableConfig.page() - 1);
	                self.tableConfig.reload();
	            }
	        });
	    }
		function toggleIsDeleting(){
	    	if(self.isDeleting){
	    		self.isDeleting = false;
	    	}else{
	    		self.isDeleting = true;
	    	}
	    }
		function addNewConfigBtn(){
			self.isEditing = true;
			self.isAdding = true;

			self.tableConfig.settings().dataset.unshift({
				name: "New Module",
				type: "New Type",
				value: "New Value",
				isEditing: true
			});
			self.originalData = angular.copy(self.tableConfig.settings().dataset);
			self.tableConfig.sorting({});
			self.tableConfig.page(1);
			self.tableConfig.shouldGetData = false;
			self.tableConfig.reload();
			self.isAdding = false;
		}
		
		function add(type) {
			self.isEditing = true;
			self.isAdding = true;

			self.tableConfig.settings().dataset.unshift({
				name: "New Module",
				type: type,
				value: "New Value",
				isEditing: true
			});
			self.originalData = angular.copy(self.tableConfig.settings().dataset);
			self.tableConfig.sorting({});
			self.tableConfig.page(1);
			self.tableConfig.shouldGetData = false;
			self.tableConfig.reload();
			self.isAdding = false;
		}
		
		function save(row, rowForm) {
	    	self.isEditing = false;
	    	SystemPropertyService.save(row).then(function(response){
				if(response.status === 200){
					$ngToast.create({
	      	    		  className: 'success',
	      	    		  content: 'System Property Save was Successful!',
	      	    		  animation:'slide'
	      	    	 });
					self.tableConfig.shouldGetData = true;
			        self.tableConfig.reload().then(function (data) {
			            if (data.length === 0 && self.tableConfig.total() > 0) {
			                self.tableConfig.page(self.tableConfig.page() - 1);
			                self.tableConfig.reload();
			            }
			        });
				}
			});
	    }
		
		 function resetRow(row, rowForm) {
		        row.isEditing = false;
		        rowForm.$setPristine();
		        self.tableTracker.untrack(row);
		        return window._.find(self.originalData,{id:row.id});
		 }
		 
		function cancel(row,rowForm) {
	    	var originalRow = resetRow(row, rowForm);
	    	if(row.id){
	    		angular.extend(row, originalRow);
	    	}else{
	    		_.remove(self.tableConfig.settings().dataset, function (item) {
		            return row === item;
		        });
	    		self.tableConfig.shouldGetData = false;
		        self.tableConfig.reload().then(function (data) {
		            if (data.length === 0 && self.tableConfig.total() > 0) {
		                self.tableConfig.page(self.tableConfig.page() - 1);
		                self.tableConfig.reload();
		            }
		        });
	    	} 
	    }
		
		self.tableConfig = new NgTableParams(
				{
					group: "type"
				}, 
				{	
	        getData: function(params) {
	          if(params.filter().name){	
	        	return $filter('filter')(self.tableConfig.settings().dataset, params.filter());
	          }
	          if(!self.tableConfig.shouldGetData){
	        	  return self.tableConfig.settings().dataset;
	          }
	          $log.info("Data getting from modules ajax ..."); 
	          return  SystemPropertyService.getAll().then(function(response){
	        	  if(response.status == '200'){
	        		$log.info("Data received from modules ajax ...");        	 
	        	    self.originalData = angular.copy(response.data);
	        	    self.tableConfig.settings().dataset = response.data;
	        	    self.tableConfig.shouldGetData = true;
	        	  	return response.data;
	        	  }
	          });
	          }
	    });
		self.tableConfig.shouldGetData = true;
		
		AdminService.getRoles().then(function(data){
			self.roleList = data;
		});
		
		SystemPropertyService.getAll().then(function(response){
			self.sysprop = response.data;
		});
		
		self.saveSysPropBtn = function(){
			SystemPropertyService.save(self.sysprop.activemodule).then(function(response){
				if(response.status == '200'){
					alert("Config changed was saved successfully.");
					self.sysprop.activemodule = response.data;
				}
			});
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
	        	
	        	if(!$scope.data){
	        		self.tableParams.shouldGetData = true;
	        	}
	        	else{
	        		var orderedFilteredDataset = $scope.data;
	        		if (params.sorting().ssoId
		           		  || params.sorting().firstName
		          		  || params.sorting().lastName
		          		  || params.sorting().email
		          		  || params.sorting().state
	        			  || params.sorting().rolesStr){
	        			
		        		self.tableParams.shouldGetData = false;
		        		orderedFilteredDataset = $filter('orderBy')($scope.data, params.orderBy());			  				
			  		}
	        		
			        if(params.filter().ssoId 
	        		  || params.filter().firstName 
	        		  || params.filter().lastName 
	        		  || params.filter().email
	        		  || params.filter().state 
	        		  || params.filter().rolesStr){	
			        	
			        	self.tableParams.shouldGetData = false;
			        	orderedFilteredDataset = $filter('filter')(orderedFilteredDataset, params.filter());			        	
			        }
			        
			        self.tableParams.settings().dataset = orderedFilteredDataset;	        		
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
	        	  $scope.data = data;
	        	  self.tableParams.settings().dataset = data;
	        	  self.tableParams.shouldGetData = true;
                  return data;
              });
	         }
	      });
		self.tableParams.shouldGetData = true;
		
	}
	
})();