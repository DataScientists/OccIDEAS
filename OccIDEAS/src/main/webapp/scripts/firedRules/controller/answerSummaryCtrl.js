(function() {
	angular.module('occIDEASApp.FiredRules').controller('AnswerSummaryCtrl',
			AnswerSummaryCtrl);

	AnswerSummaryCtrl.$inject = [ '$scope','$timeout',
	                           'AssessmentsService','$log','$compile',
	                           'ngToast', '$mdDialog','NgTableParams','interviewId',
	                           'answerId','answerName','InterviewsService','$q','$sessionStorage','nodeCode','nodeVO'];
	function AnswerSummaryCtrl($scope,$timeout,
			AssessmentsService,$log,$compile,
			$ngToast, $mdDialog,NgTableParams,interviewId,
			answerId,answerName,InterviewsService,$q,$sessionStorage,nodeCode,nodeVO) {
		var vm = this;
		$scope.moduleName = nodeVO.name;
		$scope.nodeClassName = nodeVO.nodeclass == 'M'?'Module':'AJSM';
		$scope.answerId = answerId;
		$scope.answerName = answerName;
		$scope.nodeCode = nodeCode;
		$scope.answerSummaryFilter = {
				answerId:$scope.answerId,
				name:$scope.answerName,
				idParticipant:null,
				reference:null,
				idinterview:null,
				moduleName:null,
				assessedStatus:null,
				statusDescription:null,
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

			$scope.statusDescriptions = function(column) {
				var arr = [];
				arr.push({
			          'id': '',
			          'title': ''
			        });
				arr.push({
			          'id': 'Running',
			          'title': 'Running'
			        });
				arr.push({
			          'id': 'Partial',
			          'title': 'Partial'
			        });
				arr.push({
			          'id': 'Completed',
			          'title': 'Completed'
			        });
				return arr;
			};
			
			$scope.assessedStatusList = function(column) {
				var arr = [];
				arr.push({
			          'id': '',
			          'title': ''
			        });
				arr.push({
			          'id': 'Not Assessed',
			          'title': 'Not Assessed'
			        });
				arr.push({
			          'id': 'Complete',
			          'title': 'Complete'
			        });
				arr.push({
			          'id': 'Auto Assessed',
			          'title': 'Auto Assessed'
			        });
				return arr;
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
	        	$scope.answerSummaryFilter.idParticipant=lengthGreaterThan2(params.filter().idParticipant);
	        	$scope.answerSummaryFilter.reference=lengthGreaterThan2(params.filter().reference);
	        	$scope.answerSummaryFilter.idinterview=lengthGreaterThan2(params.filter().idinterview);
	        	$scope.answerSummaryFilter.moduleName=lengthGreaterThan2(params.filter().interviewModuleName);
	        	$scope.answerSummaryFilter.assessedStatus=lengthGreaterThan2(params.filter().assessedStatus);
	        	$scope.answerSummaryFilter.statusDescription=lengthGreaterThan2(params.filter().statusDescription);
	        	$scope.answerSummaryFilter.pageNumber=params.page();
	        	$scope.answerSummaryFilter.size=params.count();
	        	return AssessmentsService.getAnswerSummaryByName($scope.answerSummaryFilter)
				.then(function(response){
					if(response.status == '200'){
		        		  var data = response.data.content;
		        		  if(firstLoad){
	        			  $scope.answerDesc = data[0].answerFreetext?data[0].answerFreetext:data[0].name;
		        		  $scope.count = response.data.totalSize;
		        		  }
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
		
		vm.openFiredRules = function(interviewId,reference,interviewModuleName) {
			InterviewsService.findModulesByInterviewId(interviewId).then(function(response){
				if(response.status == '200'){
					if(response.data.length > 0){								
						var modules = response.data;

						var isSameIntroModule = true;
						var introModule = _.find(modules,function(module){
							return module.idModule == $sessionStorage.activeIntro.value;
						});
						
						if(!introModule){
							isSameIntroModule = false;
						}
						
						var interview = {
								interviewId:interviewId,
								referenceNumber:reference,
								moduleName:$scope.moduleName,
								isSameIntroModule:isSameIntroModule
						};
				    	$scope.addFiredRulesTab(interview);
						
				    	console.log('FiredRulesTab Opened');
					}
				}
			});
	    }
	}

})();