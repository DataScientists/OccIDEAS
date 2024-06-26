(function() {
  angular.module('occIDEASApp.Participants')
    .service('ParticipantsService', ParticipantsService);

  ParticipantsService.$inject = ['$http'];

  function ParticipantsService($http) {
    var apiUrl = 'web/rest/';
    var modulesUrl = apiUrl + 'participant';
    var apiKey = '';

    //Unused
    var getParticipants = function() {
      var request = $http({
        method: 'GET',
        url: modulesUrl + '/getlist'
      });
      return request.then(handleSuccess1, handleError);
    };

    var getPaginatedParticipantWithModList = function(assessmentFilter) {
      var request = $http({
        method: 'POST',
        url: modulesUrl + '/getPaginatedParticipantWithModList',
        data: assessmentFilter
      });
      return request.then(handleSuccess1, handleError);
    };

    var getPaginatedAssessmentWithModList = function(assessmentFilter) {
      var request = $http({
        method: 'POST',
        url: modulesUrl + '/getPaginatedAssessmentWithModList',
        data: assessmentFilter
      });
      return request.then(handleSuccess1, handleError);
    };

    var getPaginatedParticipantList = function(participantFilter) {
      var request = $http({
        method: 'POST',
        url: modulesUrl + '/getPaginatedParticipantList',
        data: participantFilter
      });
      return request.then(handleSuccess1, handleError);
    };

    function save(data) {
      var restSaveUrl = 'web/rest/participant/update';
      var request = $http({
        method: 'POST',
        url: restSaveUrl,
        data: data
      });
      return request.then(handleSuccess1, handleError);
    }

    function findParticipant(idParticipant) {
      var restUrl = 'web/rest/participant/get?id=' + idParticipant;
      var request = $http({
        method: 'GET',
        url: restUrl
      });
      return request.then(handleSuccess1, handleError);
    }

    function findInterviewParticipant(idParticipant) {
      var restUrl = 'web/rest/participant/getinterviewparticipant?id=' + idParticipant;
      var request = $http({
        method: 'GET',
        url: restUrl
      });
      return request.then(handleSuccess1, handleError);
    }

    //Unused
    function checkExists(reference) {
      var restUrl = 'web/rest/participant/checkexists?reference=' + reference;
      var request = $http({
        method: 'GET',
        url: restUrl
      });
      return request.then(handleSuccess, handleError);
    }

    function getByReferenceNumber(reference) {
      var restUrl = 'web/rest/participant/getByReferenceNumber?referenceNumber=' + reference;
      var request = $http({
        method: 'GET',
        url: restUrl
      });
      return request.then(handleSuccess1, handleError);
    }

    var deleteParticipant = function(p) {
      var request = $http({
        method: 'POST',
        url: modulesUrl + '/delete',
        data: p
      });
      return request.then(handleSuccess1, handleError);
    };

    var createParticipant = function(p) {
      var request = $http({
        method: 'POST',
        url: modulesUrl + '/create',
        data: p
      });
      return request.then(handleSuccess1, handleError);
    };
    var createAMRBatchParticipant = function(p) {
      var request = $http({
        method: 'POST',
        url: modulesUrl + '/createAMRBatch',
        data: p
      });
      return request.then(handleSuccess1, handleError);
    };

    //Unused
    function getNextQuestion(data) {
      var nextQ = 'web/rest/participant/nextquestion';
      var request = $http({
        method: 'POST',
        url: nextQ,
        data: data
      });
      return request.then(handleSuccess1, handleError);
    }

    function checkIfStudyAgentPreLoaded() {
      var url = 'web/rest/participant/checkIfStudyAgentPreLoaded';
      var request = $http({
        method: 'GET',
        url: url
      });
      return request.then(handleSuccess1, handleError);
    }

	  function getByReferenceNumberPrefix(referenceNumberPrefix) {
		  var restUrl = "web/rest/participant/getByReferenceNumberPrefix?referenceNumberPrefix=" + referenceNumberPrefix;
		  var request = $http({
			  method: 'GET',
			  url: restUrl
		  });
		  return request.then(handleSuccess1, handleError);
	  }

    return {
      getNextQuestion: getNextQuestion,
      checkExists: checkExists,
      getParticipants: getParticipants,
      save: save,
      findInterviewParticipant: findInterviewParticipant,
      findParticipant: findParticipant,
      deleteParticipant: deleteParticipant,
      createParticipant: createParticipant,
      getPaginatedParticipantWithModList: getPaginatedParticipantWithModList,
      getPaginatedParticipantList: getPaginatedParticipantList,
      getPaginatedAssessmentWithModList: getPaginatedAssessmentWithModList,
      getByReferenceNumber: getByReferenceNumber,
	  createAMRBatchParticipant: createAMRBatchParticipant,
	  getByReferenceNumberPrefix: getByReferenceNumberPrefix,
      checkIfStudyAgentPreLoaded: checkIfStudyAgentPreLoaded
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

    //Unused
    function handleSuccess(response) {
      return (response.data);
    }

    function handleSuccess1(response) {
      return (response);
    }
  }

})();