(function () {
    angular.module('occIDEASApp.Book')
        .service('BookService', BookService);

    BookService.$inject = ['$http'];

    function BookService($http) {
        const apiUrl = 'web/rest/';
        const bookEndPoint = apiUrl + 'book';

        const getBook = function () {
            return $http.get(bookEndPoint, {cache: false}).then(function (response) {
                const data = response.data;
                console.log('data', data);
                return data;
            });
        };

        return {
            get: getBook
        };
    }

})();