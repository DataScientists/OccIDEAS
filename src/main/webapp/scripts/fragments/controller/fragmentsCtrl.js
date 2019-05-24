(function() {
  angular.module('occIDEASApp.Fragments')
    .controller('FragmentCtrl', FragmentCtrl);

  FragmentCtrl.$inject = ['FragmentsService', 'NgTableParams', '$state', '$scope', '$filter', '$mdDialog', '$q',
    'InterviewsService', '$timeout', 'QuestionsService', 'ModuleRuleService', 'NodeLanguageService', '$sessionStorage'];

  function FragmentCtrl(FragmentsService, NgTableParams, $state, $scope, $filter, $mdDialog, $q,
                        InterviewsService, $timeout, QuestionsService, ModuleRuleService, NodeLanguageService, $sessionStorage) {
    var self = this;
    self.isDeleting = false;
    var dirtyCellsByRow = [];
    var invalidCellsByRow = [];
    $scope.$root.tabsLoading = false;
    $scope.selectLanguage = undefined;
    $scope.selectedLanguage = {language: ''};
    $scope.languages = undefined;
    $scope.$storage = $sessionStorage;
    $scope.isLangEnabledOnLoad = angular.copy($scope.$storage.langEnabled);

    $scope.$watch('$storage.langEnabled', function(value) {
      if($scope.$storage.langEnabled && !$scope.isLangEnabledOnLoad) {
        self.tableParams.shouldGetData = true;
        self.tableParams.reload();
      }
    });

    $scope.openModuleLanguage = function(row) {
      $mdDialog.cancel();
      $scope.openFragmentLanguageTab($scope.selectedLanguage.language.id, row);
    };

    $scope.openModuleLanguageByFlagIcon = function(language) {
      var cloneLanguage = _.cloneDeep(language);
      if(language.topNodeId != 0) {
        cloneLanguage.idNode = language.topNodeId;
      }
      $scope.openFragmentLanguageTab(cloneLanguage.languageId, cloneLanguage);
    };

    self.displayNodeLanguage = function(row) {
      NodeLanguageService.getAllLanguage().then(function(response) {
        if(response.status == '200') {
          $scope.languages = response.data;
//					var english = {
//						id: -1,
//						language: 'EN'
//					}
//					$scope.languages.unshift(english);
          $scope.selectLanguage = $scope.languages[0];
          $scope.selectedLanguage.language = $scope.languages[0];
          safeDigest($scope.selectLanguage);
          var newScope = $scope.$new();
          newScope.row = row;
          $mdDialog.show({
            scope: newScope,
            templateUrl: 'scripts/modules/partials/translateDialog.html',
            parent: angular.element(document.body),
            clickOutsideToClose: true
          }).then(function(answer) {
            $scope.status = 'You said the information was "' + answer + '".';
          }, function() {
            $scope.status = 'You cancelled the dialog.';
          });
        }
      })
    };


    $scope.showRuleCount = function(row, $event) {
      ModuleRuleService.getRuleCountById(row.idNode).then(function(response) {
        if(response.status == '200') {
          row.ruleCount = response.data;
          safeDigest(row);
        }
      });
      if($event.stopPropagation) $event.stopPropagation();
      if($event.preventDefault) $event.preventDefault();
      $event.cancelBubble = true;
      $event.returnValue = false;
    };
    var safeDigest = function(obj) {
      if(!obj.$$phase) {
        try {
          obj.$digest();
        } catch(e) {
        }
      }
    };

    $scope.cancel = function() {
      $mdDialog.cancel();
    };

    $scope.cancelChanges = function() {
      $scope.cancel();
      cancel($scope.row, $scope.rowForm);
    };

    $scope.updateInterviewModuleNames = function() {
      var row = $scope.row;
      if($scope.interviewExist) {
        $scope.cancel();
        $scope.interviewIdCount = $scope.interviewDataForUpdate.length;
        $scope.counter = 0;
        $scope.interviewCount = $scope.counter;
        $mdDialog.show({
          scope: $scope,
          preserveScope: true,
          templateUrl: 'scripts/fragments/partials/updateInterviewFragmentDialog.html',
          clickOutsideToClose: false
        });
        $scope.interviewDataForUpdate.reduce(function(p, data) {
          return p.then(function() {
            $scope.interviewIdInProgress = data.interviewId;
            $scope.counter++;
            $scope.interviewCount = $scope.counter;
            return InterviewsService.updateModuleNameForInterviewId(data.interviewPrimaryKey, row.name)
              .then(function(response) {
                if(response.status == 200) {
                  console.log("successful update for " + data.interviewId);
                }
              });
          });
        }, $q.when(true)).then(function(finalResult) {
          $scope.cancel();
          //update linking nodes
          $scope.linkTotalCount = $scope.linkingNodesForUpdate.length;
          $scope.linkCounter = 0;
          $scope.linkCount = $scope.linkCounter;
          $mdDialog.show({
            scope: $scope,
            preserveScope: true,
            templateUrl: 'scripts/fragments/partials/updateLinkFragmentDialog.html',
            clickOutsideToClose: false
          });
          $scope.linkingNodesForUpdate.reduce(function(p, data) {
            return p.then(function() {
              $scope.linkIdInProgress = data.idNode;
              $scope.linkCounter++;
              $scope.linkCount = $scope.linkCounter;
              data.name = row.name;
              return QuestionsService.save(data).then(function(response) {
                if(response.status == 200) {
                  console.log("successful update for " + data.idNode);
                }
              });
            });
          }, $q.when(true)).then(function(finalResult) {
            $scope.cancel();
            $timeout(function() {
              FragmentsService.save(row).then(function(response) {
                if(response.status === 200) {
                  console.log('Fragment Save was Successful!');
                  self.tableParams.shouldGetData = true;
                  self.tableParams.reload().then(function(data) {
                    if(data.length === 0 && self.tableParams.total() > 0) {
                      self.tableParams.page(self.tableParams.page() - 1);
                      self.tableParams.reload();
                    }
                  });
                }
              });
            }, 1000);
          });
        }, function(err) {
          console.log('error');
        });
      } else if($scope.linkingNodeExist) {
        //update linking nodes
        $scope.cancel();
        $scope.linkTotalCount = $scope.linkingNodesForUpdate.length;
        $scope.linkCounter = 0;
        $scope.linkCount = $scope.linkCounter;
        $mdDialog.show({
          scope: $scope,
          preserveScope: true,
          templateUrl: 'scripts/fragments/partials/updateLinkFragmentDialog.html',
          clickOutsideToClose: false
        });
        $scope.linkingNodesForUpdate.reduce(function(p, data) {
          return p.then(function() {
            $scope.linkIdInProgress = data.idNode;
            $scope.linkCounter++;
            $scope.linkCount = $scope.linkCounter;
            data.name = row.name;
            return QuestionsService.save(data).then(function(response) {
              if(response.status == 200) {
                console.log("successful update for " + data.idNode);
              }
            });
          });
        }, $q.when(true)).then(function(finalResult) {
          $scope.cancel();
          $timeout(function() {
            FragmentsService.save(row).then(function(response) {
              if(response.status === 200) {
                console.log('Fragment Save was Successful!');
                self.tableParams.shouldGetData = true;
                self.tableParams.reload().then(function(data) {
                  if(data.length === 0 && self.tableParams.total() > 0) {
                    self.tableParams.page(self.tableParams.page() - 1);
                    self.tableParams.reload();
                  }
                });
              }
            });
          }, 1000);
        });
      }
    };

    self.tableParams = new NgTableParams({group: "type"}, {
      getData: function(params) {
        if(params.filter().name || params.filter().description) {
          return $filter('filter')(self.tableParams.settings().dataset, params.filter());
        }
        if(!self.tableParams.shouldGetData) {
          return self.tableParams.settings().dataset;
        }
        return FragmentsService.get().then(function(data) {
          console.log("Data get list from fragments ajax ...");
          if(!data) {
            self.originalData = angular.copy(data);
            self.tableParams.settings().dataset = data;
            self.tableParams.shouldGetData = true;
          } else {
            if($sessionStorage.langEnabled) {
              NodeLanguageService.getNodeNodeLanguageFragmentList().then(function(response) {
                if(response.status == '200') {
                  $scope.nodeNodeLanguageMap = response.data;
                  _.each(data, function(dataTemp) {
                    dataTemp.flags = _.uniqBy(_.filter($scope.nodeNodeLanguageMap, function(nodeNodeLanguageMapTemp) {
                      return dataTemp.idNode == nodeNodeLanguageMapTemp.idNode ||
                        dataTemp.idNode == nodeNodeLanguageMapTemp.topNodeId;
                    }), 'flag');
                  });
                  self.originalData = angular.copy(data);
                  self.tableParams.settings().dataset = data;
                  self.tableParams.shouldGetData = true;
                } else {
                  self.originalData = angular.copy(data);
                  self.tableParams.settings().dataset = data;
                  self.tableParams.shouldGetData = true;
                }
              });
            } else {
              self.originalData = angular.copy(data);
              self.tableParams.settings().dataset = data;
              self.tableParams.shouldGetData = true;
            }
          }
          data.map(function(x) {
            if(x.type === 'F_ajsm')
              x['type'] = " Task Modules";
          });
          return data;
        });
      },
    });
    self.tableParams.shouldGetData = true;
    self.treeView = treeView;
    self.cancel = cancel;
    self.del = del;
    self.save = save;
    self.add = add;

    self.toggleIsDeleting = toggleIsDeleting;

    function toggleIsDeleting() {
      if(self.isDeleting) {
        self.isDeleting = false;
      } else {
        self.isDeleting = true;
      }
    }

    function add(type) {
      self.isEditing = true;
      self.isAdding = true;

      self.tableParams.settings().dataset.unshift({
        name: "New Task Module",
        placeholder: "New Task Module",
        type: type,
        description: "New Description",
        isEditing: true
      });
      self.originalData = angular.copy(self.tableParams.settings().dataset);
      self.tableParams.sorting({});
      self.tableParams.page(1);
      self.tableParams.shouldGetData = false;
      self.tableParams.reload();
      self.isAdding = false;
    }

    function treeView(row) {
      $state.go("questionView", {row: row});
    }

    function cancel(row, rowForm) {
      var originalRow = resetRow(row, rowForm);
      if(row.idNode) {
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
      row.deleted = 1;
      row.name = 'DELETED_' + row.name;
      var data = FragmentsService.deleteFragment(row).then(function(response) {
        if(response.status === 200) {
          console.log('Module was deleted!');
          self.tableParams.shouldGetData = true;
          self.tableParams.reload().then(function(data) {
            if(data.length === 0 && self.tableParams.total() > 0) {
              self.tableParams.page(self.tableParams.page() - 1);
              self.tableParams.reload();
            }
          });
        }
      });
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

    function resetRow(row, rowForm) {
      row.isEditing = false;
      rowForm.$setPristine();
      self.tableTracker.untrack(row);
      return window._.find(self.originalData, {idNode: row.idNode});
    }

    function save(row, rowForm) {
      self.isEditing = false;
      $scope.row = row;
      $scope.rowForm = rowForm;
      if(row.idNode) {
        $scope.interviewExist = false;
        $scope.linkingNodeExist = false;
        $scope.validationInProgress = true;
        $mdDialog.show({
          scope: $scope,
          preserveScope: true,
          templateUrl: 'scripts/fragments/partials/validateFragmentDialog.html',
          clickOutsideToClose: false
        });
        FragmentsService.findInterviewByFragmentId(row.idNode).then(function(response) {
          if(response.status == 200) {

            if(response.data.length > 0) {
              $scope.interviewExist = true;
              $scope.interviewDataForUpdate = response.data;
            }
            //check if fragment is linked on other modules
            FragmentsService.getLinkingNodes(row.idNode).then(function(response) {
              if(response.status == 200) {
                $scope.validationInProgress = false;
                if(response.data.length > 0) {
                  $scope.linkingNodeExist = true;
                  $scope.linkingNodesForUpdate = response.data;
                } else if(!$scope.linkingNodeExist && !$scope.interviewExist) {
                  $mdDialog.cancel();
                  FragmentsService.save(row).then(function(response) {
                    if(response.status === 200) {
                      console.log('Fragment Save was Successful!');
                      self.tableParams.shouldGetData = true;
                      self.tableParams.reload().then(function(data) {
                        if(data.length === 0 && self.tableParams.total() > 0) {
                          self.tableParams.page(self.tableParams.page() - 1);
                          self.tableParams.reload();
                          $location.hash("");
                          $anchorScroll();
                        }
                      });
                    }
                  });
                }
              }
            });
          }
        });
      } else {
        FragmentsService.save(row).then(function(response) {
          if(response.status === 200) {
            console.log('Fragment Save was Successful!');
            self.tableParams.shouldGetData = true;
            self.tableParams.reload().then(function(data) {
              if(data.length === 0 && self.tableParams.total() > 0) {
                self.tableParams.page(self.tableParams.page() - 1);
                self.tableParams.reload();
                $location.hash("");
                $anchorScroll();
              }
            });
          }
        });
      }
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

    $scope.validateAllBtn = function(shouldFilter) {
      //Show validation popup
      $scope.validationAllModuleInProgress = true;
      $mdDialog.show({
        scope: $scope.$new(),
        templateUrl: 'scripts/modules/partials/validateAllModuleDialog.html',
        parent: angular.element(document.body),
        clickOutsideToClose: true
      });

      FragmentsService.getAllFragmentsReport().then(function(response) {
        if(response.status == 200) {
          $scope.validationAllModuleInProgress = false;
          if(response.data.length > 0) {
            //Open tab
            $scope.addAllFragmentValidationTab(response.data);
            $mdDialog.cancel();
          } else {
            console.log("No data to validate");
            $mdDialog.cancel();
          }
        }
      });
    };

    $scope.issuePopOver = {
      templateUrl: 'scripts/modules/partials/issuePopOver.html'
    };

    $scope.showLinkPopup = function(row) {
      $scope.fragmentLinkProgress = true;

      $mdDialog.show({
        scope: $scope.$new(),
        templateUrl: 'scripts/fragments/partials/fragmentLinkDialog.html',
        parent: angular.element(document.body),
        clickOutsideToClose: true
      });

      FragmentsService.getFragmentParents(row.idNode).then(function(response) {
        if(response.status == 200) {
          $scope.fragmentLinkProgress = true;
          if(response.data.length > 0) {
            //Open tab
            $scope.addFragmentsLinkTab(response.data);
            $mdDialog.cancel();
          } else {
            console.log("No data to validate");
            $mdDialog.cancel();
          }
        } else {
          $scope.fragmentLinkProgress = true;
          console.log("getFragmentParents " + response.status);
        }
      });
    };
  }
})();

