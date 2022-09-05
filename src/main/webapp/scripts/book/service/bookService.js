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
                return data;
            });
        };

        const save = function (data) {
            const request = $http({
                method: 'POST',
                url: bookEndPoint,
                data
            });
            return request;
        };

        const deleteBook = function (data) {
            const request = $http({
                method: 'DELETE',
                url: `${bookEndPoint}/${data.id}`,
            });
            return request;
        };

        const deleteModuleInBook = function (bookId, name) {
            const request = $http({
                method: 'DELETE',
                url: `${bookEndPoint}/${bookId}/${name}`,
            });
            return request;
        };

        const saveToBook = (idNode, bookId) => {
            const request = $http({
                method: 'POST',
                url: `${bookEndPoint}/addToBook`,
                data: {
                    idNode,
                    bookId
                }
            });
            return request;
        }

        const uploadToBook = (formData) => {
            const request = $http({
                method: 'POST',
                url: `${bookEndPoint}/uploadJsonToBook`,
                data: formData,
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            });
            return request;
        }

        const getModuleInBook = function (bookId, name) {
            return $http.get(`${bookEndPoint}/${bookId}/${name}`, {cache: false}).then(function (response) {
                const data = response.data;
                return data;
            });
        };

        return {
            get: getBook,
            save,
            deleteBook,
            saveToBook,
            deleteModuleInBook,
            uploadToBook,
            getModuleInBook
        };
    }

})();