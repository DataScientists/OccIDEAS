(function() {
	angular.module('occIDEASApp.Questions')
			.controller('QuestionsCtrl',QuestionsCtrl);

	QuestionsCtrl.$inject = [ 'data', '$scope', '$mdDialog','FragmentsService' ];
	function QuestionsCtrl(data, $scope, $mdDialog, FragmentsService) {
		var self = this;
		$scope.data = data;
		$scope.rightNav = "slideFrag";
		FragmentsService.get().then(function(val) {
			$scope.fragment = val;
		});
		$scope.toggleRight = function(){
		    if ($scope.rightNav === "slideFrag"){
		      $scope.rightNav = "";
		    }
		    else{
		      $scope.rightNav = "slideFrag";
		    }
		};
		$scope.toggle = function(scope) {
			scope.toggle();
		};

		$scope.moveLastToTheBeginning = function() {
			var a = $scope.data.pop();
			$scope.data.splice(0, 0, a);
		};
		$scope.newSubItem = function(scope) {
			var nodeData = scope.$modelValue;
			if (!nodeData.nodes) {
				nodeData.nodes = [];
			}
			if (nodeData.type == 'PossibleAnswer') {
				nodeData.nodes.push({
					idNode : nodeData.idNode * 10 + nodeData.nodes.length,
					name : "",
					description : "default",
					topNodeId : nodeData.idNode,
					type : "Question",
					nodes : []
				});
			} else if (nodeData.type == 'Question') {
				nodeData.nodes.push({
					idNode : nodeData.idNode * 10 + nodeData.nodes.length,
					name : "",
					description : "default",
					topNodeId : nodeData.idNode,
					type : "PossibleAnswer",
					nodes : []
				});
			}
		};

		$scope.collapseAll = function() {
			$scope.$broadcast('collapseAll');
		};

		$scope.expandAll = function() {
			$scope.$broadcast('expandAll');
		};

		$scope.saveEdit = function(scope) {
			$scope.safeApply(function() {
				scope.$modelValue.editEnabled = false;
			});
		};

		$scope.enable = function(scope) {
			$scope.safeApply(function() {
				scope.$modelValue.editEnabled = true;
			});
		};

		$scope.safeApply = function(fn) {
			var phase = this.$root.$$phase;
			if (phase == '$apply' || phase == '$digest') {
				if (fn && (typeof (fn) === 'function')) {
					fn();
				}
			} else {
				this.$apply(fn);
			}
		};

		$scope.menuOptions = 
			[ [ 'Show/Hide Children', function($itemScope) {
									
				var toggleChildren = function (scope) {
	        		var i, subScope,
	                nodes = scope.childNodes();
		            for (i = 0; i < nodes.length; i++) {
		              subScope = nodes[i].$childNodesScope;
		              if (subScope) {
		            	  var collapsed = !subScope.collapsed;
		            	  for (i = 0; i < nodes.length; i++) {
		    	              collapsed ? nodes[i].collapse() : nodes[i].expand();    	              
		    	            }
		              }
		            }
	              };
	              toggleChildren($itemScope);
								} 
			  ],
			  [ 'Open aJMS', function($itemScope) {	
				  					$scope.addFragmentTab($itemScope.node)
				  				} 
				  ], null, // Dividier
			  [ 'Export to JSON', function($itemScope) {
				  					$scope.items.splice($itemScope.$index, 1);
			  					} 
			  ],
			  [ 'Export to PDF', function($itemScope) {
					$scope.items.splice($itemScope.$index, 1);
				} 
			  ]
			];
		
	}
})();