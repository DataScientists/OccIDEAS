(function() {
  angular.module('occIDEASApp.ParticipantDetails')
    .service('ParticipantDetailsService', ParticipantDetailsService);

  ParticipantDetailsService.$inject = ['$http'];

  function ParticipantDetailsService($http) {
    var apiUrl = 'web/rest/';
    var participantDetailsUrl = apiUrl + 'participantDetails';

    function getParticipantDetailsByParticipantId(idParticipant) {
      var restUrl = notesUrl + "/getparticipantdetailsbyparticipantid?participantId=" + idParticipant;
      var request = $http({
        method: 'GET',
        url: restUrl
      });
      return request.then(handleSuccess1, handleError);
    }

    return {
      getParticipantDetailsByParticipantId: getParticipantDetailsByParticipantId
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