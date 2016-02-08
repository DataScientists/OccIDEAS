(function(){
	angular
	  .module('occIDEASApp.Interviews',['ui.router'])
	  .config(Config)
	  .factory('InterviewsCache',InterviewsCache);
	
	Config.$inject = ['$stateProvider'];
	function Config($stateProvider){
	}
	
	InterviewsCache.$inject = ['$cacheFactory'];
	function InterviewsCache($cacheFactory){
		return $cacheFactory('interviews-cache');
	}
})();