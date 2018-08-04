(function(){
	angular.module('occIDEASApp.Assessments')
		   .controller('AssessmentsCtrl',AssessmentsCtrl);
	AssessmentsCtrl.$inject = ['AssessmentsService','InterviewsService','RulesService','ngTableParams','$scope','$filter',
                          'data','$log','$compile','$http','$q','$mdDialog','$timeout','ParticipantsService','QuestionsService'
                          ,'$sessionStorage','ReportsService','SystemPropertyService',
                          'ngToast','AgentsService','FiredRulesService'];
	function AssessmentsCtrl(AssessmentsService,InterviewsService,RulesService,NgTableParams,$scope,$filter,
			data,$log,$compile,$http,$q,$mdDialog,$timeout,ParticipantsService,QuestionsService,
			$sessionStorage,ReportsService,SystemPropertyService,$ngToast,AgentsService,FiredRulesService){
		var self = this;
		$scope.data = data;
		$scope.$root.tabsLoading = false;
		$scope.updateButtonDisabled = false;
		
		$scope.statuses = ['Not Assessed','Manually assessed','Incomplete','Needs Review','Finished','Auto Assessed'];				
		$scope.onChangeSaveStatus = function (idinterview,assessmentStatus){
			InterviewsService.getInterviewWithRules(idinterview).then(function(response){
				if(response.status == 200){
					var interview = response.data[0];
					if(interview){
						interview.assessedStatus = assessmentStatus;
						if (!(interview.notes)) {
							interview.notes = [];
						}
						interview.notes.push({
							interviewId : idinterview,
							text : "Updated Status",
							type : 'System'
						});
						saveInterview(interview);
					}
				}else{
					$ngToast.create({
			    		  className: 'danger',
			    		  content: 'Error calling webservice getInterview',
			    		  dismissButton: true,
		      	    	  dismissOnClick:false,
		      	    	  animation:'slide'
					});
				}
			});
		};
		
		$scope.getStatusList = function(column) {
			var statusArr = [];
			var index = 0;
			_.each($scope.statuses,function(value){
				statusArr.push({
					'id':value,
					'title':value
				});
				index++;
			})
			
			return statusArr;
		};
		
		$scope.interviewStatusList = [{
			'id':0,
			'title':'Running'
		},
		{
			'id':1,
			'title':'Partial'
		},
		{
			'id':2,
			'title':'Completed'
		},
		{
			'id':3,
			'title':'To be excluded'
		}
		];
		$scope.getInterviewStatusList = function(column) {
			return $scope.interviewStatusList;
		};
		
		function saveInterview(interview) {
			
			InterviewsService.save(interview).then(function(response) {
				if (response.status === 200) {
					$log.info("Saving interview at assessment note with id:"+ interview.interviewId + " successful");
						$ngToast.create({
				    		  className: 'success',
				    		  content: "Save successful",
				    		  timeout: 4000,
				    		  dismissButton:true
				    	});
				}else{
					$ngToast.create({
			    		  className: 'danger',
			    		  content: "Save failed",
			    		  timeout: 4000,
			    		  dismissButton:true
			    	});
				}
			});
		}
		
		$scope.updateRules = function(){
			$scope.currentParticipants.reduce(function(p, data) {
				return p.then(function() {
					$scope.interviewIdInProgress = data.idinterview;
					$scope.counter++;
					$scope.interviewCount = $scope.counter;
					AgentsService.getStudyAgentsWithRules(data.idinterview).
					then(function(response)
							{
						data.agents = [];

						_.each(response, function(agent) {
							data.agents.push(agent);
						});
						
						FiredRulesService.getByInterviewId(data.idinterview).then(function(response){
							if(response.status == '200'){
								var interviewFiredRules = response.data;
								data.interviewFiredRules = interviewFiredRules;
								data.firedRules = [];
								data.agentCount = [];
								for(var i=0;i<interviewFiredRules.length;i++){
									var rules = interviewFiredRules[i].rules;
									var key = data.idinterview;
									for(var j=0;j<rules.length;j++){
										for(var x=0;x<rules[j].conditions.length;x++){
											var node = rules[j].conditions[x];
										}
										data.firedRules.push(rules[j]);
										var agentCount = _.find(data.agentCount, 
												function(o) { return o.idAgent == rules[j].agentId; });
										if(agentCount){
											agentCount.count = agentCount.count + 1; 
										}else{
											data.agentCount.push({
												idAgent: rules[j].agentId,
												count: 1
											});
										}
									}
									if(data.agentCount && data.agentCount.length > 0){
									var arr = _.map(data.agentCount, function(element, idx) {
										  return element.count;
									});
									var num=_.max(arr);
//									if(num > 12){
										if(num < 3){
											num = 3;
										}
										var borderTopPx = 7;
										var borderLowPx = 7;
										var result = num;
										var height = (result/3)*5.5;
										data[key] = height+borderTopPx+borderLowPx;
//									}
									}
								}
							}
						});
						
					});
				});
			}, $q.when(true)).then(function(finalResult) {
				console.log('finish loading rules');
//				$timeout(function() {
//					$scope.cancel();
//					ModulesService.save(row).then(function(response){
//						if(response.status === 200){
//							console.log('Module Save was Successful!');
//							self.tableParams.shouldGetData = true;
//							self.tableParams.reload().then(function (data) {
//								if (data.length === 0 && self.tableParams.total() > 0) {
//									self.tableParams.page(self.tableParams.page() - 1);
//									self.tableParams.reload();
//									$location.hash("");
//									$anchorScroll();
//								}
//							});
//						}
//					});
//				}, 1000);	
//				self.tableParams.reload();
			}, function(err) {
				console.log('error');
			});

		}
		
		
		$scope.modules = function(column) {
			var def = $q.defer();

			/* http service is based on $q service */
			InterviewsService.getDistinctModules().then(function(response) {

				var arr = [],
				module = [];
				angular.forEach(response.data, function(item) {
					if (!_.find(module, _.matchesProperty('title', item.interviewModuleName))) {
						if(item.idModule != $sessionStorage.activeIntro.value){
							arr.push(item.interviewModuleName);
							module.push({
								'id': item.interviewModuleName,
								'title': item.interviewModuleName
							});
							module =  _.sortBy(module, 'title');
							arr = arr.sort();
						}
					}
				});

				/* whenever the data is available it resolves the object */
				def.resolve(module);

			});

			return def;
		};

		
		
		
		
		// QuestionsService.getAllMultipleQuestion().then(function(response){
		// if(response.status == '200'){
		// $scope.multipleQuestions = response.data;
		// }
		// });
		
		
		$scope.openInterviewBtn = function(){
			
		}
		
		self.cols = [
		    { field: "idParticipant", title: "idParticipant"}
		];
		
		
		self.openFiredRules = function(interviewId,reference,interviewModuleName) {
			
			InterviewsService.findModulesByInterviewId(interviewId).then(function(response){
				if(response.status == '200'){
					if(response.data.length > 0){								
						var modules = response.data;

						var isSameIntroModule = true;
						var introModule = _.find(modules,function(module){
							return module.idModule == $sessionStorage.activeIntro.value;
						});
						
						if(!introModule){
							isSameIntroModule = false;
						}
						
						var interview = {
								interviewId:interviewId,
								referenceNumber:reference,
								moduleName:interviewModuleName,
								isSameIntroModule:isSameIntroModule
						};
				    	$scope.addFiredRulesTab(interview);
						
				    	console.log('FiredRulesTab Opened');
					}
				}
			});
	    }
		
		
		$scope.getInterviewForCSVButton = function(){
			$scope.showExportCSV();
		};
		
		$scope.getCSVButton = function(){
			return $scope.csv;
		}
		
//		AssessmentsService.getAssessmentSize('All').then(function(response){				
//			$scope.totalAssessmentSize = 0;
//			if(response.status == '200'){
//				$scope.totalAssessmentSize = response.data;
//			}
//		});
		
		$scope.updateAutoAssessmentsButton = function(ev) {
			var status = 'All';
			
			$scope.status = status;
			$scope.estimatedDuration = getEstimatedDuration(status);
			$scope.showButtons = true;
			$mdDialog.show({
				scope: $scope,  
				preserveScope: true,
				templateUrl : 'scripts/assessments/partials/autoAssessmentDialog.html',
				clickOutsideToClose:false
			});

		}
				
		$scope.updateButton = function(status) {
			$scope.updateButtonDisabled = true;
			$scope.processAutoAssessment = true;
			$scope.showButtons = false;
			AssessmentsService.updateAutoAssessments(status).then(function(response){
				if(response){
					$scope.updateButtonDisabled = false;
					$scope.processAutoAssessment = false;
					$scope.cancel();
					$ngToast.create({
	      	    		  className: 'success',
	      	    		  content: 'Auto assessment is completed.',
	      	    		  animation:'slide'
	      	    	 });
					console.log('Assessments Updated: '+status);
					self.tableParams.reload();
				}
			});	  
		}
		
		// Make a guesstimate, 3 seconds per assessment
		function getEstimatedDuration(status){
			
			var estimateInMin = 0;
			var defaultDurationInSec = 3; // Slow
			
			if(status === 'All'){
				estimateInMin = defaultDurationInSec * self.tableParams.total();
			}
			else if(status === 'Auto Assessed'){
				estimateInMin = defaultDurationInSec *  $scope.assessedSize;
			}
			else{
				estimateInMin = defaultDurationInSec * $scope.notAssessedSize;
			}
			
			return estimateInMin/60;
		}
		
		$scope.showExportCSVButton = function() {
		// get list of interview id
		InterviewsService.getInterviewIdList().then(function(response){
			if(response.status == '200'){
				// display modal with list of id + progress bar
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
				$scope.csv = [];
				$scope.csvTemp = [{
					Q:[]
				}];
				var listOfQuestion = [];
				InterviewsService.getInterviewsWithoutAnswers().then(function(response){
					if(response.status == '200'){
						var questionIdList = listAllInterviewQuestions(response.data,$scope.csvTemp,listOfQuestion);
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
								addToReports("Export Report CSV",$scope.csv,"completed");
				            }, 1000);	
						}, function(err) {
							addToReports("Export Report CSV",$scope.csv,"error");
							console.log('error');
						});
					}
				});
			}
		});
		
		function addToReports(name,data,status){
			var jsonData = JSON.stringify(data);
			var report = {
				type:"Module JSON (Export)",
				name:name,
				path:"",
				status:status,
				progress:"100",
				requestor:$sessionStorage.userId,
				updatedBy:$sessionStorage.userId,
				jsonData:jsonData
			}
			ReportsService.save(report).then(function(response){
				if(response.status == '200'){
					console.log("reports was successfully saved.");
				}
			});
		}
		
		function populateInterviewAnswerList(interviewId,questionIdList){
			return InterviewsService.getInterviewQuestionAnswer(interviewId).then(function(response){
				if(response.status == '200'){
					listAllInterviewAnswers(response.data,$scope.csvTemp,questionIdList);								
				}
			});
		}
		
			// loop through and trigger
			// InterviewService.getInterview(interviewId)
			// with deffered
		};
		function generateUniqueAgentsList(interviews){			
			_.each(interviews,function(interview){
				appendToUniqueListOfAgents(interview);					
			});							
		}
		function prepareHeaderRow(){			
			var obj = {A:[]};
			obj.A.push("InterviewId");
			obj.A.push("ReferenceNumber");
			_.each($scope.uniqueListOfAgents,function(agent){				
				obj.A.push(agent.name+"_Auto");				
				obj.A.push(agent.name+"_Manual");
			});
			$scope.csv.push(obj.A);						
		}
		function prepareNoiseHeaderRow(){			
			var obj = {A:[]};
			obj.A.push("InterviewId");
			obj.A.push("ReferenceNumber");
			obj.A.push("totalPartialExposure");
			obj.A.push("autoExposureLevel");
			obj.A.push("peakNoise");
			obj.A.push("ShiftLength");
			
			$scope.csv.push(obj.A);						
		}
		$scope.showExportAssessmentCSVButton = function() {
			// get list of interview id
			InterviewsService.getInterviewsListWithRules().then(function(response){
				if(response.status == '200'){
					// display modal with list of id + progress bar
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
					$scope.csv = [];
					$scope.csvTemp = [{
						Q:[]
					}];
					generateUniqueAgentsList(response.data);
					prepareHeaderRow();
					$scope.interviewIdList.reduce(function(p, interview) {
					    return p.then(function() {
					    	$scope.interviewIdInProgress = interview.interviewId;
					    	$scope.counter++;
							$scope.interviewCount = $scope.counter;
					        return convertInterviewToAssessmentRow(interview.interviewId);
					    });
					}, $q.when(true)).then(function(finalResult) {
						console.log('finish extracting data for CSV');
						$timeout(function() {
							angular.element(document.querySelector('#exportAssessmentCSV')).triggerHandler('click');
							$scope.cancel();
			            }, 1000);	
					}, function(err) {
						console.log('error');
					});
				}
			});
			function convertInterviewToAssessmentRow(interviewId){
				return InterviewsService.getInterviewWithRules(interviewId).then(function(response){
					if(response.status == '200'){
						addAssessmentRowToCsv(response.data[0]);
					}
				});
			}
		};
		$scope.showExportAssessmentNoiseCSVButton = function() {
			SystemPropertyService.getAll().then(function(response){
				var sysprops = response.data;
				var ssagents = _.filter(sysprops, function(sysprop) {
					return sysprop.type=='studyagent';
				});
				$scope.agents = [];
				_.each(ssagents, function(ssagent) {
					var agent = {name:ssagent.name,idAgent:ssagent.value};
					$scope.agents.push(agent);
				});
				// get list of interview id
				InterviewsService.getInterviewsListWithRules().then(function(response){
					if(response.status == '200'){
						// display modal with list of id + progress bar
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
						$scope.csv = [];
						$scope.csvTemp = [{
							Q:[]
						}];
						if($scope.agents.length==0){
							generateUniqueAgentsList(response.data);
						}else{
							$scope.uniqueListOfAgents = $scope.agents;
						}
						prepareNoiseHeaderRow();
						$scope.interviewIdList.reduce(function(p, interview) {
						    return p.then(function() {
						    	$scope.interviewIdInProgress = interview.interviewId;
						    	$scope.counter++;
								$scope.interviewCount = $scope.counter;
						        return convertInterviewToAssessmentNoiseRow(interview.interviewId);
						    });
						}, $q.when(true)).then(function(finalResult) {
							console.log('finish extracting data for CSV');
							$timeout(function() {
								angular.element(document.querySelector('#exportAssessmentCSV')).triggerHandler('click');
								$scope.cancel();
				            }, 1000);	
						}, function(err) {
							console.log('error');
						});
					}
				});
				function convertInterviewToAssessmentNoiseRow(interviewId){
					return AssessmentsService.updateFiredRules(interviewId).then(function(response){
						if(response.status == '200'){
							addAssessmentNoiseRowToCsv(response.data[0]);
						}
					});
				}				
			});			
		};
		$scope.cancel = function() {
			$mdDialog.cancel();
		};
		$scope.uniqueListOfAgents = [];
		function appendToUniqueListOfAgents(interview){
			_.each(interview.agents,function(agent){
				var agentInList = false;
				_.each($scope.uniqueListOfAgents,function(agnt){
					if(agnt.idAgent==agent.idAgent){
						agentInList = true;
					}					
				});
				if(!agentInList){
					$scope.uniqueListOfAgents.push(agent);
				}				
			});
		}
		function addAssessmentRowToCsv(interview){
			var obj = {A:[]};
			obj.A.push(interview.interviewId);
			obj.A.push(interview.referenceNumber);
			_.each($scope.uniqueListOfAgents,function(agent){
				var level = "Not Set";
				_.each(interview.autoAssessedRules,function(rule){
					if(agent.idAgent==rule.agent.idAgent){
						level = rule.level;	
					}								
				});	
				obj.A.push(level);
				level = "Not Set";
				_.each(interview.manualAssessedRules,function(rule){
					if(agent.idAgent==rule.agent.idAgent){
						level = rule.level;	
					}								
				});
				obj.A.push(level);
			});
			$scope.csv.push(obj.A);
		}
		function addAssessmentNoiseRowToCsv(interview){
			var model = interview;
			var obj = {A:[]};
			obj.A.push(interview.interviewId);
			obj.A.push(interview.referenceNumber);
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
						  // if(model.module){
							// cascadeFindNode(model.module.nodes,parentNode);
						  // }else{
							  cascadeFindNode(model.answerHistory,parentNode); 
						  // }
						  var answeredValue = 0;
						  if($scope.foundNode){
							 var frequencyHoursNode = findFrequencyIdNode($scope.foundNode);
								
							 if(frequencyHoursNode){
								 answeredValue = frequencyHoursNode.answerFreetext;
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
					  if(noiseRule.ruleAdditionalfields == undefined){
						  alert("Error on Noise rule:"+noiseRule.idRule);
						  break;
					  }
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
						// if(model.module){
							  cascadeFindNode(model.answerHistory,parentNode);
						// }else{
						// cascadeFindNode(model.fragment.nodes,parentNode);
						 // }
						  if($scope.foundNode){
							  var frequencyHoursNode = findFrequencyIdNode($scope.foundNode);								  
							  if(frequencyHoursNode){
								  frequencyhours = frequencyHoursNode.answerFreetext;
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
								partialExposure:partialExposure}
				
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
					obj.A.push(totalPartialExposure);
					obj.A.push(autoExposureLevel);
					obj.A.push(peakNoise);
					obj.A.push(shiftHours);
			  }
			$scope.csv.push(obj.A);
		}
		function listAllInterviewAnswers(response,csvTemp,questionIdList){
			_.each(response,function(data){
				var obj = {A:[]};
				obj.A.push(data.interviewId);
				obj.A.push(data.referenceNumber);
				// put notes and status
// obj.A.push(data.module.idNode);
				_.each(questionIdList,function(qId){
					// check if qId has delimeter "_" which means is multiple
					if(typeof qId == 'string' && qId.indexOf('_') > -1){
						var temp = qId.split('_');
						var questionId = temp[0];
						var number = temp[1];
						var question = _.find(data.questionHistory,function(qHistory){
							return qHistory.questionId == questionId
							&& qHistory.deleted == 0;
						});
						if(question){
							if(question.answers.length > 0){
								var numberExist = false;
								_.each(question.answers,function(ans){
									if(ans.number == number && !numberExist){
										if(ans.answerFreetext){
											obj.A.push(ans.answerFreetext);
											numberExist = true;
										}else{
											obj.A.push(ans.name);
											numberExist = true;
											// console.error("could not find
											// freetext");
											// invalid = true;
											// notes = ""
										}
									}
								})
								if(!numberExist){
									obj.A.push("-- No Answer --");
								}
							}
						}else{
							obj.A.push("-- Question Not Asked --");
						}		
					}else{
						var question = _.find(data.questionHistory,function(qHistory){
							return qHistory.questionId == qId
							&& qHistory.deleted == 0;
						});
						if(question){
							// check for deleted
							if(question.answers.length > 0){
								    var ans = question.answers[0];
									if(ans.answerFreetext){
										obj.A.push(ans.answerFreetext);	
									}else{
										obj.A.push(ans.name);
									}
							}else{
								obj.A.push("-- No Answer --");
							}
						}else{
							obj.A.push("-- Question Not Asked --");
						}
					}
				});
				$scope.csv.push(obj.A);
			});
		}
		
		function listAllInterviewQuestions(response,csvTemp,listOfQuestion){
			var questionIdList = [];
			_.each(response,function(data){
				data.questionHistory = _.filter(data.questionHistory,function(qh){
					// $log.info("Interviewid: "+data.interviewId+" Questionid:
					// "+qh.questionId);
					return qh.deleted == 0 && qh.topNodeId==15001;
				});
				// $log.info("Interviewid: "+data.interviewId+" Questionid: ");
				// join all questions to listOfQuestion
				listOfQuestion = listOfQuestion.concat(data.questionHistory);
			});
			var sortHeaderList = {};
			var header = "";
			_.each(listOfQuestion,function(data){
				// check if the unique question is a module/ajsm or fragment, if
				// yes add it to the header
				// to be display along with the question number in the CSV
				if(data.nodeClass == 'M' || data.type == 'M_IntroModule' || data.type == 'Q_linkedajsm' || data.type == 'F_ajsm'){
					if(header != ""){
						sortHeaderList[header] = _.sortBy(sortHeaderList[header], 'header');
					}
					header = data.name.substring(0, 4);
					if(!sortHeaderList[topHeader]){
						sortHeaderList[topHeader] = [];
					}
				// if the unique question is an actual question get the number
				// and append to its
				// respective header which can be a module/ajsm or fragment
				}else if(data.questionId){
					// check if the question is of type Multiple, if yes will
					// need to
					// add header for each possible answer
					if(data.type == 'Q_multiple'){
						// check if question is in the multiple question bucket
						var nodeQuestion = _.find($scope.multipleQuestions,function(question){
							return question.idNode == data.questionId;
						});
						// look for the top node id in listquestion
						// build the header and check it in sortHeaderList
						var topModule = _.find(listOfQuestion,function(lq){
							return lq.link == data.topNodeId;
						});
						var topHeader = topModule.name.substring(0, 4);
						if(!sortHeaderList[topHeader]){
						sortHeaderList[topHeader] = [];
						}
						_.each(nodeQuestion.nodes,function(posAns){
							// loop through all possible answer
							sortHeaderList[topHeader].push({
								header:topHeader+"_"+data.number +"_"+posAns.number,
								questionId:data.questionId +"_"+posAns.number
							});
							
						});
					}
					// for standard questions add the header + question number
					else{
					// look for the top node id in listquestion
					// build the header and check it in sortHeaderList
						if(data.questionId==43552){
							console.log(data);
						}
					var topModule = _.find(listOfQuestion,function(lq){
						return lq.link == data.topNodeId;
					});
					var topHeader = topModule.name.substring(0, 4);
					if(!sortHeaderList[topHeader]){
						sortHeaderList[topHeader] = [];
					}
					sortHeaderList[topHeader].push({
						header:topHeader+"_"+data.number,
						questionId:data.questionId
					});
					}
				}
			});
			
			
			_.each(sortHeaderList,function(headerGroup){
				
				// var uniqueHeaders =
				// _.unionBy(headers,headers,function(o){return o.header;});
				_.each(headerGroup,function(data){
					csvTemp[0].Q.push(data.header);
					questionIdList.push(data.questionId);
				})
			});
			csvTemp[0].Q.unshift('AWES ID');
			csvTemp[0].Q.unshift('Interview Id');
			$scope.csv.push(csvTemp[0].Q);
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
		
		$scope.assessmentFilter = {
				idParticipant:null,
				interviewId:null,
				reference:null,
				status:null,
				interviewModuleName:null,
				pageNumber:null,
				size:null
		};
		
		self.tableParams = new NgTableParams(
				{	
					page: 1,            
	                count: 10
	            }, 
				{	
					getData: function(params) {
						var currentPage = $scope.assessmentFilter.pageNumber;
						$scope.assessmentFilter.idParticipant=lengthGreaterThan2(params.filter().idParticipant);
						$scope.assessmentFilter.interviewId=lengthGreaterThan2(params.filter().idinterview);
						$scope.assessmentFilter.reference=lengthGreaterThan2(params.filter().reference);
						/*if(lengthGreaterThan2(params.filter().status)){
							if(params.filter().status.startsWith('run') ){
								$scope.assessmentFilter.status = 0
							}else if(params.filter().status.startsWith('par') ){
								$scope.assessmentFilter.status = 1
							}else if(params.filter().status.startsWith('com') ){
								$scope.assessmentFilter.status = 2
							}else if(params.filter().status.startsWith('tob') ){
								$scope.assessmentFilter.status = 3
							}						 
						}*/
						$scope.assessmentFilter.status = params.filter().status;
						$scope.assessmentFilter.assessedStatus=lengthGreaterThan2(params.filter().assessedStatus);
						$scope.assessmentFilter.interviewModuleName=lengthGreaterThan2(params.filter().interviewModuleName);
						$scope.assessmentFilter.pageNumber=params.page();
						$scope.assessmentFilter.size=params.count();
						params.goToPageNumber = null;
						var assessmentFilter = $scope.assessmentFilter;
						if(!self.tableParams.settings().dataset || (assessmentFilter.pageNumber != currentPage)  
								|| assessmentFilter.idParticipant
								|| assessmentFilter.interviewId || assessmentFilter.reference
								|| assessmentFilter.status || (assessmentFilter.status===0) || assessmentFilter.interviewModuleName || 
								ifEmptyFilter(params.filter())){
					    $log.info("Data getting from interviews ajax ..."); 
					    return ParticipantsService.getPaginatedAssessmentWithModList(assessmentFilter).then(function(response) {
				        	  if(response.status == '200'){
				        		  var data = response.data.content;
				        		  $scope.currentParticipants = data;
// _.each(data,function(participant){
// participant.interviewId = participant.interviews[0].interviewId;
// });
				        		  console.log("Data get list from getParticipants ajax ...");        	 
				        		  self.originalData = angular.copy(data);
					        	  self.tableParams.settings().dataset = data;
					        	  self.tableParams.shouldGetData = false;
					        	  self.tableParams.total(response.data.totalSize);					        	  
						          return data;
				        	  }
				          });
						}
					},
				});
				self.tableParams.shouldGetData = true;
				$scope.isModulesSet = false;
				$scope.refreshModules = function(){
					$scope.isModulesSet = true;
					SystemPropertyService.getByName("activeintro").then(function(response){
						if(response.status == '200'){
							var introModuleId = response.data.value;
							_.each(self.tableParams.settings().dataset,function(participant){
								participant.inProgress = true;
								InterviewsService.findModulesByInterviewId(participant.interviewId).then(function(response){
									if(response.status == '200'){
										var introModule = "";
										var idModule = "";
										if(!response.data || response.data.length == 0){								
											participant.module = 'Error no module.';
										}else{
											participant.module = "";
											for(var i=0;i<response.data.length;i++){
												var module = response.data[i];
												if(module.idModule!=introModuleId){
													participant.module += module.interviewModuleName;
													participant.idModule += module.idModule;
													if(i<response.data.length-1){
														participant.module += ":";
													}
												}else{
													introModule = module.interviewModuleName;
													idModule = module.idModule;
												}												
											}										
										}
										if(participant.module==""){								
											participant.module = introModule;
											participant.idModule += idModule;
										}
										participant.inProgress = false;
									}
								});
							})
						}
					});
					
				}
		
		function ifEmptyFilter(filter){
			if((!filter.idParticipant || filter.idParticipant.length == 0)&&
				(!filter.idinterview || filter.idinterview.length == 0)&&
				(!filter.reference || filter.reference.length == 0)&&
				(!filter.status || filter.status.length == 0)&&
				(!filter.interviewModuleName || filter.interviewModuleName.length == 0)){
						return true;
				}
		}		
				
		function lengthGreaterThan2(variable){
			if(variable && variable.length > 2){
				return variable;
			}else{
				return null;
			}
		}
				
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
									partialExposure:partialExposure}
					
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
        
		self.tableParams.goTo = function(event){
	        if(event.keyCode == 13 
	        		&& self.tableParams.goToPageNumber != null
	        		&& !isNaN(self.tableParams.goToPageNumber)){
	        	self.tableParams.page(self.tableParams.goToPageNumber);
	        	self.tableParams.reload();
	        }
	    };
        
	}
})();

