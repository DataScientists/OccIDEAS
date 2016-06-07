(function(){
	angular.module('occIDEASApp.Agents')
		   .controller('AgentCtrl',AgentCtrl);
	
	AgentCtrl.$inject = ['AgentsService','NgTableParams','$state','$scope','$filter','$rootScope'];
	function AgentCtrl(AgentsService,NgTableParams,$state,$scope,$filter,$rootScope){
		var self = this;
		self.isDeleting = false;
		var dirtyCellsByRow = [];
	    var invalidCellsByRow = [];
	    $scope.$root.tabsLoading = false;
		self.tableParams = new NgTableParams({group: "agentGroup.name",count: 100}, {	
	        getData: function($defer,params) {
	        	if(params.filter().name || params.filter().description){	
		        	return $filter('filter')(self.tableParams.settings().dataset, params.filter());
		          }
		          if(!self.tableParams.shouldGetData){
		        	  return self.tableParams.settings().dataset;
		          }
	          return  AgentsService.get().then(function(data) {
	        	  console.log("Data get list from agents ajax ...");        	 
	        	  self.originalData = angular.copy(data);
	        	  self.tableParams.settings().dataset = data;
	            return data;
	          });
	        }
	      });
		self.tableParams.shouldGetData = true;
	    self.cancel = cancel;
	    self.del = del;
	    self.save = save;
	    self.add = add;
	    self.checkAndAddAgentRulesTab = checkAndAddAgentRulesTab;
	    self.toggleIsDeleting = toggleIsDeleting;
	    
	    function toggleIsDeleting(){
	    	if(self.isDeleting){
	    		self.isDeleting = false;
	    	}else{
	    		self.isDeleting = true;
	    	}
	    }
	    function add(group) {
	    	self.isEditing = true;
	        self.isAdding = true;
	        
	        self.tableParams.settings().dataset.unshift({
	        	name: "New Agent",
	        	agentGroup: group.data[0].agentGroup,
		        description: "New Agent Description",
		        isEditing: true
	        });
	        self.originalData = angular.copy(self.tableParams.settings().dataset);
	        self.tableParams.sorting({});
	        self.tableParams.page(1);
	        self.tableParams.shouldGetData = false;
	        self.tableParams.reload();
	        self.isAdding = false;
	     }
	    function cancel(row, rowForm) {
	        var originalRow = resetRow(row, rowForm);
	        angular.extend(row, originalRow);
	    }
	    function del(row) {
            AgentsService.hasRules(row.idAgent).then(function(response){
                if("false" == response.data){
                    AgentsService.deleteAgent({idAgent:row.idAgent}).then(function(resp) { // Delete agent here via ajax
                        console.log("Deleted agent:", resp);
                        if(200 == resp.status){
                            //_.remove(self.tableParams.settings().dataset, function (item) {
                            //    return row === item;
                            //});
                            //self.tableParams.reload().then(function (data) {
                            //    if (data.length === 0 && self.tableParams.total() > 0) {
                            //        self.tableParams.page(self.tableParams.page() - 1);
                            //        self.tableParams.reload();
                            //    }
                            //});

                            self.tableParams.shouldGetData = true;
                            self.tableParams.reload().then(function (data) {
                                if (data.length === 0 && self.tableParams.total() > 0) {
                                    self.tableParams.page(self.tableParams.page() - 1);
                                    self.tableParams.reload();
                                }
                            });
                        } else {
                            console.log("Error when deleting agent:",resp);
                        }
                    });
                }else{
                    alert("This agent already had rules belong to");
                }
            });
	    }
        function checkAndAddAgentRulesTab(row){
            AgentsService.hasRules(row.idAgent).then(function(response){
                if("false" == response.data){
                    alert("This agent has no rules!");
                }else{
                    $rootScope.$emit("addAgentRulesTab", row);
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
	    	row.isEditing = false;
	        AgentsService.save(row).then(function(response){
				if(response.status === 200){
					console.log('Agent Save was Successful!');

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

