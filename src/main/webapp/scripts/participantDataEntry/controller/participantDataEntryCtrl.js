(function() {
	angular.module('occIDEASApp.ParticipantDataEntry')
		.controller('ParticipantDataEntryCtrl', ParticipantDataEntryCtrl);

	ParticipantDataEntryCtrl.$inject = ['ParticipantsService', 'InterviewsService', 'QuestionsService',
		'data', 'startWithReferenceNumber',
		'$state', '$scope', '$filter', '$rootScope', '$mdDialog', 'ngToast', '$sessionStorage', '$q'];

	function ParticipantDataEntryCtrl(ParticipantsService, InterviewsService, QuestionsService,
		data, startWithReferenceNumber,
		$state, $scope, $filter, $rootScope, $mdDialog, ngToast, $sessionStorage, $q) {
		var self = this;

        self.introModule = data[0];

		if (startWithReferenceNumber) {
			$scope.referenceNumber = startWithReferenceNumber;
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

                    populateParticipantDetails();

                    var interview = {};
                    interview.participant = participant;
                    interview.module = self.introModule;
                    interview.referenceNumber = $scope.referenceNumber
                    InterviewsService.startInterview(interview).then(function(response) {
                        if (response.status === 200) {
                            interview.interviewId = response.data.interviewId;
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
  		setDetailComment = function(){
  		    var participant = $rootScope.participant;
              var theDetails = participant.participantDetails;
  		    var detail = theDetails.find(detail => detail.detailName === 'DetailComment');
  		    self.detailComment = detail;
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
            setDetailComment();
            setTranscriptSent();
  		}
        function saveParticipant(){
            var participant = $rootScope.participant;
            ParticipantsService.save(participant).then(function(response) {
                if (response.status === 200) {
                    console.log('Saved the Participant');
                }
            });
        }

        self.saveParticipant = saveParticipant;
        $rootScope.saveParticipant = saveParticipant;

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