(function () {
  angular.module('occIDEASApp.Book')
    .service('DifferenceUtil', DifferenceUtil);

  DifferenceUtil.$inject = [];

  function DifferenceUtil() {

    const getDiffBook = (firstBook, secondBook) => {
        const delta = jsondiffpatch.create({
            propertyFilter: function (name, context) {
                return name.slice(0, 1) !== '$' && name !== 'bookId' && name !== 'lastUpdated' && name !== 'moduleRule' && name !== 'rule';
            }
        }).diff(firstBook, secondBook);

        if (delta) {
            document.getElementById('visual').innerHTML = jsondiffpatch.formatters.html.format(delta, firstBook);
        } else {
            document.getElementById('visual').innerHTML = JSON.stringify(firstBook);
        }
    }

    return {
        getDiffBook
    };
  }

})();