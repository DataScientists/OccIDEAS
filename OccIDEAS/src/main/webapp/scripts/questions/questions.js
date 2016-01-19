(function(){
	angular
	  .module('occIDEASApp.Questions',['ui.router'])
	  .config(Config);
	
	Config.$inject = ['$stateProvider'];
	function Config($stateProvider){
		 $stateProvider
		    .state('questionView', {
		        url: '/questionView',
		        templateUrl: 'scripts/questions/view/questions.html',
		        controller: function($scope, list){
		        	console.log("list up:"+list);
		            $scope.list = list;
		        },
		        params:{row: null},
		        resolve:{
		        	list: function($stateParams, $http, $q, $timeout) {
		        		console.log("initializing list");
		        		return 	$http({
		  				  method: 'GET',
		  				  url: 'rest/module/get?id=' + $stateParams.row.idNode
		  				}).then(function successCallback(response) {
		  				    console.log("Success1");
		  				    return response;
		  				  }, function errorCallback(response) {
		  					  console.log("error retrieving questions:"+response);
		  				  });
		        	}
		        }
		 });
	}
})();