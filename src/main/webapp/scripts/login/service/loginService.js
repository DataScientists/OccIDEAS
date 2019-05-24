(function() {

  angular.module('occIDEASApp.Login').service('loginService', loginService);
  loginService.$inject = ['$q', '$filter', '$timeout', '$http'];

  function loginService($q, $filter, $timeout, $http) {

    function getLoginResp(userId, password) {
      var wsUrl = "web/security/login";
      return $http({
        url: wsUrl,
        method: 'POST',
        headers: {
          'X-Auth-Username': userId,
          'X-Auth-Password': password
        }
      });
    }

    function changePassword(data) {
      var restChangePassUrl = 'web/rest/user/changePassword';
      var request = $http({
        method: 'POST',
        url: restChangePassUrl,
        data: data
      });
      return request.then(handleSuccess, handleError);
    }

    function handleError(response) {
      if(!angular.isObject(response.data) || !response.data.message) {
        return ($q.reject("An unknown error occurred."));
      }
      return ($q.reject(response.data.message));
    }

    function handleSuccess(response) {
      return (response);
    }

    return {
      getLoginResp: getLoginResp,
      changePassword: changePassword
    };

  }
})();