(function(){
	angular.module('occIDEASApp.Reports')
	.controller('ReportsCtrl',ReportsCtrl);
	
	ReportsCtrl.$inject = ['ReportsService','NgTableParams','$state','data','$scope',
	                       '$filter','$resource','$mdDialog','InterviewsService',
	                       'SystemPropertyService','ngToast'];
	function ReportsCtrl(ReportsService,NgTableParams,$state,data,$scope,
			$filter,$resource,$mdDialog,InterviewsService,
			SystemPropertyService,ngToast){
		var self = this;
		$scope.data = data;
		$scope.$root.tabsLoading = false;
		
		$scope.del = function(reportHistoryVO){
			ReportsService.deleteReport(reportHistoryVO).then(function(response) {
	    		if(response.status === 200){
					console.log('Report was deleted!');
					self.tableParams.shouldGetData = true;
			        self.tableParams.reload().then(function (data) {
			            if (data.length === 0 && self.tableParams.total() > 0) {
			                self.tableParams.page(self.tableParams.page() - 1);
			                self.tableParams.reload();
			            }
			        });
				}
	        });
		}
		
		$scope.downloadReport = function(reportHistoryVO){
			ReportsService.downloadReport(reportHistoryVO).then(function(response){
				var data = response.data;
				if(response.status == '200'){
					 var anchor = angular.element('<a/>');
				     anchor.attr({
				         href: 'data:attachment/csv;charset=utf-8,' + encodeURI(data),
				         target: '_blank',
				         download: reportHistoryVO.name
				     })[0].click();
				}
			});
		};
		
		$scope.getArray = function(jsonData){
			return JSON.parse(jsonData);
		}
		
	    self.tableParams = new NgTableParams(
			{	
				page: 1,            
                count: 10
            }, {	
            
			getData: function(params) {
	          if ((params.sorting().reference)||(params.sorting().idParticipant)||(params.sorting().statusDescription)){
				return $filter('orderBy')(self.tableParams.settings().dataset, params.orderBy());
		      }
	        	
	          if(params.filter().reference || params.filter().idParticipant){
	        	  if(awesIdIsValid(params.filter().reference)){
	        		  return $filter('filter')(self.tableParams.settings().dataset, params.filter()); 
	        	  }     	  
		      }
	          if(!self.tableParams.shouldGetData){
	        	  var last = params.page() * params.count();
		          return _.slice(self.tableParams.settings().dataset,last - params.count(),last);
	          }
	          return  ReportsService.getAll().then(function(response) {
	        	  if(response.status == '200'){
	        		  var data = response.data;
	        		  console.log("Data get list from get reports ajax ...");        	 
	        		  self.originalData = angular.copy(data);
		        	  self.tableParams.settings().dataset = data;
		        	  self.tableParams.shouldGetData = false;
		        	  self.tableParams.total(self.originalData.length);
		        	  var last = params.page() * params.count();
			          return _.slice(data,last - params.count(),last);
	        	  }
	          });
	        },
	      });
	    self.tableParams.shouldGetData = true;
	    
	    $scope.newExportCSVButton = function(){
			$scope.checkboxes = { 'checked': false, items: {} };
			$scope.fileName = "interviewsExport";
			$mdDialog.show({
				scope: $scope.$new(),  
				preserveScope: true,
				templateUrl : 'scripts/assessments/partials/filterModuleDialog.html',
				clickOutsideToClose:false
			});
		}
	    $scope.newExportAssessmentCSVButton = function(){
			$scope.checkboxes = { 'checked': false, items: {} };
			$scope.fileName = "assessmentExport";
			$scope.exportType = "ASSESSMENT";
			$mdDialog.show({
				scope: $scope.$new(),  
				preserveScope: true,
				templateUrl : 'scripts/assessments/partials/filterModuleDialog.html',
				clickOutsideToClose:false
			});
		}
	    $scope.newExportAssessmentNoiseCSVButton = function(){
			$scope.checkboxes = { 'checked': false, items: {} };
			$scope.fileName = "assessmentNoiseExport";
			$scope.exportType = "ASSESSMENTNOISE";
			$mdDialog.show({
				scope: $scope.$new(),  
				preserveScope: true,
				templateUrl : 'scripts/assessments/partials/filterModuleDialog.html',
				clickOutsideToClose:false
			});
		}
	    $scope.newExportAssessmentVibrationCSVButton = function(){
			$scope.checkboxes = { 'checked': false, items: {} };
			$scope.fileName = "assessmentVibrationExport";
			$scope.exportType = "ASSESSMENTVIBRATION";
			$mdDialog.show({
				scope: $scope.$new(),  
				preserveScope: true,
				templateUrl : 'scripts/assessments/partials/filterModuleDialog.html',
				clickOutsideToClose:false
			});
		}
	    $scope.showInterviewCount = function(mod,$event){
			InterviewsService.findInterviewIdByModuleId(mod.idModule)
				.then(function(response){
				if(response.status == '200'){
					mod.intCount = response.data.length;
					safeDigest(mod);
				}
			});
			if ($event.stopPropagation) $event.stopPropagation();
			if ($event.preventDefault) $event.preventDefault();
			$event.cancelBubble = true;
			$event.returnValue = false;
		};
		$scope.exportCSVInterviews = function(fileName){
			SystemPropertyService.getByName("REPORT_EXPORT_CSV_DIR").then(function(response){
				if(response.status == '200'){
					if(response.data){
						$scope.cancel();
						 ngToast.create({
				    		  className: 'success',
				    		  content: 'Your report is now running .... Kindly check the reports tab for details.'
				    	 });
						 var filterModule = [];
						 _.each($scope.checkboxes.items,function(value, key){
							 filterModule.push(key);
						 });
						 if($scope.exportType=="ASSESSMENT"){
							 InterviewsService.exportAssessmentsCSV(filterModule,fileName).then(function(response){});
						 } else if($scope.exportType=="ASSESSMENTNOISE"){
							 InterviewsService.exportAssessmentsNoiseCSV(filterModule,fileName).then(function(response){});
						 }else if($scope.exportType=="ASSESSMENTVIBRATION"){
							 InterviewsService.exportAssessmentsVibrationCSV(filterModule,fileName).then(function(response){});
						 }else{
							 InterviewsService.exportInterviewsCSV(filterModule,fileName).then(function(response){}); 
						 }
						
					}else{
						ngToast.create({
				    		  className: 'danger',
				    		  content: 'Unable to generate report no directory path defined. SystemProperty "REPORT_EXPORT_CSV_DIR" is not defined.',
				    		  dismissButton: true,
			      	    	  dismissOnClick:false,
			      	    	  animation:'slide'
						});
					}
				}
			});
		}
		$scope.cancel = function() {
			$mdDialog.cancel();
		};
		self.filterModTableParams =  new NgTableParams(
				{
				}, 
			{	
	        getData: function(params) {
	          if(params.filter().interviewModuleName){	
		        return $filter('filter')(self.filterModTableParams.settings().dataset, params.filter());
		      }
		      if(!self.filterModTableParams.shouldGetData){
		        return self.filterModTableParams.settings().dataset;
		      }	          
	          return  InterviewsService.getDistinctModules().then(function(response) {	        	         	
	        	  var data = response.data;
	        	  self.originalData = angular.copy(data);
	        	  self.filterModTableParams.settings().dataset = data;
	        	  self.filterModTableParams.shouldGetData = true;
	        	  return data;
	          	});
	          }
	      });
		self.filterModTableParams.shouldGetData = true;
		$scope.checkboxes = { 'checked': false, items: {} };

		// watch for check all checkbox
		$scope.$watch('checkboxes.checked', function(value) {	    	
		    angular.forEach(self.filterModTableParams.settings().dataset, function(item) {
		        if (angular.isDefined(item.idModule)) {
		            $scope.checkboxes.items[item.idModule] = value;
		             
		        }
		    });
		});
		
		// watch for data checkboxes
		$scope.$watch('checkboxes.items', function(values) {
		    if (!self.filterModTableParams.settings().dataset) {
		        return;
		    }
		    var checked = 0, unchecked = 0,
		        total = self.filterModTableParams.settings().dataset.length;
		    angular.forEach(self.filterModTableParams.settings().dataset, function(item) {
		        checked   +=  ($scope.checkboxes.items[item.idModule]) || 0;
		        unchecked += (!$scope.checkboxes.items[item.idModule]) || 0;
		    });
		    if ((unchecked == 0) || (checked == 0)) {
		        $scope.checkboxes.checked = (checked == total);
		    }
		    // grayed checkbox
		    angular.element(document.getElementById("select_all")).prop("indeterminate", (checked != 0 && unchecked != 0));
		    
		    $scope.fileName = "";
		    var moduleList = self.filterModTableParams.settings().dataset;
		    _.each(values, function(v,k) { 
			    	if(v){
			    		_.each(moduleList, function(o) { 
					    	if(k==o.idModule){
					    		$scope.fileName += o.interviewModuleName.substr(0, 4);
					    	} 
				    	});
			    	} 
		    	});
		    
		  }, true);
		
		var safeDigest = function (obj){
			if (!obj.$$phase) {
		        try {
		        	obj.$digest();
		        }
		        catch (e) { }
			}
		}
	}
	
})();