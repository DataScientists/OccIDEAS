(function() {
	angular.module('occIDEASApp.FiredRules').controller('FiredRulesCtrl',
			FiredRulesCtrl);

	FiredRulesCtrl.$inject = [ '$scope', 'data','FiredRulesService','$timeout','InterviewsService','AssessmentsService','$log'];
	function FiredRulesCtrl($scope, data,FiredRulesService,$timeout,InterviewsService,AssessmentsService,$log) {
		var vm = this;
		vm.firedRulesByModule = [];
		$scope.interview = data[0];
		$scope.displayHistoryNew = undefined;
		refreshInterviewDisplay();
		function refreshInterviewDisplay(){
			
			if(!$scope.displayHistoryNew){
				$scope.displayHistoryNew = angular.copy($scope.interview.questionHistory);
				_.remove($scope.displayHistoryNew, function(node) {
					  return node.link || node.deleted || !node.processed;
					});
			}
			_.each($scope.displayHistoryNew, function(node) {
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
			});	
		}
		refreshAssessmentDisplay();
		function refreshAssessmentDisplay(){
			AssessmentsService.updateFiredRules($scope.interview.interviewId)
			.then(function(response){
				$log.info("Interview from questions AJAX ...");
				$scope.data = response.data[0];
			});
		}
		vm.interviewFiredRules = null;
		vm.getFiredRulesByInterviewId = function(interviewId){
			FiredRulesService.getByInterviewId(interviewId).then(function(response){
				if(response.status == '200'){
					var interviewFiredRules = response.data;
					vm.interviewFiredRules = interviewFiredRules;
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
											existingModule.firedRules.push(existingAgent);
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
	
		vm.runFiredRulesBtn = function(){
			//loop each answer
			_.each($scope.data,function(question){
				_.each(question.answers,function(answer){
					//get answer module rule
					FiredRulesService.getAnswerWithModuleRule(answer.answerId)
						.then(function(response){
							if(response.status == '200'){
								var possibleAnswerNode = response.data;
									if(possibleAnswerNode.moduleRule.length > 0){
										//loop each module rule, call service fire rules
										_.each(possibleAnswerNode.moduleRule,function(moduleRule){
											var isExist = _.find(vm.firedRules,function(firedRules){
												return firedRules.idRule==moduleRule.rule.idRule;
											});
											if(!isExist){
											var firedRule = {
												idinterview:answer.idInterview,
												idRule:moduleRule.rule.idRule
											};
											if(vm.interviewFiredRules){
												var firedRuleExist = _.find(vm.interviewFiredRules,function(firedrule){
													return firedrule.idRule == moduleRule.rule.idRule;
												});
												if(firedRuleExist){
													firedRule.id = firedRuleExist.id;
												}
											}
											FiredRulesService.save(firedRule).then(function(response){
												if(response.status == '200'){
												}
											});
											}
											
										});
									}
							}
						});
				})
			});
			$timeout(function() {
				vm.getFiredRulesByInterviewId(data[0].interviewId);
            }, 2000);
		}
		
//		$scope.scrollTo = function(target) {
//			$scope.selected = "";
//			var scrollPane = $('#interview-question-list');
//
//			var scrollTarget = $('#questionlist-' + target);
//			if (scrollTarget) {
//				$scope.selected = target;
//				if (scrollTarget.offset()) {
//					var currentScroll = 0;
//					if (scrollPane.scrollTop()) {
//						currentScroll = scrollPane.scrollTop();
//					}
//					var offset = 150;
//					var top = scrollTarget.offset().top;
//					// alert(top);
//					var currentScroll = scrollPane.scrollTop();
//					// alert(currentScroll);
//					var scrollY = top - offset + currentScroll;
//					scrollPane.animate({
//						scrollTop : scrollY
//					}, 1000, 'swing');
//				}
//			}
//		};
		

		if(data[0]){
			$scope.data = data[0].questionHistory;
			vm.getFiredRulesByInterviewId(data[0].interviewId);
		}else{
			alert("no answered question for this interview id!");
		}
		vm.showRulesMenu = function(scope){
			var menu = angular.copy(vm.rulesMenuOptions);
			if(scope.agent.idAgent!=116){
				_.remove(menu, {
				    0: 'Run Noise Assessment'
				});
			}else{
				
			}
			return menu;
		}
		vm.showAssessmentsMenu = function(scope){
			return vm.assessmentsMenuOptions;
		}
		vm.showEditAssessmentMenu = function(scope){
			return vm.editAssessmentsMenuOptions;
		}
		
		vm.rulesMenuOptions =
			[
			  [ 'Show Rules', function($itemScope, $event, model) {
				  var ruleArray =_.filter(model.firedRules, function(r){
						return $itemScope.agent.idAgent === r.agentId; 
				  	});
				  	 
				  	for(var i=0;i<ruleArray.length;i++){
					  	var scope = $itemScope.$new();
				  		scope.model = model;
				  		scope.rule = ruleArray[i];
				  		scope.agentName = $itemScope.agent.name;
				  		newInterviewNote($event.currentTarget.parentElement,scope,$compile);
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
					  for(var m=0;m<model.questionsAsked.length;m++){
						  var iqa = model.questionsAsked[m];
						  if(iqa.possibleAnswer.type=='P_frequencyshifthours'){
							  shiftHours = iqa.interviewQuestionAnswerFreetext;
							  break;
						  }
					  }
					  for(var k=0;k<noiseRules.length;k++){
						  var noiseRule = noiseRules[k];
						  if(noiseRule.type!='BACKGROUND'){
							  var parentNode = noiseRule.conditions[0];
							  if(model.module){
								  cascadeFindNode(model.module.nodes,parentNode);
							  }else{
								  cascadeFindNode(model.fragment.nodes,parentNode); 
							  }
							  var answeredValue = 0;
							  if($scope.foundNode){
								  if($scope.foundNode.nodes[0]){
									  if($scope.foundNode.nodes[0].nodes[0]){
										  var frequencyHoursIdNode = $scope.foundNode.nodes[0].nodes[0].idNode;
										  for(var l=0;l<model.questionsAsked.length;l++){
											  var iqa = model.questionsAsked[l];
											  if(iqa.possibleAnswer.idNode==frequencyHoursIdNode){
												  answeredValue = iqa.interviewQuestionAnswerFreetext;
												  break;
											  }
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
							}
							var partialExposure = 4*hoursbg*(Math.pow(10,(level-100)/10));
							partialExposure = partialExposure.toFixed(4);
							hoursbg = hoursbg.toFixed(4);
							level = noiseRule.ruleAdditionalfields[0].value;
							var noiseRow = {nodeNumber:noiseRule.conditions[0].number,
											dB:level+'B',
											backgroundhours: hoursbg,
											partialExposure:partialExposure,
											type:'backgroundNoise'}
							
							$scope.noiseRows.push(noiseRow);
							if(partialExposure>maxBackgroundPartialExposure){
								maxBackgroundPartialExposure = partialExposure;
								maxBackgroundHours = hoursbg;
							}
						  }else{
							var hours = 0.0;
							var frequencyhours = 0;
							var parentNode = noiseRule.conditions[0];
							if(model.module){
								  cascadeFindNode(model.module.nodes,parentNode);
							  }else{
								  cascadeFindNode(model.fragment.nodes,parentNode); 
							  }
							  if($scope.foundNode){
								  if($scope.foundNode.nodes[0]){
									  if($scope.foundNode.nodes[0].nodes[0]){
										  var frequencyHoursIdNode = $scope.foundNode.nodes[0].nodes[0].idNode;
										  for(var l=0;l<model.questionsAsked.length;l++){
											  var iqa = model.questionsAsked[l];
											  if(iqa.possibleAnswer.idNode==frequencyHoursIdNode){
												  frequencyhours = iqa.interviewQuestionAnswerFreetext;
											  }
										  }
									  } 
								  }
								  $scope.foundNode=null;
							  }
							if(useRatio){
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
							
							var noiseRow = {nodeNumber:noiseRule.conditions[0].number,
									dB:level,
									backgroundhours: modHours,
									partialExposure:partialExposurePercentageAdjusted}
					
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
				  model.autoAssessedRules = [];			  
				  for(var i=0;i<model.agents.length;i++){
					  var agentAssessing = model.agents[i]
					  var rule = {levelValue:99};
					  for(var j=0;j<model.firedRules.length;j++){
						  var firedRule = model.firedRules[j];
						  if(agentAssessing.idAgent == firedRule.agent.idAgent){
							  if(firedRule.levelValue<rule.levelValue){
								  rule = firedRule;
							  }
						  }	  
					  }
					  model.autoAssessedRules.push(rule);
				  }
				  $scope.data = model;
				  InterviewsService.save(model).then(function (response) {
		                if (response.status === 200) {
		                	$log.info("Interview saved with auto assessments");
		                }
				  });
				  
			  	}			  
			  ],
			  [ 'Use Auto', function($itemScope, $event, model) {
                    if(model.manualAssessedRules.length==0){
                      model.manualAssessedRules = [];
                  	  var assessments = angular.copy(model.autoAssessedRules);
                  	  for(var i=0;i<assessments.length;i++){
  						  var assessment = assessments[i];
  						  assessment.idRule = '';
  						  assessment.conditions = [];
  						  model.manualAssessedRules.push(assessment);
  					  }
      				  InterviewsService.save(model).then(function (response) {
      		                if (response.status === 200) {
      		                	$log.info("Interview saved with manual assessments");
      		                }
      				  });
                    }	  
			  	}
			  ]
			];
		self.editAssessmentsMenuOptions =
			[
			  [ 'Edit Assessment', function($itemScope, $event, model) {
				  var ruleArray =_.filter(model.manualAssessedRules, function(r){
						return $itemScope.agent.idAgent === r.agentId; 
				  	});
				  	 
				  	for(var i=0;i<ruleArray.length;i++){
					  	var scope = $itemScope.$new();
				  		scope.model = model;
				  		scope.rule = ruleArray[i];
				  		scope.agentName = $itemScope.agent.name;
				  		editAssessmentDialog($event.currentTarget.parentElement,scope,$compile);
				  	}
			  	}			  
			  ]
			];
	}

})();