(function() {
	angular.module('occIDEASApp.ParticipantDataEntry')
		.controller('ParticipantDataEntryCtrl', ParticipantDataEntryCtrl);

	ParticipantDataEntryCtrl.$inject = ['ParticipantsService', 'InterviewsService', 'ParticipantDetailsService',
		'data', 'startWithReferenceNumber',
		'$state', '$scope', '$filter', '$rootScope', '$mdDialog', 'ngToast', '$sessionStorage', '$q'];

	function ParticipantDataEntryCtrl(ParticipantsService, InterviewsService, ParticipantDetailsService,
		data, startWithReferenceNumber,
		$state, $scope, $filter, $rootScope, $mdDialog, ngToast, $sessionStorage, $q) {
		var self = this;

        self.introModule = data[0];

        self.groupedAddresses = {};

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
                            //groupTheAddresses();
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

        self.showResidentialHistory = true; // Initial state: show history

        self.hideShowResidentialHistory = function() {
          self.showResidentialHistory = !self.showResidentialHistory;
        };
        function removeObjectsWithNameStarting(arr, prefix) {
          return arr.filter(obj => !obj.detailName.startsWith(prefix));
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