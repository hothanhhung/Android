angular.module('myApp', ['myApp.services','myApp.directives','ngBootbox','ui.bootstrap'])
.controller("listObjectControl", ['$scope', '$http', 'DataService','$ngBootbox','$modal', function ($scope, $http, DataService, $ngBootbox, $modal){
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
			// var uploadUrl = "/?api=upload&path="+$scope.currentPath;
			// for (var i = 0, f; f = files[i]; i++) {
				// var fd = new FormData();
				// fd.append('file', f);
				// $http.post(uploadUrl, fd, {
					// transformRequest: angular.identity,
					// headers: {'Content-Type': undefined}
				// })
				// .success(function(respone){
				// })
				// .error(function(respone){
					// alert(respone);
				// });
			// }
			
			var modalInstance = $modal.open({
			  animation: $scope.animationsEnabled,
			  templateUrl: 'uploadingFiles.html',
			  controller: 'ModalUploadingFilesCtrl',
			  backdrop : 'static',
			  keyboard :false,
			  resolve: {
				uploadingFiles: function () {
				  return $scope.uploadingFiles;
				},
				currentPath: function () {
				  return $scope.currentPath;
				}
			  }
			});

			modalInstance.result.then(function () {
				alert('close');
			}, function () {
			  alert('Modal dismissed at: ' + new Date());
			});
		  
		  
			//fileUpload.uploadFileToUrl(files, uploadUrl);
		}
    };

	$scope.deleteItem = function(selectedObject){
		$ngBootbox.confirm('Are you sure you want to delete?')
            .then(function() {
				$http({
				  method: 'GET',
				  url: '/?api=delete&path='+selectedObject.FullPath
				}).then(function successCallback(response) {
					alert(response.data);
				  }, function errorCallback(response) {
					  alert(response.data);
				  });
            }, function() {
                //alert('Confirm dismissed!');
            });
			
    };
	
	$scope.renameItem = function(selectedObject, isBack){
			bootbox.prompt({
			  title: "Please type new name!",
			  value: selectedObject.Name,
			  callback: function(result) {
				  if(result!=null){
					  $http({
						  method: 'GET',
						  url: '/?api=rename&path='+selectedObject.FullPath+'&newName='+result
						}).then(function successCallback(response) {
							alert(response.data);
						  }, function errorCallback(response) {
							  alert(response.data);
						  });
				  }
			  }
			});
			return;
		/*
		$ngBootbox.prompt('Please type new name!')
            .then(function(result) {
				$http({
				  method: 'GET',
				  url: '/?api=rename&path='+selectedObject.FullPath+'&newName='+result
				}).then(function successCallback(response) {
					alert(response.data);
				  }, function errorCallback(response) {
					  alert(response.data);
				  });
            }, function() {
                //alert('Prompt dismissed!');
            });
			*/
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
		//$scope.dataInfo.directories = DataService.getDirectories('/');
		getDirectories(selectedObject, isBack);
    };
	
	$scope.removeUploadedFile = function($event, file){
		$event.stopPropagation();
		if($scope.uploadingFiles.length>0){
			var index = $scope.uploadingFiles.indexOf(file);
			if(index!=-1){
				$scope.uploadingFiles.splice(index, 1);
			}
		}
    };

    function getDirectories(selectedObject, isBack){
	//$scope.dataInfo.directories = DataService.getDirectories('/');
	//return;
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
			  alert(response.data);
    		// called asynchronously if an error occurs
    		// or server returns response with an error status.
    	  });
    }
}]).controller('ModalUploadingFilesCtrl', function ($scope, $http, $uibModalInstance, uploadingFiles, currentPath) {
	$scope.currentPath = currentPath;
  $scope.displayButton = false;
  $scope.uploadingFiles = uploadingFiles;
  $scope.uploadingInfos = new Array(uploadingFiles.length);
	for (var i = 0, f; f = uploadingFiles[i]; i++) {
		uploadProcess(f, i)
	}

  $scope.ok = function () {
    $uibModalInstance.close();
  };

  
  function uploadProcess(f, index)
  {
	var uploadUrl = "/?api=upload&path="+$scope.currentPath;
	$scope.uploadingInfos[index] = {file:f, status:0, message:""};
		var fd = new FormData();
		fd.append('file', f);
		$http.post(uploadUrl, fd, {
			transformRequest: angular.identity,
			headers: {'Content-Type': undefined}
		})
		.success(function(respone){
			$scope.uploadingInfos[index].status = 1;
			$scope.uploadingInfos[index].message = 'successfully';
			updateDisplayButtonValue();
		})
		.error(function(respone){
			$scope.uploadingInfos[index].status = 2;
			$scope.uploadingInfos[index].message = respone;
			updateDisplayButtonValue();
		});
  }
  
  function updateDisplayButtonValue()
  {
	for (var i = 0, info; info = $scope.uploadingInfos[i]; i++) {
		if(info.status == 0){
			$scope.displayButton = false;
			return;
		}
	}
	$scope.displayButton = true;
  }
});