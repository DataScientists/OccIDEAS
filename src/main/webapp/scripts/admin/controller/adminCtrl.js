(function() {
  angular
    .module('occIDEASApp.Admin')
    .controller('AdminCtrl', AdminCtrl);

  AdminCtrl.$inject = ['$log', 'NgTableParams', '$scope', '$filter', 'AdminService',
    '$mdDialog', 'SystemPropertyService', 'ngToast', 'InterviewsService', '$sessionStorage'];

  function AdminCtrl($log, NgTableParams, $scope, $filter, AdminService,
                     $mdDialog, SystemPropertyService, $ngToast, InterviewsService, $sessionStorage) {
    var self = this;
    self.isDeleting = false;
    var dirtyCellsByRow = [];
    var invalidCellsByRow = [];
    self.cancel = cancel;
    self.del = del;
    self.save = save;
    self.add = add;
    self.toggleIsDeleting = toggleIsDeleting;
    self.addNewConfigBtn = addNewConfigBtn;

    $scope.randomIntCount = 0;
    $scope.randomAnswerChecked = true;
    $scope.dbConnect = {
      host: 'jdbc:mysql://o3library.occideas.org:3306/occideas',
      username: '',
      password: ''
    };

    self.importLibraryDialog = function() {
      $scope.importLibraryDisabled = false;
      $scope.importLibraryProgress = false;
      $mdDialog.show({
        scope: $scope,
        preserveScope: true,
        templateUrl: 'scripts/admin/partials/importLibrary.html',
        clickOutsideToClose: false
      });
    };

    self.importLibrary = function() {
      if(!$scope.dbConnect.host || !$scope.dbConnect.username || !$scope.dbConnect.password) {
        $ngToast.create({
          className: 'danger',
          content: 'Field in * are required.',
          dismissButton: true,
          dismissOnClick: false,
          animation: 'slide'
        });
        return;
      }
      $scope.importLibraryDisabled = true;
      $scope.importLibraryProgress = true;
      AdminService.importLibrary($scope.dbConnect).then(function(response) {
        if(response.status == '200') {
          $ngToast.create({
            className: 'success',
            content: 'Import library successfull.',
            animation: 'slide'
          });
        } else {
          $ngToast.create({
            className: 'danger',
            content: 'Import library failed, check the logs.',
            dismissButton: true,
            dismissOnClick: false,
            animation: 'slide'
          });
        }
        $mdDialog.cancel();
        $scope.importLibraryDisabled = false;
        $scope.importLibraryProgress = false;
      });
    };

    self.deleteQSFSurveys = function(){
      AdminService.deleteQSFSurveys().then(function(response) {
        if (response.status == '200') {
          $ngToast.create({
            className: 'success',
            content: 'Delete QSF Surveys successful.',
            animation: 'slide'
          });
        } else {
          $ngToast.create({
            className: 'danger',
            content: 'Delete QSF Surveys failed, check the logs.',
            dismissButton: true,
            dismissOnClick: false,
            animation: 'slide'
          });
        }
      });
    };

    self.openCopySurveys = function(){
      self.copySurveyReq = {userId:'',prefix:''};
      $mdDialog.show({
        scope: $scope,
        preserveScope: true,
        templateUrl: 'scripts/admin/partials/copySurveys.html',
        clickOutsideToClose: false
      });
    }

    $scope.copySurveys = function(copySurveyReq){
      if(copySurveyReq.userId == '' || copySurveyReq.prefix ==''){
        $ngToast.create({
          className: 'danger',
          content: 'All fields are required.',
          dismissButton: true,
          dismissOnClick: false,
          animation: 'slide'
        });
        return;
      }
      AdminService.copySurveys(copySurveyReq.userId, copySurveyReq.prefix).then(function(response) {
        if (response.status == '200') {
          $ngToast.create({
            className: 'success',
            content: 'Copy surveys was successful, duration is based on number of surveys to copy.',
            animation: 'slide'
          });
          $mdDialog.cancel();
        } else {
          $ngToast.create({
            className: 'danger',
            content: 'Copy surveys failed, check the logs.',
            dismissButton: true,
            dismissOnClick: false,
            animation: 'slide'
          });
          $mdDialog.cancel();
        }
      });
    };

    self.importQSFResponses = function(){
      AdminService.importQSFResponses().then(function(response){
        if (response.status == '200') {
          $ngToast.create({
            className: 'success',
            content: 'Import QSF Response Successful , check result in a few minutes.',
            animation: 'slide'
          });
        } else {
          $ngToast.create({
            className: 'danger',
            content: 'Import QSF Responses failed, check the logs.',
            dismissButton: true,
            dismissOnClick: false,
            animation: 'slide'
          });
        }
      });
    };

    self.purgeParticipants = function() {
      AdminService.purgeParticipants().then(function(response) {
        if(response.status == '200') {
          $ngToast.create({
            className: 'success',
            content: 'Purge participants successfull.',
            animation: 'slide'
          });
        } else {
          $ngToast.create({
            className: 'danger',
            content: 'Purge participants failed, check the logs.',
            dismissButton: true,
            dismissOnClick: false,
            animation: 'slide'
          });
        }
      });
    };

    self.purgeModule = function() {
      AdminService.purgeModule().then(function(response) {
        if(response.status == '200') {
          $ngToast.create({
            className: 'success',
            content: 'Purge module successfull.',
            animation: 'slide'
          });
        } else {
          $ngToast.create({
            className: 'danger',
            content: 'Purge module failed, check the logs.',
            dismissButton: true,
            dismissOnClick: false,
            animation: 'slide'
          });
        }
      });
    };

    self.cleanOrphans = function() {
      AdminService.cleanOrphans().then(function(response) {
        if(response.status == '200') {
          $ngToast.create({
            className: 'success',
            content: 'Cleanup Orphans successfull.',
            animation: 'slide'
          });
        } else {
          $ngToast.create({
            className: 'danger',
            content: 'Cleanup Orphans failed, check the logs.',
            dismissButton: true,
            dismissOnClick: false,
            animation: 'slide'
          });
        }
      });
    };

    self.preloadActiveIntro = function() {
      AdminService.purgeModule().then(function (response) {
        if (response.status == '200') {
          console.info("purge successful");
          InterviewsService.preloadActiveIntro().then(function (response) {
            if (response.status === 200) {
              $ngToast.create({
                className: 'success',
                content: 'Preload study agents successful',
                animation: 'slide',
                dismissOnTimeout: false,
                dismissButton: true
              });
            } else if (response.status !== 417) {
              $ngToast.create({
                className: 'danger',
                content: 'Preload study agents failed, check the logs.',
                dismissButton: true,
                dismissOnClick: false,
                animation: 'slide'
              });
            }
          });
        } else {
          $ngToast.create({
            className: 'danger',
            content: 'Purge module failed, check the logs.',
            dismissButton: true,
            dismissOnClick: false,
            animation: 'slide'
          });
        }
      });
    };

//	    self.preloadAllModules = function(){
//	    	InterviewsService.preloadAllModules().then(function(response){
//	    		if(response.status == '200'){
//	    			alert("Preload all modules successful");
//	    		}else{
//	    			alert("Preload all modules failed, check the logs.");
//	    		}
//	    	});
//	    }

    self.filterModTableParams = new NgTableParams(
      {},
      {
        getData: function(params) {
          if(params.filter().interviewModuleName) {
            return $filter('filter')(self.filterModTableParams.settings().dataset, params.filter());
          }
          if(!self.filterModTableParams.shouldGetData) {
            return self.filterModTableParams.settings().dataset;
          }
          return InterviewsService.getLinksByModule($sessionStorage.activeIntro.value).then(function(response) {
            var data = response.data;
            if(data) {
              data = _.uniqBy(data, 'link');
            }
            self.originalData = angular.copy(data);
            self.filterModTableParams.settings().dataset = data;
            self.filterModTableParams.shouldGetData = true;
            return data;
          });
        }
      });
    self.filterModTableParams.shouldGetData = true;
    $scope.checkboxes = {'checked': false, items: {}};
    $scope.$watch('checkboxes.items', function(values) {
      if(!self.filterModTableParams.settings().dataset) {
        return;
      }
      var checked = 0, unchecked = 0,
        total = self.filterModTableParams.settings().dataset.length;
      angular.forEach(self.filterModTableParams.settings().dataset, function(item) {
        checked += ($scope.checkboxes.items[item.link]) || 0;
        unchecked += (!$scope.checkboxes.items[item.link]) || 0;
      });
      if((unchecked == 0) || (checked == 0)) {
        $scope.checkboxes.checked = (checked == total);
      }
      // grayed checkbox
      angular.element(document.getElementById("select_all")).prop("indeterminate", (checked != 0 && unchecked != 0));

      $scope.fileName = "";
      var moduleList = self.filterModTableParams.settings().dataset;
    }, true);

    self.createRandomInterviews = function() {
      if($scope.randomIntCount == 0) {
        $ngToast.create({
          className: 'danger',
          content: 'count must be greater than 0.',
          dismissButton: true,
          dismissOnClick: false,
          animation: 'slide'
        });
        return;
      }
      $scope.randomReport = undefined;
      var filterModule = [];
      _.each($scope.checkboxes.items, function(value, key) {
        if(value) {
          filterModule.push(key);
        }
      });
      var requestParam = {
        count: $scope.randomIntCount,
        isRandomAnswers: $scope.randomAnswerChecked,
        filterModule: filterModule
      };
      InterviewsService.createRandomInterviews(requestParam).then(function(response) {
        if(response.status == '200') {
          $scope.randomReport = response.data;
          _.each($scope.randomReport, function(report) {
            var i = 0;
            for(i; i < report.listQuestion.length; i++) {
              if(i < report.listAnswer.length) {
                var a = _.find(report.listAnswer, function(ans) {
                  return ans.parentQuestionId == report.listQuestion[i].questionId;
                });
                if(a) {
                  report.listQuestion[i].answer = a;
                } else {
                  report.listQuestion[i].answer = {name: 'No Answer Available.'};
                }
              }
            }
          });
        }
      });
    };

    self.listTranslations = function() {
      $scope.addLanguageTab();
    };

    self.states = [{
      name: 'Active'
    }, {name: 'Inactive'}];

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

    function del(row) {
      row.deleted = 1;
      var data = SystemPropertyService.deleteProperty(row).then(function(response) {
        if(response.status === 200) {
          $ngToast.create({
            className: 'success',
            content: 'System Property was deleted successfully.',
            animation: 'slide'
          });
          self.tableConfig.shouldGetData = true;
          self.tableConfig.reload().then(function(data) {
            if(data.length === 0 && self.tableConfig.total() > 0) {
              self.tableConfig.page(self.tableConfig.page() - 1);
              self.tableConfig.reload();
            }
          });
        }
      });
      _.remove(self.tableConfig.settings().dataset, function(item) {
        return row === item;
      });
      self.tableConfig.shouldGetData = false;
      self.tableConfig.reload().then(function(data) {
        if(data.length === 0 && self.tableConfig.total() > 0) {
          self.tableConfig.page(self.tableConfig.page() - 1);
          self.tableConfig.reload();
        }
      });
    }

    function toggleIsDeleting() {
      if(self.isDeleting) {
        self.isDeleting = false;
      } else {
        self.isDeleting = true;
      }
    }

    function addNewConfigBtn() {
      self.isEditing = true;
      self.isAdding = true;

      self.tableConfig.settings().dataset.unshift({
        name: "New Module",
        type: "New Type",
        value: "New Value",
        isEditing: true
      });
      self.originalData = angular.copy(self.tableConfig.settings().dataset);
      self.tableConfig.sorting({});
      self.tableConfig.page(1);
      self.tableConfig.shouldGetData = false;
      self.tableConfig.reload();
      self.isAdding = false;
    }

    function add(type) {
      self.isEditing = true;
      self.isAdding = true;

      self.tableConfig.settings().dataset.unshift({
        name: "New Module",
        type: type,
        value: "New Value",
        isEditing: true
      });
      self.originalData = angular.copy(self.tableConfig.settings().dataset);
      self.tableConfig.sorting({});
      self.tableConfig.page(1);
      self.tableConfig.shouldGetData = false;
      self.tableConfig.reload();
      self.isAdding = false;
    }

    function save(row, rowForm) {
      self.isEditing = false;
      SystemPropertyService.save(row).then(function(response) {
        if(response.status === 200) {
          $ngToast.create({
            className: 'success',
            content: 'System Property Save was Successful!',
            animation: 'slide'
          });
          self.tableConfig.shouldGetData = true;
          self.tableConfig.reload().then(function(data) {
            if(data.length === 0 && self.tableConfig.total() > 0) {
              self.tableConfig.page(self.tableConfig.page() - 1);
              self.tableConfig.reload();
            }
          });
        }
      });
    }

    function resetRow(row, rowForm) {
      row.isEditing = false;
      rowForm.$setPristine();
      self.tableTracker.untrack(row);
      return window._.find(self.originalData, {id: row.id});
    }

    function cancel(row, rowForm) {
      var originalRow = resetRow(row, rowForm);
      if(row.id) {
        angular.extend(row, originalRow);
      } else {
        _.remove(self.tableConfig.settings().dataset, function(item) {
          return row === item;
        });
        self.tableConfig.shouldGetData = false;
        self.tableConfig.reload().then(function(data) {
          if(data.length === 0 && self.tableConfig.total() > 0) {
            self.tableConfig.page(self.tableConfig.page() - 1);
            self.tableConfig.reload();
          }
        });
      }
    }

    self.tableConfig = new NgTableParams(
      {
        group: "type"
      },
      {
        getData: function(params) {
          if(params.filter().name) {
            return $filter('filter')(self.tableConfig.settings().dataset, params.filter());
          }
          if(!self.tableConfig.shouldGetData) {
            return self.tableConfig.settings().dataset;
          }
          $log.info("Data getting from modules ajax ...");
          return SystemPropertyService.getAll().then(function(response) {
            if(response.status == '200') {
              $log.info("Data received from modules ajax ...");
              self.originalData = angular.copy(response.data);
              self.tableConfig.settings().dataset = response.data;
              self.tableConfig.shouldGetData = true;
              self.sysprop = response.data;
              return response.data;
            }
          });
        }
      });
    self.tableConfig.shouldGetData = true;

    AdminService.getRoles().then(function(data) {
      self.roleList = data;
    });

    self.saveSysPropBtn = function() {
      SystemPropertyService.save(self.sysprop.activemodule).then(function(response) {
        if(response.status == '200') {
          $ngToast.create({
            className: 'success',
            content: 'Config changed was saved successfully.',
            animation: 'slide'
          });
          self.sysprop.activemodule = response.data;
        }
      });
    };

    self.showAddUserDialog = function() {
      self.newUser = {roles: []};
      $mdDialog.show({
        scope: $scope,
        preserveScope: true,
        templateUrl: 'scripts/admin/partials/addNewUser.html',
        clickOutsideToClose: false
      });
    };

    self.showRandomInterviews = function() {
      $mdDialog.show({
        scope: $scope,
        preserveScope: true,
        templateUrl: 'scripts/admin/partials/createRandomInterviews.html',
        clickOutsideToClose: false
      });
    };

    $scope.cancel = function() {
      $mdDialog.cancel();
    };

    $scope.addUserBtn = function(newUser) {
      newUser.state = 'Active';
      if(newUser.email === undefined)
        newUser.email = '';
      if(newUser.firstName === undefined)
        newUser.firstName = '';
      if(newUser.lastName === undefined)
        newUser.lastName = '';

      if(newUser.roles.length == 0) {
        $ngToast.create({
          className: 'danger',
          content: 'Please select a role for the user.',
          dismissButton: true,
          dismissOnClick: false,
          animation: 'slide'
        });
        return;
      }

      AdminService.addUser(newUser).then(function(response) {
        if(response.status === 200) {
          let msg = 'User was successfully added';
          console.log(msg);
          $ngToast.create({
            className: 'success',
            content: msg,
            dismissButton: true,
            dismissOnClick: false,
            animation: 'slide'
          });

          // time to add the roles
          var profiles = [];
          _.each(newUser.roles, function(role) {
            var profile = {
              userId: response.data.id,
              userProfileId: role.id
            };
            profiles.push(profile);
          });
          AdminService.saveUserProfileList(profiles).then(function(response) {
            if(response.status === 200) {
              self.tableParams.reload();
              $mdDialog.cancel();
            }
          });

        } else {
          $mdDialog.cancel();
        }
      });
    };

    self.showChangePasswordDialog = function(existingUser) {
      $scope.existingUser = existingUser;
      $scope.existingUser.id = existingUser.id;
      $scope.existingUser.roles = existingUser.userProfiles;
      $scope.existingUser.state = existingUser.state;
      $scope.existingUser.password = existingUser.password;
      $scope.existingUser.previousPassword = existingUser.password;
      $mdDialog.show({
        scope: $scope,
        preserveScope: true,
        templateUrl: 'scripts/admin/partials/changePasswordDialog.html',
        clickOutsideToClose: false
      });
    };

    $scope.changePasswordBtn = function(existingUser) {
      if($scope.existingUser.previousPassword == existingUser.newPassword) {
        $ngToast.create({
          className: 'danger',
          content: 'No changes on the password.',
          dismissButton: true,
          dismissOnClick: false,
          animation: 'slide'
        });
        return;
      }
      $scope.existingUser.password = existingUser.newPassword;
      AdminService.updatePassword(existingUser).then(function(response) {
        if(response.status == 200) {
          console.log('User was successfully updated');
          self.tableParams.reload();
        }
        $mdDialog.cancel();
      });
    };

    self.showEditUserDialog = function(existingUser) {
      $scope.existingUser = existingUser;
      $scope.existingUser.id = existingUser.id;
      $scope.existingUser.roles = existingUser.userProfiles;
      $mdDialog.show({
        scope: $scope,
        preserveScope: true,
        templateUrl: 'scripts/admin/partials/editUser.html',
        clickOutsideToClose: false
      });
    };

    $scope.editUserBtn = function(existingUser) {
      if(!existingUser.state) {
        $ngToast.create({
          className: 'danger',
          content: 'State is a required field.',
          dismissButton: true,
          dismissOnClick: false,
          animation: 'slide'
        });
        return;
      }
      if(existingUser.roles.length == 0) {
        $ngToast.create({
          className: 'danger',
          content: 'Please select a role for the user.',
          dismissButton: true,
          dismissOnClick: false,
          animation: 'slide'
        });
        return;
      }

      AdminService.updateUser(existingUser).then(function(response) {
        if(response.status == 200) {
          var userId = response.data.id;
          let msg = 'User was successfully updated';
          console.log(msg);
          $ngToast.create({
            className: 'success',
            content: msg,
            dismissButton: true,
            dismissOnClick: false,
            animation: 'slide'
          });
          // delete all roles for user id
          AdminService.deleteUserProfile(userId)
            .then(function(response) {

              if(response.status == '200') {
                console.log("Delete user in role map was successful");
                // time to add the roles
                var profiles = [];
                _.each(existingUser.roles, function(role) {
                  var profile = {
                    userId: userId,
                    userProfileId: role.id
                  };
                  profiles.push(profile);
                });
                AdminService.saveUserProfileList(profiles).then(function(response) {
                  if(response.status == 200) {
                    self.tableParams.reload();
                    $mdDialog.cancel();
                  }
                });
              } else {
                $mdDialog.cancel();
              }
            });
        } else {
          $mdDialog.cancel();
        }
        $mdDialog.cancel();
      });
    };

    self.tableParams = new NgTableParams(
      {},
      {
        getData: function(params) {

          if(!$scope.data) {
            self.tableParams.shouldGetData = true;
          } else {
            var orderedFilteredDataset = $scope.data;
            if(params.sorting().ssoId
              || params.sorting().firstName
              || params.sorting().lastName
              || params.sorting().email
              || params.sorting().state
              || params.sorting().rolesStr) {

              self.tableParams.shouldGetData = false;
              orderedFilteredDataset = $filter('orderBy')($scope.data, params.orderBy());
            }

            if(params.filter().ssoId
              || params.filter().firstName
              || params.filter().lastName
              || params.filter().email
              || params.filter().state
              || params.filter().rolesStr) {

              self.tableParams.shouldGetData = false;
              orderedFilteredDataset = $filter('filter')(orderedFilteredDataset, params.filter());
            }

            self.tableParams.settings().dataset = orderedFilteredDataset;
          }

          if(!self.tableParams.shouldGetData) {
            return self.tableParams.settings().dataset;
          }
          $log.info("Data getting from admin ajax ...");
          return AdminService.getUserRoles().then(function(data) {
            data = _.uniqBy(data, 'ssoId');
            _.each(data, function(user) {
              user.rolesStr = _.map(user.userProfiles, 'type').join(', ');
            });
            $scope.data = data;
            self.tableParams.settings().dataset = data;
            self.tableParams.shouldGetData = true;
            return data;
          });
        }
      });
    self.tableParams.shouldGetData = true;

  }

})();