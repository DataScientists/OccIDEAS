(function() {
  angular.module('occIDEASApp.Agents')
    .controller('AgentCtrl', AgentCtrl);

  AgentCtrl.$inject = ['AgentsService', 'NgTableParams', '$state', '$scope', '$filter', '$rootScope', '$mdDialog', 'ngToast'];

  function AgentCtrl(AgentsService, NgTableParams, $state, $scope, $filter, $rootScope, $mdDialog, $ngToast) {
    var self = this;
    self.isDeleting = false;
    var dirtyCellsByRow = [];
    var invalidCellsByRow = [];
    $scope.$root.tabsLoading = false;

    function loadStudyAgents() {
      AgentsService.loadStudyAgents().then(function(data) {
        $scope.studyAgents = data;
      });
    }

    loadStudyAgents();

    self.tableParams = new NgTableParams({group: "agentGroup.name", count: 100}, {
      getData: function($defer, params) {
        if(params.filter().name || params.filter().description || params.filter().isChecked) {
          return $filter('filter')(self.tableParams.settings().dataset, params.filter());
        }
        if(!self.tableParams.shouldGetData) {
          return self.tableParams.settings().dataset;
        }
        return AgentsService.get().then(function(data) {
          console.log("Data get list from agents ajax ...");
          _.each(data, function(agent) {
            if($scope.studyAgents && $scope.studyAgents.data) {
              var isStudyAgent = _.find($scope.studyAgents.data, function(studyAgents) {
                return agent.idAgent == studyAgents.value;
              });
              if(isStudyAgent) {
                agent.isChecked = true;
              }
            }
          });
          self.originalData = angular.copy(data);
          self.tableParams.settings().dataset = data;
          return data;
        });
      }
    });
    self.tableParams.shouldGetData = true;
    self.cancel = cancel;
    self.del = del;
    self.save = save;
    self.add = add;
    self.addGroup = addGroup;
    self.checkAndAddAgentRulesTab = checkAndAddAgentRulesTab;
    self.toggleIsDeleting = toggleIsDeleting;

    function toggleIsDeleting() {
      if(self.isDeleting) {
        self.isDeleting = false;
      } else {
        self.isDeleting = true;
      }
    }

    function add(group) {
      self.isEditing = true;
      self.isAdding = true;

      self.tableParams.settings().dataset.unshift({
        name: "New Agent",
        agentGroup: group.data[0].agentGroup,
        description: "New Agent Description",
        isEditing: true
      });
      self.originalData = angular.copy(self.tableParams.settings().dataset);
      self.tableParams.sorting({});
      self.tableParams.page(1);
      self.tableParams.shouldGetData = false;
      self.tableParams.reload();
      self.isAdding = false;
    }

    function cancel(row, rowForm) {
      var originalRow = resetRow(row, rowForm);

      if(row.idAgent) {
        //Undo existing
        angular.extend(row, originalRow);
      } else {
        _.remove(self.tableParams.settings().dataset, function(item) {
          return row === item;
        });
        self.tableParams.shouldGetData = false;
        self.tableParams.reload().then(function(data) {
          if(data.length === 0 && self.tableParams.total() > 0) {
            self.tableParams.page(self.tableParams.page() - 1);
            self.tableParams.reload();
          }
        });
      }
    }

    function del(row) {
      AgentsService.hasRules(row.idAgent).then(function(response) {
        if("false" == response.data) {
          AgentsService.deleteAgent({idAgent: row.idAgent}).then(function(resp) { // Delete agent here via ajax
            console.log("Deleted agent:", resp);
            if(200 == resp.status) {
              //_.remove(self.tableParams.settings().dataset, function (item) {
              //    return row === item;
              //});
              //self.tableParams.reload().then(function (data) {
              //    if (data.length === 0 && self.tableParams.total() > 0) {
              //        self.tableParams.page(self.tableParams.page() - 1);
              //        self.tableParams.reload();
              //    }
              //});

              self.tableParams.shouldGetData = true;
              self.tableParams.reload().then(function(data) {
                if(data.length === 0 && self.tableParams.total() > 0) {
                  self.tableParams.page(self.tableParams.page() - 1);
                  self.tableParams.reload();
                }
              });
            } else {
              console.log("Error when deleting agent:", resp);
            }
          });
        } else {
          $ngToast.create({
            className: 'danger',
            content: 'This agent already had rules belong to',
            dismissButton: true,
            dismissOnClick: false,
            animation: 'slide'
          });
        }
      });
    }

    function checkAndAddAgentRulesTab(row) {
      AgentsService.hasRules(row.idAgent).then(function(response) {
        if("false" == response.data) {
          $ngToast.create({
            className: 'danger',
            content: 'This agent has no rules',
            dismissButton: true,
            dismissOnClick: false,
            animation: 'slide'
          });
        } else {
          $rootScope.$emit("addAgentRulesTab", row);
        }
      });
    }

    function resetRow(row, rowForm) {
      row.isEditing = false;
      rowForm.$setPristine();
      self.tableTracker.untrack(row);
      return window._.find(self.originalData, {idAgent: row.idAgent});
    }

    function save(row, rowForm) {
      row.isEditing = false;
      AgentsService.save(row).then(function(response) {
        if(response.status === 200) {
          console.log('Agent Save was Successful!');
          row.idAgent = response.data;
        }
      });
    }

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

    $scope.toggleStudyAgent = function(row) {
      if($scope.checkboxes.items.length < 1) {
        return;
      }

      if($scope.checkboxes.items[row.idAgent]) {
        if(confirm('Are you sure you want to add ' + row.name + ' as study agent?')) {
          AgentsService.updateStudyAgents(row).then(function(response) {
            if(response.status === 200) {
              console.log('Added to Study Agent was Successful!');
              loadStudyAgents();
            }
          });
        } else {
          $scope.checkboxes.items[row.idAgent] = false;
        }
      } else {
        var studyAgent = _.find($scope.studyAgents.data, function(studyAgent) {
          return studyAgent.value == row.idAgent;
        });
        if(studyAgent) {
          if(confirm('Are you sure you want to delete ' + row.name + ' as study agent?')) {
            AgentsService.deleteStudyAgents(studyAgent).then(function(response) {
              if(response.status === 200) {
                console.log('Delete Study Agent was Successful!');
                loadStudyAgents();
              }
            });
          } else {
            $scope.checkboxes.items[row.idAgent] = true;
          }
        }
      }
    };

    $scope.checkboxes = {'checked': false, items: {}};

    // watch for check all checkbox
    $scope.$watch('checkboxes.checked', function(value) {
      angular.forEach(self.tableParams.settings().dataset, function(item) {
        if(angular.isDefined(item.idAgent)) {
          $scope.checkboxes.items[item.idAgent] = value;
        }
      });
    });

    // watch for data checkboxes
    $scope.$watch('checkboxes.items', function(values) {
      if(!self.tableParams.settings().dataset) {
        return;
      }
      var checked = 0, unchecked = 0,
        total = self.tableParams.settings().dataset.length;
      angular.forEach(self.tableParams.settings().dataset, function(item) {
        checked += ($scope.checkboxes.items[item.idAgent]) || 0;
        unchecked += (!$scope.checkboxes.items[item.idAgent]) || 0;
      });
      if((unchecked == 0) || (checked == 0)) {
        $scope.checkboxes.checked = (checked == total);
      }
      // grayed checkbox
      angular.element(document.getElementById("select_all")).prop("indeterminate", (checked != 0 && unchecked != 0));

    }, true);

    function addGroup(group) {
      //Show prompt
      $mdDialog.show({
        scope: $scope.$new(),
        templateUrl: 'scripts/agents/view/addGroupDialog.html',
        clickOutsideToClose: true
      });
    }

    $scope.cancel = function() {
      $mdDialog.cancel();
    };

    $scope.saveNewGroupButton = function(groupName, groupDesc, agentName, agentDesc) {
      var agentgroup = {
        name: groupName,
        description: groupDesc,
        agents: [{
          name: agentName,
          description: agentDesc
        }]
      };

      AgentsService.saveAgentGroup(agentgroup).then(function(resp) {
        if(resp.status === 200) {
          //Reload
          self.tableParams.reload();
        }
      });

      $scope.cancel();
    }
  }
})();

