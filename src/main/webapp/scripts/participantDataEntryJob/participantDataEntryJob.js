(function() {
  angular
    .module('occIDEASApp.ParticipantDataEntryJob', ['ui.router', 'ngResource'])
    .config(Config)
    .factory('ParticipantDataEntryJobCache');

  Config.$inject = ['$stateProvider'];

  function Config($stateProvider) {
  }

})();