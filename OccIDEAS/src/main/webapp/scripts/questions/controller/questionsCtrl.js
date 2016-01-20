(function(){
	angular.module('occIDEASApp.Questions')
	.controller('QuestionsCtrl',QuestionsCtrl);
	
	QuestionsCtrl.$inject = ['data','$scope'];
	function QuestionsCtrl(data,$scope){
		var vm = this;
		vm.data = data;
		
		$scope.remove = function (scope) {
	        scope.remove();
	      };

	      $scope.toggle = function (scope) {
	        scope.toggle();
	      };

	      $scope.moveLastToTheBeginning = function () {
	        var a = $scope.data.pop();
	        $scope.data.splice(0, 0, a);
	      };

	      $scope.newSubItem = function (scope) {
	        var nodeData = scope.$modelValue;
	        if(!nodeData.nodes){
	        	nodeData.nodes = [];
			}
	        nodeData.nodes.push({
	          idNode: nodeData.idNode * 10 + nodeData.nodes.length,
	          name: "",
	          description: "default",
	          topNodeId: nodeData.idNode,
	          type: "Module",
	          nodes: []
	        });
	      };

	      $scope.collapseAll = function () {
	        $scope.$broadcast('collapseAll');
	      };

	      $scope.expandAll = function () {
	        $scope.$broadcast('expandAll');
	      };
	}
})();