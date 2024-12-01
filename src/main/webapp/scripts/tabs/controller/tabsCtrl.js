(function() {
	angular.module("occIDEASApp.Tabs").controller("TabsCtrl", TabsCtrl);

	TabsCtrl.$inject = ['$scope', '$state', '$rootScope', '$log', '$stickyState',
		'AuthenticationService', '$sessionStorage', 'runtimeStates'];

	function TabsCtrl($scope, $state, $rootScope, $log, $stickyState, auth, $sessionStorage, runtimeStates) {
		$scope.loading = false;
		$scope.tabOptions = [];
		if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_ADMIN', 'ROLE_ADMIN'])) {
			$scope.tabOptions.push({
				state: "tabs.admin",
				data: ""
			});
		}
		if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_CONTDEV', 'ROLE_ADMIN', 'ROLE_STUDYMANAGER'])) {
			$scope.tabOptions.push({
				state: "tabs.modules",
				data: ""
			});
		}
		if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_CONTDEV', 'ROLE_ADMIN', 'ROLE_STUDYMANAGER'])) {
			$scope.tabOptions.push({
				state: "tabs.fragments",
				data: ""
			});
		}
		if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_CONTDEV', 'ROLE_ADMIN', 'ROLE_STUDYMANAGER'])) {
			$scope.tabOptions.push({
				state: "tabs.agents",
				data: ""
			});
		}
		if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_INTERVIEWER', 'ROLE_INTERVIEWERA', 'ROLE_ALLOCATOR', 'ROLE_DATAENTRY', 'ROLE_ADMIN', 'ROLE_SELFINTERVIEWER', 'ROLE_STUDYMANAGER'])) {
			$scope.tabOptions.push({
				state: "tabs.participants",
				data: ""
			});
			$stickyState.reset('tabs.participants');
		}
		if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_ASSESSOR', 'ROLE_ADMIN', 'ROLE_STUDYMANAGER'])) {
			$scope.tabOptions.push({
				state: "tabs.assessments",
				data: ""
			});
			$stickyState.reset('tabs.assessments');
		}
		if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_ASSESSOR', 'ROLE_ADMIN', 'ROLE_STUDYMANAGER'])) {
			$scope.tabOptions.push({
				state: "tabs.reports",
				data: ""
			});
		}
		/*
		if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_ADMIN'])) {
			$scope.tabOptions.push({
				state: "tabs.jmx",
				data: ""
			});
		}
		*/
		if ($sessionStorage.langEnabled) {
			if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_CONTDEV'])) {
				$scope.tabOptions.push({
					state: "tabs.languageSummary",
					data: ""
				});
			}
		}
		var shouldPassHiddenParam = false;
		$scope.$watch('selectedIndex', function(current, old) {
			var state = null;
			var data = null;

			if ($scope.tabOptions[current]) {
				if ($scope.tabOptions[current].state) {
					$log.info("Navigating to " + $scope.tabOptions[current].state);
					state = $scope.tabOptions[current].state;
				} else {
					if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_CONTDEV', 'ROLE_STUDYMANAGER'])) {
						state = "tabs.modules";
					} else if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_INTERVIEWER', 'ROLE_INTERVIEWERA', 'ROLE_SELFINTERVIEWER', 'ROLE_STUDYMANAGER', 'ROLE_DATAENTRY', 'ROLE_ALLOCATOR'])) {
						state = "tabs.participants";
					} else if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_ASSESSOR', 'ROLE_STUDYMANAGER'])) {
						state = "tabs.assessments";
						$stickyState.reset('tabs.assessments');
					}

				}
				if ($scope.tabOptions[current].data) {
					$log.info("with data: " + $scope.tabOptions[current].data);
					$log.info("with idNode: " + $scope.tabOptions[current].data.row);
					data = $scope.tabOptions[current].data;
				}

			} else {
				if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_CONTDEV', 'ROLE_STUDYMANAGER'])) {
					state = "tabs.modules";
				} else if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_INTERVIEWER', 'ROLE_INTERVIEWERA', 'ROLE_SELFINTERVIEWER', 'ROLE_STUDYMANAGER', 'ROLE_DATAENTRY', 'ROLE_ALLOCATOR'])) {
					state = "tabs.participants";
				} else if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_ASSESSOR', 'ROLE_STUDYMANAGER'])) {
					state = "tabs.assessments";
				}
			}
			$log.info("going to state " + state);
			if ($scope.tabOptions[current].state.startsWith('tabs.firedrules')) {
				$state.go(state, data);
			} else if (shouldPassHiddenParam ||
				$scope.tabOptions[current].state == 'tabs.answerSummary') {
				$state.go(state, { data: data });
			} else {
				$state.go(state, data);
			}
			shouldPassHiddenParam = false;
		});

		var tabs = [];
		tabs.selected = null;
		tabs.previous = null;
		if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_ADMIN', 'ROLE_ADMIN'])) {
			tabs.push({
				title: 'Admin',
				viewName: 'admin@tabs'
			});
		}
		if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_CONTDEV', 'ROLE_ADMIN', 'ROLE_STUDYMANAGER'])) {
			tabs.push({
				title: 'Job Modules',
				viewName: 'modules@tabs',
			});
		}
		if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_CONTDEV', 'ROLE_ADMIN', 'ROLE_STUDYMANAGER'])) {
			tabs.push({
				title: 'Task Modules',
				viewName: 'fragments@tabs'
			});
		}
		if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_CONTDEV', 'ROLE_ADMIN', 'ROLE_STUDYMANAGER'])) {
			tabs.push({
				title: 'Agents',
				viewName: 'agents@tabs'
			});
		}
		if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_INTERVIEWER', 'ROLE_INTERVIEWERA', 'ROLE_ADMIN', 'ROLE_SELFINTERVIEWER', 'ROLE_STUDYMANAGER', 'ROLE_DATAENTRY', 'ROLE_ALLOCATOR'])) {
			tabs.push({
				title: 'Participants',
				viewName: 'participants@tabs'
			});
		}
		if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_ASSESSOR', 'ROLE_ADMIN', 'ROLE_STUDYMANAGER'])) {
			tabs.push({
				title: 'Assessments',
				viewName: 'assessments@tabs'
			});
		}
		if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_ASSESSOR', 'ROLE_ADMIN', 'ROLE_STUDYMANAGER'])) {
			tabs.push({
				title: 'Reports',
				viewName: 'reports@tabs'
			});
		}
		if (auth.isLoggedIn() && $sessionStorage.langEnabled) {
			if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_CONTDEV', 'ROLE_STUDYMANAGER'])) {
				tabs.push({
					title: 'Language Summary',
					viewName: 'languageSummary@tabs'
				});
			}
		}
		/*
		if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_ADMIN'])) {
		  tabs.push({
				title: 'JMeter',
				viewName: 'jmx@tabs'
			});
		}
		*/
		$scope.tabs = tabs;
		$scope.selectedIndex = 0;

		$scope.closeAndSwitchToParticipantsTab = function() {
			var index = angular.copy($scope.selectedIndex);
			tabs.splice(index, 1);
			$scope.tabOptions.splice(index, 1);
			$scope.selectedIndex = 3;
		};

		$scope.addLanguageTab = function() {
			var tabTitle = "Language ";
			var state = "tabs.language";
			$stickyState.reset(state);
			if (!checkIfTabIsOpen(tabs, tabTitle)) {
				tabs.push({
					title: tabTitle,
					viewName: 'language@tabs',
					canClose: true,
					disabled: false
				});
				$scope.tabOptions.push({
					state: state,
					data: {}
				});
			}
		};

		$scope.openAnswerSummaryTab = function(node, moduleName, interviewId) {
			var tabTitle = "Answer Summary-" + node.idNode;
			var state = "tabs.answerSummary";
			var nodeCode = node.header + " " + node.number;
			$stickyState.reset(state);
			if (!checkIfTabIsOpen(tabs, tabTitle)) {
				tabs.push({
					title: tabTitle,
					viewName: 'answerSummary@tabs',
					canClose: true,
					disabled: false
				});
				$scope.tabOptions.push({
					state: state,
					data: {
						answerId: node.idNode,
						name: node.name,
						moduleName: moduleName,
						interviewId: interviewId,
						nodeCode: nodeCode,
						topNodeId: node.topNodeId,
						type: node.type
					}
				});
				shouldPassHiddenParam = true;
			}

		};

		$scope.openModuleLanguageTab = function(lang, row) {
			var tabTitle = "Job Module Language-" + row.idNode + "-" + lang;
			var state = "tabs.moduleLanguage";
			$stickyState.reset(state);
			if (!checkIfTabIsOpen(tabs, tabTitle)) {
				tabs.push({
					title: tabTitle,
					viewName: 'moduleLanguage@tabs',
					canClose: true,
					disabled: false
				});
				$scope.tabOptions.push({
					state: state,
					data: {
						row: row.idNode,
						lang: lang
					}
				});
			}

		};

		$scope.openFragmentLanguageTab = function(lang, row) {
			var tabTitle = "Fragment Language ";
			var state = "tabs.fragmentLanguage";
			$stickyState.reset(state);
			if (!checkIfTabIsOpen(tabs, tabTitle)) {
				tabs.push({
					title: tabTitle,
					viewName: 'fragmentLanguage@tabs',
					canClose: true,
					disabled: false
				});
				$scope.tabOptions.push({
					state: state,
					data: {
						row: row.idNode,
						lang: lang
					}
				});
			}

		};

		$rootScope.addLanguageTab = function() {
			var tabTitle = "Language Summary";
			var state = "tabs.languageSummary";
			$stickyState.reset(state);
			if (!checkIfTabIsOpen(tabs, tabTitle)) {
				tabs.push({
					title: tabTitle,
					viewName: 'languageSummary@tabs',
					canClose: true,
					disabled: false
				});
				$scope.tabOptions.push({
					state: state,
					data: {}
				});
			}
		};

		$rootScope.addLanguageBreakdownTab = function(flag, type) {
			var suffix = type == 'M' ? 'Modules' : 'Fragments';
			var tabTitle = "Language " + suffix;
			var state = "tabs.languageBreakdown";
			$stickyState.reset(state);
			if (!checkIfTabIsOpen(tabs, tabTitle)) {
				tabs.push({
					title: tabTitle,
					viewName: 'languageBreakdown@tabs',
					canClose: true,
					disabled: false
				});
				$scope.tabOptions.push({
					state: state,
					data: {
						flag: flag,
						type: type
					}
				});
			}
		};

		$rootScope.closeLanguageTab = function() {
			var tabTitle = "Language Summary";
			var state = "tabs.languageSummary";
			$stickyState.reset(state);
			var index = undefined;
			_.find(tabs, function(el, ind) {
				if (el.title === tabTitle) {
					index = ind;
				}
			});
			if (index > 0) {
				$scope.removeTab(index);
			}
		};

		$scope.addAllModuleValidationTab = function(response) {
			tabs.push({
				title: "Validation Report - All Job Modules",
				viewName: 'allModuleValidation@tabs',
				canClose: true,
				disabled: false
			});
			$scope.tabOptions.push({
				state: "tabs.allModuleValidation",
				data: {
					row: response
				}
			});
			$scope.allModuleValidationReport = response;
			$rootScope.tabsLoading = true;
			safeDigest($rootScope.tabsLoading);
		};

		$scope.addAllFragmentValidationTab = function(response) {
			tabs.push({
				title: "Validation Report - All Fragments",
				viewName: 'allFragmentValidation@tabs',
				canClose: true,
				disabled: false
			});
			$scope.tabOptions.push({
				state: "tabs.allFragmentValidation",
				data: {
					row: response
				}
			});
			$scope.allFragmentValidationReport = response;
			$rootScope.tabsLoading = true;
			safeDigest($rootScope.tabsLoading);
		};

		$scope.addFragmentsLinkTab = function(response) {
			tabs.push({
				title: "Task Module Links",
				viewName: 'fragmentLinks@tabs',
				canClose: true,
				disabled: false
			});
			$scope.tabOptions.push({
				state: "tabs.fragmentLinks",
				data: {
					row: response
				}
			});
			$scope.linkData = response;
			$rootScope.tabsLoading = true;
			safeDigest($rootScope.tabsLoading);
		};
		$scope.addImportJsonValidationTab = function(report) {
			tabs.push({
				title: "Validation Report (Import JSON)",
				viewName: 'importJsonValidationReport@tabs',
				canClose: true,
				disabled: false
			});
			$scope.tabOptions.push({
				state: "tabs.importJsonValidationReport",
				data: {
					row: report.idNode
				}
			});
			$scope.importJsonValidationReport = report;
			$rootScope.tabsLoading = true;
			safeDigest($rootScope.tabsLoading);
		};
		$scope.addFragmentTab = function(row) {
			tabs.push({
				title: row.name,
				viewName: 'fragment@tabs',
				canClose: true,
				disabled: false
			});
			$scope.tabOptions.push({
				state: "tabs.fragment",
				data: {
					row: row.idNode
				}
			});
			$rootScope.tabsLoading = true;
			safeDigest($rootScope.tabsLoading);
		};
		$scope.addAssessmentTab = function(row) {
			var state = "tabs.assessment";
			tabs.push({
				title: row.referenceNumber,
				viewName: 'assessment@tabs',
				canClose: true,
				disabled: false
			});
			$scope.tabOptions.push({
				state: "tabs.assessment",
				data: {
					row: row.interviewId
				}
			});
			$rootScope.tabsLoading = true;
			safeDigest($rootScope.tabsLoading);
			$stickyState.reset(state);
		};
		$scope.addModuleTab = function(row, scrollTo) {
			var state = "tabs.questions";
			if (!checkIfTabIsOpen(tabs, row.name)) {
				tabs.push({
					title: row.name,
					viewName: 'questions@tabs',
					canClose: true,
					disabled: false
				});
				$scope.tabOptions.push({
					state: state,
					data: {
						row: row.idNode,
						lang: null
					}
				});
			}
			$rootScope.tabsLoading = true;
			safeDigest($rootScope.tabsLoading);
			$stickyState.reset(state);
			$log.info("addModuleTab questionsLoading end");
		};

		$scope.addIntroTab = function(row) {
			var state = "tabs.intro";
			$stickyState.reset(state);
			if (!checkIfTabIsOpen(tabs, row.name)) {
				tabs.push({
					title: row.name,
					viewName: 'intro@tabs',
					canClose: true,
					disabled: false
				});
				$scope.tabOptions.push({
					state: state,
					data: {
						row: row.idNode
					}
				});
			}
			$rootScope.tabsLoading = true;
			safeDigest($rootScope.tabsLoading);
			$log.info("addIntroTab called");
		};
		var tsInc = 0;
		$scope.reloadTab = function(row) {
			var state = $state.current.name;
			$stickyState.reset(state);
			if (checkIfTabIsOpen(tabs, row.name)) {
				$scope.removeTab(getCurrentIndexIfOpenTab(tabs, row.name));
				tabs.push({
					title: row.name,
					viewName: Object.keys($state.current.views)[0],
					canClose: true,
					disabled: false
				});
				$scope.tabOptions.push({
					state: state,
					data: {
						row: row.idNode,
						ts: ++tsInc
					}
				});
			}
			$rootScope.tabsLoading = true;
			safeDigest($rootScope.tabsLoading);
			$log.info("addIntroTab called");
		};

		$scope.addRulesTab = function(scope) {
			var nodeData = scope.$modelValue;
			var tabTitle = "Rules " + nodeData.name;
			var state = "tabs.rules";
			$stickyState.reset(state);
			if (!checkIfTabIsOpen(tabs, tabTitle)) {
				tabs.push({
					title: tabTitle,
					viewName: 'rules@tabs',
					canClose: true,
					disabled: false
				});
				$scope.tabOptions.push({
					state: state,
					data: {
						row: nodeData.idNode
					}
				});
			}
			$rootScope.tabsLoading = true;
			safeDigest($rootScope.tabsLoading);
		};
		$scope.addInterviewTab = function(scope) {
			var nodeData = scope.$modelValue;
			var tabTitle = "Interview " + nodeData.name;
			var state = "tabs.interview";
			$stickyState.reset(state);
			if (!checkIfTabIsOpen(tabs, tabTitle)) {
				tabs.push({
					title: tabTitle,
					viewName: 'interview@tabs',
					canClose: false,
					disabled: false
				});
				$scope.tabOptions.push({
					state: state,
					data: {
						row: nodeData.idNode
					}
				});
			}
			$rootScope.tabsLoading = true;
			safeDigest($rootScope.tabsLoading);
		};
		$scope.addInterviewTabInterviewers = function(idNode, awesId) {
			//close other interview tabs
			for (var i = tabs.length - 1; i >= 0; i--) {
				var tab = tabs[i];
				if (tab.viewName == 'interview@tabs') {
					tabs.splice(i, 1);
					$scope.tabOptions.splice(i, 1);
				}
			}
			var tabTitle = "New Interview " + awesId;
			var state = "tabs.interview";
			$stickyState.reset(state);
			if (!checkIfTabIsOpen(tabs, tabTitle)) {
				tabs.push({
					title: tabTitle,
					viewName: 'interview@tabs',
					canClose: false,
					disabled: false
				});
				$scope.tabOptions.push({
					state: state,
					data: {
						row: idNode,
						startWithReferenceNumber: awesId
					}
				});
			}
			$rootScope.tabsLoading = true;
			safeDigest($rootScope.tabsLoading);
		};
		$scope.addParticipantDetailsTab = function(idNode, awesId, isEditMode, interviewId) {
			//close other interview tabs
			/*
				  for(var i = tabs.length - 1; i >= 0; i--) {
					var tab = tabs[i];
					if(tab.viewName == 'participantDetails@tabs') {
					  tabs.splice(i, 1);
					  $scope.tabOptions.splice(i, 1);
					}
				  }
			*/
			var tabTitle = "" + awesId;
			var state = "tabs.participantDetails";
			$stickyState.reset(state);
			if (!checkIfTabIsOpen(tabs, tabTitle)) {
				tabs.push({
					title: tabTitle,
					viewName: 'participantDetails@tabs',
					canClose: true,
					disabled: false
				});
				if (isEditMode == false) {
					$scope.tabOptions.push({
						state: state,
						data: {
							row: idNode,
							startWithReferenceNumber: awesId
						}
					});
				} else {
					$scope.tabOptions.push({
						state: state,
						data: {
							row: interviewId,
							updateData: awesId
						}
					});
				}

			}
			$rootScope.tabsLoading = true;
			safeDigest($rootScope.tabsLoading);
		};
        $scope.addParticipantDataEntryTab = function(studyId) {
			var tabTitle = "" + studyId;
			var state = "tabs.participantDataEntry";
			$stickyState.reset(state);
			if (!checkIfTabIsOpen(tabs, tabTitle)) {
				tabs.push({
					title: tabTitle,
					viewName: 'participantDataEntry@tabs',
					canClose: true,
					disabled: false
				});
				$scope.tabOptions.push({
                    state: state,
                    data: {
                        startWithReferenceNumber: studyId
                    }
                });
                console.log("Added tabOptions");
			}
			$rootScope.tabsLoading = true;
			safeDigest($rootScope.tabsLoading);
		};
		$scope.addParticipantMappingTab = function(awesId, interviewId) {
			//close other mapping tabs

			for (var i = tabs.length - 1; i >= 0; i--) {
				var tab = tabs[i];
				if (tab.viewName == 'participantMapping@tabs') {
					tabs.splice(i, 1);
					$scope.tabOptions.splice(i, 1);
				}
			}
            let array = awesId.split("-");
			var tabTitle = "Allocating " + array[0];
			var state = "tabs.participantMapping";
			$stickyState.reset(state);
			if (!checkIfTabIsOpen(tabs, tabTitle)) {
				tabs.push({
					title: tabTitle,
					viewName: 'participantMapping@tabs',
					canClose: true,
					disabled: false
				});

				$scope.tabOptions.push({
					state: state,
					data: {
						row: interviewId,
						mapping: awesId
					}
				});


			}
			$rootScope.tabsLoading = true;
			safeDigest($rootScope.tabsLoading);
		};
		$scope.addParticipantAddressTab = function(studyId) {
			//close other address tabs

			for (var i = tabs.length - 1; i >= 0; i--) {
				var tab = tabs[i];
				if (tab.viewName == 'participantAddress@tabs') {
					tabs.splice(i, 1);
					$scope.tabOptions.splice(i, 1);
				}
			}

			var tabTitle = "Addresses " + studyId;
			var state = "tabs.participantAddress";
			$stickyState.reset(state);
			if (!checkIfTabIsOpen(tabs, tabTitle)) {
				tabs.push({
					title: tabTitle,
					viewName: 'participantAddress@tabs',
					canClose: true,
					disabled: false
				});

				$scope.tabOptions.push({
					state: state,
					data: {
                        startWithReferenceNumber: studyId
                    }
				});


			}
			$rootScope.tabsLoading = true;
			safeDigest($rootScope.tabsLoading);
		};
		$scope.addParticipantJobTab = function(studyId) {
            //close other address tabs

            for (var i = tabs.length - 1; i >= 0; i--) {
                var tab = tabs[i];
                if (tab.viewName == 'participantJob@tabs') {
                    tabs.splice(i, 1);
                    $scope.tabOptions.splice(i, 1);
                }
            }

            var tabTitle = "Job " + studyId;
            var state = "tabs.participantJob";
            $stickyState.reset(state);
            if (!checkIfTabIsOpen(tabs, tabTitle)) {
                tabs.push({
                    title: tabTitle,
                    viewName: 'participantJob@tabs',
                    canClose: true,
                    disabled: false
                });

                $scope.tabOptions.push({
                    state: state,
                    data: {
                        startWithReferenceNumber: studyId
                    }
                });


            }
            $rootScope.tabsLoading = true;
            safeDigest($rootScope.tabsLoading);
        };
		$scope.addInterviewTabInterviewersEdit = function(participant) {
			var isSameIntroModule = true;
			//participant.idModule is the intro module id
			//if(participant.idModule != $sessionStorage.activeIntro.value){
			//	isSameIntroModule = false;
			//}
			//close other interview tabs
			for (var i = tabs.length - 1; i >= 0; i--) {
				var tab = tabs[i];
				if (tab.viewName == 'interviewresume@tabs') {
					tabs.splice(i, 1);
					$scope.tabOptions.splice(i, 1);
				}
			}
			var tabTitle = "Resume Interview " + participant.reference;
			var state = "tabs.interviewresume";
			$stickyState.reset(state);
			if (!checkIfTabIsOpen(tabs, tabTitle)) {
				tabs.push({
					title: tabTitle,
					viewName: 'interviewresume@tabs',
					canClose: false,
					disabled: false,
					isSameIntroModule: isSameIntroModule
				});
				$scope.tabOptions.push({
					state: state,
					data: {
						row: participant.idParticipant,
						interviewId: participant.idinterview
					}
				});
			}
			$rootScope.tabsLoading = true;
			safeDigest($rootScope.tabsLoading);
		};
		$scope.addParticipantTab = function(participant) {
			//var participant = scope.$modelValue;
			var tabTitle = "Participant " + participant.reference;
			var state = "tabs.participant";
			$stickyState.reset(state);
			if (!checkIfTabIsOpen(tabs, tabTitle)) {
				tabs.push({
					title: tabTitle,
					viewName: 'participant@tabs',
					canClose: true,
					disabled: false
				});
				$scope.tabOptions.push({
					state: state,
					data: {
						row: participant.idParticipant
					}
				});
			}
			$rootScope.tabsLoading = true;
			safeDigest($rootScope.tabsLoading);
		};
		var firedRulesTabCount = 0;
		$scope.addFiredRulesTab = function(interview) {
			var tabTitle = "Assessment " + interview.referenceNumber;

			if (!checkIfTabIsOpen(tabs, tabTitle)) {

				var count = firedRulesTabCount++;
				var state = "tabs.firedrules" + count;
				var viewName = 'firedrules' + count + '@tabs';

				var newState = {
					name: state,
					url: '/firedrules/:interviewId',
					sticky: true,
					deepStateRedirect: true,
					authenticate: true,
					views: {}
				};

				var views = {
					templateUrl: 'scripts/firedRules/view/fireRules.html',
					controller: 'FiredRulesCtrl as vm',
					resolve: {
						data: function($stateParams, InterviewsService) {
							return $stateParams.interviewId;
						},
						moduleName: function($stateParams, InterviewsService) {
							return $stateParams.moduleName;
						}
					}
				};

				//Add dynamic view name
				newState.views[viewName] = views;
				//Add state
				runtimeStates.addState(state, newState);

				tabs.push({
					title: tabTitle,
					viewName: viewName,
					canClose: true,
					disabled: false,
					isSameIntroModule: interview.isSameIntroModule
				});
				$scope.tabOptions.push({
					state: state,
					data: {
						interviewId: interview.interviewId,
						moduleName: interview.moduleName
					}
				});
			}
			$stickyState.reset(state);
			shouldPassHiddenParam = true;
		};
		$rootScope.addErrorTab = function(error) {
			var tabTitle = "Error!";
			var state = "tabs.error";
			$stickyState.reset(state);
			tabs.push({
				title: tabTitle,
				viewName: 'error@tabs',
				canClose: true,
				disabled: false
			});
			$scope.tabOptions.push({
				state: state,
				data: {
					error: error
				}
			});

			$rootScope.tabsLoading = false;
			safeDigest($rootScope.tabsLoading);
		};

		$scope.addAgentRulesTab = function(row) {
			var state = "tabs.agentrules";
			$stickyState.reset(state);
			if (!checkIfTabIsOpen(tabs, row.name)) {
				tabs.push({
					title: row.name,
					viewName: 'agentrules@tabs',
					canClose: true,
					disabled: false
				});
				$scope.tabOptions.push({
					state: state,
					data: {
						row: row.idAgent
					}
				});
			}
			$rootScope.tabsLoading = true;
			safeDigest($rootScope.tabsLoading);
			$log.info("addAgentRulesTab called");
		};

		$scope.continueInterview = function(data) {
			var node = data.interviews[0].module;
			var tabTitle = "Interview " + node.name;
			var state = "tabs.interview";
			$stickyState.reset(state);
			if (!checkIfTabIsOpen(tabs, tabTitle)) {
				tabs.push({
					title: tabTitle,
					viewName: 'interview@tabs',
					canClose: true,
					disabled: false
				});
				$scope.tabOptions.push({
					state: state,
					data: {
						row: node.idNode,
						interviewId: data.interviews[0].interviewId
					}
				});
			}
			$rootScope.tabsLoading = true;
			safeDigest($rootScope.tabsLoading);
		};
		$scope.removeCurrentTab = function(){
		    var index = $scope.selectedIndex;
        	tabs.splice(index, 1);
        	$scope.tabOptions.splice(index, 1);
		}

		$scope.removeTab = function(tab) {
			if (tab.title == 'Error!') {
				var index = tabs.indexOf(tab);
				tabs.splice(index, 1);
				$scope.tabOptions.splice(index, 1);
			} else if (auth.userHasPermission(['ROLE_INTERVIEWER'])) {
				if ($rootScope.participant) {
					if ($rootScope.participant.status != 2) {//complete
						if (confirm('Are you sure you want to stop the interview?')) {
							if ($rootScope.participant) {
								var participant = $scope.participant;
								participant.status = 1;//partial
								$rootScope.saveParticipant(participant);
							}
							var index = tabs.indexOf(tab);
							tabs.splice(index, 1);
							$scope.tabOptions.splice(index, 1);
						}
					} else {
						var index = tabs.indexOf(tab);
						tabs.splice(index, 1);
						$scope.tabOptions.splice(index, 1);
					}
				}
			} else if (auth.userHasPermission(['ROLE_CONTDEV'])) {
				var index = tabs.indexOf(tab);
				tabs.splice(index, 1);
				$scope.tabOptions.splice(index, 1);
				if ($scope.selectedIndex == 5) {
					$scope.selectedIndex = 0;
				}
			}  else if (auth.userHasPermission(['ROLE_DATAENTRY'])) {
                if ($rootScope.participant) {

                    var participant = $rootScope.participant;

                    $rootScope.saveParticipant();

                    var index = tabs.indexOf(tab);
                    tabs.splice(index, 1);
                    $scope.tabOptions.splice(index, 1);

                }
            } else {
				var index = tabs.indexOf(tab);
				tabs.splice(index, 1);
				$scope.tabOptions.splice(index, 1);
			}
			$scope.agentsData = null;
		};

		$scope.turnOffProgressBar = function turnOffProgressBar() {
			$scope.loading = false;
			return 'Done';
		};

		$scope.tabMenu = function(tab) {
			if (tabs.indexOf(tab) > 4) {
				return [
					['Close Tab', function(tab) {
						$scope.removeTab(tab);
					}]
				]
			} else {
				return [];
			}
		};

		$rootScope.$on("addAgentRulesTab", function(event, row) {
			$scope.addAgentRulesTab(row);
		});

		function checkIfTabIsOpen(tabs, title) {
			var openedTab = false;
			_.find(tabs, function(el, ind) {
				if (el.title === title) {
					$scope.selectedIndex = ind;
					safeDigest($scope.selectedIndex);
					openedTab = true;
				}
			});
			return openedTab;
		}

		function getCurrentIndexIfOpenTab(tabs, title) {
			_.find(tabs, function(el, ind) {
				if (el.title === title) {
					$scope.selectedIndex = ind;
					safeDigest($scope.selectedIndex);
				}
			});
			return $scope.selectedIndex;
		}

		var safeDigest = function(obj) {
			if (!obj.$$phase) {
				try {
					obj.$digest();
				} catch (e) {
				}
			}
		}

	}
})();
