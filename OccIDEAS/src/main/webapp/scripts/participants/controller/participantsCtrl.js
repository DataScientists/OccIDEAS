(function(){
	angular.module('occIDEASApp.Participants')
		   .controller('ParticipantsCtrl',ParticipantsCtrl);
	
	ParticipantsCtrl.$inject = ['ParticipantsService','NgTableParams','$state','$scope','$filter','data','InterviewsService','$resource'];
	function ParticipantsCtrl(ParticipantsService,NgTableParams,$state,$scope,$filter,data,InterviewsService,$resource){
		var self = this;
		$scope.data = data;
		$scope.$root.tabsLoading = false;
		$scope.setSelectedInterview = function(interview){
			$scope.selectedInterview = interview;
		}
		$scope.awesIdMaxSize = 7;
		$scope.awesIdSize = 0;
		$scope.filterAndValidate = function(event){			
			var elem = angular.element( document.querySelector('#awesid'));
			var counter = angular.element( document.querySelector('#awesidcounter'));
			var label = angular.element( document.querySelector('#awesidlabel'));
			if(event.which === 13 || event.which === 32){
				if(awesIdIsValid(elem.val())){
					self.add();
				}else{
					counter.append("Please enter a valid AWES ID");
				}
			}else{
				if(!awesIdIsValid(elem.val())){
					elem.addClass("awesidwarning");
					label.addClass("awesidwarning");
					counter.addClass("awesidwarning");
				}else{
					elem.removeClass("awesidwarning");
					label.removeClass("awesidwarning");
					counter.removeClass("awesidwarning");						
				}								
			}
		}
		function awesIdIsValid(awesId){
			$scope.searchAWESID = '';
			var retValue = false;
			$scope.awesIdSize = awesId.length;
			if($scope.awesIdSize==$scope.awesIdMaxSize){
				if(_.startsWith(awesId, 'H')){
					retValue = true;
					$scope.searchAWESID = awesId;
				}
			}			
			return retValue;
		}
		self.isDeleting = false;
		var dirtyCellsByRow = [];
	    var invalidCellsByRow = [];
	    self.tableParams = new NgTableParams(
			{	
				page: 1,            
                count: 10
            }, {	
            
			getData: function(params) {
	          if ((params.sorting().reference)||(params.sorting().idParticipant)||(params.sorting().statusDescription)){
				return $filter('orderBy')(self.tableParams.settings().dataset, params.orderBy());
		      }
	        	
	          if(params.filter().reference || params.filter().idParticipant){
	        	  return $filter('filter')(self.tableParams.settings().dataset, params.filter());
		      }
	          if(!self.tableParams.shouldGetData){
	        	  var last = params.page() * params.count();
		          return _.slice(self.tableParams.settings().dataset,last - params.count(),last);
	          }
	          return  ParticipantsService.getParticipants().then(function(response) {
	        	  if(response.status == '200'){
	        		  var data = response.data;
	        		  console.log("Data get list from getParticipants ajax ...");        	 
	        		  self.originalData = angular.copy(data);
		        	  self.tableParams.settings().dataset = data;
		        	  self.tableParams.shouldGetData = false;
		        	  self.tableParams.total(self.originalData.length);
		        	  var last = params.page() * params.count();
			          return _.slice(data,last - params.count(),last);
	        	  }
	          });
	        },
	      });
	    self.tableParams.shouldGetData = true;
	    self.cancel = cancel;
	    self.del = del;
	    self.save = save;
	    self.add = add;
	    
	    self.toggleIsDeleting = toggleIsDeleting; 
	    function toggleIsDeleting(){
	    	if(self.isDeleting){
	    		self.isDeleting = false;
	    	}else{
	    		self.isDeleting = true;
	    	}
	    }
	    function add() {
	    	if(awesIdIsValid($scope.searchAWESID)){
	    		InterviewsService.checkReferenceNumberExists($scope.searchAWESID).then(function(data){
	    			if(data.status == 200){
	    				if(confirm("This AWES ID has already been used. Would you like to add a duplicate?")){
	    					$scope.addInterviewTabInterviewers(-1,$scope.searchAWESID);
	    				}
	    			}else if(data.status == 204){
	    				$scope.addInterviewTabInterviewers(-1,$scope.searchAWESID);
	    			}else{
	    				alert("Error occured during checkReferenceNumberExists.");
	    			}
	    		})
	    	}else{
	    		alert("You need to add a valid AWES ID before you can start.");
	    	}
	    }
	    function cancel(row,rowForm) {
	    	var originalRow = resetRow(row, rowForm);
	    	if(row.idNode){
	    		angular.extend(row, originalRow);
	    	}else{
	    		_.remove(self.tableParams.settings().dataset, function (item) {
		            return row === item;
		        });
	    		self.tableParams.shouldGetData = false;
		        self.tableParams.reload().then(function (data) {
		            if (data.length === 0 && self.tableParams.total() > 0) {
		                self.tableParams.page(self.tableParams.page() - 1);
		                self.tableParams.reload();
		            }
		        });
	    	} 
	    }
	    function del(row) {
	    	row.deleted = 1;
	    	var data =  ParticipantsService.deleteParticipant(row).then(function(response) {
	    		if(response.status === 200){
					console.log('Participant was deleted!');
					self.tableParams.shouldGetData = true;
			        self.tableParams.reload().then(function (data) {
			            if (data.length === 0 && self.tableParams.total() > 0) {
			                self.tableParams.page(self.tableParams.page() - 1);
			                self.tableParams.reload();
			            }
			        });
				}
	        });
	    	_.remove(self.tableParams.settings().dataset, function (item) {
	            return row === item;
	        });
	        self.tableParams.shouldGetData = false;
	        self.tableParams.reload().then(function (data) {
	            if (data.length === 0 && self.tableParams.total() > 0) {
	                self.tableParams.page(self.tableParams.page() - 1);
	                self.tableParams.reload();
	            }
	        });
	    }
	    function resetRow(row, rowForm) {
	        row.isEditing = false;
	        rowForm.$setPristine();
	        self.tableTracker.untrack(row);
	        return window._.find(self.originalData,{idNode:row.idNode});
	    }
	    function save(row, rowForm) {
	    	self.isEditing = false;
	    	ParticipantsService.save(row).then(function(response){
				if(response.status === 200){
					console.log('Participant Save was Successful!');
					self.tableParams.shouldGetData = true;
			        self.tableParams.reload().then(function (data) {
			            if (data.length === 0 && self.tableParams.total() > 0) {
			                self.tableParams.page(self.tableParams.page() - 1);
			                self.tableParams.reload();
			                $location.hash("");
			    		    $anchorScroll();
			            }
			        });
				}
			});
	    }
	    
	    function setInvalid(isInvalid) {
	        self.$invalid = isInvalid;
	        self.$valid = !isInvalid;
	      }
	    
	    function untrack(row) {
	        _.remove(invalidCellsByRow, function(item) {
	          return item.row === row;
	        });
	        _.remove(dirtyCellsByRow, function(item) {
	          return item.row === row;
	        });
	        setInvalid(invalidCellsByRow.length > 0);
	      }
	}
})();

