(function() {
	angular.module("occIDEASApp.Tabs").controller("TabsCtrl", TabsCtrl);

	TabsCtrl.$inject = ['$scope','$state'];
	function TabsCtrl($scope,$state) {
		$scope.loading = false;
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
			var state = null;
			var data = null;
			if($scope.tabOptions[current]){
				if($scope.tabOptions[current].state){
					console.log("Navigating to "+$scope.tabOptions[current].state);
					state = $scope.tabOptions[current].state;
				}else{
					state = "tabs.modules";
				}
				if($scope.tabOptions[current].data){
					console.log("with data: "+$scope.tabOptions[current].data)
					console.log("with idNode: "+$scope.tabOptions[current].data.row)
					data = $scope.tabOptions[current].data;
				}
				
			}else{
				state = "tabs.modules";
			}
			$state.go(state,data);

		});

		var tabs = [ {
			title : 'Module List',
			viewName: 'moduleView',
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
			var check = _.some( tabs, function( el ) {
			    return el.title === row.name;
			} );
			if(!check){
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
			}else{
				_.find(tabs, function(el, index){ 
					if(el.title === row.name){
						$scope.selectedIndex = index;
				    } 
				});
			}
		};
		$scope.removeTab = function(tab) {
			var index = tabs.indexOf(tab);
			tabs.splice(index, 1);
			$scope.tabOptions.splice(index, 1);
			if($scope.selectedIndex==3){
				$scope.selectedIndex=0;
			}
		};
		$scope.turnOffProgressBar = function turnOffProgressBar(){
			$scope.loading = false;
			return 'Done';
		}
	}
})();