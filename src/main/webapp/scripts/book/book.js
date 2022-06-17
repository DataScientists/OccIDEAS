(function () {
    angular
        .module('occIDEASApp.Book', ['ui.router'])
        .config(Config);

    Config.$inject = ['$stateProvider'];

    function Config($stateProvider) {
    }
})();