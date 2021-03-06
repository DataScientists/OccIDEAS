(function() {
  angular.module('occIDEASApp.Modules')
    .controller('ModuleCtrl', ModuleCtrl);
  ModuleCtrl.$inject = ['ModulesService', 'ngTableParams', '$state', '$scope', 'ModulesCache', '$filter',
    '$anchorScroll', '$location', '$log', '$mdDialog', 'Upload', '$timeout', 'InterviewsService'
    , '$q', 'ngToast', 'ModuleRuleService', 'NodeLanguageService',
    '$sessionStorage'];

  function ModuleCtrl(ModulesService, NgTableParams, $state, $scope, ModulesCache, $filter,
                      $anchorScroll, $location, $log, $mdDialog, Upload, $timeout, InterviewsService, $q,
                      $ngToast, ModuleRuleService, NodeLanguageService, $sessionStorage) {
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
      $scope.openModuleLanguageTab($scope.selectedLanguage.language.id, row);
    };

    $scope.openModuleLanguageByFlagIcon = function(language) {
      var cloneLanguage = _.cloneDeep(language);
      if(language.topNodeId != 0) {
        cloneLanguage.idNode = language.topNodeId;
      }
      $scope.openModuleLanguageTab(cloneLanguage.languageId, cloneLanguage);
    };


    self.displayNodeLanguage = function(row) {
      if($sessionStorage.languages) {
        $scope.languages = angular.copy($sessionStorage.languages);
        _.remove($scope.languages, function(o) {
          return o.language == 'GB';
        });
        $scope.selectedLanguage.language = $scope.languages[0];
      }
      var newScope = $scope.$new();
      newScope.row = row;
      newScope.languages = $scope.languages;
      $mdDialog.show({
        scope: newScope,
        templateUrl: 'scripts/modules/partials/translateDialog.html',
        parent: angular.element(document.body),
        clickOutsideToClose: true
      })
        .then(function(answer) {
          $scope.status = 'You said the information was "' + answer + '".';
        }, function() {
          $scope.status = 'You cancelled the dialog.';
        });
    };

//	    NodeLanguageService.getDistinctLanguage().then(function(response){
//	    	if(response.status == '200'){
//	    		$scope.flags = response.data;
//	    	}
//	    });

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

    $scope.validatePopOver = {
      templateUrl: 'scripts/modules/partials/validatePopOver.html',
      open: function(row) {
        if(angular.isUndefined(row.info)) {
          row.info = [];
        }
        row.info["Mod" + row.idNode] = {
          idNode: row.idNode,
          name: row.name,
          modPopover: {
            isOpen: false
          },
          modPopoverInProgress: false
        };
        var modInPopup = row.info["Mod" + row.idNode];
        modInPopup.modPopover.isOpen = true;
      },
      close: function close(row) {
        row.info["Mod" + row.idNode].modPopover.isOpen = false;
      }
    };
    var validationFilterEnabled = false;

    function isValidationFilterEnabled() {
      if(self.tableParams.filter().isDuplicate == true) {
        validationFilterEnabled = true;
      } else {
        validationFilterEnabled = false;
      }
      return validationFilterEnabled;
    }

    function cleanseValidation() {
      _.each(self.tableParams.settings().dataset, function(row) {
        delete row.info;
        delete row.isDuplicate;
        delete row.duplicateItems;
      });
      self.tableParams.filter().isDuplicate = undefined;
    }

    function checkUniqueModule(module) {
      var data = self.tableParams.settings().dataset;
      var duplicateItem =
        _.filter(data, function(item) {
          //exclude the current module for the check
          if(item.idNode == module.idNode) {
            return false;
          }
          if(_.startsWith(item.name, module.name.substring(0, 4))) {
            item.isDuplicate = true;
            return true;
          }
          return false;
        });
      if(duplicateItem.length > 0) {
        module.isDuplicate = true;
        module.duplicateItems = duplicateItem;
      }
    }

    $scope.filterValidateItems = function() {
      self.tableParams.filter().isDuplicate = true;
    };

    $scope.unfilterValidateItems = function() {
      self.tableParams.filter().isDuplicate = false;
      $scope.validateBtn(false);
    };

    $scope.validateAllBtn = function(shouldFilter) {
      //Show validation popup
      $scope.validationAllModuleInProgress = true;
      $mdDialog.show({
        scope: $scope.$new(),
        templateUrl: 'scripts/modules/partials/validateAllModuleDialog.html',
        parent: angular.element(document.body),
        clickOutsideToClose: true
      });

      ModulesService.getAllModulesReport().then(function(response) {
        if(response.status == 200) {
          $scope.validationAllModuleInProgress = false;
          if(response.data.length > 0) {
            //Open tab
            $scope.addAllModuleValidationTab(response.data);
            $mdDialog.cancel();
          } else {
            console.log("No data to validate");
            $mdDialog.cancel();
          }
        }
      });
    };

    $scope.validateBtn = function(shouldFilter) {
      //check if we have data
      var data = self.tableParams.settings().dataset;
      if(data.length > 0) {
        _.each(data, function(val) {
          checkUniqueModule(val);
        });
        // check if there are items with errors
        var result = _.find(data, function(item) {
          return item.isDuplicate == true;
        });
        if(result && (shouldFilter == true)) {
          self.tableParams.filter().isDuplicate = true;
        }
      } else {
        $ngToast.create({
          className: 'danger',
          content: 'Cannot validate no modules are available',
          animation: 'slide'
        });

      }
    };

    $scope.uploadFiles = function(file, errFiles) {
      $scope.f = file;
      $scope.errFile = errFiles && errFiles[0];
    };

    $scope.importJsonBtn = function() {
      var file = $scope.f;
      if(file) {
        file.upload = Upload.upload({
          url: 'web/rest/module/importJson',
          file: file
        });

        file.upload.then(function(response) {
          $timeout(function() {
            file.result = response.data;
            self.tableParams.reload();
            $mdDialog.cancel();
            $scope.addImportJsonValidationTab(response.data);
          });
        }, function(response) {
          if(response.status > 0)
            $scope.errorMsg = response.status + ': ' + response.data;
        }, function(evt) {
          file.progress = Math.min(100, parseInt(100.0 *
            evt.loaded / evt.total));
        });
      }
    };

    $scope.cancel = function() {
      $mdDialog.cancel();
    };

    $scope.importJson = function() {
      $mdDialog.show({
        scope: $scope.$new(),
        templateUrl: 'scripts/modules/partials/importJson.html',
        parent: angular.element(document.body),
        clickOutsideToClose: true
      })
        .then(function(answer) {
          $scope.status = 'You said the information was "' + answer + '".';
        }, function() {
          $scope.status = 'You cancelled the dialog.';
        });
    };

    self.tableParams = new NgTableParams(
      {
        group: "type"
      },
      {
        getData: function(params) {
          if(params.filter().name || params.filter().description || params.filter().isDuplicate) {
            return $filter('filter')(self.tableParams.settings().dataset, params.filter());
          }
          if(!self.tableParams.shouldGetData) {
            return self.tableParams.settings().dataset;
          }
          $log.info("Data getting from modules ajax ...");
          return ModulesService.get().then(function(data) {
            $log.info("Data received from modules ajax ...");
            if(!data) {
              self.originalData = angular.copy(data);
              self.tableParams.settings().dataset = data;
              self.tableParams.shouldGetData = true;
            } else {
              if($sessionStorage.langEnabled) {
                NodeLanguageService.getNodeNodeLanguageList().then(function(response) {
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
            return data;
          });
        }
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
        name: "New Module",
        placeholder: "New Module",
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
      cleanseValidation();
      var data = ModulesService.deleteModule(row).then(function(response) {
        if(response.status === 200) {
          console.log('Module was deleted!');
          self.tableParams.shouldGetData = true;
          self.tableParams.reload().then(function(data) {
            if(data.length === 0 && self.tableParams.total() > 0) {
              self.tableParams.page(self.tableParams.page() - 1);
              self.tableParams.reload();
              if(isValidationFilterEnabled == true) {
                $scope.validateBtn(true);
              }
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
          if(isValidationFilterEnabled == true) {
            $scope.validateBtn(true);
          }
        }
      });
    }

    function resetRow(row, rowForm) {
      row.isEditing = false;
      rowForm.$setPristine();
      self.tableTracker.untrack(row);
      return window._.find(self.originalData, {idNode: row.idNode});
    }

    $scope.cancelChanges = function() {
      $scope.cancel();
      cancel($scope.row, $scope.rowForm);
    };

    $scope.updateInterviewModuleNames = function() {
      var row = $scope.row;
      $scope.cancel();
      $scope.interviewIdCount = $scope.interviewDataForUpdate.length;
      $scope.counter = 0;
      $scope.interviewCount = $scope.counter;
      $mdDialog.show({
        scope: $scope,
        preserveScope: true,
        templateUrl: 'scripts/modules/partials/updateInterviewModuleDialog.html',
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
        console.log('finish updating modules');
        $timeout(function() {
          $scope.cancel();
          ModulesService.save(row).then(function(response) {
            if(response.status === 200) {
              console.log('Module Save was Successful!');
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
        }, 1000);
      }, function(err) {
        console.log('error');
      });
    };

    function save(row, rowForm) {
      self.isEditing = false;
      //display dialog box to inform user that we are verifying the module name
      // change if it would affect the interviews
      $scope.interviewExist = false;
      $scope.validationInProgress = true;
      $scope.row = row;
      $scope.rowForm = rowForm;
      cleanseValidation();
      if(row.idNode) {
        $mdDialog.show({
          scope: $scope,
          preserveScope: true,
          templateUrl: 'scripts/modules/partials/validateModuleDialog.html',
          clickOutsideToClose: false
        });
        ModulesService.findInterviewByModuleId(row.idNode).then(function(response) {
          if(response.status == 200) {
            $scope.validationInProgress = false;
            if(response.data.length > 0) {
              $scope.interviewExist = true;
              $scope.interviewDataForUpdate = response.data;
            } else {
              $mdDialog.cancel();
              ModulesService.save(row).then(function(response) {
                if(response.status === 200) {
                  console.log('Module Save was Successful!');
                  self.tableParams.shouldGetData = true;
                  self.tableParams.reload().then(function(data) {
                    if(data.length === 0 && self.tableParams.total() > 0) {
                      self.tableParams.page(self.tableParams.page() - 1);
                      self.tableParams.reload();
                      if(isValidationFilterEnabled == true) {
                        $scope.validateBtn(true);
                      }
                      $location.hash("");
                      $anchorScroll();
                    }
                  });
                }
              });
            }
          }
        });
      } else {
        ModulesService.save(row).then(function(response) {
          if(response.status === 200) {
            console.log('Module Save was Successful!');
            self.tableParams.shouldGetData = true;
            self.tableParams.reload().then(function(data) {
              if(data.length === 0 && self.tableParams.total() > 0) {
                self.tableParams.page(self.tableParams.page() - 1);
                self.tableParams.reload();
                if(isValidationFilterEnabled == true) {
                  $scope.validateBtn(true);
                }
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

    $scope.issuePopOver = {
      templateUrl: 'scripts/modules/partials/issuePopOver.html'
    };
  }
})();

