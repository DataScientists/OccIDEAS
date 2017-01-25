(function() {
	angular.module('occIDEASApp.FiredRules').controller('AnswerSummaryCtrl',
			AnswerSummaryCtrl);

	AnswerSummaryCtrl.$inject = [ '$scope', 'data','$timeout',
	                           'AssessmentsService','$log','$compile',
	                           'ngToast', '$mdDialog','NgTableParams','name'];
	function AnswerSummaryCtrl($scope, data,$timeout,
			AssessmentsService,$log,$compile,
			$ngToast, $mdDialog,NgTableParams,name) {
		var vm = this;
		$scope.answerDesc = data[0].answerFreetext?data[0].answerFreetext:data[0].name;
		$scope.count = data.length;
		$scope.moduleName = name;
		if(data.length > 0){
			data[0].hide = true;
			data[0].strong = true;
		}
		vm.answerSummaryTableParams = new NgTableParams(
				{
				},
			{
	        getData: function(params) {
	        	if(params.filter().reference){	
		        	return $filter('filter')(vm.answerSummaryTableParams.settings().dataset, params.filter());
	        	}
	        	vm.answerSummaryTableParams.settings().dataset = data;
	            return data;
	        }
	      });
		
		
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