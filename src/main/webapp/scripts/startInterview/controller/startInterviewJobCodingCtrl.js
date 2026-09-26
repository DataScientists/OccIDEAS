(function() {
  angular.module('occIDEASApp.StartInterview')
    .controller('StartInterviewJobCodingCtrl', StartInterviewJobCodingCtrl);

  StartInterviewJobCodingCtrl.$inject = [
    '$scope', '$rootScope', '$state', '$stateParams', '$sessionStorage', '$translate',
    'NodeLanguageService', 'AnzscoCoderService', 'ngToast', 'InterviewsService', 'AgentsService',
    'ParticipantsService', '$q'
  ];

  function StartInterviewJobCodingCtrl($scope, $rootScope, $state, $stateParams, $sessionStorage, $translate,
    NodeLanguageService, AnzscoCoderService, ngToast, InterviewsService, AgentsService,
    ParticipantsService, $q) {

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
    // Occupation matching that doesn't lead to a questionnaire - either the ABS Coder found no
    // match, or none of its matches map to an OccIDEAS job module. The first time we ask the
    // participant to refine their details; after that we end politely rather than let them
    // self-pick a questionnaire, which could give a misleading exposure estimate.
    $scope.noMatchAttempts = 0;
    $scope.noMatchShown = false;
    $scope.notCovered = false;
    // Optional employer code - typed in, or supplied by the employer's link via ?code=. A valid one
    // becomes the participant's reference prefix (e.g. ACMEM00042) instead of the default.
    $scope.employerCode = $stateParams.code || '';
    $scope.employerCodeStatus = null; // null (not checked) | 'valid' | 'invalid'
    // A valid code also needs the participant to tick that their employer may see their answers and
    // will be told if their estimated exposure is above an exposure limit - without it the code isn't passed on at all.
    // Object (not a bare boolean) so the checkbox inside the template's ng-if child scope writes back here.
    $scope.consent = {employerSharing: false};

    $scope.employerCodeChanged = function() {
      $scope.employerCodeStatus = null;
      $scope.consent.employerSharing = false;
    };

    // Resolves true when the code is blank (it's optional) or valid, false when it isn't recognised.
    $scope.checkEmployerCode = function() {
      var code = ($scope.employerCode || '').trim();
      if (!code) {
        $scope.employerCodeStatus = null;
        return $q.when(true);
      }
      if ($scope.employerCodeStatus) {
        return $q.when($scope.employerCodeStatus === 'valid');
      }
      return ParticipantsService.checkEmployerCode(code).then(function(response) {
        var valid = !!(response.data && response.data.valid);
        if (valid) {
          $scope.employerCode = response.data.code;
        }
        $scope.employerCodeStatus = valid ? 'valid' : 'invalid';
        return valid;
      }, function() {
        $scope.employerCodeStatus = 'invalid';
        return false;
      });
    };

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

    if ($scope.employerCode) {
      $scope.checkEmployerCode();
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

      $scope.checkEmployerCode().then(function(codeOk) {
        if (!codeOk) {
          ngToast.create({
            className: 'danger',
            content: 'Employer code not recognised - please check it, or leave it blank',
            animation: 'slide'
          });
          return;
        }
        if ($scope.employerCodeStatus === 'valid' && !$scope.consent.employerSharing) {
          ngToast.create({
            className: 'danger',
            content: 'Please tick the box to confirm you understand what your employer will see, or remove the employer code',
            animation: 'slide'
          });
          return;
        }
        lookupAnzscoCode();
      });
    };

    function lookupAnzscoCode() {
      $scope.isLooking = true;
      $scope.suggestions = null;
      $scope.selectedSuggestion = null;

      AnzscoCoderService.lookup($scope.jobTitle, $scope.jobDescription).then(function(response) {
        $scope.isLooking = false;
        // Only offer matches that lead to a questionnaire.
        var usable = _.filter((response.data && response.data.suggestions) || [], hasQuestionnaire);
        if (usable.length > 0) {
          $scope.noMatchShown = false;
          $scope.suggestions = usable;
          $scope.selectedSuggestion = usable[0];
          return;
        }
        $scope.noMatchAttempts++;
        if ($scope.noMatchAttempts >= 2) {
          $scope.notCovered = true;
        } else {
          $scope.noMatchShown = true;
        }
      }, function(errorMessage) {
        $scope.isLooking = false;
        ngToast.create({
          className: 'danger',
          content: errorMessage,
          animation: 'slide'
        });
      });
    }

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

    // Editing the job title or description makes any shown match stale - clear it so the
    // participant re-runs the match rather than continuing with the old one.
    $scope.clearMatch = function() {
      $scope.suggestions = null;
      $scope.selectedSuggestion = null;
    };

    $scope.continueToInterview = function() {
      $state.go('startInterviewRun', {
        startWithReferenceNumber: $scope.startWithReferenceNumber,
        jobModuleCode: ($scope.selectedSuggestion && $scope.selectedSuggestion.moduleCode) || null,
        employerCode: ($scope.employerCodeStatus === 'valid' && $scope.consent.employerSharing) ? $scope.employerCode : null
      });
    };

    function hasQuestionnaire(suggestion) {
      return !!suggestion.moduleName ||
        !!(suggestion.disambiguationOptions && suggestion.disambiguationOptions.length > 0);
    }

    function isValidJobTitle() {
      return !!($scope.jobTitle && $scope.jobTitle.trim().length > 0);
    }
  }
})();
