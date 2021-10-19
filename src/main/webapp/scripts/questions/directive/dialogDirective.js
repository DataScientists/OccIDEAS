(function() {
  angular.module('occIDEASApp.Questions').directive('sticky', function() {
    return {
      restrict: 'A',
      link: function(scope, element, attrs) {
        element.height({
          height: function(event, ui) {
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
          restrict: 'A',
          link: function(scope, element, attrs) {
            element.on("click", function() {
              newNote();
            });
          }
        };
      });

  angular
    .module('occIDEASApp.Questions')
    .directive(
      'showRuleDialog',
      function() {
        return {
          restrict: 'A',
          link: function(scope, element, attrs) {
            element.on("click", function() {
              showRuleDialog();
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
          restrict: 'A',
          link: function(scope, element, attrs) {
            element.on("click", function() {
              closeRuleDialog(element);
            });
          }
        };
      });


})();

var noteZindex = 10500;

function newNote(element, $itemScope, $compile, config) {
  if($itemScope.rule == null) {
    return;
  }
  if(angular.element('.note') && angular.element('.note').length == 0) {
    noteZindex = 10500;
  }
  var tpl = $compile(angular.element("#rules-template").html())($itemScope);
  angular.element(tpl).zIndex(++noteZindex);

  var wrapper = angular.element('#allModuleRulesOfAgent').parent().parent()[0];
  var maxDialogNum = 4;

  if(wrapper != null) {
    var position = wrapper.getBoundingClientRect();
    leftPoint = (position.left);
  }

  var zFactor = 10500;
  config.leftPoint = config.leftPoint + config.tplWidth;

  var temp = noteZindex - zFactor;
  if ((temp % maxDialogNum) === 1) {
    config.topPoint = config.topPoint + config.maxHeight;
    config.leftPoint = 100;
    config.maxHeight = 0;
  }

  angular.element(tpl).css('left', config.leftPoint + 'px');
  angular.element(tpl).css('top', config.topPoint + 'px');

  angular.element(tpl).hide().appendTo(element).show("fade", 300).draggable().on(
    'dragstart', function() {
      angular.element(this).zIndex(++noteZindex);
    });
  angular.element('textarea').autogrow();
  angular.element('.note');

  var tplElement = angular.element(tpl);
  //console.log('tplElement', tplElement);
  config.tplWidth = tplElement[0].clientWidth;
  config.tplHeight = tplElement[0].clientHeight;

  if (config.tplHeight > 130) {
    config.tplHeight = config.tplHeight + 100
  } else {
    config.tplHeight = config.tplHeight + 5
  }

  if (config.tplHeight > config.maxHeight) {
    config.maxHeight = config.tplHeight;
  }

  return false;
}

var noteWrapperZindex = 10499;

function newRuleWrapper(element, $itemScope, $compile) {
  if($itemScope.ruleWrapper == null) {
    return;
  }
  var wrapper = $compile(angular.element("#rules-template-wrapper").html())($itemScope);
  angular.element(wrapper).zIndex(++noteWrapperZindex);

  var zFactor = 10499;
  var topPoint = ((noteWrapperZindex - zFactor) * 100);

  var maxDialogNum = 5;
  var leftPoint = 50;
  var temp = noteWrapperZindex - zFactor;
  if(temp > maxDialogNum) {
    topPoint = (((temp % 6) + 1) * 100) + 5;
    leftPoint = leftPoint + (Math.floor((noteWrapperZindex - zFactor) / maxDialogNum) * 20);
  }

  angular.element(wrapper).css('left', leftPoint + 'px');
  angular.element(wrapper).css('top',  topPoint + 'px');

  angular.element(wrapper).hide().appendTo(element).show("fade", 300).draggable().on(
    'dragstart', function() {
      angular.element(this).zIndex(++noteWrapperZindex);
    });

  angular.element('.noteWrapper');

  return false;
}

function showRuleDialog(element, $itemScope, $compile) {
  if($itemScope.rule == null) {
    return;
  }
  if(angular.element('.note') && angular.element('.note').length == 0) {
    noteZindex = 10500;
  }
  var tpl = $compile(angular.element("#rules-template").html())($itemScope);
  angular.element(tpl).zIndex(++noteZindex);
  var substractFrom = noteZindex - 10500;
  substractFrom = substractFrom * 20;
  var leftPoint = -Math.abs(1000 - substractFrom);
  angular.element(tpl).css('left', leftPoint + 'px');
  //angular.element(tpl).css('top', topPoint+'px');
  angular.element(tpl).hide().appendTo(element).show("fade", 300).draggable().on(
    'dragstart', function() {
      angular.element(this).zIndex(++noteZindex);
    });
  angular.element('textarea').autogrow();
  angular.element('.note');

  return false;
}

var noteIntZindex = 1050;

function firedRuleDialog(element, $itemScope, $compile, interviewId) {
  var tpl = $compile(angular.element("#interview-template").html())($itemScope);
  angular.element(tpl).zIndex(++noteIntZindex);

  var wrapper = angular.element('#interview-wrapper-' + interviewId).parent()[0];
  var maxDialogNum = 5;
  var leftPoint = 965;

  if(wrapper != null) {
    var position = wrapper.getBoundingClientRect();
    leftPoint = position.right;
  }
  var zFactor = 1050;
  var topPoint = ((noteIntZindex - zFactor) * 100) + 50;
  var temp = noteIntZindex - zFactor;

  if(temp > maxDialogNum) {
    topPoint = (((temp % 6) + 1) * 100) + 50;
    leftPoint = leftPoint + (Math.floor((noteIntZindex - zFactor) / maxDialogNum) * 20);
  }
  angular.element(tpl).css('left', leftPoint + 'px');
  angular.element(tpl).css('top', topPoint + 'px');
  angular.element(tpl).hide().appendTo("#interview-wrapper-" + interviewId).show("fade", 300).draggable().on(
    'dragstart', function() {
      angular.element(this).zIndex(++noteIntZindex);
    });
  angular.element('textarea').autogrow();
  angular.element('.int-note');
  return false;
}

function editAssessmentDialog(element, $itemScope, $compile, interviewId) {
  var tpl = $compile(angular.element("#editAssessment-template").html())($itemScope);
  angular.element(tpl).zIndex(++noteIntZindex);
  angular.element(tpl).hide().appendTo("#interview-wrapper-" + interviewId).show("fade", 300).draggable().on(
    'dragstart', function() {
      angular.element(this).zIndex(++noteIntZindex);
    });
  angular.element('textarea').autogrow();
  angular.element('.int-note');

  return false;
}