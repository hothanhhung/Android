angular.module('myApp', ['myApp.services'])
.controller("listObjectControl", ['$scope', 'DataService', function ($scope, DataService){
	$scope.currentPath = '/';
	$scope.parentPath = '';
	$scope.dataInfo = DataService.getDirectories('');
	
    $scope.uploadFile = function(){
        var file = $scope.myFile;
        console.log('file is ' );
        console.dir(file);
        var uploadUrl = "/fileUpload";
        fileUpload.uploadFileToUrl(file, uploadUrl);
    };

	$scope.getDirectories = function(path){
		alert(path);
		$scope.parentPath = $scope.currentPath;
		$scope.currentPath = path;
       // $scope.directories = DataService.getDirectories('');
    };
}]);
