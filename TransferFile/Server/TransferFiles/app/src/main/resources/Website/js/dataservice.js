var myApp = angular.module('myApp.directives', []);

myApp.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;
            
            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);

myApp.service('fileUpload', ['$http', function ($http) {
    this.uploadFileToUrl = function(file, uploadUrl){
        var fd = new FormData();
        fd.append('file', file);
        $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
        .success(function(){
        })
        .error(function(){
        });
    }
}]);

myApp.directive("dropzone", function() {
    return {
        restrict : "A",
        link: function (scope, elem, attrs) {
            elem.bind('drop', function(evt) {
                evt.stopPropagation();
                evt.preventDefault();

                var files = evt.dataTransfer.files;
                for (var i = 0, f; f = files[i]; i++) {
						if(typeof(scope.uploadingFiles) === undefined) scope.uploadingFiles = [];
						scope.uploadingFiles.push(f);
                    //var reader = new FileReader();
                    //reader.readAsArrayBuffer(f);

                    //reader.onload = (function(theFile) {
						//if(typeof(scope.uploadingFiles) === undefined) scope.uploadingFiles = [];
						//scope.uploadingFiles.push(theFile);
                        // return function(e) {
                            // var newFile = { name : theFile.name,
                                // type : theFile.type,
                                // size : theFile.size,
                                // lastModifiedDate : theFile.lastModifiedDate
                            // }

                           ////scope.addfile(newFile);
                        // };
                  //  })(f);
                }			
				scope.$apply();				
				this.className = attrs.class;
            });
			elem.bind('dragover', function(evt) {
					if (event != null) {
				  event.preventDefault();
				}
				this.className = attrs.dragOverClass;
				event.dataTransfer.effectAllowed = 'copy';
				return false;
			});
			elem.bind('dragleave', function(evt) {
					if (event != null) {
				  event.preventDefault();
				}
				this.className = attrs.class;
				event.dataTransfer.effectAllowed = 'copy';
				return false;
			});
			elem.bind('click', function(evt) {
				var fileSelector = document.createElement('input');
				fileSelector.setAttribute('type', 'file');
				fileSelector.setAttribute('multiple', true);
				
				fileSelector.onchange = function(evt) {
					 for (var i = 0, f; f = this.files[i]; i++) {
						if(typeof(scope.uploadingFiles) === undefined) scope.uploadingFiles = [];
						scope.uploadingFiles.push(f);
					 }
					 scope.$apply();
				};
				fileSelector.click();
				return false;
			});
        }
    }
});

angular.module('myApp.services', []).service('DataService', function(){
	this.getDirectories = function(path){
	/*
	$http({
	  method: 'GET',
	  url: '/?path='+path
	}).then(function successCallback(response) {
		// this callback will be called asynchronously
		// when the response is available
	  }, function errorCallback(response) {
		// called asynchronously if an error occurs
		// or server returns response with an error status.
	  });
  */
  var data = [{
				Name :'hung ',
				FullPath:'hung dep trai1',
				Size:"",
				IsFile: false ,
				DisplayLastModified:'10-1-2003',
			  },
			  {
				Name :'hung 2',
				FullPath:'hung dep trai2',
				Size:"14G",
				IsFile: false ,
				DisplayLastModified:'23-2-2013',
			  },
			  {
				Name :'có con chim là chim chich chè',
				FullPath:'hung dep trai',
				Size:"",
				IsFile: false ,
				DisplayLastModified:'10-1-2003',
			  },
			  {
				Name :'hung 231',
				FullPath:'hung dep trai3',
				Size:"134B",
				IsFile: false ,
				DisplayLastModified:'10-11-2014',
			  },
			  {
				Name :'hung ',
				FullPath:'hung dep trai',
				Size:"",
				IsFile: false ,
				DisplayLastModified:'10-1-2003',
			  },
			  {
				Name :'hung 2',
				FullPath:'hung dep trai',
				Size:"14G",
				IsFile: false ,
				DisplayLastModified:'23-2-2013',
			  },
			  {
				Name :'hung 231',
				FullPath:'hung dep trai',
				Size:"134B",
				IsFile: true ,
				DisplayLastModified:'10-11-2014',
			  },
			  {
				Name :'có con chim là chim chich chè',
				FullPath:'hung dep trai',
				Size:"",
				IsFile: false ,
				DisplayLastModified:'10-1-2003',
			  },
			  {
				Name :'hung 2',
				FullPath:'hung dep trai',
				Size:"14G",
				IsFile: true ,
				DisplayLastModified:'23-2-2013',
			  },
			  {
				Name :'hung 231',
				FullPath:'hung dep trai',
				Size:"134B",
				IsFile: true ,
				DisplayLastModified:'10-11-2014',
			  }];
  return data;
  }
});