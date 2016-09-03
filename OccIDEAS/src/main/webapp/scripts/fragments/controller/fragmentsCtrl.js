(function(){
	angular.module('occIDEASApp.Fragments')
		   .controller('FragmentCtrl',FragmentCtrl);
	
	FragmentCtrl.$inject = ['FragmentsService','NgTableParams','$state','$scope','$filter','$mdDialog','$q','InterviewsService','$timeout'];
	function FragmentCtrl(FragmentsService,NgTableParams,$state,$scope,$filter,$mdDialog,$q,InterviewsService,$timeout){
		var self = this;
		self.isDeleting = false;
		var dirtyCellsByRow = [];
	    var invalidCellsByRow = [];
	    $scope.$root.tabsLoading = false;
	    
	    $scope.cancel = function() {
			$mdDialog.cancel();
		};
		
		$scope.cancelChanges = function(){
	    	$scope.cancel();
	    	cancel($scope.row,$scope.rowForm);
	    }
		
		$scope.updateInterviewModuleNames = function(){
	    	var row = $scope.row;
	    	$scope.cancel();
	    	$scope.interviewIdCount = $scope.interviewDataForUpdate.length;
	    	$scope.counter = 0;
			$scope.interviewCount = $scope.counter;
			$mdDialog.show({
				scope: $scope,  
				preserveScope: true,
				templateUrl : 'scripts/fragments/partials/updateInterviewFragmentDialog.html',
				clickOutsideToClose:false
			});
	    	$scope.interviewDataForUpdate.reduce(function(p, data) {
			    return p.then(function() {
			    	$scope.interviewIdInProgress = data.interviewId;
			    	$scope.counter++;
					$scope.interviewCount = $scope.counter;
			        return InterviewsService.updateModuleNameForInterviewId(data.interviewPrimaryKey,row.name)
	    			.then(function(response){
	    				if(response.status == 200){
	    					console.log("successful update for "+data.interviewId);
	    				}
	    			});
			    });
			}, $q.when(true)).then(function(finalResult) {
				console.log('finish updating modules');
				$timeout(function() {
					$scope.cancel();
					FragmentsService.save(row).then(function(response){
						if(response.status === 200){
							console.log('Fragment Save was Successful!');
							self.tableParams.shouldGetData = true;
					        self.tableParams.reload().then(function (data) {
					            if (data.length === 0 && self.tableParams.total() > 0) {
					                self.tableParams.page(self.tableParams.page() - 1);
					                self.tableParams.reload();
					            }
					        });
						}
					});
	            }, 1000);	
			}, function(err) {
				console.log('error');
			});
	    }
	    
		self.tableParams = new NgTableParams({group: "type"}, {	
	        getData: function(params) {
	          if(params.filter().name || params.filter().description){	
		        	return $filter('filter')(self.tableParams.settings().dataset, params.filter());
		      }
	          if(!self.tableParams.shouldGetData){
	        	  return self.tableParams.settings().dataset;
	          }
	          return  FragmentsService.get().then(function(data) {
	        	  console.log("Data get list from fragments ajax ...");        	 
	        	  self.originalData = angular.copy(data);
	        	  self.tableParams.settings().dataset = data;
	        	  self.tableParams.shouldGetData = true;
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
	    function toggleIsDeleting(){
	    	if(self.isDeleting){
	    		self.isDeleting = false;
	    	}else{
	    		self.isDeleting = true;
	    	}
	    }
	    function add(type) {
	        self.isEditing = true;
	        self.isAdding = true;
	        
	        self.tableParams.settings().dataset.unshift({
	        	name: "New Fragment",
	        	placeholder: "New Fragment",
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
	    
	    function treeView(row){
	    	$state.go("questionView",{row:row});
	    }
	    
	    function cancel(row,rowForm) {
	    	var originalRow = resetRow(row, rowForm);
	    	if(row.idNode){
	    		angular.extend(row, originalRow);
	    	}else{
	    		_.remove(self.tableParams.settings().dataset, function (item) {
		            return row === item;
		        });
	    		self.tableParams.shouldGetData = false;
		        self.tableParams.reload().then(function (data) {
		            if (data.length === 0 && self.tableParams.total() > 0) {
		                self.tableParams.page(self.tableParams.page() - 1);
		                self.tableParams.reload();
		            }
		        });
	    	} 
	    }
	    function del(row) {
	    	row.deleted = 1;
	    	var data =  FragmentsService.deleteFragment(row).then(function(response) {
	    		if(response.status === 200){
					console.log('Module was deleted!');
					self.tableParams.shouldGetData = true;
			        self.tableParams.reload().then(function (data) {
			            if (data.length === 0 && self.tableParams.total() > 0) {
			                self.tableParams.page(self.tableParams.page() - 1);
			                self.tableParams.reload();
			            }
			        });
				}
	        });
	    	_.remove(self.tableParams.settings().dataset, function (item) {
	            return row === item;
	        });
	        self.tableParams.shouldGetData = false;
	        self.tableParams.reload().then(function (data) {
	            if (data.length === 0 && self.tableParams.total() > 0) {
	                self.tableParams.page(self.tableParams.page() - 1);
	                self.tableParams.reload();
	            }
	        });
	    }
	    function resetRow(row, rowForm) {
	        row.isEditing = false;
	        rowForm.$setPristine();
	        self.tableTracker.untrack(row);
	        return window._.find(self.originalData,{idNode:row.idNode});
	    }
	    function save(row, rowForm) {
	    	self.isEditing = false;
	    	$scope.row = row;
	    	$scope.rowForm = rowForm;
	    	if(row.idNode){
	    	$scope.interviewExist = false;
	    	$scope.validationInProgress = true;
			$mdDialog.show({
				scope: $scope,  
				preserveScope: true,
				templateUrl : 'scripts/fragments/partials/validateFragmentDialog.html',
				clickOutsideToClose:false
			});
			FragmentsService.findInterviewByFragmentId(row.idNode).then(function(response){
				if(response.status == 200){
					$scope.validationInProgress = false;
					if(response.data.length > 0){
						$scope.interviewExist = true;
						$scope.interviewDataForUpdate = response.data;
					}else{
					$mdDialog.cancel();
					FragmentsService.save(row).then(function(response){
						if(response.status === 200){
							console.log('Fragment Save was Successful!');
							self.tableParams.shouldGetData = true;
					        self.tableParams.reload().then(function (data) {
					            if (data.length === 0 && self.tableParams.total() > 0) {
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
			}else{
				FragmentsService.save(row).then(function(response){
					if(response.status === 200){
						console.log('Fragment Save was Successful!');
						self.tableParams.shouldGetData = true;
				        self.tableParams.reload().then(function (data) {
				            if (data.length === 0 && self.tableParams.total() > 0) {
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
	}
})();

