(function () {
  angular.module('occIDEASApp.Book')
    .controller('BookCtrl', BookCtrl);

  BookCtrl.$inject = ['BookService', 'DifferenceUtil', 'NgTableParams', '$state', '$scope', '$filter', '$mdDialog'];

  function BookCtrl(BookService, DifferenceUtil, NgTableParams, $state, $scope, $filter, $mdDialog) {
    const self = this;

    const getDataFromResponse = response => {
      return response?.body?.books;
    }
    self.copyOfData = [];
    self.tableParams = new NgTableParams({group: "name", count: 100}, {
      getData: function ($defer, params) {
        if (params.filter().name || params.filter().description) {
          return $filter('filter')(self.tableParams.settings().dataset, params.filter());
        }
        if (self.tableParams.shouldGetData === false) {
          return self.tableParams.settings().dataset;
        }
        return BookService.get().then(function (data) {
          self.copyOfData = getDataFromResponse(data);
          self.originalData = angular.copy(self.data);
          self.tableParams.settings().dataset = self.data;
          return getDataFromResponse(data);
        });
      }
    });

    self.add = () => {
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
      $mdDialog.cancel();
    }

    self.save = book => {
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
      const {name, ...other} = book.data[0];
      if (confirm(`Are you sure you would like to delete the book ${name}?`)) {
        BookService.deleteBook(other).then(response => {
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

    self.deleteModuleInBook = (bookName, module) => {
      const {bookId, idNode, fileName} = module;
      if (confirm(`Are you sure you would like to delete module ${fileName} in ${bookName}?`)) {
        BookService.deleteModuleInBook(bookId, idNode).then(response => {
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

    self.compareBooks = group => {
      console.log("comparing book ", group);
      const book = self.copyOfData.find(b => b.name === group.value);
      const filteredBooks = self.copyOfData.filter(b => b.id !== book.id);
      if (filteredBooks.length === 0) {
        alert("You need to add more books for comparison.");
        return;
      }

      if (book.modules.length === 0) {
        alert("No modules exist for this book");
        return;
      }

      const compareScope = {
        books: filteredBooks,
        compareComplete: false,
        book,
        firstBook: book.modules,
        secondBook: null,
        selectedBook: null,
        compareToBook: null,
        results: {},
        cancel() {
          $mdDialog.cancel();
        },
        notExistInBook(module, bookNum = 0) {
          if (!this.compareComplete || !this.secondBook) {
            return;
          }
          switch (bookNum) {
            case 1 : {
              return this.results.secondBookMissingIdNodes.includes(module.jobModule.idNode)
            }
            default: {
              return this.results.firstBookMissingIdNodes.includes(module.jobModule.idNode)
            }
          }
        },
        hasDifference(node) {
          if (!node) {
            return false;
          }

          if (!this.compareComplete || !this.results.difference.length) {
            return false;
          }

          return this.results.difference.includes(node.idNode)
        },
        isToggled(node) {
          if (!node) {
            return false;
          }

          if (!this.compareComplete) {
            return false;
          }

          return this.results.toggleDiffs.includes(`${node.idNode}-toggle-1`);
        },
        toggle(node) {
          if (!node) {
            return false;
          }

          if (!this.compareComplete) {
            return false;
          }

          const toggledFalseIndex = this.results.toggleDiffs.indexOf(`${node.idNode}-toggle-0`);
          const toggledTrueIndex = this.results.toggleDiffs.indexOf(`${node.idNode}-toggle-1`);

          if (toggledFalseIndex !== -1) {
            this.results.toggleDiffs[toggledFalseIndex] = `${node.idNode}-toggle-1`;
          } else {
            this.results.toggleDiffs[toggledTrueIndex] = `${node.idNode}-toggle-0`;
          }
        },
        startComparison() {
          console.log('your book ', this.book);
          this.compareToBook = this.books.find(b => b.id === this.selectedBook);
          console.log('compareToBook ', this.compareToBook);
          this.secondBook = this.compareToBook.modules;
          this.results = DifferenceUtil.diff(this.firstBook, this.secondBook);
          this.compareComplete = true;
        }
      }
      $scope.compareScope = {...compareScope};
      $mdDialog.show({
        scope: $scope,
        preserveScope: true,
        templateUrl: 'scripts/book/partials/comparisonDialog.html',
        clickOutsideToClose: false
      });
    }

    self.startComparison = () => {

    }
  }
})();