(function(){
	angular
	  .module('occIDEASApp.Modules',['ui.router'])
	  .config(Config)
	  .factory('RulesCache',RulesCache);
	
	Config.$inject = ['$stateProvider'];
	function Config($stateProvider){
	}
	
	RulesCache.$inject = ['$cacheFactory'];
	function RulesCache($cacheFactory){
		return $cacheFactory('rules-cache');
	}
})();