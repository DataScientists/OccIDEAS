(function () {
  angular.module('occIDEASApp.Book')
    .service('DifferenceUtil', DifferenceUtil);

  DifferenceUtil.$inject = [];

  function DifferenceUtil() {

    const identifyMissingModules = (firstBook, secondBook) => {
      const firstBookIdNodes = firstBook.map(module => module.jobModule.idNode);
      const secondBookIdNodes = secondBook.map(module => module.jobModule.idNode);
      console.log("firstBookIdNodes", firstBookIdNodes);
      console.log("secondBookIdNodes", secondBookIdNodes);

      const buildMissingModulesAsCommaStr = (missingModIdNodes1, missingModIdNodes2, book) => {
        return missingModIdNodes1.filter(idNode => !missingModIdNodes2.includes(idNode))
          .map(idNode => book.find(module => module.jobModule.idNode === idNode).fileName).join(",");
      }

      const buildMissingModulesAsIdNodes = (missingModIdNodes1, missingModIdNodes2) => {
        return missingModIdNodes1.filter(idNode => !missingModIdNodes2.includes(idNode));
      }

      return {
        firstBookMissingIdNodes: buildMissingModulesAsIdNodes(secondBookIdNodes, firstBookIdNodes),
        firstBookMissingModules: buildMissingModulesAsCommaStr(secondBookIdNodes, firstBookIdNodes, secondBook),
        secondBookMissingModules: buildMissingModulesAsCommaStr(firstBookIdNodes, secondBookIdNodes, firstBook),
        secondBookMissingIdNodes: buildMissingModulesAsIdNodes(firstBookIdNodes, secondBookIdNodes),
      }
    }

    const diff = (firstBook, secondBook) => {
      return identifyMissingModules(firstBook, secondBook);
    }

    return {
      diff
    };
  }

})();