angular.module('myApp', ['myApp.services','myApp.directives'])
.controller("listObjectControl", ['$scope', '$http', 'DataService', function ($scope, $http, DataService){
	$scope.uploadingFiles=[];
	$scope.currentPath='';
	$scope.rootPath = {
							Name :'/',
							FullPath:'/'
						};
	$scope.parentPaths = [];
	$scope.dataInfo = {
						error:false,
						directories:[]
						};
	
	//$scope.dataInfo.directories = DataService.getDirectories('/');
	getDirectories($scope.rootPath);
	
    $scope.uploadFile = function(){
		if(typeof($scope.uploadingFiles) != undefined && $scope.uploadingFiles.length > 0)
		{
			var files = $scope.uploadingFiles;
			//console.log('file is ' );
		   // console.dir(file);
			var uploadUrl = "/?api=upload&path="+$scope.currentPath;
			for (var i = 0, f; f = files[i]; i++) {
				var fd = new FormData();
				fd.append('file', f);
				$http.post(uploadUrl, fd, {
					transformRequest: angular.identity,
					headers: {'Content-Type': undefined}
				})
				.success(function(respone){
				})
				.error(function(respone){
					alert(respone.data);
				});
			}
			//fileUpload.uploadFileToUrl(files, uploadUrl);
		}
    };

	$scope.deleteItem = function(selectedObject){
		alert('delete has not implemented');
    };
	$scope.renameItem = function(selectedObject, isBack){
		alert('rename has not implemented');
    };
	$scope.getFile = function(selectedObject){
		 // var element = angular.element('<a/>');
                         // element.attr({
                             // href: '/?api=get&path='+selectedObject.FullPath,
                             // target: '_blank',
                             // download: selectedObject.Name
                         // })[0].click();
		window.open('/?api=get&path='+selectedObject.FullPath, "_blank");
    };
	
	$scope.getDirectories = function(selectedObject, isBack){
		$scope.dataInfo.directories = DataService.getDirectories('/');
		//getDirectories(selectedObject, isBack);
    };

    function getDirectories(selectedObject, isBack){
	$scope.dataInfo.directories = DataService.getDirectories('/');
	return;
    $http({
    	  method: 'GET',
    	  url: '/?api=browser&path='+selectedObject.FullPath
    	}).then(function successCallback(response) {
    		// this callback will be called asynchronously
    		// when the response is available
			$scope.dataInfo.error = false;
			$scope.dataInfo.directories = response.data;
			if(isBack){
				if($scope.parentPaths.length>0){
					$scope.parentPaths.pop();
				}
			}else{
				$scope.parentPaths.push(selectedObject);
			}
			$scope.currentPath = selectedObject.FullPath;
    	  }, function errorCallback(response) {
			  alert(response.data)
    		// called asynchronously if an error occurs
    		// or server returns response with an error status.
    	  });
    }
}]);
