(function(){
	angular
	  .module('occIDEASApp.Admin')
	  .controller('AdminCtrl',AdminCtrl);
	
	AdminCtrl.$inject = ['data'];
	function AdminCtrl(data){
		var vm = this;
		vm.data = data;
	}
	
})();