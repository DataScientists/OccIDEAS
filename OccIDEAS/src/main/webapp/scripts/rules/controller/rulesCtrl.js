(function(){
	angular.module('occIDEASApp.Rules')
		   .controller('RulesCtrl',RulesCtrl);
	RulesCtrl.$inject = ['RulesService','ngTableParams','$state','$scope','$rootScope','RulesCache','$filter',
                          '$anchorScroll','$location','templateData','QuestionsService','AgentsService'];
	function RulesCtrl(RulesService,NgTableParams,$state,$scope,$rootScope,RulesCache,$filter,
			$anchorScroll,$location,templateData,QuestionsService){
		var self = this;
		self.isDeleting = false;
		var dirtyCellsByRow = [];
	    var invalidCellsByRow = [];
		self.tableParams = new NgTableParams(
				{
					group: "agentName",
					count: 200
				}, 
				{	
					getData: function($defer,params) {					
                              if(params.filter().name || params.filter().description){
                                return $filter('filter')(self.tableParams.settings().dataset, params.filter());
                              }
                              if(!self.tableParams.shouldGetData){
                                  return self.tableParams.settings().dataset;
                              }
                              console.log("templateData", templateData);
                              if(templateData.moduleId){
                                  return  RulesService.listByModule(templateData.moduleId).then(function(data) {
                                      console.log("Data getting from moduleruless ajax ... id:"+templateData.moduleId);
                                      self.originalData = angular.copy(data);
                                      self.tableParams.settings().dataset = data;
                                      self.tableParams.shouldGetData = true;
                                      $defer.resolve();
                                      return data;
                                  });
                              }else if(templateData.agentId){
                                  return RulesService.listByAgent(templateData.agentId).then(function(response) {
                                      console.log("Data getting from agentrules ajax ... id:"+templateData.agentId);
                                      self.originalData = angular.copy(response);
                                      self.tableParams.settings().dataset = response;
                                      self.tableParams.shouldGetData = true;
                                      $defer.resolve();
                                      return response;
                                  });
                              }
				          }
				});
		
		self.tableParams.shouldGetData = true;
		$rootScope.tabsLoading = false;
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
	    $scope.nodePopover = {
	    		templateUrl: 'scripts/questions/partials/nodePopover.html',
    		    open: function(x,nodeclass) {
    		    	if(!nodeclass){
    		    		nodeclass = 'P';
    		    	}
    		    	if(!x.idNode){
    		    		var convertX = {};
    		    		convertX.idNode = x;
    		    		x = convertX;
    		    	}
    		    	if(angular.isUndefined(x.info)){
  		    		  x.info = [];
  		    	  	}
    		    	 x.info["Node"+x.idNode] = {
							    				  idNode:x.idNode,
							    				  nodeclass:nodeclass,
							    				  nodePopover:{
							    					  isOpen: false
							    				  },
							    				  nodePopoverInProgress : false
		    		  							};
    		    	 var nodeInPopup = x.info["Node"+x.idNode];
    		    	 nodeInPopup.nodePopover.isOpen = true;
    		    	 nodeInPopup.nodePopoverInProgress = true;
    		          
    		    	 if(nodeclass=='P'){
    		    		 QuestionsService.findPossibleAnswer(nodeInPopup.idNode).then(function(data) {	
    		    			 nodeInPopup.data = data.data[0];
   							nodeInPopup.nodePopoverInProgress = false;
 					     });
    		    	 }else{
    		    		 QuestionsService.findQuestion(nodeInPopup.idNode).then(function(data) {	
  							nodeInPopup.data = data.data[0];		
  							nodeInPopup.nodePopoverInProgress = false;
  					     });
    		    	 }
    		         
    		    },
  		        close: function close(x) {
  		        	x.info["Node"+x.idNode].nodePopover.isOpen = false;
  		        }
    	};
	}
})();

