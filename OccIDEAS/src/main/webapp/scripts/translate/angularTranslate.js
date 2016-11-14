(function() {
   
    angular.module('occIDEASApp.angular.translate',['pascalprecht.translate']).config(Config);
   
    Config.$inject = ['$translateProvider'];
    function Config($translateProvider){
     $translateProvider.translations('en', {
          });
      $translateProvider.translations('my', translation_my);
      $translateProvider.useSanitizeValueStrategy(null);
      $translateProvider.preferredLanguage('en');
    }
   
})();