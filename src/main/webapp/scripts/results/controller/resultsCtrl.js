(function () {

    angular.module('occIDEASApp.Results').controller('ResultCtrl',
        ResultCtrl);

    ResultCtrl.$inject = ['$state', 'ngToast', '$timeout',
        '$scope', '$http', '$rootScope',
        'dataBeanService', '$window', 'loginService',
        '$sessionStorage', 'AuthenticationService', '$mdDialog',
        'SystemPropertyService', '$translate', 'NodeLanguageService',
        'NgTableParams'];

    function ResultCtrl($state, ngToast, $timeout,
                        $scope, $http, $rootScope,
                        dataBeanService, $window, loginService,
                        $sessionStorage, auth, $mdDialog, SystemPropertyService,
                        $translate, NodeLanguageService, NgTableParams) {

    }
})();