(function(){
	angular.module('occIDEASApp.Participants')
		   .controller('ParticipantCtrl',ParticipantCtrl);
	
	ParticipantCtrl.$inject = ['ParticipantsService','NgTableParams','$state','$scope','$filter'];
	function ParticipantCtrl(ParticipantsService,NgTableParams,$state,$scope,$filter){
		var self = this;
		self.isDeleting = false;
		var dirtyCellsByRow = [];
	    var invalidCellsByRow = [];
		self.tableParams = new NgTableParams({group: "type"}, {	
	        getData: function(params) {
	          if(params.filter().name || params.filter().description){	
		        	return $filter('filter')(self.tableParams.settings().dataset, params.filter());
		      }
	          if(!self.tableParams.shouldGetData){
	        	  return self.tableParams.settings().dataset;
	          }
	          return  ParticipantsService.get().then(function(data) {
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
	    function add(type) {
	        self.isEditing = true;
	        self.isAdding = true;
	        
	        self.tableParams.settings().dataset.unshift({
	        	name: "New Participant",
	        	placeholder: "New Participant",
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

