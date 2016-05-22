(function() {

    angular.module('cams.login').controller('LoginCtrl',
            LoginCtrl);

    LoginCtrl.$inject = [ '$state', 'toaster', '$timeout', '$scope', '$http'  ,'$rootScope', 'dataBeanService', '$locale', '$window', '$translate','loginService'];
    function LoginCtrl($state, toaster, $timeout, $scope, $http, $rootScope, dataBeanService, $locale, $window, $translate, loginService) {
        var vm = this;
        $scope.user = {};
        vm.userId = '';
        vm.password = '';
        vm.hasErrMsg = false;
        vm.errMsg = '';
        vm.isAuthenticated = false;

        if(dataBeanService.getCamsStatetransitionHasErr() === '1') {
            vm.hasErrMsg = true;
            vm.errMsg = $translate.instant('NOT_AUTH');
        }

        vm.login = function() {

            if(!(angular.isUndefinedOrNull(vm.userId))
                    && !(angular.isUndefinedOrNull(vm.password))){
                vm.hasErrMsg = false;

                var res = loginService.getLoginResp(vm.userId, vm.password);

                res.success(function(data, status) {

                    if(status === 200) {

                        $window.sessionStorage.camsUserId = vm.userId;
                        $window.sessionStorage.camsUserIdToken = data.token;
                        dataBeanService.setFacRoleDDValues(data.facRoleDDValues);
                        vm.isAuthenticated = true;

                        $window.sessionStorage.camsShowLogout = true;
                        $rootScope.showLogout = true;
                        $rootScope.sessionStorage = $window.sessionStorage;
                        $state.go('leftNav.main');
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
            $window.sessionStorage.camsUserId = null;
            $window.sessionStorage.camsUserIdToken = null;
            $window.sessionStorage.camsCurrFac = null;
            vm.isAuthenticated = false;
            vm.camsLoginHasErr = false;
            vm.hasErrMsg = false;
            $scope.FacilityDdValues = [];
            $scope.RoleDdValues = [];
        };
    }
})();