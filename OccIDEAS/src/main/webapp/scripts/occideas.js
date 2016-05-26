(function(){

angular
  .module("occIDEASApp", [
    "ui.router",
    "toaster",
    "ngMaterial",
    "ngStorage",
    "ui.tree",
    "ngTable",
    "ngSanitize",
    "angular-confirm",
    "sticky",
    "fsm",
    "angular.filter",
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
    "occIDEASApp.Participants",
    "occIDEASApp.InterviewResults",
    "occIDEASApp.ModuleRule",
    "occIDEASApp.Login",
    "occIDEASApp.Logout"
  ], function($rootScopeProvider){
	  $rootScopeProvider.digestTtl(100);
  })
  .constant('_', window._)
  .config(['$urlRouterProvider', '$stateProvider','$httpProvider',function($urlRouterProvider, $stateProvider,$httpProvider) {
	$httpProvider.interceptors.push('ErrorHandler');
	$httpProvider.interceptors.push('TokenRefreshInterceptor');
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
//	  $exceptionHandler: function(){
//		   var $log =  angular.injector(['ng']).get('$log');
//	        var handler = function(exception, cause) {
//	            alert("Exception:"+exception+":Cause:"+cause);
//	            $log.error("Exception:"+exception+":Cause:"+cause);
//	        };
//
//	        this.$get = function() {
//	            return handler;
//	        };
//	    }
  })
  .service(service)
  .factory('TokenRefreshInterceptor',TokenRefreshInterceptor)
  .factory('ErrorHandler',ErrorHandler);	

   configureDefaults.$inject = ['ngTableDefaults','$state', '$rootScope','AuthenticationService', 'dataBeanService','$window','$sessionStorage'];
   function configureDefaults(ngTableDefaults,$state,$rootScope,AuthenticationService, dataBeanService,$window,$sessionStorage) {
	   	$rootScope._ = window._; 
	   	ngTableDefaults.params.count = 5;
        ngTableDefaults.settings.counts = [];
        $rootScope.isReadOnly = false; 
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
        
        var windowElement = angular.element($window);
        windowElement.on('beforeunload', function (event) {
            // do whatever you want in here before the page unloads.
            // the following line of code will prevent reload or navigating away.
            event.preventDefault();
        });
        
        $rootScope.$on("$stateChangeStart", function(event, toState){
            if (toState.authenticate){
                var resp = AuthenticationService.checkUserCredentials($sessionStorage.userId);
                if(resp === '1'){
                	$sessionStorage.showLogout = false;
                    dataBeanService.setStatetransitionHasErr('1');
                    $state.go('login', {}, {reload: true});
                    event.preventDefault();
                }
            }
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
   			if (response.status != 200 && response.status != 401) {
   	            var state = $injector.get('$state');       
   	            state.go('error',{
   	            	error:"Response Status returned:"
   	            		+response.status+" "
   	            		+response.statusText+" "
   	            		+response.data});
   	        }else if(response.status == 401){
   	        	var state = $injector.get('$state');       
   	          	alert("Occideas is in READ-ONLY mode, update/delete action is not premitted.");
   	        }
   		    return response;
   		}
   	}
   }
   
   TokenRefreshInterceptor.$inject = ['$injector','$window'];
   function TokenRefreshInterceptor($injector,$window){
	   var $sessionStorage = $injector.get('$sessionStorage');
       return {
           'request': function(config) {
               if ($sessionStorage.token) {
                   config.headers['X-Auth-Token'] = $sessionStorage.token;
                   var http = $injector.get('$http');
                   http.defaults.headers.common['X-Auth-Token'] = $sessionStorage.token;
               }
               return config;
           },
       'response': function(response) {
                       var data = response.headers('X-Auth-Token');
                   if(data){
                       var json = angular.fromJson(data);
                       $sessionStorage.token = json.token;
                       var http = $injector.get('$http');
                       http.defaults.headers.common['X-Auth-Token'] = json.token;
                   }
                 return response;
            },
           'responseError': function(response) {
           	
               if (response.status === 401) {
            	   delete $sessionStorage.token;
                   var http = $injector.get('$http');
                   http.defaults.headers.common['X-Auth-Token'] = "";
                   var state1 = $injector.get('$state');
                   state1.go('login', {}, {reload: true});
               }else{
                    http.defaults.headers.common['X-Auth-Token'] = "";
                    var state2 = $injector.get('$state');
                    state2.go('login', {}, {reload: true});
               }
               return response;
           }
       }
   }
   
   service.$inject = ['$state', '$rootScope', 'AuthenticationService', 'dataBeanService', 'toaster','$window','$sessionStorage'];
   function service ($state, $rootScope, AuthenticationService, dataBeanService, toaster,$window,$sessionStorage){
       var app = this;
       app.logout = function() {
           toaster.pop('success', "Logout Successfull", "Goodbye");
           $sessionStorage.userId = null;
           $sessionStorage.token = null;
           $state.go('login', {}, {reload: true});
       };
   }
   
   angular.isUndefinedOrNull = function(val) {
       return angular.isUndefined(val) || val == null || val === "null";
   }
   
})();