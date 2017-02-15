(function() {
	angular.module('occIDEASApp.FiredRules').controller('AnswerSummaryCtrl',
			AnswerSummaryCtrl);

	AnswerSummaryCtrl.$inject = [ '$scope','$timeout',
	                           'AssessmentsService','$log','$compile',
	                           'ngToast', '$mdDialog','NgTableParams','name','interviewId',
	                           'answerId','answerName'];
	function AnswerSummaryCtrl($scope,$timeout,
			AssessmentsService,$log,$compile,
			$ngToast, $mdDialog,NgTableParams,name,interviewId,
			answerId,answerName) {
		var vm = this;
		$scope.moduleName = name;
		$scope.answerId = answerId;
		$scope.answerName = answerName;
		$scope.answerSummaryFilter = {
				answerId:$scope.answerId,
				name:$scope.answerName,
				moduleName:null,
				pageNumber:null,
				size:null
		};
		
		vm.answerSummaryTableParams = new NgTableParams(
				{
					page: 1,            
	                count: 10
				},
			{
	        getData: function(params) {
	        	var currentPage = $scope.answerSummaryFilter.pageNumber;
	        	$scope.answerSummaryFilter.answerId=$scope.answerId;
	        	$scope.answerSummaryFilter.name=$scope.answerName;
	        	$scope.answerSummaryFilter.moduleName=lengthGreaterThan2(params.filter().moduleName);
	        	$scope.answerSummaryFilter.pageNumber=params.page();
	        	$scope.answerSummaryFilter.size=params.count();
//	        	if(params.filter().reference){	
//		        	return $filter('filter')(vm.answerSummaryTableParams.settings().dataset, params.filter());
//	        	}
	        	return AssessmentsService.getAnswerSummaryByName($scope.answerSummaryFilter)
				.then(function(response){
					if(response.status == '200'){
		        		  var data = response.data.content;
		        		  $scope.answerDesc = data[0].answerFreetext?data[0].answerFreetext:data[0].name;
		        		  $scope.count = data.length;
//		        		  if(data.length > 0){
//		        				var removedData = _.remove(data,function(d){
//		        					return d.idinterview == interviewId;
//		        				});
//		        				data.unshift(removedData[0]);
//		        				var resultData = _.find(data,function(d){
//		        					return d.idinterview == interviewId;
//		        				});
//		        				resultData.hide = true;
//		        				resultData.strong = true;
//		        		  }
		        		  vm.originalData = angular.copy(data);
		        		  vm.answerSummaryTableParams.settings().dataset = data;
		        		  vm.answerSummaryTableParams.shouldGetData = false;
		        		  vm.answerSummaryTableParams.total(response.data.totalSize);					        	  
				          return data;
		        	  }
				});
	        	vm.answerSummaryTableParams.settings().dataset = data;
	            return data;
	        }
	      });
		
		vm.answerSummaryTableParams.goTo = function(event){
	        if(event.keyCode == 13 
	        		&& vm.answerSummaryTableParams.goToPageNumber != null
	        		&& !isNaN(vm.answerSummaryTableParams.goToPageNumber)){
	        	vm.answerSummaryTableParams.page(vm.answerSummaryTableParams.goToPageNumber);
	        	vm.answerSummaryTableParams.reload();
	        }
	    };
		
		function ifEmptyFilter(filter){
			if((!filter.answerId || filter.answerId.length == 0)&&
				(!filter.name || filter.name.length == 0)&&
				(!filter.moduleName || filter.moduleName.length == 0)){
						return true;
				}
		}
		
		function lengthGreaterThan2(variable){
			if(variable && variable.length > 2){
				return variable;
			}else{
				return null;
			}
		}
		
		vm.openFiredRules = function(interviewId,reference) {
			var interview = {
					interviewId:interviewId,
					referenceNumber:reference,
					moduleName:$scope.moduleName
			};
	    	$scope.addFiredRulesTab(interview);
	    	console.log('FiredRulesTab Opened');
	    }
	}

})();