angular.module('myApp', ['myApp.services'])
.controller("listObjectControl", ['$scope', 'DataService', function ($scope, DataService){
	$scope.directories = [];
	$scope.directories = DataService.getDirectories('');
                      
}]);
