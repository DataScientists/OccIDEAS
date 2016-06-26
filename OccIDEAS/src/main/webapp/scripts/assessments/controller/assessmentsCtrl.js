(function(){
	angular.module('occIDEASApp.Assessments')
		   .controller('AssessmentsCtrl',AssessmentsCtrl);
	AssessmentsCtrl.$inject = ['AssessmentsService','InterviewsService','RulesService','ngTableParams','$scope','$filter',
                          'data','$log','$compile','$http','$q','$mdDialog','$timeout'];
	function AssessmentsCtrl(AssessmentsService,InterviewsService,RulesService,NgTableParams,$scope,$filter,
			data,$log,$compile,$http,$q,$mdDialog,$timeout){
		var self = this;
		$scope.data = data;
		$scope.$root.tabsLoading = false;
		$scope.getInterviewForCSVButton = function(){
			$scope.showExportCSV();
		};
		
		$scope.getCSVButton = function(){
			return $scope.csv;
		}
		
		$scope.showExportCSVButton = function() {
		//get list of interview id
		InterviewsService.getInterviewIdList().then(function(response){
			if(response.status == '200'){
				//display modal with list of id + progress bar
				$scope.interviewIdList = response.data;
				$scope.interviewIdCount = $scope.interviewIdList.length;
				$scope.counter = 0;
				$scope.interviewCount = $scope.counter;
				$mdDialog.show({
					scope: $scope,  
					preserveScope: true,
					templateUrl : 'scripts/assessments/partials/exportCSVDialog.html',
					clickOutsideToClose:false
				});
				$scope.csv = [{
					Q:[]
				}];
				var listOfQuestion = [];
				InterviewsService.getInterviewsWithoutAnswers().then(function(response){
					if(response.status == '200'){
						var questionIdList = listAllInterviewQuestions(response.data,$scope.csv,listOfQuestion);
						$scope.interviewIdList.reduce(function(p, interviewId) {
						    return p.then(function() {
						    	$scope.interviewIdInProgress = interviewId;
						    	$scope.counter++;
								$scope.interviewCount = $scope.counter;
						        return populateInterviewAnswerList(interviewId,questionIdList);
						    });
						}, $q.when(true)).then(function(finalResult) {
							console.log('finish extracting data for CSV');
							$timeout(function() {
								angular.element(document.querySelector('#exportCSV')).triggerHandler('click');
								$scope.cancel();
				            }, 1000);	
						}, function(err) {
							console.log('error');
						});
					}
				});
			}
		});
		
		function populateInterviewAnswerList(interviewId,questionIdList){
			return InterviewsService.getInterviewQuestionAnswer(interviewId).then(function(response){
				if(response.status == '200'){
					listAllInterviewAnswers(response.data,$scope.csv,questionIdList);
				}
			});
		}
		
			//loop through and trigger InterviewService.getInterview(interviewId) 
			//with deffered
		};
		
		$scope.cancel = function() {
			$mdDialog.cancel();
		};
		
		function listAllInterviewAnswers(response,csv,questionIdList){
			_.each(response,function(data){
				var obj = {A:[]};
				obj.A.push(data.interviewId);
				obj.A.push(data.referenceNumber);
//				obj.A.push(data.module.idNode);
				_.each(questionIdList,function(qId){
					var question = _.find(data.questionHistory, _.matchesProperty('questionId', qId));
					if(question){
						if(question.answers.length > 0){
							_.each(question.answers,function(ans){
								if(ans.answerFreetext){
									obj.A.push(ans.answerFreetext);	
								}else{
									obj.A.push(ans.name);
								}
							})
						}else{
							obj.A.push("-- No Answer --");
						}
					}else{
						obj.A.push("-- Question Not Asked --");
					}
				});
				csv.push(obj);
			});
		}
		
		function listAllInterviewQuestions(response,csv,listOfQuestion){
			var questionIdList = [];
			_.each(response,function(data){
				listOfQuestion = _.unionBy(listOfQuestion, data.questionHistory, function(item){
					return item.number && item.name;
				});
			});
			var sortQuestionList = {};
			var header = "";
			_.each(listOfQuestion,function(data){
				if(data.nodeClass == 'M' || data.type == 'M_IntroModule' || data.type == 'Q_linkedajsm'){
					if(header != ""){
						sortQuestionList[header] = _.sortBy(sortQuestionList[header], 'header');
					}
					header = data.name.substring(0, 4);
					sortQuestionList[header] = [];
				}else if(data.questionId){
					sortQuestionList[header].push({
						header:header+"_"+data.number,
						questionId:data.questionId
					});
				}
			});
			
			
			_.each(sortQuestionList,function(headers){
				var uniqueHeaders = _.unionBy(headers,headers,function(o){return o.header;});
				_.each(uniqueHeaders,function(data){
					csv[0].Q.push(data.header);
					questionIdList.push(data.questionId);
				})
			});
			csv[0].Q.unshift('AWES ID');
			csv[0].Q.unshift('Interview Id');
			return questionIdList;
		}
		
		var getData = function(){
			$log.info("Data getting from interviews ajax"); 
			AssessmentsService.getInterviews().then(function(data) {
				$log.info("Data received from interviews ajax");     	 
		    	  return data;
		      });
			}
		self.showRulesMenu = function(scope){
			var menu = angular.copy(self.rulesMenuOptions);
			if(scope.agent.idAgent!=116){
				_.remove(menu, {
				    0: 'Run Noise Assessment'
				});
			}else{
				
			}
			return menu;
		}
		self.showAssessmentsMenu = function(scope){
			return self.assessmentsMenuOptions;
		}
		self.showEditAssessmentMenu = function(scope){
			return self.editAssessmentsMenuOptions;
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
					    return null;
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
		self.assessmentsMenuOptions =
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
        var safeDigest = function (obj){
        	if (!obj.$$phase) {
		        try {
		        	obj.$digest();
		        }
		        catch (e) { }
        	}
        }
        $scope.saveRule = function(rule){
        	RulesService.save(rule).then(function(response){
    			if(response.status === 200){
    				$log.info('Rule Save was Successful!'+rule);
    			}
    		});
        	
        }
        function cascadeFindNode(nodes,node){
			_.each(nodes, function(data) {
				if(data.idNode == node.idNode){
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
	}
})();

