(function() {
  angular
    .module('occIDEASApp.ParticipantDataEntry', ['ui.router', 'ngResource'])
    .config(Config)
    .factory('ParticipantDataEntryCache');

  Config.$inject = ['$stateProvider'];

  function Config($stateProvider) {
  }

})();