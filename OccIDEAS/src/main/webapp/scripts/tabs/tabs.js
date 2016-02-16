(function() {
	angular.module("occIDEASApp.Tabs", [ 'ui.router' ])
	.config(Config)
	.factory('TabsCache',TabsCache);
	
	TabsCache.$inject = ['$cacheFactory'];
	function TabsCache($cacheFactory){
		return $cacheFactory('tabs-cache');
	}

	Config.$inject = ['$stateProvider','$windowProvider','$rootScopeProvider'];
	function Config($stateProvider,$windowProvider,$rootScopeProvider) {
		var $window = $windowProvider.$get();
		$rootScopeProvider.$window = $window;
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
			        params:{row: null,module:null},
			        resolve:{
			        	data: function($stateParams,QuestionsService,TabsCache) {
			        		if(TabsCache.get($stateParams.row)){
			        			console.log("Data getting from questions Cache ...");
			        			return TabsCache.get($stateParams.row);
			        		}
			        		
			        		return QuestionsService.findQuestions($stateParams.row,'M')
			        				.then(function(response){
			        					console.log("Data getting from questions AJAX ...");
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
			        					TabsCache.put($stateParams.row,response.data);
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
				        },
				        agentsData: function(AgentsService){
				        	return  AgentsService.get().then(function(x) {
					            return x;
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
			        	data: function($stateParams,QuestionsService) {
			        		
			        		return QuestionsService.findQuestions($stateParams.row,'F')
			        				.then(function(response){
			        					if(angular.isUndefined($window.sliderVal)){
				        					$window.sliderVal = {};
				        				}
			        					var idNode = 'Node'+response.data[0].idNode;
			        					$window.sliderVal.idNode = {
			        							showFragmentSlider:true,
			        							showModuleSlider:true,
			        							showAgentSlider:true
			        					};
			        					console.log("Fragment Data from AJAX ...");
			        					console.log("Fragment IdNode: "+$stateParams.row);			        					
			        					$window.sliderVal.idNode.showModuleSlider = false;
			        					$window.sliderVal.idNode.showFragmentSlider = true;		        					
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
				        },
				        agentsData: function(AgentsService){
				        	return  AgentsService.get().then(function(x) {
					            return x;
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
                                    viewData.showedQuestion = response.data[0];
                                    viewData.showAgentSlider = false;
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
                'rulesView':{
                    templateUrl: 'scripts/rules/view/rulesTable.html',
                    controller: 'RulesCtrl as vm',
                    params:{row: null},
                    resolve:{
                        data: function($stateParams,RulesService) {
                            return RulesService.listByModule($stateParams.row)
                                .then(function(data){
                                    console.log("Data getting from module rules AJAX ... for "+$stateParams.row);
                                    var viewData = data;
                                    
                                    console.log(viewData);
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