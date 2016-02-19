(function() {
	angular.module("occIDEASApp.Tabs").controller("TabsCtrl", TabsCtrl);

	TabsCtrl.$inject = ['$scope','$state','$rootScope'];
	function TabsCtrl($scope,$state,$rootScope) {
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
		$scope.questionsCount = 0;
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
			viewName: 'modules@tabs',
		}, {
			title : 'Fragment List',
			viewName: 'fragments@tabs'
		},
		{
			title : 'Agent List',
			viewName: 'agents@tabs'
		}], selected = null, previous = null;
		$scope.tabs = tabs;
		$scope.selectedIndex = 0;

		$scope.addFragmentTab = function(row) {
			
			tabs.push({
				title : row.name,
				viewName: 'fragments@tabs',
				canClose: true,
				disabled : false
			});
			$scope.tabOptions.push({
				state: "tabs.fragment",
				data: {row:row.idNode}
			});
			
		};
		$scope.addModuleTab = function(row) {
			$rootScope.questionsLoading = true;
			var check = _.some( tabs, function( el ) {
			    return el.title === row.name;
			} );
			if(!check && ($scope.questionsCount < 3)){
			tabs.push({
				title : row.name,
				viewName: 'questionsView',
				canClose: true,
				disabled : false
			});
			var questionState = '';
			if($scope.questionsCount > 0){
				questionState = $scope.questionsCount;
			}
			$scope.tabOptions.push({
				state: "tabs.questions"+questionState,
				data: {row:row.idNode}
			});
			$scope.questionsCount++;
			}
			else if($scope.questionsCount == 2){
				$rootScope.questionsLoading = false;
				alert("Unable to have more than 3 questions tab opened at the same time.");
			}
			else{
				_.find(tabs, function(el, index){ 
					if(el.title === row.name){
						$scope.selectedIndex = index;
						$rootScope.questionsLoading = false;
				    } 
				});
			}
		};
        $scope.addRulesTab = function(scope) {
            var nodeData = scope.$modelValue;
            var tabTitle = "Rules "+nodeData.name;
            var check = _.some( tabs, function( el ) {
                return el.title === tabTitle;
            } );
            if(!check){
                tabs.push({
                    title : tabTitle,
                    viewName: 'rules@tabs',
                    canClose: true,
                    disabled : false
                });
                $scope.tabOptions.push({
                    state: "tabs.rules",
                    data: {row:nodeData.idNode}
                });
            }else{
                _.find(tabs, function(el, index){
                    if(el.title === tabTitle){
                        $scope.selectedIndex = index;
                    }
                });
            }
        };
        $scope.addInterviewTab = function(scope) {
        	$rootScope.questionsLoading = true;
            var nodeData = scope.$modelValue;
            var tabTitle = "Interview "+nodeData.name;
            var check = _.some( tabs, function( el ) {
                return el.title === tabTitle;
            } );
            if(!check){
                tabs.push({
                    title : tabTitle,
                    viewName: 'interview@tabs',
                    canClose: true,
                    disabled : false
                });
                $scope.tabOptions.push({
                    state: "tabs.interview",
                    data: {row:nodeData.idNode}
                });
            }else{
                _.find(tabs, function(el, index){
                    if(el.title === tabTitle){
                        $scope.selectedIndex = index;
                        $rootScope.questionsLoading = false;
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
			$scope.questionsCount--;
		};
		$scope.turnOffProgressBar = function turnOffProgressBar(){
			$scope.loading = false;
			return 'Done';
		}
	}
})();