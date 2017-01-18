(function() {

    angular.module('occIDEASApp.Login').controller('LoginCtrl',
            LoginCtrl);

    LoginCtrl.$inject = ['$state','ngToast','$timeout',
                         '$scope','$http','$rootScope', 
                         'dataBeanService', '$window','loginService',
                         '$sessionStorage','AuthenticationService','$mdDialog',
                         'SystemPropertyService','$translate','NodeLanguageService',
                         'NgTableParams'];
    function LoginCtrl($state, ngToast, $timeout, 
    		$scope, $http, $rootScope, 
    		dataBeanService,$window, loginService,
    		$sessionStorage,auth,$mdDialog,SystemPropertyService,
    		$translate,NodeLanguageService,NgTableParams) {
        var vm = this;
        $scope.user = {};
        vm.userId = $sessionStorage.userId;
        vm.password = '';
        vm.hasErrMsg = false;
        vm.errMsg = '';
        vm.isAuthenticated = $sessionStorage.isAuthenticated;
        
        $scope.showLanguageDialog = function(){
        	$mdDialog.show({
				scope: $scope,  
				preserveScope: true,
				templateUrl : 'scripts/translate/view/languageDialog.html',
				clickOutsideToClose:false
			});
        }
        
        $scope.$storage = $sessionStorage; 
        $scope.isEnabled = $sessionStorage.langEnabled;
        $scope.saveLanguageSettings = function(){
        	if($scope.isEnabled){
        		$sessionStorage.langEnabled = true;
        		if(!$sessionStorage.languages){
        			NodeLanguageService.getAllLanguage().then(function(response){
        				if(response.status == '200'){
        					$sessionStorage.languages = response.data;
        				}
        			})
        		}
        		$rootScope.addLanguageTab();
        	}else{
        		$sessionStorage.langEnabled = false;
        		$rootScope.closeLanguageTab();
        		$translate.use('GB');
        		$translate.refresh();
        	}
        	$mdDialog.cancel();
        }
        
		
        vm.passwordVO = {};
        
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
                        
                        //Get config
                        SystemPropertyService.getByName("activeIntro").then(function(response){
                			if(response.status == '200'){
                				if(response.data){
                					$sessionStorage.activeIntro = response.data; 
                				}else{
                					ngToast.create({
          				    		  className: 'danger',
          				    		  content: 'activeIntro is not set in system property. contact admin.',
          				    		  dismissButton: true,
          			      	    	  dismissOnClick:false,
          			      	    	  animation:'slide'
                					});
                				}
                			}
                        });
                        
                        SystemPropertyService.getByName("studyidprefix").then(function(response){
                			if(response.status == '200'){
                				if(response.data){
                					$sessionStorage.awesIdPrefix = response.data.value; 
                				}else{
                					//Set default
                					$sessionStorage.awesIdPrefix = 'A';
                				}
                			}
                        });
                        
                        SystemPropertyService.getByName("studyidlength").then(function(response){
                			if(response.status == '200'){
                				if(response.data){
                					$sessionStorage.awesIdLength = response.data.value; 
                				}else{
                					//Set default
                					$sessionStorage.awesIdLength = 7;
                				}
                			}
                        });
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
                        vm.errMsg = "Hmm, that's not the right password. Please try again or email info@occideas.org to request a new one.";
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
  			if(vm.passwordVO.newPassword != vm.passwordVO.retypeNewPassword){
  				alert("New Password and Retype Password is not the same.");
  				return;
  			}
  			var passwordJson = {
  					userId:$sessionStorage.userId,
  					currentPassword:vm.passwordVO.currentPassword,
  					newPassword:vm.passwordVO.newPassword
  			}
  			loginService.changePassword(passwordJson).then(function(response){
  				if(response.status == 200){
  					alert("Password change was successful.");
  					$mdDialog.cancel();
  				}
  			});
  		}
  		
  		$scope.cancel = function() {
			$mdDialog.cancel();
		};
    }
})();