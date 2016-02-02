(function(){
	angular.module('occIDEASApp.Fragments')
		   .controller('FragmentCtrl',FragmentCtrl);
	
	FragmentCtrl.$inject = ['FragmentsService','NgTableParams','$state','$scope','$filter'];
	function FragmentCtrl(FragmentsService,NgTableParams,$state,$scope,$filter){
		var self = this;
		self.isDeleting = false;
		var dirtyCellsByRow = [];
	    var invalidCellsByRow = [];
		self.tableParams = new NgTableParams({group: "type"}, {	
	        getData: function($defer,params) {
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
	          name: "",
	          idNode:Math.max.apply(null, _.pluck(self.tableParams.settings().dataset, "idNode"))+1,
	          type: type,
	          description: null
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
	    
	    function cancel(row, rowForm) {
	        var originalRow = resetRow(row, rowForm);
	        angular.extend(row, originalRow);
	    }
	    function del(row) {
	    	//  Modules.deleteModule().then(function(data) {});////Delete module here via ajax//
	        _.remove(self.tableParams.settings().dataset, function (item) {
	            return row === item;
	        });
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
	        return window._.findWhere(self.originalData,{idNode:row.idNode});
	    }
	    function save(row, rowForm) {
	        var originalRow = resetRow(row, rowForm);
	        angular.extend(originalRow, row);
	        self.isAdding = false;
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

