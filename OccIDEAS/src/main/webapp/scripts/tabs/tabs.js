(function() {
	angular.module("occIDEASApp.Tabs", [ 'ui.router' ]).config(Config);

	Config.$inject = ['$stateProvider'];
	function Config($stateProvider) {
		$stateProvider.state('tabs', {
			abstract : true,
			templateUrl : "scripts/tabs/view/tabs.html"
		}).state('tabs.modules', {
			views:{
				'moduleView':{
					templateUrl : "scripts/modules/view/modulesTable.html",
					controller: 'ModuleCtrl as vm'
				}
			}
		}).state('tabs.fragments', {
			views:{
				'fragmentView':{
					templateUrl : "scripts/fragments/view/fragmentsTable.html",
					controller: 'FragmentCtrl as vm'
				}
			}
		}).state('tabs.agents', {
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
			        	data: function($stateParams,QuestionsService,QuestionsCache) {
			        		if(QuestionsCache.get($stateParams.row)){
			        			console.log("Data getting from questions cache ...");
			        			return QuestionsCache.get($stateParams.row);
			        		}
			        		return QuestionsService.findQuestions($stateParams.row)
			        				.then(function(data){
			        					console.log("Data getting from questions AJAX ...");
			        					QuestionsCache.put($stateParams.row,data);
						        		return data;
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
			        	data: function($stateParams,QuestionsService) {
			        		
			        		return QuestionsService.findFragment($stateParams.row)
			        				.then(function(data){
			        					console.log("Fragment Data from AJAX ...");
			        					console.log("Fragment IdNode: "+$stateParams.row);		        					
						        		return data;
		    				})
			        	}
			        }
				}
			}
		});
	}
})();