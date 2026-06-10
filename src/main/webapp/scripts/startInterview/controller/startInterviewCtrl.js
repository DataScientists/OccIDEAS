(function() {
  angular.module('occIDEASApp.StartInterview')
    .controller('StartInterviewCtrl', StartInterviewCtrl);

  StartInterviewCtrl.$inject = [
    '$scope', '$rootScope', '$state', '$sessionStorage', '$translate',
    'InterviewsService', 'NodeLanguageService', 'ngToast'
  ];

  function StartInterviewCtrl($scope, $rootScope, $state, $sessionStorage, $translate,
    InterviewsService, NodeLanguageService, ngToast) {

    $scope.$storage = $sessionStorage;
    $scope.awesIdMaxSize = $sessionStorage.awesIdLength;
    $scope.awesIdPrefix = $sessionStorage.awesIdPrefix;
    $scope.awesIdSize = 0;
    $scope.searchAWESID = '';
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

      InterviewsService.checkReferenceNumberExists($scope.searchAWESID).then(function(response) {
        if (response.status == 200) {
          if (confirm("This Participant ID has already been used. Would you like to add a duplicate?")) {
            $state.go('startInterviewRun', { startWithReferenceNumber: $scope.searchAWESID });
          }
        } else if (response.status == 204) {
          $state.go('startInterviewRun', { startWithReferenceNumber: $scope.searchAWESID });
        } else {
          ngToast.create({
            className: 'danger',
            content: 'Error occurred during ID check.',
            animation: 'slide'
          });
        }
      });
    };

    function isValidAwesId(awesId) {
      return awesId && awesId.length === $scope.awesIdMaxSize;
    }
  }
})();
