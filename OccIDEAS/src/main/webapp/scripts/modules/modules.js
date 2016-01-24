(function(){
	angular
	  .module('occIDEASApp.Modules',['ui.router'])
	  .config(Config)
	  .factory('ModulesCache',ModulesCache);
	
	Config.$inject = ['$stateProvider'];
	function Config($stateProvider){
	}
	
	ModulesCache.$inject = ['$cacheFactory'];
	function ModulesCache($cacheFactory){
		return $cacheFactory('modules-cache');
	}
})();