(function() {
  angular
    .module('occIDEASApp.Fragments', ['ui.router'])
    .config(Config)
    .factory('FragmentsCache');

  Config.$inject = ['$stateProvider'];

  function Config($stateProvider) {
  }

  /*FragmentsCache.$inject = ['$cacheFactory'];
  function FragmentsCache($cacheFactory){
    return $cacheFactory('fragment-cache');
  }*/
})();