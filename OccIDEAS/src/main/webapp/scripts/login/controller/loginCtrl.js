(function() {

    angular.module('occIDEASApp.Login').controller('LoginCtrl',
            LoginCtrl);

    LoginCtrl.$inject = [ '$state', 'toaster', '$timeout', '$scope', '$http'  ,'$rootScope', 'dataBeanService', '$window','loginService'];
    function LoginCtrl($state, toaster, $timeout, $scope, $http, $rootScope, dataBeanService,$window, loginService) {
        var vm = this;
        $scope.user = {};
        vm.userId = '';
        vm.password = '';
        vm.hasErrMsg = false;
        vm.errMsg = '';
        vm.isAuthenticated = false;

        if(dataBeanService.getStatetransitionHasErr() === '1') {
            vm.hasErrMsg = true;
            vm.errMsg = 'NOT_AUTH'
        }

        vm.login = function() {

            if(!(angular.isUndefinedOrNull(vm.userId))
                    && !(angular.isUndefinedOrNull(vm.password))){
                vm.hasErrMsg = false;

                var res = loginService.getLoginResp(vm.userId, vm.password);

                res.success(function(data, status) {

                    if(status === 200) {

                        $window.sessionStorage.UserId = vm.userId;
                        $window.sessionStorage.UserIdToken = data.token;
                        dataBeanService.setFacRoleDDValues(data.facRoleDDValues);
                        vm.isAuthenticated = true;

                        $window.sessionStorage.ShowLogout = true;
                        $rootScope.showLogout = true;
                        $rootScope.sessionStorage = $window.sessionStorage;
                        $state.go('tabs.modules');
                    }
                    else if (status === 401) {
                        $state.go('login');
                        vm.hasErrMsg = true;
                        vm.errMsg = 'Invalid UserId/Password.';
                    }
                    else {
                        $state.go('login');
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
            $window.sessionStorage.UserId = null;
            $window.sessionStorage.UserIdToken = null;
            $window.sessionStorage.CurrFac = null;
            vm.isAuthenticated = false;
            vm.LoginHasErr = false;
            vm.hasErrMsg = false;
            $scope.FacilityDdValues = [];
            $scope.RoleDdValues = [];
        };
    }
})();