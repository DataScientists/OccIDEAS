(function() {

  angular.module('occIDEASApp.angular.translate', ['pascalprecht.translate']).config(Config).factory('asyncLoader', AsyncLoader);

  Config.$inject = ['$translateProvider'];

  function Config($translateProvider) {
    $translateProvider.useSanitizeValueStrategy(null);
    $translateProvider.useLoader('asyncLoader');
  }

  AsyncLoader.$inject = ['$q', '$timeout', 'NodeLanguageService', '$sessionStorage'];

  function AsyncLoader($q, $timeout, NodeLanguageService, $sessionStorage) {
    return function(options) {
      var translations = [];
      var deferred = $q.defer();
      var key = options.key;
      if(key == 'EN') {
        return translations;
      }

      if(!$sessionStorage.languages) {
        NodeLanguageService.getAllLanguage().then(function(response) {
          if(response.status == '200') {
            $sessionStorage.languages = response.data;
            var lang = _.find($sessionStorage.languages, function(o) {
              return o.language == key;
            });
            $sessionStorage.chosenLang = lang;
            if(lang && lang.id) {
              NodeLanguageService.getNodeLanguageById(lang.id).then(function(response) {
                if(response.status == '200') {
                  translations = _.map(response.data, function(o) {
                    return {
                      [o.word.toLowerCase().trim()]: o.translation
                    };
                  });
                  return deferred.resolve(translations);
                } else {
                  return deferred.reject(lang.id);
                }
              });
            }
          }
        })
      } else {
        var lang = _.find($sessionStorage.languages, function(o) {
          return o.language == key;
        });
        $sessionStorage.chosenLang = lang;
        if(lang && lang.id) {
          NodeLanguageService.getNodeLanguageById(lang.id).then(function(response) {
            if(response.status == '200') {
              translations = _.map(response.data, function(o) {
                return {
                  [o.word.toLowerCase().trim()]: o.translation
                };
              });
              return deferred.resolve(translations);
            } else {
              return deferred.reject(lang.id);
            }
          });
        }
      }
//  		$timeout(function () {
//  			deferred.resolve(translations);
//  		}, 2000);

      return deferred.promise;
    };
  }


})();