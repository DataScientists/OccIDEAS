(function() {
    angular.module("occIDEASApp.Tabs").controller("TabsCtrl", TabsCtrl);

    TabsCtrl.$inject = ['$scope', '$state', '$rootScope', '$log', '$stickyState', 'AuthenticationService'];

    function TabsCtrl($scope, $state, $rootScope, $log, $stickyState, auth) {
        $scope.loading = false;
        $scope.tabOptions = [];
        if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_ADMIN', 'ROLE_ADMIN'])) {
        	$scope.tabOptions.push({
        		state: "tabs.admin",
        		data: ""
        	});
        }
        if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_CONTDEV','ROLE_ADMIN'])) {
            $scope.tabOptions.push({
                state: "tabs.modules",
                data: ""
            });
        }
        if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_CONTDEV','ROLE_ADMIN'])) {
            $scope.tabOptions.push({
                state: "tabs.fragments",
                data: ""
            });
        }
        if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_CONTDEV','ROLE_ADMIN'])) {
            $scope.tabOptions.push({
                state: "tabs.agents",
                data: ""
            });
        }
        if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_INTERVIEWER','ROLE_ADMIN'])) {
            $scope.tabOptions.push({
                state: "tabs.participants",
                data: ""
            });
        }
        if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_ASSESSOR','ROLE_ADMIN'])) {
            $scope.tabOptions.push({
                state: "tabs.assessments",
                data: ""
            });
        }
        $scope.$watch('selectedIndex', function(current, old) {
            var state = null;
            var data = null;
            if ($scope.tabOptions[current]) {
                if ($scope.tabOptions[current].state) {
                    $log.info("Navigating to " + $scope.tabOptions[current].state);
                    state = $scope.tabOptions[current].state;
                } else {
                	if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_CONTDEV'])) {
                		state = "tabs.modules";
                    }else if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_INTERVIEWER'])) {
                    	state = "tabs.participants";
                    }else if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_ASSESSOR'])) {
                    	state = "tabs.assessments";
                    }
                    
                }
                if ($scope.tabOptions[current].data) {
                    $log.info("with data: " + $scope.tabOptions[current].data)
                    $log.info("with idNode: " + $scope.tabOptions[current].data.row)
                    data = $scope.tabOptions[current].data;
                }

            } else {
            	if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_CONTDEV'])) {
            		state = "tabs.modules";
                }else if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_INTERVIEWER'])) {
                	state = "tabs.participants";
                }else if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_ASSESSOR'])) {
                	state = "tabs.assessments";
                }
            }
            $log.info("going to state " + state);
            $state.go(state, data);

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
        if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_CONTDEV','ROLE_ADMIN'])) {
            tabs.push({
                title: 'Module List',
                viewName: 'modules@tabs',
            });
        }
        if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_CONTDEV','ROLE_ADMIN'])) {
            tabs.push({
                title: 'Fragment List',
                viewName: 'fragments@tabs'
            });
        }
        if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_CONTDEV','ROLE_ADMIN'])) {
            tabs.push({
                title: 'Agent List',
                viewName: 'agents@tabs'
            });
        }
        if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_INTERVIEWER','ROLE_ADMIN'])) {
            tabs.push({
                title: 'Participants',
                viewName: 'participants@tabs'
            });
        }
        if (auth.isLoggedIn() && auth.userHasPermission(['ROLE_ASSESSOR','ROLE_ADMIN'])) {
            tabs.push({
                title: 'Assessments',
                viewName: 'assessments@tabs'
            });
        }
        $scope.tabs = tabs;
        $scope.selectedIndex = 0;

        $scope.closeAndSwitchToParticipantsTab = function() {
            var index = angular.copy($scope.selectedIndex);
            tabs.splice(index, 1);
            $scope.tabOptions.splice(index, 1);
            $scope.selectedIndex = 3;
        }

        
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
                	row:report.idNode
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
        };
        $scope.addModuleTab = function(row) {
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
                        row: row.idNode
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
                    canClose: true,
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
        $scope.addInterviewTabInterviewersEdit = function(participant) {
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
        $scope.addFiredRulesTab = function(interviewId) {
            var tabTitle = "Fired Rules " + interviewId;
            var state = "tabs.firedrules";
            if (!checkIfTabIsOpen(tabs, tabTitle)) {
                tabs.push({
                    title: tabTitle,
                    viewName: 'firedrules@tabs',
                    canClose: true,
                    disabled: false
                });
                $scope.tabOptions.push({
                    state: state,
                    data: {
                    	interviewId: interviewId
                    }
                });
            }
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
        }

        $scope.removeTab = function(tab) {       
        	if (tab.title == 'Error!') {
        		var index = tabs.indexOf(tab);
                tabs.splice(index, 1);
                $scope.tabOptions.splice(index, 1);
        	} else if (auth.userHasPermission(['ROLE_INTERVIEWER'])) {
        		if($rootScope.participant){
        			if($rootScope.participant.status != 2){//complete
        				if (confirm('Are you sure you want to stop the interview?')) {
                    		if($rootScope.participant){
                        		var participant = $scope.participant;
                        		participant.status = 1;//partial
                        		$rootScope.saveParticipant(participant);
                        	}
                    		var index = tabs.indexOf(tab);
                            tabs.splice(index, 1);
                            $scope.tabOptions.splice(index, 1);
                    	}
        			} else{
        				var index = tabs.indexOf(tab);
                        tabs.splice(index, 1);
                        $scope.tabOptions.splice(index, 1);
        			}  			
        		}
            	
            	
            }else if (auth.userHasPermission(['ROLE_CONTDEV'])) {
            	var index = tabs.indexOf(tab);
                tabs.splice(index, 1);
                $scope.tabOptions.splice(index, 1);
            	if ($scope.selectedIndex == 5) {
                    $scope.selectedIndex = 0;
                }
            }else{
            	var index = tabs.indexOf(tab);
                tabs.splice(index, 1);
                $scope.tabOptions.splice(index, 1);
            }
            $scope.agentsData = null;
        };
        
        $scope.turnOffProgressBar = function turnOffProgressBar() {
            $scope.loading = false;
            return 'Done';
        }

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
        }

        $rootScope.$on("addAgentRulesTab", function(event, row){
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

        var safeDigest = function(obj) {
            if (!obj.$$phase) {
                try {
                    obj.$digest();
                } catch (e) {}
            }
        }
        
        $scope.$watch('$stateChangeError', function(current, old) {
        	

        });
    }
})();
