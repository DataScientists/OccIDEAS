(function() {
  angular
    .module('occIDEASApp.Notes', ['ui.router', 'ngResource'])
    .config(Config);

  Config.$inject = ['$stateProvider'];

  function Config($stateProvider) {
  }

})();