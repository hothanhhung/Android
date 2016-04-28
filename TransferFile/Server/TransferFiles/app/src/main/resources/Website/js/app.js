angular.module('myApp', ['myApp.services','myApp.directives','ngBootbox','ui.bootstrap','ngMaterial'])
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
	$scope.authenticationData={
		AllowDownload:true,
		AllowUpload:true,
		AllowRename:true,
		AllowDelete:true,
		EnableKey:false,
		Token:'',
		Message:''
	};
	
	$scope.UISizes=
		[
			{
				IconSize:"30px",
				IconActionSize:"20px",
				ImageSize:"50px",
				BoxSize:"50px",
				ItemBoxHeight:"100px",
				ItemBoxWidth:"80px"
			},
			{
				IconSize:"40px",
				IconActionSize:"25px",
				ImageSize:"60px",
				BoxSize:"60px",
				ItemBoxHeight:"110px",
				ItemBoxWidth:"90px"
			},
			{
				IconSize:"50px",
				IconActionSize:"25px",
				ImageSize:"70px",
				BoxSize:"70px",
				ItemBoxHeight:"120px",
				ItemBoxWidth:"100px"
			},
			{
				IconSize:"60px",
				IconActionSize:"30px",
				ImageSize:"80px",
				BoxSize:"80px",
				ItemBoxHeight:"130px",
				ItemBoxWidth:"110px"
			},
			{
				IconSize:"70px",
				IconActionSize:"30px",
				ImageSize:"90px",
				BoxSize:"90px",
				ItemBoxHeight:"140px",
				ItemBoxWidth:"120px"
			},
			{
				IconSize:"70px",
				IconActionSize:"35px",
				ImageSize:"110px",
				BoxSize:"110px",
				ItemBoxHeight:"160px",
				ItemBoxWidth:"140px"
			},
			{
				IconSize:"70px",
				IconActionSize:"35px",
				ImageSize:"130px",
				BoxSize:"130px",
				ItemBoxHeight:"180px",
				ItemBoxWidth:"160px"
			}
		];
	$scope.UISize = $scope.UISizes[0];
	
	//$scope.dataInfo.directories = DataService.getDirectories('/');
	//getDirectories($scope.rootPath);
	
	$scope.login = function(key){
		$http({
			  method: 'GET',
			  url: '/?api=login&key='+key
			}).then(function successCallback(response) {
				if(response.data!=""){
					$scope.authenticationData.Token = response.data;
					getDirectories($scope.rootPath);
				}
				else{
					$scope.authenticationData.Message = 'Login fail';
				}
			  }, function errorCallback(response) {
				  $scope.authenticationData.Message = 'Error: '+respone.data;
			  });
	}
	$scope.login("");
	
	$scope.updateUISize = function(uiIndex)
	{
		var index = 0;
		if(uiIndex)
		{
			if(uiIndex > 0 && uiIndex < $scope.UISizes.length) {
				index = uiIndex - 1
			}else if( uiIndex >= $scope.UISizes.length) {
				index = $scope.UISizes.length - 1;
			}
		}
		
		$scope.UISize = $scope.UISizes[index];
	}
	
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
				},
				tokenAuth: function () {
				  return genAuth();
				}
			  }
			});

			modalInstance.result.then(function () {
				$scope.uploadingFiles=[];
				refreshCurrentDirectories();
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
				  url: '/?'+ genAuth() + '&api=delete&path='+selectedObject.FullPath
				}).then(function successCallback(response) {
					alertWithCallback(response.data, refreshCurrentDirectories);
				  }, function errorCallback(response) {
					  alertWithCallback(response.data);
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
						  url: '/?'+ genAuth() + '&api=rename&path='+selectedObject.FullPath+'&newName='+result
						}).then(function successCallback(response) {
							alertWithCallback(response.data, refreshCurrentDirectories);
						  }, function errorCallback(response) {
							  alertWithCallback(response.data);
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
	
	$scope.downItem = function(selectedObject){
		alert('down');
	}
	$scope.getFile = function(selectedObject){
		if($scope.authenticationData.AllowRename) 
		{
			 // var element = angular.element('<a/>');
							 // element.attr({
								 // href: '/?api=get&path='+selectedObject.FullPath,
								 // target: '_blank',
								 // download: selectedObject.Name
							 // })[0].click();
			window.open('/?'+ genAuth() + '&api=get&path='+selectedObject.FullPath, "_blank");
		}
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
	
	function getAuth()
	{
		$http({
			  method: 'GET',
			  url: '/?'+ genAuth() + '&api=auth'
			}).then(function successCallback(response) {
				$scope.authenticationData.AllowDownload = response.data.AllowDownload;
				$scope.authenticationData.AllowUpload = response.data.AllowUpload;
				$scope.authenticationData.AllowRename = response.data.AllowRename;
				$scope.authenticationData.AllowDelete = response.data.AllowDelete;				
			  }, function errorCallback(response) {
				  
			  });
	}
	
	function genAuth()
	{
		return 'token='+ $scope.authenticationData.Token;
	}
	function alertWithCallback(message, callback){
		$ngBootbox.alert(message)
		.then(function() {
			if(callback){
				callback();
			}
		});
	}

	function refreshCurrentDirectories(){
		if($scope.parentPaths.length>0)
		{
			getDirectories($scope.parentPaths.pop(), false);
		}
	}
	
    function getDirectories(selectedObject, isBack){
	//$scope.dataInfo.directories = DataService.getDirectories('/');
	//return;
	getAuth();					
    $http({
    	  method: 'GET',
    	  url: '/?'+ genAuth() + '&api=browser&path='+selectedObject.FullPath
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
}]).controller('ModalUploadingFilesCtrl', function ($scope, $http, $uibModalInstance, uploadingFiles, currentPath, tokenAuth) {
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
	var uploadUrl = '/?'+tokenAuth+'&api=upload&path='+$scope.currentPath;
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