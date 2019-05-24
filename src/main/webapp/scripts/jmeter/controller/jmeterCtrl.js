(function(){
	angular
	  .module('occIDEASApp.JMeter')
	  .controller('JMeterCtrl',JMeterCtrl);
	
	JMeterCtrl.$inject = ['$log','ngTableParams','$scope','$filter','JMeterService','ngToast'];
	function JMeterCtrl($log,NgTableParams,$scope,$filter,JMeterService,ngToast){
		var self = this;
		
		self.exportJMX = function(){
			if($scope.checkboxes.items == undefined && $scope.checkboxes.items.length < 1){
				ngToast.create({
		    		  className: 'danger',
		    		  content: "No logs selected for export."
		    	 });
				return;
			}
			var data = [];
			_.each($scope.checkboxes.items,function(value, key){
				 if(value){
					 var log = _.find($scope.data,function(log){
						 return log.id == key;
					 });
					 data.push(log);
				 }
			 });
			JMeterService.exportJMeter(data).then(function(response){
	        	  if(response.status == 200){
	        		   var jmxFile = new Blob([response.data], { type: 'application/octet-stream' }); 
						var jmxUrl = window.URL.createObjectURL(jmxFile);
						 var a = document.createElement("a");
				            document.body.appendChild(a);
				            a.style = "display: none";
		                a.href = jmxUrl;
		                a.download = 'jmeterreport.jmx';
		                a.click();
	        	  }else{
	        			ngToast.create({
				    		  className: 'danger',
				    		  content: "response was "+response.status+" - Unable to export JMX."
				    	 });
	        			return;
	        	  }
            });
	         }
		
		self.tableParams = new NgTableParams(
				{
				}, 
				{	
	        getData: function(params) {
	        	
	        	if(!$scope.data){
	        		self.tableParams.shouldGetData = true;
	        	}
	        	else{
	        		var orderedFilteredDataset = $scope.data;
	        		if (params.sorting().sessionId
		           		  || params.sorting().userId
		          		  || params.sorting().function
	        			  || params.sorting().createdDate){
	        			
		        		self.tableParams.shouldGetData = false;
		        		orderedFilteredDataset = $filter('orderBy')($scope.data, params.orderBy());			  				
			  		}
	        		
			        if(params.filter().sessionId 
	        		  || params.filter().userId 
	        		  || params.filter().function
	        		  || params.filter().createdDate){	
			        	
			        	self.tableParams.shouldGetData = false;
			        	orderedFilteredDataset = $filter('filter')(orderedFilteredDataset, params.filter());			        	
			        }
			        
			        self.tableParams.settings().dataset = orderedFilteredDataset;	        		
	        	}

	          if(!self.tableParams.shouldGetData){
	        	  return self.tableParams.settings().dataset;
	          }
	          $log.info("Data getting from admin ajax ..."); 
	          return  JMeterService.getJMXLogs().then(function(response){
	        	  if(response.status == 200){
	        		  $scope.data = response.data;
	        		  self.tableParams.settings().dataset = $scope.data;
	        		  self.tableParams.shouldGetData = true;
	        		  return $scope.data;
	        	  }else{
	        			ngToast.create({
				    		  className: 'danger',
				    		  content: "response was "+response.status+" - Unable to retrieve JMXLogs."
				    	 });
	        			return [{}];
	        	  }
              });
	         }
	      });
		self.tableParams.shouldGetData = true;
		
		$scope.checkboxes = { 'checked': false, items: {} };

		// watch for check all checkbox
		$scope.$watch('checkboxes.checked', function(value) {	    	
		    angular.forEach(self.tableParams.settings().dataset, function(item) {
		        if (angular.isDefined(item.id)) {
		            $scope.checkboxes.items[item.id] = value;
		        }
		    });
		});
		
		// watch for data checkboxes
		$scope.$watch('checkboxes.items', function(values) {
		    if (!self.tableParams.settings().dataset) {
		        return;
		    }
		    var checked = 0, unchecked = 0,
		        total = self.tableParams.settings().dataset.length;
		    angular.forEach(self.tableParams.settings().dataset, function(item) {
		        checked   +=  ($scope.checkboxes.items[item.id]) || 0;
		        unchecked += (!$scope.checkboxes.items[item.id]) || 0;
		    });
		    if ((unchecked == 0) || (checked == 0)) {
		        $scope.checkboxes.checked = (checked == total);
		    }
		    // grayed checkbox
		    angular.element(document.getElementById("select_all")).prop("indeterminate", (checked != 0 && unchecked != 0));
		    
		  }, true);
		
	}
	
})();