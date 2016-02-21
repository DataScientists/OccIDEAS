(function() {
	angular.module("occIDEASApp.Tabs", [ 'ui.router', 'ct.ui.router.extras.core',
	                                     'ct.ui.router.extras.dsr',
	                                     'ct.ui.router.extras.sticky' ])
	.config(Config)
	.factory('TabsCache',TabsCache)
	.run(function ($rootScope, $state, $window, $timeout) {
		$rootScope.$state = $state;
	})
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
		var $window = $windowProvider.$get();
		$rootScopeProvider.$window = $window;
		$stateProvider.state('tabs', {
			abstract : true,
			templateUrl : "scripts/tabs/view/tabs.html",
			controller: 'TabsCtrl as vm'
		}).state('tabs.modules', {
			url: '/modules/',
			sticky: false,
		    deepStateRedirect: true,
			views:{
				'modules@tabs':{
					templateUrl : "scripts/modules/view/modulesTable.html",
					controller: 'ModuleCtrl as vm'
				}
			}
		}).state('tabs.fragments', {
			url: '/fragments/',
			sticky: false,
		    deepStateRedirect: true,
			views:{
				'fragments@tabs':{
					templateUrl : "scripts/fragments/view/fragmentsTable.html",
					controller: 'FragmentCtrl as vm'
				}
			}
		}).state('tabs.agents', {
			url: '/agents/',
			sticky: false,
		    deepStateRedirect: true,
			views:{
				'agents@tabs':{
					templateUrl : "scripts/agents/view/agentsTable.html",
					controller: 'AgentCtrl as vm'
				}
			}
		}).state('tabs.questions', {
			url: '/questions/:row',
			sticky: true,
		    deepStateRedirect: true,
			views:{
				'questions@tabs':{
					templateUrl: 'scripts/questions/view/questions.html',
			        controller: 'QuestionsCtrl as vm',
			        params:{row: null,module:null},
			        resolve:{
			        	data: function($stateParams,QuestionsService,TabsCache) {
			        		console.log("inside questions@tabs resolve");
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
		    				});
			        }
			     }
			}
			}
		}).state('tabs.questions1', {
			url: '/questions1/:row',
			sticky: true,
		    deepStateRedirect: true,
			views:{
				'questions1@tabs':{
					templateUrl: 'scripts/questions/view/questions1.html',
			        controller: 'QuestionsCtrl as vm',
			        params:{row: null,module:null},
			        resolve:{
			        	data: function($stateParams,QuestionsService,TabsCache) {
			        		console.log("inside questions1@tabs resolve");
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
		    				});
			        }
				}
			}
			}
		}).state('tabs.questions2', {
			url: '/questions2/:row',
			sticky: true,
		    deepStateRedirect: true,
			views:{
				'questions2@tabs':{
					templateUrl: 'scripts/questions/view/questions2.html',
			        controller: 'QuestionsCtrl as vm',
			        params:{row: null,module:null},
			        resolve:{
			        	data: function($stateParams,QuestionsService,TabsCache) {
			        		console.log("inside questions2@tabs resolve");
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
		    				});
			        }
			     }
			}
			}
		}).state('tabs.fragment', {
			url: '/fragment/:row',
			sticky: true,
		    deepStateRedirect: true,
			views:{
				'fragment@tabs':{
					template: '<div scope-question></div>',
			        controller: 'QuestionsCtrl as vm',
			        params:{row: null},
			        resolve:{
			        	data: function($stateParams,QuestionsService,TabsCache) {
			        		console.log("inside questions1@tabs resolve");
			        		if(TabsCache.get($stateParams.row)){
			        			console.log("Data getting from questions Cache ...");
			        			return TabsCache.get($stateParams.row);
			        		}
			        		
			        		return QuestionsService.findQuestions($stateParams.row,'F')
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
		    				});
			        }
				}
				}
			}
		}).state('tabs.interview', {
            url: '/interview/:row',
            sticky: false,
		    deepStateRedirect: true,
            views:{
                'interview@tabs':{
                    templateUrl: 'scripts/interviews/view/interview.html',
                    controller: 'InterviewsCtrl as vm',
                    params:{row: null,module:null},
                    resolve:{
                        data: function($stateParams,QuestionsService,TabsCache) {
                            if(TabsCache.get($stateParams.row)){
                                console.log("Data getting from questions Cache ...");
                                var viewData = TabsCache.get($stateParams.row);
                                viewData.showedQuestion = viewData[0].nodes[0];
                                viewData.showAgentSlider = false;
                                return viewData;
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
                                    var viewData = response.data;
                                    viewData.showedQuestion = response.data[0].nodes[0];
                                    viewData.showAgentSlider = false;
                                    return viewData;
                                })
                        }
                    }
                }
            }
        }).state('tabs.rules', {
            url: '/rules/:row',
            sticky: false,
		    deepStateRedirect: true,
            views:{
                'rules@tabs':{
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