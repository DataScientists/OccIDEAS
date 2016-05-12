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
			name:'tabs.modules',
			url: '/modules/',
			sticky: false,
		    deepStateRedirect: false,
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
			views:{
				'participants@tabs':{
					templateUrl : "scripts/participants/view/participantsTable.html",
					controller: 'ParticipantsCtrl as vm',
					resolve:{
				        data: function(InterviewsService,TabsCache) {
			        		if(TabsCache.get('studyintromodule')){
			        			$log.info("Data getting from questions Cache ...");
		        				return TabsCache.get('studyintromodule');
			        		}
			        		return InterviewsService.findModule('-1')
                            .then(function(response){
                            	TabsCache.put("studyintromodule",response.data);
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
			sticky: false,
		    deepStateRedirect: false,
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
			url: '/questions/:row',
			sticky: true,
		    deepStateRedirect: true,
			views:{
				'questions@tabs':{
					template: '<div scope-question></div>',
			        controller: 'QuestionsCtrl as vm',
			        params:{row: null,module:null},
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
			        }
			     }
			}
			}
		}).state( {
			name:'tabs.intro',
			url: '/intro/:row',
			sticky: true,
			views:{
				'intro@tabs':{
					template: '<div scope-question></div>',
			        controller: 'QuestionsCtrl as vm',
			        params:{row: null,module:null},
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
			        }
			     }
			}
			}
		}).state( {
			name:'tabs.fragment',
			url: '/fragment/:row',
			sticky: true,
		    deepStateRedirect: true,
			views:{
				'fragment@tabs':{
					template: '<div scope-question></div>',
			        controller: 'QuestionsCtrl as vm',
			        params:{row: null},
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
			        }
				}
				}
			}
		}).state( {
            name:'tabs.interview',
			url: '/interview/:row/:interviewId/:startWithReferenceNumber',
            sticky: true,
		    deepStateRedirect: true,
            views:{
                'interview@tabs':{
                    templateUrl: 'scripts/interviews/view/interview.html',
                    controller: 'InterviewsCtrl as vm',
                    params:{row: null,module:null,interviewId:null,startWithReferenceNumber:null},
                    resolve:{
                        data: function($stateParams,InterviewsService,TabsCache) {
			        		if(TabsCache.get('studyintromodule')){
			        			$log.info("Data getting from questions Cache ...");
		        				return TabsCache.get('studyintromodule');
			        		}
                            return InterviewsService.findModule($stateParams.row)
                                .then(function(response){
                                    $log.info("Data getting from findModule AJAX ...");
                                    return response.data;
                                });
                        },
                        updateData: function($stateParams,InterviewsService){
                        	if(!$stateParams.interviewId){
                        		return null;
                        	}
                        	return InterviewsService.getInterview($stateParams.interviewId)
                        		.then(function(response){
                        			return response.data;
                        		});
                        },
                        startWithReferenceNumber: function($stateParams,InterviewsService){
                        	return $stateParams.startWithReferenceNumber;
                        }
                    }
                }
            }
        }).state( {
            name:'tabs.rules',
        	url: '/rules/:row',
            sticky: false,
		    deepStateRedirect: false,
            views:{
                'rules@tabs':{
                    templateUrl: 'scripts/rules/view/rulesTable.html',
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
        });
	}
})();