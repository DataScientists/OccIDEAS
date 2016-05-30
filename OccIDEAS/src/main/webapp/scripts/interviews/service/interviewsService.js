(function () {
    angular.module('occIDEASApp.Interviews')
        .service('InterviewsService', InterviewsService);

    InterviewsService.$inject = ['$http', '$q'];
    function InterviewsService($http, $q) {
        function get(idNode) {
            var restUrl = 'web/rest/interview/get?id=' + idNode;
            var request = $http({
                method: 'GET',
                url: restUrl
            })
            return request.then(handleSuccess, handleError);
        }
        function checkReferenceNumberExists(referenceNumber) {
            var restUrl = 'web/rest/interview/getbyref?ref=' + referenceNumber;
            var request = $http({
                method: 'GET',
                url: restUrl
            })
            return request.then(handleSuccess, handleError);
        }
        function findModule(idNode) {
			var restUrl = 'web/rest/module/getinterviewmodule?id=' + idNode;
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
        function findFragment(idNode) {
			var restUrl = 'web/rest/fragment/getinterviewfragment?id=' + idNode;
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
        function save(data) {
        	console.log("Saving interview");
        	console.dir(data);
            var restSaveUrl = 'web/rest/interview/update';
            var request = $http({
                method: 'POST',
                url: restSaveUrl,
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
        function getNextQuestion(data) {
            var nextQ = 'web/rest/interview/nextquestion';
            var request = $http({
                method: 'POST',
                url: nextQ,
                data: data
            })
            return request.then(handleSuccess, handleError);
        }
        
        function getInterview(interviewId) {
            var url = 'web/rest/interview/getInterview?interviewId=' + interviewId;
            var request = $http({
                method: 'GET',
                url: url
            })
            return request.then(handleSuccess, handleError);
        }
        
        function saveInterviewMod(data) {
     		var restUrl = 'web/rest/interview/saveMod';
     		var request =  $http({
     			method: 'POST',
     			url: restUrl,
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
			var restUrl = 'web/rest/interviewquestionanswer/save';
			var request =  $http({
				  method: 'POST',
				  url: restUrl,
				  data:data
				})
			return request.then(handleSuccess,handleError);
		}
        
        function saveAnswers(data) {
			var restUrl = 'web/rest/interviewquestionanswer/saveAnswers';
			var request =  $http({
				  method: 'POST',
				  url: restUrl,
				  data:data
				})
			return request.then(handleSuccess,handleError);
		}
        
        function getIntQuestion(idInterview,questionId,modCount) {
			var restUrl = 'web/rest/interviewquestionanswer/getIntQuestion?idInterview='
							+idInterview+'&questionId='+questionId+'&modCount='+modCount;
			var request =  $http({
				  method: 'GET',
				  url: restUrl
				})
			return request.then(handleSuccess,handleError);
		}
        
        function saveIntDisplay(data){
        	var restUrl = 'web/rest/interviewdisplay/update';
        	var request =  $http({
				  method: 'POST',
				  url: restUrl,
				  data:data
				})
			return request.then(handleSuccess,handleError);
        }
        
        function getIntDisplay(interviewId){
        	var restUrl = 'web/rest/interviewdisplay/getIntDisplay?id=' + interviewId;
        	var request =  $http({
				  method: 'GET',
				  url: restUrl
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
            saveAnswers:saveAnswers,
            getInterview:getInterview,
            saveInterviewMod:saveInterviewMod,
            saveIntDisplay:saveIntDisplay,
            getIntDisplay:getIntDisplay,
            getIntQuestion:getIntQuestion
        };
    }
})();