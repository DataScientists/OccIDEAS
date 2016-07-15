(function() {
	angular.module('occIDEASApp.FiredRules').controller('FiredRulesCtrl',
			FiredRulesCtrl);

	FiredRulesCtrl.$inject = [ '$scope', 'data','FiredRulesService','$timeout','InterviewsService'];
	function FiredRulesCtrl($scope, data,FiredRulesService,$timeout,InterviewsService) {
		var vm = this;
		vm.firedRulesByModule = [];
		
		refreshInterviewDisplay();
		function refreshInterviewDisplay(){
			InterviewsService.getIntDisplay(data[0].interviewId).then(function(response){
				if(response.status == 200){
					$scope.answeredQuestion = response.data;
				}
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
						_.each(data.rules,function(rules){
							var condition = rules.conditions[0];
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
											return agent.idAgent == rules.agent.agentGroup.idAgent;
										});
										if(!existingAgent){
											existingAgent = {
													idAgent:rules.agent.agentGroup.idAgent,
													child:[],
													agentGroupName:rules.agent.agentGroup.name
											};
											existingModule.firedRules.push(existingAgent);
										}
										//make sure we dont get duplicate
										var existingRule = _.find(existingAgent.child,function(exRule){
											return rules.idRule == exRule.idRule;
										});
										if(!existingRule){
										existingAgent.child.push({
											agentName:rules.agent.name,
											idRule:rules.idRule,
											level:rules.level,
											levelValue:rules.levelValue,
											conditions:rules.conditions
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
		
		$scope.scrollTo = function(target) {
			$scope.selected = "";
			var scrollPane = $('#interview-question-list');

			var scrollTarget = $('#questionlist-' + target);
			if (scrollTarget) {
				$scope.selected = target;
				if (scrollTarget.offset()) {
					var currentScroll = 0;
					if (scrollPane.scrollTop()) {
						currentScroll = scrollPane.scrollTop();
					}
					var offset = 150;
					var top = scrollTarget.offset().top;
					// alert(top);
					var currentScroll = scrollPane.scrollTop();
					// alert(currentScroll);
					var scrollY = top - offset + currentScroll;
					scrollPane.animate({
						scrollTop : scrollY
					}, 1000, 'swing');
				}
			}
		};
		

		if(data[0]){
			$scope.data = data[0].questionHistory;
			vm.getFiredRulesByInterviewId(data[0].interviewId);
		}else{
			alert("no answered question for this interview id!");
		}
	}

})();