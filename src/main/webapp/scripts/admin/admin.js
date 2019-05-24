(function() {
  angular
    .module('occIDEASApp.Admin', ['ui.router'])
    .config(Config)
    .directive('multiSelect', function($q) {
      return {
        restrict: 'E',
        require: 'ngModel',
        scope: {
          selectedLabel: "@",
          availableLabel: "@",
          displayAttr: "@",
          available: "=",
          model: "=ngModel"
        },
        template: '<div class="row">' +
          '<div class="col-md-6" style="padding-left: 0px;text-align: left;><span class="control-label" for="multiSelectSelected">{{ selectedLabel }} ' +
          '({{ model.length }})</span></div>' +
          '<div class="col-md-6" style="padding-right: 0px;text-align: right;"><span>{{ availableLabel }} ' +
          '({{ available.length }})</span></div>' +
          '<div class="row">' +
          '<div class="col-md-5"><div class="select">' +
          '<select id="currentRoles" style="width: 100%;" ng-model="selected.current" multiple ' +
          'ng-options="e as e[displayAttr] for e in model">' +
          '</select>' +
          '</div></div>' +
          '<div class="col-md-2 select buttons">' +
          '<button class="btn mover left" ng-click="add()" title="Add selected" ' +
          'ng-disabled="selected.available.length == 0">' +
          '<i class="glyphicon glyphicon-chevron-left"></i>' +
          '</button>' +
          '<button class="btn mover right" ng-click="remove()" title="Remove selected" ' +
          'ng-disabled="selected.current.length == 0">' +
          '<i class="glyphicon glyphicon-chevron-right"></i>' +
          '</button>' +
          '</div>' +
          '<div class="col-md-5" select">' +
          '<select id="multiSelectAvailable" style="width: 100%;" ng-model="selected.available" multiple ' +
          'ng-options="e as e[displayAttr] for e in available"></select>' +
          '</div>'
        ,
        link: function(scope, elm, attrs) {
          scope.selected = {
            available: [],
            current: []
          };

          /* Handles cases where scope data hasn't been initialized yet */
          var dataLoading = function(scopeAttr) {
            var loading = $q.defer();
            if(scope[scopeAttr]) {
              loading.resolve(scope[scopeAttr]);
            } else {
              scope.$watch(scopeAttr, function(newValue, oldValue) {
                if(newValue !== undefined)
                  loading.resolve(newValue);
              });
            }
            return loading.promise;
          };

          /* Filters out items in original that are also in toFilter. Compares by reference. */
          var filterOut = function(original, toFilter) {
            var filtered = [];
            angular.forEach(original, function(entity) {
              var match = false;
              for(var i = 0; i < toFilter.length; i++) {
                if(toFilter[i][attrs.displayAttr] == entity[attrs.displayAttr]) {
                  match = true;
                  break;
                }
              }
              if(!match) {
                filtered.push(entity);
              }
            });
            return filtered;
          };

          scope.refreshAvailable = function() {
            scope.available = filterOut(scope.available, scope.model);
            scope.selected.available = [];
            scope.selected.current = [];
          };

          scope.add = function() {
            scope.model = scope.model.concat(scope.selected.available);
            scope.refreshAvailable();
          };
          scope.remove = function() {
            scope.available = scope.available.concat(scope.selected.current);
            scope.model = filterOut(scope.model, scope.selected.current);
            scope.refreshAvailable();
          };

          $q.all([dataLoading("model"), dataLoading("available")]).then(function(results) {
            scope.refreshAvailable();
          });
        }
      }
    });

  Config.$inject = ['$stateProvider'];

  function Config($stateProvider) {
  }
})();