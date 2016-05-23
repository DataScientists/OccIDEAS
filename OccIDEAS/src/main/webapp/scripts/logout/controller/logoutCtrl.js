(function() {

    angular.module('occIDEASApp.Logout').controller('LogoutCtrl',
            LogoutCtrl);

    LogoutCtrl.$inject = ['$state', '$rootScope', 'dataBeanService','$window'];
    function LogoutCtrl($state, $rootScope, dataBeanService,$window) {
        $window.sessionStorage.UserId = null;
        $window.sessionStorage.UserIdToken = null;
        $window.sessionStorage.CurrFac = null;
        $rootScope.showLogout = false;
        $rootScope.sessionStorage = null;
        dataBeanService.setStatetransitionHasErr('0');
        $state.go('login', {}, {reload: true});
    }
})();