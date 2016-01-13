angular
  .module('occIDEASApp')
  .controller('moduleCtrl', ['$scope', 'Modules', function($scope, Modules) {
    $scope.title = "Modules";
    console.log('Modules');
    
    Modules.get().then(function(data) {
      $scope.items = data;
    });
  }]);