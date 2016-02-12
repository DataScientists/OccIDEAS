(function() {
	angular.module('occIDEASApp.Questions').directive('sticky', function() {
		return {
			restrict : 'A',
			link : function(scope, element, attrs) {
				element.height({
					height : function(event, ui) {
						$scope.apply(function() {
							$scope.height = document.height;
						});
					}
				});
			}
		};
	});

	angular
			.module('occIDEASApp.Questions')
			.directive(
					'addNote',
					function() {
						return {
							restrict : 'A',
							link : function(scope, element, attrs) {
								element.on( "click", function() {
									  newNote();
								});
							}
						};
					});
	
	angular
	.module('occIDEASApp.Questions')
	.directive(
			'deleteNote',
			function() {
				return {
					restrict : 'A',
					link : function(scope, element, attrs) {
						element.on( "click", function() {
							deleteNote(element);
						});
					}
				};
			});

	
})();

var noteZindex = 1050;
function deleteNote(elem) {
	angular.element(elem).parent().remove();
};

function newNote(element,$itemScope,$compile) {
if($itemScope.rule == null){
	return;
}	
var tpl = $compile(angular.element("#rules-template").html())($itemScope);	
angular.element(tpl).hide().appendTo(element).show("fade", 300).draggable().on(
		'dragstart', function() {
			angular.element(this).zIndex(++noteZindex);
		});

angular.element('textarea').autogrow();

angular.element('.note')
	return false;
};