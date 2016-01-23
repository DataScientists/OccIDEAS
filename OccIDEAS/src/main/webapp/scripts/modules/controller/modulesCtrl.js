(function(){
	angular.module('occIDEASApp.Modules')
		   .controller('ModuleCtrl',ModuleCtrl);
	
	ModuleCtrl.$inject = ['ModulesService','NgTableParams','$state','$scope'];
	function ModuleCtrl(ModulesService,NgTableParams,$state,$scope){
		var self = this;
		
		var tabs = [
		            { title: 'Module List', content: "Tabs will become paginated if there isn't enough room for them."},
		            { title: 'Fragment List', content: "You can swipe left and right on a mobile device to change tabs."},
		            { title: 'Agent List', content: "You can bind the selected tab via the selected attribute on the md-tabs element."},
		           ],
		          selected = null,
		          previous = null;
		      $scope.tabs = tabs;
		      $scope.selectedIndex = 0;
		      
		      $scope.addTab = function (title, view) {
		        view = view || title + " Content View";
		        tabs.push({ title: title, content: view, disabled: false});
		      };
		      $scope.removeTab = function (tab) {
		        var index = tabs.indexOf(tab);
		        tabs.splice(index, 1);
		      };
		
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
	    	//$state.go("questionView",{row:row});
	    	//var tabs = $scope.tabs;
	    	tabs.push({ title: row.name, content: $state.go("questionView",{row:row}), disabled: false});    	         
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

