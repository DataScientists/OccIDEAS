angular
  .module('occIDEASApp')
  .controller('jerseymoduleCtrl', ['$scope', 'jerseyModules', function($scope, jerseyModules) {
    $scope.title = "Jersey Modules";
    console.log('jerseyModules');
    
    jerseyModules.get().then(function(data) {
      $scope.items = data;
    });
  }]);