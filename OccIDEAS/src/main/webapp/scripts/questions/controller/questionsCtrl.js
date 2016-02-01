(function() {
	angular.module('occIDEASApp.Questions')
			.controller('QuestionsCtrl',QuestionsCtrl);

	QuestionsCtrl.$inject = [ 'data', '$scope', '$mdDialog','FragmentsService','$q','QuestionsService','ModulesService'];
	function QuestionsCtrl(data, $scope, $mdDialog, FragmentsService,$q,QuestionsService,ModulesService) {
		var self = this;
		$scope.data = data;	
		$scope.isDragging = false;
		$scope.showFragmentSlider = true;
		$scope.showModuleSlider = false;
		$scope.aJsmTreeOptions = {
				accept: function(sourceNodeScope, destNodesScope, destIndex) {
					//var sourceNode = sourceNodeScope.node;
				      return true;
				    },
				beforeDrop:function(event){
					var sourceNode = event.source.nodeScope.node;
					var destNode = event.dest.nodesScope.node;
					console.log("source"+sourceNode.type);
					if(!destNode){
						return false;						
					}else{
						if (!sourceNode.nodes) {
							sourceNode.nodes = [];
						}
						destNode.nodes.unshift({
								idNode : sourceNode.idNode * 10 + sourceNode.nodes.length,
								name : sourceNode.name,
								description : sourceNode.description,
								topNodeId : sourceNode.idNode,
								type : sourceNode.type,
								nodeclass : sourceNode.nodeclass,
								link : sourceNode.idNode,
								nodes : []
						});
						return false;
					}
					
				}
		}
		$scope.templateTreeOptions = {
				accept: function(sourceNodeScope, destNodesScope, destIndex) {
					//var sourceNode = sourceNodeScope.node;
				      return true;
				    },
				beforeDrop:function(event){
					var sourceNode = event.source.nodeScope.node;
					var destNode = event.dest.nodesScope.node;
					console.log("source"+sourceNode.type);
					if(!destNode){
						sourceNode.warning = 'warning';						
					}
					if (!destNode.nodes) {
						destNode.nodes = [];
					}
					$scope.isDragging = false;
					var deferred = $q.defer();
					
					return FragmentsService.findFragmentChildNodes(sourceNode.idNode).then(function(fragmentData) {						
						destNode.nodes.unshift({
							idNode : fragmentData[0].idNode * 10 + fragmentData[0].nodes.length,
							name : fragmentData[0].name,
							description : fragmentData[0].description,
							topNodeId : fragmentData[0].idNode,
							type : fragmentData[0].type,
							nodeclass : fragmentData[0].nodeclass,
							nodes : fragmentData[0].nodes
						});
						deferred.resolve();
						return false;
					});
				},
				beforeDrag: function(sourceNodeScope){
					$scope.isDragging = true;	
					
					return true;
				}
		}
		$scope.moduleTreeOptions = {
				accept: function(sourceNodeScope, destNodesScope, destIndex) {
					var sourceNode = sourceNodeScope.node;
				      return true;
				    },
				beforeDrop:function(event){
					var sourceNode = event.source.nodeScope.node;
					sourceNode.warning = null;
					var destNode = event.dest.nodesScope.node;
					console.log("source"+sourceNode.type);
					if(!destNode){
						sourceNode.warning = 'warning';						
					}else{
						console.log("dest "+destNode.type);
						if(sourceNode.nodeclass=='Q'){
							if(destNode.nodeclass=='Q'){
								console.log("dropped Q on Q")
								sourceNode.warning = 'warning';
							}			
						}else if(sourceNode.nodeclass=='P'){
							if(destNode.nodeclass=='P'){
								console.log("Droped P on P")
								sourceNode.warning = 'warning';
							}else if(destNode.nodeclass=='M'){
								console.log("Droped P on M")
								sourceNode.warning = 'warning';			
							}			
						}
					}
					$scope.isDragging = false;
				},
				beforeDrag: function(sourceNodeScope){
					$scope.isDragging = true;
					if(sourceNodeScope.node.type === 'M_Module'){					
						return false;
					}else{
						return true;
					}
				}
		}
		ModulesService.getActiveModules().then(function(data) {	
			for(var i=0;i < data.length;i++){
				var node = data[i];
				node.type = "Q_linkedmodule";
				node.nodeclass = "Q";
			}
			$scope.moduleSlider = data;
		});
		$scope.rightNav = "slideFrag";
		FragmentsService.getByType('F_ajsm').then(function(data) {	
			for(var i=0;i < data.length;i++){
				var node = data[i];
				node.type = "Q_linkedajsm";
				node.nodeclass = "Q";
			}
			$scope.aJsmFragmentSlider = data;
		});
		FragmentsService.getByType('F_template').then(function(data) {	
			for(var i=0;i < data.length;i++){
				var node = data[i];
				node.type = "Q_linkedtemplate";
				node.nodeclass = "Q";
			}
			$scope.templateFragmentSlider = data;
		});
		FragmentsService.getByType('F_frequency').then(function(data) {		
			$scope.frequencyFragmentSlider = data;
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
			scope.$modelValue.deleted = 1;
			cascadeDelete(scope.$modelValue.nodes);
		};
		
		function cascadeDelete(arrayInp){
			if(!angular.isUndefined(arrayInp) && arrayInp.length > 0){
				_.each(arrayInp, function(obj) {
					  _.each(obj, function(value, key) {
					    if(key === 'deleted') {
					      obj[key] = 1;
					    }
					  });
				});
				if(!angular.isUndefined(arrayInp.nodes) && arrayInp.nodes.length > 0){
					cascadeDelete(arrayInp.nodes);
				}
			}
		}
		
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
					placeholder: "New Question",
					description : "default",
					topNodeId : nodeData.idNode,
					parentId:nodeData.idNode,
					type : "Q_simple",
					nodeclass : "Q",
					nodes : []
				});
			} else if (nodeData.type == 'Q_single') {
				nodeData.nodes.push({
					idNode : nodeData.idNode * 10 + nodeData.nodes.length,
					name : "New Possible Answer",
					placeholder:"New Possible Answer",
					topNodeId : nodeData.idNode,
					parentId:nodeData.idNode,
					type : "P_single",
					nodeclass : "P",
					nodes : []
				});
			}else if (nodeData.type == 'Q_multiple') {
				nodeData.nodes.push({
					idNode : nodeData.idNode * 10 + nodeData.nodes.length,
					name : "New Multi Possible Answer",
					placeholder: "New Multi Possible Answer",
					topNodeId : nodeData.idNode,
					parentId:nodeData.idNode,
					type : "P_multiple",
					nodeclass : "P",
					nodes : []
				});
			}else if (nodeData.type == 'Q_simple') {
				nodeData.nodes.push({
					idNode : nodeData.idNode * 10 + nodeData.nodes.length,
					name : "New Possible Answer",
					placeholder:"New Possible Answer",
					topNodeId : nodeData.idNode,
					parentId:nodeData.idNode,
					type : "P_single",
					nodeclass : "P",
					nodes : []
				});
			}else if (nodeData.type == 'M_Module') {
				nodeData.nodes.push({
					idNode : nodeData.idNode * 10 + nodeData.nodes.length,
					name : "New Question",
					placeholder: "New Question",
					topNodeId : nodeData.idNode,
					parentId:nodeData.idNode,
					type : "Q_simple",
					nodeclass : "Q",
					nodes : []
				});
			}else{
				var nodeData = scope.$modelValue;
		        nodeData.nodes.push({
		          id: nodeData.idNode * 10 + nodeData.nodes.length,
		          name: "new default node",
		          placeholder:"new node",
		          topNodeId : nodeData.idNode,
		          parentId:nodeData.idNode,
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
			}else{
				return $scope.defaultMenuOptions;
			}
		};
		$scope.moduleMenuOptions = 
			[ [ 'Add Question', function($itemScope) {
						$scope.newSubItem($itemScope);
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
			  [ 'Run Interview', function($itemScope) {
					alert('under development');
				} 
			  ],
			  [ 'Export to JSON', function($itemScope) {
					alert('under development');
				} 
			  ],
			  [ 'Export to PDF', function($itemScope) {
				  					alert('under development');
								} 
			  ]
			];
		
		$scope.questionMenuOptions = 
			[ [ 'Add Possible Answer', function($itemScope) {
						$scope.newSubItem($itemScope);
						}
			  ],
			  [ 'Remove', function($itemScope) {
					$scope.remove($itemScope);
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
	  					var node = $itemScope.node;
	  					node.idNode = node.link;
	  					$scope.addFragmentTab(node);
	  				} 
				  ]
			];
		$scope.possibleAnswerMenuOptions = 
			[ [ 'Add Quesiton', function($itemScope) {
						$scope.newSubItem($itemScope);
						}
			  ],
			  [ 'Remove', function($itemScope) {
					$scope.remove($itemScope);
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
		$scope.defaultMenuOptions = 
			[ 
			  [ 'Remove', function($itemScope) {
					$scope.remove($itemScope);
					}
			  ]
			];
		
		$scope.save = function (){
			QuestionsService.save($scope.data[0]).then(function(response){
				if(response.status === 200){
					alert('Save was Successful!');
				}
			});
		};
		
	}
})();