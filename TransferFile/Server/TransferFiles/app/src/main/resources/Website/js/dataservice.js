var myApp = angular.module('myApp.services', []);

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
				isFile: false ,
				DisplayLastModified:'10-1-2003',
			  },
			  {
				Name :'hung 2',
				FullPath:'hung dep trai2',
				Size:"14G",
				isFile: false ,
				DisplayLastModified:'23-2-2013',
			  },
			  {
				Name :'hung 231',
				FullPath:'hung dep trai3',
				Size:"134B",
				isFile: false ,
				DisplayLastModified:'10-11-2014',
			  },
			  {
				Name :'hung ',
				FullPath:'hung dep trai',
				Size:"",
				isFile: false ,
				DisplayLastModified:'10-1-2003',
			  },
			  {
				Name :'hung 2',
				FullPath:'hung dep trai',
				Size:"14G",
				isFile: false ,
				DisplayLastModified:'23-2-2013',
			  },
			  {
				Name :'hung 231',
				FullPath:'hung dep trai',
				Size:"134B",
				isFile: true ,
				DisplayLastModified:'10-11-2014',
			  },
			  {
				Name :'có con chim là chim chich chè',
				FullPath:'hung dep trai',
				Size:"",
				isFile: false ,
				DisplayLastModified:'10-1-2003',
			  },
			  {
				Name :'hung 2',
				FullPath:'hung dep trai',
				Size:"14G",
				isFile: true ,
				DisplayLastModified:'23-2-2013',
			  },
			  {
				Name :'hung 231',
				FullPath:'hung dep trai',
				Size:"134B",
				isFile: true ,
				DisplayLastModified:'10-11-2014',
			  }];
	result = {
		error:false,
		directories:data
	}
  return result;
  }
});