(function() {
  angular.module('occIDEASApp.JMeter')
    .service('JMeterService', JMeterService);

  JMeterService.$inject = ['$http', '$q'];

  function JMeterService($http, $q) {
    var apiUrl = 'web/rest/';
    var endpoint = apiUrl + 'jmx';
    var getJMXLogs = function() {
      var request = $http.get(endpoint + '/list', {
        cache: false
      });
      return request.then(handleSuccess, handleError);
    };

    var exportJMeter = function(data) {
      var restDownloadReportUrl = endpoint + '/exportJMeter';
      var request = $http({
        method: 'POST',
        url: restDownloadReportUrl,
        data: data,
        ignoreLoadingBar: false
      });
      return request.then(handleSuccess, handleError);
    };

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
      getJMXLogs: getJMXLogs,
      exportJMeter: exportJMeter
    };
  }

})();