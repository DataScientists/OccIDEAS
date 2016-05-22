(function(){

    angular.module('cams.login').service('loginService',loginService);
    loginService.$inject = [ '$q', '$filter', '$timeout','$http'];

    function loginService($q, $filter, $timeout,$http){

        function getLoginResp(userId, password){
            var wsUrl = "/web/security/login";
            return $http({
                url: wsUrl,
                method: 'POST',
                headers:{
                    'X-Auth-Username':userId,
                    'X-Auth-Password':password
                }
            });
        }

        return {
            getLoginResp:getLoginResp
        };

    }
})();