(function () {
    angular.module('occIDEASApp.Book')
        .controller('BookCtrl', BookCtrl);

    BookCtrl.$inject = ['BookService', 'NgTableParams', '$state', '$scope', '$filter', '$mdDialog'];

    function BookCtrl(BookService, NgTableParams, $state, $scope, $filter, $mdDialog) {
        const self = this;

        self.newBook = {};

        const getDataFromResponse = response => {
            return response?.body?.books;
        }

        self.tableParams = new NgTableParams({group: "name", count: 100}, {
            getData: function ($defer, params) {
                if (params.filter().name || params.filter().description) {
                    return $filter('filter')(self.tableParams.settings().dataset, params.filter());
                }
                if (self.tableParams.shouldGetData === false) {
                    return self.tableParams.settings().dataset;
                }
                return BookService.get().then(function (data) {
                    console.log("Data get list from agents ajax ...", data);
                    self.originalData = angular.copy(getDataFromResponse(data));
                    self.tableParams.settings().dataset = getDataFromResponse(data);
                    return getDataFromResponse(data);
                });
            }
        });

        self.add = () => {
            console.log('adding');
            $mdDialog.show({
                scope: $scope,
                preserveScope: true,
                templateUrl: 'scripts/book/partials/addBookDialog.html',
                clickOutsideToClose: false
            });
        }

        self.edit = book => {
            book.isEditing = true;
        }

        self.cancel = book => {
            book.isEditing = false;
        }

        self.save = book => {
            console.log("saving")
            BookService.save(book).then(response => {
                if (response.status === 200) {
                    book.isEditing = false;
                    self.tableParams.shouldGetData = true;
                    self.tableParams.reload().then(function (data) {
                        if (data.length === 0 && self.tableParams.total() > 0) {
                            self.tableParams.page(self.tableParams.page() - 1);
                            self.tableParams.reload();
                        }
                    });
                } else {
                    alert('error occurred', response);
                }
            }).catch(e => {
                alert('error occurred', e);
            });
            $mdDialog.cancel();
        }

        self.deleteBook = book => {
            BookService.deleteBook(book).then(response => {
                if (response.status === 200) {
                    self.tableParams.shouldGetData = true;
                    self.tableParams.reload().then(function (data) {
                        if (data.length === 0 && self.tableParams.total() > 0) {
                            self.tableParams.page(self.tableParams.page() - 1);
                            self.tableParams.reload();
                        }
                    });
                } else {
                    alert('error occurred', response);
                }
            }).catch(e => {
                alert('error occurred', e);
            });
        }


    }
})();