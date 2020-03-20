(function() {
  angular.module('occIDEASApp.Admin')
    .service('AdminService', AdminService);

  AdminService.$inject = ['$http', '$q'];

  function AdminService($http, $q) {
    var apiUrl = 'web/rest/';
    var adminEndpoint = apiUrl + 'admin';

    var getUserRoles = function() {
      return $http.get(adminEndpoint + '/getUserRoles', {
        cache: false
      }).then(function(response) {
        var data = response.data;
        return data;
      });
    };

    var addUser = function(data) {
      var restSaveUrl = adminEndpoint + '/addUser';
      var request = $http({
        method: 'POST',
        url: restSaveUrl,
        data: data
      });
      return request.then(handleSuccess, handleError);
    };

    var importLibrary = function(data) {
      var restSaveUrl = adminEndpoint + '/importLibrary';
      var request = $http({
        method: 'POST',
        url: restSaveUrl,
        data: data
      });
      return request.then(handleSuccess, handleError);
    };

    var updateUser = function(data) {
      var restSaveUrl = adminEndpoint + '/updateUser';
      var request = $http({
        method: 'POST',
        url: restSaveUrl,
        data: data
      });
      return request.then(handleSuccess, handleError);
    };

    var updatePassword = function(data) {
      var restSaveUrl = adminEndpoint + '/updatePassword';
      var request = $http({
        method: 'POST',
        url: restSaveUrl,
        data: data
      });
      return request.then(handleSuccess, handleError);
    };

    var saveUserProfile = function(data) {
      var restSaveUrl = adminEndpoint + '/saveUserProfile';
      var request = $http({
        method: 'POST',
        url: restSaveUrl,
        data: data
      });
      return request.then(handleSuccess, handleError);
    };

    var saveUserProfileList = function(data) {
      var restSaveUrl = adminEndpoint + '/saveUserProfileList';
      var request = $http({
        method: 'POST',
        url: restSaveUrl,
        data: data
      });
      return request.then(handleSuccess, handleError);
    };

    var deleteUserProfile = function(id) {
      var restSaveUrl = adminEndpoint + '/deleteUserProfile';
      var request = $http({
        method: 'POST',
        url: restSaveUrl,
        data: id
      });
      return request.then(handleSuccess, handleError);
    };

    var getRoles = function() {
      return $http.get(adminEndpoint + '/getRoles', {
        cache: true
      }).then(function(response) {
        var data = response.data;
        return data;
      });
    };

    var cleanOrphans = function() {
      return $http.get(adminEndpoint + '/cleanOrphans', {
        cache: false
      }).then(function(response) {
        return response;
      });
    };

    var purgeParticipants = function() {
      return $http.get(adminEndpoint + '/purgeParticipants', {
        cache: false
      }).then(function(response) {
        return response;
      });
    };

    var purgeModule = function() {
      return $http.get(adminEndpoint + '/purgeModule', {
        cache: false
      }).then(function(response) {
        return response;
      });
    };

    var deleteQSFSurveys = function(){
      return $http.get(adminEndpoint + '/deleteQSFSurveys', {
        cache: false
      }).then(function(response) {
        return response;
      });
    }

    var importQSFResponses = function(){
      return $http.get(adminEndpoint + '/importQSFResponses', {
        cache: false
      }).then(function(response) {
        return response;
      });
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
      getUserRoles: getUserRoles,
      addUser: addUser,
      getRoles: getRoles,
      saveUserProfile: saveUserProfile,
      deleteUserProfile: deleteUserProfile,
      updateUser: updateUser,
      saveUserProfileList: saveUserProfileList,
      updatePassword: updatePassword,
      cleanOrphans: cleanOrphans,
      purgeParticipants: purgeParticipants,
      purgeModule: purgeModule,
      importLibrary: importLibrary,
      deleteQSFSurveys: deleteQSFSurveys,
      importQSFResponses: importQSFResponses
    };
  }

})();