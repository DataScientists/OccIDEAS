(function(){
	angular.module('occIDEASApp.Assessments')
		   .controller('AssessmentsCtrl',AssessmentsCtrl);
	AssessmentsCtrl.$inject = ['AssessmentsService','ngTableParams','$scope','$filter',
                          'data','$log','$compile'];
	function AssessmentsCtrl(AssessmentsService,NgTableParams,$scope,$filter,
			data,$log,$compile){
		var self = this;
		$scope.data = data;
		var getData = function(){
			$log.info("Data getting from interviews ajax"); 
			AssessmentsService.getInterviews().then(function(data) {
				$log.info("Data received from interviews ajax");     	 
		    	  return data;
		      });
			}
		self.showRulesMenu = function(scope){
			return self.rulesMenuOptions;
		}
		self.tableParams = new NgTableParams(
				{}, 
				{	
					getData: function(params) {
						if((params.filter().referenceNumber)||(params.filter().moduleName)){	
				        	return $filter('filter')(self.tableParams.settings().dataset, params.filter());
				        }						
						if ((params.sorting().referenceNumber)||(params.sorting().moduleName)){
							return $filter('orderBy')(self.tableParams.settings().dataset, params.orderBy());
				        }
					    if(!self.tableParams.shouldGetData){
					    	return self.tableParams.settings().dataset;
					    }
					    $log.info("Data getting from interviews ajax ..."); 
					    return AssessmentsService.getInterviews().then(function(data) {
					        	  $log.info("Data received from interviews ajax ...");        	 
					        	  self.originalData = angular.copy(data);
					        	  self.tableParams.settings().dataset = data;
					            return data;
					          });
					    },
				});
		self.tableParams.shouldGetData = true;
		
		$scope.nodePopover = {
	    		templateUrl: 'scripts/questions/partials/nodePopover.html',
    		    open: function(x,nodeclass) {
    		    	if(!nodeclass){
    		    		nodeclass = 'P';
    		    	}
    		    	if(!x.idNode){
    		    		var convertX = {};
    		    		convertX.idNode = x;
    		    		x = convertX;
    		    	}
    		    	if(angular.isUndefined(x.info)){
  		    		  x.info = [];
  		    	  	}
    		    	 x.info["Node"+x.idNode] = {
							    				  idNode:x.idNode,
							    				  nodeclass:nodeclass,
							    				  nodePopover:{
							    					  isOpen: false
							    				  },
							    				  nodePopoverInProgress : false
		    		  							};
    		    	 var nodeInPopup = x.info["Node"+x.idNode];
    		    	 nodeInPopup.nodePopover.isOpen = true;
    		    	 nodeInPopup.nodePopoverInProgress = true;
    		          
    		    	 if(nodeclass=='P'){
    		    		 QuestionsService.findPossibleAnswer(nodeInPopup.idNode).then(function(data) {	
    		    			 nodeInPopup.data = data.data[0];
   							nodeInPopup.nodePopoverInProgress = false;
 					     });
    		    	 }else{
    		    		 QuestionsService.findQuestion(nodeInPopup.idNode).then(function(data) {	
  							nodeInPopup.data = data.data[0];		
  							nodeInPopup.nodePopoverInProgress = false;
  					     });
    		    	 }
    		         
    		    },
  		        close: function close(x) {
  		        	x.info["Node"+x.idNode].nodePopover.isOpen = false;
  		        }
    	};
		self.rulesMenuOptions =
			[
			  [ 'Run Auto Assessmemt', function($itemScope, $event, model) {
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
			  	}			  
			  ],
			  [ 'Show Rules', function($itemScope, $event, model) {
				  var ruleArray =_.filter(model.firedRules, function(r){
						return $itemScope.agent.idAgent === r.idAgent; 
				  	});
				  	 
				  	for(var i=0;i<ruleArray.length;i++){
					  	var scope = $itemScope.$new();
				  		scope.model = model;
				  		scope.rule = ruleArray[i];
				  		scope.agentName = $itemScope.agent.name;
				  		newInterviewNote($event.currentTarget.parentElement,scope,$compile);
				  	}
			  	}			  
			  ]
			];
        
        $scope.closeIntDialog = function(elem,$event) {
        	$($event.target).closest('.int-note').remove();
        	$scope.activeIntRuleDialog = '';
        	$scope.activeIntRuleCell = '';
        	safeDigest($scope.activeIntRuleDialog);
        	safeDigest($scope.activeIntRuleCell);
        };
        
        $scope.setActiveIntRule = function(model,el){
        	$scope.activeIntRuleDialog = el.$id;
        	$scope.activeIntRuleCell = model.idAgent;
        	safeDigest($scope.activeIntRuleDialog);
        	safeDigest($scope.activeIntRuleCell);
        }
	}
})();

