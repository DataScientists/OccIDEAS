(function() {

    angular.module('occIDEASApp.Logout').controller('LogoutCtrl',
            LogoutCtrl);

    LogoutCtrl.$inject = ['$state', '$rootScope', 'dataBeanService','$window','$sessionStorage'];
    function LogoutCtrl($state, $rootScope, dataBeanService,$window,$sessionStorage) {
    	delete $sessionStorage.userId;
    	delete $sessionStorage.token;
    	$sessionStorage.showLogout = false;
    	$rootScope.showLogout = $sessionStorage.showLogout;
        dataBeanService.setStatetransitionHasErr('0');
        $state.go('login', {}, {reload: true});
    }
})();