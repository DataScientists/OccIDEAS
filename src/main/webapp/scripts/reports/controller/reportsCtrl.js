(function() {
  angular.module('occIDEASApp.Reports')
    .controller('ReportsCtrl', ReportsCtrl);

  ReportsCtrl.$inject = ['ReportsService', 'NgTableParams', '$state', 'data', '$scope',
    '$filter', '$resource', '$mdDialog', 'InterviewsService',
    'SystemPropertyService', 'ngToast', '$timeout', '$interval', 'AgentsService'];

  function ReportsCtrl(ReportsService, NgTableParams, $state, data, $scope,
                       $filter, $resource, $mdDialog, InterviewsService,
                       SystemPropertyService, ngToast, $timeout, $interval, AgentsService) {
    var self = this;
    $scope.data = data;
    $scope.$root.tabsLoading = false;
    $scope.interval = null;

    $scope.del = function(reportHistoryVO) {
      ReportsService.deleteReport(reportHistoryVO).then(function(response) {
        if(response.status === 200) {
          console.log('Report was deleted!');
          self.tableParams.shouldGetData = true;
          self.tableParams.reload().then(function(data) {
            if(data.length === 0 && self.tableParams.total() > 0) {
              self.tableParams.page(self.tableParams.page() - 1);
              self.tableParams.reload();
            }
          });
        }
      });
    };

    $scope.downloadLookup = function(reportHistoryVO) {
      ReportsService.downloadLookup(reportHistoryVO).then(function(response) {
        var data = response.data;
        if(response.status == '200') {

          csvData = new Blob([data], {type: 'application/octet-stream'});
          var csvUrl = URL.createObjectURL(csvData);

          var anchor = angular.element('<a/>');
          anchor.attr({
            href: csvUrl,
            target: '_blank',
            download: reportHistoryVO.name
                .substring(0, reportHistoryVO.name.length - 4)
              + "-Lookup.zip"
          })[0].click();
        } else {
          ngToast.create({
            className: 'danger',
            content: "File not available."
          });
        }
      });
    };

    $scope.downloadReport = function(reportHistoryVO) {
      ReportsService.downloadReport(reportHistoryVO).then(function(response) {
        var data = response.data;
        if(response.status == '200') {

          csvData = new Blob([data], {type: 'application/octet-stream'});
          var csvUrl = URL.createObjectURL(csvData);

          var anchor = angular.element('<a/>');
          anchor.attr({
            href: csvUrl,
            target: '_blank',
            download: reportHistoryVO.name
                .substring(0, reportHistoryVO.name.length - 4)
              + '.zip'
          })[0].click();
        } else {
          ngToast.create({
            className: 'danger',
            content: "File not available."
          });
        }
      });
    };

    $scope.uploadQSF = function(reportHistoryVO) {
      ReportsService.uploadQSF(reportHistoryVO).then(function(response) {
        var data = response.data;
        if(response.status == '200') {
          ngToast.create({
            className: 'success',
            content: "QSF file uploaded. Check qualtrics to verify."
          });
        } else {
          ngToast.create({
            className: 'danger',
            content: "Unable to upload. Contact IT."
          });
        }
      });
    };

    $scope.getArray = function(jsonData) {
      return JSON.parse(jsonData);
    };

    self.tableParams = new NgTableParams(
      {
        page: 1,
        count: 10
      }, {

        getData: function(params) {

          if(!self.originalData) {
            self.tableParams.shouldGetData = true;
          } else {
            var orderedFilteredData = self.originalData;

            if((params.sorting().id)
              || (params.sorting().name)
              || (params.sorting().type)
              || (params.sorting().requestor)
              || (params.sorting().status)
              || (params.sorting().startDt)
              || (params.sorting().endDt)
              || (params.sorting().recordCount)) {
              self.tableParams.shouldGetData = false;
              orderedFilteredData = $filter('orderBy')(self.originalData, params.orderBy());
            }

            if(params.filter().id
              || params.filter().name
              || params.filter().type
              || params.filter().requestor
              || params.filter().status
              || params.filter().startDt
              || params.filter().endDt
              || params.filter().recordCount) {
              self.tableParams.shouldGetData = false;
              orderedFilteredData = $filter('filter')(orderedFilteredData, params.filter());
            }

            self.tableParams.settings().dataset = orderedFilteredData;
          }
          if(!self.tableParams.shouldGetData) {
            params.total(orderedFilteredData.length);
            var last = params.page() * params.count();
            return _.slice(self.tableParams.settings().dataset, last - params.count(), last);
          }
          return ReportsService.getAll().then(function(response) {
            if(response.status == '200') {
              var data = response.data;
              console.log("Data get list from get reports ajax ...");
              self.originalData = angular.copy(data);
              self.tableParams.settings().dataset = data;
              self.tableParams.shouldGetData = false;
              self.tableParams.total(self.originalData.length);
              var last = params.page() * params.count();
              return _.slice(data, last - params.count(), last);
            }
          });
        },
      });
    self.tableParams.shouldGetData = true;


  
  

    $scope.newExportCSVButton = function() {
      $scope.checkboxes = {'checked': false, items: {}};
      $scope.exportType = "INTERVIEW";
      $scope.fileName = "interviewsExport";
      
      $mdDialog.show({
        scope: $scope.$new(),
        preserveScope: true,
        templateUrl: 'scripts/assessments/partials/filterModuleDialog.html',
        clickOutsideToClose: false
      });
    };
    $scope.newExportAssessmentCSVButton = function() {
      $scope.checkboxes = {'checked': false, items: {}};
      $scope.fileName = "assessmentExport";
      $scope.exportType = "ASSESSMENT";
      
      $mdDialog.show({
        scope: $scope.$new(),
        preserveScope: true,
        templateUrl: 'scripts/assessments/partials/filterModuleDialog.html',
        clickOutsideToClose: false
      });
    };

    $scope.exportNotesCSVButton = function() {
      $scope.checkboxes = {'checked': false, items: {}};
      $scope.fileName = "notesExport";
      $scope.exportType = "NOTES";
      $mdDialog.show({
        scope: $scope.$new(),
        preserveScope: true,
        templateUrl: 'scripts/assessments/partials/filterModuleDialog.html',
        clickOutsideToClose: false
      });
    };

    $scope.exportInterviewRulesButton = function() {
      $scope.agentcheckboxes = {'checked': false, items: {}};
      $scope.fileName = "InterviewFiredRules";
      $mdDialog.show({
        scope: $scope.$new(),
        preserveScope: true,
        templateUrl: 'scripts/assessments/partials/filterAgentDialog.html',
        clickOutsideToClose: false
      });
    };

    $scope.exportInterviewRules = function(fileName) {
      var filterAgent = [];
      _.each($scope.agentcheckboxes.items, function (value, key) {
        if (value) {
          filterAgent.push(key);
        }
      });

      var data = {
        fileName: fileName,
        filterAgent: filterAgent
      };
      InterviewsService.exportInterviewRules(data);
      ngToast.create({
        className: 'success',
        content: 'Reports are being generated. Please come back later and refresh the page.',
        animation: 'slide',
        timeout: 7000
      });
      $scope.cancel();
      $scope.reloadWithDelay(500);
      $scope.refreshPage();
    };

    $scope.newExportAssessmentNoiseCSVButton = function() {
      $scope.checkboxes = {'checked': false, items: {}};
      $scope.fileName = "assessmentNoiseExport";
      $scope.exportType = "ASSESSMENTNOISE";
      $mdDialog.show({
        scope: $scope.$new(),
        preserveScope: true,
        templateUrl: 'scripts/assessments/partials/filterModuleDialog.html',
        clickOutsideToClose: false
      });
    };
    $scope.newExportAssessmentVibrationCSVButton = function() {
      $scope.checkboxes = {'checked': false, items: {}};
      $scope.fileName = "assessmentVibrationExport";
      $scope.exportType = "ASSESSMENTVIBRATION";
      $mdDialog.show({
        scope: $scope.$new(),
        preserveScope: true,
        templateUrl: 'scripts/assessments/partials/filterModuleDialog.html',
        clickOutsideToClose: false
      });
    };
    $scope.showInterviewCount = function(mod, $event) {
      InterviewsService.findInterviewIdByModuleId(mod.idModule)
        .then(function(response) {
          if(response.status == '200') {
            mod.intCount = response.data.length;
            safeDigest(mod);
          }
        });
      if($event.stopPropagation) $event.stopPropagation();
      if($event.preventDefault) $event.preventDefault();
      $event.cancelBubble = true;
      $event.returnValue = false;
    };

    $scope.data.sortColumns = true;

    $scope.exportCSVInterviews = async function (fileName) {
      var checkedItems = false;
      _.each($scope.checkboxes.items, function (value, key) {
        if (value) {
          checkedItems = true;
        }
      });

      if (!checkedItems) {
        ngToast.create({
          className: 'warning',
          content: 'Please choose at least one module',
          dismissOnTimeout: false,
          dismissButton: true
        });
      } else {
              $scope.cancel();
              ngToast.create({
                className: 'success',
                content: "Your report is now running... Kindly wait until it is completed."
              });
              var filterModule = [];
              _.each($scope.checkboxes.items, function(value, key) {
                if(value) {
                  filterModule.push(key);
                }
              });
			  
	          var sortColumns = $scope.data.sortColumns;
	
              if($scope.exportType == "ASSESSMENT") {
                await InterviewsService.exportAssessmentsCSV(filterModule, fileName);
              } else if ($scope.exportType == "ASSESSMENTNOISE") {
                await InterviewsService.exportAssessmentsNoiseCSV(filterModule, fileName);
              } else if ($scope.exportType == "ASSESSMENTVIBRATION") {
                await InterviewsService.exportAssessmentsVibrationCSV(filterModule, fileName);
              } else if ($scope.exportType == "NOTES") {
                await InterviewsService.exportNotesCSV(filterModule, fileName);
              } else {
	            
                await InterviewsService.exportInterviewsCSV(filterModule, fileName,sortColumns);
              }

        $scope.reloadWithDelay(500);
        $scope.refreshPage();
      }
    };

    $scope.reloadWithDelay = (delay) => {
      $timeout(function () {
        self.tableParams.shouldGetData = true;
        self.tableParams.reload().then(function () {
          self.tableParams.reload();
        });
      }, delay);
    }

    $scope.refreshPage = () => {
      const interval = $interval(function () {
        self.tableParams.shouldGetData = true;
        self.tableParams.reload().then(function (data) {
          self.tableParams.reload();

          let allCompleted = false;
          _.each(data, function (value) {
            if (value.status === 'Completed') {
              allCompleted = true;
            }
          });

          if (allCompleted) {
            $interval.cancel(interval);
          }
        });
      }, 1000 * 30, 10);
    }

    $scope.cancel = function () {
      $mdDialog.cancel();
    };
    self.filterModTableParams = new NgTableParams(
        {},
        {
          getData: function (params) {
            if (params.filter().interviewModuleName) {
              return $filter('filter')(self.filterModTableParams.settings().dataset, params.filter());
          }
          if(!self.filterModTableParams.shouldGetData) {
            return self.filterModTableParams.settings().dataset;
          }
          return InterviewsService.getDistinctModules().then(function(response) {
            var data = response.data;
            self.originalData = angular.copy(data);
            self.filterModTableParams.settings().dataset = data;
            self.filterModTableParams.shouldGetData = true;
            return data;
          });
        }
      });
    self.filterModTableParams.shouldGetData = true;
    $scope.checkboxes = {'checked': false, items: {}};

    // watch for check all checkbox
    $scope.$watch('checkboxes.checked', function(value) {
      angular.forEach(self.filterModTableParams.settings().dataset, function(item) {
        if(angular.isDefined(item.idModule)) {
          $scope.checkboxes.items[item.idModule] = value;

        }
      });
    });

    // watch for data checkboxes
    $scope.$watch('checkboxes.items', function(values) {
      if(!self.filterModTableParams.settings().dataset) {
        return;
      }
      var checked = 0, unchecked = 0,
        total = self.filterModTableParams.settings().dataset.length;
      angular.forEach(self.filterModTableParams.settings().dataset, function(item) {
        checked += ($scope.checkboxes.items[item.idModule]) || 0;
        unchecked += (!$scope.checkboxes.items[item.idModule]) || 0;
      });
      if((unchecked == 0) || (checked == 0)) {
        $scope.checkboxes.checked = (checked == total);
      }
      // grayed checkbox
      angular.element(document.getElementById("select_all")).prop("indeterminate", (checked != 0 && unchecked != 0));

      $scope.fileName = "";
      var moduleList = self.filterModTableParams.settings().dataset;
      _.each(values, function(v, k) {
        if(v) {
          _.each(moduleList, function(o) {
            if(k == o.idModule) {
              if($scope.fileName.length < 20) {
                $scope.fileName += o.interviewModuleName.substr(0, 4);
              }
            }
          });
        }
      });

    }, true);

    self.filterAgentTableParams = new NgTableParams(
      {},
      {
        getData: function(params) {
          if(params.filter().name) {
            return $filter('filter')(
              self.filterAgentTableParams.settings().dataset, params.filter());
          }
          if(!self.filterAgentTableParams.shouldGetData) {
            return self.filterAgentTableParams.settings().dataset;
          }
          return AgentsService.getStudyAgents().then(function(response) {
            var data = response;
            self.filterAgentTableParams.settings().dataset = data;
            self.filterAgentTableParams.shouldGetData = true;
            return data;
          });
        }
      });
    self.filterAgentTableParams.shouldGetData = true;

    $scope.agentcheckboxes = {'checked': false, items: {}};

    // watch for check all checkbox
    $scope.$watch('agentcheckboxes.checked', function(value) {
      angular.forEach(self.filterAgentTableParams.settings().dataset, function(item) {
        if(angular.isDefined(item.idAgent)) {
          $scope.agentcheckboxes.items[item.idAgent] = value;

        }
      });
    });

    // watch for data checkboxes
    $scope.$watch('agentcheckboxes.items', function(values) {
      if(!self.filterAgentTableParams.settings().dataset) {
        return;
      }
      var checked = 0, unchecked = 0,
        total = self.filterAgentTableParams.settings().dataset.length;
      angular.forEach(self.filterAgentTableParams.settings().dataset, function(item) {
        checked += ($scope.agentcheckboxes.items[item.idAgent]) || 0;
        unchecked += (!$scope.agentcheckboxes.items[item.idAgent]) || 0;
      });
      if((unchecked == 0) || (checked == 0)) {
        $scope.agentcheckboxes.checked = (checked == total);
      }
      // grayed checkbox
      angular.element(document.getElementById("select_all")).prop("indeterminate", (checked != 0 && unchecked != 0));
    });


    var safeDigest = function(obj) {
      if(!obj.$$phase) {
        try {
          obj.$digest();
        } catch(e) {
        }
      }
    }
  }

})();