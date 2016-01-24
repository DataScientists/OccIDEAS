(function() {
	angular.module("occIDEASApp.Tabs").controller("TabsCtrl", TabsCtrl);

	TabsCtrl.$inject = ['$scope','$state'];
	function TabsCtrl($scope,$state) {
		$scope.tabOptions = [];
		$scope.tabOptions[0] = {
			state: "tabs.modules",
			data: ""
		};
		$scope.tabOptions[1] = {
			state: "tabs.fragments",
			data: ""
		};
		$scope.tabOptions[2] = {
			state: "tabs.agents",
			data: ""
		};
				
		$scope.$watch('selectedIndex', function(current, old) {
			$state.go($scope.tabOptions[current].state,$scope.tabOptions[current].data);
		});

		var tabs = [ {
			title : 'Module List'
		}, {
			title : 'Fragment List'
		}, {
			title : 'Agent List'
		}, ], selected = null, previous = null;
		$scope.tabs = tabs;
		$scope.selectedIndex = 0;

		$scope.addTab = function(row) {
			tabs.push({
				title : row.name,
				disabled : false
			});
			$scope.tabOptions.push({
				state: "tabs.questions",
				data: {row:row}
			});
			$state.go("tabs.questions",{row:row});
		};
		$scope.removeTab = function(tab) {
			var index = tabs.indexOf(tab);
			tabs.splice(index, 1);
		};
	}
})();