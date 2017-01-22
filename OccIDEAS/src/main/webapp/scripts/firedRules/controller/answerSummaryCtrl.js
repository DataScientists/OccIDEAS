(function() {
	angular.module('occIDEASApp.FiredRules').controller('AnswerSummaryCtrl',
			AnswerSummaryCtrl);

	AnswerSummaryCtrl.$inject = [ '$scope', 'data','$timeout',
	                           'AssessmentsService','$log','$compile',
	                           'ngToast', '$mdDialog','NgTableParams'];
	function AnswerSummaryCtrl($scope, data,$timeout,
			AssessmentsService,$log,$compile,
			$ngToast, $mdDialog,NgTableParams) {
		var vm = this;
		$scope.answerDesc = data[0].answerFreetext?data[0].answerFreetext:data[0].name;
		$scope.count = data.length;
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

	}

})();