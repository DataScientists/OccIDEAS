(function() {
	angular.module('occIDEASApp.FiredRules').controller('FiredRulesCtrl',
			FiredRulesCtrl);

	FiredRulesCtrl.$inject = [ '$scope', 'data','FiredRulesService'];
	function FiredRulesCtrl($scope, data,FiredRulesService) {
		var vm = this;

		if(data[0]){
			$scope.data = data[0].questionHistory;
			vm.getFiredRulesByInterviewId(data[0].interviewId);
		}else{
			alert("no answered question for this interview id!");
		}
		
		vm.firedRules = [];
		vm.getFiredRulesByInterviewId = function(interviewId){
			FiredRulesService.getByInterviewId(interviewId).then(function(response){
				if(response.status == '200'){
					vm.firedRules = response.data;
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
	}

})();