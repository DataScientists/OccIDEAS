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
        
        function checkPermissionForView(view) {
            if (!view.authenticate) {
                return true;
            }
             
            return userHasPermissionForView(view);
        };
         
         
        var userHasPermissionForView = function(view){
            if(!$sessionStorage.token){
                return false;
            }
             
            if(!view.permissions || !view.permissions.length){
                return true;
            }
             
            return userHasPermission(view.permissions);
        };
         
         
        function userHasPermission(permissions){
            var found = false;
            angular.forEach(permissions, function(permission, index){
            	 var hasRole = _.find($sessionStorage.roles,function(auth){
                 	return auth.authority == permission
                 });
            	 if(hasRole){
                  	found = true;
                    return;
                 }                      
            });
             
            return found;
        };
        
        function isLoggedIn(){
            return $sessionStorage.token != null;
        };
        

        return {
                getLoginDetails:getLoginDetails,
                checkUserCredentials:checkUserCredentials,
                checkPermissionForView:checkPermissionForView,
                isLoggedIn:isLoggedIn,
                userHasPermission:userHasPermission
        };
    }
})();