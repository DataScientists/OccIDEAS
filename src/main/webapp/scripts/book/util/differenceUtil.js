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

    const getDiff = (firstBookJobModule, secondBookJobModule, diff = []) => {
      if (firstBookJobModule.deleted !== secondBookJobModule.deleted
        || firstBookJobModule.name !== secondBookJobModule.name
        || firstBookJobModule.number !== secondBookJobModule.number
        || firstBookJobModule.topNodeId !== secondBookJobModule.topNodeId
        || firstBookJobModule.parentId !== secondBookJobModule.parentId
        || firstBookJobModule.link !== secondBookJobModule.link) {
        diff.push(firstBookJobModule.idNode);
      }
      if (firstBookJobModule.nodes.length === 0 && secondBookJobModule.nodes.length === 0) {
        return diff;
      }
      if (firstBookJobModule.nodes.length && !secondBookJobModule.nodes.length) {
        diff.push(firstBookJobModule.idNode);
        return diff;
      }
      if (!firstBookJobModule.nodes.length && secondBookJobModule.nodes.length) {
        diff.push(firstBookJobModule.idNode);
        return diff;
      }
      firstBookJobModule.nodes.forEach(node => {
        const secModuleNode = secondBookJobModule.nodes.find(secondNode => node.idNode === secondNode.idNode);
        if (!secModuleNode) {
          diff.push(node.idNode);
        } else {
          getDiff(node, secModuleNode, diff);
        }
      })
      secondBookJobModule.nodes.forEach(node => {
        const firstModuleNode = firstBookJobModule.nodes.find(secondNode => node.idNode === secondNode.idNode);
        if (!firstModuleNode) {
          diff.push(node.idNode);
        } else {
          getDiff(node, firstModuleNode, diff);
        }
      })
      return diff;
    }

    const getDiffBook = (firstBook, secondBook) => {
      const firstBookJobModules = firstBook.map(module => module.jobModule);
      const secondBookJobModules = secondBook.map(module => module.jobModule);
      let difference = [];
      firstBookJobModules.forEach(jobModule1 => {
        const secondBookJobModule = secondBookJobModules.find(jobModule2 => jobModule2.idNode === jobModule1.idNode);
        if (secondBookJobModule) {
          const foundDifference = getDiff(jobModule1, secondBookJobModule);
          if (foundDifference.length) {
            difference = [...difference, ...foundDifference, jobModule1.idNode];
          } else {
            difference = [...difference, ...foundDifference];
          }
        }
      });
      console.log('difference', difference);

      const toggleDiffs = difference.map(diff => `${diff}-toggle-0`);

      return {
        difference,
        toggleDiffs
      }
    }

    const diff = (firstBook, secondBook) => {
      return {...identifyMissingModules(firstBook, secondBook), ...getDiffBook(firstBook, secondBook)};
    }

    return {
      diff
    };
  }

})();