'use strict';

angular.module('angular-js-xlsx', [])
  .directive('jsXls', function () {
    return {
      restrict: 'E',
      template: '<input type="file" />',
      replace: true,
      link: function (scope, element, attrs) {
    	  /*Get File Extesion*/
    	  
    	  function getFileExtension(filename){
    		  return filename.slice((filename.lastIndexOf(".") -1 >>> 0) + 2);
    	  }
        function handleSelect() {
          var files = this.files;
          for (var i = 0, f = files[i]; i != files.length; ++i) {
            var reader = new FileReader();
            var name = f.name;
            if(getFileExtension(name) != "xlsx" ){
            	 var handleError = scope[attrs.onerror];
                 if (typeof handleError === "function") {
                	 var e = {};
                	 e.type = 1;
                	 e.message = "Invalid File Type. Only excel files are allowed";
                   handleError(e);
                 }
                 element.val('');
                 return;
            }
            reader.onload = function (e) {
              if (!e) {
                var data = reader.content;
              } else {
                var data = e.target.result;
              }

              /* if binary string, read with type 'binary' */
              try {
                var workbook = XLS.read(data, { type: 'binary' });

                
                
                if (attrs.onread) {
                  var handleRead = scope[attrs.onread];
                  if (typeof handleRead === "function") {
                    handleRead(workbook);
                  }
                }
              } catch (e) {
                if (attrs.onerror) {
                  var handleError = scope[attrs.onerror];
                  if (typeof handleError === "function") {
                    handleError(e);
                  }
                }
              }

              // Clear input file
              element.val('');
            };

            //extend FileReader
            if (!FileReader.prototype.readAsBinaryString) {
              FileReader.prototype.readAsBinaryString = function (fileData) {
                var binary = "";
                var pt = this;
                var reader = new FileReader();
                reader.onload = function (e) {
                  var bytes = new Uint8Array(reader.result);
                  var length = bytes.byteLength;
                  for (var i = 0; i < length; i++) {
                    binary += String.fromCharCode(bytes[i]);
                  }
                  //pt.result  - readonly so assign binary
                  pt.content = binary;
                  $(pt).trigger('onload');
                }
                reader.readAsArrayBuffer(fileData);
              }
            }

            reader.readAsBinaryString(f);

          }
        }

        element.on('change', handleSelect);
      }
    };
  });