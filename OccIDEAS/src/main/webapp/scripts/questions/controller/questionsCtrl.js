(function() {
	angular.module('occIDEASApp.Questions')
			.controller('QuestionsCtrl',QuestionsCtrl);

	QuestionsCtrl.$inject = [ 'data', '$scope', '$mdDialog','FragmentsService',
	                          '$q','QuestionsService','ModulesService',
	                          '$anchorScroll','$location','$mdMedia','$window','$state',
	                          'AgentsService','RulesService','$compile','TabsCache','$rootScope','ModuleRuleService'];
	function QuestionsCtrl(data, $scope, $mdDialog, FragmentsService,
			$q,QuestionsService,ModulesService,
			$anchorScroll,$location,$mdMedia,$window,$state,
			AgentsService,RulesService,$compile,TabsCache,$rootScope,ModuleRuleService) {
		var self = this;
		console.log('inside QuestionsCtrl');
		$scope.data = data;	
		var moduleIdNode = $scope.data[0].idNode;
		$scope.$window = $window;  
		$scope.isDragging = false;
		$scope.activeNodeId = 0;
		$anchorScroll.yOffset = 200;
    	$scope.rulesObj = [];
    	$scope.rulesInt = [];
    	$scope.agentsData = null;
    	$scope.nodePopover = {
    		    templateUrl: 'scripts/questions/partials/nodePopover.html',
    		    open: function(x,idRule) {
    		    	var nodeclass = 'P';
    		    	if(angular.isUndefined(x.info)){
  		    		  x.info = [];
  		    	  	}
    		    	 x.info["Node"+x.idNode+idRule] = {
							    				  idNode:x.idNode,
							    				  nodeclass:nodeclass,
							    				  nodePopover:{
							    					  isOpen: false
							    				  },
							    				  nodePopoverInProgress : false
		    		  							};
    		    	 var nodeInPopup = x.info["Node"+x.idNode+idRule];
    		    	 nodeInPopup.nodePopoverInProgress = true;
    		         var deffered = $q.defer();
    		         QuestionsService.findPossibleAnswer(nodeInPopup.idNode).then(function(data) {	
    		    		nodeInPopup.data = data.data[0];
    		    		nodeInPopup.idRule =idRule;
   						nodeInPopup.nodePopoverInProgress = false;
   						deffered.resolve();
 					 });
    		         deffered.promise.then(function(){
    		        	 nodeInPopup.nodePopover.isOpen = true;
    		    	 })
    		    },   		    
  		        close: function close(x,idRule) {
  		        	x.info["Node"+x.idNode+idRule].nodePopover.isOpen = false;
  		        }
    	};
    	
    	function initAgentData(){
    		AgentsService.get().then(function(agent) {
    		var group = _.groupBy(agent, function(b) { 
    			return b.groupName;
    		});
    		if($scope.data[0].moduleRule){
        		_.forOwn(group, function(x, key) { 
        		var totalVal = 0; 
        		_.forEach(x,function(v,k) {
        			  var ruleArray =_.filter($scope.data[0].moduleRule, function(r){
        					return v.idAgent === r.idAgent; 
        			  });
        			  v.total = _.uniq(ruleArray, 'rule.idRule').length;
        			  totalVal = totalVal + v.total;
        			});
        		x.total = totalVal;
        		} );
        	}
    		group = setOrder(group);
    		$scope.agentsLoading = false;
    		$scope.agentsData = group;
    		});
    	}
    	
    	function initFragmentData(){
    		$scope.fragmentsLoading = true;
    		 FragmentsService.get().then(function(data) {	
	    			var object = {};
	    			var ajsms = [];
	    			var templates = [];
	    			var frequencies = [];
	    			for(var i=0;i < data.length;i++){
	    				var node = data[i];
	    				node.idnode = "";
	    				node.nodeclass = "Q";
	    				if(node.type=='F_ajsm'){
	    					node.type = "Q_linkedajsm";
	    					ajsms.push(node);
	    				}else if(node.type=='F_template'){
	    					templates.push(node);
	    				}else if(node.type=='F_frequency'){
	    					frequencies.push(node);
	    				}
	    			}
	    			$scope.templateData = templates;
	    			$scope.frequencyData = frequencies;
	    			$scope.aJSMData = ajsms;
	    			$scope.fragmentsLoading = false;
	    			return object;
    		 });
    	}
    	
    	function setOrder (obj) {
    	    var out = [];
    	    _.forEach(obj,function(value,key) {
    	      out.push({ key: key, value: value ,total: value.total});
    	    });
    	    return out;
    	}
    	
		$scope.toggleRulesObj = function (agents){
			if(_.findIndex($scope.rulesObj, function(o) { 
					return o.idAgent === agents.idAgent; }) != -1){
				$scope.safeApply(function () {
		              $scope.rulesObj.splice(_.findIndex($scope.rulesObj, function(o) { 
		            	  return o.idAgent === agents.idAgent; }), 1)[0];
		        });
			}else{
				$scope.rulesObj.push(agents);
			}
			
		};
    	$scope.getRulesIfAny = function(node,agents){
    		var filteredAgent = _.filter(node.moduleRule, _.matches({ 'idAgent': agents.idAgent }));
			if(filteredAgent.length > 0){
				/*var rules=  _.reduce(filteredAgent, function(memo, current) { 
					return _.extend(memo, current) 
					},  {});
				var id = rules.idRule;
				var existRules = _.filter($scope.rulesInt, { 'id': id});
				if(existRules.length > 0){
					existRules[0].nodes.push({
						nodeNumber:node.number,
						idNode:node.idNode
					});
				}else{
				$scope.rulesInt.push({
					'id':id,
					nodes:[{
						nodeNumber:node.number,
						idNode:node.idNode
					}]
				});
				}*/
				return filteredAgent;
			}
			return null;			
    	}
    	
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
			if(node){				
				if($scope.isModuleHeaderNode(node)){
					return false;
				}else if(node.nodes.length==0){
					return false;
				}else if(node.deleted==1){
					return false;
				}else{
					return true;
				}
			}else{
				return false;
			}
		}
		$scope.isModuleHeaderNode = function(node){		
			if(node){	
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
			}else{
				return false;
			}
		}
		
		$scope.setNodeType = function (node,type){
			if(type == 'Prod'){
				if (node.nodeclass == 'M') {
					node.type = 'M_Module';
				}
			}else if(type == 'Dev'){
				if (node.nodeclass == 'M') {
					node.type = 'M_Module_';
				}
			}else if(type == 'Test'){
				if (node.nodeclass == 'M') {
					node.type = 'M_Module__';
				}
			} else if(type == 'Intro'){
				if (node.nodeclass == 'M') {
					node.type = 'M_IntroModule';
				}
			} 
			saveModuleWithoutReload();
		}
		
		$scope.aJsmTreeOptions = {
				accept: function(sourceNodeScope, destNodesScope, destIndex) {
					  
				      return true;
				    },
				dragStart: function(event){
						$scope.undoEnable = false;
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
						if(sourceNode.nodeclass=='Q'){
							if(destNode.nodeclass=='Q'){
								sourceNode.warning = '';
								$scope.isDragging = false;
								return false;
							}			
						}else if(sourceNode.nodeclass=='P'){
							if(destNode.nodeclass=='P'){
								sourceNode.warning = '';
								$scope.isDragging = false;
								return false;
							}else if(destNode.nodeclass=='M'){
								sourceNode.warning = '';
								$scope.isDragging = false;
								return false;			
							}			
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
					  
				      return true;
				    },
				dragStart: function(event){
						$scope.undoEnable = false;
					},
				beforeDrop:function(event){
					var sourceNode = event.source.nodeScope.node;
					var destNode = event.dest.nodesScope.node;
					console.log("source"+sourceNode.type);
					if(!destNode){
						$scope.isDragging = false;
						return false;					
					}
					if(sourceNode.nodeclass=='Q'){
						if(destNode.nodeclass=='Q'){
							sourceNode.warning = '';
							$scope.isDragging = false;
							return false;
						}			
					}else if(sourceNode.nodeclass=='P'){
						if(destNode.nodeclass=='P'){
							sourceNode.warning = '';
							$scope.isDragging = false;
							return false;
						}else if(destNode.nodeclass=='M'){
							sourceNode.warning = '';
							$scope.isDragging = false;
							return false;			
						}			
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
					var destNode = destNodesScope.node;
					var placeholder = document.getElementsByClassName("angular-ui-tree-placeholder");
					var wrappedplaceholder = angular.element(placeholder);
					if(!destNode){
						sourceNode.warning = 'warning';						
					}else{
						if(sourceNode.nodeclass=='Q'){
							if(destNode.nodeclass=='Q'){
								console.log("Hovering Q on Q");
								
								wrappedplaceholder.addClass('angular-ui-tree-placeholder-warning');
								sourceNode.warning = 'warning';		
							}else{
								wrappedplaceholder.removeClass('angular-ui-tree-placeholder-warning');
								sourceNode.warning = '';
							}			
						}else if(sourceNode.nodeclass=='P'){
							if(destNode.nodeclass=='P'){
								console.log("Hovering P on P");
								wrappedplaceholder.addClass('angular-ui-tree-placeholder-warning');
								sourceNode.warning = 'warning';		
							}else if(destNode.nodeclass=='M'){
								console.log("Hovering P on M");
								wrappedplaceholder.addClass('angular-ui-tree-placeholder-warning');
								sourceNode.warning = 'warning';				
							}else{
								wrappedplaceholder.removeClass('angular-ui-tree-placeholder-warning');
								sourceNode.warning = '';
							}			
						}else{
							wrappedplaceholder.removeClass('angular-ui-tree-placeholder-warning');
							sourceNode.warning = '';
						}
					}
				    return true;
				},
				beforeDrop:function(event){
					var sourceNode = event.source.nodeScope.node;
					var destNode = event.dest.nodesScope.node;
					//if(event.source.index != event.dest.index){
						recordAction($scope.data);
					//}
					$scope.isDragging = false;
					var retValue = true;
					if(!destNode){
						$scope.isDragging = false;
						retValue=false;
					}else{
						if(sourceNode.nodeclass=='Q'){
							if(destNode.nodeclass=='Q'){
								console.log("dropped Q on Q");							
								retValue=false;		
							}			
						}else if(sourceNode.nodeclass=='P'){
							if(destNode.nodeclass=='P'){
								console.log("Dropped P on P");
								retValue=false;		
							}else if(destNode.nodeclass=='M'){
								console.log("Dropped P on M");
								retValue=false;			
							}else{
								sourceNode.warning = '';
							}			
						}else{
							sourceNode.warning = '';	
						}
					}
					return retValue;
				},
				beforeDrag: function(sourceNodeScope){
					$scope.isDragging = true;
					if(sourceNodeScope.node.classtype === 'M'){	
						$scope.isDragging = false;
						return false;
					}else if(sourceNodeScope.node.classtype === 'F'){	
						$scope.isDragging = false;
						return false;
					}else{
						return true;
					}
				},
				dragStart: function(event){
					if($scope.isClonable){

						event.elements.placeholder.replaceWith(event.elements.dragging.find('li').clone()[0]);

						event.source.nodeScope.node.idNode = "";
						var name = event.source.nodeScope.node.name;
						event.source.nodeScope.node.name = name+"(Copy)";
						cascadeIdCleanse(event.source.nodeScope.node.nodes);
					}
				},
				dragStop: function(event){
					
				},
				dropped: function (event){
					
					var sourceNode = event.source.nodeScope.node;
					sourceNode.warning = null;
					var destNode = event.dest.nodesScope.node;
					if($scope.isClonable){
						console.log("Just cloned so turning undo off ");
						$scope.undoEnable = false;
					}
					
					console.log("source"+sourceNode.type);
					if(!destNode){
						sourceNode.warning = 'warning';						
					}else{
						console.log("dest "+destNode.type);
						
					}
					sourceNode.parentId = destNode.idNode;
					$scope.isDragging = false;
					if(sourceNode.warning != 'warning'){
						if($scope.isClonable){						
							saveModuleAndReload();
							$scope.isClonable = false;												
						}else{
							saveModuleWithoutReload();
						}
					}
				}
		}
		function reorderSequence(arrayList){
			var seq = 1;
			_.each(arrayList, function(data) {
				 data.sequence = seq++;			
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
		$scope.toggleRight = function(){
		    if ($scope.rightNav === "slideFrag"){
		      $scope.rightNav = "";
		    }
		    else{
		      $scope.rightNav = "slideFrag";
		    }
		    if(angular.isUndefined($scope.templateData) || angular.isUndefined($scope.aJSMData)
		    			|| angular.isUndefined($scope.frequencyData)){
				initFragmentData();
			}
		};
		
		$scope.leftNav = "slideFragLeft";
		$scope.toggleLeft = function(){
		    if ($scope.leftNav === "slideFragLeft"){
		      $scope.leftNav = "";
		    }
		    else{
		      $scope.leftNav = "slideFragLeft";
		    }
		    if((angular.isUndefined($scope.agentsData))||($scope.agentsData == null)){
		    	$scope.agentsLoading = true;
				initAgentData();
			}
		};
		
		$scope.toggle = function(scope) {
			scope.toggle();
		};
		
		$scope.toggleMultipleChoice = function(scope) {
			recordAction($scope.data);
			if(scope.$modelValue.type=='Q_multiple'){
				scope.$modelValue.type = 'Q_single';
			}else{
				scope.$modelValue.type = 'Q_multiple';
			}
			saveModuleWithoutReload();
		};
		$scope.deleteNode = function(scope) {
			recordAction($scope.data);
			if(scope.$modelValue.deleted){
				scope.$modelValue.deleted = 0;
				cascadeDelete(scope.$modelValue.nodes,0);
			}else{
				scope.$modelValue.deleted = 1;
				cascadeDelete(scope.$modelValue.nodes,1);
			}
			var deffered = $q.defer();
			saveModuleWithoutReload('',deffered);
			deffered.promise.then(function(resolve){
				searchAndRemoveNode($scope.data,scope);
			});
		};
		
		function searchAndRemoveNode(objSearch,scope){
			 var index = _.findIndex(objSearch, function(o) {
				if(o.$$hashKey === scope.$modelValue.$$hashKey ){
					return o.number;
				}
				if(!angular.isUndefined(o.nodes) && o.nodes.length > 0){
					var x = searchAndRemoveNode(o.nodes,scope);
					if (x > -1) {
			               o.nodes.splice(x, 1)[0];
			            return;
			        }
				}
			 });
			 return index;
		}
		
		function cascadeIdCleanse(arrayInp){
			if(arrayInp.length > 0){
				_.each(arrayInp, function(obj) {
					  _.each(obj, function(value, key) {
					    if(key === 'idNode') {
					      obj[key] = "";
					    }
					  });
					if(obj.nodes.length > 0){
						cascadeIdCleanse(obj.nodes);
					}
				});
				
			}
		}
		
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
			$scope.undoEnable = false;
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
				var type = "P_simple";
				var defaultText = "New Possible Answer";
				if(scope.isFreeText){
					type = "P_freetext";
					defaultText = "[Freetext]";
				}
				nodeData.nodes.push({
					anchorId : locationId,
					name : defaultText,
					topNodeId : nodeData.idNode,
					parentId: nodeData.idNode,
					type : type,
					nodeclass : "P",
					nodes : []
				});
			}else if (nodeData.type == 'Q_multiple') {
				var type = "P_simple";
				var defaultText = "New Possible Answer";
				if(scope.isFreeText){
					type = "P_freetext";
					defaultText = "[Freetext]";
				}
				nodeData.nodes.push({
					anchorId : locationId,
					name : defaultText,
					placeholder: "New Multi Possible Answer",
					topNodeId : nodeData.idNode,
					parentId: nodeData.idNode,
					type : type,
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
			}else if (nodeData.type.indexOf('P') == 0) { //P is first character
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
			}else if (nodeData.type.indexOf('Q') == 0) { //Q is first character
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
			//reorderSequence(scope.$modelValue.nodes);
			saveModuleWithoutReload(locationId);
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
			});
			saveModuleWithoutReload();
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
		$scope.showRulesMenu = function(scope){
			return $scope.rulesMenuOptions;
		}
		$scope.showMenu = function(scope) {
			if(scope.node.nodeclass=='M'){
				return $scope.moduleMenuOptions;
			}else if(scope.node.nodeclass=='F'){
				return $scope.moduleMenuOptions;
			}else if(scope.node.nodeclass=='Q'){
				$scope.selectedNode = scope.node;
				var menuOptions;
				if(scope.node.type=='Q_linkedmodule'){
					menuOptions = $scope.linkedModuleMenuOptions;
				}else if(scope.node.type=='Q_linkedajsm'){
					menuOptions = $scope.linkedAjsmMenuOptions;
				}else{
					menuOptions = $scope.questionMenuOptions;
				}
				return menuOptions;
			}else if(scope.node.nodeclass=='P'){
				return $scope.possibleAnswerMenuOptions;
			}else{
				return $scope.defaultMenuOptions;
			}
		};
		$scope.moduleMenuOptions = 
			[ 
			  [ 'Show Rules', function($itemScope) {
					$scope.addRulesTab($itemScope);
					}
			  ],
			  [ 'Add Question', function($itemScope) {
					$scope.newSubItem($itemScope);
				}
			  ],
			  [ 'Show/Hide Children', function($itemScope) {
					
				  var collapseOrExpand = function (scope) {
			          var i, subScope,
			              nodes = scope.childNodes();
			          for (i = 0; i < nodes.length; i++) {
			        	var collapsed = nodes[i].collapsed;
			            !collapsed ? nodes[i].collapse() : nodes[i].expand();
			            subScope = nodes[i].$childNodesScope;
			            if (subScope) {
			              collapseOrExpand(subScope);
			            }
			          }
			        };
			        collapseOrExpand($itemScope);
					} 
				  ], null, // Dividier
			  [ 'Run Interview', function($itemScope) {
                  $scope.addInterviewTab($itemScope);
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
			  		$itemScope.isFreeText = false;
				}
			  ],
			  
			  [ 'Multiple Choice (Toggle)', function($itemScope) {
					$scope.toggleMultipleChoice($itemScope);
					}
			  ],
			  
			  [ 'Save as Fragment', function($itemScope) {
				  
				  $mdDialog.show({
					  //scope: $scope,
					  scope: $scope.$new(),
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
					$scope.deleteNode($itemScope);
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
		$scope.linkedAjsmMenuOptions =
			[
			  [ 'Remove (Toggle)', function($itemScope) {
					$scope.deleteNode($itemScope);
					}
			  ],
			  [ 'Open as aJSM', function($itemScope) {	
					var node = angular.copy($itemScope.node);
					node.idNode = node.link;
					node.type = 'F_ajsm';
					node.classtype = 'F';
					$scope.addFragmentTab(node);
				} 
			  ]
			];
		$scope.linkedModuleMenuOptions =
			[
			  [ 'Remove (Toggle)', function($itemScope) {
					$scope.deleteNode($itemScope);
					}
			  ],
			  [ 'Open as Module', function($itemScope) {
				  		var node = angular.copy($itemScope.node);
	  					node.idNode = node.link;
	  					node.type = 'M_Module';
	  					node.classtype = 'M';
	  					$scope.addModuleTab(node);
	  				}
			  ]
			];
		
		function addPopoverInfo(x,idRule){
			 if(angular.isUndefined(x[0].info)){
	    		  x[0].info = [];
	    	  }
			 x[0].info["Node"+x[0].idNode+idRule] = {
	    				  idNode:x[0].idNode,
	    				  idRule:idRule,
	    				  nodeclass:x[0].nodeclass,
	    				  nodePopover:{
	    					  isOpen: false
	    				  },
	    				  nodePopoverInProgress : false
	          };
		}
		
		$scope.rulesMenuOptions =
			[
			  [ 'Show Rules', function($itemScope, $event, model) {
				  var rules =_.filter(model.moduleRule, function(r){
  					return $itemScope.$parent.obj.idAgent === r.idAgent; 
  			      });
				  if(rules.length > 0){
				  	for(var i=0;i<rules.length;i++){
				  		var scope = $itemScope.$new();
				  		scope.model = model;
				  		scope.rule = rules[i].rule;
				  		scope.agentName = rules[i].agentName;
					  	var x = scope.rule.conditions;
					  	x.idRule = scope.rule.idRule;
					  	addPopoverInfo(x,scope.rule.idRule);
					  	newNote($event.currentTarget.parentElement,scope,$compile);
					  	$scope.activeRule = scope.rule;
				  	}
				  }	  
				  
			  	}			  
			  ],
			  [ 'Add Rule', function($itemScope, $event, model) {
			  	  var conditions = [];
			  	  conditions.push(model);
			  	  $itemScope.model = model;
				  var rule = {agentId:$itemScope.$parent.obj.idAgent,conditions:conditions};
				  RulesService.create(rule).then(function(response){
	    				if(response.status === 200){
	    					if(response.data.idRule){
	    						ModuleRuleService.getModuleRule(model.idNode).then(function(response) {
							  
									if(response.status === 200){
										var result = response.data[response.data.length-1];
										$itemScope.rule = result.rule;
										$itemScope.agentName = result.agentName;
										var x = $itemScope.rule.conditions;
										addPopoverInfo(x,$itemScope.rule.idRule);
										newNote($event.currentTarget.parentElement,$itemScope,$compile);									
										$scope.activeRule = result.rule;
										if($itemScope.rules==null){
											$itemScope.rules = [];
										}
										if(angular.isUndefined(model.moduleRule)){
											model.moduleRule = [];
										}
										
										_.merge(model.moduleRule, response.data);
						    			if (!model.moduleRule.$$phase) {
						    			        try {
						    			        	model.moduleRule.$digest();
						    			        }
						    			        catch (e) { }
						    		    }
						    			$scope.data[0].moduleRule.push(result);
						    			initAgentData();
									}
									});
								}else{
									  alert("Try reload");
								}
	    				}
	    			});
					  
			  	}			  
			  ]
			];
		
		$scope.possibleAnswerMenuOptions =
			[ [ 'Add Question', function($itemScope) {
						$scope.newSubItem($itemScope);
						}
			  ],
			  [ 'Remove (Toggle)', function($itemScope) {
					$scope.deleteNode($itemScope);
					}
			  ],
			  [ 'Show/Hide Children', function($itemScope) {
					
					var toggleChildren = function (nodes) {
		                //var nodes = $itemScope.childNodes();
			            for (var i = 0; i < nodes.length; i++) {
			              var subScope = nodes[i].$childNodesScope;
			              if (subScope) {
			            	  var collapsed = !subScope.collapsed;
			            	  for (i = 0; i < nodes.length; i++) {
			    	              collapsed ? nodes[i].collapse() : nodes[i].expand();    	              
			    	            }
			              }
			            }
		              };
		              toggleChildren($itemScope.childNodes());
					} 
			   ]
			];
		$scope.defaultMenuOptions = 
			[ 
			  [ 'Remove (Toggle)', function($itemScope) {
					$scope.deleteNode($itemScope);
					}
			  ]
			];
		function saveModuleAndReload(locationId){
			QuestionsService.getMaxId().then(function(response){
				if(response.status === 200){
					var nodes = $scope.data[0].nodes;
					var maxId = response.data;
					var parentId = $scope.data[0].idNode;
					var parentNodeNumber = $scope.data[0].number;
					var topNodeId = $scope.data[0].idNode;
					generateIdNodeCascade(nodes,maxId,parentId,parentNodeNumber,topNodeId);
					QuestionsService.saveNode($scope.data[0]).then(function(response){
						if(response.status === 200){
							console.log('Save was Successful Now Reloading!');
							QuestionsService.findQuestions($scope.data[0].idNode,$scope.data[0].nodeclass).then(function(data) {	
								TabsCache.removeAll();
								$scope.data = data.data;
								if(locationId){
									//$scope.scrollTo(locationId);
								}
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
		function saveModuleWithoutReload(locationId,deffered){
			QuestionsService.getMaxId().then(function(response){
				if(response.status === 200){
					var nodes = $scope.data[0].nodes;
					var maxId = response.data;
					var parentId = $scope.data[0].idNode;
					var parentNodeNumber = $scope.data[0].number;
					var topNodeId = $scope.data[0].idNode;
					generateIdNodeCascade(nodes,maxId,parentId,parentNodeNumber,topNodeId);		
					QuestionsService.saveNode($scope.data[0]).then(function(response){
						if(response.status === 200){
							console.log('Save was Successful! Not Reloading');
							TabsCache.put(response.data.idNode,response.data);
							if(locationId && locationId != ''){
								//$scope.scrollTo(locationId);
							}
							if(deffered){
								deffered.resolve();
							}
						}
						else{
							console.log('ERROR on Save!');
							if(deffered){
								deffered.reject();
							}
						}
					});
					}else{
						console.log('ERROR on Get max ID!');
						if(deffered){
							deffered.reject();
						}
					}
			});	
		}
		
		
		function saveAsFragment(data,mydata){
			QuestionsService.getMaxId().then(function(response){
				if(response.status === 200){
					var maxId = response.data+1;
					destNode = {
							idNode : maxId,
							name : mydata.name,
							type : mydata.group,
							nodeclass : "F",
							nodes : []
						};
					var childNodes = angular.copy(data.nodes);
					destNode.nodes.unshift({
						name : data.name,
						description : data.description,
						type : data.type,
						nodeclass : data.nodeclass,
						nodes : childNodes
					});
					cascadeTemplateNullIds(destNode.nodes);
					var nodes = destNode.nodes;
					var parentId = destNode.idNode;
					var parentNodeNumber = destNode.idNode.number;
					var topNodeId = destNode.idNode;
					generateIdNodeCascadeFragment(nodes,maxId,parentId);

					var deffered = $q.defer();
					FragmentsService.createFragment(destNode).then(function(response){
						if(response.status === 200){
							console.log("Fragment saved");
							FragmentsService.getByType('F_template').then(function(template) {	
				    			for(var i=0;i < template.length;i++){
				    				var node = template[i];
				    				node.nodeclass = "Q";
				    			}
				    			if($scope.templateData != null){
				    			_.merge($scope.templateData, template);
				    			if (!$scope.templateData.$$phase) {
				    			        try {
				    			        	$scope.templateData.$digest();
				    			        }
				    			        catch (e) { }
				    			    }
				    			}
				    				deffered.resolve();
				    		});
							FragmentsService.getByType('F_ajsm').then(function(data) {
			        			for(var i=0;i < data.length;i++){
			        				var node = data[i];
			        				node.type = "Q_linkedajsm";
			        				node.nodeclass = "Q";
			        			}
			        			if($scope.aJSMData != null){
			        			_.merge($scope.aJSMData, data);
			        			if (!$scope.aJSMData.$$phase) {
			    			        try {
			    			        	$scope.aJSMData.$digest();
			    			        }
			    			        catch (e) { }
			    			    }
			        			}
			        			deffered.resolve();
			        		});
							FragmentsService.getByType('F_frequency').then(function(data) {	
			        			for(var i=0;i < data.length;i++){
			        				var node = data[i];
			        				//node.type = "Q_linkedtemplate";
			        				node.nodeclass = "Q";
			        			}
			        			if($scope.frequencyData != null){
				    			_.merge($scope.frequencyData, data);
			        			if (!$scope.frequencyData.$$phase) {
			    			        try {
			    			        	$scope.frequencyData.$digest();
			    			        }
			    			        catch (e) { }
			    			    }
			        			}
			        			deffered.resolve();
			        		});
						   
						}else{
							deffered.reject();
						}
					});
					deffered.promise.then(function(){
						$mdDialog.hide();
					});
				}
			});
		}
		
		var increment = 0;
		function generateIdNodeCascadeFragment(arrayInp,maxId,parentId){
			increment = maxId;
			if(arrayInp.length > 0){
				var i=0;
				_.each(arrayInp, function(node) {
					
					node.sequence = i;
					if(!node.idNode){
						increment =(increment + 1);
						node.idNode = increment;
						
						if(parentId){
							node.parentId = parentId;
						}
					}  
					if(node.nodes){
						if(node.nodes.length > 0){
							generateIdNodeCascadeFragment(node.nodes,increment,node.idNode);
						}
					}
					i++;
				});
				
			}
		}
		
		var maxIdIncrement = 0;
		function generateIdNodeCascade(arrayInp,maxId,parentId,parentNodeNumber,topNodeId){
			maxIdIncrement = maxId;

			if(arrayInp.length > 0){
				var i=0;
				_.each(arrayInp, function(node) {
					
					node.sequence = i;
					node.topNodeId = topNodeId;
					if(node.nodeclass=='Q'){
						node.number = parentNodeNumber + (i+1);
					}else if (node.nodeclass=='P'){
						if(!isNaN(parentNodeNumber)){
							if(i>26){
								var character = String.fromCharCode('A'.charCodeAt()+(i-27));
								node.number = parentNodeNumber + 'Z' + $scope.getNextKey(character);
							}else{
								var character = String.fromCharCode('A'.charCodeAt()+(i-1));
								node.number = parentNodeNumber + $scope.getNextKey(character);
							}

						}else{
							var length = parentNodeNumber.length;
							var lastCharacter = parentNodeNumber.substring(length-1);
							if(!isNaN(lastCharacter)){
								if(i>26){
									var character = String.fromCharCode('A'.charCodeAt()+(i-27));
									node.number = parentNodeNumber + 'Z' + $scope.getNextKey(character);
								}else{
									var character = String.fromCharCode('A'.charCodeAt()+(i-1));
									node.number = parentNodeNumber + $scope.getNextKey(character);
								}

							}
						}

					}

					if(!node.idNode){
						maxIdIncrement =(maxIdIncrement + 1);
						node.idNode = maxIdIncrement;
						
						if(parentId){
							node.parentId = parentId;
						}
					}  
					if(node.nodes){
						if(node.nodes.length > 0){
							generateIdNodeCascade(node.nodes,maxIdIncrement,node.idNode,node.number,topNodeId);
						}
					}
					i++;
				});
				
			}
		}
		$scope.getNextKey = function(key) {
			  if (/^Z+$/.test(key)) {
			    // If all z's, replace all with a's
			    key = key + 'A';
			  } else {
			    // (take till last char) append with (increment last char)
			    key = key.slice(0, -1) + String.fromCharCode(key.slice(-1).charCodeAt() + 1);
			  }
			  return key;
			};
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
			saveModuleWithoutReload();
			$scope.undoEnable = false;
		}
		
		$scope.isClonable = false;
		$scope.copy = function(event){
			if (event.ctrlKey) {
				$scope.isClonable = true;
			} 
		}

		$scope.scrollTo = function( target){
			var scrollPane = $("body");
			var scrollTarget = $('#'+target);
			var scrollY = scrollTarget.offset().top - 150;
			scrollPane.animate({scrollTop : scrollY }, 2000, 'swing');
		};

        $scope.highlightNode = function(idNode){
        	var elementId = 'node-'+idNode;
        	$scope.scrollTo(elementId);
        	$('#'+elementId).toggleClass('highlight');  
        	   setTimeout(function(){
        	     $('#'+elementId).toggleClass('highlight');  
        	   },5000);
        }
        
        $scope.toggleCollapse = function(node,scope){
        	scope.toggle();
        	if(node.collapsed){
        		node.collapsed = false;
        	}else{
        		node.collapsed = true;
        	}
        	var o = getObject(TabsCache.get(moduleIdNode)[0].nodes,node.idNode);
        	if(!angular.isUndefined(o)){
        		o.collapsed = node.collapsed;
        	}
        }
        
        function getObject (array,idNode){
        	var object = _.find(array, _.matchesProperty('idNode', idNode));
        	if(object != null || !angular.isUndefined(object)){
    			return object;
    		}
        	_.forEach(array,function(v,k) {
        		if(v.nodes){
        			return getObject(v.nodes,idNode);
        		}
        	});
        }
        $rootScope.tabsLoading = false;
        
        $scope.setActiveRule = function(rule,el){
        	$scope.activeRuleDialog = el;
        	$scope.activeRule = rule;
        }
        $scope.addToActiveRule = function(node,rules){
        	
        	var rule = $scope.activeRule;
        	var bAlreadyInRule = false;
        	for(var i=0;i<rule.conditions.length;i++){
        		var iCondition = rule.conditions[i];
        		if(iCondition.idNode==node.idNode){
        			bAlreadyInRule = true;
        			break;
        		}
        	}
        	if(!bAlreadyInRule){
        		rule.conditions.push(node);
            	if(rules.rules==null){
            		rules.rules = [];
            	}
            	var ruleLevel = "";
            	if(rule.level==0){
            		ruleLevel = "probHigh";
            	}else if(rule.level==1){
            		ruleLevel = "probMedium";
            	}else if(rule.level==2){
            		ruleLevel = "probLow";
            	}else if(rule.level==3){
            		ruleLevel = "probUnknown";
            	}else if(rule.level==4){
            		ruleLevel = "possUnknown";
            	}else if(rule.level==5){
            		ruleLevel = "noExposure";
            	}
            	rules.rules.push({
            		ruleLevel:ruleLevel,
            		idNode:node.idNode
            		})
            	RulesService.save(rule).then(function(response){
    				if(response.status === 200){
    					console.log('Rule Save was Successful!');
    					
    				}
    			});
        	}
        }
        $scope.saveRule = function(rule,model){
        	RulesService.save(rule).then(function(response){
    			if(response.status === 200){
    				console.log('Rule Save was Successful!');	
    				ModuleRuleService.getModuleRule(model.idNode).then(function(response) {
						if(response.status === 200){
							var result = response.data[response.data.length-1];
							if(angular.isUndefined(model.moduleRule)){
								model.moduleRule = [];
							}
							_.merge(model.moduleRule, response.data);
			    			if (!model.moduleRule.$$phase) {
			    			        try {
			    			        	model.moduleRule.$digest();
			    			        }
			    			        catch (e) { }
			    		    }
						}
						});
    			}
    		});
        	
        }
        $scope.removeNodeFromRule = function(node){
        	var rule = $scope.activeRule;
        	
        	for(var i=0;i<rule.conditions.length;i++){
        		var iCondition = rule.conditions[i];
        		if(iCondition.idNode==node.idNode){
        			rule.conditions.splice(i,1);
        			
        		}
        	}
        	RulesService.save(rule).then(function(response){
				if(response.status === 200){
					console.log('Rule Save was Successful!');
					
				}
			});
        }
	}
})();