(function() {
  angular.module('occIDEASApp.Modules').service('ModulesService',
    ModulesService);

  ModulesService.$inject = ['$http', 'ModulesCache'];

  function ModulesService($http, ModulesCache) {
    var apiUrl = 'web/rest/';
    var modulesUrl = apiUrl + 'module';
    var apiKey = '';

    function exportToVoxco(idNode) {
      var restUrl = 'web/rest/module/exportToVoxco?id=' + idNode
      var request = $http({
        method: 'GET',
        url: restUrl
      });
      return request.then(handleSuccess, handleError);
    }

    function convertModuleToApplicationQSF(idNode,filterStudyAgent) {
        var restUrl = 'web/rest/module/convertModuleToApplicationQSF?id=' + idNode+'&filter='+filterStudyAgent;

        var request = $http({
          method: 'GET',
          url: restUrl
        });
        return request.then(handleSuccess, handleError);
      }

    function exportQSFResponse(idNode) {
      var restSaveUrl = 'web/rest/module/exportQSFResponse?id=' + idNode;
      var request = $http({
        method: 'GET',
        url: restSaveUrl
      });
      return request.then(handleSuccess, handleError);
    }
    
    function getModuleById(idNode) {
      var restUrl = 'web/rest/module/getModuleById?id=' + idNode;

      var request = $http({
        method: 'GET',
        url: restUrl
      });
      return request.then(handleSuccess, handleError);
    }

    function getNodeById(idNode) {
      var restUrl = 'web/rest/module/getNodeById?id=' + idNode;

      var request = $http({
        method: 'GET',
        url: restUrl
      });
      return request.then(handleSuccess, handleError);
    }

    function getNodeNameById(idNode) {
      var restUrl = 'web/rest/module/getNodeNameById?id=' + idNode;

      var request = $http({
        method: 'GET',
        url: restUrl
      });
      return request.then(handleSuccess, handleError);
    }

    function findInterviewByModuleId(idModule) {
      var restUrl = 'web/rest/interviewintromodule/findInterviewByModuleId?id='
        + idModule;

      var request = $http({
        method: 'GET',
        url: restUrl
      });
      return request.then(handleSuccess, handleError);
    }

    function getModuleFragmentByModuleId(idModule) {
      var restUrl = 'web/rest/modulefragment/getModuleFragmentByModuleId?id='
        + idModule;

      var request = $http({
        method: 'GET',
        url: restUrl
      });
      return request.then(handleSuccess, handleError);
    }

    function getModuleIntroModuleByModuleId(idModule) {
      var restUrl = 'web/rest/moduleintromodule/getModuleIntroModuleByModuleId?id='
        + idModule;

      var request = $http({
        method: 'GET',
        url: restUrl
      });
      return request.then(handleSuccess, handleError);
    }

    function get(idModule) {
      var restUrl = 'web/rest/module/get?id=' + idModule;

      var request = $http({
        method: 'GET',
        url: restUrl
      });
      return request.then(handleSuccess, handleError);
    }

    function getWithFragments(idModule) {
      var restUrl = 'web/rest/moduleintromodule/getWithFragments?id='
        + idModule;

      var request = $http({
        method: 'GET',
        url: restUrl
      });
      return request.then(handleSuccess, handleError);
    }

    function getModuleFilterStudyAgent(idModule) {
      var restUrl = 'web/rest/module/getModuleFilterStudyAgent?id='
        + idModule;

      var request = $http({
        method: 'GET',
        url: restUrl
      });
      return request.then(handleSuccess, handleError);
    }

    function getModuleFilterAgent(idModule, idAgent) {
      var restUrl = 'web/rest/module/getModuleFilterAgent?id=' + idModule
        + "&idAgent=" + idAgent;

      var request = $http({
        method: 'GET',
        url: restUrl
      });
      return request.then(handleSuccess, handleError);
    }

    var getModules = function() {
      return $http.get(modulesUrl + '/getlist').then(function(response) {
        var data = response.data;
        return data;
      });
    };

    var getActiveModules = function() {
      return $http.get(modulesUrl + '/getlist').then(function(response) {
        var data = response.data;
        var filteredData = [];
        for(var i = 0; i < data.length; i++) {
          var node = data[i];
          if(node.type == 'M_Module') {
            filteredData.push(node);
          }
        }
        return filteredData;
      });
    };

    var postNewModule = function(moduleObj) {
      return $http.post(modulesUrl + '?apiKey=' + apiKey).then(
        function(response) {
          console.log(response.data.id);
        });
    };

    function save(data) {
      var restSaveUrl = 'web/rest/module/update';
      var request = $http({
        method: 'POST',
        url: restSaveUrl,
        data: data
      });
      return request.then(handleSuccess, handleError);
    }

    function setActiveIntroModule(data) {
      var restSaveUrl = 'web/rest/module/setActiveIntroModule';
      var request = $http({
        method: 'POST',
        url: restSaveUrl,
        data: data
      });
      return request.then(handleSuccess, handleError);
    }

    var deleteModule = function(moduleObj) {
      var request = $http({
        method: 'POST',
        url: modulesUrl + '/delete',
        data: moduleObj
      });
      return request.then(handleSuccess, handleError);
    };

    var copyModule = function(vo) {
      var request = $http({
        method: 'POST',
        url: modulesUrl + '/saveAs',
        data: vo
      });
      return request.then(handleSuccess, handleError);
    };

    var importJson = function(vo) {
      var request = $http({
        method: 'POST',
        url: modulesUrl + '/importJson',
        data: vo
      });
      return request.then(handleSuccess, handleError);
    };

    function getAllModulesReport() {
      var restUrl = 'web/rest/module/getAllModulesReport';

      var request = $http({
        method: 'GET',
        url: restUrl
      });
      return request.then(handleSuccess, handleError);
    }

    function getModuleTranslationTotalCount(idNode) {
      var restUrl = 'web/rest/module/getModuleTranslationTotalCount?id='
        + idNode;

      var request = $http({
        method: 'GET',
        url: restUrl
      });
      return request.then(handleSuccess, handleError);
    }

    function getModuleTranslationCurrentCount(idNode, languageId) {
      var restUrl = 'web/rest/module/getModuleTranslationCurrentCount?id='
        + idNode + '&languageId=' + languageId;

      var request = $http({
        method: 'GET',
        url: restUrl
      });
      return request.then(handleSuccess, handleError);
    }

    function getModuleLanguageBreakdown(languageId) {
      var restUrl = 'web/rest/module/getModuleLanguageBreakdown?languageId='
        + languageId;

      var request = $http({
        method: 'GET',
        url: restUrl
      });
      return request.then(handleSuccess, handleError);
    }

    function getTotalUntranslatedModule(languageId) {
      var restUrl = 'web/rest/module/getTotalUntranslatedModule?languageId='
        + languageId;

      var request = $http({
        method: 'GET',
        url: restUrl
      });
      return request.then(handleSuccess, handleError);
    }

    function getTotalTranslatedNodeByLanguage(languageId) {
      var restUrl = 'web/rest/module/getTotalTranslatedNodeByLanguage?languageId='
        + languageId;

      var request = $http({
        method: 'GET',
        url: restUrl
      });
      return request.then(handleSuccess, handleError);
    }

    function getModulesWithTranslationCount(languageId) {
      var restUrl = 'web/rest/module/getModulesWithTranslationCount?languageId='
        + languageId;

      var request = $http({
        method: 'GET',
        url: restUrl
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
      getActiveModules: getActiveModules,
      get: getModules,
      save: save,
      post: postNewModule,
      deleteModule: deleteModule,
      copyModule: copyModule,
      importJson: importJson,
      findInterviewByModuleId: findInterviewByModuleId,
      getModuleFragmentByModuleId: getModuleFragmentByModuleId,
      getModuleIntroModuleByModuleId: getModuleIntroModuleByModuleId,
      getWithFragments: getWithFragments,
      setActiveIntroModule: setActiveIntroModule,
      getModuleFilterStudyAgent: getModuleFilterStudyAgent,
      getModuleFilterAgent: getModuleFilterAgent,
      getAllModulesReport: getAllModulesReport,
      getNodeNameById: getNodeNameById,
      getModuleTranslationTotalCount: getModuleTranslationTotalCount,
      getModuleTranslationCurrentCount: getModuleTranslationCurrentCount,
      getModuleLanguageBreakdown: getModuleLanguageBreakdown,
      getTotalUntranslatedModule: getTotalUntranslatedModule,
      getTotalTranslatedNodeByLanguage: getTotalTranslatedNodeByLanguage,
      getModulesWithTranslationCount: getModulesWithTranslationCount,
      getModuleById: getModuleById,
      getNodeById: getNodeById,
      convertModuleToApplicationQSF:convertModuleToApplicationQSF,
      exportQSFResponse:exportQSFResponse,
      exportToVoxco: exportToVoxco
    };
  }

})();