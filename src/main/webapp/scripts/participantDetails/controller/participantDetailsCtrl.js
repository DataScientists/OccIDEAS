(function() {
	angular.module('occIDEASApp.ParticipantDetails')
		.controller('ParticipantDetailsCtrl', ParticipantDetailsCtrl);

	ParticipantDetailsCtrl.$inject = ['ParticipantDetailsService', 'ParticipantsService', 'InterviewsService', 'QuestionsService',
		'data', 'updateData', 'startWithReferenceNumber', 'mapping', 'addingAddress',
		'$state', '$scope', '$filter', '$rootScope', '$mdDialog', 'ngToast', '$sessionStorage', '$q'];

	function ParticipantDetailsCtrl(ParticipantDetailsService, ParticipantsService, InterviewsService, QuestionsService,
		data, updateData, startWithReferenceNumber, mapping, addingAddress,
		$state, $scope, $filter, $rootScope, $mdDialog, $ngToast, $sessionStorage, $q) {
		var self = this;

		self.addJobHistoryParticipant = addJobHistoryParticipant;
		self.addParticipantAddress = addParticipantAddress;
		self.saveParticipantDetails = saveParticipantDetails;
		self.isZeroRecord = false;
		self.isMapping = false;
		self.isAddresses = false;
		self.charCode = "----";
		self.yearOfBirth = "";
		self.transcriptSent = false;

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
			populateParticipantAddresses($scope.interview.interviewId);
		} else if (mapping) {
			self.isMapping = true;
			console.log("mapping");
			$scope.interview = mapping;
			$rootScope.referenceNumber = $scope.interview.referenceNumber;
			checkIsFirstJob($rootScope.referenceNumber);
			getOtherParticipantJob(self.referenceNumberPrefix);
			getFullSurveyLink($scope.interview);

		} else if (addingAddress) {
			self.isAddresses = true;
			$scope.interview = addingAddress;
			checkIsFirstJob($scope.interview.referenceNumber);
			populateParticipantAddresses($scope.interview.interviewId);
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
															var yearOfBirth = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'YearOfBirth',
																detailValue: ''
															}
															var gender = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'Gender',
																detailValue: ''
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
															var transcriptSent = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'TranscriptSent',
																detailValue: 'false'
															}
															var priority = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'Priority',
																detailValue: '1'
															}

															var addressCountry = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'R1-Country',
																detailValue: '----'
															}
															var addressAddress = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'R1-Address',
																detailValue: '----'
															}
															var addressFrom = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'R1-From',
																detailValue: '----'
															}
															var addressUntil = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'R1-Until',
																detailValue: '----'
															}


															$rootScope.participant.participantDetails.push(priority);
															$rootScope.participant.participantDetails.push(charCode);
															$rootScope.participant.participantDetails.push(yearOfBirth);
															$rootScope.participant.participantDetails.push(gender);
															$rootScope.participant.participantDetails.push(numberCode);
															$rootScope.participant.participantDetails.push(detailComment);
															$rootScope.participant.participantDetails.push(transcriptSent);

															$rootScope.participant.participantDetails.push(addressCountry);
															$rootScope.participant.participantDetails.push(addressAddress);
															$rootScope.participant.participantDetails.push(addressFrom);
															$rootScope.participant.participantDetails.push(addressUntil);
														} else {
															$rootScope.participant.participantDetails = [];
															var from = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'FromDate',
																detailValue: '----'
															}
															var until = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'UntilDate',
																detailValue: '----'
															}
															var employer = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'Employer',
																detailValue: '----'
															}
															var address = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'Address',
																detailValue: '----'
															}
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
															var averageHours = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'AverageHours',
																detailValue: '----'
															}

															$rootScope.participant.participantDetails.push(priority);
															$rootScope.participant.participantDetails.push(from);
															$rootScope.participant.participantDetails.push(until);
															$rootScope.participant.participantDetails.push(employer);
															$rootScope.participant.participantDetails.push(address);
															$rootScope.participant.participantDetails.push(title);
															$rootScope.participant.participantDetails.push(product);
															$rootScope.participant.participantDetails.push(averageHours);


														}
														ParticipantsService.save(participant).then(function(response) {
															if (response.status === 200) {
																populateParticipantDetails($rootScope.participant.idParticipant);
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
		function addParticipantAddress() {
			var theReferenceNumber = "";
			if ($scope.referenceNumber) {
				theReferenceNumber = $scope.referenceNumber
			} else if ($scope.interview.referenceNumber) {
				theReferenceNumber = $scope.interview.referenceNumber;
			}

			$scope.addParticipantAddressTab(theReferenceNumber, $scope.interview.interviewId)
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

						detail = theDetails.find(detail => detail.detailName === 'YearOfBirth');
						detail.detailValue = self.yearOfBirth;
						detail = theDetails.find(detail => detail.detailName === 'Gender');
						detail.detailValue = self.gender;
						detail = theDetails.find(detail => detail.detailName === 'NumberCode');
						detail.detailValue = self.numberCode;
						detail = theDetails.find(detail => detail.detailName === 'Comments');
						detail.detailValue = self.comments;
						detail = theDetails.find(detail => detail.detailName === 'TranscriptSent');
						detail.detailValue = self.transcriptSent;

					} else {
						if (theDetails.length > 0) {
							var detail = theDetails.find(detail => detail.detailName === 'FromDate');
							detail.detailValue = self.fromDate;
							detail = theDetails.find(detail => detail.detailName === 'UntilDate');
							detail.detailValue = self.untilDate;
							detail = theDetails.find(detail => detail.detailName === 'Employer');
							detail.detailValue = self.employer;
							detail = theDetails.find(detail => detail.detailName === 'Address');
							detail.detailValue = self.address;
							detail = theDetails.find(detail => detail.detailName === 'Title');
							detail.detailValue = self.jobTitle;
							detail = theDetails.find(detail => detail.detailName === 'Product');
							detail.detailValue = self.product;
							detail = theDetails.find(detail => detail.detailName === 'AverageHours');
							detail.detailValue = self.averageHours;
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
						detail = theDetails.find(detail => detail.detailName === 'YearOfBirth');
						self.yearOfBirth = detail.detailValue;
						detail = theDetails.find(detail => detail.detailName === 'Gender');
						self.gender = detail.detailValue;
						detail = theDetails.find(detail => detail.detailName === 'NumberCode');
						self.numberCode = detail.detailValue;
						detail = theDetails.find(detail => detail.detailName === 'TranscriptSent');
						if (detail.detailValue === 'true') {
							self.transcriptSent = true;
						} else {
							self.transcriptSent = false;
						}

						//todo participant status
					} else {
						if (theDetails.length > 0) {
							var detail = theDetails.find(detail => detail.detailName === 'FromDate');
							self.fromDate = detail.detailValue;
							detail = theDetails.find(detail => detail.detailName === 'UntilDate');
							self.untilDate = detail.detailValue;
							detail = theDetails.find(detail => detail.detailName === 'Employer');
							self.employer = detail.detailValue;
							detail = theDetails.find(detail => detail.detailName === 'Address');
							self.address = detail.detailValue;
							detail = theDetails.find(detail => detail.detailName === 'Title');
							self.jobTitle = detail.detailValue;
							detail = theDetails.find(detail => detail.detailName === 'Product');
							self.product = detail.detailValue;
							detail = theDetails.find(detail => detail.detailName === 'AverageHours');
							self.averageHours = detail.detailValue;
						} else {
							//todo
							console.log("no details yet");
						}

					}
					getFullSurveyLink(participant);

				}
			});
		}
		function populateParticipantAddresses(participantId) {
			ParticipantsService.findParticipant(participantId).then(function(response) {
				if (response.status === 200) {
					var participant = response.data[0];
					$rootScope.participant = participant;
					var theDetails = participant.participantDetails;
					var addresses = theDetails.filter(detail => detail.detailName.startsWith('R'));

					self.groupedAddresses = {};

					// Iterate through the notes array
					for (let detail of addresses) {
						if (detail.detailName) {
							// Extract the prefix if it starts with 'R' followed by a digit
							const match = detail.detailName.match(/^R(\d+)/);
							if (match) {
								const prefix = match[0]; // e.g., 'R1', 'R2', etc.
								if (!self.groupedAddresses[prefix]) {
									self.groupedAddresses[prefix] = [];
								}
								self.groupedAddresses[prefix].push(detail);
							}
						}
					}

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
		function findHighestRDigit(details) {
			// Check if notes is a valid array
			if (!Array.isArray(details)) {
				throw new Error("Invalid input: notes array is not valid.");
			}

			// Variable to keep track of the highest digit found
			let highestDigit = null;

			// Iterate through the notes array
			for (let detail of details) {
				if (detail.detailName) {
					// Extract the prefix if it starts with 'R' followed by a digit
					const match = detail.detailName.match(/^R(\d+)/);
					if (match) {
						const digit = parseInt(match[1], 10);
						// Update the highest digit if the current digit is higher
						if (highestDigit === null || digit > highestDigit) {
							highestDigit = digit;
						}
					}
				}
			}

			return highestDigit + 1;
		}
		function addAddress() {
			var nextAddressNumber = findHighestRDigit($rootScope.participant.participantDetails);
			var addressCountry = {
				participantId: $rootScope.participant.idParticipant,
				detailName: 'R' + nextAddressNumber + '-Country',
				detailValue: '----'
			}
			var addressAddress = {
				participantId: $rootScope.participant.idParticipant,
				detailName: 'R' + nextAddressNumber + '-Address',
				detailValue: '----'
			}
			var addressFrom = {
				participantId: $rootScope.participant.idParticipant,
				detailName: 'R' + nextAddressNumber + '-From',
				detailValue: '----'
			}
			var addressUntil = {
				participantId: $rootScope.participant.idParticipant,
				detailName: 'R' + nextAddressNumber + '-Until',
				detailValue: '----'
			}
			$rootScope.participant.participantDetails.push(addressCountry);
			$rootScope.participant.participantDetails.push(addressAddress);
			$rootScope.participant.participantDetails.push(addressFrom);
			$rootScope.participant.participantDetails.push(addressUntil);
			ParticipantsService.save($rootScope.participant).then(function(response) {
				if (response.status === 200) {
					console.log('it works');
					populateParticipantAddresses($rootScope.participant.idParticipant);
				}
			});


		}
		self.addAddress = addAddress;
		function saveParticipantAddresses() {
			ParticipantsService.save($rootScope.participant).then(function(response) {
				if (response.status === 200) {
					console.log('it works');
				}
			});
		}
		self.saveParticipantAddresses = saveParticipantAddresses;

		function showParticipantMappingTab() {
			if (self.otherParticipantJobs) {
				if (self.otherParticipantJobs.length > 1) {
					var participant = self.otherParticipantJobs[1];
					$scope.addParticipantMappingTab(participant.reference, participant.idParticipant);
				} else {
					var msg = "No Jobs to map.";
					ngToast.create({
						className: 'danger',
						content: msg,
						animation: 'slide'
					});
				}
			} else {
				var msg = "No Jobs to map.";
				ngToast.create({
					className: 'danger',
					content: msg,
					animation: 'slide'
				});
			}

		}
		self.showParticipantMappingTab = showParticipantMappingTab;

		$scope.yearsOfBirth = ['Unknown', ''];

		// Start year and end year
		var startYear = 1900;
		var endYear = 2050;

		// Populate the years of birth array with years from startYear to endYear
		for (var year = startYear; year <= endYear; year++) {
			$scope.yearsOfBirth.push(year.toString());
		}
		$scope.participantStatusList = ['To be updated', 'To be reviewed', 'Ready for Interview', 'JSM Interviews complete', 'No Further Contact Please']
	}
})();