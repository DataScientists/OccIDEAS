(function() {
  angular.module('occIDEASApp.angular.translate.service', []).service('translateService', translateService);

  translateService.$inject = ['$scope', '$rootScope', '$translate'];

  function translateService($scope, $rootScope, $translate) {

    function changeLanguage(key) {
      $translate.use(key);
    }

    return {
      changeLanguage: changeLanguage
    };
  }

})();