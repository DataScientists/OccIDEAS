(function() {
  angular
    .module('occIDEASApp.Participants', ['ui.router', 'ngResource'])
    .config(Config)
    .factory('ParticipantsCache');

  Config.$inject = ['$stateProvider'];

  function Config($stateProvider) {
  }

  /*FragmentsCache.$inject = ['$cacheFactory'];
  function FragmentsCache($cacheFactory){
    return $cacheFactory('fragment-cache');
  }*/
})();