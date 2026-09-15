(function() {
  angular.module('occIDEASApp.StartInterview')
    .service('AnzscoCoderService', AnzscoCoderService);

  AnzscoCoderService.$inject = ['$http', '$q'];

  function AnzscoCoderService($http, $q) {
    var url = 'web/rest/anzscocoder';

    function lookup(jobTitle, jobDescription) {
      var restUrl = url + '/lookup';
      var request = $http({
        method: 'POST',
        url: restUrl,
        data: {
          jobTitle: jobTitle,
          jobDescription: jobDescription
        }
      });
      return request.then(handleSuccess, handleError);
    }

    function handleError(response) {
      if (angular.isString(response.data) && response.data) {
        return $q.reject(response.data);
      }
      return $q.reject('Unable to determine ANZSCO code for this job.');
    }

    function handleSuccess(response) {
      return response;
    }

    return {
      lookup: lookup
    };
  }
})();
