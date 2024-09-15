(function() {
	angular.module('occIDEASApp.ParticipantDetails')
		.service('ParticipantDetailsService', ParticipantDetailsService);

	ParticipantDetailsService.$inject = ['$http'];

	function ParticipantDetailsService($http) {
		var apiUrl = 'web/rest/';
		var participantDetailsUrl = apiUrl + 'participantDetails';

		function getParticipantDetailsByParticipantId(idParticipant) {
			var restUrl = participantDetailsUrl + "/getparticipantdetailsbyparticipantid?participantId=" + idParticipant;
			var request = $http({
				method: 'GET',
				url: restUrl
			});
			return request.then(handleSuccess1, handleError);
		}
		function deleteParticipantDetails(idParticipant,startsWith) {
            //var restUrl = participantDetailsUrl + "/deleteList?participantId="+idParticipant+"&startsWith=" + startsWith;
            var restUrl = "web/rest/participant/deleteList?participantId="+idParticipant+"&startsWith=" + startsWith;
            var request = $http({
                method: 'POST',
                url: restUrl
            });
            return request.then(handleSuccess1, handleError);
        }
		
		return {
			getParticipantDetailsByParticipantId: getParticipantDetailsByParticipantId,
			deleteParticipantDetails: deleteParticipantDetails
		};

		function handleError(response) {
			if (
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