(function() {
  angular
    .module('occIDEASApp.ParticipantDetails', ['ui.router', 'ngResource'])
    .config(Config)
    .factory('ParticipantDetailsCache');

  Config.$inject = ['$stateProvider'];

  function Config($stateProvider) {
  }

})();