(function(){
	angular
	  .module('occIDEASApp.Questions').directive("saveFragmentModal", function ($modal) {
		    "use strict";
		    return {
		      template: '<div ng-click="openFragmentModal(rowData)" ng-transclude></div>',
		      replace: true,
		      transclude: true,
		      scope: {
		        rowData: '&saveFragmentModal' 
		      },
		      link: function (node, element, attrs) {
		    	  node.openFragmentModal = function (node) {
		            $modal.open({
		            template: "<div>Save Fragment</div>"
		            	+ "<div class=\"modal-footer\">"
		            	+ "<label>Fragment name {{node.name}}</label><input type='text' ng-model='node.name' />"
                        + "<button class=\"btn btn-primary\" ng-click=\"ok()\">Save</button>"
		                        + "<button class=\"btn btn-warning\" ng-click=\"cancel()\">Cancel</button>"
		                        + "</div>",
		            controller: function ($scope, $modalInstance) {
		                $scope.ok = function ($scope) {
		                	console.log($scope);
		                    $modalInstance.close();
		                };
		      
		                $scope.cancel = function () {
		                	$modalInstance.close();
		                };
	
		            },
		            backdrop: "static"
		        });
		        }
		      }
		    };
		});
})();