(function() {
	angular.module('occIDEASApp.Interviews', [ 'ui.router' ]).config(Config)
			.factory('InterviewsCache', InterviewsCache).factory(
					'focus',
					function($timeout, $window) {
						return function(elem) {
							$timeout(function() {
								if (elem)
									elem.focus();
							});
						};
					}).directive('eventFocus', function(focus) {
						return function(scope, elem, attr) {
							focus(elem[0]);
							scope.$on('$destroy', function() {
							});
						};
			});

	Config.$inject = [ '$stateProvider' ];
	function Config($stateProvider) {
	}

	InterviewsCache.$inject = [ '$cacheFactory' ];
	function InterviewsCache($cacheFactory) {
		return $cacheFactory('interviews-cache');
	}
})();