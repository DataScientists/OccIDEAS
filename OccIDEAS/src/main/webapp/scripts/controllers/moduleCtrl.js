angular
  .module('occIDEASApp')
  .controller('moduleCtrl', ['$scope', 'Modules', function($scope, Modules) {
    $scope.title = "Modules";
    console.log('Modules');
    
    Modules.get().then(function(data) {
      $scope.items = data;
    });
    
    //Modules.deleteModule(moduleIndex).then(function(data) {
    //	$scope.items.splice(moduleIndex,1);
    //});
    
  }]);