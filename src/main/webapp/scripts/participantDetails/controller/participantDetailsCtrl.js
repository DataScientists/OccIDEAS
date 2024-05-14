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
		self.isFirstJob = true;

		if (updateData) {
			checkIsFirstJob(updateData.referenceNumber);
		}
		$scope.data = data;
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
					interview.module = $scope.data[0];
					interview.referenceNumber = $scope.referenceNumber;
					InterviewsService.startInterview(interview).then(function(response) {
						if (response.status === 200) {
							interview.interviewId = response.data.interviewId;

							var copyParticipant = angular.copy($scope.participant);
							interview.participant = copyParticipant;
							$scope.interview = {};
							$scope.interview = interview;
							$scope.interviewStarted = true;
							$scope.interviewId = response.data.interviewId;

							var firstLinkIntervewQuestion = populateInteviewQuestionJsonByModule(interview, interview.module);
							firstLinkIntervewQuestion.number = "0";
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
														//add function to set the Nono module later
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
	}
})();