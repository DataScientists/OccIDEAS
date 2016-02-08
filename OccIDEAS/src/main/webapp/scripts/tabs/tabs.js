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
			        	},
			        	templateData: function(FragmentsService) {		        		        	
				    		return FragmentsService.get().then(function(data) {	
				    			var object = {};
				    			var ajsms = [];
				    			var templates = [];
				    			var frequencies = [];
				    			for(var i=0;i < data.length;i++){
				    				var node = data[i];
				    				node.idnode = "";
				    				node.nodeclass = "Q";
				    				if(node.type=='F_ajsm'){
				    					node.type = "Q_linkedajsm";
				    					ajsms.push(node);
				    				}else if(node.type=='F_template'){
				    					templates.push(node);
				    				}else if(node.type=='F_frequency'){
				    					frequencies.push(node);
				    				}
				    			}
				    			object.template = templates;
				    			object.frequency = frequencies;
				    			object.ajsm = ajsms;
				    			
				    			return object;
				    		});
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
			        					data.showModuleSlider = false;
			        					data.showFragmentSlider = true;		        					
						        		return data;
		    				})
			        	},
			        	templateData: function(FragmentsService) {		        		        	
				    		return FragmentsService.get().then(function(data) {	
				    			var object = {};
				    			var ajsms = [];
				    			var templates = [];
				    			var frequencies = [];
				    			for(var i=0;i < data.length;i++){
				    				var node = data[i];
				    				node.nodeclass = "Q";
				    				if(node.type=='F_ajsm'){
				    					node.type = "Q_linkedajsm";				 			
				    					ajsms.push(node);
				    				}else if(node.type=='F_template'){
				    					templates.push(node);
				    				}else if(node.type=='F_frequency'){
				    					frequencies.push(node);
				    				}
				    			}
				    			object.template = templates;
				    			object.frequency = frequencies;
				    			object.ajsm = ajsms;
				    			
				    			return object;
				    		});
				        }
			        }
				}
			}
		}).state('tabs.interview', {
            url: '/questions/:row',
            views:{
                'interviewView':{
                    templateUrl: 'scripts/interviews/view/interview.html',
                    controller: 'InterviewsCtrl as vm',
                    params:{row: null},
                    resolve:{
                        data: function($stateParams,InterviewsService) {
                            return InterviewsService.findQuestions($stateParams.row,'M')
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
        }).state('tabs.rules', {
            url: '/rules/:row',
            views:{
                'ruleView':{
                    templateUrl: 'scripts/rules/view/rules.html',
                    controller: 'RulesCtrl as vm',
                    params:{row: null},
                    resolve:{
                        data: function($stateParams,RulesService) {
                            return RulesService.getModuleRules($stateParams.row,'M')
                                .then(function(response){
                                    console.log("Data getting from questions AJAX ...");
                                    var viewData = response.data;
                                    
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