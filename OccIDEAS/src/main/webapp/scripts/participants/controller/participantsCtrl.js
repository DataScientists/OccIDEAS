(function(){
	angular.module('occIDEASApp.Participants')
		   .controller('ParticipantsCtrl',ParticipantsCtrl);
	
	ParticipantsCtrl.$inject = ['ParticipantsService','NgTableParams','$state','$scope','$filter','data','InterviewsService'];
	function ParticipantsCtrl(ParticipantsService,NgTableParams,$state,$scope,$filter,data,InterviewsService){
		var self = this;
		$scope.data = data;
		$scope.$root.tabsLoading = false;
		$scope.setSelectedInterview = function(interview){
			$scope.selectedInterview = interview;
		}
		
		self.isDeleting = false;
		var dirtyCellsByRow = [];
	    var invalidCellsByRow = [];
		self.tableParams = new NgTableParams({}, {	
	        getData: function(params) {
	        	
	        	
	          if(params.filter().reference ){
	        	  $scope.searchAWESID = params.filter().reference;
	        	  
		        	return $filter('filter')(self.tableParams.settings().dataset, params.filter());
		      }
	          if(!self.tableParams.shouldGetData){
	        	  return self.tableParams.settings().dataset;
	          }
	          return  ParticipantsService.getParticipants().then(function(data) {
	        	  console.log("Data get list from fragments ajax ...");        	 
	        	  self.originalData = angular.copy(data);
	        	  self.tableParams.settings().dataset = data;
	        	  self.tableParams.shouldGetData = true;
	            return data;
	          });
	        },
	      });
		self.tableParams.shouldGetData = true;
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
	    function add() {
	    	if($scope.searchAWESID){
	    		InterviewsService.checkReferenceNumberExists($scope.searchAWESID).then(function(data){
	    			if(data.status == 200){
	    				$scope.addInterviewTabInterviewers(data.data[0].module.idNode,$scope.searchAWESID);
	    			}
	    			else if(data.status == 204){
	    				$scope.addInterviewTabInterviewers(-1,$scope.searchAWESID);
	    			}
	    			else{
	    				alert("Error occured during awesId.");
	    			}
	    		})
	    	}else{
	    		alert("You need to add the awesId to the filter box before you can start.");
	    	}
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
	    	var data =  ParticipantsService.deleteParticipant(row).then(function(response) {
	    		if(response.status === 200){
					console.log('Participant was deleted!');
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
	    	ParticipantsService.save(row).then(function(response){
				if(response.status === 200){
					console.log('Participant Save was Successful!');
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
