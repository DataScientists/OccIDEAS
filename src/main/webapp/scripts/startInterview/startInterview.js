(function() {
  angular.module('occIDEASApp.StartInterview', ['ui.router', 'ngResource'])
    .config(Config);

  Config.$inject = ['$stateProvider'];

  function Config($stateProvider) {
    $stateProvider
      .state('startInterview', {
        url: '/startInterview',
        templateUrl: 'scripts/startInterview/view/startInterviewForm.html',
        controller: 'StartInterviewCtrl',
        authenticate: false
      })
      .state('startInterviewRun', {
        url: '/startInterview/interview/:startWithReferenceNumber',
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
          treeView: function() {
            return undefined;
          }
        }
      });
  }
})();
