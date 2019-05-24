(function() {

  angular.module('occIDEASApp.Login').service('dataBeanService', dataBeanService);
  dataBeanService.$inject = ['$q', '$filter', '$timeout', '$http', '$window'];

  function dataBeanService($q, $filter, $timeout, $http, $window) {
    var StatetransitionHasErr;
    var FacRoleDDValues = {};
    var FacDdValues = [];
    var RoleDdValues = [];

    function setStatetransitionHasErr(value) {
      StatetransitionHasErr = value;
    }

    function getStatetransitionHasErr() {
      return StatetransitionHasErr;
    }

    function setFacRoleDDValues(obj) {
      FacRoleDDValues = obj;
      $window.sessionStorage.setItem('FacRoleDDValues', angular.toJson(FacRoleDDValues));
    }

    function getFacRoleDDValues() {
      if(Object.keys(FacRoleDDValues).length === 0) {

        FacRoleDDValues = angular.fromJson($window.sessionStorage.getItem('FacRoleDDValues'));
      }
      return FacRoleDDValues;
    }

    function setFacDdValues(obj) {
      FacDdValues = obj;
      $window.sessionStorage.FacDdValues = FacDdValues;
    }

    function getFacDdValues() {
      if(FacDdValues.length === 0) {
        FacDdValues = $window.sessionStorage.FacDdValues.split(',');
      }
      return FacDdValues;
    }

    function setRoleDdValues(obj) {
      RoleDdValues = obj;
      $window.sessionStorage.RoleDdValues = RoleDdValues;
    }

    function getRoleDdValues() {
      if(RoleDdValues.length === 0) {
        RoleDdValues = $window.sessionStorage.RoleDdValues.split(',');
      }
      return RoleDdValues;
    }

    return {
      setStatetransitionHasErr: setStatetransitionHasErr,
      getStatetransitionHasErr: getStatetransitionHasErr,
      setFacRoleDDValues: setFacRoleDDValues,
      getFacRoleDDValues: getFacRoleDDValues,
      setFacDdValues: setFacDdValues,
      getFacDdValues: getFacDdValues,
      setRoleDdValues: setRoleDdValues,
      getRoleDdValues: getRoleDdValues
    };

  }
})();