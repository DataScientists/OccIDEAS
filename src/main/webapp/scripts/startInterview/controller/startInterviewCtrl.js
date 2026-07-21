(function() {
  angular.module('occIDEASApp.StartInterview')
    .controller('StartInterviewCtrl', StartInterviewCtrl);

  StartInterviewCtrl.$inject = [
    '$scope', '$rootScope', '$state', '$stateParams', '$sessionStorage', '$translate',
    'NodeLanguageService', 'ngToast'
  ];

  function StartInterviewCtrl($scope, $rootScope, $state, $stateParams, $sessionStorage, $translate,
    NodeLanguageService, ngToast) {

    $scope.$storage = $sessionStorage;
    $scope.awesIdMaxSize = 5;
    $scope.awesIdPrefix = "T";
    $scope.awesIdSize = 0;
    $scope.searchAWESID = $stateParams.id || '';
    $scope.externalMode = !!$stateParams.id;
    $scope.selectLanguage = {};

    // Stub on $rootScope so InterviewsCtrl can find it via scope chain
    if (!$rootScope.addInterviewTabInterviewers) {
      $rootScope.addInterviewTabInterviewers = function() {};
    }

    if ($scope.$storage.langEnabled) {
      $translate.refresh();
      loadLanguages();
    }

    function loadLanguages() {
      NodeLanguageService.getNodeNodeLanguageList().then(function(response) {
        if (response.status == '200') {
          $scope.languages = [];
          var nodeLanguageCopy = _.uniqBy(angular.copy(response.data), 'flag');
          if ($sessionStorage.languages) {
            _.each(nodeLanguageCopy, function(nl) {
              var langToPush = _.find($sessionStorage.languages, function(o) {
                return o.flag == nl.flag;
              });
              $scope.languages.push(langToPush);
            });
          }
          $scope.selectLanguage = {};
        }
      });
    }

    $scope.changeNodeLanguage = function(data) {
      if (data && data.selected) {
        $translate.refresh();
        $translate.use(data.selected.language);
      }
    };

    $scope.$watch('searchAWESID', function(value) {
      $scope.awesIdSize = value ? value.length : 0;
    });

    $scope.filterAndValidate = function(event) {
      if (event.which === 13) {
        $scope.add($scope.selectLanguage);
      }
    };

    $scope.add = function(selectLanguage) {
      if (!isValidAwesId($scope.searchAWESID)) {
        ngToast.create({
          className: 'danger',
          content: 'You need to add a valid Study ID before you can start',
          animation: 'slide'
        });
        return;
      }

      if ($scope.$storage.langEnabled && selectLanguage && !selectLanguage.selected) {
        $translate.refresh();
        $translate.use('GB');
      }

      $state.go('startInterviewRun', { startWithReferenceNumber: $scope.searchAWESID });
    };

    function isValidAwesId(awesId) {
      return !!(awesId && awesId.trim().length > 0);
    }

    if ($scope.externalMode) {
      $scope.add($scope.selectLanguage);
    }
  }
})();
