(function() {

    angular.module('occIDEASApp.Login')
        .service('AuthenticationService',AuthenticationService);

    AuthenticationService.$inject = [ '$http', '$q', '$rootScope','$window'];
    function AuthenticationService($http, $q, $rootScope,$window){
        var myData = {};
       
        function getLoginDetails(){
            var deferred = $q.defer();
            $http.post('web/security/login').then(function(response){
                myData = response.data;
                deferred.resolve(myData);
            });
            return deferred.promise;
        }

        function checkUserCredentials(userId){
            if ( !angular.isUndefinedOrNull(userId)
              && !angular.isUndefinedOrNull($window.sessionStorage.UserIdToken)) {
                    $rootScope.showLogout = true;
                    $window.sessionStorage.ShowLogout = false;
                    $rootScope.sessionStorage = $window.sessionStorage;
                    return '0';
            }
            $rootScope.showLogout = false;
            $window.sessionStorage.ShowLogout = false;
            $rootScope.sessionStorage = $window.sessionStorage;
            return '1';
        }

        return {
                getLoginDetails:getLoginDetails,
                checkUserCredentials:checkUserCredentials
        };
    }
})();