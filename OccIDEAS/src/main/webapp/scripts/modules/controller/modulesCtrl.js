(function(){
	angular.module('occIDEASApp.Modules')
		   .controller('ModuleCtrl',ModuleCtrl);
	
	ModuleCtrl.$inject = ['ModulesService','NgTableParams','$state'];
	function ModuleCtrl(ModulesService,NgTableParams,$state){
		var self = this;
		self.tableParams = new NgTableParams({}, {
	        getData: function(params) {
	          return  ModulesService.get().then(function(data) {
	        	  self.originalData = angular.copy(data);
	        	  self.tableParams.total(data.length);
	        	  self.tableParams.settings().dataset = data;
	            return data;
	          });
	        },
	      });
	    self.treeView = treeView;
	    self.cancel = cancel;
	    self.del = del;
	    self.save = save;

	    function treeView(row){
	    	console.log("Inside treeView: " +row);
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
	        return window._.findWhere(self.originalData, function (r) {
	            return r.id === row.id;
	        });
	    }
	    function save(row, rowForm) {
	        var originalRow = resetRow(row, rowForm);
	        angular.extend(originalRow, row);
	    }
	}
})();

