(function () {
    angular.module('occIDEASApp.Interviews')
        .service('InterviewsService', InterviewsService);

    InterviewsService.$inject = ['$http', '$q'];
    function InterviewsService($http, $q) {
        function get(idNode) {
            var restURL = 'web/rest/interview/get?id=' + idNode;
            var request = $http({
                method: 'GET',
                url: restURL
            })
            return request.then(handleSuccess, handleError);
        }
        
        function getInterviewWithFiredRules(idNode){
        	var restURL = 'web/rest/interview/getInterviewWithFiredRules?id='+idNode;
        	var request = $http({
                method: 'GET',
                url: restURL
            })
            return request.then(handleSuccess, handleError);
        }
        
        function getUnprocessedQuestions(idNode) {
            var restURL = 'web/rest/interview/getUnprocessedQuestions?id=' + idNode;
            var request = $http({
                method: 'GET',
                url: restURL
            })
            return request.then(handleSuccess, handleError);
        }
        
        
        function checkReferenceNumberExists(referenceNumber) {
            var restURL = 'web/rest/interview/getbyref?ref=' + referenceNumber;
            var request = $http({
                method: 'GET',
                url: restURL
            })
            return request.then(handleSuccess, handleError);
        }
        function findModule(idNode) {
			var restURL = 'web/rest/module/getinterviewmodule?id=' + idNode;
			var request =  $http({
				  method: 'GET',
				  url: restURL
				})
			return request.then(handleSuccess,handleError);
		}
        function findFragment(idNode) {
			var restURL = 'web/rest/fragment/getinterviewfragment?id=' + idNode;
			var request =  $http({
				  method: 'GET',
				  url: restURL
				})
			return request.then(handleSuccess,handleError);
		}
        function save(data) {
        	console.log("Saving interview");
        	console.dir(data);
            var restURL = 'web/rest/interview/update';
            var request = $http({
                method: 'POST',
                url: restURL,
                data: data
            })
            return request.then(handleSuccess, handleError);
        }

        function getNextQuestionOld(data) {
            var saveAndNextQ = 'web/rest/interview/saveAndNextQ';
            var request = $http({
                method: 'POST',
                url: saveAndNextQ,
                data: data
            })
            return request.then(handleSuccess, handleError);
        }
        function getNextQuestion(lookupNode) {
            var nextQ = 'web/rest/interview/nextquestion';
            var request = $http({
                method: 'POST',
                url: nextQ,
                params:{
                	parentId: lookupNode.parentId,
                	number:lookupNode.number
                	}
                
            })
            return request.then(handleSuccess, handleError);
        }
        
        function getInterview(interviewId) {
            var url = 'web/rest/interview/getInterview?interviewId=' + interviewId;
            var request = $http({
                method: 'GET',
                url: url,
                ignoreLoadingBar: true
            })
            return request.then(handleSuccess, handleError);
        }
        
        function getInterviewQuestionAnswer(interviewId) {
            var url = 'web/rest/interview/getInterviewQuestionAnswer?interviewId=' + interviewId;
            var request = $http({
                method: 'GET',
                url: url,
                ignoreLoadingBar: true
            })
            return request.then(handleSuccess, handleError);
        }
        
        function getInterviewIdList(){
        	var url = 'web/rest/interview/getAllInterviewId';
        	var request = $http({
                method: 'GET',
                url: url,
                ignoreLoadingBar: true
            })
            return request.then(handleSuccess, handleError);
        }
        
        function getInterviewsWithoutAnswers(){
        	var url = 'web/rest/interview/getInterviewsWithoutAnswers';
        	var request = $http({
                method: 'GET',
                url: url,
                ignoreLoadingBar: true
            })
            return request.then(handleSuccess, handleError);
        }
        
        function getInterviewQuestionList(interviewId) {
            var url = 'web/rest/interviewquestionanswer/getbyinterviewid?id=' + interviewId;
            var request = $http({
                method: 'GET',
                url: url
            })
            return request.then(handleSuccess, handleError);
        }

        function saveInterviewMod(data) {
     		var restURL = 'web/rest/interview/saveMod';
     		var request =  $http({
     			method: 'POST',
     			url: restURL,
     			data:data
     		})
     		return request.then(handleSuccess,handleError);
        }

        function startInterview(data) {
            var startInterview = 'web/rest/interview/create';
            var request = $http({
                method: 'POST',
                url: startInterview,
                data: data
            })
            return request.then(handleSuccess, handleError);
        }

        function saveQuestion(data) {
			var restURL = 'web/rest/interviewquestionanswer/save';
			var request =  $http({
				  method: 'POST',
				  url: restURL,
				  data:data
				})
			return request.then(handleSuccess,handleError);
		}
        function saveLinkQuestionAndQueueQuestions(data) {
			var restURL = 'web/rest/interviewquestionanswer/saveLinkAndQueueQuestions';
			var request =  $http({
				  method: 'POST',
				  url: restURL,
				  data:data
				})
			return request.then(handleSuccess,handleError);
		}
        
        function saveAnswers(data) {
			var restURL = 'web/rest/interviewquestionanswer/saveAnswers';
			var request =  $http({
				  method: 'POST',
				  url: restURL,
				  data:data
				})
			return request.then(handleSuccess,handleError);
		}
        function saveQuestions(data) {
			var restURL = 'web/rest/interviewquestionanswer/saveQuestions';
			var request =  $http({
				  method: 'POST',
				  url: restURL,
				  data:data
				})
			return request.then(handleSuccess,handleError);
		}
        function saveAnswersAndQueueQuestions(data) {
			var restURL = 'web/rest/interviewquestionanswer/saveAnswersandQueueQuestions';
			var request =  $http({
				  method: 'POST',
				  url: restURL,
				  data:data
				})
			return request.then(handleSuccess,handleError);
		}
        
        function getIntQuestion(idInterview,questionId,modCount) {
			var restURL = 'web/rest/interviewquestionanswer/getIntQuestion?idInterview='
							+idInterview+'&questionId='+questionId+'&modCount='+modCount;
			var request =  $http({
				  method: 'GET',
				  url: restURL
				})
			return request.then(handleSuccess,handleError);
		}
        
        function saveIntDisplay(data){
        	var restURL = 'web/rest/interviewdisplay/update';
        	var request =  $http({
				  method: 'POST',
				  url: restURL,
				  data:data
				})
			return request.then(handleSuccess,handleError);
        }
        
        function saveIntDisplayList(data){
        	var restURL = 'web/rest/interviewdisplay/updateList';
        	var request =  $http({
				  method: 'POST',
				  url: restURL,
				  data:data
				})
			return request.then(handleSuccess,handleError);
        }
        
        function updateDisplayAnswerList(data){
        	var restURL = 'web/rest/interviewdisplay/updateDisplayAnswerList';
        	var request =  $http({
				  method: 'POST',
				  url: restURL,
				  data:data
				})
			return request.then(handleSuccess,handleError);
        }
        
        function getIntDisplay(interviewId){
        	var restURL = 'web/rest/interviewdisplay/getIntDisplay?id=' + interviewId;
        	var request =  $http({
				  method: 'GET',
				  url: restURL
				})
			return request.then(handleSuccess,handleError);
        }
        function getInterviewQuestion(interviewQuestionId){
        	var restURL = 'web/rest/interviewquestionanswer/getInterviewQuestion?interviewQuestionId=' + interviewQuestionId;
        	var request =  $http({
				  method: 'GET',
				  url: restURL
				})
			return request.then(handleSuccess,handleError);
        }

        function saveNote(data){
            var restURL = 'web/rest/note/update';
            var request =  $http({
                method: 'POST',
                url: restURL,
                data:data
            })
            return request.then(handleSuccess,handleError);
        }

        function getListNote(interviewId){
            var restURL = 'web/rest/note/getlistbyinterview?interviewId=' + interviewId;
            var request =  $http({
                method: 'GET',
                url: restURL
            })
            return request.then(handleSuccess,handleError);
        }
        
        function handleError(response) {
            if (
                !angular.isObject(response.data) || !response.data.message
            ) {
                return ( $q.reject("An unknown error occurred.") );
            }
            return ( $q.reject(response.data.message) );
        }

        function handleSuccess(response) {
            return (response);
        }

        return {
        	checkReferenceNumberExists:checkReferenceNumberExists,
        	findFragment: findFragment,
        	findModule: findModule,
        	save: save,
            get: get,
            startInterview:startInterview,
            getNextQuestion: getNextQuestion,
            saveQuestion:saveQuestion,
            saveQuestions:saveQuestions,
            saveAnswers:saveAnswers,
            saveAnswersAndQueueQuestions:saveAnswersAndQueueQuestions,
            saveLinkQuestionAndQueueQuestions:saveLinkQuestionAndQueueQuestions,
            getInterview:getInterview,
            saveInterviewMod:saveInterviewMod,
            saveIntDisplay:saveIntDisplay,
            saveIntDisplayList:saveIntDisplayList,
            getIntDisplay:getIntDisplay,
            getIntQuestion:getIntQuestion,
            getInterviewQuestion:getInterviewQuestion,
            getInterviewQuestionList:getInterviewQuestionList,
            saveNote:saveNote,
            getListNote:getListNote,
            getInterviewIdList:getInterviewIdList,
            getInterviewsWithoutAnswers:getInterviewsWithoutAnswers,
            getInterviewQuestionAnswer:getInterviewQuestionAnswer,
            getUnprocessedQuestions:getUnprocessedQuestions,
            getInterviewWithFiredRules:getInterviewWithFiredRules,
            updateDisplayAnswerList:updateDisplayAnswerList
        };
    }
})();