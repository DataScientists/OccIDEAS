(function() {
	angular.module('occIDEASApp.ParticipantDataEntry')
		.controller('ParticipantDataEntryCtrl', ParticipantDataEntryCtrl);

	ParticipantDataEntryCtrl.$inject = ['ParticipantsService', 'InterviewsService', 'ParticipantDetailsService', 'QuestionsService',
		'data', 'startWithReferenceNumber',
		'$state', '$scope', '$filter', '$rootScope', '$mdDialog', 'ngToast', '$sessionStorage', '$q'];

	function ParticipantDataEntryCtrl(ParticipantsService, InterviewsService, ParticipantDetailsService, QuestionsService,
		data, startWithReferenceNumber,
		$state, $scope, $filter, $rootScope, $mdDialog, ngToast, $sessionStorage, $q) {
		var self = this;

        self.introModule = data[0];

        self.groupedAddresses = {};

		if (startWithReferenceNumber) {
			$scope.referenceNumber = startWithReferenceNumber;
			setReferenceNumberPrefix($scope.referenceNumber);
            InterviewsService.checkReferenceNumberExists($scope.referenceNumber).then(function(data) {
				if (data.status == 200) {
					$scope.interview = data.data[0];
					ParticipantsService.getByReferenceNumber($scope.referenceNumber).then(function(response) {
                        if (response.status === 200) {
                            var participant = response.data;
                            $rootScope.participant = participant;
                            populateParticipantDetails();
                            $scope.$root.tabsLoading = false;
                        }
                    });
				} else if (data.status == 204) {
					newParticipant($scope.referenceNumber);
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
        newParticipant = function(){
            var participant = {
                reference: $scope.referenceNumber,
                status: 1,
                interviews: []
            };
            ParticipantsService.createParticipant(participant).then(function(response) {
                if (response.status === 200) {
                    participant = response.data;
                    $rootScope.participant = participant;
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
                    var comments = {
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
                    $rootScope.participant.participantDetails.push(comments);
                    $rootScope.participant.participantDetails.push(transcriptSent);

                    $rootScope.participant.participantDetails.push(addressCountry);
                    $rootScope.participant.participantDetails.push(addressNumberStreet);
                    $rootScope.participant.participantDetails.push(addressSuburbTown);
                    $rootScope.participant.participantDetails.push(addressStateProvince);
                    $rootScope.participant.participantDetails.push(addressPostcode);
                    $rootScope.participant.participantDetails.push(addressFrom);
                    $rootScope.participant.participantDetails.push(addressUntil);

                    populateParticipantDetails();

                    var interview = {};
                    interview.participant = participant;
                    interview.module = self.introModule;
                    interview.referenceNumber = $scope.referenceNumber;
                    interview.notes = [];
                    interview.notes.push({
                        interviewId: participant.idParticipant,
                        text: findQualtricsSurveyLink('NONO'),
                        type: 'AMRSurveyLink'
                    });
                    InterviewsService.startInterview(interview).then(function(response) {
                        if (response.status === 200) {
                            interview.interviewId = response.data.interviewId;

                            var copyParticipant = angular.copy($scope.participant);
                            interview.participant = copyParticipant;
                            //$scope.interview = {};
                            $scope.interview = interview;
                           // $scope.interviewStarted = true;
                           // $scope.interviewId = response.data.interviewId;

                            var firstLinkInterviewQuestion = populateInterviewQuestionJsonByModule(interview, self.introModule);
                            firstLinkInterviewQuestion.number = "0";
                            firstLinkInterviewQuestion.isProcessed = true;
                            //inserting the first link intro module
                            InterviewsService.saveLinkQuestionAndQueueQuestions(firstLinkInterviewQuestion).then(function(response) {
                                if (response.status === 200) {
                                    InterviewsService.get($scope.interview.interviewId).then(function(response) {
                                        if (response.status == 200) {

                                            $scope.interview = response.data[0];

                                            var question = findNextQuestionQueued($scope.interview);
                                            if (question) {
                                                QuestionsService.findQuestionSingleChildLevel(question.questionId).then(function(response) {
                                                    if (response.status === 200) {
                                                        var ques = response.data[0];

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
		setCharCode = function(){
		    var participant = $rootScope.participant;
            var theDetails = participant.participantDetails;
		    var detail = theDetails.find(detail => detail.detailName === 'CharCode');
		    self.charCode = detail;
		}
 		setYearOfBirth = function(){
 		    var participant = $rootScope.participant;
             var theDetails = participant.participantDetails;
 		    var detail = theDetails.find(detail => detail.detailName === 'YearOfBirth');
 		    self.yearOfBirth = detail;
 		}
 		setGender = function(){
 		    var participant = $rootScope.participant;
             var theDetails = participant.participantDetails;
 		    var detail = theDetails.find(detail => detail.detailName === 'Gender');
 		    self.gender = detail;
 		}
  		setNumberCode = function(){
  		    var participant = $rootScope.participant;
              var theDetails = participant.participantDetails;
  		    var detail = theDetails.find(detail => detail.detailName === 'NumberCode');
  		    self.numberCode = detail;
  		}
  		setComments = function(){
  		    var participant = $rootScope.participant;
              var theDetails = participant.participantDetails;
  		    var detail = theDetails.find(detail => detail.detailName === 'Comments');
  		    self.comments = detail;
  		}
  		setTranscriptSent = function(){
  		    var participant = $rootScope.participant;
              var theDetails = participant.participantDetails;
  		    var detail = theDetails.find(detail => detail.detailName === 'TranscriptSent');
  		    self.transcriptSent = detail;
  		}
  		populateParticipantDetails = function(){
            setPriority();
            setCharCode();
            setYearOfBirth();
            setGender();
            setNumberCode();
            setComments();
            setTranscriptSent();
            groupTheAddresses();
            getOtherParticipantJob();
  		}
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

        function groupTheAddresses(){
            if($rootScope.participant.participantDetails){
                var theDetails = $rootScope.participant.participantDetails;
                var addresses = theDetails.filter(detail => detail.detailName.startsWith('R'));

                self.groupedAddresses = {};

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
            }
        }

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
		function viewParticipantAddress() {
		    saveParticipant();
            $scope.addParticipantAddressTab($rootScope.participant.reference)
        }
        self.viewParticipantAddress = viewParticipantAddress;
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

			saveParticipant();

			groupTheAddresses();
			//ParticipantsService.save($rootScope.participant).then(function(response) {
			//	if (response.status === 200) {
			//		console.log('it works');
			//		populateParticipantAddresses($rootScope.participant.idParticipant);
			//	}
			//});


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
                    groupTheAddresses();
                }
            });
        }
        self.removeAddress = removeAddress;

        function addJobHistoryParticipant() {
            saveParticipant();
            var theReferenceNumber = $rootScope.participant.reference;

            var nextJobHistoryReferenceNumber = splitAndIncrementLast(theReferenceNumber);

            $scope.addParticipantJobTab(nextJobHistoryReferenceNumber);

        }
        self.addJobHistoryParticipant = addJobHistoryParticipant;

        function getOtherParticipantJob() {
			ParticipantsService.getByReferenceNumberPrefix(self.referenceNumberPrefix).then(function(response) {
				if (response.status === 200) {
					var otherParticipantJobs = response.data;
					self.otherParticipantJobs = otherParticipantJobs;
					populateMappingDetails(self.otherParticipantJobs);
				}
			});
		}

		function saveParticipantMappings() {
            var participants = self.otherParticipantJobs.filter(obj => obj.mappedTo !== "NONO");
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

                                var ques = self.introModule.nodes[0];
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
            /*
            var participantRecord = self.otherParticipantJobs.find(obj => obj.mappedTo === "NONO");
            var detail = participantRecord.participantDetails.find(detail => detail.detailName === 'Comments');
            detail.detailValue = self.comments;
            participantRecord.status = self.participantStatus;
            ParticipantsService.save(participantRecord).then(function(response) {
                if (response.status === 200) {
                    console.log('it works');
                }
            });
            */
        }
        self.saveParticipantMappings = saveParticipantMappings;

        function populateMappingDetails(participants) {
		    $scope.sameAsList = [];
			//var firstParticipant = participants[0];
			//populateParticipantDetails(firstParticipant.idParticipant,true);
			participants.reduce(function(p, data) {
				return p.then(function() {
				    let array = data.reference.split('-');
				    var jobNumber = "";
				    if(array.length>1){
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


        /* utilities */
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
        function findNextQuestionQueued(interview) {
            var questionsList = angular.copy(interview.questionHistory);
            questionsList = _.reverse(questionsList);
            var question = _.find(questionsList, function(queuedQuestion) {
                return !queuedQuestion.processed && !queuedQuestion.deleted;
            });
            return question;
        }
        function populateInterviewQuestionJsonByModule(interview, module) {
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
        function getJobModulePriority(participant) {
            var jobModulePriority = { detailValue: "" };
            if (participant.participantDetails != undefined) {
                if(participant.participantDetails.length>0) {
                    jobModulePriority = participant.participantDetails.find(jobModulePriority => jobModulePriority.detailName === 'Priority');
                }
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
                jobStartedIn = participant.participantDetails.find(detail => (detail.detailName === 'FromDate (M/YYYY)'));
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
        function getJobModule(participant) {
            var jobModuleName = { name: "" };
            var surveyLink = getSurveyLink(participant);
            if (surveyLink != undefined) {
                jobModuleName = $scope.qualtricsSurveyLinks.find(jobModuleName => jobModuleName.surveyLink === surveyLink.text);
            }
            return jobModuleName.name;
        }
        function getSurveyLink(participant) {
            var surveyLink = "";
            if (participant.notes != undefined) {
                surveyLink = participant.notes.find(qualtricsSurveyLinkNote => (qualtricsSurveyLinkNote.type === 'AMRSurveyLink' && !qualtricsSurveyLinkNote.deleted));
            }
            return surveyLink;
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
        function allMapped(participants) {
            for (let participant of participants) {
                if (!participant.mappedTo) {
                    return false;
                }
            }
            return true;
        }
        function findQualtricsSurveyLink(moduleName) {
            var qualtricsSurveyLink = { surveyLink: "" };
            if (moduleName != undefined) {
                qualtricsSurveyLink = $scope.qualtricsSurveyLinks.find(qualtricsSurveyLink => qualtricsSurveyLink.name === moduleName);

            }
            return qualtricsSurveyLink.surveyLink;
        }
        function setReferenceNumberPrefix(string) {
            if (string !== undefined) {
                let array = string.split("-");
                self.referenceNumberPrefix = array[0];
            }
        }


        self.hideShowParticipantDetails = function() {
          $rootScope.showParticipantDetails = !$rootScope.showParticipantDetails;
        };


        self.hideShowResidentialHistory = function() {
          $rootScope.showResidentialHistory = !$rootScope.showResidentialHistory;
        };


        self.hideShowOccupationalHistory = function() {
          $rootScope.showOccupationalHistory = !$rootScope.showOccupationalHistory;
        };
		if($rootScope.showResidentialHistory == undefined){
			$rootScope.showResidentialHistory = true; 
		}
		if($rootScope.showParticipantDetails == undefined){
			$rootScope.showParticipantDetails = true; 
		}
		if($rootScope.showOccupationalHistory == undefined){
			$rootScope.showOccupationalHistory = true; 
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
        $scope.sameAsList = [];
		$scope.jobModules = ['ASMM',
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
			{ name: 'NONO', surveyLink: 'SV_eXo3qHX2ImA3ew6' },
			{ name: 'ASMM', surveyLink: 'SV_ag70P403slO7TAq' },
			{ name: 'AREM', surveyLink: 'SV_3Od2wz0m3yae1Aa' },
			{ name: 'ANEC', surveyLink: 'SV_37YKkJ74ay8zank' },
			{ name: 'AUTO', surveyLink: 'SV_bPFthxTUZNDLQ2i' },
			{ name: 'CEMT', surveyLink: 'SV_d07maUaDKeorwcC' },
			{ name: 'FURN', surveyLink: 'SV_6oicogSLDw0k7Qy' },
			{ name: 'INSU', surveyLink: 'SV_3Kj9ChYCAb4dfbo' },
			{ name: 'LAND', surveyLink: 'SV_8u1ypPnvbsP34iy' },
			{ name: 'TEXT', surveyLink: 'SV_dil1gEkXYAGZWV8' },
			{ name: 'TIPW', surveyLink: 'SV_9Xec2Rm2z6ItV78' },
			{ name: 'TRAD', surveyLink: 'SV_bPZBJn5TRD36RvM' },
			{ name: 'WATE', surveyLink: 'SV_7VhkvA4exkT47X0' },
			{ name: 'JEWL', surveyLink: 'SV_eSgsw77WYSqsSDI' },
			{ name: 'LAUN', surveyLink: 'SV_5aJl9yY5K90kLz0' },
			{ name: 'UnExposed', surveyLink: '' }
		];
	}
})();