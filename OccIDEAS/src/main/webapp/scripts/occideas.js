(function(){

angular
  .module("occIDEASApp", [
    "ui.router",
    "ngMaterial",
    "ui.tree",
    "ngTable",
    "ngSanitize",
    "angular-confirm",
    "sticky",
    "ui.bootstrap.contextMenu",
    "ui.bootstrap",
    "occIDEASApp.Tabs",
    "occIDEASApp.Questions",
    "occIDEASApp.Rules",
    "occIDEASApp.Interviews",
    "occIDEASApp.Modules",
    "occIDEASApp.Agents",
    "occIDEASApp.Rules",
    "occIDEASApp.Fragments",
    "occIDEASApp.Assessments",
    "occIDEASApp.InterviewResults",
    "occIDEASApp.ModuleRule"
  ], function($rootScopeProvider){
	  $rootScopeProvider.digestTtl(100);
  })
  .constant('_', window._)
  .config(['$urlRouterProvider', '$stateProvider','$httpProvider',function($urlRouterProvider, $stateProvider,$httpProvider) {
	$httpProvider.interceptors.push('ErrorHandler');
    $urlRouterProvider.otherwise('/');
    $stateProvider
    .state('allmodules', {
        url: '/allmodules',
        controller: 'TabsCtrl as vm',
        templateUrl: 'scripts/modules/view/modules.html'
      })
      .state('module', {
        url: '/module',
        templateUrl: 'scripts/modules/view/modules.html'
      })
      .state('allfragments', {
    	  url: '/allfragments',
    	  controller: 'TabsCtrl as vm',
    	  templateUrl: 'scripts/modules/view/modules.html'
      })
      .state('fragment', {
        url: '/fragment',
        templateUrl: 'fragment.html',
        controller: 'fragmentCtrl'
      })
      .state('agent', {
        url: '/agent',
        templateUrl: 'agent.html',
        controller: 'agentCtrl'
      })
      .state('error', {
        url: '/error',
        templateUrl: 'scripts/error/view/error.html',
        controller: 'ErrorCtrl as vm',
        params:{
        	error:null
        },
        resolve:{
        	error: function($stateParams) {
        		console.log($stateParams.error);
        			return $stateParams.error;
				}
        }
      });
  }])
  .run(configureDefaults)
  .provider({
	  $exceptionHandler: function(){
		   var $log =  angular.injector(['ng']).get('$log');
	        var handler = function(exception, cause) {
	            alert("Exception:"+exception+":Cause:"+cause);
	            $log.error("Exception:"+exception+":Cause:"+cause);
	        };

	        this.$get = function() {
	            return handler;
	        };
	    }
  })
  .factory('ErrorHandler',ErrorHandler);	

   configureDefaults.$inject = ['ngTableDefaults','$state', '$rootScope'];
   function configureDefaults(ngTableDefaults,$state,$rootScope) {
	   	$rootScope._ = window._; 
	   	   
	   	ngTableDefaults.params.count = 5;
        ngTableDefaults.settings.counts = [];
        
        $rootScope.$on('$stateChangeError', function (event, toState, toParams, fromState, fromParams, error) {
        	  event.preventDefault();
        	  $state.get('error').error = { code: 123, description: 'Exception stack trace' }
        	  return $state.go('error');
        });
        document.addEventListener("keyup", function(e) {
            if (e.keyCode === 27)
                $rootScope.$broadcast("escapePressed", e.target);
        });

        document.addEventListener("click", function(e) {
            $rootScope.$broadcast("documentClicked", e.target);
        });
   }
   
   ErrorHandler.$inject = ['$injector','$window','$location'];
   function ErrorHandler($injector,$window,$location){
   	return {
   		'requestError': function(rejection) {
   	      if (canRecover(rejection)) {
   	        return responseOrNewPromise
   	      }
   	      return $q.reject(rejection);
   	    },
   		'responseError': function(response) {
   			if (response.status != 200) {
   	            var state = $injector.get('$state');       
   	            state.go('error',{
   	            	error:"Response Status returned:"
   	            		+response.status+" "
   	            		+response.statusText+" "
   	            		+response.data});
   	        }
   		    return response;
   		}
   	}
   }
   
})();