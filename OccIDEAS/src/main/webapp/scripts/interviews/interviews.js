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
			}).filter('qaFilter', function() {
					return function( items, deleted) {
						var filtered = [];

							if(deleted === undefined || deleted === ''){
									return items;
							}

							angular.forEach(items, function(item) {          
								if(item.deleted === deleted){
									filtered.push(item);
								}
							});

							return filtered;
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