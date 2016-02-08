(function() {
	angular.module("occIDEASApp.Tabs", [ 'ui.router' ]).config(Config);

	Config.$inject = ['$stateProvider'];
	function Config($stateProvider) {
		$stateProvider.state('tabs', {
			abstract : true,
			templateUrl : "scripts/tabs/view/tabs.html"
		}).state('tabs.modules', {
			url: '/modules/',
			views:{
				'moduleView':{
					templateUrl : "scripts/modules/view/modulesTable.html",
					controller: 'ModuleCtrl as vm'
				}
			}
		}).state('tabs.fragments', {
			url: '/fragments/',
			views:{
				'fragmentView':{
					templateUrl : "scripts/fragments/view/fragmentsTable.html",
					controller: 'FragmentCtrl as vm'
				}
			}
		}).state('tabs.agents', {
			url: '/agents/',
			views:{
				'agentView':{
					templateUrl : "scripts/agents/view/agentsTable.html",
					controller: 'AgentCtrl as vm'
				}
			}
		}).state('tabs.questions', {
			url: '/questions/:row',
			views:{
				'questionsView':{
					templateUrl: 'scripts/questions/view/questions.html',
			        controller: 'QuestionsCtrl as vm',
			        params:{row: null},
			        resolve:{
			        	data: function($stateParams,QuestionsService) {
			        		return QuestionsService.findQuestions($stateParams.row,'M')
			        				.then(function(response){
			        					console.log("Data getting from questions AJAX ...");
			        					if(response.data[0].type=='M_IntroModule'){
			        						response.data.showModuleSlider = true;
			        						response.data.showFragmentSlider = false;
			        					}else{
			        						response.data.showModuleSlider = false;
			        						response.data.showFragmentSlider = true;
			        					}
						        		return response.data;
		    				})
			        	}
			        }
				}
			}
		}).state('tabs.fragment', {
			url: '/fragment/:row',
			views:{
				'getfragmentView':{
					templateUrl: 'scripts/questions/view/questions.html',
			        controller: 'QuestionsCtrl as vm',
			        params:{row: null},
			        resolve:{
			        	data: function($stateParams,FragmentsService) {
			        		
			        		return FragmentsService.findFragment($stateParams.row)
			        				.then(function(data){
			        					console.log("Fragment Data from AJAX ...");
			        					console.log("Fragment IdNode: "+$stateParams.row);		        					
						        		return data;
		    				})
			        	}
			        }
				}
			}
		}).state('tabs.interview', {
            url: '/questions/:row',
            views:{
                'interviewView':{
                    templateUrl: 'scripts/questions/view/interview.html',
                    controller: 'QuestionsCtrl as vm',
                    params:{row: null},
                    resolve:{
                        data: function($stateParams,QuestionsService) {
                            return QuestionsService.findQuestions($stateParams.row,'M')
                                .then(function(response){
                                    console.log("Data getting from questions AJAX ...");
                                    var viewData = response.data;
                                    viewData['showedQuestion'] = response.data[0];
                                    console.log(viewData);
                                    return viewData;
                                })
                        }
                    }
                }
            }
        });
	}
})();