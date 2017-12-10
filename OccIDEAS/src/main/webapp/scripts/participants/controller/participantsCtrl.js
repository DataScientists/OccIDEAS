(function() {
	angular.module('occIDEASApp.Participants').controller('ParticipantsCtrl',
			ParticipantsCtrl);

	ParticipantsCtrl.$inject = [ 'ParticipantsService', 'NgTableParams',
			'$state', '$scope', '$filter', 'data', 'InterviewsService',
			'$resource','NotesService','$mdDialog', '$sessionStorage'
			,'$translate','NodeLanguageService','AuthenticationService'];
	function ParticipantsCtrl(ParticipantsService, NgTableParams, $state,
			$scope, $filter, data, InterviewsService, $resource,NotesService,
			$mdDialog, $sessionStorage,$translate,NodeLanguageService,auth) {
		var self = this;
		$scope.data = data;
		$scope.$root.tabsLoading = false;
		$scope.$storage = $sessionStorage;
		$scope.isLangEnabledOnLoad = angular.copy($scope.$storage.langEnabled);
		
		$scope.interviewStatusList = [{
			'id':0,
			'title':'Running'
		},
		{
			'id':1,
			'title':'Partial'
		},
		{
			'id':2,
			'title':'Completed'
		},
		{
			'id':3,
			'title':'To be excluded'
		}
		];
		$scope.getInterviewStatusList = function(column) {
			return $scope.interviewStatusList;
		};
		
		$scope.$watch('$storage.langEnabled', function(value) {	   
			if($scope.$storage.langEnabled && !$scope.isLangEnabledOnLoad){
				$translate.refresh();
				$scope.getAllLanguage();
			}
			
		});
		
		$scope.getAllLanguage = function(){
			NodeLanguageService.getNodeNodeLanguageList().then(function(response){
				if(response.status == '200'){
					$scope.languages = [];
					$scope.nodeLanguage = response.data;
					var nodeLanguageCopy = angular.copy($scope.nodeLanguage);
					if(nodeLanguageCopy){
						nodeLanguageCopy = _.uniqBy(nodeLanguageCopy, 'flag');
					}
					if($sessionStorage.languages){
			        	_.each(nodeLanguageCopy,function(nl){
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
		
		if($scope.$storage.langEnabled){
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
		}
		$scope.awesIdMaxSize = $sessionStorage.awesIdLength;
		$scope.awesIdPrefix = $sessionStorage.awesIdPrefix;
		$scope.awesIdSize = 0;
		$scope.participantFilter = {
			idParticipant : null,
			interviewId : null,
			reference : null,
			status : null,
			pageNumber : null,
			size : null
		};
		
		self.showDelColumn = false;

		$scope.filterAndValidate = function(event) {
			var elem = angular.element(document.querySelector('#awesid'));
			var counter = angular.element(document
					.querySelector('#awesidcounter'));
			var label = angular.element(document.querySelector('#awesidlabel'));
			if (event.which === 13 || event.which === 32) {
				if (awesIdIsValid(elem.val())) {
					if($scope.$storage.langEnabled){
						self.add($scope.selectLanguage);
					}else{
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
		}
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
		
		if(auth.userHasPermission(['ROLE_ADMIN'])){
			//Show the delete column for admin only
			self.showDelColumn = true;	
		}
		
		self.tableParams = new NgTableParams(
				{
					page : 1,
					count : 10
				},
				{
					getData : function(params) {
						var currentPage = $scope.participantFilter.pageNumber;
						$scope.participantFilter.idParticipant = lengthGreaterThan2(params
								.filter().idParticipant);
						$scope.participantFilter.interviewId = lengthGreaterThan2(params
								.filter().idinterview);
						$scope.participantFilter.reference = lengthGreaterThan2(params
								.filter().reference);
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
						
						$scope.participantFilter.pageNumber = params.page();
						$scope.participantFilter.size = params.count();
						params.goToPageNumber = null;
						var participantFilter = $scope.participantFilter;
						if (!self.tableParams.settings().dataset
								|| (participantFilter.pageNumber != currentPage)
								|| participantFilter.idParticipant
								|| participantFilter.interviewId
								|| participantFilter.reference
								|| participantFilter.status
								|| (participantFilter.status===0)
								|| ifEmptyFilter(params.filter())) {
							return ParticipantsService.getPaginatedParticipantList(participantFilter).then(function(response) {
									if (response.status == '200') {
										var data = response.data.content;
										console.log("Data get list from getParticipants ajax ...");
										self.originalData = angular.copy(data);
										self.tableParams.settings().dataset = data;
										self.tableParams.shouldGetData = false;
										$scope.totalSize = response.data.totalSize;
										self.tableParams.total(response.data.totalSize);
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
				if($scope.$storage.langEnabled){
					if(!data.selected){
						$translate.refresh();
			      		$translate.use('GB');
					}
				}
				
				InterviewsService
						.checkReferenceNumberExists($scope.searchAWESID)
						.then(
								function(data) {
									if (data.status == 200) {
										if (confirm("This AWES ID has already been used. Would you like to add a duplicate?")) {
											$scope.addInterviewTabInterviewers(
													-1, $scope.searchAWESID);
										}
									} else if (data.status == 204) {
										$scope.addInterviewTabInterviewers(-1,
												$scope.searchAWESID);
									} else {
										alert("Error occured during checkReferenceNumberExists.");
									}
								})
			} else {
				alert("You need to add a valid AWES ID before you can start.");
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
				scope : $scope.$new(),
				templateUrl : 'scripts/participants/view/deleteParticipantDialog.html',
				clickOutsideToClose:true
			});
		};
		
		function cancelDelete() {
			$mdDialog.cancel();
		};
		
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
				idNode : row.idNode
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

		$scope.showNotes = function(idInterview) {
			NotesService.getNotesByInterviewId(idInterview).then(function(response){
			if(response.status == '200'){	
			var newScope = $scope.$new();
			newScope.notes = response.data;
			if(newScope.notes.length > 0){
			$mdDialog.show({
				scope : newScope,
				templateUrl : 'scripts/notes/view/noteDialog.html',
				parent : angular.element(document.body),
				clickOutsideToClose : true
			}).then(
					function(answer) {
						$scope.status = 'You said the information was "'
								+ answer + '".';
					}, function() {
						$scope.status = 'You cancelled the dialog.';
					});
			}else{
				alert("No notes for this interview.");
			}
			}
			});
		}
		
		self.tableParams.goTo = function(event){
	        if(event.keyCode == 13 
	        		&& self.tableParams.goToPageNumber != null
	        		&& !isNaN(self.tableParams.goToPageNumber)){
	        	self.tableParams.page(self.tableParams.goToPageNumber);
	        	self.tableParams.reload();
	        }
	    };
	}
})();
