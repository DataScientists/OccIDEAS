(function() {
	angular.module('occIDEASApp.Questions')
			.controller('QuestionsCtrl',QuestionsCtrl);

	QuestionsCtrl.$inject = [ 'data', '$scope', '$mdDialog','FragmentsService' ];
	function QuestionsCtrl(data, $scope, $mdDialog, FragmentsService) {
		var self = this;
		$scope.data = data;		
		$scope.treeOptions = {
				beforeDrop:function(event){
					var sourceNode = event.source.nodeScope.node;
					sourceNode.warning = null;
					var destNode = event.dest.nodesScope.node;
					console.log("source"+sourceNode.type);
					if(!destNode){
						sourceNode.warning = 'warning';						
					}else{
						console.log("dest "+destNode.type);
						if(stringStartsWith(sourceNode.type,'Q')){
							if(stringStartsWith(destNode.type,'Q')){
								console.log("dropped Q on Q")
								sourceNode.warning = 'warning';
							}			
						}else if(stringStartsWith(sourceNode.type,'P')){
							if(stringStartsWith(destNode.type,'P')){
								console.log("Droped P on P")
								sourceNode.warning = 'warning';
							}			
						}
					}
					
					function stringStartsWith (string, prefix) {
					    return string.slice(0, prefix.length) == prefix;
					}
				},
				beforeDrag: function(sourceNodeScope){
					if(sourceNodeScope.node.type === 'M_Module'){					
						return false;
					}else{
						return true;
					}
				}
		}
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
		$scope.remove = function(scope) {
			scope.remove();
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
			if (nodeData.type == 'P_simple') {
				nodeData.nodes.push({
					idNode : nodeData.idNode * 10 + nodeData.nodes.length,
					name : "New Question",
					description : "default",
					topNodeId : nodeData.idNode,
					type : "Q_simple",
					nodeclass : "Q",
					nodes : []
				});
			} else if (nodeData.type == 'Q_single') {
				nodeData.nodes.push({
					idNode : nodeData.idNode * 10 + nodeData.nodes.length,
					name : "New Possible Answer",
					topNodeId : nodeData.idNode,
					type : "P_single",
					nodeclass : "P",
					nodes : []
				});
			}else if (nodeData.type == 'Q_multiple') {
				nodeData.nodes.push({
					idNode : nodeData.idNode * 10 + nodeData.nodes.length,
					name : "New Multi Possible Answer",
					topNodeId : nodeData.idNode,
					type : "P_multiple",
					nodeclass : "P",
					nodes : []
				});
			}else if (nodeData.type == 'Q_simple') {
				nodeData.nodes.push({
					idNode : nodeData.idNode * 10 + nodeData.nodes.length,
					name : "New Possible Answer",
					topNodeId : nodeData.idNode,
					type : "P_single",
					nodeclass : "P",
					nodes : []
				});
			}else if (nodeData.type == 'M_Module') {
				nodeData.nodes.push({
					idNode : nodeData.idNode * 10 + nodeData.nodes.length,
					name : "New Question",
					topNodeId : nodeData.idNode,
					type : "Q_simple",
					nodeclass : "Q",
					nodes : []
				});
			}else{
				var nodeData = scope.$modelValue;
		        nodeData.nodes.push({
		          id: nodeData.idNode * 10 + nodeData.nodes.length,
		          name: "new node",
		          topNodeId : nodeData.idNode,
				  type : "default",
				  warning: "warning",
		          nodes: []
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

		$scope.showMenu = function(scope) {
			if(scope.node.nodeclass=='M'){
				return $scope.moduleMenuOptions;
			}else if(scope.node.nodeclass=='Q'){
				return $scope.questionMenuOptions;
			}else if(scope.node.nodeclass=='P'){
				return $scope.possibleAnswerMenuOptions;
			}
		};
		$scope.moduleMenuOptions = 
			[ [ 'Add Question', function($itemScope) {
						$scope.newSubItem($itemScope)
						}
			  ],		  
			  [ 'Show/Hide Children', function($itemScope) {
					
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
		
		$scope.questionMenuOptions = 
			[ [ 'Add Possible Answer', function($itemScope) {
						$scope.newSubItem($itemScope)
						}
			  ],
			  [ 'Remove', function($itemScope) {
					$scope.remove($itemScope)
					}
			  ],
			  [ 'Show/Hide Children', function($itemScope) {
					
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
			  [ 'Open as aJMS', function($itemScope) {	
				  					$scope.addFragmentTab($itemScope.node)
				  				} 
			  ]
			];
		$scope.possibleAnswerMenuOptions = 
			[ [ 'Add Quesiton', function($itemScope) {
						$scope.newSubItem($itemScope)
						}
			  ],
			  [ 'Remove', function($itemScope) {
					$scope.remove($itemScope)
					}
			  ],
			  [ 'Show/Hide Children', function($itemScope) {
					
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
			   ]
			];
		
	}
})();