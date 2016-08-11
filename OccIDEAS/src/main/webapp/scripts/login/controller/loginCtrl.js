(function() {

    angular.module('occIDEASApp.Login').controller('LoginCtrl',
            LoginCtrl);

    LoginCtrl.$inject = ['$state','toaster','$timeout',
                         '$scope','$http','$rootScope', 
                         'dataBeanService', '$window','loginService',
                         '$sessionStorage','AuthenticationService','$mdDialog'];
    function LoginCtrl($state, toaster, $timeout, 
    		$scope, $http, $rootScope, 
    		dataBeanService,$window, loginService,
    		$sessionStorage,auth,$mdDialog) {
        var vm = this;
        $scope.user = {};
        vm.userId = $sessionStorage.userId;
        vm.password = '';
        vm.hasErrMsg = false;
        vm.errMsg = '';
        vm.isAuthenticated = $sessionStorage.isAuthenticated;
        
        vm.currentPassword = '';
        vm.newPassword = '';
        vm.retypeNewPassword = '';
        
        if(!(angular.isUndefinedOrNull(vm.isAuthenticated))){
        	if(!vm.isAuthenticated){
        		vm.userId = '';
        	}        	
        }
        
        if(dataBeanService.getStatetransitionHasErr() === '1') {
            vm.hasErrMsg = true;
            vm.errMsg = 'NOT_AUTH'
        }

        vm.login = function() {

            if(!(angular.isUndefinedOrNull(vm.userId))
                    && !(angular.isUndefinedOrNull(vm.password))){
                vm.hasErrMsg = false;

                var res = loginService.getLoginResp(vm.userId, vm.password);

                res.then(function(response) {
                	var data = response.data;
                	var status = response.status;
                    if(status === 200) {

                    	$sessionStorage.userId = vm.userId;
                    	$sessionStorage.token = data.token;
                    	$sessionStorage.roles = data.userInfo.roles;
                        vm.isAuthenticated = true;

                        $sessionStorage.isAuthenticated = true;
                        if(auth.userHasPermission(['ROLE_INTERVIEWER'])){
                        	$state.go('tabs.participants');
                        }else if(auth.userHasPermission(['ROLE_CONTDEV'])){
                        	$state.go('tabs.assessments');
                        }else if(auth.userHasPermission(['ROLE_ASSESSOR'])){
                        	$state.go('tabs.modules');
                        }else if(auth.userHasPermission(['ROLE_ADMIN'])){
                        	$state.go('tabs.admin');
                        }
                        else{                       	
                        	$state.go('error',{error:"No role defined for user "+vm.userId});
                        }  
                    }else if (status === 401) {
                        vm.hasErrMsg = true;
                        vm.errMsg = 'Unauthorized (Invalid UserId/Password or user is Inactive)';
                    }else {
                        vm.hasErrMsg = true;
                        vm.errMsg = 'failure message: ' + JSON.stringify({data: data}) +' status: '+status;
                    }

                });
            }
            else{
                vm.hasErrMsg = true;
                vm.errMsg = 'LoginId / Password is required.';
            }
        };

        vm.reset = function() {
            vm.userId = '';
            vm.password = '';
            $sessionStorage.userId = null;
            $sessionStorage.token = null;
            vm.isAuthenticated = false;
            vm.LoginHasErr = false;
            vm.hasErrMsg = false;
        };
        var originatorEv;
        vm.openMenu = function($mdOpenMenu, ev) {
            originatorEv = ev;
            $mdOpenMenu(ev);
          };
          vm.showChangePasswordDialog = function(){
  			$mdDialog.show({
  				scope: $scope,  
  				preserveScope: true,
  				templateUrl : 'scripts/login/partials/changePasswordDialog.html',
  				clickOutsideToClose:false
  			});
  		}
  		
  		$scope.changePasswordBtn = function(){
  			// change password by user id
//  			$sessionStorage.userId
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
    }
})();