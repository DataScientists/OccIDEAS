(function() {
	angular.module('occIDEASApp.Interviews').controller('InterviewsCtrl',
			InterviewsCtrl).filter('displayFlag',function(){
				return function(translated,node,storage){
					if(translated){
					if(translated == node.name.toLowerCase()){
						return 'bfh-flag-GB';
					}else{
						return storage.chosenLang.flag;
					}
					}
				}
			});

	InterviewsCtrl.$inject = [ 'data', '$scope', '$mdDialog',
			'FragmentsService', '$q', 'QuestionsService', 'ModulesService',
			'InterviewsService', 'ParticipantsService', 'AssessmentsService',
			'$anchorScroll', '$location', '$mdMedia', '$window', '$state',
			'$rootScope', '$compile', '$timeout', '$log', 'updateData',
			'startWithReferenceNumber','$filter','$translate','NodeLanguageService',
			'$sessionStorage'];
	function InterviewsCtrl(data, $scope, $mdDialog, FragmentsService, $q,
			QuestionsService, ModulesService, InterviewsService,
			ParticipantsService, AssessmentsService, $anchorScroll, $location,
			$mdMedia, $window, $state, $rootScope, $compile, $timeout, $log,
			updateData,startWithReferenceNumber,$filter,$translate,NodeLanguageService,
			$sessionStorage) {
		var self = this;
		$scope.data = data;
		$scope.$root.tabsLoading = false;
		$scope.multiSelected = [];
		$scope.questionHistory = [];
		$scope.referenceNumber = null;
		$scope.storage = $sessionStorage;
		if($sessionStorage.langEnabled){
			$scope.flagUsed = 'flag-icon-'+$scope.storage.chosenLang.flag.split(/[- ]+/).pop().toLowerCase();
		}
		
		function add(type) {
	    	$scope.addInterviewTabInterviewers();
	    }
		var safeDigest = function(obj) {
			if (!obj.$$phase) {
				try {
					obj.$digest();
				} catch (e) {
				}
			}
		}
		$scope.awesId = {};
		$scope.startInterview = function(data) {
			createParticipant(data);
		}
		$scope.resetInterview = function() {
			$scope.inProgress = true;
			var idInterview = $scope.interview.interviewId;
			InterviewsService.get(idInterview).then(function(response) {
				if (response.status === 200) {
					var interview = response.data[0];  
					$scope.interview = interview;
					$scope.displayHistoryNew = undefined;
					resumeInterview();
					refreshDisplayNew();
					showNextQuestionNew();	
					$scope.inProgress = false;
				} else if (response.status === 401) {
					$log.error("Inside updateData of tabs.interviewresume tabs.js could not find interview with "+idInterview);
					alert("Could not reset please close tab and resume from the participants list");
					return;
				}
			});
		}
		$scope.getWeeksArray = function() {
			var weeks = [];
			for (var i = 0; i < 53; i++) {
				weeks.push(i);
			}
			return weeks;
		};
		$scope.getShiftHoursArray = function() {
			var hours = [];
			for (var i = 0; i < 25; i++) {
				hours.push(i);
			}
			return hours;
		};
		$scope.getSecondsArray = function() {
			var seconds = [ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 15, 20, 25, 30,
					35, 40, 45, 50, 55, 60 ];

			return seconds;
		};
		$scope.getHoursPerWeekArray = function() {
			var hours = [];
			for (var i = 0; i < 169; i++) {
				hours.push(i);
			}
			return hours;
		};
		$scope.getShiftMinutesArray = function() {
			var minutes = [ 0, 1, 2, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55 ];

			return minutes;
		};
		$rootScope.saveParticipant = function (participant) {
			ParticipantsService.save(participant).then(function(response) {
				if (response.status === 200) {
					$log.info("Saving Participant "
							+ participant.idParticipant
							+ " was successful");
				}
			});
		}
		if(startWithReferenceNumber){
			$scope.referenceNumber = startWithReferenceNumber;
			$scope.startInterview(data);
		} else if (updateData) {
			$log.info("updateData is not null... interview continuation initializing...");
			$scope.interview = updateData;
			$rootScope.participant = updateData.participant;
			resumeInterview();	
		}
			
		function resumeInterview(){
			var participant = $rootScope.participant;
			if(participant.status == 1){//partial interview
				participant.status = 0;//running
				$rootScope.saveParticipant(participant);
			}
			refreshDisplayNew();
			var question = findNextQuestionQueued($scope.interview);
			if(question){										
				QuestionsService.findQuestionSingleChildLevel(question.questionId).then(function(response){
					if(response.status === 200){
						$scope.interviewStarted = true;
						var ques = response.data[0];
						$scope.interview.showedQuestion = ques;
						
						safeDigest($scope.interview.showedQuestion);
						if (ques.type == 'Q_frequency') { //if frequency set up frequency lists
							$scope.hoursPerWeekArray = $scope.getHoursPerWeekArray();
							$scope.hoursArray = $scope.getShiftHoursArray();
							$scope.minutesArray = $scope.getShiftMinutesArray();
							$scope.weeks = $scope.getWeeksArray();
							$scope.seconds = $scope.getSecondsArray();
						}
						var nodeLink = _.find($scope.interview.questionHistory,function(node){
							return node.link==ques.topNodeId;
						});
						$scope.questionheader.name = nodeLink.name.substring(0, 4);
						$scope.inProgress = false;
						safeDigest($scope.inProgress);
						$scope.scrollTo($scope.interview.showedQuestion.idNode);
					}else{
						var errorMsg = "Error getting question "+question.questionId;
						console.error(errorMsg);
						alert(errorMsg);
					}
				});	
			}else{
				if(participant.status == 2){//completed interview
					endInterview();
				}else{
					var msg = "Could not queue first question";
					console.error(msg);
					alert(msg);
					endInterview();
				}
				
			}	
		}
		function populateInteviewQuestionJsonByQuestion(interview, question) {
			var parentModuleId = undefined;
			var parentAnswerId = undefined;
			
			if(isNaN(question.number)){
				parentAnswerId = question.parentId;
			}else{
				parentModuleId = question.parentId;
			}
			
			return {
				idInterview : interview.interviewId,
				topNodeId : question.topNodeId,
				questionId : question.idNode,
				parentModuleId : parentModuleId,
				parentAnswerId : parentAnswerId,
				name : question.name,
				description : question.description,
				nodeClass : question.nodeclass,
				number : question.number,
				modCount : question.modCount,
				type : question.type,
				link : question.link,
				intQuestionSequence : intQuestionSeq++,
				deleted : 0,
				answers : []
			};
		}
		var intQuestionSeq = 1;
		function populateInteviewQuestionJsonByModule(interview, module) {
			return {
				idInterview : interview.interviewId,
				topNodeId : module.idNode,
				questionId : undefined,
				parentModuleId : module.idNode,
				parentAnswerId : undefined,
				name : module.name,
				description : module.description,
				nodeClass : module.nodeclass,
				number : module.number,
				type : module.type,
				link : module.idNode,
				modCount : 1,
				intQuestionSequence : intQuestionSeq++,
				deleted : 0,
				answers : []
			};
		}
		function populateInteviewQuestionJsonByLinkedQuestion(interview, linkedQuestion) {
			var modCount = 1;
			if(interview.questionHistory){
				_.each(interview.questionHistory,function(question){
					if(question.link){
						modCount = modCount ;
					}
				})
			}
			return {
				id:linkedQuestion.id,
				idInterview : interview.interviewId,
				topNodeId : linkedQuestion.link,
				questionId : undefined,
				parentModuleId : linkedQuestion.parentModuleId,
				parentAnswerId : linkedQuestion.parentAnswerId,
				name : linkedQuestion.name,
				description : linkedQuestion.description,
				nodeClass : linkedQuestion.nodeclass,
				number : '1',
				type : linkedQuestion.type,
				link : linkedQuestion.link,
				intQuestionSequence : intQuestionSeq++,
				modCount : modCount,
				deleted : 0,
				answers : []
			};
		}

		function populateInterviewAnswerJsonByAnswer(interview, answer) {
			return {
				idInterview : interview.interviewId,
				topNodeId : answer.topNodeId,
				parentQuestionId : answer.parentId,
				answerId : answer.idNode,
				name : answer.name,
				modCount : 1,
				answerFreetext : answer.name,
				nodeClass : answer.nodeclass,
				number : answer.number,
				type : answer.type,
				deleted : answer.deleted,
				isProcessed : false
			};
		}
		
		//Simple Question
		function processInterviewQuestionNew(interview, node) {			
			buildAndSaveQuestionNew(interview, node);	
			
		}
		//Multi Question
		function processInterviewQuestionWithMultipleAnswersNew(interview, node) {
			buildAndSaveMultipleQuestionNew(interview, node);
			
		}
		//Frequency Question
		function processFrequencyNew(interview, node) {
			buildAndSaveFrequencyNew(interview, node);
			
		}
		
		//Edit Simple Question
		function processEditInterviewQuestionNew(interview, node) {
			buildAndEditQuestionNew(interview, node);
			$mdDialog.cancel();
						
		}
		//Edit Multi Question
		function processInterviewQuestionWithMultipleAnswersEdit(interview, node) {
			buildAndEditMultipleQuestion(interview, node);
			$mdDialog.cancel();
			
		}
		//Edit Frequency Question
		function processFrequencyNewEdit(interview, node) {
			buildAndEditFrequency(interview, node);
			$mdDialog.cancel();
			
		}
		
		
		
		function buildAndEditFrequency(interview, node) {
			var hours = 0;
			if ($scope.questionBeingEdited.hours) {
				hours = $scope.questionBeingEdited.hours;
			}
			var minutes = 0;
			if ($scope.questionBeingEdited.minutes) {
				minutes = $scope.questionBeingEdited.minutes;
			}
			var answerValue = node.nodes[0].name;
			if(hours != 0 || minutes != 0){
				answerValue = Number(hours) + (Number(minutes) / 60);
			}
			var answer = node.selectedAnswer?node.selectedAnswer:node.nodes[0];
			var historyQuestion = _.find($scope.interview.questionHistory,function(ques){
				return ques.questionId == node.idNode && !ques.deleted;
			});
			InterviewsService.getInterviewQuestion(historyQuestion.id).then(function(response){
				if(response.status === 200){
					var latestQ = response.data;
					var newAnswer = _.find(latestQ.answers,function(answ){
						return answ.answerId == answer.idNode;
					});		
					if(!newAnswer){
						var msg = "OOPS! lost the actual question in buildAndEditFrequency, please take a screenshot showing AWES ID and log this issue.";
						console.error(msg);
						alert(msg);	
					}						
					newAnswer.answerFreetext = answerValue;			
					newAnswer.name = answerValue;						
					
					var questionBeingEdited = _.find(interview.questionHistory,function(queuedQuestion){
						return queuedQuestion.questionId == node.idNode 
						&& queuedQuestion.processed 
						&& !queuedQuestion.deleted;
					});
					questionBeingEdited.answers = [];
					questionBeingEdited.answers.push(newAnswer);	
					saveSingleAnswer(newAnswer,questionBeingEdited);
				}
			});
			
			
			//saveInterviewDisplay(questionBeingEdited);
			
		}
		function buildAndSaveFrequencyNew(interview, node) {
			var hours = 0;
			if ($scope.interview.showedQuestion.hours) {
				hours = $scope.interview.showedQuestion.hours;
			}
			var minutes = 0;
			if ($scope.interview.showedQuestion.minutes) {
				minutes = $scope.interview.showedQuestion.minutes;
			}
			var answerValue = node.nodes[0].name;
			if(hours != 0 || minutes != 0){
				answerValue = Number(hours) + (Number(minutes) / 60);
			}
			var answer = node.selectedAnswer?node.selectedAnswer:node.nodes[0];
			var actualAnswer = populateInterviewAnswerJsonByAnswer(interview, answer);
			var newQuestionAsked = _.find(interview.questionHistory,function(queuedQuestion){
				return queuedQuestion.questionId == node.idNode 
				&& !queuedQuestion.processed 
				&& !queuedQuestion.deleted;
			});
			actualAnswer.answerFreetext = answerValue;
			actualAnswer.interviewQuestionId = newQuestionAsked.id;
			
			newQuestionAsked.answers.push(actualAnswer);	
			var defer = $q.defer();
			newQuestionAsked.answers[0].isProcessed = true;
			saveAnswerNew(newQuestionAsked,defer);
			defer.promise.then(function(){
				newQuestionAsked.processed = true;
				InterviewsService.saveQuestion(newQuestionAsked).then(function(response) {
					if (response.status === 200) {
						//saveInterviewDisplay(newQuestionAsked);
						refreshDisplayNew(newQuestionAsked);
						var answer = newQuestionAsked.answers[0];
						var lookupQuestion = {
							parentId : newQuestionAsked.questionId,
							number : answer.number
						}
						var parentQuestion = newQuestionAsked;
						showNextQuestionNew(lookupQuestion, parentQuestion);
					}
				});
			});		
		}
		function buildAndSaveMultipleQuestionNew(interview, question, persistedAnswer) {
			var newQuestionAsked = _.find(interview.questionHistory,function(queuedQuestion){
				return queuedQuestion.questionId == question.idNode 
				&& !queuedQuestion.processed 
				&& !queuedQuestion.deleted;
			});
			$scope.displayQuestions = [];
			for(var i=0;i<interview.questionHistory.length;i++){
				var queuedQuestion = interview.questionHistory[i];
				if(queuedQuestion.deleted==0){
					if(queuedQuestion.description == 'display'){
						$scope.displayQuestions.push(queuedQuestion);
					}
				}
			}
			_.each(question.nodes, function(answer) {
				if(answer.isSelected){
					var actualAnswer = populateInterviewAnswerJsonByAnswer(interview, answer);
					actualAnswer.interviewQuestionId = newQuestionAsked.id;
					
					newQuestionAsked.answers.unshift(actualAnswer);
				}			
			});	
			var defer = $q.defer();
			//newQuestionAsked.answers[0].isProcessed = true;
			saveAnswerNew(newQuestionAsked,defer);
			defer.promise.then(function(){
				newQuestionAsked.processed = true;
				InterviewsService.saveQuestion(newQuestionAsked).then(function(response) {
					if (response.status === 200) {
						//saveInterviewDisplay(newQuestionAsked);
						refreshDisplayNew(newQuestionAsked);
						var answer = newQuestionAsked.answers[0];
						var lookupQuestion = {
							parentId : newQuestionAsked.questionId,
							number : answer.number
						}
						var parentQuestion = newQuestionAsked;
						showNextQuestionNew(lookupQuestion, parentQuestion);
					}
				});
			});
		}
		var questionsToDelete = [];
		var answersToDelete = [];
		function populateQuestionsAndAnswersToDelete(question){
			question.deleted = 1;
			questionsToDelete.push(question);
			var answers = question.answers;
			if(answers.length > 0){
				for(var i=0;i<answers.length;i++){
					var answer = answers[i];
					answer.deleted = 1;
					answersToDelete.push(answer);
					for(var j=0;j<$scope.interview.questionHistory.length;j++){
						var iQuestion = $scope.interview.questionHistory[j];
						if(iQuestion.parentAnswerId==answer.answerId){
							populateQuestionsAndAnswersToDelete(iQuestion);
						}
						
					}
				}
			}else if(question.link){
				for(var j=0;j<$scope.interview.questionHistory.length;j++){
					var iQuestion = $scope.interview.questionHistory[j];
					if(iQuestion.parentModuleId==question.link){
						populateQuestionsAndAnswersToDelete(iQuestion);
					}
					
				}
			}	
		}
		function updateFreeTextAnswer(answer){
			var answers = [];
			answers.push(answer);
			InterviewsService.saveAnswers(answers).then(function(response){
				if (response.status === 200) {
					console.log("Updated free text answer");
					$scope.resetInterview();
				}else{
					var msg = "Failed to updateFreeTextAnswer";
					console.error(msg);
					alert(msg)
				}
			});
		}
		function deleteAnswers(answers,defer){
			if(answers.length > 0){
				var uniqueAnswers = _.uniqBy(answers, 'id');
				InterviewsService.saveAnswers(uniqueAnswers).then(function(response){
					if (response.status === 200) {
						if(defer){
							defer.resolve();
						}
					}else{
						if(defer){
							defer.reject();
						}
					}
				});
			}else{
				if(defer){
					defer.resolve();
				}
			}
			if(defer){
				return defer.promise;
			}
		}
		function deleteQuestions(questions,defer){
			if(questions.length > 0){
				var uniqueQuestions = _.uniqBy(questions, 'id');
				InterviewsService.saveQuestions(uniqueQuestions).then(function(response){
					if (response.status === 200) {
						if(defer){
							defer.resolve();
						}
					}else{
						if(defer){
							defer.reject();
						}
					}
				});
			}else{
				if(defer){
					defer.resolve();
				}
			}
			if(defer){
				return defer.promise;
			}
		}
		
		function findChildQuestionsToDelete(answer){	
			// get the id of the answer to be deleted
			var ansFound = _.find(listOfAnswersGiven,function(ans){
				return answer.answerId == ans.answerId;
			});
			if(!ansFound){
				ansFound = _.find($scope.interview.answerHistory,function(ans){
					return ans.answerId == answer.answerId;
				});
			}
			if(ansFound){
				answer.id = ansFound.id;
			}
			// set answer as deleted and push to array for batch delete
			answer.deleted = 1;
			answersToDelete.push(answer);
			for(var j=0;j<$scope.interview.questionHistory.length;j++){
				var iQuestion = $scope.interview.questionHistory[j];
				if(iQuestion.parentAnswerId==answer.answerId){					
					populateQuestionsAndAnswersToDelete(iQuestion);
				}			
			}			
		}
		function buildAndEditMultipleQuestion(interview, question) {

			var newQuestionAsked = _.find(interview.questionHistory,function(queuedQuestion){
				return queuedQuestion.questionId == question.idNode 
				&& queuedQuestion.processed 
				&& !queuedQuestion.deleted;
			});
			if(!newQuestionAsked){
				var msg = "OOPS! lost the newQuestion in buildAndEditQuestionNew";
				console.error(msg);
				alert(msg);				
			}
			var bDeleteAnswersRequired = false;
			var bSaveAnswersRequired = false;
			var bIsFreeText = false;
			var newSetOfAnswers = [];
			for(var i=0;i<question.nodes.length;i++){
				var possibleAnswer = question.nodes[i];
				if(possibleAnswer.isSelected){
					var bExists = false;
					newSetOfAnswers.push(possibleAnswer);
					for(var j=0;j<newQuestionAsked.answers.length;j++){
						var newAnswer = newQuestionAsked.answers[j];
						if(possibleAnswer.idNode==newAnswer.answerId){
							if(newAnswer.deleted==0){
								bExists = true;
								if(possibleAnswer.type=='P_freetext'){
									bIsFreeText = true;
									InterviewsService.getInterviewQuestion(newQuestionAsked.id).then(function(response){
										if(response.status === 200){
											ques = response.data;
											var newAnswer = _.find(ques.answers,function(answ){
												return answ.answerId == possibleAnswer.idNode && answ.deleted==0;
											});	
											if(newAnswer){
												newAnswer.answerFreetext = possibleAnswer.name;
												newAnswer.name = possibleAnswer.name;
												updateFreeTextAnswer(newAnswer);	
											}else{
												var msg = "Could not find free text answer to update";
												console.error(msg);
												alert(msg);
											}	
										}
									});	
								}
							}													
							break;
						}
					}
					if(!bExists){
						var actualAnswer = populateInterviewAnswerJsonByAnswer(interview, possibleAnswer);
						actualAnswer.interviewQuestionId = newQuestionAsked.id;
						newQuestionAsked.answers.push(actualAnswer);
						bSaveAnswersRequired = true;
					}
				}	
			}
			var oldAnswers = [];
			for(var m=0;m<$scope.questionBeingEditedCopy.nodes.length;m++){
				var oldAnswer = $scope.questionBeingEditedCopy.nodes[m];
				if(oldAnswer.isSelected){
					oldAnswers.push(oldAnswer);
				}
			}			
			for(var k=0;k<oldAnswers.length;k++){
				var oldAnswer = oldAnswers[k];
				var bFound = false;
				for(var l=0;l<newSetOfAnswers.length;l++){
					var newAnswer = newSetOfAnswers[l];
					if(oldAnswer.idNode==newAnswer.idNode){
						bFound = true;
						break;
					}	
				}
				if(oldAnswer.type=='P_freetext'){ //if it is a free text then lets remove it, no need to save again
					_.remove(newQuestionAsked.answers,function(ans){
						return ans.answerId == oldAnswer.idNode;
					});
				}
				if(!bFound){//this old answer is missing
					var oldAnswerlisted = _.find($scope.interview.answerHistory,function(ans){
						return ans.answerId == oldAnswer.idNode;
					});
					if(!oldAnswerlisted){
						oldAnswerlisted = _.find(listOfAnswersGiven,function(ans){
							return ans.answerId == oldAnswer.idNode;
						});
					}
					if(!oldAnswerlisted){
						var msg = "Could not find answer to remove";
						console.error(msg);
						alert(msg);
					}
					findChildQuestionsToDelete(oldAnswerlisted);
					bDeleteAnswersRequired = true;
					_.remove(newQuestionAsked.answers,function(ans){
						return ans.answerId == oldAnswer.idNode;
					});
				}else{
					//same answer as before so lets remove it, no need to save again
					_.remove(newQuestionAsked.answers,function(ans){
						return ans.answerId == oldAnswer.idNode;
					});					
				}			
			}
			if(bDeleteAnswersRequired || bSaveAnswersRequired){
				$scope.displayQuestions = [];
				for(var i=0;i<interview.questionHistory.length;i++){
					var queuedQuestion = interview.questionHistory[i];
					if(queuedQuestion.deleted==0){
						if(queuedQuestion.description == 'display'){
							$scope.displayQuestions.push(queuedQuestion);
						}
					}
				}
				var defer = $q.defer();
				saveAnswerNew(newQuestionAsked,defer);
				defer.promise.then(function(){
					newQuestionAsked.processed = true;
					InterviewsService.saveQuestion(newQuestionAsked).then(function(response) {
						if (response.status === 200) {
							console.log("Saved Interview q:"+newQuestionAsked.questionId);
							//saveInterviewDisplay(newQuestionAsked);
							
							var defer1 = $q.defer();
							deleteAnswers(answersToDelete,defer1);
							defer1.promise.then(function(){
								console.log("All done deleting child questions answers");
								var defer = $q.defer();
								deleteQuestions(questionsToDelete,defer);
								defer.promise.then(function(){
									console.log("All done deleting child questions");	
									/*newQuestionAsked.answers = [];
									for(var i=0;i<newSetOfAnswers.length;i++){
										var pa = newSetOfAnswers[i];
										var newAnsw = populateInterviewAnswerJsonByAnswer(interview, pa);
										newQuestionAsked.answers.push(newAnsw);
										refreshDisplayNew(newQuestionAsked);
									}*/
									$scope.resetInterview();							
								});
							});	
						}
					});
				});																	
			}else{
				if(!bIsFreeText){
					alert("Nothing was changed");
				}	
			}		
		}
		function buildAndEditQuestionNew(interview, question) {
			var newQuestionAsked = _.find(interview.questionHistory,function(queuedQuestion){
				return queuedQuestion.questionId == question.idNode 
				&& queuedQuestion.processed 
				&& !queuedQuestion.deleted;
			});
			if(!newQuestionAsked){
				var msg = "OOPS! lost the newQuestion in buildAndEditQuestionNew";
				console.error(msg);
				alert(msg);				
			}
			var bIsFreeText = false;
			var bDeleteAnswersRequired = false;
			var selectedAnswer = question.selectedAnswer;
			_.each(newQuestionAsked.answers,function(ans){
				if(ans.deleted==0){
					if(selectedAnswer.idNode!=ans.answerId){
						findChildQuestionsToDelete(ans);
						bDeleteAnswersRequired = true;
					}else{
						if(selectedAnswer.type=='P_freetext'){
							bIsFreeText = true;
							InterviewsService.getInterviewQuestion(newQuestionAsked.id).then(function(response){
								if(response.status === 200){
									ques = response.data;
									var newAnswer = _.find(ques.answers,function(answ){
										return answ.answerId == selectedAnswer.idNode && answ.deleted==0;
									});	
									if(newAnswer){
										newAnswer.answerFreetext = selectedAnswer.name;
										newAnswer.name = selectedAnswer.name;
										updateFreeTextAnswer(newAnswer);	
									}else{
										var msg = "Could not find free text answer to update";
										console.error(msg);
										alert(msg);
									}	
								}
							});					
						}
					}
				}	
			});
			if(bDeleteAnswersRequired){
				$scope.displayQuestions = [];
				for(var i=0;i<interview.questionHistory.length;i++){
					var queuedQuestion = interview.questionHistory[i];
					if(queuedQuestion.deleted==0){
						if(queuedQuestion.description == 'display'){
							$scope.displayQuestions.push(queuedQuestion);
						}
					}
				}
				var actualAnswer = populateInterviewAnswerJsonByAnswer(interview, selectedAnswer);	
				actualAnswer.interviewQuestionId = newQuestionAsked.id;
				actualAnswer.isProcessed = true;
				newQuestionAsked.answers[0] = actualAnswer;	
				var defer = $q.defer();
				saveAnswerNew(newQuestionAsked,defer);
				defer.promise.then(function(){
					newQuestionAsked.processed = true;
					InterviewsService.saveQuestion(newQuestionAsked).then(function(response) {
						if (response.status === 200) {
							console.log("Saved Interview q:"+newQuestionAsked.questionId);
							var defer1 = $q.defer();
							deleteAnswers(answersToDelete,defer1);
							defer1.promise.then(function(){
								console.log("All done deleting child questions answers");
								var defer = $q.defer();
								deleteQuestions(questionsToDelete,defer);
								defer.promise.then(function(){
									console.log("All done deleting child questions");						
									$scope.resetInterview();
								});
							});	
						}
					});
				});						
											
			}else{
				if(!bIsFreeText){
					alert("Nothing was changed");
				}				
			}		
		}
		var listQuestionsAsked = [];
		function buildAndSaveQuestionNew(interview, question) {

			var newQuestionAsked = _.find(interview.questionHistory,function(queuedQuestion){
				return queuedQuestion.questionId == question.idNode 
				&& !queuedQuestion.processed 
				&& !queuedQuestion.deleted;
			});
			if(!newQuestionAsked){
				var msg = "OOPS! lost the newQuestion in buildAndSaveQuestionNew";
				console.error(msg);
				alert(msg);				
			}
			$scope.displayQuestions = [];
			for(var i=0;i<interview.questionHistory.length;i++){
				var queuedQuestion = interview.questionHistory[i];
				if(queuedQuestion.deleted==0){
					if(queuedQuestion.description == 'display'){
						$scope.displayQuestions.push(queuedQuestion);
					}
				}
			}
			var actualAnswer = populateInterviewAnswerJsonByAnswer(interview, question.selectedAnswer);
			actualAnswer.interviewQuestionId = newQuestionAsked.id;
			newQuestionAsked.answers.push(actualAnswer);	
			var defer = $q.defer();
			newQuestionAsked.answers[0].isProcessed = true;
			saveAnswerNew(newQuestionAsked,defer);
			defer.promise.then(function(){
				newQuestionAsked.processed = true;
				InterviewsService.saveQuestion(newQuestionAsked).then(function(response) {
					if (response.status === 200) {
						refreshDisplayNew(newQuestionAsked);
						listQuestionsAsked.push(response.data);
						var answer = newQuestionAsked.answers[0];
						var lookupQuestion = {
							parentId : newQuestionAsked.questionId,
							number : answer.number
						}
						var parentQuestion = newQuestionAsked;
						showNextQuestionNew(lookupQuestion, parentQuestion);
					}
				});
			});
		}
		var listOfAnswersGiven = [];
		function saveAnswerNew(newQuestionAsked,defer){
			if(newQuestionAsked.answers.length > 0){
				InterviewsService.saveAnswersAndQueueQuestions(newQuestionAsked.answers).then(function(response){
					if (response.status === 200) {
						_.each(response.data,function(data){
							var index = _.indexOf(listOfAnswersGiven,
									_.find(listOfAnswersGiven, {id: data.id}));
							if(index != -1){
								listOfAnswersGiven.splice(index, 1, data);
							}else{
								listOfAnswersGiven.push(data);
							}
						});
						if(defer){
							defer.resolve();
						}
					}else{
						if(defer){
							defer.reject();
						}
					}
				});
			}else{
				console.error("Trying to save empty interview answers");
				if(defer){
					defer.resolve();
				}
			}
			if(defer){
				return defer.promise;
			}
		}
		function saveSingleAnswer(answer,questionBeingEdited){	
			var container = [];
			container.push(answer);
			InterviewsService.saveAnswers(container).then(function(response){
				if (response.status === 200) {		
					console.log("Success on saveSingleAnswer");
				}else{
					console.error("Could not save "+answer.idNode);
				}
				refreshDisplayNew(questionBeingEdited);
			});
			
		}
		
		function validateIfAnswerEdited(node) {
			if (node.type == 'Q_frequency') {
				var retValue = false;
				for (var i = 0; i < node.nodes.length; i++) {
					var val = node.nodes[i];
					if (val.type == 'P_frequencyweeks') {
						if (isNaN(val.name)) {
							retValue = false;
						} else {
							retValue = true;
						}
					} else if (val.type == 'P_frequencyseconds') {
						if (isNaN(val.name)) {
							retValue = false;
						} else {
							retValue = true;
						}
					} else if (val.type == 'P_frequencyhours') {
						if (isNaN(val.name)) {
							retValue = false;
						} else {
							retValue = true;
						}
					} else if (val.type == 'P_frequencyshifthours') {
						var retValue = false;
						if ($scope.questionBeingEdited.hours) {
							retValue = true;
						}
						if ($scope.questionBeingEdited.minutes) {
							retValue = true;
						}
					} else if (val.type == 'P_frequencyhoursminute') {
						var retValue = false;
						if ($scope.questionBeingEdited.hours) {
							retValue = true;
						}
						if ($scope.questionBeingEdited.minutes) {
							retValue = true;
						}
					}
				}
				return retValue;
			} else if (node.type == 'Q_single' || node.type == 'Q_simple') {
				return $scope.questionBeingEdited.selectedAnswer;
			} else if (node.type == 'Q_multiple') {
				var retValue = false;
				_.find($scope.questionBeingEdited.nodes, function(val, ind) {
					if (val.isSelected) {
						retValue = true;
					}
				});
				return retValue;
			}
		}
		function validateIfAnswerSelected(node) {
			if (node.type == 'Q_frequency') {
				var retValue = false;
				for (var i = 0; i < node.nodes.length; i++) {
					var val = node.nodes[i];
					if (val.type == 'P_frequencyweeks') {
						if (isNaN(val.name)) {
							retValue = false;
						} else {
							retValue = true;
						}
					} else if (val.type == 'P_frequencyseconds') {
						if (isNaN(val.name)) {
							retValue = false;
						} else {
							retValue = true;
						}
					} else if (val.type == 'P_frequencyhours') {
						if (isNaN(val.name)) {
							retValue = false;
						} else {
							retValue = true;
						}
					} else if (val.type == 'P_frequencyshifthours') {
						var retValue = false;
						if ($scope.interview.showedQuestion.hours) {
							retValue = true;
						}
						if ($scope.interview.showedQuestion.minutes) {
							retValue = true;
						}
					} else if (val.type == 'P_frequencyhoursminute') {
						var retValue = false;
						if ($scope.interview.showedQuestion.hours) {
							retValue = true;
						}
						if ($scope.interview.showedQuestion.minutes) {
							retValue = true;
						}
					}
				}
				return retValue;
			} else if (node.type == 'Q_single' || node.type == 'Q_simple') {
				return $scope.interview.showedQuestion.selectedAnswer;
			} else if (node.type == 'Q_multiple') {
				var retValue = false;
				_.find($scope.interview.showedQuestion.nodes, function(val, ind) {
					if (val.isSelected) {
						retValue = true;
					}
				});
				return retValue;
			}
		}
		$scope.saveAnswerQuestionNew = function(node) {
			if (!(validateIfAnswerSelected(node))) {
				alert("Please select an answer.");
				return;
			}
			//commented out for debugging
			$scope.inProgress = true;		
			var interview = $scope.interview;
			if (interview) {
				if (node.type == 'Q_multiple') {
					processInterviewQuestionWithMultipleAnswersNew(interview, node);
				} else if (node.type == 'Q_frequency') {
					processFrequencyNew(interview, node);
				} else {
					processInterviewQuestionNew(interview, node);
				}
			}else{
				alert("OOPS! Lost the Scope Interview, Please take a screen shot and submit a issue ticket");
			}
				
		}

		$scope.stopInterview = function(node) {
			ParticipantsService.findInterviewParticipant($rootScope.participant.idParticipant).then(
			  function(response) {
				if (response.status === 200) {
					$rootScope.participant = response.data[0];
					var interview = $rootScope.participant.interviews[0];
					if (validateIfAnswerSelected(node)) {
						if (!interview) {
							return null;
						}
						if (node.type == 'Q_multiple') {
							processInterviewQuestionWithMultipleAnswersNew(interview, node);
						} else if (node.type == 'Q_frequency') {
							processFrequencyNew(interview, node);
						} else {
							processInterviewQuestionNew(interview, node);
						}
					}
					if (confirm('You have pressed the Stop Button.  Do you realy want to stop the interview?')) {
						addInterviewNote('Interview Stopped','System');
						var participant = $rootScope.participant;
						participant.status = 1;//partial
						$rootScope.saveParticipant(participant);
						endInterview();
					}
				}
			});
		}
		function addInterviewNote(note,type){
			var interview = $scope.interview;
			if (!(interview.notes)) {
				interview.notes = [];
			}
			interview.notes.push({
						interviewId : interview.interviewId,
						text : note,
						type : type
					});
			saveInterview(interview);
		}
		function saveInterview(interview) {
			var currentQuestion = interview.showedQuestion;
			InterviewsService.save(interview).then(function(response) {
				if (response.status === 200) {
					$log.info("Saving interview with id:"+ interview.interviewId + " successful");
					InterviewsService.get($scope.interview.interviewId).then(function(response) {
						if(response.status==200){
							$scope.interview = response.data[0];
							$scope.interview.showedQuestion = currentQuestion;
						}
					});
				}
			});
		}

		function translateNode(node){
			return $translate.instant(node.name);
		}
		
		function createParticipant(data) {
			var participant = {
				reference : $scope.referenceNumber,
				interviews : []
			}
			ParticipantsService.createParticipant(participant).then(function(response) {
				if (response.status === 200) {
					participant = response.data;
					$rootScope.participant = participant;
					var interview = {};
					interview.participant = participant;
					interview.module = $scope.data[0];
					interview.referenceNumber = $scope.referenceNumber;
					InterviewsService.startInterview(interview).then(function(response) {
						if (response.status === 200) {
							interview.interviewId = response.data.interviewId;
							//refreshInterviewDisplay(interview.interviewId);
							var copyParticipant = angular.copy($scope.participant);
							interview.participant = copyParticipant;
							$scope.interview = {};
							$scope.interview = interview;
							$scope.interviewStarted = true;
							$scope.interviewId = response.data.interviewId;
							
							var firstLinkIntervewQuestion = populateInteviewQuestionJsonByModule(interview,interview.module);
							firstLinkIntervewQuestion.number = "0";
							//inserting the first link intro module
							InterviewsService.saveLinkQuestionAndQueueQuestions(firstLinkIntervewQuestion).then(function(response) {
								if (response.status === 200) {
									InterviewsService.get($scope.interview.interviewId).then(function(response) {
										if(response.status==200){
											/*var unprocessedQuestions = response.data[0].questionQueueUnprocessed;
											for(var i=0;i<unprocessedQuestions.length;i++){
												var unprocessedQuestion = unprocessedQuestions[i];
												//if unprocessed not in interview.questionHistory add
												var bFound = false;
												for(var j=0;j<$scope.interview.questionHistory.length;j++){
													var question = $scope.interview.questionHistory[j];
													if(question.questionId==unprocessedQuestion.questionId){
														bFound = true;
													}		
												}
												if(!bFound){
													$scope.interview.questionHistory.push(unprocessedQuestion);
												}
											}*/
											$scope.interview = response.data[0];
											refreshDisplay();
											var question = findNextQuestionQueued($scope.interview);
											if(question){												
												QuestionsService.findQuestionSingleChildLevel(question.questionId).then(function(response){
													if(response.status === 200){
														var ques = response.data[0];
														$scope.interview.showedQuestion = ques;
														
														safeDigest($scope.interview.showedQuestion);
														if (ques.type == 'Q_frequency') { //if frequency set up frequency lists
															$scope.hoursPerWeekArray = $scope.getHoursPerWeekArray();
															$scope.hoursArray = $scope.getShiftHoursArray();
															$scope.minutesArray = $scope.getShiftMinutesArray();
															$scope.weeks = $scope.getWeeksArray();
															$scope.seconds = $scope.getSecondsArray();
														}
														var nodeLink = _.find($scope.interview.questionHistory,function(node){
															return node.link==ques.topNodeId;
														});
														$scope.questionheader.name = nodeLink.name.substring(0, 4);
														$scope.inProgress = false;
														safeDigest($scope.inProgress);
														$scope.scrollTo($scope.interview.showedQuestion.idNode);
													}else{
														var errorMsg = "Error getting question "+question.questionId;
														console.error(errorMsg);
														alert(errorMsg);
													}
												});	
											}else{
												console.error("Could not queue first question");
												alert("Could not queue first question");
											}
										}
									});
								}									
							});
						}
					});
				}
			});
		}
		$scope.scrollTo = function(target) {
			var scrollPane = $('#interview-question-list');

			var scrollTarget = $('#questionlist-' + target);
			if (scrollTarget) {
				if (scrollTarget.offset()) {
					var currentScroll = 0;
					if (scrollPane.scrollTop()) {
						currentScroll = scrollPane.scrollTop();
					}
					var offset = 150;
					var top = scrollTarget.offset().top;
					// alert(top);
					var currentScroll = scrollPane.scrollTop();
					// alert(currentScroll);
					var scrollY = top - offset + currentScroll;
					scrollPane.animate({
						scrollTop : scrollY
					}, 1000, 'swing');
				}
			}
		};

		$scope.multiToggle = function(item, list) {
			var idx = list.indexOf(item);
			if (idx > -1)
				list.splice(idx, 1);
			else
				list.push(item);
		};
		$scope.questionheader = {};
		function findNextQuestionQueued(interview){
			var questionsList = angular.copy(interview.questionHistory);
			questionsList = _.reverse(questionsList);
			var question = _.find(questionsList,function(queuedQuestion){
				return !queuedQuestion.processed && !queuedQuestion.deleted;
			})
			return question;
		}
		function refreshInterviewDisplayNew(question){
				
		} 
		function refreshInterviewDisplay(interviewId){
			var shouldUpdateInterviewDisplay = false;
			var displayAnswersExist = undefined;
			_.each($scope.answeredQuestion, function(node) {
				  var questionExist = _.find($scope.interview.questionHistory,function(qnode){
					  return node.questionId == qnode.questionId;
				  });
				  //check if question no longer exist in $scope.interview.questionHistory
				  if(!questionExist || (questionExist.deleted == 1 && node.deleted == 0)){
					  node.deleted = 1;
					  var display = _.find(listOfInterviewDisplay,function(display){
							return display.questionId == questionExist.questionId
							&& display.deleted == 0;
					  });
					  if(!display){
						  display = _.find(listOfInterviewDisplay,function(display){
								return display.questionId == questionExist.questionId;
						  });
					  }
					  var id = display?display.id:null;
					 if(id){
							display.deleted = 1;
							_.each(display.answers,function(ans){
								ans.deleted = 1;
							});
							displayAnswersExist = display;
					 }
					 
					 shouldUpdateInterviewDisplay = true;
				  } 
			});
			if(shouldUpdateInterviewDisplay){
				InterviewsService.saveIntDisplayList($scope.answeredQuestion).then(function(response){
					if(response.status == 200){
						if(displayAnswersExist){
						var interviewDisplayAnswers = 
							buildInterviewDisplayAnswers(displayAnswersExist,displayAnswersExist);
						InterviewsService.updateDisplayAnswerList(interviewDisplayAnswers).then(function(response){
							if(response.status == 200){
								InterviewsService.getIntDisplay(interviewId).then(function(response){
									if(response.status == 200){
										$scope.answeredQuestion = response.data;
										listOfInterviewDisplay = $scope.answeredQuestion;
										$scope.displayHistory = angular.copy($scope.answeredQuestion);
									}
								});
							}
						});
						}else{
						InterviewsService.getIntDisplay(interviewId).then(function(response){
							if(response.status == 200){
								$scope.answeredQuestion = response.data;
								listOfInterviewDisplay = $scope.answeredQuestion;
								$scope.displayHistory = angular.copy($scope.answeredQuestion);
							}
						});
						}
					}
				});
			}else{
				InterviewsService.getIntDisplay(interviewId).then(function(response){
					if(response.status == 200){
						$scope.answeredQuestion = response.data;
						listOfInterviewDisplay = $scope.answeredQuestion;
						$scope.displayHistory = angular.copy($scope.answeredQuestion);
//						_.reverse($scope.displayHistory);
					}
				});
			}
		}
		
		var displaySequence = 0;
		var listOfInterviewDisplay = [];
		function saveInterviewDisplay(question){
			var question = angular.copy(question);
			// get highest sequence for this interview and increment
			var maxSequence = 
				_.maxBy($scope.answeredQuestion, function(o) { return o.sequence; });
			if(maxSequence){
				displaySequence = maxSequence.sequence;
			}
			// get the header by matching topNodeId to link
			var header = "";
			var linkNode = _.find($scope.interview.questionHistory,function(qnode){
				  var retValue = false;
				  if(qnode.link){
					  if(qnode.link == question.topNodeId){
						  retValue = true;
				  }
				  }
				  return retValue;
			  });
			  if(linkNode){
				  header = linkNode.name.substr(0,4);
			  } 
			// is the question already in the display list
			var display = _.find(listOfInterviewDisplay,function(display){
				return display.questionId == question.questionId
				&& display.deleted == 0;
			});
			var id = display?display.id:null;
			if(id){
				display.deleted = 1;
				_.each(display.answers,function(ans){
					ans.deleted = 1;
				});
				//check if is a multiple question
				if(display.answers && display.type == 'Q_multiple'){
					//check if answer is in list of answers given
					// possible to be empty for resumed interviews
					var answersPushed = false;
					question.answers = [];
					_.each(display.answers,function(ans){
						var answerExist = _.find(listOfAnswersGiven,function(ansGiven){
							return ansGiven.answerId == ans.answerId;
						});
						if(answerExist && answerExist.deleted == 0){
							answersPushed = true;
							question.answers.push(answerExist);
						}
					});
					//check if there are answers pushed, if not its a possible resume
					//need to check answerHistory instead
					if(!(answersPushed)){
						_.each(display.answers,function(ans){
							var answerExist = _.find($scope.interview.answerHistory,function(ansGiven){
								return ansGiven.answerId == ans.answerId;
							});
							if(answerExist && answerExist.deleted == 0){
								question.answers.push(answerExist);
							}
						});
					}
				}
				InterviewsService.saveIntDisplay(display).then(function(response){
					if(response.status == 200){
						var index = _.indexOf(listOfInterviewDisplay,
								_.find(listOfInterviewDisplay, {id: response.data.id}));
						if(index != -1){
							listOfInterviewDisplay.splice(index, 1, response.data);
						}
						var interviewDisplayAnswers = 
							buildInterviewDisplayAnswers(response.data,display);
						InterviewsService.updateDisplayAnswerList(interviewDisplayAnswers).then(function(response){
							if(response.status == 200){
								var answeredQuestion = {
										idInterview:question.idInterview,
										number:question.number,
										name:question.name,
										type:question.type,
										questionId:question.questionId,
										sequence:++displaySequence,
										header:header,
										parentModuleId:question.parentModuleId,
										topNodeId:question.topNodeId,
										parentAnswerId:question.parentAnswerId,
										link:question.link,
										description:question.description,
										nodeClass:question.nodeClass,
										deleted:0
								};
								InterviewsService.saveIntDisplay(answeredQuestion).then(function(response){
									if(response.status == 200){
										var index = _.indexOf(listOfInterviewDisplay,
												_.find(listOfInterviewDisplay, {id: response.data.id}));
										if(index != -1){
											listOfInterviewDisplay.splice(index, 1, response.data);
										}else{
											listOfInterviewDisplay.push(response.data);
										}
										var interviewDisplayAnswers = 
											buildInterviewDisplayAnswers(response.data,question);
										InterviewsService.updateDisplayAnswerList(interviewDisplayAnswers).then(function(response){
											if(response.status == 200){
												//refreshInterviewDisplayNew(question);
											}
										});
									}
								});
							}
						});
					}
				});
				
				
			}else{
			var answeredQuestion = {
					idInterview:question.idInterview,
					number:question.number,
					name:question.name,
					type:question.type,
					questionId:question.questionId,
					sequence:++displaySequence,
					header:header,
					parentModuleId:question.parentModuleId,
					topNodeId:question.topNodeId,
					parentAnswerId:question.parentAnswerId,
					link:question.link,
					description:question.description,
					nodeClass:question.nodeClass,
					deleted:0
			};
			InterviewsService.saveIntDisplay(answeredQuestion).then(function(response){
				if(response.status == 200){
					var index = _.indexOf(listOfInterviewDisplay,
							_.find(listOfInterviewDisplay, {id: response.data.id}));
					if(index != -1){
						listOfInterviewDisplay.splice(index, 1, response.data);
					}else{
						listOfInterviewDisplay.push(response.data);
					}
					var interviewDisplayAnswers = 
						buildInterviewDisplayAnswers(response.data,question);
					InterviewsService.updateDisplayAnswerList(interviewDisplayAnswers).then(function(response){
						if(response.status == 200){
							//refreshInterviewDisplayNew(question.idInterview);
						}
					});
				}
			});
			}
		}
		
		function buildInterviewDisplayAnswers(intDisplay,question){
			var answers = [];
			_.each(question.answers,function(ans){
				answers.push({
					id:ans.id,
					interviewDisplayId:intDisplay.id,
					answerId:ans.answerId,
					name:ans.name,
					answerFreetext:ans.answerFreetext,
					nodeClass:ans.nodeClass,
					number:ans.number,
					type:ans.type,
					deleted:ans.deleted,
					lastUpdated:intDisplay.lastUpdated
				});
			});
			return answers;
		}
		
		// might need to deprecate below
		function refreshDisplay(node){
			$scope.displayHistory = angular.copy($scope.interview.questionHistory);
			_.remove($scope.displayHistory, function(node) {
				  return node.link || node.deleted || !node.processed;
				});
			
			_.each($scope.displayHistory, function(node) {
				  var linkNode = _.find($scope.interview.questionHistory,function(qnode){
					  var retValue = false;
					  if(qnode.link){
						  if(qnode.link == node.topNodeId){
							  retValue = true;
						  }
					  }
					  return retValue;
				  });
				  if(linkNode){
					  node.header = linkNode.name.substr(0,4);
				  } 
			});						
		}
		function refreshDisplayNew(question){
			if(!$scope.displayHistoryNew){
				$scope.displayHistoryNew = angular.copy($scope.interview.actualQuestion);
				_.remove($scope.displayHistoryNew, function(node) {
					  return node.link || node.deleted || !node.processed;
					});
			}
			_.each($scope.displayHistoryNew, function(node) {
				  var linkNode = _.find($scope.interview.questionHistory,function(qnode){
					  var retValue = false;
					  if(qnode.link){
						  if(qnode.link == node.topNodeId){
							  retValue = true;
						  }
					  }
					  return retValue;
				  });
				  if(linkNode){
					  node.header = linkNode.name.substr(0,4);
				  } 
			});	
			if(question){				
				InterviewsService.getInterviewQuestion(question.id).then(function(response){
					if(response.status === 200){
						var latestQ = response.data;
						var bFound = false;
						for(var i=0;i<$scope.displayHistoryNew.length;i++){
							var q = $scope.displayHistoryNew[i];
							if(q.id==latestQ.id){
								q = latestQ;
								$scope.displayHistoryNew[i] = q;
								bFound = true;
							}
						}
						if(!bFound){
							$scope.displayHistoryNew.push(latestQ);
						}
						_.each($scope.displayHistoryNew, function(node) {
							  var linkNode = _.find($scope.interview.questionHistory,function(qnode){
								  var retValue = false;
								  if(qnode.link){
									  if(qnode.link == node.topNodeId){
										  retValue = true;
									  }
								  }
								  return retValue;
							  });
							  if(linkNode){
								  node.header = linkNode.name.substr(0,4);
							  } 
						});	
					}
				});
			}	
								
		}
		
		function showNextQuestionNew(lookupNode, parentNode) {			
			var lookupNodeTemp = lookupNode;
			var parentNodeTemp = parentNode;
			$scope.inProgress = true; 
			safeDigest($scope.inProgress); 
			return InterviewsService.getUnprocessedQuestions($scope.interview.interviewId).then(function(response) {
				if(response.status==200){
					//$scope.interview = response.data[0];
					var unprocessedQuestions = response.data[0].questionQueueUnprocessed;
					//remove null values in array if any
					unprocessedQuestions = _.compact(unprocessedQuestions);
					for(var i=0;i<unprocessedQuestions.length;i++){
						var unprocessedQuestion = unprocessedQuestions[i];
						//if unprocessed not in interview.questionHistory add
						var bFound = false;
						for(var j=0;j<$scope.interview.questionHistory.length;j++){
							var question = $scope.interview.questionHistory[j];
							if(question.questionId==unprocessedQuestion.questionId){
								bFound = true;
							}		
						}
						if(!bFound){
							$scope.interview.questionHistory.push(unprocessedQuestion);
						}
					}
					safeDigest($scope.interview);
					//refreshDisplay();
					var question = findNextQuestionQueued($scope.interview);
					if(question){
						if(question.link==0){	
							QuestionsService.findQuestionSingleChildLevel(question.questionId).then(function(response){
								if(response.status === 200){
									var ques = response.data[0];
									
									$scope.interview.showedQuestion = ques;
									
									safeDigest($scope.interview.showedQuestion);
									if (ques.type == 'Q_frequency') { //if frequency set up frequency lists
										$scope.hoursPerWeekArray = $scope.getHoursPerWeekArray();
										$scope.hoursArray = $scope.getShiftHoursArray();
										$scope.minutesArray = $scope.getShiftMinutesArray();
										$scope.weeks = $scope.getWeeksArray();
										$scope.seconds = $scope.getSecondsArray();
									}
									var nodeLink = _.find($scope.interview.questionHistory,function(node){
										return node.link==ques.topNodeId;
									});
									$scope.questionheader.name = nodeLink.name.substring(0, 4);
									$scope.inProgress = false;
									safeDigest($scope.inProgress);
									$scope.scrollTo($scope.interview.showedQuestion.idNode);
								}else{
									var errorMsg = "Error getting question "+question.questionId;
									console.error(errorMsg);
									alert(errorMsg);
								}
							});	
						}else{
							processLinkingQuestionNew(question); //recursive process while linking question found
						}				
					}else{
						//end?
						endInterview();
					}
				}
			});			
		}

		var possibleCircularRefHandle = 0;		
		function processLinkingQuestionNew(linkeQuestion){
			var questionTemp = linkeQuestion;
			possibleCircularRefHandle = possibleCircularRefHandle+1;
			if(possibleCircularRefHandle>200){
				var msg = "Error! Possible Circular reference";
				console.error(msg);
				alert(msg);
			}
			var firstLinkIntervewQuestion = populateInteviewQuestionJsonByLinkedQuestion($scope.interview,linkeQuestion);
			firstLinkIntervewQuestion.processed = true;
			//update to processed and get child questions in the queue
			InterviewsService.saveLinkQuestionAndQueueQuestions(firstLinkIntervewQuestion).then(function(response) {
				if (response.status === 200) {
					for(var i=0;i<$scope.interview.questionHistory.length;i++){
						var question = $scope.interview.questionHistory[i];
						if(question.questionId==linkeQuestion.questionId){
							question.processed = true;
						}		
					}
					InterviewsService.getUnprocessedQuestions($scope.interview.interviewId).then(function(response) {
						if(response.status==200){
							//$scope.interview = response.data[0];
							var unprocessedQuestions = response.data[0].questionQueueUnprocessed;
							unprocessedQuestions = _.compact(unprocessedQuestions);
							for(var i=0;i<unprocessedQuestions.length;i++){
								var unprocessedQuestion = unprocessedQuestions[i];
								//if unprocessed not in interview.questionHistory add
								var bFound = false;
								
								for(var j=0;j<$scope.interview.questionHistory.length;j++){
									var question = $scope.interview.questionHistory[j];
									if(question.questionId==unprocessedQuestion.questionId){
										bFound = true;
									}		
								}
								if(!bFound){
									$scope.interview.questionHistory.push(unprocessedQuestion);
								}
							}
							safeDigest($scope.interview);
							//refreshDisplay();
							var question = findNextQuestionQueued($scope.interview);
							if(question){
								if(question.link==0){
									QuestionsService.findQuestionSingleChildLevel(question.questionId).then(function(response){
										if(response.status === 200){
											var ques = response.data[0];
											$scope.interview.showedQuestion = ques;
											safeDigest($scope.interview.showedQuestion);
											var nodeLink = _.find($scope.interview.questionHistory,function(node){
												return node.link==ques.topNodeId;
											});
											$scope.questionheader.name = nodeLink.name.substring(0, 4);
											$scope.inProgress = false;
											safeDigest($scope.inProgress);
											$scope.scrollTo($scope.interview.showedQuestion.idNode);
										}else{
											var errorMsg = "Error getting question "+question.questionId;
											console.error(errorMsg);
											alert(errorMsg);
										}
									});	
								}else{ //this is a link
									processLinkingQuestionNew(question);
								}
							}else{
								console.error("Could not queue first question");
								alert("Could not queue first question");
							}
						}
					});
				}									
			});
		}
		function endInterview() {
			$scope.inProgress = false;
			safeDigest($scope.inProgress);
			$scope.interviewStarted = false;
			$scope.interviewEnded = true;
			$scope.updateEnable = false;

		}
		$scope.finishInterview = function(){
			var interview = $scope.interview;
			if (!(interview.notes)) {
				interview.notes = [];
			}
			interview.notes
					.push({
						interviewId : interview.interviewId,
						text : interview.endNotes,
						type : 'Interviewer'
					});
			saveInterview(interview);
			var participant = $rootScope.participant;
			participant.status = 2;//completed
			$rootScope.saveParticipant(participant);
			$scope.closeAndSwitchToParticipantsTab();

		}
		$scope.closeOnStopButton = function(){
			$scope.closeAndSwitchToParticipantsTab();
		}
		$scope.cancel = function() {
			$mdDialog.cancel();
		};
		$scope.saveNewNoteButton = function(data) {
			saveNewNote(data);
		}
		$scope.saveEditQuestionButton = function(data) {
			saveEditQuestion(data);
		}
		function saveNewNote(result) {
			var noteText = result;
			var interview = $scope.interview;
			if (!(interview.notes)) {
				interview.notes = [];
			}
			interview.notes.push({
				interviewId : interview.interviewId,
				text : noteText,
				type : 'Interviewer'
			});
			saveInterview(interview);
			$mdDialog.cancel();
		}
		function saveEditQuestion(node){
			if (!(validateIfAnswerEdited(node))) {
				alert("Nothing to update.");
				return;
			}
			var interview = $scope.interview;
			if (interview) {
				if (node.type == 'Q_multiple') {
					processInterviewQuestionWithMultipleAnswersEdit(interview, node);
				} else if (node.type == 'Q_frequency') {
					processFrequencyNewEdit(interview, node);
				} else {
					processEditInterviewQuestionNew(interview, node);
				}
			}else{
				alert("OOPS! Lost the Scope Interview, Please take a screen shot and submit a issue ticket");
			}
			
		}
		$scope.showNotePrompt = function(ev) {
			$mdDialog.show({
				scope : $scope.$new(),
				templateUrl : 'scripts/interviews/view/noteDialog.html',
				clickOutsideToClose:true
			});
		};
		$scope.showEditQuestionPrompt = function(ev,node) {
			$scope.participant.status = 0;//running
			$scope.interviewStarted = true;
			InterviewsService.getInterviewQuestion(node.id).then(function(response){
				if(response.status === 200){
					var iq = response.data;
					QuestionsService.findQuestionSingleChildLevel(iq.questionId).then(function(response){
						if(response.status === 200){
							$log.info('Question Found');
							var fullQuestion = response.data[0];
							_.each(iq.answers,function(actualAnswer) {
								if(actualAnswer.deleted==0){
									_.find(fullQuestion.nodes,function(possibleAnswer) {
										if (actualAnswer.answerId == possibleAnswer.idNode) {
											possibleAnswer.isSelected = true;
											fullQuestion.selectedAnswer = possibleAnswer;
											if(actualAnswer.type == 'P_freetext'){
												fullQuestion.selectedAnswer.name = actualAnswer.name;
												fullQuestion.selectedAnswer.answerFreetext = actualAnswer.answerFreetext;
											}else if(actualAnswer.type.startsWith('P_frequency')){
												$scope.currentFrequencyValue = actualAnswer.answerFreetext;
											}
										}
									});
								}	
							});
							$scope.questionBeingEdited = fullQuestion;
							$scope.questionBeingEditedCopy = angular.copy(fullQuestion);
							if(fullQuestion.type == 'Q_frequency'){
								$scope.hoursPerWeekArray = $scope.getHoursPerWeekArray();
								$scope.hoursArray = $scope.getShiftHoursArray();
								$scope.minutesArray = $scope.getShiftMinutesArray();
								$scope.weeks = $scope.getWeeksArray();
								$scope.seconds = $scope.getSecondsArray();
								
							}					
							$mdDialog.show({
								scope : $scope.$new(),
								templateUrl : 'scripts/interviews/view/editQuestionDialog.html',
								clickOutsideToClose:true
							});
						}else{
							$log.error('ERROR on findQuestions in showEditQuestionPrompt!');
							throw response;
						}
					});
				}
			});
			
		};
        $scope.handleSaveNote = function(note){
            note.isEditing = false;
            InterviewsService.saveNote(note).then(function(response){
                if (response.status === 200) {
                    $log.info("Success in save note.");
                }else{
                    $log.error("Error in save note");
                }
            });
        };
        $scope.handleDeleteNote = function(note){
            note.deleted = 1;
            InterviewsService.saveNote(note).then(function(response){
                if (response.status === 200) {
                    $log.info("Success in delete note.");
                    loadNotes();
                }else{
                    $log.error("Error in delete note");
                }
            });
        };
        function loadNotes(){
            console.log("Interview scope",$scope.interview);
            InterviewsService.getListNote($scope.interview.interviewId).then(function(response){
                if(response){
                    $scope.interview.notes = response.data;
                }
            });
        }
        $scope.allowSpaceOnFreeText = function(event,node){
            if(event.keyCode == 32){
        		node.name = node.name + ' ';
        		node.isSelected = true;
        	}
        };
	}
})();
