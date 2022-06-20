(function () {
    angular.module('occIDEASApp.Book')
        .controller('BookCtrl', BookCtrl);

    BookCtrl.$inject = ['BookService', 'NgTableParams', '$state', '$scope', '$filter'];

    function BookCtrl(BookService, NgTableParams, $state, $scope, $filter) {
        const self = this;

        self.newBook = {};

        const getDataFromResponse = response => {
            return response?.body?.books;
        }

        self.tableParams = new NgTableParams({count: 100}, {
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
            self.isEditing = true;
            self.isAdding = true;

            self.tableParams.settings().dataset.unshift({
                name: "New Book",
                isEditing: true
            });
            self.originalData = angular.copy(self.tableParams.settings().dataset);
            self.tableParams.sorting({});
            self.tableParams.page(1);
            self.tableParams.shouldGetData = false;
            self.tableParams.reload();
            self.isAdding = false;
        }

        self.cancel = book => {
            self.isEditing = false;
        }

        self.save = book => {
            BookService.save(book).then(response => {
                if (response.status === 200) {
                    self.tableParams.shouldGetData = true;
                    self.tableParams.reload().then(function (data) {
                        if (data.length === 0 && self.tableParams.total() > 0) {
                            self.tableParams.page(self.tableParams.page() - 1);
                            self.tableParams.reload();
                        }
                    });
                }
            });
        }
    }
})();