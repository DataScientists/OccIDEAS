angular
  .module('occIDEASApp')
  .controller('agentCtrl', ['$scope', 'Modules', function($scope, Modules) {
    $scope.title = "Agents";
    Modules.get().then(function(data) {
      $scope.items = data;
    });
  }]);