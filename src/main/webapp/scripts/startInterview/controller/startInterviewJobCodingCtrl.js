(function() {
  angular.module('occIDEASApp.StartInterview')
    .controller('StartInterviewJobCodingCtrl', StartInterviewJobCodingCtrl);

  StartInterviewJobCodingCtrl.$inject = [
    '$scope', '$rootScope', '$state', '$stateParams', '$sessionStorage', '$translate',
    'NodeLanguageService', 'AnzscoCoderService', 'ngToast'
  ];

  function StartInterviewJobCodingCtrl($scope, $rootScope, $state, $stateParams, $sessionStorage, $translate,
    NodeLanguageService, AnzscoCoderService, ngToast) {

    $scope.$storage = $sessionStorage;
    // Participants are anonymous - no email/ID is collected. An external system embedding
    // this page can still supply its own reference number via ?id=, which is honoured as-is;
    // otherwise the backend auto-assigns an incrementing participant id as the reference.
    $scope.startWithReferenceNumber = $stateParams.id || '';
    $scope.selectLanguage = {};
    $scope.jobTitle = '';
    $scope.jobDescription = '';
    $scope.suggestions = null;
    $scope.selectedSuggestion = null;
    $scope.isLooking = false;

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

    $scope.findAnzscoCode = function() {
      if (!isValidJobTitle()) {
        ngToast.create({
          className: 'danger',
          content: 'Please enter a job title before continuing',
          animation: 'slide'
        });
        return;
      }

      if ($scope.$storage.langEnabled && $scope.selectLanguage && !$scope.selectLanguage.selected) {
        $translate.refresh();
        $translate.use('GB');
      }

      $scope.isLooking = true;
      $scope.suggestions = null;
      $scope.selectedSuggestion = null;

      AnzscoCoderService.lookup($scope.jobTitle, $scope.jobDescription).then(function(response) {
        $scope.isLooking = false;
        $scope.suggestions = (response.data && response.data.suggestions) || [];
        if ($scope.suggestions.length > 0) {
          $scope.selectedSuggestion = $scope.suggestions[0];
        } else {
          ngToast.create({
            className: 'warning',
            content: 'No ANZSCO code could be matched. Try adding more detail to the description.',
            animation: 'slide'
          });
        }
      }, function(errorMessage) {
        $scope.isLooking = false;
        ngToast.create({
          className: 'danger',
          content: errorMessage,
          animation: 'slide'
        });
      });
    };

    $scope.selectSuggestion = function(suggestion) {
      $scope.selectedSuggestion = suggestion;
    };

    $scope.goBack = function() {
      $scope.suggestions = null;
      $scope.selectedSuggestion = null;
    };

    $scope.continueToInterview = function() {
      $state.go('startInterviewRun', {
        startWithReferenceNumber: $scope.startWithReferenceNumber,
        jobModuleCode: ($scope.selectedSuggestion && $scope.selectedSuggestion.moduleCode) || null
      });
    };

    function isValidJobTitle() {
      return !!($scope.jobTitle && $scope.jobTitle.trim().length > 0);
    }
  }
})();
