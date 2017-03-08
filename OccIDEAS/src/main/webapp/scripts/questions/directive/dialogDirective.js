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

var noteZindex = 10500;
function newNote(element,$itemScope,$compile) {
	if($itemScope.rule == null){
		return;
	}	
	var tpl = $compile(angular.element("#rules-template").html())($itemScope);	
	angular.element(tpl).zIndex(++noteZindex);
	
	var wrapper = angular.element('#allModuleRulesOfAgent').parent().parent()[0];
	var maxDialogNum = 5;
	var leftPoint = 965;
	
	if(wrapper != null){
		var position = wrapper.getBoundingClientRect();
		leftPoint = (position.left);
	}
	var zFactor = 10500;
	var topPoint = ((noteZindex-zFactor)*100);
	var temp = noteZindex-zFactor;
	
	if(temp > maxDialogNum){
		topPoint = (((temp%6) + 1) * 100) + 5;
		leftPoint = leftPoint + (Math.floor((noteZindex-zFactor)/maxDialogNum) * 20);				
	}
	angular.element(tpl).css('left', leftPoint+'px');
	angular.element(tpl).css('top', topPoint+'px');
	
	angular.element(tpl).hide().appendTo(element).show("fade", 300).draggable().on(
		'dragstart', function() {
			angular.element(this).zIndex(++noteZindex);
		});
	angular.element('textarea').autogrow();
	angular.element('.note');

	return false;
};

var noteIntZindex = 1050;
function firedRuleDialog(element,$itemScope,$compile,interviewId) {
	var tpl = $compile(angular.element("#interview-template").html())($itemScope);
	angular.element(tpl).zIndex(++noteIntZindex);
	
	var wrapper = angular.element('#interview-wrapper-'+interviewId).parent()[0];
	var maxDialogNum = 5;
	var leftPoint = 965;
	
	if(wrapper != null){
		var position = wrapper.getBoundingClientRect();
		leftPoint = position.right;
	}
	var zFactor = 1050;
	var topPoint = ((noteIntZindex-zFactor)*100) + 50;
	var temp = noteIntZindex-zFactor;
	
	if(temp > maxDialogNum){
		topPoint = (((temp%6) + 1) * 100) + 50 ;
		leftPoint = leftPoint + (Math.floor((noteIntZindex-zFactor)/maxDialogNum) * 20);				
	}
	angular.element(tpl).css('left', leftPoint+'px');
	angular.element(tpl).css('top', topPoint+'px');
	angular.element(tpl).hide().appendTo("#interview-wrapper-"+interviewId).show("fade", 300).draggable().on(
		'dragstart', function() {
			angular.element(this).zIndex(++noteIntZindex);
		});
	angular.element('textarea').autogrow();
	angular.element('.int-note');
	return false;
};
function editAssessmentDialog(element,$itemScope,$compile,interviewId) {
	var tpl = $compile(angular.element("#editAssessment-template").html())($itemScope);	
	angular.element(tpl).zIndex(++noteIntZindex);
	angular.element(tpl).hide().appendTo("#interview-wrapper-"+interviewId).show("fade", 300).draggable().on(
		'dragstart', function() {
			angular.element(this).zIndex(++noteIntZindex);
		});
	angular.element('textarea').autogrow();
	angular.element('.int-note');

	return false;
};