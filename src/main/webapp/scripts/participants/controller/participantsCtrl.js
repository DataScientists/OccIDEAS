(function() {
	angular.module('occIDEASApp.Participants').controller('ParticipantsCtrl',
		ParticipantsCtrl);

	ParticipantsCtrl.$inject = ['ParticipantsService', 'NgTableParams',
		'$state', '$scope', '$rootScope', '$filter', 'data', 'InterviewsService',
		'$resource', 'NotesService', '$mdDialog', '$sessionStorage'
		, '$translate', 'NodeLanguageService', 'AuthenticationService', 'ngToast', '$sce', '$q'];

	function ParticipantsCtrl(ParticipantsService, NgTableParams, $state,
		$scope, $rootScope, $filter, data, InterviewsService, $resource, NotesService,
		$mdDialog, $sessionStorage, $translate, NodeLanguageService, auth, ngToast, $sce, $q) {
		var self = this;
		$scope.data = data;
		$scope.$root.tabsLoading = false;
		$scope.$storage = $sessionStorage;
		$scope.isLangEnabledOnLoad = angular.copy($scope.$storage.langEnabled);

		$rootScope.qualtricsSurveyLinks = [
			{ name: 'NONO', surveyLink: 'SV_eXo3qHX2ImA3ew6' },
			{ name: 'LAND', surveyLink: 'SV_8u1ypPnvbsP34iy' },
			{ name: 'TRAD', surveyLink: 'SV_bPZBJn5TRD36RvM' },
			{ name: 'AsMM', surveyLink: 'SV_0VP4BSv9Dm5CBdc' },
			{ name: 'UnExposed', surveyLink: '' }
		];

		$scope.interviewStatusList = [{
			'id': 0,
			'title': 'Running'
		},
		{
			'id': 1,
			'title': 'Partial'
		},
		{
			'id': 2,
			'title': 'Completed'
		},
		{
			'id': 3,
			'title': 'To be excluded'
		}
		];
		$scope.getInterviewStatusList = function(column) {
			return $scope.interviewStatusList;
		};

		$scope.$watch('$storage.langEnabled', function(value) {
			if ($scope.$storage.langEnabled && !$scope.isLangEnabledOnLoad) {
				$translate.refresh();
				$scope.getAllLanguage();
			}

		});

		$scope.getAllLanguage = function() {
			NodeLanguageService.getNodeNodeLanguageList().then(function(response) {
				if (response.status == '200') {
					$scope.languages = [];
					$scope.nodeLanguage = response.data;
					var nodeLanguageCopy = angular.copy($scope.nodeLanguage);
					if (nodeLanguageCopy) {
						nodeLanguageCopy = _.uniqBy(nodeLanguageCopy, 'flag');
					}
					if ($sessionStorage.languages) {
						_.each(nodeLanguageCopy, function(nl) {
							var langToPush = _.find($sessionStorage.languages, function(o) {
								return o.flag == nl.flag;
							});
							$scope.languages.push(langToPush);
						});
					}
					//					var english = {
					//						id: -1,
					//						language: 'EN'
					//					}
					//					$scope.languages.unshift(english);
					$scope.selectLanguage = {};
					//					$scope.selectLanguage.selected = _.find($scope.languages,function(lng){
					//						return lng.language == 'GB';
					//					});
					safeDigest($scope.selectLanguage);
				}
			})
		};

		if ($scope.$storage.langEnabled) {
			$translate.refresh();
			$scope.getAllLanguage();
		}

		self.changeNodeLanguage = function(data) {
			$translate.refresh();
			$translate.use(data.selected.language);
			$scope.selectLanguage.select = data.selected;
		};


		$scope.setSelectedInterview = function(interview) {
			$scope.selectedInterview = interview;
		};
		$scope.awesIdMaxSize = $sessionStorage.awesIdLength;
		$scope.awesIdPrefix = $sessionStorage.awesIdPrefix;
		$scope.awesIdSize = 0;
		$scope.participantFilter = {
			idParticipant: null,
			interviewId: null,
			reference: null,
			status: null,
			pageNumber: null,
			size: null
		};

		self.showDelColumn = false;

		$scope.filterAndValidate = function(event) {
			var elem = angular.element(document.querySelector('#awesid'));
			var counter = angular.element(document
				.querySelector('#awesidcounter'));
			var label = angular.element(document.querySelector('#awesidlabel'));
			if (event.which === 13 || event.which === 32) {
				if (awesIdIsValid(elem.val())) {
					if ($scope.$storage.langEnabled) {
						self.add($scope.selectLanguage);
					} else {
						self.add();
					}

				} else {
					counter.append("Please enter a valid AWES ID");
				}
			} else {
				if (!awesIdIsValid(elem.val())) {
					elem.addClass("awesidwarning");
					label.addClass("awesidwarning");
					counter.addClass("awesidwarning");
				} else {
					elem.removeClass("awesidwarning");
					label.removeClass("awesidwarning");
					counter.removeClass("awesidwarning");
				}
			}
		};

		function awesIdIsValid(awesId) {
			$scope.searchAWESID = '';
			var retValue = false;
			$scope.awesIdSize = awesId.length;
			if ($scope.awesIdSize == $scope.awesIdMaxSize) {
				if (_.startsWith(awesId, $scope.awesIdPrefix)) {
					retValue = true;
					$scope.searchAWESID = awesId;
				}
			}
			return retValue;
		}

		self.isDeleting = false;
		var dirtyCellsByRow = [];
		var invalidCellsByRow = [];

		if (auth.userHasPermission(['ROLE_ADMIN'])) {
			//Show the delete column for admin only
			self.showDelColumn = true;
		}

		if (auth.userHasPermission(['ROLE_SELFINTERVIEWER'])) {
			//Show the delete column for admin only
			self.isSelfInterviewer = true;
		} else {
			self.isSelfInterviewer = false;
		}
		self.tableParams = new NgTableParams(
			{
				page: 1,
				count: 20
			},
			{
				getData: function(params) {
					var currentPage = $scope.participantFilter.pageNumber;
					$scope.participantFilter.idParticipant = params
						.filter().idParticipant;
					$scope.participantFilter.interviewId = params
						.filter().idinterview;
					$scope.participantFilter.reference = params
						.filter().reference;
					/*
					if(lengthGreaterThan2(params.filter().status)){
					  if(params.filter().status.startsWith('run') ){
						$scope.participantFilter.status = 0
					  }else if(params.filter().status.startsWith('par') ){
						$scope.participantFilter.status = 1
					  }else if(params.filter().status.startsWith('com') ){
						$scope.participantFilter.status = 2
					  }else if(params.filter().status.startsWith('tob') ){
						$scope.participantFilter.status = 3
					  }
					}
					*/
					$scope.participantFilter.status = params.filter().status;
					$scope.participantFilter.pageNumber = params.page();
					$scope.participantFilter.size = params.count();
					params.goToPageNumber = null;
					var participantFilter = $scope.participantFilter;
					if ((!self.tableParams.settings().dataset
						|| (participantFilter.pageNumber != currentPage)
						|| participantFilter.idParticipant
						|| participantFilter.interviewId
						|| participantFilter.reference
						|| participantFilter.status
						|| (participantFilter.status === 0)
						|| ifEmptyFilter(params.filter()))
						&& !self.isSelfInterviewer) {
						return ParticipantsService.getPaginatedParticipantList(participantFilter).then(function(response) {
							if (response.status == '200') {
								var data = response.data.content;
								console.log("Data get list from getParticipants ajax ...");
								self.originalData = angular.copy(data);
								self.tableParams.settings().dataset = data;
								self.tableParams.shouldGetData = false;
								$scope.totalSize = response.data.totalSize;
								self.tableParams.total(response.data.totalSize);
								$scope.currentParticipants = data;
								return data;
							}
						});
					}
				},
			});
		self.tableParams.shouldGetData = true;
		self.cancel = cancel;
		self.deletePrompt = deletePrompt;
		self.deleteRow = deleteRow;
		self.save = save;
		self.add = add;
		self.addCaseControlParticipant = addCaseControlParticipant;
		self.showParticipantDetailsTab = showParticipantDetailsTab;
		self.showParticipantMappingTab = showParticipantMappingTab;
		self.cancelDelete = cancelDelete;

		self.toggleIsDeleting = toggleIsDeleting;

		function toggleIsDeleting() {
			if (self.isDeleting) {
				self.isDeleting = false;
			} else {
				self.isDeleting = true;
			}
		}

		function add(data) {
			if (awesIdIsValid($scope.searchAWESID)) {
				if ($scope.$storage.langEnabled) {
					if (!data.selected) {
						$translate.refresh();
						$translate.use('GB');
					}
				}
				InterviewsService.checkReferenceNumberExists($scope.searchAWESID).then(function(data) {
					if (data.status == 200) {
						if (confirm("This Participant ID has already been used. Would you like to add a duplicate?")) {
							$scope.addInterviewTabInterviewers(
								-1, $scope.searchAWESID);
						}
					} else if (data.status == 204) {
						$scope.addInterviewTabInterviewers(-1,
							$scope.searchAWESID);
					} else {
						var msg = "Error occured during checkReferenceNumberExists.";
						ngToast.create({
							className: 'danger',
							content: msg,
							animation: 'slide'
						});
					}
				})
			} else {
				ngToast.create({
					className: 'danger',
					content: 'You need to add a valid AWES ID before you can start',
					animation: 'slide'
				});
			}
		}
		function showParticipantDetailsTab(participant) {
			$scope.addParticipantDetailsTab(-1, participant.reference, true, participant.idinterview);
		}
		function showParticipantMappingTab(participant) {
			$scope.addParticipantMappingTab(participant.reference, participant.idinterview);
		}
		function addCaseControlParticipant() {
			if (awesIdIsValid($scope.searchAWESID)) {
				$scope.searchAWESID = $scope.searchAWESID + "-P0";
				InterviewsService.checkReferenceNumberExists($scope.searchAWESID).then(function(data) {
					if (data.status == 200) {
						if (confirm("This Participant ID has already been used. ")) {
							//$scope.addInterviewTabInterviewers(
							//  -1, $scope.searchAWESID);
						}
					} else if (data.status == 204) {
						$scope.addParticipantDetailsTab(-1, $scope.searchAWESID, false);
					} else {
						var msg = "Error occured during checkReferenceNumberExists.";
						ngToast.create({
							className: 'danger',
							content: msg,
							animation: 'slide'
						});
					}
				})
			} else {
				ngToast.create({
					className: 'danger',
					content: 'You need to add a valid AWES ID before you can start',
					animation: 'slide'
				});
			}
		}

		function cancel(row, rowForm) {
			var originalRow = resetRow(row, rowForm);
			if (row.idNode) {
				angular.extend(row, originalRow);
			} else {
				_.remove(self.tableParams.settings().dataset, function(item) {
					return row === item;
				});
				self.tableParams.shouldGetData = false;
				self.tableParams.reload().then(function(data) {
					if (data.length === 0 && self.tableParams.total() > 0) {
						self.tableParams.page(self.tableParams.page() - 1);
						self.tableParams.reload();
					}
				});
			}
		}

		function deletePrompt(participant) {

			$scope.participantToDelete = participant;
			$mdDialog.show({
				scope: $scope.$new(),
				templateUrl: 'scripts/participants/view/deleteParticipantDialog.html',
				clickOutsideToClose: true
			});
		}

		function cancelDelete() {
			$mdDialog.cancel();
		}

		function deleteRow() {
			row = $scope.participantToDelete;
			row.deleted = 1;
			var data = ParticipantsService
				.deleteParticipant(row)
				.then(
					function(response) {
						if (response.status === 200) {
							console.log('Participant was deleted!');
							self.tableParams.shouldGetData = true;
							self.tableParams
								.reload()
								.then(
									function(data) {
										if (data.length === 0
											&& self.tableParams
												.total() > 0) {
											self.tableParams
												.page(self.tableParams
													.page() - 1);
											self.tableParams
												.reload();
										}
									});
						}
					});
			_.remove(self.tableParams.settings().dataset, function(item) {
				return row === item;
			});
			self.tableParams.shouldGetData = false;
			self.tableParams.reload().then(function(data) {
				if (data.length === 0 && self.tableParams.total() > 0) {
					self.tableParams.page(self.tableParams.page() - 1);
					self.tableParams.reload();
				}
			});

			$mdDialog.cancel();
		}

		function resetRow(row, rowForm) {
			row.isEditing = false;
			rowForm.$setPristine();
			self.tableTracker.untrack(row);
			return window._.find(self.originalData, {
				idNode: row.idNode
			});
		}

		function save(row, rowForm) {
			self.isEditing = false;
			ParticipantsService.save(row).then(function(response) {
				if (response.status === 200) {
					console.log('Participant Save was Successful!');
					self.tableParams.shouldGetData = true;
					self.tableParams.reload().then(function(data) {
						if (data.length === 0 && self.tableParams.total() > 0) {
							self.tableParams.page(self.tableParams.page() - 1);
							self.tableParams.reload();
							$location.hash("");
							$anchorScroll();
						}
					});
				}
			});
		}

		function setInvalid(isInvalid) {
			self.$invalid = isInvalid;
			self.$valid = !isInvalid;
		}

		function untrack(row) {
			_.remove(invalidCellsByRow, function(item) {
				return item.row === row;
			});
			_.remove(dirtyCellsByRow, function(item) {
				return item.row === row;
			});
			setInvalid(invalidCellsByRow.length > 0);
		}

		function ifEmptyFilter(filter) {
			if ((!filter.idParticipant || filter.idParticipant.length == 0)
				&& (!filter.idinterview || filter.idinterview.length == 0)
				&& (!filter.reference || filter.reference.length == 0)
				&& (!filter.status || filter.status.length == 0)) {
				return true;
			}
		}

		$scope.cancelNotes = function() {
			$mdDialog.cancel();
		};

		self.showAMRInterview = function(participant) {
			ParticipantsService.findParticipant(participant.idParticipant).then(function(response) {
				if (response.status === 200) {
					var participant = response.data[0];
					$scope.activeParticipant = participant;

					var qualtricsSurveyLinkNote = getSurveyLink($scope.activeParticipant);
					var newScope = $scope.$new();
					newScope.activeParticipant = $scope.activeParticipant;

					newScope.notes = $scope.activeParticipant.notes;


					newScope.qualtricsLink = 'https://curtin.au1.qualtrics.com/jfe/form/' + qualtricsSurveyLinkNote.text + '?AMRID=' + $scope.activeParticipant.reference;
					newScope.trustedIframeUrl = $sce.trustAsResourceUrl(newScope.qualtricsLink);
					if (newScope.notes.length > 0) {
						$mdDialog.show({
							scope: newScope,
							templateUrl: 'scripts/notes/view/amrInterviewDialog.html',
							parent: angular.element(document.body),
							clickOutsideToClose: true
						})
					}


				}
			});
		}
		self.showAMRInterviewOld = function(idInterview) {
			var newScope = $scope.$new();
			//newScope.notes = response.data;
			$mdDialog.show({
				scope: newScope,
				width: '1000px',
				height: '1000px',
				templateUrl: 'scripts/notes/view/amrInterviewDialog.html',

				clickOutsideToClose: true
			});
		}
		$scope.showNotes = function(idInterview) {
			NotesService.getNotesByInterviewId(idInterview).then(function(response) {
				if (response.status == '200') {
					var newScope = $scope.$new();
					newScope.notes = response.data;
					if (newScope.notes.length > 0) {
						$mdDialog.show({
							scope: newScope,
							templateUrl: 'scripts/notes/view/noteDialog.html',
							parent: angular.element(document.body),
							clickOutsideToClose: true
						}).then(
							function(answer) {
								$scope.status = 'You said the information was "'
									+ answer + '".';
							}, function() {
								$scope.status = 'You cancelled the dialog.';
							});
					} else {
						ngToast.create({
							className: 'danger',
							content: 'No notes for this interview',
							animation: 'slide'
						});
					}
				}
			});
		};

		self.tableParams.goTo = function(event) {
			if (event.keyCode == 13
				&& self.tableParams.goToPageNumber != null
				&& !isNaN(self.tableParams.goToPageNumber)) {
				self.tableParams.page(self.tableParams.goToPageNumber);
				self.tableParams.reload();
			}
		};
		function createAMRBatchParticipant(data) {


			ParticipantsService.createAMRBatchParticipant(data).then(function(response) {
				if (response.status === 200) {
					self.tableParams.reload();
				}
			});
		}
		$scope.importFromExcel = function(workbook) {
			var worksheet = workbook.Sheets[workbook.SheetNames[0]];
			/*Skip the first row as it is header*/
			var range = XLSX.utils.decode_range(worksheet['!ref']);
			range.s.r = 1;
			worksheet['!ref'] = XLSX.utils.encode_range(range);
			var data = XLSX.utils.sheet_to_json(worksheet, { header: 1 });

			var count = 0;
			var jobCount = 0;

			const allAmrJobs = [];
			var amrReferenceNumber = '';
			for (const row of data) {
				if (row.length != 0) {
					if (count > 46) {
						break;
					}
					if ((count - 2) % 15 == 0) {
						let amrData = {
							referenceNumber: '',
							refNumberJob: '',
							notes: '',
							priority: '',
							jobModuleID: '',
							interviews: []
						};
						amrReferenceNumber = row[0];
						amrData.referenceNumber = amrReferenceNumber;
						amrData.refNumberJob = amrReferenceNumber + '-0';
						amrData.notes = 'These questions are about NONO';

						let participant = {
							reference: amrData.refNumberJob,
							notes: [],
							interviews: []
						};
						participant.notes.push({
							text: amrData.notes,
							type: 'AMR'
						});
						participant.notes.push({
							text: '75456',
							type: 'AMRSurveyLink'
						});

						allAmrJobs.push(participant);
					}
					var priority = row[8];
					if (angular.isUndefined(priority)) {
						count++;
						continue;
					}
					if (priority == 99) {
						count++;
						continue;
					}
					if (priority == 'Priority') {
						count++;
						continue;
					}

					if (priority.length != 0) {
						let amrData = {
							referenceNumber: '',
							refNumberJob: '',
							notes: '',
							priority: '',
							jobModuleID: '',
							interviews: []
						};
						amrData.referenceNumber = amrReferenceNumber;
						amrData.refNumberJob = amrData.referenceNumber + '-' + row[0];
						var moduleId = row[10];
						var moduleName = '';
						if (moduleId == '75442') {  //TRAD
							surveyId = 'SV_3lsCyfioMEqPlgq';
							moduleName = 'TRAD';
						} else if (moduleId == '75439') { //LAND
							surveyId = 'SV_41PCl5vY1gaiGyy';
							moduleName = 'LAND';
						} else if (moduleId == '75443') { //WATE
							surveyId = 'SV_cI9hrld1AtS94hM';
							moduleName = 'WATE';
						}

						amrData.notes = moduleName + ':Priority:' + priority + ':The next set of questions refer to when you where working as a '
							+ row[5] + ' with '
							+ row[3] + ' that job started in '
							+ row[1];
						//amrData.priority = row[8];
						amrData.jobModuleID = row[10];
						//createAMRParticipant(amrData);
						let participant = {
							reference: amrData.refNumberJob,
							notes: [],
							interviews: []
						};
						participant.notes.push({
							text: amrData.notes,
							type: 'AMR'
						});

						participant.notes.push({
							text: row[10],
							type: 'AMRSurveyLink'
						});
						allAmrJobs.push(participant);
					}



				}


				count++;
				jobCount++;


			}
			createAMRBatchParticipant(allAmrJobs);
			/*
					for(const row of data){
						if(row.length != 0){
							
							if((count == 2)||(count == 17)||(count == 32)){
								
								amrData.referenceNumber = row[0];
								amrData.refNumberJob = row[0]+'-0';
								createAMRParticipant(amrData);
							}
							if((count >= 5) && (count <=13)){
								amrData.refNumberJob = amrData.referenceNumber+'-'+row[0];
								amrData.notes = 'The next set of questions refer to when you where working as a '
								+row[5]+ ' with '
								+row[3]+ ' that    job started in '				
								+row[1];
								createAMRParticipant(amrData);
							}
							
						}
						
						count++;
						jobCount++;
					}
			 */

			$mdDialog.cancel();
			self.tableParams.reload();
		};
		self.showImportFromExcel = function() {
			$mdDialog.show({
				scope: $scope,
				preserveScope: true,
				templateUrl: 'scripts/participants/view/importFromExcel.html',
				clickOutsideToClose: true
			});
		};
		$scope.closeDialog = function() {
			$mdDialog.cancel();
		}
		self.closeDialog = function() {
			$mdDialog.cancel();
		}

		function createAMRParticipant(data) {
			var participant = {
				reference: data.refNumberJob,
				interviews: []
			};

			ParticipantsService.createParticipant(participant).then(function(response) {
				if (response.status === 200) {
					var participantData = response.data;

					var interview = {};
					interview.participant = participantData;
					interview.module = $scope.data[0];
					interview.referenceNumber = participantData.reference + '-0';

					interview.notes = [];

					interview.notes.push({

						text: "Here will be all the details about this interview",
						type: 'Interviewer'
					});
					InterviewsService.startInterview(interview).then(function(response) {
						if (response.status === 200) {
							var idinterview = response.data.interviewId;
							interview.interviewId = idinterview;

							interview.notes = [];

							interview.notes.push({
								interviewId: idinterview,
								text: "All the details will be here",
								type: 'Interviewer'
							});
							saveInterview(interview);

						}
					});
				}
			});
		}

		function getSurveyLink(participant) {
			var surveyLink = "";
			if (participant.notes != undefined) {
				surveyLink = participant.notes.find(qualtricsSurveyLinkNote => (qualtricsSurveyLinkNote.type === 'AMRSurveyLink' && !qualtricsSurveyLinkNote.deleted));
			}
			return surveyLink;
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
		self.getSurveyLink = getSurveyLink;
		function getJobModule(participant) {
			var jobModuleName = { name: "" };
			var surveyLink = getSurveyLink(participant);
			if (surveyLink != undefined) {
				jobModuleName = $rootScope.qualtricsSurveyLinks.find(jobModuleName => jobModuleName.surveyLink === surveyLink.text);
			}
			return jobModuleName.name;
		}
		self.getJobModule = getJobModule;
		function getJobModulePriority(participant) {
			var jobModulePriority = { detailValue: "" };
			if (participant.participantDetails != undefined) {
				jobModulePriority = participant.participantDetails.find(jobModulePriority => jobModulePriority.detailName === 'Priority');
			}
			return jobModulePriority.detailValue;
		}
		function saveInterview(interview) {

			InterviewsService.save(interview).then(function(response) {
				if (response.status === 200) {


				}
			});
		}

		$scope.showMapping = function(participant) {
			ParticipantsService.findParticipant(participant.idParticipant).then(function(response) {
				if (response.status === 200) {


					var participantFull = response.data[0];
					participant.mappedTo = getJobModule(participantFull);
					participant.mappedPriority = getJobModulePriority(participantFull);


				}
			});
			showAllMapping();
		};

		function showAllMapping() {
			$scope.currentParticipants.reduce(function(p, data) {
				return p.then(function() {
					$scope.interviewIdInProgress = data.idinterview;
					$scope.counter++;
					$scope.interviewCount = $scope.counter;
					ParticipantsService.findParticipant(data.idParticipant).then(function(response) {
						if (response.status === 200) {


							var participantFull = response.data[0];
							data.mappedTo = getJobModule(participantFull);
							data.mappedPriority = getJobModulePriority(participantFull);

						}
					});
				});
			}, $q.when(true)).then(function(finalResult) {
				console.log('finish loading rules');

			}, function(err) {
				console.log('error');
			});
		}
		$scope.showAllMapping = showAllMapping;
		function showAllInterviewLinks() {
			$scope.currentParticipants.reduce(function(p, data) {
				return p.then(function() {
					$scope.interviewIdInProgress = data.idinterview;
					$scope.counter++;
					$scope.interviewCount = $scope.counter;
					ParticipantsService.findParticipant(data.idParticipant).then(function(response) {
						if (response.status === 200) {


							var participantFull = response.data[0];
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

							console.log(data.interviewDetails);

						}
					});
				});
			}, $q.when(true)).then(function(finalResult) {
				console.log('finish loading rules');

			}, function(err) {
				console.log('error');
			});
		}
		$scope.showAllInterviewLinks = showAllInterviewLinks;
		function updateParticipantStatus(event, participant) {
			participant.status = 2;
			ParticipantsService.save(participant).then(function(response) {
				if (response.status === 200) {
					console.log('it works');
				}
			});
		}
		self.updateParticipantStatus = updateParticipantStatus;

	}
})();
