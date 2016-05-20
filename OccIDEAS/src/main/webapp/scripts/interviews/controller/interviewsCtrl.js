(function() {
	angular.module('occIDEASApp.Interviews').controller('InterviewsCtrl',
			InterviewsCtrl);
	
	InterviewsCtrl.$inject = [ 'data', '$scope', '$mdDialog',
			'FragmentsService', '$q', 'QuestionsService', 'ModulesService',
			'InterviewsService', 'ParticipantsService', 'AssessmentsService',
			'$anchorScroll', '$location', '$mdMedia', '$window', '$state',
			'$rootScope', '$compile', '$timeout', '$log', 'updateData','startWithReferenceNumber'];
	function InterviewsCtrl(data, $scope, $mdDialog, FragmentsService, $q,
			QuestionsService, ModulesService, InterviewsService,
			ParticipantsService, AssessmentsService, $anchorScroll, $location,
			$mdMedia, $window, $state, $rootScope, $compile, $timeout, $log,
			updateData,startWithReferenceNumber) {
		var self = this;
		$scope.data = data;
		$scope.displayQuestions=[];
		$scope.$root.tabsLoading = false;
		$scope.multiSelected = [];
		$scope.questionHistory = [];
		$scope.referenceNumber = null;
		$scope.intQuestionSequence = 0;

		function add(type) {
	    	$scope.addInterviewTabInterviewers();
	    }
		$scope.awesId = {};	
		$scope.startInterview = function(data) {
			$scope.intDisplay = [];
			$scope.referenceNumber = $scope.awesId.id;
			createParticipant(data);
							
		}
		if(!$scope.interview){
			console.log($scope.interviewId);
			if(startWithReferenceNumber){	
				$scope.awesId.id = startWithReferenceNumber;
	    		$scope.startInterview(data);		
			}
		}		
		if (updateData) {
			$log.info("updateData is not null... interview continuation initializing...");
			
			populateInterviewModules(updateData[0].interviewId);
		}	
		function getLastQuestionAsked(mod){
			return _.maxBy(mod.questionsAsked, function(o) { 
				return o.intQuestionSequence; 
			});
		}	 
		function populateInterviewModules(interviewId){
			$scope.interviewId = interviewId;			
			InterviewsService.getInterview(interviewId).then(function(data){
				if(data){
					var interview = data.data[0];
					$scope.intDisplay = [];
					if(interview.modules.length==0){
						interview.module.questionsAsked = interview.actualQuestion;
						var module = interview.module;//start at first module again
						module.count = 1;
						interview.modules.push(module);
					}
					var modules = interview.modules;
					interview.active = true;
					$scope.participant = interview.participant;
					var participant = $scope.participant;
					participant.status = 0;//running
					saveParticipant(participant);
					
					$scope.interview = interview;
					$scope.interview.interviewStarted = true;
					$scope.interview.interviewEnded = false;	
					var lastQuestion = interview.actualQuestion[interview.actualQuestion.length - 1];						
					determineNextUnansweredQuestion(lastQuestion);
					
					
				}
				
			})
		}

		$rootScope.$on('InterviewCtrl:update', function(event, elId) {
			$scope.addClassWithTimeout('IntResult' + elId);
			safeDigest($scope.interviews);
		});
		$scope.isCollapsableNode = function(node) {
			if (node) {
				if (node.nodes.length == 0) {
					return false;
				} else if (node.type == 'I') {
					return false;
				} else {
					return true;
				}
			} else {
				return false;
			}
		}
		$scope.isEditableNode = function(node) {
			if (node) {
				if (node.type == 'Q') {
					return true;
				}
			} else {
				return false;
			}
		}
		$scope.addClassWithTimeout = function(elId) {
			$timeout(function() {
				if (!$('#' + elId).hasClass("highlight")) {
					$('#' + elId).addClass("highlight");
				}
			}, 1000);
		};

		function getPreviousNumber(number){
			var retValue = 0;
			if(isNaN(number)){
				var numArray = number.split(/[a-zA-Z]+/)
				var lastNumber = numArray[numArray.length - 1];
				var newNumber = lastNumber -1;
				if(newNumber>0){
					retValue = number.substr(0, number.lastIndexOf(lastNumber));
					retValue = retValue + newNumber;
				}else{
					retValue = number.substr(0, number.lastIndexOf(lastNumber));
				}				
			}else{
				if(number>0){
					retValue = number - 1;
				}
			}
			return retValue;
		}
		self.editQuestion = function(question) {
			if(!question.count){
				question.count=1;
			}
			InterviewsService.getIntQuestion(question.idInterview,question.questionId,question.count).then(
			  function(response) {
				if (response.status === 200) {
					console.log("retrieve question successful...");
					question = response.data;
					question.nodes = question.answers;
					refreshInterview();
					$scope.inProgress = true;
					$scope.updateEnable = true;
					$scope.updateFirst = true;
					safeDigest($scope.inProgress);
					
					var newNum = getPreviousNumber(question.number);
					var parentId = question.number.length > 1 ? question.parentAnswerId
							: question.parentId;
					var actualQuestion = {};
					actualQuestion.topNodeId = question.topNodeId;
					actualQuestion.questionId = question.questionId;
					actualQuestion.parentId = parentId,
					actualQuestion.number = newNum;
					var topMod = _.find($scope.interview.modules, function(mod) {
						return mod.idNode == question.topNodeId &&
						mod.count == question.modCount;
					});
					var modCount = 1;
					if(topMod){
						modCount = topMod.count;
					}
					var status = showNextQuestion(actualQuestion, false, true,modCount);
					if (status) {
						status.then(function(data) {
							if (data == 200) {
								var answerList = [];
								var mod = _.find($scope.interview.modules,function(mod) {
											return mod.idNode == $scope.interview.showedQuestion.topNodeId;
										});
								var qs = _.find(mod.questionsAsked,function(qs) {
											return $scope.interview.showedQuestion.idNode == qs.questionId;
										});
								if (qs.type == 'Q_multiple') {
									_.each($scope.interview.showedQuestion.nodes,
											function(node) {
												_.find(qs.answers,
												function(ans) {
												if (ans.answerId == node.idNode) {
													$scope.multiToggle(
														node,
														$scope.multiSelected);
															node.isSelected = true;
															answerList.push(ans);
															}
														});
											});
									$scope.previousAnswer = answerList;
								} else {
									var qs = _.find(mod.questionsAsked,function(qs) {
												return $scope.interview.showedQuestion.idNode == qs.questionId;
											});
									var ansNode = _.find($scope.interview.showedQuestion.nodes,function(ans) {
												return ans.idNode == qs.answers[0].answerId;
											});
									_.find(qs.answers, function(ans) {
										if (ans.answerId == ansNode.idNode) {
											answerList.push(ans);
										}
									});
									//P_freetext
									if(ansNode.type == 'P_freetext'){
										ansNode.name = qs.answers[0].name;
									}
									$scope.interview.showedQuestion.selectedAnswer = ansNode;
									$scope.previousAnswer = angular.copy(ansNode);
								}
								// deleteQuestion([question]);
							}
						});
					}
					
					
					
				}
			});
		};

		function determineNextUnansweredQuestion(question) {
			var questionAsked = hasQuestionBeenAsked(question);
			if (questionAsked && !$scope.updateFirst) {
				if (questionAsked.answers.length == 1) {
					var actualQuestion = {
						topNodeId : questionAsked.topNodeId,
						questionId : questionAsked.questionId,
						parentId : questionAsked.answers[0].answerId,
						number : questionAsked.answers[0].number,
						link : questionAsked.link
					}
					return showNextQuestion(actualQuestion, true, false,
							question.count);
				}
			}
			$scope.updateFirst = false;
			$scope.interview.showedQuestion = question;
			return false;
		}

		function refreshInterview() {
			$scope.interview.interviewStarted = true;
			$scope.interview.interviewEnded = false;
			safeDigest($scope.interview.interviewStarted);
		}

		function deleteQuestionWithParentAnswer(answerList) {
			var mod = _.find($scope.interview.modules, function(mod) {
				return mod.idNode == $scope.interview.showedQuestion.topNodeId;
			});
			
			for (var i = 0; i < answerList.length; i++) {
				var answer = answerList[i];
				for (var j = 0; j < mod.questionsAsked.length; j++) {
					if (mod.questionsAsked[j].questionId == answer.parentQuestionId
							|| mod.questionsAsked[j].parentAnswerId == answer.answerId) {
						var question = mod.questionsAsked[j];
						deleteModuleWithParentAnswer(answer);
						removeLinksFromInterviewDisplay(answer.answerId);
						question.deleted = 1;
						answer.deleted = 1;
						if (mod.questionsAsked[j].parentAnswerId == answer.answerId) {
							deleteQuestionWithParentAnswer(question.answers);
						}
						addModuleCount();
						var defer = $q.defer();
						saveAnswer(question,defer);
						defer.promise.then(function(){
						InterviewsService
								.saveQuestion(question)
								.then(
										function(response) {
											if (response.status === 200) {
												console
														.log("Delete question successful...");
												_
														.remove(
																mod.questionsAsked,
																function(val) {
																	if(val.deleted == 1){
																		deleteIntDisplay(val.questionId,false);
																	}
																	return val.deleted == 1;
																});
											}
										});
						});
					}
				}
			}
		}

		function deleteModuleWithParentAnswer(answer) {
			_.find($scope.interview.modules, function(mod, index) {
				if (index == 0) {
					return false;
				}
				if (!mod) {
					return false;
				}
				if (mod.parentAnswerId == answer.answerId) {
					_.each(mod.questionsAsked, function(qs) {
						mod.deleted = 1;
						qs.deleted = 1;
						deleteIntDisplay(qs.questionId,false);
						_.each(qs.answers, function(ans) {
							ans.deleted = 1;
							saveAnswer(angular.copy(qs));
							deleteModuleWithParentAnswer(ans);
						});
						saveQuestion(angular.copy(qs));
						saveModule(angular.copy(mod));
					});
					return;
				}
				_.remove($scope.interview.modules, function(mod) {
					if(mod){
						if (mod.answerNode && mod.answerNode == answer.answerId) {
							deleteModuleWithParentModule(mod);
							InterviewsService.saveInterviewMod(mod).then(function(response) {
								if (response.status === 200) {
									$log.info("Removed module "+mod.name);
								}else{
									$log.error("Error in saveInterviewMod");
								}
							});
							return true;
						}
					}
					return false;
				});
			});
		}

		function deleteModuleWithParentModule(module) {
			_.remove($scope.interview.modules, function(mod) {
				if (mod.parentAnswerId && mod.parentAnswerId == module.idNode) {
					mod.deleted = 1;
					InterviewsService.saveInterviewMod(mod).then(
							function(response) {
								if (response.status === 200) {
									$log.info("Success in saveInterviewMod");
								}else{
									$log.error("Error in saveInterviewMod");
								}
					});
					deleteModuleWithParentModule(mod);
					deleteQuestion(angular.copy(mod.questionsAsked));
					return true;
				}
				return false;
			});
		}

		function deleteQuestion(questions, defer) {
			_.each(questions,function(question) {
				question.deleted = 1;
					_.each(question.answers,function(ans) {
								ans.deleted = 1;
								deleteQuestionWithParentAnswer(question.answers);
					});
					addModuleCount();
					var deferred = $q.defer();
					saveAnswer(question,deferred);
					deferred.promise.then(function(){
						InterviewsService.saveQuestion(question)
							.then(function(response) {
								if (response.status === 200) {
									console.log("Delete question successful...");
									var mod = _.find($scope.interview.modules,
											function(mod) {
												return mod.idNode == $scope.interview.showedQuestion.topNodeId
														&& mod.count == $scope.interview.showedQuestion.count;
													    });
														if (!mod) {
															mod = _
																	.find(
																			$scope.interview.modules,
																			function(
																					mod) {
																				return mod.idNode == $scope.interview.showedQuestion.topNodeId
																			});
														}
														_.remove(
																		mod.questionsAsked,
																		function(
																				val) {
																			if(val.deleted == 1){
																				deleteIntDisplay(question.questionId,false);
																			}
																			return val.deleted == 1;
																		});
														if(defer){
														defer.resolve();
														}
													} else {
														if(defer){
														defer.reject();
														}
													}
												});
								});
							})
							if(defer){
								return defer.promise;
							}
		}

		var safeDigest = function(obj) {
			if (!obj.$$phase) {
				try {
					obj.$digest();
				} catch (e) {
				}
			}
		}

		function populateNewQuestionAskedJsonByNode(interview, node) {
			return {
				idInterview : $scope.interviewId,
				topNodeId : node.topNodeId,
				questionId : node.idNode,
				parentId : $scope.parentQId ? $scope.parentQId : node.parentId,
				parentAnswerId : node.parentId,
				name : node.name,
				description : node.description,
				nodeClass : node.nodeclass,
				number : node.number,
				type : node.type,
				link : node.link,
				deleted : 0,
				answers : []
			};
		}

		function populateAnswerJsonByNode(value, node) {
			return {
				idInterview : $scope.interviewId,
				topQuestionId : node.topNodeId,
				parentQuestionId : node.idNode,
				answerId : value.idNode,
				name : value.name,
				answerFreetext : value.name,
				nodeClass : value.nodeclass,
				number : value.number,
				type : value.type,
				link : node.link,
				deleted : 0,
				isProcessed : false
			};
		}

		function processInterviewQuestionsWithMultipleAnswers(interview, node) {
			var deffered = undefined;
			var persistedAnswer = undefined;
			if ($scope.updateEnable
					&& $scope.previousAnswer != $scope.multiSelected) {
				var qs = hasQuestionBeenAsked(node);
				deffered = $q.defer();
				if (qs) {
					qsTemp = angular.copy(qs);
					qsAnswerCopy = angular.copy(qs.answers);
					qsTemp.answers = _.difference(qsAnswerCopy,
							$scope.multiSelected);
					var sameAnswer = _.difference($scope.multiSelected,qsAnswerCopy);
					_.each(sameAnswer,function(ans){
						var removedData = _.remove(qsAnswerCopy,function(val){
							return val.answerId== ans.idNode});
						if(removedData){
							if(persistedAnswer){
								persistedAnswer.push(removedData);
							}else{
								persistedAnswer = [];
								persistedAnswer.push(removedData);
							}
						}
					});
					qsTemp.answers = qsAnswerCopy;
					$scope.intQuestionSequence = qsTemp.intQuestionSequence;
					deleteQuestion([ angular.copy(qsTemp) ], deffered);
					_.remove($scope.interview.modules, function(mod) {
						return mod.deleted == 1;
					});
				} else {
					deffered.resolve();
				}
			}
			if (deffered) {
				deffered.promise.then(function() {
					buildAndSaveMultipleQuestion(interview, node,persistedAnswer);
				})
			} else {
				buildAndSaveMultipleQuestion(interview, node,persistedAnswer);
			}
		}

		function buildAndSaveMultipleQuestion(interview, node, persistedAnswer) {
			var mod = _.find(interview.modules,
					function(val, ind) {
						return val.idNode === node.topNodeId
								&& node.count == val.count;
					});
			if (!mod) {
				mod = _.find(interview.modules, function(val, ind) {
					return val.idNode === node.topNodeId;
				});
			}

			var newQuestionAsked = populateNewQuestionAskedJsonByNode(
					interview, node);

			var selectedEl = $scope.multiSelected;
			$scope.multiSelected = [];
			_.each(selectedEl, function(value, i) {
				var actualAnswer = populateAnswerJsonByNode(value, node);
				newQuestionAsked.answers.push(actualAnswer);
			});
			// check if question already exist, do not push, just update the
			// question in database
			var qsIndex = _.indexOf(mod.questionsAsked, _.find(
					mod.questionsAsked, function(qs) {
						return qs.questionId == node.idNode;
					}));
			if (qsIndex == -1) {
				newQuestionAsked.intQuestionSequence = angular.copy($scope.intQuestionSequence);
				mod.questionsAsked.push(newQuestionAsked);
				updateInterviewDisplay(newQuestionAsked);
				$scope.intQuestionSequence ++;
			} else {
				newQuestionAsked.intQuestionSequence = mod.questionsAsked[qsIndex].intQuestionSequence;
				mod.questionsAsked.splice(qsIndex, 1, newQuestionAsked);
				spliceIntDisplay(node.idNode,newQuestionAsked);
			}
			addModuleCount();
			var defer = $q.defer();
			newQuestionAsked.answers[0].isProcessed = true;
			saveAnswer(newQuestionAsked,defer);
			defer.promise.then(function(){
				//@TODO save on those answers not persisted
			InterviewsService.saveQuestion(newQuestionAsked).then(
					function(response) {
						if (response.status === 200) {
							syncQs(mod,response.data);
							var answer = newQuestionAsked.answers[0];
							var actualQuestion = {
								topNodeId : node.topNodeId,
								questionId : node.idNode,
								parentId : answer.answerId,
								number : answer.number,
								link : node.link
							}
							showNextQuestion(actualQuestion, true, false,
									mod.count);
							
						}
					});
					});
		}

		function spliceIntDisplay(idNode,element){
			var indexDisplay = _.indexOf($scope.intDisplay, _.find(
					$scope.intDisplay, function(idisp) {
						return idisp.questionId == idNode;
			}));
			var copyEl = angular.copy(element);
			copyEl.type=element.questionsAsked?"M":"Q";
			if(!element.count){
				addModuleCount();
			}
			copyEl.count = element.count?element.count:element.modCount;
			copyEl.idInterview = $scope.interviewId;
			copyEl.sequence = $scope.intDisplay[indexDisplay].sequence;
			InterviewsService.saveIntDisplay(copyEl).then(
					function(response) {
						if (response.status === 200) {
							$scope.intDisplay.splice(indexDisplay,1,copyEl);
							$log.info("Success in save interview display.");
						}else{
							$log.error("Error in save interview display");
						}
			});
		}
		
		function saveModule(mod){
			InterviewsService.saveInterviewMod(mod).then(
					function(response) {
						if (response.status === 200) {
							
						}
					});
		}
		
		function saveQuestion(question){
			InterviewsService.saveQuestion(question)
			.then(function(response) {
				if (response.status === 200) {
					
				}
			});
		}
		
		function saveAnswer(newQuestionAsked,defer){
			if(newQuestionAsked.answers.length > 0){
			InterviewsService.saveAnswers(newQuestionAsked.answers)
				.then(function(response){
					if (response.status === 200) {
						_.each(newQuestionAsked.answers,function(qas,ind){
							_.find(response.data,function(as,ind){
								if(qas.answerId == as.answerId){
									qas.id = as.id;
								};
							})
						});
						if(defer){
							defer.resolve();
						}	
//						syncAs(newQuestionAsked,response.data);
					}else{
						if(defer){
						defer.reject();
						}
					}
			});
			}else{
				if(defer){
				defer.resolve();
				}
			}
			if(defer){
			return defer.promise;
			}
		}
		
		function syncAs(newQuestionAsked,data){
			var mod = _.find($scope.interview.modules,function(mods,ind){
				return mods.idNode == newQuestionAsked.topNodeId 
					&& newQuestionAsked.modCount == mods.count; 
			});
			_.find(mod.questionsAsked,function(qs,ind){
				if(qs.questionId == newQuestionAsked.questionId
						&& qs.modCount == newQuestionAsked.modCount){
					mod.questionsAsked[ind].answers = data;
				}
			});
		}

		function processFrequency(interview, node) {
			var deffered = undefined;
			if ($scope.updateEnable
					&& $scope.previousAnswer != $scope.interview.showedQuestion.selectedAnswer) {
				var qs = hasQuestionBeenAsked(node);
				deffered = $q.defer();
				if (qs) {
					$scope.intQuestionSequence = qs.intQuestionSequence;
					$scope.parentQId = qs.parentId;
					deleteQuestion([ angular.copy(qs) ], deffered);
					_.remove($scope.interview.modules, function(mod) {
						return mod.deleted == 1;
					});
					node.selectedAnswer = $scope.interview.showedQuestion.selectedAnswer;
				} else {
					deffered.resolve();
				}
			}
			if (deffered) {
				deffered.promise.then(function() {
					buildAndSaveFrequency(interview, node);
				})
			} else {
				buildAndSaveFrequency(interview, node);
			}
		}

		function buildAndSaveFrequency(interview, node) {
			var hours = 0;
			if ($scope.interview.showedQuestion.hours) {
				hours = $scope.interview.showedQuestion.hours;
			}
			var minutes = 0;
			if ($scope.interview.showedQuestion.minutes) {
				minutes = $scope.interview.showedQuestion.minutes;
			}
			var answerValue = node.nodes[0].name;
			if(hours != 0 || minutes != 0){
				answerValue = Number(hours) + (Number(minutes) / 60);
			}
			var answer = node.selectedAnswer?node.selectedAnswer:node.nodes[0];

			var newQuestionAsked = {
				idInterview : $scope.interviewId,
				topNodeId : node.topNodeId,
				questionId : node.idNode,
				parentId : $scope.parentQId ? $scope.parentQId : node.parentId,
				name : node.name,
				description : node.description,
				nodeClass : node.nodeclass,
				number : node.number,
				type : node.type,
				link : node.link,
				deleted : 0,
				answers : [ {
					idInterview : $scope.interviewId,
					topQuestionId : node.topNodeId,
					parentQuestionId : node.idNode,
					answerId : answer.idNode,
					name : answer.name,
					description : answer.description,
					nodeClass : answer.nodeclass,
					number : answer.number,
					answerFreetext : answerValue,
					isProcessed:true,
					link : answer.link,
					type : answer.type,
					deleted : 0
				} ]
			}
			var mod = _.find(interview.modules,
					function(val, ind) {
						return val.idNode === node.topNodeId
								&& node.count == val.count;
					});
			if (!mod) {
				mod = _.find(interview.modules, function(val, ind) {
					return val.idNode === node.topNodeId;
				});
			}
			// check if question already exist, do not push, just update the
			// question in database
			var qsIndex = _.indexOf(mod.questionsAsked, _.find(
					mod.questionsAsked, function(qs) {
						return qs.questionId == node.idNode;
					}));
			if (qsIndex == -1) {
				newQuestionAsked.intQuestionSequence = angular.copy($scope.intQuestionSequence);
				mod.questionsAsked.push(newQuestionAsked);
				updateInterviewDisplay(newQuestionAsked);
				$scope.intQuestionSequence ++;
			} else {
				newQuestionAsked.intQuestionSequence = mod.questionsAsked[qsIndex].intQuestionSequence;
				mod.questionsAsked.splice(qsIndex, 1, newQuestionAsked);
				spliceIntDisplay(node.idNode,newQuestionAsked);
			}
			addModuleCount();
			var defer = $q.defer();
			saveAnswer(newQuestionAsked,defer);
			defer.promise.then(function(){
			InterviewsService.saveQuestion(newQuestionAsked).then(
					function(response) {
						if (response.status === 200) {
							syncQs(mod,response.data);
							var actualQuestion = {
								topNodeId : node.topNodeId,
								questionId : node.idNode,
								parentId : answer.idNode,
								number : answer.number,
								link : node.link
							}
							showNextQuestion(actualQuestion, true, false,
									mod.count);
							
						}
					});
			});
		}
		
		function syncQs(mod,respData){
			_.find(mod.questionsAsked,function(qs,ind){
				if(qs.questionId == respData.questionId
						&& qs.modCount == respData.modCount){
					mod.questionsAsked[ind] = respData;
				}
			});
		}

		var intDisplaySequenceUpdate = undefined;
		function deleteIntDisplay(idNode,isModule){
			var intDisplayQ = _.find(
					$scope.intDisplay, function(idisp) {
						if(isModule){
							return idisp.idNode == idNode;
						}else{
							return idisp.questionId == idNode;
						}
					});
			intDisplayQ.deleted = 1;
			intDisplaySequenceUpdate = intDisplayQ.sequence;
			InterviewsService.saveIntDisplay(intDisplayQ).then(
					function(response) {
						if (response.status === 200) {
							$log.info("Success in save interview display.");
							_.remove(
									$scope.intDisplay,
									function(
											val) {
										return val.deleted == 1;
						   });
						}else{
							$log.error("Error in save interview display");
						}
					});
		}
		function removeLinksFromInterviewDisplay(nodeId){
			for(var i=0;i<$scope.intDisplay.length;i++){
				var displayNode = $scope.intDisplay[i];
				if(displayNode.linkNum){
					if(nodeId==displayNode.answerNode){
						displayNode.deleted = 1;
						intDisplaySequenceUpdate = displayNode.sequence;
						InterviewsService.saveIntDisplay(displayNode).then(
								function(response) {
									if (response.status === 200) {
										$log.info("Success in save interview display.");								
									}else{
										$log.error("Error in save interview display");
									}
								});
					}
				}
			}
			var size = $scope.intDisplay.length;
			_.remove($scope.intDisplay,function(val) {
				return val.deleted == 1;
			});
			size = $scope.intDisplay.length;
		}
		function processQuestion(interview, node) {
			var deffered = undefined;
			if ($scope.updateEnable
					&& $scope.previousAnswer.name != $scope.interview.showedQuestion.selectedAnswer.name) {
				var qs = hasQuestionBeenAsked(node);
				deffered = $q.defer();
				if (qs) {
					$scope.intQuestionSequence = qs.intQuestionSequence;
					$scope.parentQId = qs.parentId;
					deleteQuestion([ angular.copy(qs) ], deffered);
					_.remove($scope.interview.modules, function(mod) {
						if(mod.deleted == 1){
							deleteIntDisplay(mod.idNode,true);
							
						}
						return mod.deleted == 1;
					});
					node.selectedAnswer = $scope.interview.showedQuestion.selectedAnswer;
				} else {
					deffered.resolve();
				}
			}
			if (deffered) {
				deffered.promise.then(function() {
					buildAndSaveQuestion(interview, node);
				})
			} else {
				buildAndSaveQuestion(interview, node);
			}
		}

		function buildAndSaveQuestion(interview, node) {
			var answer = node.selectedAnswer;
			var newQuestionAsked = {
				idInterview : $scope.interviewId,
				topNodeId : node.topNodeId,
				questionId : node.idNode,
				parentId : $scope.parentQId ? $scope.parentQId : node.parentId,
				parentAnswerId : node.parentId,
				name : node.name,
				description : node.description,
				nodeClass : node.nodeclass,
				number : node.number,
				type : node.type,
				link : node.link,
				deleted : 0,
				answers : [ {
					idInterview : $scope.interviewId,
					topQuestionId : node.topNodeId,
					parentQuestionId : node.idNode,
					answerId : answer.idNode,
					name : answer.name,
					description : answer.description,
					nodeClass : answer.nodeclass,
					number : answer.number,
					isProcessed:true,
					link : answer.link,
					type : answer.type,
					deleted : 0
				} ]
			}
			if(newQuestionAsked.description){
				if(newQuestionAsked.description=='display'){
					$scope.displayQuestions = [];
					$scope.displayQuestions.push(newQuestionAsked);
					var noteText = '';
					_.each(newQuestionAsked.answers,function(answer){
						noteText = answer.name;
					});
					addInterviewNote(noteText,'Display'); 
				}
			}
			var mod = _.find(interview.modules,function(val, ind) {
						return val.idNode === node.topNodeId
								&& node.count == val.count;
					});
			if (!mod) {
				mod = _.find(interview.modules, function(val, ind) {
					return val.idNode === node.topNodeId;
				});
			}
			// check if question already exist, do not push, just update the
			// question in database
			var qsIndex = _.indexOf(mod.questionsAsked, _.find(mod.questionsAsked, function(qs) {
						return qs.questionId == node.idNode && node.count == mod.count;
					}));
			if (qsIndex == -1) {
				newQuestionAsked.intQuestionSequence = angular.copy($scope.intQuestionSequence);
				mod.questionsAsked.push(newQuestionAsked);
				updateInterviewDisplay(newQuestionAsked);
				$scope.intQuestionSequence ++;
			} else {
				newQuestionAsked.intQuestionSequence = mod.questionsAsked[qsIndex].intQuestionSequence;
				mod.questionsAsked.splice(qsIndex, 1, newQuestionAsked);
				spliceIntDisplay(node.idNode,newQuestionAsked);
			}
			addModuleCount();
			var defer = $q.defer();
			saveAnswer(newQuestionAsked,defer);
			defer.promise.then(function(){
				InterviewsService.saveQuestion(newQuestionAsked).then(function(response) {
					if (response.status === 200) {
						syncQs(mod,response.data);
						var currentNode = {
							topNodeId : node.topNodeId,
							questionId : node.idNode,
							parentId : answer.idNode,
							number : answer.number,
							link : node.link
						}
						showNextQuestion(currentNode, true, false,mod.count);					
					}
				});
			});
		}

		function validateIfAnswerSelected(node) {
			if (node.type == 'Q_multiple') {
				var retValue = false;
				_.find($scope.interview.showedQuestion.nodes, function(val, ind) {
					if (val.isSelected) {
						retValue = true;
					}
					;
				});
				return retValue;
			} else if (node.type == 'Q_frequency') {
				var retValue = false;
				for (var i = 0; i < node.nodes.length; i++) {
					var val = node.nodes[i];
					if (val.type == 'P_frequencyweeks') {
						if (isNaN(val.name)) {
							retValue = false;
						} else {
							retValue = true;
						}
					} else if (val.type == 'P_frequencyseconds') {
						if (isNaN(val.name)) {
							retValue = false;
						} else {
							retValue = true;
						}
					} else if (val.type == 'P_frequencyhours') {
						if (isNaN(val.name)) {
							retValue = false;
						} else {
							retValue = true;
						}
					} else if (val.type == 'P_frequencyshifthours') {
						var retValue = false;
						if ($scope.interview.showedQuestion.hours) {
							retValue = true;
						}
						if ($scope.interview.showedQuestion.minutes) {
							retValue = true;
						}
					} else if (val.type == 'P_frequencyhoursminute') {
						var retValue = false;
						if ($scope.interview.showedQuestion.hours) {
							retValue = true;
						}
						if ($scope.interview.showedQuestion.minutes) {
							retValue = true;
						}
					}
				}
				return retValue;
			} else if (node.type == 'Q_single' || node.type == 'Q_simple') {
				return $scope.interview.showedQuestion.selectedAnswer;
			}
		}

		$scope.saveAnswerQuestion = function(node) {
			if (!(validateIfAnswerSelected(node))) {
				alert("Please select an answer.");
				return;
			}
			$scope.inProgress = true;
			ParticipantsService.findInterviewParticipant($scope.participant.idParticipant).then(function(response) {
				if (response.status === 200) {
					$scope.participant = response.data[0];
					var interview = $scope.interview;
					if (interview) {
						if (node.type == 'Q_multiple') {
							processInterviewQuestionsWithMultipleAnswers(interview, node);						
						} else if (node.type == 'Q_frequency') {
							processFrequency(interview, node);					
						} else {
							processQuestion(interview, node);			
						}
					}else{
						alert("OOPS! Lost the Scope Interview, Please take a screen shot and submit a issue ticket");					
					}				
				}
			});
		}
		$scope.stopInterview = function(node) {
			ParticipantsService.findInterviewParticipant($scope.participant.idParticipant).then(
			  function(response) {
				if (response.status === 200) {
					$scope.participant = response.data[0];
					var interview = $scope.participant.interviews[0];
					if (validateIfAnswerSelected(node)) {
						if (!interview) {
							return null;
						}
						if (node.type == 'Q_multiple') {
							processInterviewQuestionsWithMultipleAnswers(interview, node);
						} else if (node.type == 'Q_frequency') {
							processFrequency(interview, node);
						} else {
							processQuestion(interview, node);
						}
					}
					if (confirm('Stop Interview?')) {
						addInterviewNote('Interview Stopped','System');					
						var participant = $scope.participant;
						participant.status = 1;//partial
						saveParticipant(participant);
						endInterview();
					}
				}
			});
		}
		function addInterviewNote(note,type){
			var interview = $scope.interview;
			if (!(interview.notes)) {
				interview.notes = [];
			}
			interview.notes.push({
						interviewId : interview.interviewId,
						text : note,
						type : type
					});
			saveInterview(interview);
		}
		function saveInterview(interview) {
			InterviewsService.save(interview).then(
					function(response) {
						if (response.status === 200) {
							$log.info("Saving interview with id:"
									+ interview.interviewId + " successful");
						}
					});
		}
		function saveParticipant(participant) {
			participant.interviews = [];
			participant.interviews.push($scope.interview);
			ParticipantsService.save(participant).then(
					function(response) {
						if (response.status === 200) {
							$log.info("Saving Participant "
									+ participant.idParticipant
									+ " was successful");
						}
					});
		}

		function addModuleCount(){
			_.each($scope.interview.modules,function(mod,ind){
				_.each(mod.questionsAsked,function(qs,index){
					qs.modCount = mod.count;
					addModuleCountAnswers(qs.answers,mod.count);
				});
			});
		}
		
		function addModuleCountAnswers(answers,count){
			_.each(answers,function(as,index){
				as.modCount = count;
			});
		}
		
		function createParticipant(data) {
			var participant = {
				reference : $scope.referenceNumber,
				interviews : []
			}
			ParticipantsService.createParticipant(participant).then(function(response) {
				if (response.status === 200) {
					participant = response.data;
					$scope.participant = participant;
					var interview = {};
					interview.participant = participant;
					interview.module = $scope.data[0];
					interview.referenceNumber = $scope.referenceNumber;					
					InterviewsService.startInterview(interview).then(function(response) {
						if (response.status === 200) {
							interview.interviewId = response.data.interviewId;
							interview.modules = [];
							var jsonMod = {
									idInterview : $scope.interviewId,
									name : interview.module.name,
									idNode : interview.module.idNode,
									topNodeId : 0,
									parentNode : 0,
									answerNode : 0,
									parentAnswerId : 0,
									number : 0,
									count : 1,
									linkNum: 0,
									sequence: ++sequence,
									deleted : 0,
									questionsAsked : []
								};
							interview.modules.push(jsonMod);
							var copyParticipant = angular.copy($scope.participant);
							interview.participant = copyParticipant;
							$scope.interview = {};
							$scope.interview = interview;																	
							$scope.interview.interviewStarted = true;
							$scope.interviewId = response.data.interviewId;
							updateInterviewDisplay(jsonMod);
							InterviewsService.saveInterviewMod(interview.modules[0]).then(
								function(response) {
									if (response.status === 200) {
										$log.info("Success in saveInterviewMod");
									}else{
										$log.error("Error in saveInterviewMod");
									}
							});
							var startingNode = {
									parentId : interview.module.idNode,
									number : '0'
								}
							var isAnswer=false; //this flag is for when the passed in node is an answer
							var statusRequired=false;//this flag is for when the call needs to wait for a status back
							var count=1;
							showNextQuestion(startingNode,isAnswer,statusRequired,1);
						}
					});
				}
			});
		}
		
		function validReferenceNumber(referenceNumber) {
			var retValue = false;
			if (referenceNumber) {
				if (referenceNumber.substr(0, 1) == 'H') {
					if (referenceNumber.length == 7) {
						$scope.referenceNumber = referenceNumber;
						retValue = true;
					}
				}
			}
			return retValue;
		}
		function checkReferenceNumberExists(referenceNumber) {
			return InterviewsService
					.checkReferenceNumberExists(referenceNumber).then(
							function(response) {
								if (response.status === 200) {
									return response.data
								} else if (response.status === 401) {
									return;
								}
							});
		}
		$scope.scrollWithTimeout = function(elId) {
			$timeout(function() {
				$scope.scrollTo(elId);
			}, 500);
		}

		resetSelectedIndex();

		$scope.singleChoiceHandler = function($index) {
			$scope.selectedIndex = $index;
		}
		$scope.multipleChoiceHandler = function(parentId, id) {
			var el = angular.element(document.querySelector("#interviewing-"
					+ parentId + " #answer-" + id));
			if (el.hasClass('selected')) {
				el.removeClass('selected');
			} else {
				el.addClass('selected');
			}
		}
		function resetSelectedIndex() {
			$scope.selectedIndex = -1;
		}

		$scope.scrollTo = function(target) {
			var scrollPane = $('#interview-question-list');

			var scrollTarget = $('#questionlist-' + target);
			if (scrollTarget) {
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

		$scope.multiToggle = function(item, list) {
			var idx = list.indexOf(item);
			if (idx > -1)
				list.splice(idx, 1);
			else
				list.push(item);
		};
		$scope.multiExists = function(item, list) {
			return list.indexOf(item) > -1;
		};
		$scope.debug = function(element) {
			var scrollPane = $('#interview-intromodule-tree');
			// var top = element.$element.context.offsetTop;
			// alert(top);
			// scrollPane.animate({scrollTop: (top-150)}, 2000, 'swing');
			var jQObject = $('#interviewnode-' + element.$modelValue.idNode);
			var top = jQObject.offset().top;
			alert(top);
			var currentScroll = scrollPane.scrollTop();
			alert(currentScroll);
			scrollPane.animate({
				scrollTop : (top - 150 + currentScroll)
			}, 2000, 'swing');

		}
		$scope.getWeeksArray = function() {
			var weeks = [];
			for (var i = 0; i < 53; i++) {
				weeks.push(i);
			}
			return weeks;
		};
		$scope.getShiftHoursArray = function() {
			var hours = [];
			for (var i = 0; i < 25; i++) {
				hours.push(i);
			}
			return hours;
		};
		$scope.getSecondsArray = function() {
			var seconds = [ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 15, 20, 25, 30,
					35, 40, 45, 50, 55, 60 ];

			return seconds;
		};
		$scope.getHoursPerWeekArray = function() {
			var hours = [];
			for (var i = 0; i < 169; i++) {
				hours.push(i);
			}
			return hours;
		};
		$scope.getShiftMinutesArray = function() {
			var minutes = [ 0, 1, 2, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55 ];

			return minutes;
		};
		$scope.questionheader = {};

		function showNextQuestion(currentNode, isAnswer, statusRequired,count) {
			var defer;
			if (statusRequired) {
				defer = $q.defer();
			}
			var currentNodeTemp = currentNode;
			var tempCount = count;
			if (isAnswer) {//if currentNode is answer then set its parentid (which is a questionid) to use later if needed
				$scope.parentQId = currentNodeTemp.questionId;
			} else {
				$scope.parentQId = undefined;
			}
			return InterviewsService.getNextQuestion(currentNode).then(function(response) {
					if (response.status === 200) {
						var question = response.data;
						question.count = tempCount;
						resetSelectedIndex();
						if (question.link == 0) {//not a linking q
							$scope.inProgress = false; //so stop the progress bar we should find this guy fast
							safeDigest($scope.inProgress);  //lets make sure that signal gets through we don't want never ending progress bars
							//find this questions module
							var currentModules = $scope.interview.modules;
							var mod = _.find(currentModules,function(val, ind) {
										return val.idNode == question.topNodeId;
									});
							if(!mod){
								$log.error("No parent module found for "+question.idNode);
							}else{
								if(hasQuestionBeenAsked(question)){
									determineNextUnansweredQuestion(question);
								}else{
									$scope.questionheader.name = mod.name.substring(0, 4);
									if ($scope.interview.showedQuestion) {
										$scope.scrollTo($scope.interview.showedQuestion.idNode);
									}
									if (question.type == 'Q_frequency') { //if frequency set up frequency lists
										$scope.hoursPerWeekArray = $scope.getHoursPerWeekArray();
										$scope.hoursArray = $scope.getShiftHoursArray();
										$scope.minutesArray = $scope.getShiftMinutesArray();
										$scope.weeks = $scope.getWeeksArray();
										$scope.seconds = $scope.getSecondsArray();
									}
									$scope.interview.showedQuestion = question;								
									safeDigest($scope.interview.showedQuestion);
								}							
							}	
						}else if (question.link !== 0) {
							return processLinkingQuestion(question,currentNodeTemp);
						}
						if (statusRequired) {
							defer.resolve(response.status);
							return defer.promise;
						}
					} else if (response.status == 204) { //could not find that question
						if (statusRequired) {
							defer.resolve(response.status);
							return defer.promise;
						}
						var mod = _.find($scope.interview.modules,function(val, ind) {
											return val.idNode == currentNodeTemp.topNodeId && val.count == tempCount;
										});
						if (!mod) {
							mod = _.find($scope.interview.modules,function(val, ind) {
												return val.idNode == currentNodeTemp.topNodeId;
											});
						}
						var results = _.find(mod.questionsAsked,function(val, index) {
											return val.questionId == currentNodeTemp.questionId;
										});
	
						var multiAnswer = results ? _.find(results.answers,function(val, ind) {
											return val.isProcessed == false;
										})
								: undefined;
	
						if (multiAnswer) {
							multiAnswer.isProcessed = true;
							saveAnswer(results);
							var answer = multiAnswer;
	
							var actualQuestion = {
								topNodeId : results.topNodeId,
								questionId : results.questionId,
								parentId : answer.answerId,
								number : answer.number
							}
							showNextQuestion(actualQuestion, true,
									false, results.modCount);
						} else if (results) {
							var parent_id = 0;
							if (results.link != 0) {
								parent_id = results.link;
							} else if (results.parentAnswerId) {
								parent_id = results.parentAnswerId;
							} else {
								parent_id = results.parentId;
							}
							var actualQuestion = {
								topNodeId : results.topNodeId,
								questionId : results.parentId,
								parentId : parent_id,
								number : results.number,
								link : results.link
							}
							var isParentAnAnswer = false;
							if(results.parentId!=results.topNodeId){
								isParentAnAnswer = true;
							}
							showNextQuestion(actualQuestion, isParentAnAnswer,
									false, results.modCount);
						} else if (mod.parentNode) {
							verifyQuestionInParentModule(mod);
						} else {
							AssessmentsService
									.updateFiredRules(
											$scope.interviewId)
									.then(
											function(response) {
												if (response.status === 200) {
													console
															.log('Updated Fired Rules');
												}
											});
							endInterview();
						}
					} else {
						console.log('ERROR on Get!');
					}
					angular.element('#numId').focus();
	
				});
		}

		function hasQuestionBeenAsked(node) {
			if(!node.idNode){
				node.idNode = node.questionId;
			}
			var mod = _.find($scope.interview.modules,function(val, ind) {
						return ((val.idNode == node.topNodeId) && (node.count == val.count));//if we can find it with a count use that first
					});
			if (!mod) {
				mod = _.find($scope.interview.modules,function(val, ind) {
							return val.idNode == node.topNodeId;//find the module but don't worry about the count
						});
			}
			var results;
			if(mod){//dont set results if we cant even find the module must be an unasked question
				results = _.find(mod.questionsAsked, function(val, index) {//now that we found the module see if its in the list
					return val.questionId == node.idNode;
				});
			}
			return results;
		}

		var sequence = 0;
		function processLinkingQuestion(question, actualQuestionTemp) {
			// check if link is a child
			var parentMod = _.find($scope.interview.modules,function(mod,ind){
				return mod.idNode == actualQuestionTemp.parentId;
			});
			
			var modDetail = {
				idInterview:$scope.interviewId,
				name : question.name,
				idNode : question.link,
				topNodeId : question.topNodeId,
				parentNode : actualQuestionTemp.link ? actualQuestionTemp.link
						: actualQuestionTemp.questionId,
				answerNode : actualQuestionTemp.parentId,
				parentAnswerId : question.parentId,
				number : question.number,
				count : 1,
				sequence: ++sequence,
				deleted : 0,
				linkNum:parentMod?parentMod.number:question.number,
				questionsAsked : []
			};
			var mdIndex = _
					.indexOf(
							$scope.interview.modules,
							_
									.find(
											$scope.interview.modules,
											function(val) {
												return (val.answerNode == question.parentId 
														&& val.idNode == question.link
														&& val.count == question.count);
											}));
			if (mdIndex == -1) {
				var modules = _.filter($scope.interview.modules,
						function(val) {
							return val.idNode == modDetail.idNode;
						});
				if (modules) {
					modDetail.count = modDetail.count + modules.length;
				}
				$scope.interview.modules.push(modDetail);
				updateInterviewDisplay(modDetail);
				InterviewsService.saveInterviewMod(modDetail).then(
						function(response) {
							if (response.status === 200) {
								$log.info("Success in saveInterviewMod");
							}else{
								$log.error("Error in saveInterviewMod");
							}
						});
			}
			var num = 0;
			_.find($scope.interview.modules, function(val) {
				if (val.idNode == question.link
						&& val.parentAnswerId == question.parentId) {
					if (val.questionsAsked.length > 0) {
						num = val.questionsAsked.slice(-1)[0].number;
					}
				}
			});
			var actualQuestion = {
				topNodeId : question.topNodeId,
				questionId : question.idNode,
				parentId : question.link,
				link : actualQuestionTemp.link,
				number : num
			}
			showNextQuestion(actualQuestion, false, false, modDetail.count);
		}

		function verifyQuestionInParentModule(mod) {
			var tempMod = angular.copy(mod);
			var mod = _
					.find(
							$scope.interview.modules,
							function(val, ind) {
								return (val.idNode === tempMod.topNodeId || val.topNodeId == tempMod.topNodeId)
										&& val.count == tempMod.count;
							});
			var question = _.find(mod.questionsAsked, function(val, index) {
				return val.questionId === tempMod.parentNode;
			});

			var results;
			if (question) {
				results = _.find(question.answers, function(val, index) {
					return val.answerId === tempMod.answerNode;
				});
			}
			if (!question) {
				var actualQuestion = {
					topNodeId : mod.topNodeId,
					questionId : mod.answerNode,
					parentId : mod.idNode,
					number : tempMod.number
				}
				var status = showNextQuestion(actualQuestion, null, true,
						mod.count);
				if (status) {
					status
							.then(function(data) {
								if (data == 204) {
									if (mod.idNode == $scope.interview.modules[0].idNode) {
										return endInterview();
									}

									var modParent = _
											.find(
													$scope.interview.modules,
													function(val, ind) {
														return val.idNode === mod.topNodeId;
													});
									var modQuestion = modParent.questionsAsked
											.slice(-1)[0];
									var actualQuestion = {
										topNodeId : modQuestion.topNodeId,
										questionId : modQuestion.questionId,
										parentId : modQuestion.link > 0 ? modQuestion.link
												: modQuestion.parentId,
										number : modQuestion.number
									}
									return showNextQuestion(actualQuestion,
											null, false, mod.count);
								}
							});
				}
				return;
			}
			if (results) {
				var actualQuestion = {
					topNodeId : results.topQuestionId,
					questionId : results.parentQuestionId,
					parentId : results.answerId,
					number : tempMod.number
				}
				return showNextQuestion(actualQuestion, true, false, mod.count);
			} else {
				var actualQuestion = {
					topNodeId : question.topNodeId,
					questionId : question.questionId,
					parentId : question.parentId,
					number : question.number
				}
				return showNextQuestion(actualQuestion, false, false, mod.count);
			}
		}

		function endInterview() {
			$scope.inProgress = false;
			safeDigest($scope.inProgress);
			$scope.interview.interviewStarted = false;
			$scope.interview.interviewEnded = true;
			$scope.updateEnable = false;
			
		}
		$scope.finishInterview = function(){
			var interview = $scope.interview;
			if (!(interview.notes)) {
				interview.notes = [];
			}
			interview.notes
					.push({
						interviewId : interview.interviewId,
						text : interview.endNotes,
						type : 'Interviewer'
					});
			saveInterview(interview);
			var participant = $scope.participant;
			participant.status = 2;//completed
			saveParticipant(participant);
			$scope.closeAndSwitchToParticipantsTab();
			
		}
		function convertToTree() {
			var root = {
				id : 0,
				parent_id : null,
				type : 'I',
				name : $scope.interview.referenceNumber,
				number : 'Interview',
				nodes : []
			};
			var node_list = {
				0 : root
			};
			var table = [];
			_.each($scope.interview.modules, function(module) {
				var parent_id = null;
				if (module.parentAnswerId) {
					parent_id = module.parentAnswerId;
				} else if (module.parentNode) {
					parent_id = module.parentNode;
				} else {
					parent_id = 0;
				}
				var number = null;
				if (module.name.length > 4) {
					number = module.name.substr(0, 4);
				} else {
					number = module.name;
				}
				table.push({
					id : module.idNode,
					type : 'M',
					parent_id : parent_id,
					name : module.name,
					number : number,
					nodes : []
				});
				_.each(module.questionsAsked, function(qa) {
					var multi = null;
					if (qa.type.indexOf('multi') > -1) {
						multi = true;
					}
					table.push({
						id : qa.questionId,
						parent_id : qa.parentId,
						type : 'Q',
						number : qa.number,
						name : qa.name,
						multi : multi,
						parentAnswerId : qa.parentAnswerId,// edit required
															// field
						parentId : qa.parentId,// edit required field
						topeNodeId : qa.topeNodeId,// edit required field
						questionId : qa.questionId,// edit required field
						nodes : []
					});
					_.each(qa.answers, function(answer) {
						table.push({
							id : answer.answerId,
							parent_id : answer.parentQuestionId,
							type : 'P',
							number : answer.number,
							name : answer.name,
							nodes : []
						});
					});
				});
			});
			for (var i = 0; i < table.length; i++) {
				node_list[table[i].id] = table[i];
				if (!(table[i].parent_id)) {
					table[i].parent_id = 0;
				}
				if (node_list[table[i].parent_id]) {
					node_list[table[i].parent_id].nodes
							.push(node_list[table[i].id]);
				}
			}
			$scope.treeView = {
				0 : node_list[0]
			};
		}
		$scope.showTree = false;
		$scope.showList = true;
		$scope.viewStatus = "List View";
		$scope.toggleTreeList = function() {
			if (!$scope.showTree) {
				convertToTree();
				$scope.showTree = true;
				$scope.showList = false;
				$scope.viewStatus = "Tree View";
			} else {
				$scope.showTree = false;
				$scope.showList = true;
				$scope.viewStatus = "List View";
			}
		}
		$scope.toggleCollapse = function(scope) {
			scope.toggle();
		}
		$scope.cancel = function() {
			$mdDialog.cancel();
		};
		$scope.saveNewNote = function(data) {
			saveNewNote(data);
		}
		function saveNewNote(result) {
			var noteText = result;
			var interview = $scope.interview;
			if (!(interview.notes)) {
				interview.notes = [];
			}
			interview.notes.push({
				interviewId : $scope.interviewId,
				text : noteText,
				type : 'Interviewer'
			});
			saveInterview(interview);
			$mdDialog.cancel();
		}
		$scope.showNotePrompt = function(ev) {
			$mdDialog.show({
				// scope: $scope,
				scope : $scope.$new(),
				templateUrl : 'scripts/interviews/view/noteDialog.html'
			});
		};
//		"\n".charCodeAt(0)
		$scope.orderByNumber = function(questionAsked) {
			   return questionAsked.number.charCodeAt();
		};
		$scope.intDisplay = [];
		var sequenceDisplay = 0;
		function updateInterviewDisplay(element){
			//push element to interview display, can be a module/question
			var copyEl = angular.copy(element);
			copyEl.type=element.questionsAsked?"M":"Q";
			if(!element.count){
				addModuleCount();
			}
			copyEl.count = element.count?element.count:element.modCount;
			copyEl.idInterview = $scope.interviewId;
			if(!intDisplaySequenceUpdate){
			copyEl.sequence = sequenceDisplay;
			sequenceDisplay++;
			}else{
				copyEl.sequence = angular.copy(intDisplaySequenceUpdate);
				intDisplaySequenceUpdate = undefined;
			}
			InterviewsService.saveIntDisplay(copyEl).then(
					function(response) {
						if (response.status === 200) {
							copyEl.id = response.data.id;
							$scope.intDisplay.push(copyEl);
							$log.info("Success in save interview display.");
						}else{
							$log.error("Error in save interview display");
						}
					});
		}
    	$rootScope.removeTab = function(tab) {
    		addInterviewNote('Interview Closed','System');
    		var participant = $scope.participant;
    		participant.status = 1;//partial
    		saveParticipant(participant);
    		
    		$rootScope.removeTabFromOverride();
    	};
	}
})();