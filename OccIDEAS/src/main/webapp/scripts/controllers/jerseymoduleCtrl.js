angular
  .module('occIDEASApp')
  .controller('jerseymoduleCtrl', ['$scope', 'JerseyModules', function($scope, Modules) {
    $scope.title = "Modules";
    Modules.get().then(function(data) {
      $scope.items = data;
    });
  }]);