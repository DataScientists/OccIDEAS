(function(){
	angular.module('occIDEASApp.Rules')
		   .controller('RulesCtrl',RulesCtrl);
	RulesCtrl.$inject = ['RulesService','ngTableParams','$state','$scope','RulesCache','$filter',
                          '$anchorScroll','$location','templateData'];
	function RulesCtrl(RulesService,NgTableParams,$state,$scope,RulesCache,$filter,
			$anchorScroll,$location,templateData){
		var self = this;
		self.isDeleting = false;
		var dirtyCellsByRow = [];
	    var invalidCellsByRow = [];
		self.tableParams = new NgTableParams(
				{
					group: "agentName",
					group: "moduleName",
					count: 50,
				}, 
				{	
					getData: function($defer,params) {					
				          if(params.filter().name || params.filter().description){	
				        	return $filter('filter')(self.tableParams.settings().dataset, params.filter());
				          }
				          if(!self.tableParams.shouldGetData){
				        	  return self.tableParams.settings().dataset;
				          }
				          console.log("ModuleId:"+templateData.moduleId); //Ask Jed about a better way to do this
				          return  RulesService.listByModule(templateData.moduleId).then(function(data) {
				        	  console.log("Data getting from moduleruless ajax ... id:"+templateData.moduleId);        	 
				        	  self.originalData = angular.copy(data);
				        	  self.tableParams.settings().dataset = data;
				        	  self.tableParams.shouldGetData = true;
				        	  $defer.resolve();
				            return data;
				          });
				          },
				});
		
		self.tableParams.shouldGetData = true;
		
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

