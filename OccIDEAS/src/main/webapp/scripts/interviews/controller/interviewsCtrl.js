(function() {
	angular.module('occIDEASApp.Interviews')
			.controller('InterviewsCtrl',InterviewsCtrl);

	InterviewsCtrl.$inject = [ 'data', '$scope', '$mdDialog','FragmentsService',
	                          '$q','QuestionsService','ModulesService',
	                          '$anchorScroll','$location','$mdMedia','$window','$state'];
	function InterviewsCtrl(data, $scope, $mdDialog, FragmentsService,
			$q,QuestionsService,ModulesService,
			$anchorScroll,$location,$mdMedia,$window,$state) {
		var self = this;
		$scope.data = data;	
		
		
		
		
		
		
	}
})();