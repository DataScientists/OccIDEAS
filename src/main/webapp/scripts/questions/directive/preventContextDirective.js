(function() {
  angular
    .module('occIDEASApp.Questions').directive('preventRightClick', function() {
    return {
      retrict: 'A',
      link: function($scope, $ele) {
        $ele.bind("contextmenu", function(e) {
          e.preventDefault();
          e.stopPropagation();
        });
      }
    }
  });
})();