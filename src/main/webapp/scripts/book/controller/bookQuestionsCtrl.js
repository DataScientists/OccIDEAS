(function () {
    angular.module('occIDEASApp.Book')
        .controller('BookQuestionsCtrl', BookQuestionsCtrl);

    BookQuestionsCtrl.$inject = ['data', 'BookService', 'NgTableParams', '$state', '$scope', '$filter', '$mdDialog', '$timeout'];

    function BookQuestionsCtrl(data, BookService, NgTableParams, $state, $scope, $filter, $mdDialog, $timeout) {
        const self = this;
        self.data = data;
        self.node = data;
        $scope.isClonable = false;
        $scope.isReadOnly = true;
    }
})();