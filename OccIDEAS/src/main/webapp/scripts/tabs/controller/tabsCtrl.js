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
			console.log("Navigating to "+$scope.tabOptions[current].state);
			console.log("with data: "+$scope.tabOptions[current].data)
			$state.go($scope.tabOptions[current].state,$scope.tabOptions[current].data);
		});

		var tabs = [ {
			title : 'Module List',
			viewName: 'moduleView'
		}, {
			title : 'Fragment List',
			viewName: 'fragmentView'
		},
		{
			title : 'Agent List',
			viewName: 'agentView'
		}], selected = null, previous = null;
		$scope.tabs = tabs;
		$scope.selectedIndex = 0;

		$scope.addFragmentTab = function(row) {
			tabs.push({
				title : row.name,
				viewName: 'getfragmentView',
				canClose: true,
				disabled : false
			});
			$scope.tabOptions.push({
				state: "tabs.fragment",
				data: {row:row.idNode}
			});
		};
		$scope.addModuleTab = function(row) {
			tabs.push({
				title : row.name,
				viewName: 'questionsView',
				canClose: true,
				disabled : false
			});
			$scope.tabOptions.push({
				state: "tabs.questions",
				data: {row:row.idNode}
			});
		};
		$scope.removeTab = function(tab) {
			var index = tabs.indexOf(tab);
			tabs.splice(index, 1);
		};
	}
})();