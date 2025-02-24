(function() {
	angular.module('occIDEASApp.ParticipantDataEntryJob')
		.controller('ParticipantDataEntryJobCtrl', ParticipantDataEntryJobCtrl);

	ParticipantDataEntryJobCtrl.$inject = ['ParticipantsService', 'InterviewsService', 'ParticipantDetailsService',
		'data', 'startWithReferenceNumber',
		'$state', '$scope', '$filter', '$rootScope', '$mdDialog', 'ngToast', '$sessionStorage', '$q'];

	function ParticipantDataEntryJobCtrl(ParticipantsService, InterviewsService, ParticipantDetailsService,
		data, startWithReferenceNumber,
		$state, $scope, $filter, $rootScope, $mdDialog, ngToast, $sessionStorage, $q) {
		var self = this;

        self.introModule = data[0];

		if (startWithReferenceNumber) {
			$scope.referenceNumber = startWithReferenceNumber;
			checkIsFirstJob($scope.referenceNumber);
            InterviewsService.checkReferenceNumberExists($scope.referenceNumber).then(function(data) {
				if (data.status == 200) {
					$scope.interview = data.data[0];
					ParticipantsService.getByReferenceNumber($scope.referenceNumber).then(function(response) {
                        if (response.status === 200) {
                            var participant = response.data;
                            $rootScope.participant = participant;
                            populateParticipantDetails();
                            //groupTheAddresses();
                            //$scope.$root.tabsLoading = false;
                            $rootScope.tabsLoading = false;
                        }
                    });
				} else if (data.status == 204) {
					newParticipantAsJob($scope.referenceNumber);
				} else {
					var msg = "Error occured during checkReferenceNumberExists.";
					ngToast.create({
						className: 'danger',
						content: msg,
						animation: 'slide'
					});
				}
			})
		}
        newParticipantAsJob = function(){
            var participant = {
                reference: $scope.referenceNumber,
                status: 0,
                interviews: []
            };
            ParticipantsService.createParticipant(participant).then(function(response) {
                if (response.status === 200) {
                    participant = response.data;
                    $rootScope.participant = participant;
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

                    populateParticipantDetails();

                    saveParticipant();

                    var interview = {};
                    interview.participant = participant;
                    interview.module = self.introModule;
                    interview.referenceNumber = $scope.referenceNumber
                    InterviewsService.startInterview(interview).then(function(response) {
                        if (response.status === 200) {
                            interview.interviewId = response.data.interviewId;
                            var copyParticipant = angular.copy($scope.participant);
                            interview.participant = copyParticipant;

                            $scope.interview = interview;

                            var firstLinkInterviewQuestion = populateInterviewQuestionJsonByModule(interview, self.introModule);
                            firstLinkInterviewQuestion.number = "0";
                            firstLinkInterviewQuestion.isProcessed = true;
                            //inserting the first link intro module
                            InterviewsService.saveLinkQuestionAndQueueQuestions(firstLinkInterviewQuestion).then(function(response) {
                                if (response.status === 200) {
                                    console.log('IntroModule Prepared');
                                /*
                                    InterviewsService.get($scope.interview.interviewId).then(function(response) {
                                        if (response.status == 200) {

                                            $scope.interview = response.data[0];

                                            var question = findNextQuestionQueued($scope.interview);
                                            if (question) {
                                                QuestionsService.findQuestionSingleChildLevel(question.questionId).then(function(response) {
                                                    if (response.status === 200) {
                                                        var ques = response.data[0];



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
                                            }
                                        }
                                    });
                                    */
                                }
                            });


                            $scope.$root.tabsLoading = false;
                       }
                    });


                }
            });
        }
		setPriority = function(){
		    var participant = $rootScope.participant;
            var theDetails = participant.participantDetails;
		    var detail = theDetails.find(detail => detail.detailName === 'Priority');
		    self.priority = detail;
		}
		setAnzsco = function(){
		    var participant = $rootScope.participant;
            var theDetails = participant.participantDetails;
		    var detail = theDetails.find(detail => detail.detailName === 'ANZSCO');
		    self.Anzsco = detail;
		}
 		setFrom = function(){
 		    var participant = $rootScope.participant;
             var theDetails = participant.participantDetails;
 		    var detail = theDetails.find(detail => detail.detailName === 'FromDate (M/YYYY)');
 		    self.from = detail;
 		}
 		setUntil = function(){
 		    var participant = $rootScope.participant;
             var theDetails = participant.participantDetails;
 		    var detail = theDetails.find(detail => detail.detailName === 'UntilDate (M/YYYY)');
 		    self.until = detail;
 		}
  		setEmployer = function(){
  		    var participant = $rootScope.participant;
              var theDetails = participant.participantDetails;
  		    var detail = theDetails.find(detail => detail.detailName === 'Employer');
  		    self.employer = detail;
  		}
  		setAddress = function(){
  		    var participant = $rootScope.participant;
              var theDetails = participant.participantDetails;
  		    var detail = theDetails.find(detail => detail.detailName === 'Address');
  		    self.address = detail;
  		}
  		setTitle = function(){
  		    var participant = $rootScope.participant;
              var theDetails = participant.participantDetails;
  		    var detail = theDetails.find(detail => detail.detailName === 'Title');
  		    self.title = detail;
  		}
  		setProduct = function(){
  		    var participant = $rootScope.participant;
              var theDetails = participant.participantDetails;
  		    var detail = theDetails.find(detail => detail.detailName === 'Product');
  		    self.product = detail;
  		}
  		setAverageHours = function(){
  		    var participant = $rootScope.participant;
              var theDetails = participant.participantDetails;
  		    var detail = theDetails.find(detail => detail.detailName === 'AverageHours');
  		    self.averageHours = detail;
  		}
  		populateParticipantDetails = function(){
            setFrom();
            setUntil();
            setPriority();
            setAnzsco();
            setEmployer();
            setAddress();
            setTitle();
            setProduct();
            setAverageHours();

  		}
  		function addJobHistoryParticipant() {
            saveParticipant();
            var theReferenceNumber = $rootScope.participant.reference;

            var nextJobHistoryReferenceNumber = splitAndIncrementLast(theReferenceNumber);

            $scope.addParticipantJobTab(nextJobHistoryReferenceNumber);

        }
        self.addJobHistoryParticipant = addJobHistoryParticipant;
        function saveParticipant(){
            var participant = $rootScope.participant;
            ParticipantsService.save(participant).then(function(response) {
                if (response.status === 200) {
                    console.log('Saved the Participant');
                    ParticipantsService.getByReferenceNumber($scope.referenceNumber).then(function(response) {
                        if (response.status === 200) {
                            var participant = response.data;
                            $rootScope.participant = participant;
                            populateParticipantDetails();
                        }
                    });
                }
            });
        }

        self.saveParticipant = saveParticipant;
        $rootScope.saveParticipant = saveParticipant;

        self.validateDate = function(detail) {
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






        /* utilities */
        function populateInterviewQuestionJsonByModule(interview, module) {
            return {
                idInterview: interview.interviewId,
                topNodeId: module.idNode,
                questionId: undefined,
                parentModuleId: module.idNode,
                parentAnswerId: undefined,
                name: module.name,
                description: module.description,                nodeClass: module.nodeclass,
                number: module.number,
                type: module.type,
                link: module.idNode,
                modCount: 1,
                intQuestionSequence: 1,
                deleted: 0,
                answers: []
            };
        }
        function checkIsFirstJob(string) {
            if (string !== undefined) {
                let array = string.split("-");

                // Convert the last element of the array to an integer and increment by 1
                let lastElement = parseInt(array[array.length - 1].replace(/[PJ]/g, ''));

                self.jobNumber = "J" + lastElement;
                self.referenceNumberPrefix = array[0];
            }
        }

        function removeObjectsWithNameStarting(arr, prefix) {
          return arr.filter(obj => !obj.detailName.startsWith(prefix));
        }
        function splitAndIncrementLast(string) {
            let array = string.split("-");
            let lastElement = parseInt(array[array.length - 1].replace(/[PJ]/g, '')) + 1;
            array[array.length - 1] = String(lastElement);
            let result = array.join("-J");

            return result;
        }

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