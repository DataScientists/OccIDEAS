(function () {
    angular.module('occIDEASApp.Book')
        .controller('BookCtrl', BookCtrl);

    BookCtrl.$inject = ['BookService', 'NgTableParams', '$state', '$scope', '$filter', '$rootScope', '$mdDialog', 'ngToast'];

    function BookCtrl(BookService, NgTableParams, $state, $scope, $filter, $rootScope, $mdDialog, $ngToast) {
        const self = this;
        self.tableParams = new NgTableParams({group: "book.name", count: 100}, {
            getData: function ($defer, params) {
                if (params.filter().name || params.filter().description || params.filter().isChecked) {
                    return $filter('filter')(self.tableParams.settings().dataset, params.filter());
                }
                if (!self.tableParams.shouldGetData) {
                    return self.tableParams.settings().dataset;
                }
                return BookService.get().then(function (data) {
                    console.log("Data get list from agents ajax ...", data);
                    self.originalData = angular.copy(data);
                    self.tableParams.settings().dataset = data;
                    return data;
                });
            }
        });

    }
})();