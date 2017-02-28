(function() {
	angular.module('occIDEASApp.FiredRules').controller('FiredRulesCtrl',
			FiredRulesCtrl);

	FiredRulesCtrl.$inject = [ '$scope', 'data','FiredRulesService','$timeout',
	                           'InterviewsService','AssessmentsService','$log','$compile',
	                           'RulesService','ngToast','SystemPropertyService', '$mdDialog','AgentsService', 
	                           '$q','$sessionStorage','moduleName','$rootScope','ManualAssessmentService','ngToast'];
	function FiredRulesCtrl($scope, data,FiredRulesService,$timeout,
			InterviewsService,AssessmentsService,$log,$compile,
			RulesService,$ngToast,SystemPropertyService, $mdDialog,
			AgentsService,$q, $sessionStorage,moduleName,$rootScope,ManualAssessmentService,ngToast) {
		var vm = this;
		vm.firedRulesByModule = [];
		$scope.interview = undefined;
		$scope.interviewId = data;
		$scope.linkedModule = undefined;
		vm.answersDisplayed = false;
		$scope.data = data;
		$scope.moduleName = moduleName;
		$scope.loadingTree = false;
		$scope.openAnswerSummary = function(node){
			$scope.openAnswerSummaryTab(node,$scope.moduleName,$scope.interviewId);
		}
		
		$(window).scroll(function () {
            if ($(this).scrollTop() > 50) {
                $('#back-to-top').fadeIn();
            } else {
                $('#back-to-top').fadeOut();
            }
        });
        // scroll body to 0px on click
        $('#back-to-top').click(function () {
            $('#back-to-top').tooltip('hide');
            $('body,html').animate({
                scrollTop: 0
            }, 800);
            return false;
        });
        
        $('#back-to-top').tooltip('show');
        
		$scope.statuses = ['Incomplete','Needs Review','Complete'];				
		$scope.onChangeSaveStatus = function (){
			if($scope.interview){
				var interview = $scope.interview;
				interview.assessedStatus = $scope.assessmentStatus;
				if (!(interview.notes)) {
					interview.notes = [];
				}
				interview.notes.push({
					interviewId : interview.interviewId,
					text : "Updated Status",
					type : 'System'
				});
				saveInterview(interview);
			}		
		};
		
		$scope.toggleAgentView = function (agent){		
			var agentShown = _.find($scope.agents,function(a){
				return agent.idAgent == a.idAgent;
			});
			
			if(agent.agentGroup){
				agentGroup = _.find($scope.agentsData,function(value){				
					return agent.agentGroup.name === value.key;				
				});
				
				agentData = _.find(agentGroup.value,function(value){
					return value.idAgent === agent.idAgent;
				});	
			}
			
			if(agentShown && agent.idAgent){
				//Hide the agent
				_.remove($scope.agents, function(a) {
					  //Remove from agents table
					  return agent.idAgent == a.idAgent;
				});
				
				if(agentData){
					agentData.style = "";	
				}				
				
			} else if(agent.idAgent){
				//Show the agent
				var showAgent = {name:agent.name,idAgent:agent.idAgent};
				//Push to agents table
				$scope.agents.push(showAgent);
				
				agentData.style = "agent-shown";
			}
		};
		$scope.openedAgentGroup = function (agentGroup){
			for(var i=0;i<agentGroup.value.length;i++){
				var agent = agentGroup.value[i];
				var agentShown = _.find($scope.agents,function(a){
					return agent.idAgent == a.idAgent;
				  });
				if(agentShown){
					if(agentGroup.isOpened){
						agent.style = "";
						_.remove($scope.agents, function(a) {
							  return agent.idAgent == a.idAgent;
							});
					}
					
				} else{
					if(!agentGroup.isOpened){
						if(agent.total > 0){
							agent.style = "agent-shown";
							var showAgent = {name:agent.name,idAgent:agent.idAgent};
							$scope.agents.push(showAgent);
						}
					}					
				}	
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
		    	//$scope.agentsLoading = true;
				initAgentData();
			}
		};
		
		if(!$scope.linkedModule){
			refreshInterviewDisplay();
		}
		function setOrder (obj) {
    	    var out = [];
    	    _.forEach(obj,function(value,key) {
    	      out.push({ key: key, value: value ,total: value.total});
    	    });
    	    return out;
    	}
		function initAgentData(){
			AgentsService.getStudyAgents().then(function(agent) {				
				
	    		var group = _.groupBy(agent, function(b) { 
	    			return b.agentGroup.name;
	    		});
	    		
        		_.forOwn(group, function(x, key) { 
	        		var totalVal = 0; 
	        		_.forEach(x,function(v,k) {
	        			  var ruleArray =_.filter($scope.data.firedRules, function(r){
	        					return v.idAgent === r.agentId; 
	        			  });
	        			  var uniqueArray = _.map(_.groupBy(ruleArray,function(rule){
	        				  return rule.idRule;
	        				}),function(grouped){
	        				  return grouped[0];
	        				});
	        			  v.total = uniqueArray.length;
	        			  totalVal = totalVal + v.total;
	        			});
	        		x.total = totalVal;
        		} );
        		
				_.forEach(group,function(item,key) {					
					_.forEach(item,function(i,k) {
						//Mark on-zero as shown
						if(i.total > 0){
							i.style = "agent-shown";	
						}					
					});					
				});	
				
				//Group
        		group = setOrder(group);
        		
        		//Sort by group count
        		group = _.sortBy(group, function(item){
        		    return item.total * -1;
        		});
        		
        		_.forEach(group,function(item,key) {
        			//Sort inner group
        			item.value = _.sortBy(item.value, function(inner){
            		    return inner.total * -1;
            		});
        			
        			//Open non-zero group by default
        			if(item.total > 0){
        				item.isOpened = true;
        			}
        		});
        		
	    		$scope.agentsData = group;
	    		safeDigest($scope.agentsData);
	    		
	    	});
		}
		
		function refreshInterviewDisplay(){
			
			if(!$scope.linkedModule){
				
				AssessmentsService.getFiredRules($scope.interviewId).then(function(response){
					if(response.status == '200'){	
						
						$scope.interview = response.data[0];
						$scope.assessmentStatus = $scope.interview.assessedStatus;
						$scope.data = response.data[0];
						
						$('#back-to-top').fadeOut();
					}
                });
			}
			
			AgentsService.getStudyAgentsWithRules($scope.interviewId).then(function(response) {
				$scope.agents = [];
				
				_.each(response, function(agent) {
					$scope.agents.push(agent);
				});
				
				if($scope.agents.length==0 && $scope.interview){
					$scope.agents = $scope.interview.agents;
				}
			});
			
			InterviewsService.findModulesByInterviewId($scope.interviewId).then(function(response){
				if(response.status == '200'){
					if(response.data.length > 0){								
						vm.modulesInInterview = response.data;
						//vm.firedRulesByModule = $scope.modulesInInterview
					}
				}
			});
			InterviewsService.findFragmentsByInterviewId($scope.interviewId).then(function(response){
				if(response.status == '200'){
					if(response.data.length > 0){								
						vm.fragmentsInInterview = response.data;
						//vm.firedRulesByModule = $scope.modulesInInterview
					}
				}
			});
		}
		
		function processQuestionHistory(){
			
			InterviewsService.getModuleForInterview($scope.interviewId).then(function(response){
				if(response.status == '200' && response.data && response.data[0]){
					$scope.loadingTree = false;
					$scope.linkedModule = response.data[0];
					addHeader($scope.linkedModule.nodes);					
				}
				else{
					$scope.loadingTree = false;
				}
			});
		}
		
		function addHeader(nodes){
			
			_.each(nodes, function(node) {
				
				  if(node.nodes){
					  var temp = node.nodes[0];
					  
					  if (temp && temp.name == "Ignore")
					  {
						  node.nodes = temp.nodes;
					  }
				  }
				  
				  var linkNode = _.find($scope.interview.questionHistory,function(qnode){
					  var retValue = false;
					  if(qnode.link){
						  if(qnode.link == node.topNodeId){
							  retValue = true;
						  }
					  }
					  return retValue;
				  });
				  if(linkNode){
					  node.header = linkNode.name.substr(0,4);
				  }
				  
				  if(node.nodes){
					  addHeader(node.nodes);
				  }
			});
		}
		function refreshAssessmentDisplay(){
			AssessmentsService.updateFiredRules($scope.interviewId)
			.then(function(response){
				$log.info("refreshAssessmentDisplay");
				$scope.data = response.data[0];
			});
			//getFiredRulesByInterviewId($scope.interview.interviewId);
			
		}
		vm.interviewFiredRules = null;
		function getFiredRulesByInterviewId(interviewId){
			FiredRulesService.getByInterviewId(interviewId).then(function(response){
				if(response.status == '200'){
					var interviewFiredRules = response.data;
					vm.interviewFiredRules = interviewFiredRules;
					InterviewsService.findModulesByInterviewId(interviewId).then(function(response){
						if(response.status == '200'){
							if(response.data.length > 0){								
								vm.modulesInInterview = response.data;
								//vm.firedRulesByModule = $scope.modulesInInterview
							}
						}
					});
					
					//loop to each fired rules and construct object to be used by the
					//view
					_.each(interviewFiredRules,function(data){
						//loop rules
						_.each(data.rules,function(rule){
							var condition = rule.conditions[0];
							if(condition){
								FiredRulesService.findNodeById(condition.topNodeId)
									.then(function(response){
										if(response.status == 200){
											//find module details by idNode
											var module = response.data;
											var existingModule = _.find(vm.firedRulesByModule,function(mod){
												return mod.idNode == module.idNode;
											});
											if(!existingModule){
												existingModule = {
														idNode:module.idNode,
														name:module.name,
														firedRules:[]
												}; 
												vm.firedRulesByModule.push(existingModule);
											}
										
										// check if agent has already been registered
										// otherwise register agentId
										// this is to group it by agent
										var existingAgent = _.find(existingModule.firedRules,function(agent){
											return agent.idAgent == rule.agent.agentGroup.idAgent;
										});
										if(!existingAgent){
											existingAgent = {
													idAgent:rule.agent.agentGroup.idAgent,
													child:[],
													agentGroupName:rule.agent.agentGroup.name
											};
											var isSSAgent = _.find($scope.agents,function(agent){
												return agent.idAgent == rule.agent.idAgent;
											});
											if(isSSAgent){
												existingModule.firedRules.push(existingAgent);
											}											
										}
										//make sure we dont get duplicate
										var existingRule = _.find(existingAgent.child,function(exRule){
											return rule.idRule == exRule.idRule;
										});
										if(!existingRule){
										existingAgent.child.push({
											agentName:rule.agent.name,
											idRule:rule.idRule,
											level:rule.level,
											levelValue:rule.levelValue,
											conditions:rule.conditions
										});
										}
										
										}
									});
								
							}
						});
					});
				}
			});
		}
		var safeDigest = function (obj){
        	if (!obj.$$phase) {
		        try {
		        	obj.$digest();
		        }
		        catch (e) { }
        	}
        }
		$scope.closeIntDialog = function(elem,$event) {
			$($event.target).closest('.int-note').remove();
        	$scope.activeIntRuleDialog = '';
        	$scope.activeIntRuleCell = '';
        	safeDigest($scope.activeIntRuleDialog);
        	safeDigest($scope.activeIntRuleCell);
        	noteIntZindex = 1050;
        };
        
        $scope.highlightFragment = function(fragment){        	
        	
        	if(!$scope.linkedModule){
        		ngToast.create({
		    		  className: 'warning',
		    		  content: "Please click Show Questions button."
		    	 });
        		return;
        	}  
        	
        	var expandDefer = $q.defer();
        	var defer = $q.defer();
        	var idNode = fragment.idFragment;
        	var elementId = 'node-' + fragment.idFragment;
        	
        	//Check if fragment is processed
        	InterviewsService.checkFragmentProcessed(fragment).then(function(response){
        		if(response.status == '200'){
        			
        			var processedIdNode = response.data;
        			
        			if(processedIdNode == 0){
        				console.log(fragment.idFragment +" not processed");
        				expandDefer.resolve();
        			}   
        			else{
        				idNode = processedIdNode;
        				elementId = 'node-'+processedIdNode;
        				console.log(processedIdNode +" processed");
        				defer.resolve();
        			}
        		}        		
        	});  
        	
        	defer.promise.then(function(){
            	
	        	var moduleDefer = $q.defer();        
	        	var tempDefer = $q.defer(); 
	        	
	        	if ($('#' + idNode).length === 0) {        	
	        		//Not loaded yet
	        	
	        		if($scope.linkedModule){
	        			loadChildNode($scope.linkedModule.nodes, idNode, idNode, moduleDefer);
	        		}      
	        		
	        		$scope.$watch(function() {
	        			return document.getElementById(idNode) != null;
					}, function() {
						if(angular.element(document.getElementById(idNode)).scope()){	 
							expandNode(angular.element(document.getElementById(idNode)).scope());
							expandDefer.resolve();							
	    				}   						    					
					}); 		
	        	}        	
	        	else{
	        		//Might be collapsed
	        		var elementScope = angular.element(document.getElementById(idNode)).scope();
	        		expandNode(elementScope);
	        		expandDefer.resolve();
	        	}        	
        	});
        	
        	expandDefer.promise.then(function(){
        		
    			if ($('#'+elementId).length == 0) {
    				$ngToast.create({
        	    		  className: 'warning',
        	    		  content: 'The node ' + idNode + ' does not exist.',
        	    		  animation:'slide'
        	    	 });
    				return false;
        		}
        		else{        			
        			$scope.scrollTo(elementId);		
        			
        			var temp = angular.element(document.getElementById(elementId)).scope();
        			if(temp){
        				
                		$scope.toggleNode(temp.$modelValue, temp);
                		temp.toggle();	
        			}  
        			
        			$('#'+elementId).addClass('highlight-rulenode');        			
        			//FIXME Hack to re-calculate correct offset().top value for the new element
        			$scope.scrollTo(elementId);        			      			
        		}
        	});
        }        
        
        $scope.highlightNode = function(idNode, node){
        	
        	var expandDefer = $q.defer();
        	var defer = $q.defer();
        	
        	//Initial check if node exists
        	InterviewsService.checkQuestionAnswered(idNode, $scope.interviewId).then(function(response){
        		if(response.status == '200'){
        			var answered = response.data;
        			
        			if(answered == 0){
        				console.log(idNode +" not answered");
        				expandDefer.resolve();
        			}   
        			else{
        				console.log(idNode +" answered");
        				defer.resolve();
        			}
        		}        		
        	});        	
        	
        	$(".tree-node-content span").removeClass("highlight-rulenode");
        	var elementId = 'node-' + idNode;	
        	
        	defer.promise.then(function(){
        	
	        	var moduleDefer = $q.defer();        
	        	var tempDefer = $q.defer(); 
	        	
	        	if ($('#' + idNode).length === 0) {        	
	        		//Not loaded yet
	        	
	        		if($scope.linkedModule){
	        			loadChildNode($scope.linkedModule.nodes, idNode, node.topNodeId, moduleDefer);
	        		}        		
	        		
	        		moduleDefer.promise.then(function(){
	        			if(node.topNodeId != $sessionStorage.activeIntro.value){
	        				//Must be a fragment
	        				loadChildNode($scope.linkedModule.nodes, idNode, node.topNodeId, tempDefer);
	        			}
	        			else{
	        				//Should be a module
	        				expandDefer.resolve();
	        			}
	        		});
	        		
	        		$scope.$watch(function() {
	        			
	        			return document.getElementById(idNode) != null;
					}, function() {
						if(angular.element(document.getElementById(idNode)).scope()){	    					
	    					expandNode(angular.element(document.getElementById(idNode)).scope());
	        				expandDefer.resolve();		
	    				}   						    					
					});
	        		
	        	}        	
	        	else{
	        		//Might be collapsed
	        		var elementScope = angular.element(document.getElementById(idNode)).scope();
	        		expandNode(elementScope);
	        		expandDefer.resolve();
	        	}        	
        	});
        	
        	expandDefer.promise.then(function(){
    			if ($('#node-' + idNode).length == 0) {
    				$ngToast.create({
        	    		  className: 'warning',
        	    		  content: 'The node ' + idNode + ' does not exist.',
        	    		  animation:'slide'
        	    	 });
    				return false;
        		}
        		else{        			
        			$scope.scrollTo(elementId);			
        			$('#'+elementId).addClass('highlight-rulenode');        			
        			//FIXME Hack to re-calculate correct offset().top value for the new element
        			$scope.scrollTo(elementId);
        		}
        	});
        }
        
        function loadChildNode(parentNode, idNode, topNodeId, defer){
        	
        	for(var i = 0; i < parentNode.length; i++) {
        		node = parentNode[i];        		
        		
        		if ( node.type == "Q_linkedmodule" 
        		|| ((node.type == "Q_linkedajsm" && node.link == topNodeId))){
        			//Check if loaded 
        			
        			if(node.nodes == null || node.nodes.length == 0){
        				//Load module
        				loadModule(node, topNodeId, idNode, defer);        				
        			}
        			else{
        				
        				if ((node.type == "Q_linkedmodule" && node.idNode == idNode)
        				|| node.type == "Q_linkedajsm" && node.link == topNodeId){
        					defer.resolve();	    							
							return true;
        				}        				
        			}
        		}
	        	
        		if(node.nodes != null && node.nodes.length > 0){        			
        			loadChildNode(node.nodes, idNode, topNodeId, defer);	
        		}	        	
        	}
        }
        
        function loadModule(node, topNodeId, idNode, defer){
        	
        	InterviewsService.getModuleForSubModule($scope.interviewId, 
					node.link).then(function(response){
						
				if(response.status == '200' && response.data && response.data[0]){
					subNode = response.data[0];
					addHeader(subNode.nodes);
					
					$scope.safeApply(function(){
						node.nodes = subNode.nodes;
					});
					
					safeDigest($scope.linkedModule);
					
					if(topNodeId != $sessionStorage.activeIntro.value 
						|| node.idNode == idNode 
						|| node.link == topNodeId){
						
						defer.resolve();	    							
						return true;	
					}
				}	
			});        	
        }
        
        function expandNode(node){
        	
        	if(!angular.isUndefined(node) || node != null){
        		if(typeof node.expand === 'function'){
            		node.expand();
            	}
            	else if(typeof node.$nodesScope.expand === 'function'){
            		node.expand();
            	}
            	
            	if(node.$parentNodesScope){
            		expandNode(node.$parentNodesScope);
            	}
        	}      	
        }        
        
		$scope.scrollTo = function( target){
			var scrollPane = $("body");
			var scrollTarget = $('#'+target);
			var scrollY = scrollTarget.offset().top - 150;			
			scrollPane.animate({scrollTop : scrollY }, 500, 'swing' , function(){
				
				if(scrollY < 0){
					//When scrollTarget.offset().top = 0
					console.log("Re-scroll");
					$scope.scrollTo(target);
				}
			});
		};

		vm.showRulesMenu = function(scope){
			var menu = angular.copy(vm.rulesMenuOptions);
			if(scope.agent.idAgent!=116){
				_.remove(menu, {
				    0: 'Run Noise Assessment'
				});
			}
			if(scope.agent.idAgent!=157){
				_.remove(menu, {
				    0: 'Run Vibration Assessment'
				});
			}
			return menu;
		}
		vm.showAssessmentsMenu = function(scope){
			return vm.assessmentsMenuOptions;
		}
		vm.showEditAssessmentMenu = function(scope){
			return vm.editAssessmentsMenuOptions;
		}
		function cascadeFindNode(nodes,node){
			_.each(nodes, function(data) {
				if(data.answerId == node.idNode){
					$scope.foundNode = data;
				}else{
					if(data.nodes){
						if(data.nodes.length>0){
							cascadeFindNode(data.nodes,node);
						}
					}
				}	
						 
			});
		}
		
		function findFrequencyIdNode(node){ 
			var childQuestionNode = _.find($scope.interview.questionHistory,function(qnode){
				var retValue = false;
				  
				if(qnode.parentAnswerId == node.answerId){
					retValue = true;
				}				  
				return retValue;
			});
			if(childQuestionNode){
				var childFrequencyNode = _.find($scope.interview.answerHistory,function(anode){
					var retValue = false;
					  
					if(anode.parentQuestionId == childQuestionNode.questionId){
						retValue = true;
					}				  
					return retValue;
				});
				return childFrequencyNode;
			}else{
				return;
			}
			
		}
		vm.rulesMenuOptions =
			[
			  [ 'Show Rules', function($itemScope, $event, model) {
				  
				  var ruleArray =_.filter($scope.data.firedRules, function(r){
						return $itemScope.agent.idAgent == r.agentId; 
				  	});
				  	 
				  	for(var i=0;i<ruleArray.length;i++){
					  	var scope = $itemScope.$new();
				  		scope.model = model;
				  		scope.rule = ruleArray[i];
				  		scope.agentName = $itemScope.agent.name;
						if($("#rule-dialog-"+$scope.interviewId+"-"+scope.rule.idRule).length == 0){
							firedRuleDialog($event.currentTarget.parentElement,scope,$compile,$scope.interviewId);
						}
				  	}
			  	}			  
			  ],
			  [ 'Run Noise Assessment', function($itemScope, $event, model) {
				  var bFoundNoiseRules = false;
				  var noiseRules = [];
				  for(var i=0;i<model.agents.length;i++){
					  var agentAssessing = model.agents[i]
					  var rule = {levelValue:99};
					  for(var j=0;j<model.firedRules.length;j++){
						  var firedRule = model.firedRules[j];
						  if(agentAssessing.idAgent == firedRule.agent.idAgent){
							  if(firedRule.agent.idAgent==116){
								  bFoundNoiseRules = true;
								  noiseRules.push(firedRule);
							  }
						  }	  
					  }  
				  }
				  if(bFoundNoiseRules){
					  var totalPartialExposure = 0;
					  var totalPartialExposurePerAdj = 0;
					  var peakNoise = 0;
					  var maxBackgroundPartialExposure = 0;
					  var maxBackgroundHours = 0;
					  
					  $scope.noiseRows = [];
					  var totalFrequency = 0;
					  var backgroundHours = 0;
					  var shiftHours = 0;
					  for(var m=0;m<model.answerHistory.length;m++){
						  var iqa = model.answerHistory[m];
						  if(iqa.type=='P_frequencyshifthours'){
							  shiftHours = iqa.answerFreetext;
							  break;
						  }
					  }
					  for(var k=0;k<noiseRules.length;k++){
						  var noiseRule = noiseRules[k];
						  if(noiseRule.type!='BACKGROUND'){
							  var parentNode = noiseRule.conditions[0];
							  //if(model.module){
								//  cascadeFindNode(model.module.nodes,parentNode);
							  //}else{
								  cascadeFindNode(model.answerHistory,parentNode); 
							  //}
							  var answeredValue = 0;
							  if($scope.foundNode){
								 var frequencyHoursNode = findFrequencyIdNode($scope.foundNode);
									
								 if(frequencyHoursNode){
									 if(frequencyHoursNode.type!='P_frequencyseconds'){
										 answeredValue = frequencyHoursNode.answerFreetext;
									 }									 
								 }								
								 $scope.foundNode=null;
							  }
							  
							  totalFrequency += Number(answeredValue);
						  }
					  }
					  var useRatio = false;
					  var ratio = 1.0;
					  if(totalFrequency>shiftHours){
						  useRatio = true;
						  ratio = parseFloat(totalFrequency)/parseFloat(shiftHours);
						  ratio = ratio.toFixed(4);
					  }				
					  var level = 0;
					  var peakNoise = 0;
					  for(var k=0;k<noiseRules.length;k++){
						  var noiseRule = noiseRules[k];
						  if(noiseRule.type=='BACKGROUND'){
							var hoursbg = shiftHours-totalFrequency;
							if(hoursbg<0){
								hoursbg = 0;
							}
							if(noiseRule.ruleAdditionalfields[0]==undefined){
								$ngToast.create({
				    	    		  className: 'danger',
				    	    		  content: 'The background rule ' + noiseRule.idRule + ' has no dB value.',
				    	    		  dismissButton:true,
				    	    		  animation:'slide'
				    	    	 });
							}
							level = noiseRule.ruleAdditionalfields[0].value;
							hoursbg = hoursbg.toFixed(4);
							var partialExposure = 4*hoursbg*(Math.pow(10,(level-100)/10));
							partialExposure = partialExposure.toFixed(4);
							
							
							var moduleName = getModuleNameOfNode(noiseRule.conditions[0]);
							var noiseRow = {nodeNumber:noiseRule.conditions[0].number,
											idNode:noiseRule.conditions[0].idNode,
											nodeText:noiseRule.conditions[0].name,
											dB:level+'B',
											backgroundhours: hoursbg,
											partialExposure:partialExposure,
											type:'backgroundNoise',
											moduleName: moduleName,
											topNodeId:noiseRule.conditions[0].topNodeId}
							
							$scope.noiseRows.push(noiseRow);
							if(partialExposure>maxBackgroundPartialExposure){
								maxBackgroundPartialExposure = partialExposure;
								maxBackgroundHours = hoursbg;
							}
						  }else{
							var hours = 0.0;
							var frequencyhours = 0;
							var parentNode = noiseRule.conditions[0];
							var isSecondsTimeFrequency = false;
							//if(model.module){
								  cascadeFindNode(model.answerHistory,parentNode);
							//  }else{
							//	  cascadeFindNode(model.fragment.nodes,parentNode); 
							 // }
							  if($scope.foundNode){
								  var frequencyHoursNode = findFrequencyIdNode($scope.foundNode);								  
								  if(frequencyHoursNode){
									  frequencyhours = frequencyHoursNode.answerFreetext;
									  if(frequencyHoursNode.type=='P_frequencyseconds'){
										  frequencyhours = parseFloat(frequencyhours)/3600; //convert seconds to hours
										  isSecondsTimeFrequency = true;
									  }
								  }
								  $scope.foundNode=null;
							  }
							if(useRatio && !isSecondsTimeFrequency){
								hours = parseFloat(frequencyhours)/ratio;		
							}else{
								hours = parseFloat(frequencyhours);		
							}
							level = noiseRule.ruleAdditionalfields[0].value;
							var percentage = 100;
							var partialExposure = 4*hours*(Math.pow(10,(level-100)/10));
							partialExposure = partialExposure.toFixed(4);				
							hours = hours.toFixed(4);
							var modHours = "";
							if(useRatio){
								modHours = "*"+hours+"*";
							}else{
								modHours = hours;
							}
							var moduleName = getModuleNameOfNode(noiseRule.conditions[0]);
							var noiseRow = {nodeNumber:noiseRule.conditions[0].number,
									idNode:noiseRule.conditions[0].idNode,
									nodeText:noiseRule.conditions[0].name,
									dB:level,
									backgroundhours: modHours,
									partialExposure:partialExposure,
									moduleName:moduleName,
									topNodeId:noiseRule.conditions[0].topNodeId}
					
							$scope.noiseRows.push(noiseRow);	
							totalPartialExposure = (parseFloat(totalPartialExposure)+parseFloat(partialExposure));
							
						  }
						  if(peakNoise<Number(level)){
							peakNoise = Number(level);
						  }
					  }
					  	totalPartialExposure = (parseFloat(totalPartialExposure)+parseFloat(maxBackgroundPartialExposure));
					  	totalPartialExposure = totalPartialExposure.toFixed(4);
						totalFrequency += maxBackgroundHours;

						var autoExposureLevel = 10*(Math.log10(totalPartialExposure/(3.2*(Math.pow(10,-9)))))
						autoExposureLevel = autoExposureLevel.toFixed(4);
						$scope.totalPartialExposure = totalPartialExposure;
						$scope.autoExposureLevel = autoExposureLevel;
						$scope.peakNoise = peakNoise;
						$scope.shiftHours = shiftHours;
				  }
			  	}			  
			  ],
			  [ 'Run Vibration Assessment', function($itemScope, $event, model) {
				  var bFoundVibrationRules = false;
				  var vibrationRules = [];
				  
				  for(var i=0;i<model.agents.length;i++){
					  var agentAssessing = model.agents[i]
					  var rule = {levelValue:99};
					  for(var j=0;j<model.firedRules.length;j++){
						  var firedRule = model.firedRules[j];
						  if(agentAssessing.idAgent == firedRule.agent.idAgent){
							  if(firedRule.agent.idAgent==157){
								  bFoundVibrationRules = true;
								  vibrationRules.push(firedRule);
							  }
						  }	  
					  }  
				  }
				  if(bFoundVibrationRules){
					  var totalExposure = 0;
					  var totalFrequency = 0;
					  var dailyVibration = 0;
					  var level = 0;					  				  
					  $scope.vibrationRows = [];
					  /*for(var m=0;m<model.questionsAsked.length;m++){
						  var iqa = model.questionsAsked[m];
						  if(iqa.possibleAnswer.type=='P_frequencyshifthours'){
							  shiftHours = iqa.interviewQuestionAnswerFreetext;
							  break;
						  }
					  }*/
					  for(var k=0;k<vibrationRules.length;k++){
						  var vibrationRule = vibrationRules[k];
						  
						  var parentNode = vibrationRule.conditions[0];
						  cascadeFindNode(model.answerHistory,parentNode); 
						  var frequencyhours = 0;
						  if($scope.foundNode){
							  var frequencyHoursNode = findFrequencyIdNode($scope.foundNode);								  
							  if(frequencyHoursNode){
								  frequencyhours = frequencyHoursNode.answerFreetext;
							  }
							  $scope.foundNode=null;
						  }
						  var level = vibrationRule.ruleAdditionalfields[0].value;
						  var moduleName = getModuleNameOfNode(vibrationRule.conditions[0]);
						  particalVibration = Math.sqrt(Number(frequencyhours)*Number(frequencyhours)*Number(level)/8);

						  var vibrationRow = {nodeNumber:vibrationRule.conditions[0].number,
								  	idNode:vibrationRule.conditions[0].idNode,
								  	nodeText:vibrationRule.conditions[0].name,
								  	vibMag:level,								
									frequencyhours:frequencyhours.toFixed(4),
									partialExposure:particalVibration.toFixed(4),
									type:'vibration',
									moduleName:moduleName,
									topNodeId:vibrationRule.conditions[0].topNodeId}
						  
						  $scope.vibrationRows.push(vibrationRow);
						  totalFrequency += Number(frequencyhours);
						  totalExposure += Number(level);
						  dailyVibration += Number(particalVibration);
						  
					  }
					  
					 
					  //dailyVibration = Math.sqrt(Number(totalFrequency)*Number(totalFrequency)*Number(totalExposure)/8);

					  $scope.dailyVibration = dailyVibration.toFixed(4);
				  }
			  	}			  
			  ]
			];
		vm.assessmentsMenuOptions =
			[
			  [ 'Update Fired Rules', function($itemScope, $event, model) {
				  
				  
				  AssessmentsService.updateFiredRules(model.interviewId).then(function (response) {
		                if (response.status === 200) {
		                	$log.info("Updated Fired Rules");
		                	$scope.data = response.data[0];
		                }
				  });
			  	}			  
			  ],
			  [ 'Run Auto Assessment', function($itemScope, $event, model) {
				  AssessmentsService.updateFiredRules(model.interviewId).then(function (response) {
		                if (response.status === 200) {
		                	$log.info("Updated Fired Rules");
		                	$scope.data = response.data[0];
		                	var rules = [];
		                	if($scope.data.autoAssessedRules.length>0){
		                		for(var i=0;i<$scope.data.autoAssessedRules.length;i++){
		                			var rule = $scope.data.autoAssessedRules[i];
		                			rule.deleted=1;
		                			rules.push(rule);
		                		}
		                    	RulesService.saveList(rules).then(function(response){
		                			if(response.status === 200){
		                				$log.info('Rules SaveList was Successful!'+rule);
		                			}
		                		});
		                	}
		                	$scope.data.autoAssessedRules = [];
		                	AgentsService.getStudyAgents().then(function(agents) {
		                		for(var i=0;i<agents.length;i++){
				  					var agentAssessing = agents[i]
				  					var rule = {agentId:agentAssessing.idAgent,level:'noExposure',levelValue:5};
				  					for(var j=0;j<$scope.data.firedRules.length;j++){
				  						var firedRule = $scope.data.firedRules[j];
				  						if(agentAssessing.idAgent == firedRule.agent.idAgent){
				  							if(firedRule.levelValue<rule.levelValue){
				  								//rule = firedRule;
				  								rule = {agentId:agentAssessing.idAgent,level:firedRule.level,levelValue:firedRule.levelValue}					
				  							}
				  						}	  
				  					}
				  					$scope.data.autoAssessedRules.push(rule);
				  				 }
				  				 InterviewsService.save($scope.data).then(function (response) {
			  		                if (response.status === 200) {
			  		                	$log.info("Interview saved with auto assessments");
			  		                }
				  				 });
		                	});
			  				
		                }
				  });
				  
				  
			  	}			  
			  ],
			  [ 'Use Auto', function($itemScope, $event, model) {
				  var rules = [];
                    if(model.manualAssessedRules.length>0){
                    	for(var i=0;i<$scope.data.manualAssessedRules.length;i++){
                			var rule = $scope.data.manualAssessedRules[i];
                			rule.deleted=1;
                			rules.push(rule);
                		}
                    	RulesService.saveList(rules).then(function(response){
                			if(response.status === 200){
                				$log.info('Rules SaveList was Successful!'+rule);
                			}
                		});
 
                    }
                    model.manualAssessedRules = [];
  				  
                    var interviewManualAssessments = [];
                    var assessments = angular.copy(model.autoAssessedRules);
                    for(var i=0;i<assessments.length;i++){
					  var assessment = assessments[i];
					  var manualAssessment = {agentId:assessment.agentId,level:assessment.level,levelValue:assessment.levelValue};
					  
					  //model.manualAssessedRules.push(manualAssessment);
					  
						var interviewManualAssessment = {idInterview:$scope.interviewId,rule:manualAssessment};
						interviewManualAssessments.push(interviewManualAssessment);		
		    										  
                    }
                    ManualAssessmentService.saveManualAssessments(interviewManualAssessments).then(function (response) {
		                if (response.status === 200) {
		                	$log.info("manual assessment saved");
		                	for(var i=0;i<response.data.length;i++){
		                		model.manualAssessedRules.push(response.data[i].rule);
		                	}               	
		                }
                    });
                    
                    
			  	}
			  ]
			];
		vm.editAssessmentsMenuOptions =
			[
			  [ 'Edit Assessment', function($itemScope, $event, model) {
				  var ruleArray =_.filter(model.manualAssessedRules, function(r){
						return $itemScope.agent.idAgent == r.agentId; 
				  	});
				  	 
				  	for(var i=0;i<ruleArray.length;i++){
				  		var rule = ruleArray[i];				  	
				  		if(rule.deleted==0){
				  			var scope = $itemScope.$new();
					  		scope.model = model;
					  		scope.rule = rule;
					  		scope.agentName = $itemScope.agent.name;
					  		editAssessmentDialog($event.currentTarget.parentElement,scope,$compile,$scope.interviewId);
				  		}					  	
				  	}
			  	}			  
			  ]
			];
		
		$scope.saveRule = function(rule){
        	RulesService.save(rule).then(function(response){
    			if(response.status === 200){
    				$log.info('Rule Save was Successful!'+rule);
    			}
    		});
        	
        }
		function getModuleNameOfNode(node){
			var moduleName = "";
			var linkNode = _.find($scope.interview.questionHistory,function(qnode){
				  var retValue = false;
				  if(qnode.link){
					  if(qnode.link == node.topNodeId){
						  retValue = true;
					  }
				  }
				  return retValue;
			  });
			  if(linkNode){
				  moduleName = linkNode.name.substr(0,4);
			  } 
			return moduleName;  
		}
		$scope.showNotePrompt = function(ev) {
			$mdDialog.show({
				scope : $scope.$new(),
				templateUrl : 'scripts/interviews/view/noteDialog.html',
				clickOutsideToClose:true
			});
		};
		$scope.saveNewNoteButton = function(data) {
			saveNewNote(data);
		}
		function saveNewNote(result) {
			var noteText = result;
			var interview = $scope.interview;
			if (!(interview.notes)) {
				interview.notes = [];
			}
			var newNote = {
					interviewId : interview.interviewId,
					text : noteText,
					type : 'Assessor'
				}
			
			interview.notes.push(newNote);
			
			InterviewsService.saveNote(newNote).then(function(response) {
				if (response.status === 200) {					

					//Get new note list with idNote reflected
					InterviewsService.getListNote(interview.interviewId).then(function(response) {
						if (response.status === 200) {					
							interview.notes = response.data;
						}
					});
				}
			});			
			
			$mdDialog.cancel();
		}
		$scope.cancel = function() {
			$mdDialog.cancel();
		};
		function saveInterview(interview) {
			
			InterviewsService.save(interview).then(function(response) {
				if (response.status === 200) {
					$log.info("Saving interview at assessment note with id:"+ interview.interviewId + " successful");
					
				}
			});
		}
		
		$scope.deleteNote = function(note, model, $event) {
			
			$scope.noteToDelete = note;
			$mdDialog.show({
				scope : $scope.$new(),
				templateUrl : 'scripts/interviews/view/deleteNoteDialog.html',
				clickOutsideToClose:true
			});
		};
		
		$scope.deleteNoteButton = function(){
			
			if($scope.noteToDelete){
				//Mark as deleted
				$scope.noteToDelete.deleted = 1;			
				InterviewsService.saveNote($scope.noteToDelete);
				$scope.noteToDelete = null;
				
				if($scope.interview && $scope.interview.notes){
					
					//Remove manually from list
					var i = $scope.interview.notes.length;
					while (i--){
					    if ($scope.interview.notes[i].deleted == 1){
					        $scope.interview.notes.splice(i, 1);
					    }
					}
				}	
			}				
			
			$mdDialog.cancel();
		}
		
		//Show sub module/fragment
		$scope.toggleNode = function(node, scope){
			if(!node.nodes || node.nodes.length == 0){
				InterviewsService.getModuleForSubModule($scope.interviewId, node.link).then(function(response){
					if(response.status == '200' && response.data && response.data[0]){
						subNode = response.data[0];
						addHeader(subNode.nodes);
						node.nodes = subNode.nodes;
						node.link = 0;
					}
				});
			}
			scope.toggle();	
		}
		
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
		
		$scope.expandAll = function() {
			
			
				$scope.loadingTree = true;
				
				InterviewsService.getExpandedModule($scope.interviewId).then(function(response){
					if(response.status == '200' && response.data && response.data[0]){
						$scope.loadingTree = false;
						$scope.linkedModule = response.data[0];
						addHeader($scope.linkedModule.nodes);		
						
						if(document.getElementById('tree-root')){	
							//Expand all
							console.log("Expand all tree");
							var scope = angular.element(document.getElementById('tree-root')).scope();
							
							$timeout(function(){
								$scope.$broadcast('angular-ui-tree:expand-all');
							}, 200);							
						}
						
						$scope.cancel();
					}	
					else{
						$scope.loadingTree = false;
					}
				});	
			
			$mdDialog.show({
				scope : $scope.$new(),
				templateUrl : 'scripts/assessments/partials/expandAllDialog.html',
				clickOutsideToClose:false
			});
		}
	}

})();