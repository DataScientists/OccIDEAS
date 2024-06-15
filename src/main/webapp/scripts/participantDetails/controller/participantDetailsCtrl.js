(function() {
	angular.module('occIDEASApp.ParticipantDetails')
		.controller('ParticipantDetailsCtrl', ParticipantDetailsCtrl);

	ParticipantDetailsCtrl.$inject = ['ParticipantDetailsService', 'ParticipantsService', 'InterviewsService', 'QuestionsService',
		'data', 'updateData', 'startWithReferenceNumber', 'mapping',
		'$state', '$scope', '$filter', '$rootScope', '$mdDialog', 'ngToast', '$sessionStorage', '$q'];

	function ParticipantDetailsCtrl(ParticipantDetailsService, ParticipantsService, InterviewsService, QuestionsService,
		data, updateData, startWithReferenceNumber, mapping,
		$state, $scope, $filter, $rootScope, $mdDialog, $ngToast, $sessionStorage, $q) {
		var self = this;

		self.addJobHistoryParticipant = addJobHistoryParticipant;
		self.saveParticipantDetails = saveParticipantDetails;
		self.isZeroRecord = false;
		self.isMapping = false;
		self.charCode = "----";

		$scope.jobModules = ['AsMM',
			'AREM',
			'ANEC',
			'AUTO',
			'CEMT',
			'FURN',
			'INSU',
			'LAND',
			'TEXT',
			'TIPW',
			'TRAD',
			'WATE',
			'JEWL',
			'LAUN',
			'UnExposed'];

		if (startWithReferenceNumber) {
			checkIsFirstJob(startWithReferenceNumber);
			//getOtherParticipantJob(self.referenceNumberPrefix);
			$scope.referenceNumber = startWithReferenceNumber;
			$rootScope.referenceNumber = startWithReferenceNumber;
			self.currentReferenceNumber = startWithReferenceNumber;
			$scope.introModule = data[0];
			InterviewsService.checkReferenceNumberExists($scope.referenceNumber).then(function(data) {
				if (data.status == 200) {
					$scope.interview = data.data[0];
					var participantId = $scope.interview.interviewId;
					populateParticipantDetails(participantId);


				} else if (data.status == 204) {
					$scope.startInterview($rootScope.referenceNumber);
				} else {
					var msg = "Error occured during checkReferenceNumberExists.";
					ngToast.create({
						className: 'danger',
						content: msg,
						animation: 'slide'
					});
				}
			})

		} else if (updateData) {

			//updateData.participant = $scope.data;
			$scope.interview = updateData;
			//$rootScope.participant = updateData.participant;
			//$scope.participant = updateData;
			//resumeInterview();
			checkIsFirstJob($scope.interview.referenceNumber);
			getOtherParticipantJob(self.referenceNumberPrefix);
			self.currentReferenceNumber = $scope.interview.referenceNumber;
			populateParticipantDetails($scope.interview.interviewId);
		} else if (mapping) {
			self.isMapping = true;
			console.log("mapping");
			$scope.interview = mapping;
			$rootScope.referenceNumber = $scope.interview.referenceNumber;
			checkIsFirstJob($rootScope.referenceNumber);
			getOtherParticipantJob(self.referenceNumberPrefix);
			getFullSurveyLink($scope.interview);

		}

		$scope.$root.tabsLoading = false;
		$scope.referenceNumber = null;
		$scope.storage = $sessionStorage;

		$scope.startInterview = function(referenceNumber) {
			createParticipant(referenceNumber);
		};
		function createParticipant(referenceNumber) {
			var status = 0;
			if (self.isZeroRecord) {
				status = 1;
			}
			var participant = {
				reference: referenceNumber,
				status: status,
				interviews: []
			};

			ParticipantsService.createParticipant(participant).then(function(response) {
				if (response.status === 200) {
					participant = response.data;
					$rootScope.participant = participant;
					var interview = {};
					interview.participant = participant;
					interview.module = $scope.introModule;
					interview.referenceNumber = $rootScope.referenceNumber;
					if (self.isZeroRecord) {
						interview.notes = [];
						interview.notes.push({
							interviewId: $rootScope.participant.idParticipant,
							text: findQualtricsSurveyLink('NONO'),
							type: 'AMRSurveyLink'
						});
					}
					InterviewsService.startInterview(interview).then(function(response) {
						if (response.status === 200) {
							interview.interviewId = response.data.interviewId;

							var copyParticipant = angular.copy($scope.participant);
							interview.participant = copyParticipant;
							$scope.interview = {};
							$scope.interview = interview;
							$scope.interviewStarted = true;
							$scope.interviewId = response.data.interviewId;

							var firstLinkIntervewQuestion = populateInteviewQuestionJsonByModule(interview, $scope.introModule);
							firstLinkIntervewQuestion.number = "0";
							firstLinkIntervewQuestion.isProcessed = true;
							//inserting the first link intro module
							InterviewsService.saveLinkQuestionAndQueueQuestions(firstLinkIntervewQuestion).then(function(response) {
								if (response.status === 200) {
									InterviewsService.get($scope.interview.interviewId).then(function(response) {
										if (response.status == 200) {

											$scope.interview = response.data[0];

											var question = findNextQuestionQueued($scope.interview);
											if (question) {
												QuestionsService.findQuestionSingleChildLevel(question.questionId).then(function(response) {
													if (response.status === 200) {
														var ques = response.data[0];

														checkIsFirstJob(participant.reference);

														if (self.isZeroRecord) {
															var actualAnswer = ques.nodes.find(possibleAnswer => possibleAnswer.name === 'NONO');
															var interviewQuestion = $scope.interview.questionHistory[1];
															actualAnswer.interviewQuestionId = interviewQuestion.id;
															actualAnswer.lastUpdated = getCurrentDateTime();
															actualAnswer.interviewQuestionId = question.id;
															actualAnswer.parentQuestionId = ques.idNode;
															actualAnswer.answerId = actualAnswer.idNode;
															actualAnswer.isProcessed = true;
															actualAnswer.idInterview = $scope.interview.interviewId;
															actualAnswer.nodeClass = "P";
															actualAnswer.answerFreetext = "NONO";
															interviewQuestion.answers.push(actualAnswer);
															InterviewsService.saveAnswersAndQueueQuestions(interviewQuestion.answers).then(function(response) {
																if (response.status === 200) {
																	console.log("Job module saved as first answer");
																	var questions = [];
																	interviewQuestion.processed = true;
																	questions.push(interviewQuestion);
																	InterviewsService.saveQuestions(questions).then(function(response) {
																		if (response.status == 200) {
																			console.log("Updated question to be processed");

																			self.selectedJobModule = "NONO";
																		}
																	});

																} else {
																	console.log("Could not save first question as module");
																}
															});
														}
														if (self.isZeroRecord) {

															$rootScope.participant.participantDetails = [];
															var charCode = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'CharCode',
																detailValue: '----'
															}
															var numberCode = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'NumberCode',
																detailValue: '----'
															}
															var detailComment = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'Comments',
																detailValue: '----'
															}
															var priority = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'Priority',
																detailValue: '1'
															}
															$rootScope.participant.participantDetails.push(priority);
															$rootScope.participant.participantDetails.push(charCode);
															$rootScope.participant.participantDetails.push(numberCode);
															$rootScope.participant.participantDetails.push(detailComment);
														} else {
															$rootScope.participant.participantDetails = [];
															var title = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'Title',
																detailValue: '----'
															}
															var product = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'Product',
																detailValue: '----'
															}
															var priority = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'Priority',
																detailValue: '-'
															}
															$rootScope.participant.participantDetails.push(priority);
															$rootScope.participant.participantDetails.push(title);
															$rootScope.participant.participantDetails.push(product);


														}
														ParticipantsService.save(participant).then(function(response) {
															if (response.status === 200) {
																console.log('it works');
															}
														});

													} else {
														var errorMsg = "Error getting question " + question.questionId;
														console.error(errorMsg);
														ngToast.create({
															className: 'danger',
															content: errorMsg,
															animation: 'slide'
														});
													}
												});
											} else {
												var msg = "Ending interview";
												ngToast.create({
													className: 'danger',
													content: msg,
													animation: 'slide'
												});
												console.log(msg);
												endInterview();
											}
										}
									});
								}
							});
						}
					});
				}
			});
		}

		function populateInteviewQuestionJsonByModule(interview, module) {
			return {
				idInterview: interview.interviewId,
				topNodeId: module.idNode,
				questionId: undefined,
				parentModuleId: module.idNode,
				parentAnswerId: undefined,
				name: module.name,
				description: module.description,
				nodeClass: module.nodeclass,
				number: module.number,
				type: module.type,
				link: module.idNode,
				modCount: 1,
				intQuestionSequence: 1,
				deleted: 0,
				answers: []
			};
		}
		function findNextQuestionQueued(interview) {
			var questionsList = angular.copy(interview.questionHistory);
			questionsList = _.reverse(questionsList);
			var question = _.find(questionsList, function(queuedQuestion) {
				return !queuedQuestion.processed && !queuedQuestion.deleted;
			});
			return question;
		}
		function splitAndIncrementLast(string) {
			// Split the string on the "-" character
			let array = string.split("-");

			// Convert the last element of the array to an integer and increment by 1
			let lastElement = parseInt(array[array.length - 1]) + 1;

			// Update the last element in the array
			array[array.length - 1] = String(lastElement);

			// Join the array elements back into a string with "-"
			let result = array.join("-");

			return result;
		}
		function checkIsFirstJob(string) {
			if (string !== undefined) {
				let array = string.split("-");

				// Convert the last element of the array to an integer and increment by 1
				let lastElement = parseInt(array[array.length - 1]);

				self.jobNumber = "J" + lastElement;
				self.referenceNumberPrefix = array[0];
				if (lastElement > 0) {
					self.isZeroRecord = false;
				} else {
					self.isZeroRecord = true;
				}
			}
		}

		function addJobHistoryParticipant() {

			var theReferenceNumber = "";
			if ($scope.referenceNumber) {
				theReferenceNumber = $scope.referenceNumber
			} else if ($scope.interview.referenceNumber) {
				theReferenceNumber = $scope.interview.referenceNumber;
			}
			var nextJobHistoryReferenceNumber = splitAndIncrementLast(theReferenceNumber);
			$scope.referenceNumber = nextJobHistoryReferenceNumber;

			$scope.addParticipantDetailsTab(-1, $scope.referenceNumber, false, $scope.interview.interviewId);
			//$scope.startInterview(data);

		}

		function saveParticipantDetails() {
			var participant = $rootScope.participant;
			ParticipantsService.findParticipant(participant.idParticipant).then(function(response) {
				if (response.status === 200) {
					var participant = response.data[0];

					var theDetails = participant.participantDetails;
					if (self.isZeroRecord) {
						var detail = theDetails.find(detail => detail.detailName === 'CharCode');
						detail.detailValue = self.charCode;
						detail = theDetails.find(detail => detail.detailName === 'Comments');
						detail.detailValue = self.comments;
					} else {
						if (theDetails.length > 0) {
							var detail = theDetails.find(detail => detail.detailName === 'Title');
							detail.detailValue = self.jobTitle;
							detail = theDetails.find(detail => detail.detailName === 'Product');
							detail.detailValue = self.product;
						} else {
							//todo
							console.log("no details yet");
						}

					}
					participant.participantDetails = theDetails;
					ParticipantsService.save(participant).then(function(response) {
						if (response.status === 200) {
							console.log('it works');
						}
					});

				} else {
					console.error("Inside data of tabs.interviewresume tabs.js could not findParticipant with " + $stateParams.row);

				}

			});


		}
		function getCurrentDateTime() {
			let now = new Date();

			let day = String(now.getDate()).padStart(2, '0');
			let month = String(now.getMonth() + 1).padStart(2, '0'); // Months are zero-based
			let year = now.getFullYear();

			let hours = String(now.getHours()).padStart(2, '0');
			let minutes = String(now.getMinutes()).padStart(2, '0');
			let seconds = String(now.getSeconds()).padStart(2, '0');

			return `${day}/${month}/${year} ${hours}:${minutes}:${seconds}`;
		}

		function findQualtricsSurveyLink(moduleName) {
			var qualtricsSurveyLink = { surveyLink: "" };
			if (moduleName != undefined) {
				qualtricsSurveyLink = $rootScope.qualtricsSurveyLinks.find(qualtricsSurveyLink => qualtricsSurveyLink.name === moduleName);

			}
			return qualtricsSurveyLink.surveyLink;
		}
		function getFullSurveyLink(participant) {
			var fullQualtricsLink = "";
			if (participant.notes != undefined) {
				surveyLink = participant.notes.find(qualtricsSurveyLinkNote => qualtricsSurveyLinkNote.type === 'AMRSurveyLink');
				if (surveyLink != undefined) {
					fullQualtricsLink = 'https://curtin.au1.qualtrics.com/jfe/form/' + surveyLink.text + '?AMRID=' + $scope.referenceNumber;
					var jobModuleName = $rootScope.qualtricsSurveyLinks.find(jobModuleName => jobModuleName.surveyLink === surveyLink.text);
					self.selectedJobModule = jobModuleName.name;
				}

			}
			self.fullQualtricsLink = fullQualtricsLink;
			return fullQualtricsLink;
		}
		function populateParticipantDetails(participantId) {
			ParticipantsService.findParticipant(participantId).then(function(response) {
				if (response.status === 200) {
					var participant = response.data[0];
					$rootScope.participant = participant;
					var theDetails = participant.participantDetails;
					if (self.isZeroRecord) {
						var detail = theDetails.find(detail => detail.detailName === 'CharCode');
						self.charCode = detail.detailValue;
						detail = theDetails.find(detail => detail.detailName === 'Comments');
						self.comments = detail.detailValue;
					} else {
						if (theDetails.length > 0) {
							var detail = theDetails.find(detail => detail.detailName === 'Title');
							self.jobTitle = detail.detailValue;
							detail = theDetails.find(detail => detail.detailName === 'Product');
							self.product = detail.detailValue;
						} else {
							//todo
							console.log("no details yet");
						}

					}
					getFullSurveyLink(participant);

				}
			});
		}
		function getOtherParticipantJob(referenceNumberPrefix) {
			ParticipantsService.getByReferenceNumberPrefix(referenceNumberPrefix).then(function(response) {
				if (response.status === 200) {
					var otherParticipantJobs = response.data;
					self.otherParticipantJobs = otherParticipantJobs;

					populateMappingDetails(self.otherParticipantJobs);



				}
			});
		}

		function saveParticipantMapping() {
			var mappedJobModule = self.selectedJobModule;
			if (!($scope.interview.notes)) {
				$scope.interview.notes = [];
			} else {
				deleteOldSurveyLink($scope.interview);
			}
			$scope.interview.notes.push({
				interviewId: $scope.interview.interviewId,
				text: findQualtricsSurveyLink(mappedJobModule),
				type: 'AMRSurveyLink'
			});

			InterviewsService.save($scope.interview).then(function(response) {
				if (response.status === 200) {
					console.log('it works');
				}
			});
		}
		self.saveParticipantMapping = saveParticipantMapping;
		function allMapped(participants) {

			for (let participant of participants) {
				// Check if the note type is 'surveyLink' and it is not marked as deleted
				if (!participant.mappedTo) {
					// Return the found note
					return false;
				}
			}
			return true;
		}
		function saveParticipantMappings(participants) {
			if (allMapped(participants)) {
				participants.reduce(function(p, data) {
					return p.then(function() {


						InterviewsService.get(data.idParticipant).then(function(response) {
							if (response.status === 200) {
								var interview = response.data[0];
								if (!(interview.notes)) {
									interview.notes = [];
								} else {
									deleteOldSurveyLink(interview);
								}
								interview.notes.push({
									interviewId: interview.interviewId,
									text: findQualtricsSurveyLink(data.mappedTo),
									type: 'AMRSurveyLink'
								});

								InterviewsService.save(interview).then(function(response) {
									if (response.status === 200) {
										console.log('it works');
									}
								});


								//data.mappedTo = getJobModule(data);


							}
						});
						var status = 0;
						if (data.mappedTo === 'UnExposed') {
							status = 4;
						} else if (data.mappedTo != '') {
							status = 1;
						}
						data.status = status;
						ParticipantsService.save(data).then(function(response) {
							if (response.status === 200) {
								console.log('it works');
							}
						});

					});
				}, $q.when(true)).then(function(finalResult) {
					console.log('finish loading rules');

				}, function(err) {
					console.log('error');
				});




				var mappedJobModule = self.selectedJobModule;
				if (!($scope.interview.notes)) {
					$scope.interview.notes = [];
				} else {
					deleteOldSurveyLink($scope.interview);
				}
				$scope.interview.notes.push({
					interviewId: $scope.interview.interviewId,
					text: findQualtricsSurveyLink(mappedJobModule),
					type: 'AMRSurveyLink'
				});

				InterviewsService.save($scope.interview).then(function(response) {
					if (response.status === 200) {
						console.log('it works');
					}
				});
			} else {
				$ngToast.create({
					className: 'danger',
					content: 'One or more Jobs are not mapped.',
					dismissButton: true,
					dismissOnClick: false,
					animation: 'slide'
				});
			}

		}
		self.saveParticipantMappings = saveParticipantMappings;
		function deleteOldSurveyLink(interview) {
			if (Array.isArray(interview.notes)) {
				for (let note of interview.notes) {
					if (note.type === 'AMRSurveyLink') {
						// Mark the note as deleted
						note.deleted = 1;
						//break;
					}
				}
			}
		}

		function openOtherDetailsTab(participant) {
			$scope.addParticipantDetailsTab(-1, participant.reference, true, participant.idParticipant);
		}
		self.openOtherDetailsTab = openOtherDetailsTab;
		function getJobModule(participant) {
			var jobModuleName = { name: "" };
			var surveyLink = getSurveyLink(participant);
			if (surveyLink != undefined) {
				jobModuleName = $rootScope.qualtricsSurveyLinks.find(jobModuleName => jobModuleName.surveyLink === surveyLink.text);
			}
			return jobModuleName.name;
		}
		function populateMappingDetails(participants) {
			participants.reduce(function(p, data) {
				return p.then(function() {
					//data.mappedTo = getJobModule(data);

					ParticipantsService.findParticipant(data.idParticipant).then(function(response) {
						if (response.status === 200) {


							var participantFull = response.data[0];
							data.mappedTo = getJobModule(participantFull);


						}
					});

				});
			}, $q.when(true)).then(function(finalResult) {
				console.log('finish loading rules');

			}, function(err) {
				console.log('error');
			});
		}
		function getSurveyLink(participant) {
			var surveyLink = "";
			if (participant.notes != undefined) {
				surveyLink = participant.notes.find(qualtricsSurveyLinkNote => (qualtricsSurveyLinkNote.type === 'AMRSurveyLink' && !qualtricsSurveyLinkNote.deleted));
			}
			return surveyLink;
		}
	}
})();