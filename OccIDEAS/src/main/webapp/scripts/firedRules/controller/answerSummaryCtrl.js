(function() {
	angular.module('occIDEASApp.FiredRules').controller('AnswerSummaryCtrl',
			AnswerSummaryCtrl);

	AnswerSummaryCtrl.$inject = [ '$scope','$timeout',
	                           'AssessmentsService','$log','$compile',
	                           'ngToast', '$mdDialog','NgTableParams','name','interviewId',
	                           'answerId','answerName','InterviewsService','$q','$sessionStorage'];
	function AnswerSummaryCtrl($scope,$timeout,
			AssessmentsService,$log,$compile,
			$ngToast, $mdDialog,NgTableParams,name,interviewId,
			answerId,answerName,InterviewsService,$q,$sessionStorage) {
		var vm = this;
		$scope.moduleName = name;
		$scope.answerId = answerId;
		$scope.answerName = answerName;
		$scope.answerSummaryFilter = {
				answerId:$scope.answerId,
				name:$scope.answerName,
				moduleName:null,
				pageNumber:null,
				size:null
		};
		
		$scope.modules = function(column) {
			  var def = $q.defer();
			 
			  /* http service is based on $q service */
			  InterviewsService.getDistinctModules().then(function(response) {

			    var arr = [],
			      module = [];
			    angular.forEach(response.data, function(item) {
			      if (!_.find(module, _.matchesProperty('title', item.interviewModuleName))) {
			        if(item.idModule != $sessionStorage.activeIntro.value){
			    	arr.push(item.interviewModuleName);
			        module.push({
			          'id': item.interviewModuleName,
			          'title': item.interviewModuleName
			        });
			        }
			      }
			    });
			    
			    /* whenever the data is available it resolves the object*/
			    def.resolve(module);

			  });

			  return def;
			};
		
		var firstLoad = true;	
		vm.answerSummaryTableParams = new NgTableParams(
				{
					page: 1,            
	                count: 10
				},
			{
	        getData: function(params) {
	        	var currentPage = $scope.answerSummaryFilter.pageNumber;
	        	$scope.answerSummaryFilter.answerId=$scope.answerId;
	        	$scope.answerSummaryFilter.name=$scope.answerName;
	        	$scope.answerSummaryFilter.moduleName=lengthGreaterThan2(params.filter().interviewModuleName);
	        	$scope.answerSummaryFilter.pageNumber=params.page();
	        	$scope.answerSummaryFilter.size=params.count();
//	        	if(params.filter().reference){	
//		        	return $filter('filter')(vm.answerSummaryTableParams.settings().dataset, params.filter());
//	        	}
	        	return AssessmentsService.getAnswerSummaryByName($scope.answerSummaryFilter)
				.then(function(response){
					if(response.status == '200'){
		        		  var data = response.data.content;
		        		  if(firstLoad){
	        			  $scope.answerDesc = data[0].answerFreetext?data[0].answerFreetext:data[0].name;
		        		  $scope.count = response.data.totalSize;
		        		  }
//		        		  if(data.length > 0){
//		        				var removedData = _.remove(data,function(d){
//		        					return d.idinterview == interviewId;
//		        				});
//		        				data.unshift(removedData[0]);
//		        				var resultData = _.find(data,function(d){
//		        					return d.idinterview == interviewId;
//		        				});
//		        				resultData.hide = true;
//		        				resultData.strong = true;
//		        		  }
		        		  vm.originalData = angular.copy(data);
		        		  vm.answerSummaryTableParams.settings().dataset = data;
		        		  vm.answerSummaryTableParams.shouldGetData = false;
		        		  vm.answerSummaryTableParams.total(response.data.totalSize);
		        		  firstLoad = false;
				          return data;
		        	  }
				});
	        	vm.answerSummaryTableParams.settings().dataset = data;
	        	return data;
	        }
	      });
		
		vm.answerSummaryTableParams.goTo = function(event){
	        if(event.keyCode == 13 
	        		&& vm.answerSummaryTableParams.goToPageNumber != null
	        		&& !isNaN(vm.answerSummaryTableParams.goToPageNumber)){
	        	vm.answerSummaryTableParams.page(vm.answerSummaryTableParams.goToPageNumber);
	        	vm.answerSummaryTableParams.reload();
	        }
	    };
		
		function ifEmptyFilter(filter){
			if((!filter.answerId || filter.answerId.length == 0)&&
				(!filter.name || filter.name.length == 0)&&
				(!filter.moduleName || filter.moduleName.length == 0)){
						return true;
				}
		}
		
		function lengthGreaterThan2(variable){
			if(variable && variable.length > 2){
				return variable;
			}else{
				return null;
			}
		}
		
		vm.openFiredRules = function(interviewId,reference) {
			var interview = {
					interviewId:interviewId,
					referenceNumber:reference,
					moduleName:$scope.moduleName
			};
	    	$scope.addFiredRulesTab(interview);
	    	console.log('FiredRulesTab Opened');
	    }
	}

})();