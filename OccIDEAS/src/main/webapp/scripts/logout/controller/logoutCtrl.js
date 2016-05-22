(function() {

    angular.module('cams.logout.ctrl', [ 'ui.router' ]).controller('LogoutCtrl',
            LogoutCtrl);

    LogoutCtrl.$inject = ['$state', '$rootScope', 'dataBeanService','$window'];
    function LogoutCtrl($state, $rootScope, dataBeanService,$window) {
        $window.sessionStorage.camsUserId = null;
        $window.sessionStorage.camsUserIdToken = null;
        $window.sessionStorage.camsCurrFac = null;
        $rootScope.camsShowLogout = false;
        $rootScope.sessionStorage = null;
        dataBeanService.setCamsStatetransitionHasErr('0');
        $state.go('login', {}, {reload: true});
    }
})();