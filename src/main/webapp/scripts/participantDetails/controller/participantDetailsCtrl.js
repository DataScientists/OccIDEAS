(function() {
	angular.module('occIDEASApp.ParticipantDetails')
		.controller('ParticipantDetailsCtrl', ParticipantDetailsCtrl);

	ParticipantDetailsCtrl.$inject = ['ParticipantDetailsService', 'ParticipantsService', 'InterviewsService', 'QuestionsService',
		'data', 'updateData', 'startWithReferenceNumber',
		'$state', '$scope', '$filter', '$rootScope', '$mdDialog', 'ngToast', '$sessionStorage'];

	function ParticipantDetailsCtrl(ParticipantDetailsService, ParticipantsService, InterviewsService, QuestionsService,
		data, updateData, startWithReferenceNumber,
		$state, $scope, $filter, $rootScope, $mdDialog, $ngToast, $sessionStorage) {
		var self = this;

		self.addJobHistoryParticipant = addJobHistoryParticipant;
		self.saveParticipantDetails = saveParticipantDetails;
		self.isFirstJob = true;
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
		$scope.qualtricsSurveyLinks = [
			{ name: 'NONO', surveyLink: 'SV_a4MpkYabheITp7U' },
			{ name: 'TRAD', surveyLink: 'SV_3lsCyfioMEqPlgq' }
		];


		if (updateData) {
			checkIsFirstJob(updateData.referenceNumber);
			ParticipantsService.findParticipant(updateData.interviewId).then(function(response) {
				if (response.status === 200) {
					var participant = response.data[0];
					var theDetails = participant.participantDetails;
					if (self.isFirstJob) {
						var detail = theDetails.find(detail => detail.detailName === 'CharCode');
						self.charCode = detail.detailValue;
						detail = theDetails.find(detail => detail.detailName === 'Comments');
						self.comments = detail.detailValue;
					} else {
						var detail = theDetails.find(detail => detail.detailName === 'Title');
						self.jobTitle = detail.detailValue;
						detail = theDetails.find(detail => detail.detailName === 'Product');
						self.product = detail.detailValue;
					}


				}
			});
		} else {
			$scope.introModule = data[0];
		}

		$scope.$root.tabsLoading = false;
		$scope.referenceNumber = null;
		$scope.storage = $sessionStorage;

		$scope.startInterview = function(data) {
			createParticipant(data);
		};
		function createParticipant(data) {
			var participant = {
				reference: $scope.referenceNumber,
				interviews: []
			};
			checkIsFirstJob(participant.reference);



			ParticipantsService.createParticipant(participant).then(function(response) {
				if (response.status === 200) {
					participant = response.data;
					$rootScope.participant = participant;
					var interview = {};
					interview.participant = participant;
					interview.module = $scope.introModule;
					interview.referenceNumber = $scope.referenceNumber;
					if (self.isFirstJob) {
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


														if (self.isFirstJob) {
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
																		}
																	});

																} else {
																	console.log("Could not save first question as module");
																}
															});
														}
														if (self.isFirstJob) {

															$rootScope.participant.participantDetails = [];
															var detail = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'CharCode',
																detailValue: '----'
															}
															var detail1 = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'NumberCode',
																detailValue: '----'
															}
															$rootScope.participant.participantDetails.push(detail);
															$rootScope.participant.participantDetails.push(detail1);
														} else {
															$rootScope.participant.participantDetails = [];
															var detail = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'Title',
																detailValue: '----'
															}
															var detail1 = {
																participantId: $rootScope.participant.idParticipant,
																detailName: 'Product',
																detailValue: '----'
															}
															$rootScope.participant.participantDetails.push(detail);
															$rootScope.participant.participantDetails.push(detail1);


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



		if (startWithReferenceNumber) {
			$scope.referenceNumber = startWithReferenceNumber;
			$scope.startInterview(data);
		} else if (updateData) {

			//updateData.participant = $scope.data;
			$scope.interview = updateData;
			//$rootScope.participant = updateData.participant;
			$scope.participant = updateData;
			//resumeInterview();
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

				if (lastElement > 0) {
					self.isFirstJob = false;
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
			$scope.startInterview(data);

		}

		function saveParticipantDetails() {
			var participant = $rootScope.participant;
			ParticipantsService.findParticipant(participant.idParticipant).then(function(response) {
				if (response.status === 200) {
					var participant = response.data[0];



					ParticipantsService.save(participant).then(function(response) {
						if (response.status === 200) {
							console.log('it works');
						}
					});

				} else {
					$log.error("Inside data of tabs.interviewresume tabs.js could not findParticipant with " + $stateParams.row);

				}

			});
			var mappedJobModule = self.selectedJobModule;
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
		/*
		function buildAndEditQuestionNew(interview, question, jobModuleAnswer) {
			var newQuestionAsked = _.find(interview.questionHistory, function(queuedQuestion) {
				return queuedQuestion.questionId == question.idNode;
			});
			if (!newQuestionAsked) {
				var msg = "OOPS! lost the newQuestion in buildAndEditQuestionNew";
				console.error(msg);
				ngToast.create({
					className: 'danger',
					content: msg,
					animation: 'slide'
				});
			}
			if (newQuestionAsked.questionId == $scope.iq.questionId) {
				newQuestionAsked.answers = $scope.iq.answers;
			}
			var bIsFreeText = false;
			var bDeleteAnswersRequired = false;
			var selectedAnswer = question.selectedAnswer;
			_.each(newQuestionAsked.answers, function(ans) {
				if (ans.deleted == 0) {
					if (selectedAnswer.idNode != ans.answerId) {
						findChildQuestionsToDelete(ans);
						bDeleteAnswersRequired = true;
					}
				}
			});
			if (bDeleteAnswersRequired) {
				$scope.displayQuestions = [];
				for (var i = 0; i < interview.questionHistory.length; i++) {
					var queuedQuestion = interview.questionHistory[i];
					if (queuedQuestion.deleted == 0) {
						if (queuedQuestion.description == 'display') {
							$scope.displayQuestions.push(queuedQuestion);
						}
					}
				}
				var actualAnswer = jobModuleAnswer;
				actualAnswer.interviewQuestionId = newQuestionAsked.id;
				actualAnswer.isProcessed = true;
				newQuestionAsked.answers[0] = actualAnswer;
				var defer = $q.defer();
				saveAnswerNew(newQuestionAsked, defer);
				defer.promise.then(function() {
					newQuestionAsked.processed = true;
					InterviewsService.saveQuestion(newQuestionAsked).then(function(response) {
						if (response.status === 200) {
							console.log("Saved Interview q:" + newQuestionAsked.questionId);
							var defer1 = $q.defer();
							deleteAnswers(answersToDelete, defer1);
							defer1.promise.then(function() {
								console.log("All done deleting child questions answers");
								var defer = $q.defer();
								deleteQuestions(questionsToDelete, defer);
								defer.promise.then(function() {
									console.log("All done deleting child questions");
									$scope.resetInterview();
								});
							});
						}
					});
				});

			} else {
				if (!bIsFreeText) {
					ngToast.create({
						className: 'warning',
						content: "Nothing was changed",
						animation: 'slide'
					});
				}
			}
		}
		
		function saveAnswerNew(newQuestionAsked, defer) {
			if (newQuestionAsked.answers.length > 0) {
				InterviewsService.saveAnswersAndQueueQuestions(newQuestionAsked.answers).then(function(response) {
					if (response.status === 200) {
						/ *_.each(response.data, function(data) {
							var index = _.indexOf(listOfAnswersGiven,
								_.find(listOfAnswersGiven, { id: data.id }));
							if (index != -1) {
								listOfAnswersGiven.splice(index, 1, data);
							} else {
								listOfAnswersGiven.push(data);
							}
						});* /
						if (defer) {
							defer.resolve();
						}
					} else {
						if (defer) {
							defer.reject();
						}
					}
				});
			} else {
				console.error("Trying to save empty interview answers");
				if (defer) {
					defer.resolve();
				}
			}
			if (defer) {
				return defer.promise;
			}
		}
		*/
		function findQualtricsSurveyLink(moduleName) {
			var qualtricsSurveyLink = "";
			qualtricsSurveyLink = $scope.qualtricsSurveyLinks.find(qualtricsSurveyLink => qualtricsSurveyLink.name === moduleName);
			return qualtricsSurveyLink.surveyLink;
		}
	}
})();