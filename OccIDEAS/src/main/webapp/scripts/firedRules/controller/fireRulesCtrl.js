(function() {
	angular.module('occIDEASApp.FiredRules').controller('FiredRulesCtrl',
			FiredRulesCtrl);

	FiredRulesCtrl.$inject = [ '$scope', 'data','FiredRulesService'];
	function FiredRulesCtrl($scope, data,FiredRulesService) {
		var vm = this;
		vm.firedRulesByModule = [{}];
		vm.getFiredRulesByInterviewId = function(interviewId){
			FiredRulesService.getByInterviewId(interviewId).then(function(response){
				if(response.status == '200'){
					var interviewFiredRules = response.data;
					//loop to each fired rules and construct object to be used by the
					//view
					_.each(interviewFiredRules,function(data){
						//loop rules
						_.each(data.rules,function(rules){
							var condition = rules.conditions[0];
							if(condition){
								//find module details by idNode
								var module = _.find($scope.data,function(questions){
									return questions.idNode = condition.topNodeId;
								});
								if(!vm.firedRulesByModule[module.idNode]){
									vm.firedRulesByModule[module.idNode] = {
											name:module.name,
											firedRules:[{}]
									}
								}
							}
							// check if agent has already been registered
							// otherwise register agentId
							// this is to group it by agent
							if(!vm.firedRulesByModule[module.idNode].firedRules[rules.agent.idAgent]){
								vm.firedRulesByModule[module.idNode].firedRules[rules.agent.idAgent] = {
										child:[],
										agentGroupName:rules.agent.agentGroup.name
								};
							}
							vm.firedRulesByModule[module.idNode].firedRules[rules.agent.idAgent].child.push({
								agentName:rules.agent.name,
								idRule:rules.idRule,
								level:rules.level,
								levelValue:rules.levelValue,
								conditions:rules.conditions
							});
							
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
											FiredRulesService.save(firedRule).then(function(response){
												if(response.status == '200'){
													alert("Fired Rules was successful");
												}
											});
											}
											
										});
									}
							}
						});
				})
			});
		}
		

		if(data[0]){
			$scope.data = data[0].questionHistory;
			vm.getFiredRulesByInterviewId(data[0].interviewId);
		}else{
			alert("no answered question for this interview id!");
		}
	}

})();