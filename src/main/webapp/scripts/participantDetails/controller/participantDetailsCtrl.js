(function() {
	angular.module('occIDEASApp.ParticipantDetails')
		.controller('ParticipantDetailsCtrl', ParticipantDetailsCtrl);

	ParticipantDetailsCtrl.$inject = ['ParticipantDetailsService', 'ParticipantsService', 'InterviewsService', 'QuestionsService',
		'data', 'updateData', 'startWithReferenceNumber', 'mapping', 'addingAddress',
		'$state', '$scope', '$filter', '$rootScope', '$mdDialog', 'ngToast', '$sessionStorage', '$q'];

	function ParticipantDetailsCtrl(ParticipantDetailsService, ParticipantsService, InterviewsService, QuestionsService,
		data, updateData, startWithReferenceNumber, mapping, addingAddress,
		$state, $scope, $filter, $rootScope, $mdDialog, ngToast, $sessionStorage, $q) {
		var self = this;

		self.addJobHistoryParticipant = addJobHistoryParticipant;
        self.removeJobHistoryParticipant = removeJobHistoryParticipant;
        self.addParticipantAddress = addParticipantAddress;
		self.saveParticipantDetails = saveParticipantDetails;
		$rootScope.saveParticipant = saveParticipant;
		self.isZeroRecord = false;
		self.isMapping = false;
		self.isAddresses = false;
		self.charCode = "----";
		self.yearOfBirth = "";
		self.transcriptSent = false;
		self.highestAddress = "R1";

        $scope.sameAsList = [];
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
			'SameAs',
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
					populateParticipantDetails(participantId,self.isZeroRecord);


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
			
			populateParticipantDetails($scope.interview.interviewId,self.isZeroRecord);
			populateParticipantAddresses($scope.interview.interviewId);
		} else if (mapping) {
			self.isMapping = true;
			$scope.introModule = data[0];
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
					self.participantStatus = participant.status;
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

																			//self.selectedJobModule = "NONO";
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
																detailValue: '0'
															}

															var addressCountry = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'R1-Country',
																detailValue: '----'
															}
															var addressNumberStreet = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'R1-Number and Street',
																detailValue: '----'
															}
															var addressSuburbTown = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'R1-Suburb/Town',
																detailValue: '----'
															}
															var addressStateProvince = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'R1-State/Province',
																detailValue: '----'
															}
															var addressPostcode = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'R1-Postcode',
																detailValue: '----'
															}
															var addressFrom = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'R1-From (M/YYYY)',
																detailValue: '--/----'
															}
															var addressUntil = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'R1-Until (M/YYYY)',
																detailValue: '--/----'
															}


															$rootScope.participant.participantDetails.push(priority);
															$rootScope.participant.participantDetails.push(charCode);
															$rootScope.participant.participantDetails.push(yearOfBirth);
															$rootScope.participant.participantDetails.push(gender);
															$rootScope.participant.participantDetails.push(numberCode);
															$rootScope.participant.participantDetails.push(detailComment);
															$rootScope.participant.participantDetails.push(transcriptSent);

															$rootScope.participant.participantDetails.push(addressCountry);
															$rootScope.participant.participantDetails.push(addressNumberStreet);
															$rootScope.participant.participantDetails.push(addressSuburbTown);
															$rootScope.participant.participantDetails.push(addressStateProvince);
															$rootScope.participant.participantDetails.push(addressPostcode);
															$rootScope.participant.participantDetails.push(addressFrom);
															$rootScope.participant.participantDetails.push(addressUntil);
														} else {
															$rootScope.participant.participantDetails = [];
															var from = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'FromDate (M/YYYY)',
																detailValue: '--/----'
															}
															var until = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'UntilDate (M/YYYY)',
																detailValue: '--/----'
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
															var anzsco = {
                                                                participantId: $rootScope.participant.idParticipant,
                                                                detailName: 'ANZSCO',
                                                                detailValue: '000000'
                                                            }
															var averageHours = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'AverageHours',
																detailValue: '----'
															}

															$rootScope.participant.participantDetails.push(priority);
															$rootScope.participant.participantDetails.push(anzsco);
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
																populateParticipantDetails($rootScope.participant.idParticipant,self.isZeroRecord);
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
        $scope.validateDate = function(detail) {
          if (detail.detailName.includes('(M/YYYY)')) {
              const regex = /^(1[0-2]|[1-9])\/\d{4}$/;
              if (regex.test(detail.detailValue)) {
                  detail.textColor = 'black'; // Valid input
                  detail.bgColor = 'white'; // Light green for valid input
              } else {
                  detail.textColor = 'red'; // Invalid input
                  detail.bgColor = '#f8d7da'; // Light red for invalid input
              }
          } else {
              detail.textColor = 'black'; // Default for non-M/YYYY fields
              detail.bgColor = 'white'; // Default background
          }
        };
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
			let lastElement = parseInt(array[array.length - 1].replace(/[PJ]/g, '')) + 1;

			// Update the last element in the array
			array[array.length - 1] = String(lastElement);

			// Join the array elements back into a string with "-"
			let result = array.join("-J");

			return result;
		}
		function checkIsFirstJob(string) {
			if (string !== undefined) {
				let array = string.split("-");

				// Convert the last element of the array to an integer and increment by 1
				let lastElement = parseInt(array[array.length - 1].replace(/[PJ]/g, ''));

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
			saveParticipantDetails();
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
		function removeJobHistoryParticipant() {
		    var participant = $rootScope.participant;
            ParticipantsService.deleteParticipant(participant).then(function(response) {
                if (response.status === 200) {
                    console.log('it works');
                    $scope.removeCurrentTab();
                }
            });

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
                    var detail = theDetails.find(detail => detail.detailName === 'FromDate (M/YYYY)');
                    detail.detailValue = self.fromDate;
                    detail = theDetails.find(detail => detail.detailName === 'UntilDate (M/YYYY)');
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
            participant.status = self.participantStatus;
            ParticipantsService.save(participant).then(function(response) {
                if (response.status === 200) {
                    console.log('Saved the Participant');
                }
            });

            saveAllParticipantStatus();

		}
		function saveParticipant(participant){
		    ParticipantsService.save(participant).then(function(response) {
        		if (response.status === 200) {
        			console.log('it works');
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
					//self.selectedJobModule = jobModuleName.name;
				}

			}
			self.fullQualtricsLink = fullQualtricsLink;
			return fullQualtricsLink;
		}
		function populateParticipantDetails(participantId,withPersonalDetails) {
			ParticipantsService.findParticipant(participantId).then(function(response) {
				if (response.status === 200) {
					var participant = response.data[0];
					$rootScope.participant = participant;
					self.participantStatus = participant.status;
					var theDetails = participant.participantDetails;
					if (withPersonalDetails) {
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
					} else {
						if (theDetails.length > 0) {
							var detail = theDetails.find(detail => detail.detailName === 'FromDate (M/YYYY)');
							self.fromDate = detail.detailValue;
							detail = theDetails.find(detail => detail.detailName === 'UntilDate (M/YYYY)');
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
					self.participantStatus = participant.status;
					var theDetails = participant.participantDetails;
					var addresses = theDetails.filter(detail => detail.detailName.startsWith('R'));

					self.groupedAddresses = {};

					// Iterate through the notes array
					for (let detail of addresses) {
						if (detail.detailName) {
							// Extract the prefix if it starts with 'R' followed by a digit
							const match = detail.detailName.match(/^R(\d+)/);
							if (match) {
								const prefix = match[0];
								self.highestAddress = prefix;// e.g., 'R1', 'R2', etc.
								if (!self.groupedAddresses[prefix]) {
									self.groupedAddresses[prefix] = [];
								}
								self.groupedAddresses[prefix].push(detail);
							}
						}
					}
					//prepopulateDetails();

				}
			});
		}
		self.populateParticipantAddresses = populateParticipantAddresses;

        function prepopulateDetails() {
            let array = $rootScope.participant.participantDetails;
            // Iterate through the array
            for (let i = 0; i < array.length - 1; i++) {
                // Check if the current detailName follows the pattern 'R[x]-Until'
                const untilMatch = array[i].detailName.match(/Until/);

                if (untilMatch) {
                    // Extract the 'x' value from the detailName
                    const currentIndex = parseInt(untilMatch.index);
                    const nextFromIndex = array.findIndex(obj => obj.detailName === `R${currentIndex + 1}-From (M/YYYY)`);

                    // Check if the corresponding 'R[x+1]-From' object exists
                    if (nextFromIndex !== -1) {
                        // Assign the detailValue from 'R[x]-Until' to 'R[x+1]-From'
                        if(array[nextFromIndex].detailValue === '--/----'){
                            array[nextFromIndex].detailValue = array[i].detailValue;
                        }

                    }
                }
            }
        }
        //self.prepopulateDetails = prepopulateDetails;

		function getOtherParticipantJob(referenceNumberPrefix) {
			ParticipantsService.getByReferenceNumberPrefix(referenceNumberPrefix).then(function(response) {
				if (response.status === 200) {
					var otherParticipantJobs = response.data;
					self.otherParticipantJobs = otherParticipantJobs;
					populateMappingDetails(self.otherParticipantJobs);
				}
			});
		}
		self.getOtherParticipantJob = getOtherParticipantJob;

		function allMapped(participants) {
			for (let participant of participants) {
				if (!participant.mappedTo) {
					return false;
				}
			}
			return true;
		}
		function saveParticipantMappings(participants) {
		    var participants = participants.filter(obj => obj.mappedTo !== "NONO");
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
								var noteText = "";
								var noteType = "";
								if(data.mappedTo.startsWith('SameAs')){
								    noteText = data.mappedTo;
								    noteType = 'AMRSameAs';
								}else{
								    noteText = findQualtricsSurveyLink(data.mappedTo);
								    noteType = 'AMRSurveyLink';
								}
								interview.notes.push({
									interviewId: interview.interviewId,
									text: noteText,
									type: noteType
								});

								InterviewsService.save(interview).then(function(response) {
									if (response.status === 200) {
										console.log('it works');
									}
								});

								var ques = $scope.introModule.nodes[0];
								var actualAnswer = ques.nodes.find(possibleAnswer => possibleAnswer.name === data.mappedTo);
                                //var actualLinkQuestion = actualAnswer.nodes.find(linkQuestion => LinkQuestion.name === data.mappedTo);
                                var interviewQuestion = interview.questionHistory[1];
                                var interviewQuestionsSize = interview.questionHistory.length;

                                interviewQuestion.answers = interview.answerHistory;
                                interviewQuestion.answers.forEach(answer => {
                                        answer.deleted = 1;
                                    });

                                actualAnswer.interviewQuestionId = interviewQuestion.id;
                                actualAnswer.lastUpdated = getCurrentDateTime();
                                actualAnswer.interviewQuestionId = interviewQuestion.id;
                                actualAnswer.parentQuestionId = ques.idNode;
                                actualAnswer.answerId = actualAnswer.idNode;
                                actualAnswer.isProcessed = true;
                                actualAnswer.idInterview = interview.interviewId;
                                actualAnswer.nodeClass = "P";
                                actualAnswer.answerFreetext = data.mappedTo;


                                interviewQuestion.answers.push(actualAnswer);

                                var questions = [];

                                if(interviewQuestionsSize>2){
                                //todo
                                    //var interviewLinkQuestion = interview.questionHistory[2];
                                    //interviewLinkQuestion.deleted = 1;
                                    //questions.push(interviewLinkQuestion);
                                    //InterviewsService.saveAnswers(interviewQuestion.answers).then(function(response) {
                                    //    if (response.status === 200) {
                                   //         console.log("Job module saved as first answer");
                                    //    } else {
                                    //        console.log("Could not save first question as module");
                                    //    }
                                   // });
                                }else{
                                    InterviewsService.saveAnswersAndQueueQuestions(interviewQuestion.answers).then(function(response) {
                                        if (response.status === 200) {
                                            console.log("Job module saved as first answer");
                                        } else {
                                            console.log("Could not save first question as module");
                                        }
                                    });
                                }


                                interviewQuestion.processed = true;
                                questions.push(interviewQuestion);

                                InterviewsService.saveQuestions(questions).then(function(response) {
                                    if (response.status == 200) {
                                        console.log("Updated question to be processed");

                                    }
                                });
							}
						});
						data.status = self.participantStatus;
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
			} else {
				ngToast.create({
					className: 'danger',
					content: 'One or more Jobs are not mapped.',
					dismissButton: true,
					dismissOnClick: false,
					animation: 'slide'
				});
			}
			var participantRecord = self.otherParticipantJobs.find(obj => obj.mappedTo === "NONO");
            var detail = participantRecord.participantDetails.find(detail => detail.detailName === 'Comments');
            detail.detailValue = self.comments;
			participantRecord.status = self.participantStatus;
            ParticipantsService.save(participantRecord).then(function(response) {
                if (response.status === 200) {
                    console.log('it works');
                }
            });
		}
		self.saveParticipantMappings = saveParticipantMappings;
		function saveAllParticipantStatus(){
		    var participants = self.otherParticipantJobs;
		    if(participants){
                participants.reduce(function(p, data) {
                    return p.then(function() {
                        data.status = self.participantStatus;
                        ParticipantsService.save(data).then(function(response) {
                            if (response.status === 200) {
                                console.log('it works');
                            }
                        });
                    });
                }, $q.when(true)).then(function(finalResult) {
                    console.log('finish saving participant statuses');
                }, function(err) {
                    console.log('error');
                });
		    }

		}
		function deleteOldSurveyLink(interview) {
			if (Array.isArray(interview.notes)) {
				for (let note of interview.notes) {
					if (note.type === 'AMRSurveyLink') {
						note.deleted = 1;
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
		    $scope.sameAsList = [];
			var firstParticipant = participants[0];
			populateParticipantDetails(firstParticipant.idParticipant,true);
			participants.reduce(function(p, data) {
				return p.then(function() {
				    let array = data.reference.split('-');
				    var jobNumber = "";
				    if(array.length>0){
				        jobNumber = array[1];
				        if(jobNumber.startsWith('J')){
				            $scope.sameAsList.push('SameAs-'+jobNumber);
				        }
				    }

					ParticipantsService.findParticipant(data.idParticipant).then(function(response) {
						if (response.status === 200) {
							var participantFull = response.data[0];
							data.mappedTo = getJobModule(participantFull);
							//WIP need to pull functions from participantCtrl
							data.qualtricsLink = getSurveyLink(participantFull);
							data.statusDescription = participantFull.statusDescription;
                            if (data.qualtricsLink) {
                                data.fullQualtricsLink = 'https://curtin.au1.qualtrics.com/jfe/form/' + data.qualtricsLink.text + '?AMRID=' + participantFull.reference;
                                data.interviewDetails = getInterviewDetails(participantFull);
                                data.mappedPriority = getJobModulePriority(participantFull);
                                data.mappedTo = getJobModule(participantFull);
                            } else {
                                data.fullQualtricsLink = "";
                            }
						}
					});
				});
			}, $q.when(true)).then(function(finalResult) {
				console.log('finish loading rules');
			}, function(err) {
				console.log('error');
			});
		}
		function getJobModulePriority(participant) {
            var jobModulePriority = { detailValue: "" };
            if (participant.participantDetails != undefined) {
                jobModulePriority = participant.participantDetails.find(jobModulePriority => jobModulePriority.detailName === 'Priority');
            }
            return jobModulePriority.detailValue;
        }
		function getInterviewDetails(participant) {
            var workingAs = "";
            var workingWith = "";
            var jobStartedIn = "";
            if (participant.participantDetails != undefined) {
                workingAs = participant.participantDetails.find(detail => (detail.detailName === 'Title'));
                workingWith = participant.participantDetails.find(detail => (detail.detailName === 'Employer'));
                jobStartedIn = participant.participantDetails.find(detail => (detail.detailName === 'FromDate'));
            }
            var interviewDetails = "";
            if (workingAs != undefined) {

                interviewDetails = "The next questions refer to when you were working as "
                    + workingAs.detailValue + " with " + workingWith.detailValue + ". That job started in " + jobStartedIn.detailValue;

            } else {
                interviewDetails = "The next questions are the non-occupational questions";
            }
            return interviewDetails;
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
			var addressNumberStreet = {
				participantId: $rootScope.participant.idParticipant,
				detailName: 'R' + nextAddressNumber + '-Number and Street',
				detailValue: '----'
			}
			var addressSuburbTown = {
                participantId: $rootScope.participant.idParticipant,
                detailName: 'R' + nextAddressNumber + '-Suburb/Town',
                detailValue: '----'
            }
			var addressStateProvince = {
                participantId: $rootScope.participant.idParticipant,
                detailName: 'R' + nextAddressNumber + '-State/Province',
                detailValue: '----'
            }
			var addressPostcode = {
                participantId: $rootScope.participant.idParticipant,
                detailName: 'R' + nextAddressNumber + '-Postcode',
                detailValue: '----'
            }
			var addressFrom = {
				participantId: $rootScope.participant.idParticipant,
				detailName: 'R' + nextAddressNumber + '-From (M/YYYY)',
				detailValue: '--/----'
			}
			var addressUntil = {
				participantId: $rootScope.participant.idParticipant,
				detailName: 'R' + nextAddressNumber + '-Until (M/YYYY)',
				detailValue: '--/----'
			}
			$rootScope.participant.participantDetails.push(addressCountry);
			$rootScope.participant.participantDetails.push(addressNumberStreet);
			$rootScope.participant.participantDetails.push(addressSuburbTown);
			$rootScope.participant.participantDetails.push(addressStateProvince);
			$rootScope.participant.participantDetails.push(addressPostcode);
			$rootScope.participant.participantDetails.push(addressFrom);
			$rootScope.participant.participantDetails.push(addressUntil);
			self.highestAddress = 'R' + nextAddressNumber;
			ParticipantsService.save($rootScope.participant).then(function(response) {
				if (response.status === 200) {
					console.log('it works');
					populateParticipantAddresses($rootScope.participant.idParticipant);
				}
			});


		}
		self.addAddress = addAddress;
		function removeAddress() {
            var nextAddressNumber = findHighestRDigit($rootScope.participant.participantDetails);
            var highestAddressNumber = 'R'+(nextAddressNumber - 1);
            $rootScope.participant.participantDetails = removeObjectsWithNameStarting($rootScope.participant.participantDetails,highestAddressNumber);
            self.highestAddress = 'R'+(nextAddressNumber - 2);

            ParticipantDetailsService.deleteParticipantDetails($rootScope.participant.idParticipant,highestAddressNumber).then(function(response) {
                if (response.status === 200) {
                    console.log('it works');
                    populateParticipantAddresses($rootScope.participant.idParticipant);
                }
            });
        }
        self.removeAddress = removeAddress;
        function removeObjectsWithNameStarting(arr, prefix) {
          return arr.filter(obj => obj.detailName.startsWith(prefix));
        }

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

        $scope.startsWithSameAs = function(mappedTo) {
            var retValue = false;
            if(mappedTo){
                retValue = mappedTo.startsWith('SameAs');
            }
            return retValue;
        };

        $scope.selectText = function(event) {
                event.target.select();
            };
		$scope.yearsOfBirth = ['Unknown', ''];

		// Start year and end year
		var startYear = 1900;
		var endYear = 2050;

		// Populate the years of birth array with years from startYear to endYear
		for (var year = startYear; year <= endYear; year++) {
			$scope.yearsOfBirth.push(year.toString());
		}
		$scope.participantStatusList =
            [
                {text: 'To be updated', value: 1},
                {text: 'To be reviewed', value: 2},
                {text: 'Ready for interview', value: 3},
                {text: 'Interviews complete', value: 4},
                {text: 'No further contact please', value: 5}
            ];

	}
})();