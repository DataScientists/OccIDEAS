(function() {

    angular.module('occIDEASApp.Login')
        .service('AuthenticationService',AuthenticationService);

    AuthenticationService.$inject = [ '$http', '$q', '$rootScope','$window','$sessionStorage'];
    function AuthenticationService($http, $q, $rootScope,$window,$sessionStorage){
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
              && !angular.isUndefinedOrNull($sessionStorage.token)) {
            		$sessionStorage.showLogout = true;
                    $rootScope.showLogout = $sessionStorage.showLogout;
                    return '0';
            }
            $sessionStorage.showLogout = false;
            $rootScope.showLogout = $sessionStorage.showLogout;
            return '1';
        }
        
        

        return {
                getLoginDetails:getLoginDetails,
                checkUserCredentials:checkUserCredentials
        };
    }
})();