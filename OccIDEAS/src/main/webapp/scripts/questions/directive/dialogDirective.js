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
			'closeRuleDialog',
			function() {
				return {
					restrict : 'A',
					link : function(scope, element, attrs) {
						element.on( "click", function() {
							closeRuleDialog(element);
						});
					}
				};
			});

	
})();

var noteZindex = 1050;
function newNote(element,$itemScope,$compile) {
	if($itemScope.rule == null){
		return;
	}	
	var tpl = $compile(angular.element("#rules-template").html())($itemScope);	
	angular.element(tpl).zIndex(++noteZindex);
	angular.element(tpl).hide().appendTo(element).show("fade", 300).draggable().on(
		'dragstart', function() {
			angular.element(this).zIndex(++noteZindex);
		});
	angular.element('textarea').autogrow();
	angular.element('.note');

	return false;
};

var noteIntZindex = 1050;
function newInterviewNote(element,$itemScope,$compile) {
	var tpl = $compile(angular.element("#interview-template").html())($itemScope);	
	angular.element(tpl).zIndex(++noteIntZindex);
	angular.element(tpl).hide().appendTo("#interview-wrapper").show("fade", 300).draggable().on(
		'dragstart', function() {
			angular.element(this).zIndex(++noteIntZindex);
		});
	angular.element('textarea').autogrow();
	angular.element('.int-note');

	return false;
};