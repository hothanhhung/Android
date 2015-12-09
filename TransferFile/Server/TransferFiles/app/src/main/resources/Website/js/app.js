angular.module('myApp', ['myApp.services'])
.controller("listObjectControl", ['$scope', '$http', 'DataService', function ($scope, $http, DataService){
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
        var file = $scope.myFile;
        console.log('file is ' );
        console.dir(file);
        var uploadUrl = "/fileUpload";
        fileUpload.uploadFileToUrl(file, uploadUrl);
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
    	  url: '/?path='+selectedObject.FullPath
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
    	  }, function errorCallback(response) {
			  alert(response.data)
    		// called asynchronously if an error occurs
    		// or server returns response with an error status.
    	  });
    }
}]);
