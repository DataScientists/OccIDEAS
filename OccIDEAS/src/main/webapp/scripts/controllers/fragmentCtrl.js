angular
  .module('occIDEASApp')
  .controller('fragmentCtrl', ['$scope', 'Modules', function($scope, Modules) {
    $scope.title = "Modules";
    Modules.get().then(function(data) {
      $scope.items = data;
    });
  }]);