(function() {
  angular.module('occIDEASApp.StartInterview')
    .controller('StartInterviewJobCodingCtrl', StartInterviewJobCodingCtrl);

  StartInterviewJobCodingCtrl.$inject = [
    '$scope', '$rootScope', '$state', '$stateParams', '$sessionStorage', '$translate',
    'NodeLanguageService', 'AnzscoCoderService', 'ngToast', 'InterviewsService', 'AgentsService'
  ];

  function StartInterviewJobCodingCtrl($scope, $rootScope, $state, $stateParams, $sessionStorage, $translate,
    NodeLanguageService, AnzscoCoderService, ngToast, InterviewsService, AgentsService) {

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

    // Assessor mode only: list the study's hazards on the front screen as a testing reference.
    // Participants don't see it - naming the agents before the task questions could bias answers.
    $scope.siAssessorMode = false;
    $scope.siStudyAgentNames = [];
    InterviewsService.getStartInterviewConfig().then(function(response) {
      $scope.siAssessorMode = !!(response.data && response.data.assessorMode);
      if ($scope.siAssessorMode) {
        AgentsService.getStudyAgents().then(function(agents) {
          $scope.siStudyAgentNames = _.sortBy(_.uniq(_.map(agents, 'name')), function(name) {
            return name.toLowerCase();
          });
        });
      }
    });

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

    // Resolves an ambiguous suggestion (one whose ANZSCO code matched more than one job
    // module) to the module the participant picked from the disambiguation question.
    $scope.selectDisambiguationOption = function(option) {
      if (!$scope.selectedSuggestion) {
        return;
      }
      $scope.selectedSuggestion.moduleCode = option.moduleCode;
      $scope.selectedSuggestion.moduleId = option.moduleId;
      $scope.selectedSuggestion.moduleName = option.moduleName;
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
