(function () {
  angular.module('occIDEASApp.Book')
    .controller('BookCtrl', BookCtrl);

  BookCtrl.$inject = ['BookService', 'DifferenceUtil', 'NgTableParams', '$state', '$scope', '$filter', '$mdDialog'];

  function BookCtrl(BookService, DifferenceUtil, NgTableParams, $state, $scope, $filter, $mdDialog) {
    const self = this;

    self.downloadJson = (book, json, name = 'all') => {
      const url = window.URL.createObjectURL(new Blob([json]));
      const a = document.createElement('a');
      a.style.display = 'none';
      a.href = url;
      // the filename you want
      a.download = `${book}-${name}.json`;
      document.body.appendChild(a);
      a.click();
      window.URL.revokeObjectURL(url);
      alert('Download successful');
    }

    const getDataFromResponse = response => {
      return response?.body?.books;
    }
    self.copyOfData = [];
    self.tableParams = new NgTableParams({group: "name", count: 100}, {
      groupOptions: {
        isExpanded: false
      },
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

    self.deleteModuleInBook = (bookId, bookName, name) => {
      if (confirm(`Are you sure you would like to delete module ${name} in ${bookName}?`)) {
        BookService.deleteModuleInBook(bookId, name).then(response => {
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
        selectedModule: null,
        selectedCompareModule: null,
        results: {},
        diffInd: null,
        numberOfDiff: 0,
        reset() {
          this.compareComplete = false;
          this.numberOfDiff = 0;
          document.getElementById('visual').innerHTML = "";
        },
        top() {
          document.getElementById('topSummary').scrollIntoView(true);
          this.diffInd = null;
        },
        next() {
          const differences = document.querySelectorAll(".jsondiffpatch-modified");
          if (differences.length === 0) {
            alert('No difference.');
            return;
          }
          if (this.diffInd === null) {
            this.diffInd = 0;
          } else {
            this.diffInd = this.diffInd + 1;
          }
          if (this.diffInd === differences.length) {
            this.diffInd = differences.length - 1;
            alert('You are already at the last difference.');
            return;
          }
          if (this.diffInd <= differences.length - 1) {
            differences[this.diffInd].scrollIntoView(true);
          }
        },
        previous() {
          const differences = document.querySelectorAll(".jsondiffpatch-modified");
          if (differences.length === 0) {
            alert('No difference.');
            return;
          }
          if (this.diffInd === null) {
            this.diffInd = 0;
          } else {
            this.diffInd = this.diffInd - 1;
          }
          if (this.diffInd < 0) {
            this.diffInd = 0;
            alert('You are already at the first difference.');
            return;
          }

          if (this.diffInd >= 0) {
            differences[this.diffInd].scrollIntoView(true);
          }
        },
        cancel() {
          $mdDialog.cancel();
        },
        startComparison() {
          DifferenceUtil.getDiffBook(JSON.parse(this.selectedModule.json), JSON.parse(this.selectedCompareModule.json));
          this.compareComplete = true;
          const differences = document.querySelectorAll(".jsondiffpatch-modified");
          this.numberOfDiff = differences.length;
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