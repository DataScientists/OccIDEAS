(function(){
	angular.module('occIDEASApp.Assessments')
		   .controller('AssessmentsCtrl',AssessmentsCtrl);
	AssessmentsCtrl.$inject = ['AssessmentsService','InterviewsService','RulesService','ngTableParams','$scope','$filter',
                          'data','$log','$compile','$http','$q','$mdDialog','$timeout','ParticipantsService','QuestionsService'
                          ,'$sessionStorage','ReportsService','SystemPropertyService'];
	function AssessmentsCtrl(AssessmentsService,InterviewsService,RulesService,NgTableParams,$scope,$filter,
			data,$log,$compile,$http,$q,$mdDialog,$timeout,ParticipantsService,QuestionsService,
			$sessionStorage,ReportsService,SystemPropertyService){
		var self = this;
		$scope.data = data;
		$scope.$root.tabsLoading = false;
		
		QuestionsService.getAllMultipleQuestion().then(function(response){
			if(response.status == '200'){
				$scope.multipleQuestions = response.data;
			}
		});
		
		
		$scope.openInterviewBtn = function(){
			
		}
		
		self.cols = [
		    { field: "idParticipant", title: "idParticipant"}
		];
		
		
		self.openFiredRules = function(interview) {
	    	$scope.addFiredRulesTab(interview);
	    }
		
		
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
				type:"csv",
				name:name,
				path:"",
				status:status,
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
		
			//loop through and trigger InterviewService.getInterview(interviewId) 
			//with deffered
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
			//get list of interview id
			InterviewsService.getInterviewsListWithRules().then(function(response){
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
				//get list of interview id
				InterviewsService.getInterviewsListWithRules().then(function(response){
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
						  //if(model.module){
							//  cascadeFindNode(model.module.nodes,parentNode);
						  //}else{
							  cascadeFindNode(model.answerHistory,parentNode); 
						  //}
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
						//if(model.module){
							  cascadeFindNode(model.answerHistory,parentNode);
						//  }else{
						//	  cascadeFindNode(model.fragment.nodes,parentNode); 
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
				//put notes and status
//				obj.A.push(data.module.idNode);
				_.each(questionIdList,function(qId){
					//check if qId has delimeter "_" which means is multiple
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
											//console.error("could not find freetext");
											//invalid = true;
											//notes = ""
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
							//check for deleted
							if(question.answers.length > 0){
								    var ans = question.answers[0];
									if(ans.answerFreetext){
										obj.A.push(ans.answerFreetext);	
									}else{
										obj.A.push(ans.name);
									}
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
					//$log.info("Interviewid: "+data.interviewId+" Questionid: "+qh.questionId);
					return qh.deleted == 0;
				});
				//$log.info("Interviewid: "+data.interviewId+" Questionid: "); 
				// join all questions to listOfQuestion
				listOfQuestion = listOfQuestion.concat(data.questionHistory);
			});
			var sortHeaderList = {};
			var header = "";
			_.each(listOfQuestion,function(data){
				//check if the unique question is a module/ajsm or fragment, if yes add it to the header
				// to be display along with the question number in the CSV
				if(data.nodeClass == 'M' || data.type == 'M_IntroModule' || data.type == 'Q_linkedajsm' || data.type == 'F_ajsm'){
					if(header != ""){
						sortHeaderList[header] = _.sortBy(sortHeaderList[header], 'header');
					}
					header = data.name.substring(0, 4);
					sortHeaderList[header] = [];
				// if the unique question is an actual question get the number and append to its
				// respective header which can be a module/ajsm or fragment
				}else if(data.questionId){
					//check if the question is of type Multiple, if yes will need to 
					//add header for each possible answer
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
							//loop through all possible answer
							sortHeaderList[topHeader].push({
								header:topHeader+"_"+data.number +"_"+posAns.number,
								questionId:data.questionId +"_"+posAns.number
							});
							
						});
					}
					//for standard questions add the header + question number
					else{
					// look for the top node id in listquestion
					// build the header and check it in sortHeaderList
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
				
				//var uniqueHeaders = _.unionBy(headers,headers,function(o){return o.header;});
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
		self.tableParams = new NgTableParams(
				{
					               
				}, 
				{	
					getData: function(params) {
//						if((params.filter().referenceNumber)||(params.filter().moduleName)){	
//				        	return $filter('filter')(self.tableParams.settings().dataset, params.filter());
//				        }						
//						if ((params.sorting().referenceNumber)||(params.sorting().moduleName)){
//							return $filter('orderBy')(self.tableParams.settings().dataset, params.orderBy());
//				        }
					    if(!self.tableParams.shouldGetData){
					    	return self.tableParams.settings().dataset;
					    }
					    $log.info("Data getting from interviews ajax ..."); 
					    return ParticipantsService.getParticipants().then(function(response) {
				        	  if(response.status == '200'){
				        		  var data = response.data;
				        		  console.log("Data get list from getParticipants ajax ...");        	 
				        		  self.originalData = angular.copy(data);
					        	  self.tableParams.settings().dataset = data;
					        	  self.tableParams.shouldGetData = false;
					        	  self.tableParams.total(self.originalData.length);
					        	  var last = params.page() * params.count();
						          return self.originalData;
				        	  }
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
	}
})();

