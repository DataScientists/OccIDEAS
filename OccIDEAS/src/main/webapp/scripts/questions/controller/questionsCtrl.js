(function() {
	angular.module('occIDEASApp.Questions')
			.controller('QuestionsCtrl',QuestionsCtrl);

	QuestionsCtrl.$inject = [ 'data', '$scope', '$mdDialog','FragmentsService',
	                          '$q','QuestionsService','ModulesService',
	                          '$anchorScroll','$location'];
	function QuestionsCtrl(data, $scope, $mdDialog, FragmentsService,
			$q,QuestionsService,ModulesService,
			$anchorScroll,$location) {
		var self = this;
		$scope.data = data;	
		$scope.isDragging = false;
		$scope.isCreatingFragments = false;
		$scope.showFragmentSlider = true;
		$scope.showModuleSlider = false;
		$anchorScroll.yOffset = 200;
		
		//typesetting
		$scope.topDirections = ['left', 'up'];
		$scope.bottomDirections = ['down', 'right'];
		self.isOpen = false;
		$scope.availableModes = ['md-fling', 'md-scale'];
		$scope.selectedMode = 'md-fling';
		$scope.availableDirections = ['up', 'down', 'left', 'right'];
		$scope.selectedDirection = 'up';

		
		
		
		
		$scope.aJsmTreeOptions = {
				accept: function(sourceNodeScope, destNodesScope, destIndex) {
					//var sourceNode = sourceNodeScope.node;
				      return true;
				    },
				beforeDrop:function(event){
					
					
				},
				dropped: function (event){
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
								
								name : sourceNode.name,
								description : sourceNode.description,
								topNodeId : sourceNode.idNode,
								type : sourceNode.type,
								nodeclass : sourceNode.nodeclass,
								link : sourceNode.idNode,
								parentId : destNode.idNode,
								nodes : []
						});
						cascadeReOrderWithParentId(destNode.nodes,destNode.idNode);
						//saveModuleAndReload();
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
						
						cascadeTemplateNullIds(fragmentData);
						
						destNode.nodes.unshift({
							
							name : fragmentData[0].name,
							description : fragmentData[0].description,
							type : fragmentData[0].type,
							nodeclass : fragmentData[0].nodeclass,
							nodes : fragmentData[0].nodes
						});
						
						//saveModuleAndReload();
						deferred.resolve();
						
						return false;
					});
				},
				beforeDrag: function(sourceNodeScope){
					$scope.isDragging = true;	
					
					return true;
				},
				dropped: function (event){
					
				}
		}
		$scope.moduleTreeOptions = {
				accept: function(sourceNodeScope, destNodesScope, destIndex) {
					var sourceNode = sourceNodeScope.node;
				      return true;
				    },
				beforeDrop:function(event){
					
				},
				beforeDrag: function(sourceNodeScope){
					$scope.isDragging = true;
					if(sourceNodeScope.node.classtype === 'M'){	
						$scope.isDragging = false;
						return false;
					}else{
						return true;
					}
				},
				dropped: function (event){
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
					sourceNode.parentId = destNode.idNode;
					$scope.isDragging = false;
					cascadeReOrderWithParentId(destNode.nodes,destNode.idNode);
					if(sourceNode.warning != 'warning'){
						saveModuleAndReload();
					}
				} 
		}
		function reorderSequence(arrayList){
			var seq = 1;
			_.each(arrayList, function(data) {
				 data.sequence = seq++;			
			})
		}
		function cascadeReOrderWithParentId(arrayInp,parentId){
			var seq = 1;
			var parentId = parentId;
			_.each(arrayInp, function(data) {
				 data.sequence = seq++;	
				 data.idNode = null;
				 data.parentId = parentId;
				 parentId = data.idNode;
				 cascadeReOrderWithParentId(data.nodes,parentId);
			})
		}
		function cascadeTemplateNullIds(nodes){
			var seq = 1;
			var parentId = parentId;
			_.each(nodes, function(data) {
				data.sequence = seq++;	
				
				data.originalId = data.idNode;
				data.idNode = null;	
				data.parentId = null;
				 
				if(data.nodes.length>0){
					cascadeTemplateNullIds(data.nodes);
				}		 
			})
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
		
		$scope.toggleMultipleChoice = function(scope) {
			if(scope.$modelValue.type=='Q_Multiple'){
				scope.$modelValue.type = 'Q_Single';
			}else{
				scope.$modelValue.type = 'Q_Multiple';
			}
		};
		$scope.toggleCreatingFragments = function(scope) {
			if($scope.isCreatingFragments){
				$scope.isCreatingFragments = false;
			}else{
				$scope.isCreatingFragments = true;
			}	
		};
		$scope.remove = function(scope) {
			if(scope.$modelValue.deleted){
				scope.$modelValue.deleted = 0;
				cascadeDelete(scope.$modelValue.nodes,0);
			}else{
				scope.$modelValue.deleted = 1;
				cascadeDelete(scope.$modelValue.nodes,1);
			}
			
			
		};
		function cascadeDelete(arrayInp,deleteFlag){
			if(arrayInp.length > 0){
				_.each(arrayInp, function(obj) {
					  _.each(obj, function(value, key) {
					    if(key === 'deleted') {
					      obj[key] = deleteFlag;
					    }
					  });
					if(obj.nodes.length > 0){
						cascadeDelete(obj.nodes,deleteFlag);
					}
				});
				
			}
		}
		
		
		$scope.moveLastToTheBeginning = function() {
			var a = $scope.data.pop();
			$scope.data.splice(0, 0, a);
		};
		$scope.getNodeId = function(scope) {
			var nodeId = 0;
			if(scope.anchorId){
				nodeId = scope.anchorId;
			}else{
				nodeId = scope.idNode;
			}
		}
		$scope.newSubItem = function(scope) {
			var nodeData = scope.$modelValue;
			var locationId = nodeData.idNode * 10 + nodeData.nodes.length
			if (!nodeData.nodes) {
				nodeData.nodes = [];
			}
			if (nodeData.type == 'P_simple') {
				nodeData.nodes.push({	
					anchorId : locationId,
					name : "New Question",
					placeholder: "New Question",
					description : "default",
					topNodeId : nodeData.idNode,
					parentId: nodeData.idNode,
					type : "Q_single",
					nodeclass : "Q",
					nodes : []
				});
			} else if (nodeData.type == 'Q_single') {
				nodeData.nodes.push({
					anchorId : locationId,
					name : "New Possible Answer",
					placeholder:"New Possible Answer",
					topNodeId : nodeData.idNode,
					parentId: nodeData.idNode,
					type : "P_simple",
					nodeclass : "P",
					nodes : []
				});
			}else if (nodeData.type == 'Q_multiple') {
				nodeData.nodes.push({
					anchorId : locationId,
					name : "New Multi Possible Answer",
					placeholder: "New Multi Possible Answer",
					topNodeId : nodeData.idNode,
					parentId: nodeData.idNode,
					type : "P_multiple",
					nodeclass : "P",
					nodes : []
				});
			}else if (nodeData.type == 'Q_simple') {
				nodeData.nodes.push({
					anchorId : locationId,
					name : "New Possible Answer",
					placeholder:"New Possible Answer",
					topNodeId : nodeData.idNode,
					parentId: nodeData.idNode,
					type : "P_simple",
					nodeclass : "P",
					nodes : []
				});
			}else if (nodeData.type == 'M_Module') {
				nodeData.nodes.push({
					anchorId : locationId,
					name : "New Question",
					placeholder: "New Question",
					topNodeId : nodeData.idNode,
					parentId: nodeData.idNode,
					type : "Q_simple",
					nodeclass : "Q",
					nodes : []
				});
			}else if (nodeData.type == 'M_Module_') {
				nodeData.nodes.push({
					anchorId : locationId,
					name : "New Question",
					placeholder: "New Question",
					topNodeId : nodeData.idNode,
					parentId: nodeData.idNode,
					type : "Q_simple",
					nodeclass : "Q",
					nodes : []
				});
			}else if (nodeData.type == 'M_Module__') {
				nodeData.nodes.push({
					anchorId : locationId,
					name : "New Question",
					placeholder: "New Question",
					topNodeId : nodeData.idNode,
					parentId: nodeData.idNode,
					type : "Q_simple",
					nodeclass : "Q",
					nodes : []
				});
			}else if (nodeData.type == 'M_IntroModule') {
				nodeData.nodes.push({
					anchorId : locationId,
					name : "New Question",
					placeholder: "New Question",
					topNodeId : nodeData.idNode,
					parentId: nodeData.idNode,
					type : "Q_simple",
					nodeclass : "Q",
					nodes : []
				});
			}else{
				var nodeData = scope.$modelValue;
		        nodeData.nodes.push({
		          anchorId : locationId,
		          name: "new default node",
		          placeholder:"new node",
		          topNodeId : nodeData.idNode,
		          parentId: nodeData.idNode,
				  type : "default",
				  warning: "warning",
		          nodes: []
		        });
			}
			reorderSequence(scope.$modelValue.nodes);
			$location.hash(locationId);
		    $anchorScroll();
		    saveModuleAndReload();
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
			}else if(scope.node.nodeclass=='F'){
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
			[ [ 'Create Fragments (Toggle)', function($itemScope) {
					$scope.toggleCreatingFragments($itemScope);
				}
			  ],
			  [ 'Add Question', function($itemScope) {
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
			  
			  [ 'Multiple Choice (Toggle)', function($itemScope) {
					$scope.toggleMultipleChoice($itemScope);
					}
			  ],
			  
			  [ 'Save as Fragment', function($itemScope) {
					//$scope.toggleMultipleChoice($itemScope);
				  
					}
			  ],
			  [ 'Remove (Toggle)', function($itemScope) {
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
	  					node.type = 'F_ajsm';
	  					node.classtype = 'F';
	  					$scope.addFragmentTab(node);
	  				} 
				  ]
			];
		$scope.possibleAnswerMenuOptions = 
			[ [ 'Add Quesiton', function($itemScope) {
						$scope.newSubItem($itemScope);
						}
			  ],
			  [ 'Remove (Toggle)', function($itemScope) {
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
			  [ 'Remove (Toggle)', function($itemScope) {
					$scope.remove($itemScope);
					}
			  ]
			];
		function saveModuleAndReload(){
			QuestionsService.getMaxId().then(function(response){
				if(response.status === 200){
					generateIdNodeCascade($scope.data[0],response.data,$scope.data[0].idNode);
					
					QuestionsService.save($scope.data[0]).then(function(response){
						if(response.status === 200){
							console.log('Save was Successful!');
							//$scope.data = QuestionsService.findQuestions($scope.data[0].idNode);
							QuestionsService.findQuestions($scope.data[0].idNode).then(function(data) {	
								$scope.data = data.data;
							});
						}else{
							console.log('ERROR on Save!');
						}
					});
				}else{
					console.log('ERROR on Save!');
				}
			});
			
		}
		
		function generateIdNodeCascade(data,maxId,parentId){
			var maxIdIncrement = maxId;
			if(!data.idNode){
				maxIdIncrement =(maxIdIncrement + 1);
				data.idNode = maxIdIncrement;
				
				if(parentId){
					data.parentId = parentId;
				}
			}
			if(data.nodes){
				if(data.nodes.length > 0){
					for (i = 0; i < data.nodes.length; i++) {
						generateIdNodeCascade(data.nodes[i],maxIdIncrement+i,data.idNode);   	              
	    	        }
					
				}
			}
			
		}
		
		$scope.saveModule = function (){
			saveModuleAndReload();
		};
		
	}
})();