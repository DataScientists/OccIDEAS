(function() {
  angular.module('occIDEASApp.Notes')
    .service('NotesService', NotesService);

  NotesService.$inject = ['$http'];

  function NotesService($http) {
    var apiUrl = 'web/rest/';
    var notesUrl = apiUrl + 'note';

    function getNotesByInterviewId(idInterview) {
      var restUrl = notesUrl + "/getlistbyinterview?interviewId=" + idInterview;
      var request = $http({
        method: 'GET',
        url: restUrl
      });
      return request.then(handleSuccess1, handleError);
    }

    return {
      getNotesByInterviewId: getNotesByInterviewId
    };

    function handleError(response) {
      if(
        !angular.isObject(response.data) ||
        !response.data.message
      ) {
        return ($q.reject("An unknown error occurred."));
      }
      return ($q.reject(response.data.message));
    }

    function handleSuccess(response) {
      return (response.data);
    }

    function handleSuccess1(response) {
      return (response);
    }
  }

})();