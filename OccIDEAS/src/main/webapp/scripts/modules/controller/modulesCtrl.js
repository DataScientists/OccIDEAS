(function(){
	angular.module('occIDEASApp.Modules')
		   .controller('ModuleCtrl',ModuleCtrl);
	ModuleCtrl.$inject = ['ModulesService','ngTableParams','$state','$scope','ModulesCache','$filter',
                          '$anchorScroll','$location','$log'];
	function ModuleCtrl(ModulesService,NgTableParams,$state,$scope,ModulesCache,$filter,
			$anchorScroll,$location,$log){
		var self = this;
		self.isDeleting = false;
		var dirtyCellsByRow = [];
	    var invalidCellsByRow = [];
	    $scope.$root.tabsLoading = false;
		self.tableParams = new NgTableParams(
				{
					group: "type"
				}, 
				{	
	        getData: function(params) {
	          if(params.filter().name || params.filter().description){	
	        	return $filter('filter')(self.tableParams.settings().dataset, params.filter());
	          }
	          if(!self.tableParams.shouldGetData){
	        	  return self.tableParams.settings().dataset;
	          }
	          $log.info("Data getting from modules ajax ..."); 
	          return  ModulesService.get().then(function(data) {
	        	  $log.info("Data received from modules ajax ...");        	 
	        	  self.originalData = angular.copy(data);
	        	  self.tableParams.settings().dataset = data;
	        	  self.tableParams.shouldGetData = true;
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
	    	var data =  ModulesService.deleteModule(row).then(function(response) {
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
	        ModulesService.save(row).then(function(response){
				if(response.status === 200){
					console.log('Module Save was Successful!');
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

