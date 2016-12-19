(function() {
   
    angular.module('occIDEASApp.angular.translate',['pascalprecht.translate']).config(Config).factory('asyncLoader', AsyncLoader);
   
    Config.$inject = ['$translateProvider'];
    function Config($translateProvider){
      $translateProvider.useSanitizeValueStrategy(null);
      $translateProvider.useLoader('asyncLoader');  
    }
    
    AsyncLoader.$inject = ['$q','$timeout','NodeLanguageService','$sessionStorage'];
    function AsyncLoader($q, $timeout,NodeLanguageService,$sessionStorage){
 		   return function (options) {
 		     var deferred = $q.defer();
 		     var translations = [];
 		     if(options.key == 'EN'){
 		    	 return translations;
 		     }
 		     
 		     var lang = _.find($sessionStorage.languages, function(o) { 
 		    	 return o.language == options.key; 
 		     });
 		     if(lang.id){
 		     NodeLanguageService.getNodeLanguageById(lang.id).then(function(response) {
 		         if(response.status == '200'){
 		        	translations = _.map(response.data, function(o){
 		        	    return {
 		        	       [o.word]: o.translation
 		        	    };
 		        	});
 		        	return deferred.resolve(translations);
 		         }else{
 		        	return deferred.reject(lang.id);
 		         }
 		     });
 		     }
// 		     $timeout(function () {
// 		       deferred.resolve(translations);
// 		     }, 2000);
 		  
 		     return deferred.promise;
 		   };
    }
   
})();