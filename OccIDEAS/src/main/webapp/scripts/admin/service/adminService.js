(function() {
	angular.module('occIDEASApp.Admin')
			.service('AdminService', AdminService);

	AdminService.$inject = [ '$http', '$q' ];
	function AdminService($http, $q) {
		var apiUrl = 'web/rest/';
		var adminEndpoint = apiUrl + 'admin';

		var getUserRoles = function() {
			return $http.get(adminEndpoint + '/getUserRoles', {
				cache : false
			}).then(function(response) {
				var data = response.data;
				return data;
			});
		};
		function handleError(response) {
			if (!angular.isObject(response.data) || !response.data.message) {
				return ($q.reject("An unknown error occurred."));
			}
			return ($q.reject(response.data.message));
		}

		function handleSuccess(response) {
			return (response);
		}

		return {
			getUserRoles : getUserRoles
		};
	}

})();