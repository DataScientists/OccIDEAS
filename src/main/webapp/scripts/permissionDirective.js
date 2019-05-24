(function() {

  angular.module('occIDEASApp')
    .directive('permission', ['AuthenticationService', function(Auth) {
      return {
        restrict: 'A',
        scope: {
          permission: '='
        },

        link: function(scope, elem, attrs) {
          scope.$watch(Auth.isLoggedIn, function() {
            if(Auth.userHasPermission(scope.permission)) {
              elem.show();
            } else {
              elem.hide();
            }
          });
        }
      }
    }]);

})();