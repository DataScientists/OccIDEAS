(function(){
	angular.module('occIDEASApp.NodeLanguage')
		   .controller('NodeLanguageCtrl',NodeLanguageCtrl);
	NodeLanguageCtrl.$inject = ['NodeLanguageService',
	                            'ngTableParams',
	                            '$state',
	                            '$scope',
	                            '$filter',
	                            '$log',
	                            '$mdDialog',
	                            '$timeout',
	                            '$q',
	                            'ngToast'];
	function NodeLanguageCtrl(NodeLanguageService,NgTableParams,$state,$scope,$filter,
			$log,$mdDialog,$timeout,$q,
			$ngToast){
		var self = this;
		self.isDeleting = false;
		var dirtyCellsByRow = [];
	    var invalidCellsByRow = [];
	    $scope.$root.tabsLoading = false;
	    
	}
})();

