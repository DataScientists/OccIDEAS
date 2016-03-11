(function(){
	angular.module('occIDEASApp.Assessments')
		   .controller('AssessmentsCtrl',AssessmentsCtrl);
	AssessmentsCtrl.$inject = ['AssessmentsService','ngTableParams','$scope','$filter',
                          'data','$log'];
	function AssessmentsCtrl(AssessmentsService,NgTableParams,$scope,$filter,
			data,$log){
		var self = this;
		
		var getData = function(){
			$log.info("Data getting from interviews ajax"); 
			AssessmentsService.getInterviews().then(function(data) {
				$log.info("Data received from interviews ajax");     	 
		    	  return data;
		      });
			}
		self.tableParams = new NgTableParams(
				{}, 
				{	
					getData: function(params) {
						if((params.filter().referenceNumber)||(params.filter().moduleName)){	
				        	return $filter('filter')(self.tableParams.settings().dataset, params.filter());
				        }	
						
						if ((params.sorting().referenceNumber)||(params.sorting().moduleName)){
							return $filter('orderBy')(self.tableParams.settings().dataset, params.orderBy());
				        }
					    if(!self.tableParams.shouldGetData){
					    	return self.tableParams.settings().dataset;
					    }
					    $log.info("Data getting from interviews ajax ..."); 
					    return AssessmentsService.getInterviews().then(function(data) {
					        	  $log.info("Data received from interviews ajax ...");        	 
					        	  self.originalData = angular.copy(data);
					        	  self.tableParams.settings().dataset = data;
					            return data;
					          });
					    },
				});
		self.tableParams.shouldGetData = true;
	}
})();

