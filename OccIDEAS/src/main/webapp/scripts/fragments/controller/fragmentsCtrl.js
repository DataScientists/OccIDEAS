(function(){
	angular.module('occIDEASApp.Fragments')
		   .controller('FragmentCtrl',FragmentCtrl);
	
	FragmentCtrl.$inject = ['FragmentsService','NgTableParams','$state','$scope','ModulesCache'];
	function FragmentCtrl(FragmentsService,NgTableParams,$state,$scope,FragmentsCache){
		var self = this;
//		$scope.data = data;
		self.tableParams = new NgTableParams({group: "type"}, {	
	        getData: function(params) {
	          /*if(FragmentsCache.get("all")){
	        	  console.log("Data getting from fragments cache ...");
	  			  return FragmentsCache.get("all");
	  		  }*/
	          return  FragmentsService.get().then(function(data) {
	        	  console.log("Data get list from fragments ajax ...");        	 
	        	  self.originalData = angular.copy(data);
//	        	  self.tableParams.total(data.length);
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

