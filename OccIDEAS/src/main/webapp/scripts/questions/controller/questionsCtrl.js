(function() {
	angular.module('occIDEASApp.Questions')
			.controller('QuestionsCtrl',QuestionsCtrl);

	QuestionsCtrl.$inject = [ 'data', '$scope', '$mdDialog','FragmentsService',
	                          '$q','QuestionsService','ModulesService',
	                          '$anchorScroll','$location','$mdMedia','$window'];
	function QuestionsCtrl(data, $scope, $mdDialog, FragmentsService,
			$q,QuestionsService,ModulesService,
			$anchorScroll,$location,$mdMedia,$window) {
		var self = this;
		$scope.data = data;	
		$scope.isDragging = false;
		$scope.isDragginEnabled = false;
		$scope.isCreatingFragments = false;
		$scope.showFragmentSlider = true;
		$scope.showModuleSlider = false;
		$anchorScroll.yOffset = 200;
		
		//typesetting
		$scope.topDirections = ['left', 'up'];
		$scope.bottomDirections = ['down', 'right'];
		self.isOpen = false;
		$scope.availableModes = ['md-fling', 'md-scale'];
		$scope.selectedMode = 'md-scale';
		$scope.availableDirections = ['up', 'down', 'left', 'right'];
		$scope.selectedDirection = 'left';

		$scope.customFullscreen = $mdMedia('xs') || $mdMedia('sm');
		
		$window.beforeNode = [];
		$window.afterNode = [];
		$scope.undoEnable = false;
		
		$scope.isCollapsableNode = function(node){
			if($scope.isModuleHeaderNode(node)){
				return false;
			}else if(node.nodes.length==0){
				return false;
			}else if(node.deleted==1){
				return false;
			}else{
				return true;
			}
		}
		$scope.isModuleHeaderNode = function(node){
			if(node.type.indexOf('M_Module')>-1){
				return true;
			}else if(node.type.indexOf('M_Module_')>-1){
				return true;
			}else if(node.type.indexOf('M_Module__')>-1){
				return true;
			}else if(node.type.indexOf('M_IntroModule')>-1){
				return true;
			}else{
				return false;
			}
		}
		
		$scope.setNodeType = function (node,type){
			if(type == 'Prod'){
				if (node.nodeclass == 'M') {
					node.type = 'M_Module'
				}
			}else if(type == 'Dev'){
				if (node.nodeclass == 'M') {
					node.type = 'M_Module_'
				}
			}else if(type == 'Test'){
				if (node.nodeclass == 'M') {
					node.type = 'M_Module__'
				}
			} else if(type == 'Intro'){
				if (node.nodeclass == 'M') {
					node.type = 'M_IntroModule'
				}
			} 
		}	
		
		$scope.aJsmTreeOptions = {
				accept: function(sourceNodeScope, destNodesScope, destIndex) {
					//var sourceNode = sourceNodeScope.node;
				      return true;
				    },
				beforeDrag: function(sourceNodeScope){
						$scope.isDragging = true;	
						
						return true;
					},
				beforeDrop:function(event){
					var sourceNode = event.source.nodeScope.node;
					var destNode = event.dest.nodesScope.node;
					console.log("source"+sourceNode.type);
					if(!destNode){
						$scope.isDragging = false;
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
						$scope.isDragging = false;
						saveModuleWithoutReload();
						return false;
					}
					
				},
				dropped: function (event){
					
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
						$scope.isDragging = false;
						return false;					
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
						saveModuleWithoutReload();
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
					var destNode = event.dest.nodesScope.node;
					if(!destNode){
						$scope.isDragging = false;
						return false;
					}
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
					recordAction($scope.data);
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
					reorderSequence(destNode.nodes);
					if(sourceNode.warning != 'warning'){
						saveModuleWithoutReload();
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
				
				if(data.nodes){
					if(data.nodes.length>0){
						cascadeTemplateNullIds(data.nodes);
					}
				}else{
					data.nodes = [];
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
			recordAction($scope.data);
			if(scope.$modelValue.deleted){
				scope.$modelValue.deleted = 0;
				cascadeDelete(scope.$modelValue.nodes,0);
			}else{
				scope.$modelValue.deleted = 1;
				cascadeDelete(scope.$modelValue.nodes,1);
			}
			saveModuleAndReload();
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
			recordAction($scope.data);
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
				var type = "P_simple";
				var defaultText = "New Possible Answer";
				if(scope.isFreeText){
					type = "P_freetext";
					defaultText = "[Freetext]";
				}
				nodeData.nodes.push({
					anchorId : locationId,
					name : defaultText,
					placeholder:defaultText,
					topNodeId : nodeData.idNode,
					parentId: nodeData.idNode,
					type : type,
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
			}else if (nodeData.type == 'F_ajsm') {
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
			}else if (nodeData.type.indexOf('P') != 0) {
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
			}else if (nodeData.type.indexOf('Q') != 0) {
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
			if(!scope.$modelValue.name){
				scope.$modelValue.name = 'Blank';
			}
			$scope.safeApply(function() {
				scope.$modelValue.editEnabled = false;
				saveModuleWithoutReload()
			});
			saveModuleAndReload();
		};

		$scope.enable = function(scope) {
			recordAction($scope.data);
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
				$scope.selectedNode = scope.node;
				return $scope.questionMenuOptions;
			}else if(scope.node.nodeclass=='P'){
				return $scope.possibleAnswerMenuOptions;
			}else{
				return $scope.defaultMenuOptions;
			}
		};
		$scope.moduleMenuOptions = 
			[ 
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
			  [ 'Add Free Text Possible Answer', function($itemScope) {
				  $itemScope.isFreeText = true;
					$scope.newSubItem($itemScope);
					}
			  ],
			  
			  [ 'Multiple Choice (Toggle)', function($itemScope) {
					$scope.toggleMultipleChoice($itemScope);
					}
			  ],
			  
			  [ 'Save as Fragment', function($itemScope) {
				  
				  $mdDialog.show({
					  //scope: $scope,
					  controller: QuestionsCtrl,
					  resolve: {
		                    data: function () {
		                        return $scope.selectedNode;
		                    },
		                    
					  },
					  /*locals: {
						  addFragmentTab: $scope.addFragmentTab
				         },*/
				      templateUrl: 'scripts/questions/view/fragmentDialog.html',
				      parent: angular.element(document.body),
				      clickOutsideToClose:true
				    })
				    .then(function(answer) {
				      $scope.status = 'You said the information was "' + answer + '".';
				    }, function() {
				      $scope.status = 'You cancelled the dialog.';
				    });
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
				  [ 'Open as aJSM', function($itemScope) {	
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
					
					generateIdNodeCascade($scope.data[0].nodes,response.data,$scope.data[0].idNode);
					
					QuestionsService.save($scope.data[0]).then(function(response){
						if(response.status === 200){
							console.log('Save was Successful!');
							QuestionsService.findQuestions($scope.data[0].idNode,$scope.data[0].nodeclass).then(function(data) {	
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
		function saveModuleWithoutReload(){
			QuestionsService.getMaxId().then(function(response){
				if(response.status === 200){
					
					generateIdNodeCascade($scope.data[0].nodes,response.data,$scope.data[0].idNode);
					
					QuestionsService.save($scope.data[0]).then(function(response){
						if(response.status === 200){
							console.log('Save was Successful!');
							//$scope.data = QuestionsService.findQuestions($scope.data[0].idNode);
							//QuestionsService.findQuestions($scope.data[0].idNode,$scope.data[0].nodeclass).then(function(data) {	
							//	$scope.data = data.data;
							//});
						}else{
							console.log('ERROR on Save!');
						}
					});
				}else{
					console.log('ERROR on Save!');
				}
			});
		}
		function saveAsFragment(data,mydata){
			QuestionsService.getMaxId().then(function(response){
				if(response.status === 200){
					var maxId = response.data;
					destNode = {
							idNode : maxId,
							name : mydata.name,
							type : mydata.group,
							nodeclass : "F",
							nodes : []
						};
					destNode.nodes.unshift({
						name : data.name,
						description : data.description,
						type : data.type,
						nodeclass : data.nodeclass,
						nodes : data.nodes
					});
					cascadeTemplateNullIds(destNode.nodes);
					generateIdNodeCascade(destNode.nodes,maxId,destNode.idNode);
					FragmentsService.save(destNode).then(function(response){
						if(response.status === 200){
							
							//$scope.addFragmentTab(destNode);
							console.log("Fragment saved")
							$mdDialog.cancel();
						}
					});
				}
			});
		}
		var maxIdIncrement = 0;
		function generateIdNodeCascade(arrayInp,maxId,parentId){
			maxIdIncrement = maxId;
			if(arrayInp.length > 0){
				var i=0;
				_.each(arrayInp, function(node) {
					console.log(node.name)
					node.sequence = i;
					if(!node.idNode){
						maxIdIncrement =(maxIdIncrement + 1);
						node.idNode = maxIdIncrement;
						
						if(parentId){
							node.parentId = parentId;
						}
					}  
					if(node.nodes){
						if(node.nodes.length > 0){
							generateIdNodeCascade(node.nodes,maxIdIncrement,node.idNode);
						}
					}
					i++;
				});
				
			}
		}
		
		$scope.saveModule = function (){
			saveModuleAndReload();
		};
		
		$scope.saveAsFragment = function (data,mydata){
			saveAsFragment(data,mydata);
		}
		
		$scope.cancel = function() {
		    $mdDialog.cancel();
		};
		
		function recordAction(data){
			$window.beforeNode = angular.copy(data);
			$scope.undoEnable = true;
		}
		
		$scope.undo = function(){
			$scope.data = $window.beforeNode;
			saveModuleAndReload();
			$scope.undoEnable = false;
		}
	}
})();