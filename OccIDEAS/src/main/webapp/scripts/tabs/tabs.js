(function() {
	angular.module("occIDEASApp.Tabs", [ 'ui.router', 'ct.ui.router.extras.core',
	                                     'ct.ui.router.extras.dsr',
	                                     'ct.ui.router.extras.sticky' ])
	.config(Config)
	.factory('TabsCache',TabsCache)
	.run(function ($rootScope, $state, $window, $timeout) {
		$rootScope.$state = $state;
	})
	.run(['$state', function ($state) {}])
	.directive("scopeQuestion", function () {
	return {
		template: '<ng-include src="\'scripts/questions/partials/moduleTree.html\'"></ng-include>'+
		'<ng-include src="\'scripts/questions/partials/agentSlider.html\'"></ng-include>'+
		'<ng-include src="\'scripts/questions/partials/fragmentSlider.html\'"></ng-include>'+
		'<ng-include src="\'scripts/questions/partials/moduleSlider.html\'"></ng-include>'+
		'<ng-include src="\'scripts/questions/partials/rulesTemplate.html\'"></ng-include>'
	}})
	.directive("scopeModuleLanguage", function () {
		return {
			template: '<ng-include src="\'scripts/translate/view/moduleLanguageTree.html\'"></ng-include>'+
			'<ng-include src="\'scripts/questions/partials/agentSlider.html\'"></ng-include>'+
			'<ng-include src="\'scripts/questions/partials/rulesTemplate.html\'"></ng-include>'
		}
		});
	
	TabsCache.$inject = ['$cacheFactory'];
	function TabsCache($cacheFactory){
		return $cacheFactory('tabs-cache');
	}

	Config.$inject = ['$stateProvider','$windowProvider','$rootScopeProvider','$stickyStateProvider'];
	function Config($stateProvider,$windowProvider,$rootScopeProvider,$stickyStateProvider) {
		var $log =  angular.injector(['ng']).get('$log');
		var $window = $windowProvider.$get();
		$rootScopeProvider.$window = $window;
		$stateProvider.state({
			name: 'tabs',
			templateUrl : "scripts/tabs/view/tabs.html",
			controller: 'TabsCtrl as vm',
			deepStateRedirect: true,
		}).state({
			name:'tabs.importJsonValidationReport',
			url: '/importJsonValidationReport/',
			sticky: true,
		    deepStateRedirect: true,
		    authenticate:true,
			views:{
				'importJsonValidationReport@tabs':{
					templateUrl : "scripts/modules/partials/importJsonValidation.html",
					controller: 'ModuleCtrl as vm'
				}
			}
		}).state({
			name:'tabs.allModuleValidation',
			url: '/allModuleValidation/',
			sticky: true,
		    deepStateRedirect: true,
		    authenticate:true,
			views:{
				'allModuleValidation@tabs':{
					templateUrl : "scripts/modules/partials/validateAllModuleTable.html",
					controller: 'ModuleCtrl as vm'
				}
			}
		}).state({
			name:'tabs.allFragmentValidation',
			url: '/allFragmentValidation/',
			sticky: true,
		    deepStateRedirect: true,
		    authenticate:true,
			views:{
				'allFragmentValidation@tabs':{
					templateUrl : "scripts/fragments/partials/validateAllFragmentTable.html",
					controller: 'FragmentCtrl as vm'
				}
			}
		}).state({
			name:'tabs.modules',
			url: '/modules/',
			sticky: false,
		    deepStateRedirect: false,
		    authenticate:true,
			views:{
				'modules@tabs':{
					templateUrl : "scripts/modules/view/modulesTable.html",
					controller: 'ModuleCtrl as vm'
				}
			}
		}).state({
			name:'tabs.fragments',
			url: '/fragments/',
			sticky: false,
		    deepStateRedirect: false,
		    authenticate:true,
			views:{
				'fragments@tabs':{
					templateUrl : "scripts/fragments/view/fragmentsTable.html",
					controller: 'FragmentCtrl as vm'
				}
			}
		}).state({
			name:'tabs.agents',
			url: '/agents/',
			sticky: false,
		    deepStateRedirect: false,
		    authenticate:true,
			views:{
				'agents@tabs':{
					templateUrl : "scripts/agents/view/agentsTable.html",
					controller: 'AgentCtrl as vm'
				}
			}
		})
		.state( {
			name:'tabs.participants',
			url: '/participants/',
			sticky: false,
		    deepStateRedirect: false,
		    authenticate:true,
			views:{
				'participants@tabs':{
					templateUrl : "scripts/participants/view/participantsTable.html",
					controller: 'ParticipantsCtrl as vm',
					resolve:{
				        data: function(InterviewsService,TabsCache) {
			        		/*if(TabsCache.get('studyintromodule')){
			        			$log.info("Data getting from questions Cache ...");
		        				return TabsCache.get('studyintromodule');
			        		}*/
			        		return InterviewsService.findModule('-1')
                            .then(function(response){
                            	//TabsCache.put("studyintromodule",response.data);
                                $log.info("Data getting from questions AJAX ...");
                                return response.data;
                            });
				        }
				    }
				}
			}
		})
		.state({
			name:'tabs.participant',
			url: '/participant/:row',
			sticky: false,
		    deepStateRedirect: false,
		    authenticate:true,
		    views:{
				'participant@tabs':{
					templateUrl: 'scripts/participants/view/participant.html',
			        controller: 'ParticipantsCtrl as vm',
			        params:{row: null},
			        resolve:{
			        	data: function($stateParams,ParticipantsService) {
			        		$log.info("inside participant@tabs resolve");
			        		$log.info("findParticipant :"+$stateParams.row);
			        		
			        		return ParticipantsService.findParticipant($stateParams.row)
			        				.then(function(response){
			        					$log.info("Found Participant :"+response.data[0].idParticipant);
			        					return response.data[0];
		    				});
			        }
			     }
				}
			}
		})
		/*.state('tabs.interviewresults', {
			url: '/interviewresults/',
			sticky: false,
		    deepStateRedirect: false,
			views:{
				'interviewresults@tabs':{
					templateUrl : "scripts/interviewresults/view/interviewresultsTable.html",
					controller: 'InterviewResultsCtrl as vm'
				}
			}
		})*/
		.state( {
			name:'tabs.assessments',
			url: '/assessments/',
			sticky: true,
		    deepStateRedirect: true,
		    authenticate:true,
			views:{
				'assessments@tabs':{
					templateUrl : "scripts/assessments/view/assessmentsTable.html",
					controller: 'AssessmentsCtrl as vm',
					resolve:{
			        	data: function() {
			        		return '';
			        	}
			        }
				}
			}
		}).state( {
			name:'tabs.assessment',
			url: '/assessment/:row',
			sticky: true,
		    deepStateRedirect: true,
		    authenticate:true,
			views:{
				'assessment@tabs':{
					templateUrl: 'scripts/assessments/view/assessment.html',
			        controller: 'AssessmentsCtrl as vm',
			        params:{row: null,module:null},
			        resolve:{
			        	data: function($stateParams,AssessmentsService) {
			        		$log.info("inside questions@tabs resolve");
//			        		if(TabsCache.get($stateParams.row)){
//			        			$log.info("Data getting from questions Cache ...");
//			        			return TabsCache.get($stateParams.row);
//			        		}
			        		
			        		return AssessmentsService.getInterview($stateParams.row)
			        				.then(function(response){
			        					$log.info("Interview from questions AJAX ...");
			        					return response.data[0];
		    				});
			        }
			     }
			}
			}
		}).state( {
			name:'tabs.questions',
			url: '/questions/:row/:lang',
			sticky: true,
		    deepStateRedirect: true,
		    authenticate:true,
			views:{
				'questions@tabs':{
					template: '<div scope-question></div>',
			        controller: 'QuestionsCtrl as vm',
			        params:{row: null,lang:null},
			        resolve:{
			        	data: function($stateParams,QuestionsService) {
			        		$log.info("inside questions@tabs resolve");
//			        		if(TabsCache.get($stateParams.row)){
//			        			$log.info("Data getting from questions Cache ...");
//			        			return TabsCache.get($stateParams.row);
//			        		}
			        		$log.info("Data getting from questions AJAX ...");
			        		return QuestionsService.findQuestions($stateParams.row,'M')
			        				.then(function(response){
			        					$log.info("Data received from questions AJAX ...");
			        					if(angular.isUndefined($window.sliderVal)){
			        					$window.sliderVal = [];
			        					}
			        					var idNode = 'Node'+response.data[0].idNode;
			        					$window.sliderVal.idNode = {
			        							showFragmentSlider:true,
			        							showModuleSlider:true,
			        							showAgentSlider:true
			        					};
			        					if(response.data[0].type=='M_IntroModule'){
			        						$window.sliderVal.idNode.showFragmentSlider =false;
			        						$window.sliderVal.idNode.showModuleSlider = true;
			        					}else{
			        						$window.sliderVal.idNode.showFragmentSlider =true;
			        						$window.sliderVal.idNode.showModuleSlider = false;
			        					}
			        					return response.data;
		    				});
			        },
			        lang: function($stateParams) {
			        	return '';
			        }
			     }
			}
			}
		}).state( {
			name:'tabs.intro',
			url: '/intro/:row/:lang',
			sticky: true,
			authenticate:true,
			views:{
				'intro@tabs':{
					template: '<div scope-question></div>',
			        controller: 'QuestionsCtrl as vm',
			        params:{row: null,lang:null},
			        resolve:{
			        	data: function($stateParams,QuestionsService) {
			        		$log.info("inside intro@tabs resolve");
			        		$log.info("Data getting from questions AJAX ...");
			        		return QuestionsService.findQuestions($stateParams.row,'M')
			        				.then(function(response){
			        					$log.info("Data received from questions AJAX ...");
			        					if(angular.isUndefined($window.sliderVal)){
			        					$window.sliderVal = [];
			        					}
			        					var idNode = 'Node'+response.data[0].idNode;
			        					$window.sliderVal.idNode = {
			        							showFragmentSlider:true,
			        							showModuleSlider:true,
			        							showAgentSlider:true
			        					};
			        					if(response.data[0].type=='M_IntroModule'){
			        						$window.sliderVal.idNode.showFragmentSlider =false;
			        						$window.sliderVal.idNode.showModuleSlider = true;
			        					}else{
			        						$window.sliderVal.idNode.showFragmentSlider =true;
			        						$window.sliderVal.idNode.showModuleSlider = false;
			        					}
			        					return response.data;
		    				});
			        },
			        lang:function($stateParams) {
			        	return '';
			        }
			       }
			    }
			}
		}).state( {
			name:'tabs.fragment',
			url: '/fragment/:row/:lang',
			sticky: true,
		    deepStateRedirect: true,
		    authenticate:true,
			views:{
				'fragment@tabs':{
					template: '<div scope-question></div>',
			        controller: 'QuestionsCtrl as vm',
			        params:{row: null,lang: null},
			        resolve:{
			        	data: function($stateParams,QuestionsService) {
			        		$log.info("inside questions1@tabs resolve");
			        		//if(TabsCache.get($stateParams.row)){
			        		//	$log.info("Data getting from questions Cache ...");
			        		//	return TabsCache.get($stateParams.row);
			        		//}
			        		
			        		return QuestionsService.findQuestions($stateParams.row,'F')
			        				.then(function(response){
			        					$log.info("Data getting from questions AJAX ...");
			        					if(angular.isUndefined($window.sliderVal)){
			        					$window.sliderVal = [];
			        					}
			        					var idNode = 'Node'+response.data[0].idNode;
			        					$window.sliderVal.idNode = {
			        							showFragmentSlider:true,
			        							showModuleSlider:true,
			        							showAgentSlider:true
			        					};
			        					if(response.data[0].type=='M_IntroModule'){
			        						$window.sliderVal.idNode.showFragmentSlider =false;
			        						$window.sliderVal.idNode.showModuleSlider = true;
			        					}else{
			        						$window.sliderVal.idNode.showFragmentSlider =true;
			        						$window.sliderVal.idNode.showModuleSlider = false;
			        					}
			        					return response.data;
		    				});
			        },
			        lang: function($stateParams) {
			        	return '';
			        }
				}
				}
			}
		}).state( {
            name:'tabs.interview',
			url: '/interview/:row/:startWithReferenceNumber',
            sticky: true,
		    deepStateRedirect: true,
		    authenticate:true,
            views:{
                'interview@tabs':{
                    templateUrl: 'scripts/interviews/view/interview.html',
                    controller: 'InterviewsCtrl as vm',
                    params:{row: null,module:null,interviewId:null,startWithReferenceNumber:null},
                    resolve:{
                        data: function($stateParams,InterviewsService,TabsCache) {
			        		/*if(TabsCache.get('studyintromodule')){
			        			$log.info("Data getting from questions Cache ...");
		        				return TabsCache.get('studyintromodule');
			        		}*/
                            return InterviewsService.findModule($stateParams.row)
                                .then(function(response){
                                    $log.info("Data getting from findModule AJAX ...");
                                    return response.data;
                                });
                        },
                        updateData: function($stateParams,InterviewsService){                     	
                        	return undefined;
                        },
                        startWithReferenceNumber: function($stateParams,InterviewsService){
                        	return $stateParams.startWithReferenceNumber;
                        },
                        treeView: function($stateParams,InterviewsService){
                        	return undefined;
                        }
                    }
                }
            }
        }).state( {
            name:'tabs.interviewresume',
			url: '/interview/:row/:interviewId',
            sticky: true,
		    deepStateRedirect: true,
		    authenticate:true,
            views:{
                'interviewresume@tabs':{
                    templateUrl: 'scripts/interviews/view/interview.html',
                    controller: 'InterviewsCtrl as vm',
                    params:{row: null,module:null,interviewId:null,startWithReferenceNumber:null},
                    resolve:{
                    	data: function($stateParams,ParticipantsService) {
                    		return ParticipantsService.findParticipant($stateParams.row).then(function(response){
                    			if (response.status === 200) {
                    				var participant = response.data[0];
    	        					return participant;
                    			}else if (response.status === 401) {
									$log.error("Inside data of tabs.interviewresume tabs.js could not findParticipant with "+$stateParams.row);
									return;
								}
	        					
	        				});
                        },
                    	updateData: function($stateParams,ParticipantsService,InterviewsService){   
                    		
                    		return InterviewsService.get($stateParams.interviewId).then(function(response) {
								if (response.status === 200) {
									var interview = response.data[0];  									
									return interview;   				    				     									
								} else if (response.status === 401) {
									$log.error("Inside updateData of tabs.interviewresume tabs.js could not find interview with "+idInterview);
									return;
								}
                    		});
                    		      	
                        },
                        startWithReferenceNumber: function($stateParams,InterviewsService){
                        	return $stateParams.startWithReferenceNumber;
                        },
                        treeView : function($stateParams,InterviewsService){
                        	return InterviewsService.getExpandedModule($stateParams.interviewId).then(function(response){
        						if(response.status == '200' && response.data && response.data[0]){
        							return response.data[0];
        						}	
        						else if (response.status === 401) {
									$log.error("Inside treeView of tabs.interviewresume tabs.js could not find expanded interview with "+$stateParams.interviewId);
									return;
								}
        					});	
                        }
                    }
                }
            }
        }).state( {
            name:'tabs.rules',
        	url: '/rules/:row',
            sticky: false,
		    deepStateRedirect: false,
		    authenticate:true,
            views:{
                'rules@tabs':{
                    templateUrl: 'scripts/rules/view/rulesTable2.html',
                    controller: 'RulesCtrl as vm',
                    params:{row: null},
                    resolve:{
                        data: function($stateParams,RulesService) {
                            return RulesService.listByModule($stateParams.row)
                                .then(function(data){
                                    $log.info("Data getting from module rules AJAX ... for "+$stateParams.row);
                                    var viewData = data;
                                    return viewData;
                                })
                        },
			        	templateData: function($stateParams) {		        		        					    		
			    			var object = {};				    						    			
			    			object.moduleId = $stateParams.row;
			    			object.agentId = null;//todo
			    			return object;
			    		}
                        
                    }
                }
            }
        }).state( {
                name:'tabs.agentrules',
                url: '/agentrules/:row',
                sticky: true,
                authenticate:true,
                views:{
                    'agentrules@tabs':{
                        templateUrl : "scripts/rules/view/rulesTable2.html",
                        controller: 'RulesCtrl as vm',
                        params:{row: null,module:null},
                        resolve:{
                            data: function($stateParams,RulesService) {
                                $log.info("inside agentinfo@tabs resolve",$stateParams);
                                $log.info("Data getting from questions AJAX ...");
                                return RulesService.listByAgent($stateParams.row).then(function(response){
                                        //$log.info("Data received from questions AJAX ...", response);
                                        if(angular.isUndefined($window.sliderVal)){
                                            $window.sliderVal = [];
                                        }
                                        return response;
                                    });
                            },
                            templateData: function($stateParams) {
                                var object = {};
                                object.moduleId = null;
                                object.agentId = $stateParams.row;
                                return object;
                            }
                        }
                    }
                }
            }).state( {
                name:'tabs.admin',
                url: '/admin',
                authenticate:true,
                views:{
                    'admin@tabs':{
                        templateUrl : "scripts/admin/view/admin.html",
                        controller: 'AdminCtrl as vm'
//                        resolve:{
//                            data: function($stateParams,AdminService) {
//                                $log.info("inside admin@tabs resolve",$stateParams);
//                                $log.info("Data getting from admin AJAX ...");
//                                return AdminService.getUserRoles().then(function(response){
//                                        $log.info("Data received from admin AJAX ...", response);
//                                        return response.data;
//                                    });
//                            }
//                        }
                    }
                }
            }).state( {
            name:'tabs.error',
        	url: '/displayerror/',
        	params: {
        		error: null
            },
        	sticky: true,
		    deepStateRedirect: true,
		    authenticate:false,
            views:{
                'error@tabs':{
                    templateUrl: 'scripts/error/view/debug.html',
                    controller: 'DisplayErrorCtrl as vm',
                    params:{error: null},
                    resolve:{
                        error: function($stateParams) {
                            return $stateParams.error;
                        }                   
                    }
                }
            }
        }).state( {
            name:'tabs.firedrules',
			url: '/firedrules/:interviewId',
            sticky: true,
		    deepStateRedirect: true,
		    authenticate:true,
		    params:{data:null},
            views:{
                'firedrules@tabs':{
                    templateUrl: 'scripts/firedRules/view/fireRules.html',
                    controller: 'FiredRulesCtrl as vm',
                    params:{data:null},
                    resolve:{
                        data: function($stateParams,InterviewsService) {
                            return $stateParams.data.interviewId;
                        },
                        moduleName: function($stateParams,InterviewsService) {
                            return $stateParams.data.moduleName;
                        }
                    }
                }
            }
        }).state( {
            name:'tabs.reports',
			url: '/reports/',
            sticky: false,
		    deepStateRedirect: true,
		    authenticate:true,
            views:{
                'reports@tabs':{
                    templateUrl: 'scripts/reports/view/reports.html',
                    controller: 'ReportsCtrl as vm',
                    resolve:{
                        data: function($stateParams,ReportsService) {
                            return ReportsService.getAll()
                                .then(function(response){
                                    $log.info("Data getting from find reports AJAX ...");
                                    return response.data;
                                });
                        }
                    }
                }
            }
        }).state( {
            name:'tabs.language',
        	url: '/language/:row',
            sticky: false,
		    deepStateRedirect: false,
		    authenticate:true,
            views:{
                'language@tabs':{
                    templateUrl: 'scripts/translate/view/manageLanguageTable.html',
                    controller: 'NodeLanguageCtrl as vm',
                    params:{row: null}
                }
            }
        }).state( {
            name:'tabs.languageSummary',
        	url: '/languageSummary/:row',
            sticky: false,
		    deepStateRedirect: false,
		    authenticate:true,
            views:{
                'languageSummary@tabs':{
                    templateUrl: 'scripts/translate/view/languageSummaryTab.html',
                    controller: 'LanguageSummaryCtrl as vm',
                    params:{row: null}
                }
            }
        }).state( {
            name:'tabs.languageBreakdown',
        	url: '/languageBreakdown/:flag/:type',
            sticky: false,
		    deepStateRedirect: false,
		    authenticate:true,
            views:{
                'languageBreakdown@tabs':{
                    templateUrl: 'scripts/translate/view/languageBreakdownTab.html',
                    controller: 'LanguageBreakdownCtrl as vm',
                    params:{flag: null,type:null},
                    resolve:{
			        	flag: function($stateParams) {
                            return $stateParams.flag;
                        },
			        	type: function($stateParams) {
                            return $stateParams.type;
                        }
			     }
                }
            }
        }).state( {
			name:'tabs.moduleLanguage',
			url: '/moduleLanguage/:row/:lang',
			sticky: false,
		    deepStateRedirect: false,
		    authenticate:true,
			views:{
				'moduleLanguage@tabs':{
					template: '<div scope-module-language></div>',
			        controller: 'QuestionsCtrl as vm',
			        params:{row: null,lang:null},
			        resolve:{
			        	data: function($stateParams,QuestionsService) {
			        		$log.info("inside moduleLanguage@tabs resolve");
			        		$log.info("Data getting from questions AJAX ...");
			        		return QuestionsService.findQuestions($stateParams.row,'M')
			        				.then(function(response){
			        					$log.info("Data received from questions AJAX ...");
			        					if(angular.isUndefined($window.sliderVal)){
			        					$window.sliderVal = [];
			        					}
			        					var idNode = 'Node'+response.data[0].idNode;
			        					$window.sliderVal.idNode = {
			        							showAgentSlider:true
			        					};
			        					return response.data;
		    				});
			        	},
			        	lang: function($stateParams,NodeLanguageService) {
			        		return NodeLanguageService.getLanguageById($stateParams.lang)
			        								.then(function(response){
			        			if(response.status == '200'){
			        				return response.data;
			        			}
			        			return '';
			        		});
			        	}
			     }
			}
			}
		}).state( {
			name:'tabs.fragmentLanguage',
			url: '/fragmentLanguage/:row/:lang',
			sticky: false,
		    deepStateRedirect: false,
		    authenticate:true,
			views:{
				'fragmentLanguage@tabs':{
					template: '<div scope-module-language></div>',
			        controller: 'QuestionsCtrl as vm',
			        params:{row: null,lang:null},
			        resolve:{
			        	data: function($stateParams,QuestionsService) {
			        		$log.info("inside fragmentLanguage@tabs resolve");
			        		$log.info("Data getting from questions AJAX ...");
			        		return QuestionsService.findQuestions($stateParams.row,'F')
			        				.then(function(response){
			        					$log.info("Data received from questions AJAX ...");
			        					if(angular.isUndefined($window.sliderVal)){
			        					$window.sliderVal = [];
			        					}
			        					var idNode = 'Node'+response.data[0].idNode;
			        					$window.sliderVal.idNode = {
			        							showAgentSlider:true
			        					};
			        					return response.data;
		    				});
			        	},
			        	lang: function($stateParams,NodeLanguageService) {
			        		return NodeLanguageService.getLanguageById($stateParams.lang)
			        								.then(function(response){
			        			if(response.status == '200'){
			        				return response.data;
			        			}
			        			return '';
			        		});
			        	}
			     }
			}
			}
		}).state( {
            name:'tabs.answerSummary',
        	url: '/answerSummary',
            sticky: false,
		    deepStateRedirect: false,
		    authenticate:true,
		    params:{data:null},
            views:{
                'answerSummary@tabs':{
                    templateUrl: 'scripts/firedRules/view/answerSummaryTab.html',
                    controller: 'AnswerSummaryCtrl as vm',
                    params:{data: null},
                    resolve:{
                        data: function($stateParams,AssessmentsService) {
			        		return AssessmentsService.getAnswerSummaryByName($stateParams.data.name,$stateParams.data.answerId)
			        				.then(function(response){
			        					return response;
		    				});
			        	},
			        	name: function($stateParams){
			        		return $stateParams.data.moduleName;
			        	},
			        	interviewId: function($stateParams){
			        		return $stateParams.data.interviewId;
			        	}
			     }
                }
            }
        });
	}
})();