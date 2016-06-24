(function(){
	angular.module('occIDEASApp.Assessments',['angular-loading-bar', 'ngAnimate'])
	.config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {
		cfpLoadingBarProvider.latencyThreshold = 1000;
	}]);
})();