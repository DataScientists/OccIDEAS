(function() {
  angular.module('occIDEASApp.StartInterview', ['ui.router', 'ngResource'])
    .config(Config);

  Config.$inject = ['$stateProvider'];

  function Config($stateProvider) {
    $stateProvider
      .state('startInterview', {
        url: '/startInterview?id&code',
        templateUrl: 'scripts/startInterview/view/startInterviewJobCoding.html',
        controller: 'StartInterviewJobCodingCtrl',
        authenticate: false
      })
      .state('startInterviewRun', {
        url: '/startInterview/interview/:startWithReferenceNumber?jobModuleCode&employerCode',
        templateUrl: 'scripts/interviews/view/interview.html',
        controller: 'InterviewsCtrl',
        authenticate: false,
        resolve: {
          data: function(InterviewsService) {
            return InterviewsService.findModule('-1').then(function(response) {
              return response.data;
            });
          },
          updateData: function() {
            return undefined;
          },
          startWithReferenceNumber: function($stateParams) {
            return $stateParams.startWithReferenceNumber;
          },
          jobModuleCode: function($stateParams) {
            return $stateParams.jobModuleCode;
          },
          treeView: function() {
            return undefined;
          }
        }
      });
  }
})();
