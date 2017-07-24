(function() {
	angular.module('occIDEASApp.FiredRules').controller('FiredRulesCtrl',
			FiredRulesCtrl);

	FiredRulesCtrl.$inject = [ '$scope', 'data','FiredRulesService','$timeout',
	                           'InterviewsService','AssessmentsService','$log','$compile',
	                           'RulesService','ngToast','SystemPropertyService', '$mdDialog','AgentsService', 
	                           '$q','$sessionStorage','moduleName','$rootScope','ManualAssessmentService',
	                           'AutoAssessmentService','ngToast','ModulesService','QuestionsService',
	                           'ParticipantsService'];
	function FiredRulesCtrl($scope, data,FiredRulesService,$timeout,
			InterviewsService,AssessmentsService,$log,$compile,
			RulesService,$ngToast,SystemPropertyService, $mdDialog,
			AgentsService,$q, $sessionStorage,moduleName,$rootScope,ManualAssessmentService,
			AutoAssessmentService,ngToast,ModulesService,QuestionsService,ParticipantsService) {
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
			   var linkNode = _.find($scope.data.topModuleNameList,function(qnode){
				  var retValue = false;
				  if(qnode.idnode){
					  if(qnode.idnode == node.topNodeId){
						  retValue = true;
					  }
				  }
				  return retValue;
			  });
			  if(linkNode){
				  node.header = linkNode.topModuleName.substr(0,4);
			  }
			$scope.openAnswerSummaryTab(node,$scope.moduleName,$scope.interviewId);
		}
		
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
		
		$(window).scroll(function () {
            if ($(this).scrollTop() > 50) {
                $('#back-to-top').fadeIn();
            } else {
                $('#back-to-top').fadeOut();
            }
        });
		// scroll body to 0px on click
        $scope.scrollToTop = function(){
    	   $('body,html').animate({
               scrollTop: 0
           }, 800);
        }
        
        $('#back-to-top').tooltip('show');
        
		$scope.statuses = ['Incomplete','Needs Review','Finished'];				
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
		
		$scope.participantStatus = "";
		$scope.pstatuses = ['Running','Partial','Completed','To be excluded'];
		$scope.onChangeSaveParticipantStatus = function(){
			if(participant){
				// get participant 
				participant.status = getParticipantStatus($scope.participantStatus);
				ParticipantsService.save(participant).then(function(rp){
					if(rp.status == 200){
						ngToast.create({
				    		  className: 'success',
				    		  content: "Save successful",
				    		  timeout: 4000,
				    		  dismissButton:true
				    	});
					}else{
						ngToast.create({
				    		  className: 'danger',
				    		  content: "Save failed",
				    		  timeout: 4000,
				    		  dismissButton:true
				    	});
					}
				})
			}
		}
		
		function getParticipantStatus(status){
			if(status == 'Running'){
				return 0;
			}
			else if(status == 'Partial'){
				return 1;
			}else if(status == 'Completed'){
				return 2;
			}else if(status == 'To be excluded'){
				return 3;
			}
		}
		
		function getParticipantDescription(status){
			if(status == 1){
				return 'Partial';
			}else if(status == 2){
				return 'Completed';
			}else if(status == 3){
				return 'To be excluded';
			}else{
				return 'Running';
			}
		}
		
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
		var participant = undefined;
		function refreshInterviewDisplay(){
			
			if(!$scope.linkedModule){
				
				InterviewsService.getInterview($scope.interviewId).then(function(response){
					if(response.status == '200'){	
						
						$scope.interview = response.data[0];
						$scope.originalInterview = _.cloneDeep($scope.interview);
						participant = $scope.interview.participant;
						$scope.participantStatus = getParticipantDescription(participant.status); 
						$scope.assessmentStatus = $scope.interview.assessedStatus;
						$scope.data = response.data[0];

						$scope.nodePopover = {
				    		    templateUrl: 'scripts/questions/partials/nodePopover.html',
				    		    open: function(x,idRule) {
				    		    	if(x.info){
				    		    		if(x.info["Node"+x.idNode+idRule].nodePopover.isOpen){
				    		    			return;
				    		    		}
				    		    	}
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
						
						$('#back-to-top').fadeOut();
						$scope.rulesLoaded = false;
						FiredRulesService.getByInterviewId($scope.interviewId).then(function(response){
							if(response.status == '200'){
								var interviewFiredRules = response.data;
								vm.interviewFiredRules = interviewFiredRules;
								$scope.data.firedRules = [];
								$scope.data.topModuleNameList = [];
								$scope.data.topNodeIds = [];
								var promises = [];
								$scope.moduleNameCount = 0;
								for(var i=0;i<interviewFiredRules.length;i++){
									var rules = interviewFiredRules[i].rules;
									for(var j=0;j<rules.length;j++){
										for(var x=0;x<rules[j].conditions.length;x++){
											var node = rules[j].conditions[x];
											if($scope.data.topNodeIds.indexOf(node.topNodeId) == -1){
												$scope.data.topNodeIds.push(node.topNodeId);
												promises.push(populateModuleName(angular.copy(node.topNodeId)));
											}
										}
											$scope.data.firedRules.push(rules[j]);
									}              		
			                	} 
								$q.all(promises).then(function () {
									console.log('firedRulesLoaded');
									$scope.rulesLoaded = true;
			        			});
							}
						});
						$scope.manualAssessmentLoaded = false;
						ManualAssessmentService.getByInterviewId($scope.interviewId).then(function(response){
							if(response.status == '200'){
								var interviewManualAssessedRules = response.data;
								$scope.data.manualAssessedRules = [];
								for(var i=0;i<interviewManualAssessedRules.length;i++){
									var rule = interviewManualAssessedRules[i].rule;					
									$scope.data.manualAssessedRules.push(rule);						             		
			                	} 
								$scope.manualAssessmentLoaded = true;
							}
						});
						$scope.autoAssessmentLoaded = false;
						AutoAssessmentService.getByInterviewId($scope.interviewId).then(function(response){
							if(response.status == '200'){
								var interviewAutoAssessedRules = response.data;
								$scope.data.autoAssessedRules = [];
								for(var i=0;i<interviewAutoAssessedRules.length;i++){
									var rule = interviewAutoAssessedRules[i].rule;					
									$scope.data.autoAssessedRules.push(rule);						             		
			                	}
								$scope.autoAssessmentLoaded = true;
							}
						});
						
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
		
		function populateModuleName(idnode){
			return ModulesService.getNodeNameById(idnode).then(function(response){
				if(response.status == '200'){
					var topModuleName =
						response.data.name;
					$scope.data.topModuleNameList.push({
						idnode:idnode,
						topModuleName:topModuleName
					});
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
			//AssessmentsService.updateFiredRules($scope.interviewId).then(function(response){
			//	$log.info("refreshAssessmentDisplay");
			//	$scope.data = response.data[0];
		//	});
			//getFiredRulesByInterviewId($scope.interview.interviewId);
			FiredRulesService.getByInterviewId($scope.interviewId).then(function(response){
				if(response.status == '200'){
					var interviewFiredRules = response.data;
					vm.interviewFiredRules = interviewFiredRules;
				}
			});
			
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
        	
        	$(".tree-node div").removeClass("highlight-rulenode");
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
				if(_.includes(childFrequencyNode.type, 'P_frequency')){
					return childFrequencyNode;
				}else{
					return findFrequencyIdNode(childFrequencyNode);
				}
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
				  		for(var x = 0;x < scope.rule.conditions.length;x++){
				  			var condition = scope.rule.conditions[x];
				  			var topMod = _.find($scope.data.topModuleNameList, 
									function(o) { return o.idnode == condition.topNodeId; });
				  			if(topMod){
				  				condition.topModName = topMod.topModuleName.substring(0, 4);
				  			}
				  		}
				  		addPopoverInfo(scope.rule.conditions,scope.rule.idRule);
				  		scope.agentName = $itemScope.agent.name;
				  		console.log($scope.data.topModuleNameList);
						if($("#rule-dialog-"+$scope.interviewId+"-"+scope.rule.idRule).length == 0){
							firedRuleDialog($event.currentTarget.parentElement,scope,$compile,$scope.interviewId);
						}
				  	}
			  	}			  
			  ],
			  [ 'Run Noise Assessment', function($itemScope, $event, model) {
				  InterviewsService.getInterviewQuestionList(model.interviewId).then(function(response){
						if(response.status == '200'){
							var questions = response.data;								
						
							$scope.interview.questionHistory = questions;
							InterviewsService.getInterviewAnswerList(model.interviewId).then(function(response){
								if(response.status == '200'){
									var answers = response.data;								
									
									$scope.interview.answerHistory = answers;
									var bFoundNoiseRules = false;
									  var noiseRules = [];
									  for(var i=0;i<$scope.agents.length;i++){
										  var agentAssessing = $scope.agents[i]
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
										  for(var m=0;m<$scope.interview.answerHistory.length;m++){
											  var iqa = $scope.interview.answerHistory[m];
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
													  cascadeFindNode($scope.interview.answerHistory,parentNode); 
												  //}
												  var answeredValue = 0;
												  if($scope.foundNode){
													 var frequencyHoursNode = findFrequencyIdNode($scope.foundNode);
														
													 if(frequencyHoursNode!=null){
														 if(frequencyHoursNode.type!='P_frequencyseconds'){
															 if(!isNaN(frequencyHoursNode.answerFreetext)){
																 answeredValue = frequencyHoursNode.answerFreetext; 
															 }															 
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
												}else if(isNaN(hoursbg)) {
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
												if(peakNoise<Number(level)){
													if(partialExposure!=0){
														peakNoise = Number(level);
													}	
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
												if(hours<0){
													hours = 0.0;
												}else if(isNaN(hours)) {
													hours = 0.0;
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
												  if(totalPartialExposure!=0){
													  peakNoise = Number(level);
												  }	
											  }
										  }
										  	totalPartialExposure = (parseFloat(totalPartialExposure)+parseFloat(maxBackgroundPartialExposure));
										  	totalPartialExposure = totalPartialExposure.toFixed(4);
											totalFrequency += maxBackgroundHours;

											var autoExposureLevel = 10*(Math.log10(totalPartialExposure/(3.2*(Math.pow(10,-9)))))
											autoExposureLevel = autoExposureLevel.toFixed(2);
											$scope.totalPartialExposure = totalPartialExposure;
											$scope.autoExposureLevel = autoExposureLevel;
											$scope.peakNoise = peakNoise;
											$scope.shiftHours = shiftHours;
									  }
								  	
								}
							});
						}
				  });
			  }
				  
				  
				  			  
			  ],
			  [ 'Run Vibration Assessment', function($itemScope, $event, model) {
				  InterviewsService.getInterviewQuestionList(model.interviewId).then(function(response){
						if(response.status == '200'){
							var questions = response.data;								
						
							$scope.interview.questionHistory = questions;
							InterviewsService.getInterviewAnswerList(model.interviewId).then(function(response){
								if(response.status == '200'){
									var answers = response.data;								
									
									$scope.interview.answerHistory = answers;
									var bFoundVibrationRules = false;
									  var vibrationRules = [];
									  
									  for(var i=0;i<$scope.agents.length;i++){
										  var agentAssessing = $scope.agents[i]
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
										  for(var k=0;k<vibrationRules.length;k++){
											  var vibrationRule = vibrationRules[k];
											  
											  var parentNode = vibrationRule.conditions[0];
											  cascadeFindNode($scope.interview.answerHistory,parentNode); 
											  var frequencyhours = 0;
											  if($scope.foundNode){
												  var frequencyHoursNode = findFrequencyIdNode($scope.foundNode);								  
												  if(frequencyHoursNode){
													  if(frequencyHoursNode.answerFreetext=='[Time]'){
														  frequencyhours = 0;
													  }else{
														  frequencyhours = Number(frequencyHoursNode.answerFreetext); 
													  }
													  
												  }
												  $scope.foundNode=null;
											  }
											  var level = vibrationRule.ruleAdditionalfields[0].value;
											  var moduleName = getModuleNameOfNode(vibrationRule.conditions[0]);
											  particalVibration = (Number(level)*Number(level)*Number(frequencyhours));

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
										  $scope.dailyVibration = Math.sqrt(dailyVibration/8).toFixed(4);
									  }

								}
							});
						}						
				  });
			  }
			 ]
			];
		vm.assessmentsMenuOptions =
			[
			  [ 'Update Fired Rules', function($itemScope, $event, model) {
				  
				  $scope.noiseRows = [];
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
		
		$scope.retryCount = 0;
		function saveInterview(interview) {
			
			if(!$scope.rulesLoaded || !$scope.manualAssessmentLoaded ||	!$scope.autoAssessmentLoaded){
				if($scope.retryCount > 3){
					alert('Rules has not been completely loaded yet. Please try again later.');
					$scope.retryCount = 0;
					$scope.assessmentStatus = $scope.originalInterview.assessedStatus;
					return;
				}else{
					$scope.retryCount = $scope.retryCount + 1;
					console.log('checking rules if loaded in 3 seconds');
					$timeout(function() {
						saveInterview(interview);
		            }, 3000);
					return; 
				}
			}
			
			InterviewsService.save(interview).then(function(response) {
				if (response.status === 200) {
					$log.info("Saving interview at assessment note with id:"+ interview.interviewId + " successful");
						ngToast.create({
				    		  className: 'success',
				    		  content: "Save successful",
				    		  timeout: 4000,
				    		  dismissButton:true
				    	});
				}else{
					ngToast.create({
			    		  className: 'danger',
			    		  content: "Save failed",
			    		  timeout: 4000,
			    		  dismissButton:true
			    	});
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
						
						$timeout(function(){
							if(document.getElementById('tree-root')){
								//Expand all
								console.log("Expand all tree");
								var scope = angular.element(document.getElementById('tree-root')).scope();
								
								$timeout(function(){
									$scope.$broadcast('angular-ui-tree:expand-all');
								}, 200);
							}
						}, 500);
						
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
		
		$scope.menuOptions = 
			[ 
			  [ 'Expand All', function($itemScope) {
				  $scope.expandAll();	
			  }]
			];
			
		$scope.showMenu = function(scope) {
			return $scope.menuOptions;
		}
	}
	

})();