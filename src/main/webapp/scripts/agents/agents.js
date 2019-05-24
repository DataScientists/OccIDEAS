(function() {
  angular
    .module('occIDEASApp.Agents', ['ui.router'])
    .config(Config);
//	  .factory('AgentsCache',AgentsCache);

  Config.$inject = ['$stateProvider'];

  function Config($stateProvider) {
  }

//	AgentsCache.$inject = ['$cacheFactory'];
//	function AgentsCache($cacheFactory){
//		return $cacheFactory('agents-cache');
//	}
})();