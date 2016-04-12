(function(){
	angular
	  .module('occIDEASApp.Questions',['ui.router'])
	  .config(Config)
	  .factory('QuestionsCache',QuestionsCache);
	
	Config.$inject = ['$stateProvider','treeConfig'];
	function Config($stateProvider,treeConfig){
		 treeConfig.defaultCollapsed = false;
	}
	
	QuestionsCache.$inject = ['$cacheFactory'];
	function QuestionsCache($cacheFactory){
		return $cacheFactory('questions-cache');
	}
	
	angular
	  .module('occIDEASApp.Questions').directive('prog_tree', function() {
	    return {
	        link: function($scope, element, attrs) {
	            // Trigger when number of children changes,
	            // including by directives like ng-repeat
	            var watch = $scope.$watch(function() {
	                return element.children().length;
	            }, function() {
	                // Wait for templates to render
	                $scope.$evalAsync(function() {
	                    // Finally, directives are evaluated
	                    // and templates are renderer here
	                    var children = element.children();
	                    $scope.$root.tabsLoading = false;
	                    console.log(children);
	                });
	            });
	        },
	    };
	});
})();