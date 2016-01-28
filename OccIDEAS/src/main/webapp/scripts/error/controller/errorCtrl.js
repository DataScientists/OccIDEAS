(function(){
	angular
	  .module("occIDEASApp")
	  .controller("ErrorCtrl",ErrorCtrl);
	
	ErrorCtrl.$inject = ['$scope','error'];
	function ErrorCtrl($scope,error){
		$scope.error = error;
	}
})();