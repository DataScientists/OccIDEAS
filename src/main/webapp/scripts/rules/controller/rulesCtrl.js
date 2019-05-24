(function() {
  angular.module('occIDEASApp.Rules')
    .controller('RulesCtrl', RulesCtrl);
  RulesCtrl.$inject = ['RulesService', 'ngTableParams', '$state', '$scope', '$rootScope', 'RulesCache', '$filter',
    '$anchorScroll', '$location', 'templateData', 'QuestionsService', 'data'];

  function RulesCtrl(RulesService, NgTableParams, $state, $scope, $rootScope, RulesCache, $filter,
                     $anchorScroll, $location, templateData, QuestionsService, data) {
    var self = this;
    self.isDeleting = false;
    var dirtyCellsByRow = [];
    var invalidCellsByRow = [];

    function transformRuleDataForTable(data) {
      var moduleRules = [];
      _.each(data, function(moduleRule) {
        var additionalFields = '';
        if(moduleRule.rule.ruleAdditionalfields[0]) {
          additionalFields = moduleRule.rule.ruleAdditionalfields[0].value;
        }
        if(moduleRule.rule.ruleAdditionalfields[1]) {
          additionalFields += ':' + moduleRule.rule.ruleAdditionalfields[1].value;
        }

        var moduleRuleFlat = {
          moduleName: moduleRule.moduleName,
          agentName: moduleRule.agentName,
          idRule: moduleRule.rule.idRule,
          idNode: moduleRule.idNode,
          nodeNumber: moduleRule.nodeNumber,
          ruleType: moduleRule.rule.type,
          ruleLevel: moduleRule.ruleLevel,
          additionalFields: additionalFields,
          valid: 'maybe'//validation todo
        };
        moduleRules.push(moduleRuleFlat);
      });
      return moduleRules;
    }

    //var tableData = transformRuleDataForTable(data);

    //$scope.tableData = [{moduleName:"test1",agentName:"agent2"},{moduleName:"test1",agentName:"agent3"}];
    $scope.tableData = transformRuleDataForTable(data);
    this.cols = [{
      field: "moduleName",
      title: "Module",
      sortable: "moduleName",
      show: false,
      groupable: "moduleName"
    }, {
      field: "agentName",
      title: "Agent",
      sortable: "agentName",
      show: true,
      groupable: "agentName"
    }, {
      field: "idRule",
      title: "Rule Id",
      sortable: "idRule",
      show: true
    }, {
      field: "nodeNumber",
      title: "nodeNumber",
      sortable: "nodeNumber",
      show: true,
      groupable: "nodeNumber"
    }, {
      field: "idNode",
      title: "idNode",
      show: false,
    }, {
      field: "ruleType",
      title: "ruleType",
      sortable: "ruleType",
      show: true
    }
      , {
        field: "ruleLevel",
        title: "ruleLevel",
        sortable: "ruleLevel",
        show: true
      }, {
        field: "additionalFields",
        title: "additionalFields",
        show: true
      }, {
        field: "valid",
        title: "valid",
        show: true
      }];

    var groupOn = {moduleName: "desc"};
    if(templateData.moduleId) {
      groupOn = {agentName: "desc"}
    }
    self.tableParams = new NgTableParams({
      // initial grouping
      count: 10,
      group: groupOn
    }, {
      dataset: $scope.tableData,
      groupOptions: {
        isExpanded: false
      }
    });


    /*self.tableParams = new NgTableParams(
        {
          page: 1,
          count: 5,
          group: {
            agentName: "desc"
              }
        },
        {
          getData: function($defer,params) {
                              if(params.filter().name || params.filter().description){
                                return $filter('filter')(self.tableParams.settings().dataset, params.filter());
                              }
                              if(!self.tableParams.shouldGetData){
                                  return self.tableParams.settings().dataset;
                              }
                              console.log("templateData", templateData);
                              if(templateData.moduleId){
                                  return  RulesService.listByModule(templateData.moduleId).then(function(data) {
                                      console.log("Data getting from moduleruless ajax ... id:"+templateData.moduleId);
                                      self.originalData = angular.copy(data);
                                      self.tableParams.settings().dataset = data;
                                      self.tableParams.shouldGetData = true;
                                      $defer.resolve();
                                      return data;
                                  });
                              }else if(templateData.agentId){
                                  return RulesService.listByAgent(templateData.agentId).then(function(response) {
                                      console.log("Data getting from agentrules ajax ... id:"+templateData.agentId);
                                      self.originalData = angular.copy(response);
                                      self.tableParams.settings().dataset = response;
                                      self.tableParams.shouldGetData = true;
                                      self.tableParams.total(self.originalData.length);
                                      var last = params.page() * params.count();
                        return _.slice(response,last - params.count(),last);

                                  });
                              }
                              if(!self.tableParams.shouldGetData){
                      var last = params.page() * params.count();
                        return _.slice(self.tableParams.settings().dataset,last - params.count(),last);
                              }
                              return RulesService.listByAgent(templateData.agentId).then(function(response) {
                                  console.log("Data getting from agentrules ajax ... id:"+templateData.agentId);
                                  self.originalData = angular.copy(response);
                                  self.tableParams.settings().dataset = response;
                                  self.tableParams.shouldGetData = true;
                                  self.tableParams.total(self.originalData.length);
                                  self.tableParams.count = 5;
                                  self.tableParams.settings().counts = [];
                                  var last = params.page() * params.count();
                      return _.slice(response,last - params.count(),last);


                              });
                  }

        });

    self.tableParams.shouldGetData = true;*/


    $rootScope.tabsLoading = false;

    function setInvalid(isInvalid) {
      self.$invalid = isInvalid;
      self.$valid = !isInvalid;
    }

    function untrack(row) {
      _.remove(invalidCellsByRow, function(item) {
        return item.row === row;
      });
      _.remove(dirtyCellsByRow, function(item) {
        return item.row === row;
      });
      setInvalid(invalidCellsByRow.length > 0);
    }

    $scope.nodePopover = {
      templateUrl: 'scripts/questions/partials/nodePopover.html',
      open: function(x, nodeclass) {
        if(!nodeclass) {
          nodeclass = 'P';
        }
        if(!x.idNode) {
          var convertX = {};
          convertX.idNode = x;
          x = convertX;
        }
        if(angular.isUndefined(x.info)) {
          x.info = [];
        }
        x.info["Node" + x.idNode] = {
          idNode: x.idNode,
          nodeclass: nodeclass,
          nodePopover: {
            isOpen: false
          },
          nodePopoverInProgress: false
        };
        var nodeInPopup = x.info["Node" + x.idNode];
        nodeInPopup.nodePopover.isOpen = true;
        nodeInPopup.nodePopoverInProgress = true;

        if(nodeclass == 'P') {
          QuestionsService.findPossibleAnswer(nodeInPopup.idNode).then(function(data) {
            nodeInPopup.data = data.data[0];
            nodeInPopup.nodePopoverInProgress = false;
          });
        } else {
          QuestionsService.findQuestion(nodeInPopup.idNode).then(function(data) {
            nodeInPopup.data = data.data[0];
            nodeInPopup.nodePopoverInProgress = false;
          });
        }

      },
      close: function close(x) {
        x.info["Node" + x.idNode].nodePopover.isOpen = false;
      }
    };
  }
})();

