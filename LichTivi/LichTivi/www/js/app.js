// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
angular.module('starter', ['ionic', 'ngSanitize', 'starter.appData','angular.filter', 'starter.appDataProcess', 'pickadate'])
.controller("leftmenuController", function ($scope, $http, $ionicPlatform, $ionicSideMenuDelegate, $ionicScrollDelegate, $ionicPopup, appData, appDataProcess, $ionicModal, $sce){
	//var listChannels = appData.lstChannels;
	// var channel : {id:1, name:"channel 1"};
	// listChannels.push(channel);
	var Message = new Date();
	$scope.predicate="";
	$scope.lstChannels = appData.lstChannels;
	$scope.lstFavoriteChannels = appDataProcess.all();
	if($scope.lstFavoriteChannels==null) $scope.lstFavoriteChannels = [];
	$scope.lstFavoriteChannels = $scope.lstFavoriteChannels.filter(function(e){return e}); 
	$scope.dataSchedule = {id:0, name:"", data: [], message:"", descriptionVN:"", descriptionEN:""};
	$scope.dataSchedule.timeForSearch = Message.getFullYear() +"-"+(Message.getMonth()+1)+"-"+Message.getDate();//'2013-11-26';
	//$scope.dataSchedule.data = $sce.trustAsHtml("<strong>a<\/strong> B\u1e3n");
	$scope.openShcedule =  function ($id, $name, isOpenMenuLeft){
		$scope.dataSchedule.id = $id;
		$scope.dataSchedule.name = $name;
		if(typeof($value) == 'undefined') $scope.dataSchedule.value = "";
		else
			$scope.dataSchedule.value = $value;
		
		if(isOpenMenuLeft)
			$scope.openShceduleGetData(true, false);
		else $scope.openShceduleGetData(false, true);
	};

	$scope.openShceduleGetData =  function (isOpenMenuLeft, isOpenMenuRight){
		$scope.dataSchedule.descriptionVN = "";
		$scope.dataSchedule.descriptionEN = "";
		//$scope.$apply();
		//var linkreqest = "http://www.thanhhung.somee.com/RetrieveData/GetSchedules/?channel=VTV2&date=26-05-2016";
		
		$scope.dataSchedule.data = [];
		$scope.dataSchedule.message = "Đang tải dữ liệu...";
		$ionicScrollDelegate.$getByHandle('ionScrollContentCenter').scrollTop();
		var url = 'http://www.thanhhung.somee.com/RetrieveData/GetSchedules/?';
		$.ajax({
			url: url,
			type: 'GET',
			data: {channel : $scope.dataSchedule.id, date : $scope.getCorrectFormatDate($scope.dataSchedule.timeForSearch,'-')} ,
			header: {'Access-Control-Allow-Origin': "*"},
			dataType: 'json',
			timeout: 25000,
			error: function(){
				$ionicPopup.alert({
					 title: 'Rất tiếc!',
					 template: "Lỗi khi yêu cầu dữ liệu."
				   });
				$scope.dataSchedule.message = "Lỗi khi yêu cầu dữ liệu.";
				if(isOpenMenuLeft) $ionicSideMenuDelegate.toggleLeft(true);
				else if(isOpenMenuRight) $ionicSideMenuDelegate.toggleRight(true);
				//$ionicScrollDelegate.$getByHandle('ionScrollContentCenter').scrollTop();
				$scope.$apply();
			},
			success: function(jsondata){
				if(jsondata != ''){
					$scope.dataSchedule.data = jsondata;//$('#schedule_content').html(jsondata);
					$scope.dataSchedule.message = "";
				}
				else{
					$scope.dataSchedule.message = $('<div/>').html('<font style="width: 100%; padding: 15px; text-align: center; float: left; color: #E67D00">Chưa có lịch phát sóng</font>').html();
				}
				//$ionicScrollDelegate.$getByHandle('ionScrollContentCenter').scrollTop();
				if(isOpenMenuLeft) $ionicSideMenuDelegate.toggleLeft(true);
				$scope.$apply();
			}
		});
		/*$http({
                method: 'GET',
                responseType : 'json',
                url: linkreqest
            }).success(function (data) {
                $scope.dataSchedule.data = JSON.stringify(data," ");
                $ionicPopup.alert({
					 title: linkreqest,
					 template: data
				   });
                }).error(function (responderror) {
					$ionicPopup.alert({
					 title: 'Don\'t eat that!',
					 template: responderror
				   });
            });*/
	};

	$scope.selectedDateForSearch = (new Date()).toLocaleDateString();;
	$scope.txtSearchGroupValue = 11;
	$scope.ResultsOfSearchProgram = {data:[], message:"", explain:""};
	$scope.searchProgramFromVietBao =  function (keyword, group, date){
		if(window.cordova && window.cordova.plugins.Keyboard) {
		  cordova.plugins.Keyboard.close();
		}
		if(typeof(keyword) == 'undefined' || keyword=="") return;
		
		$scope.ResultsOfSearchProgram.data = [];
		$scope.ResultsOfSearchProgram.message = "Đang tìm...";
		$scope.ResultsOfSearchProgram.explain = "Đang tìm...";

		var url = 'http://www.thanhhung.somee.com/RetrieveData/SearchProgram/?';
		$.ajax({
			url: url,
			type: 'GET',
			data: {query : keyword, group : group, date : $scope.getStringDateFromDate(new Date(date))} ,
			header: {'Access-Control-Allow-Origin': "*"},
			timeout: 25000,
			error: function(){
				$ionicPopup.alert({
					 title: 'Rất tiếc!',
					 template: "Lỗi khi yêu cầu dữ liệu."
				   });
				$scope.ResultsOfSearchProgram.message = "Không có dữ liệu";
				$scope.ResultsOfSearchProgram.explain = "Lỗi khi yêu cầu dữ liệu.";
				$scope.$apply();
			},
			success: function(responsedata){
				if(responsedata != ''){
					$scope.ResultsOfSearchProgram.data = responsedata;
					$scope.ResultsOfSearchProgram.message = "";
				}
				
				if($scope.ResultsOfSearchProgram.message == "") 
				{
					$scope.ResultsOfSearchProgram.message="<b style='color:red;'> Không tìm thấy trong các chương trình hôm nay. </b>";
				}

				$scope.ResultsOfSearchProgram.explain = "";
				$scope.$apply();
			}
		});
	};

	$scope.openAbout = function()
	{
		$ionicPopup.alert({
					 title: 'Thông tin sản phẩm',
					 template: "Dữ liệu được lấy từ vietbao.vn và mytvnet.vn. Thông tin hiển thị chỉ mang tính chất tham khảo. Tác giả không chịu bất kỳ trách nhiệm nào từ dữ liệu.<br/>Cảm ơn các bạn đã ủng hộ ứng dụng này!"
				   });
	}
	
	$ionicModal.fromTemplateUrl('channeldetail.html', 
        function(modal) {
            $scope.openDetailChannelModal = modal;
        },
        {
        // Use our scope for the scope of the modal to keep it simple
        scope: $scope

        }
    );
	$ionicModal.fromTemplateUrl('searchprogram.html', 
        function(modal) {
            $scope.openSearchProgramModal = modal;
        },
        {
        // Use our scope for the scope of the modal to keep it simple
        scope: $scope

        }
    );

    $scope.openSearchProgram = function() {
      $scope.openSearchProgramModal.show();
    };

	$scope.getCorrectFormatDate =  function ($stringDate, $sign){
		var res = $stringDate.split("-"); 
		return "" + (res[2].length < 2 ? "0" + res[2] : res[2]) + $sign + (res[1].length < 2 ? "0" + res[1] : res[1]) + $sign + res[0];
	};
	$scope.getStringDateFromDate = function(date){
		if(typeof(date) === undefined) return "";
		var d = date.getDate();
		var m=date.getMonth()+1;
		return (d<10?"0"+d:d)+"-"+(m<10?"0"+m:m)+"-"+date.getFullYear() ;//'26-11-2016';
	}
	$scope.correctGroupName = function(value){
		return value.length < 4? value : value.substring(3);
	}

    $ionicModal.fromTemplateUrl('datemodal.html', 
        function(modal) {
            $scope.datemodal = modal;
        },
        {
        // Use our scope for the scope of the modal to keep it simple
        scope: $scope

        }
    );

    $scope.opendateModal = function() {
      $scope.datemodal.show();
    };
    $scope.closedateModal = function(model, isSelected) {

      $scope.datemodal.hide();
      if(isSelected){
			$scope.dataSchedule.timeForSearch = model;
			$scope.openShceduleGetData(false, false);
		}

    };

    $scope.saveFavoriteChannel = function(channel) {
    	if(channel==null) return;

    	$scope.lstFavoriteChannels.push({id:channel.id, name:channel.name});
	    appDataProcess.save($scope.lstFavoriteChannels);
	  };

	  $scope.removeFavoriteChannel = function(channel) {

    	for(var i =0; i<$scope.lstFavoriteChannels.length; i++)
    	{
    		if($scope.lstFavoriteChannels[i] == null) continue;
	  		if($scope.lstFavoriteChannels[i].id == channel.id)
	  		{ 
	  			$scope.lstFavoriteChannels.splice(i, 1);
    		    appDataProcess.save($scope.lstFavoriteChannels);
    		    break;
			}
		}
	  };

	  $scope.wasFavoriteChannel = function(channelId) {
	  	for(var i =0; i<$scope.lstFavoriteChannels.length; i++)
	  	{
	  		if($scope.lstFavoriteChannels[i] == null) continue;
	  		if($scope.lstFavoriteChannels[i].id == channelId) return true;
	  	}
    	return false;
	  };

	$scope.toggleLeft = function() {
	    $ionicSideMenuDelegate.toggleLeft();
	  };

	 $scope.toggleRight = function() {
	    $ionicSideMenuDelegate.toggleRight();
	  };

	$scope.isSearchChannels = function($data) {
		if($data == null) return false;
		if(typeof($data.nameSearch) == 'undefined' || $data.nameSearch=='') return false;
		return true;
	};
	$scope.keyUpOnsearchChannels = function(event, keyword) {
		if(event.keyCode == 13) $scope.searchChannels(keyword);
	};

	$scope.oldSearchValue = "";
	$scope.searchChannels = function($keyword) {

		if(window.cordova && window.cordova.plugins.Keyboard) {
		  cordova.plugins.Keyboard.close();
		}
		if(typeof($keyword) == 'undefined' || $keyword == $scope.oldSearchValue) return;
	  	if ($keyword != "")
		{
		    var lstSimilarChannels = new Array();
		    var lstSameChannels = new Array();
		    var lstSimilarChannels = new Array();
		    for(var i =0; i< appData.lstChannels.length; i++)
		    {
				var firstString = removeTone($keyword.toLowerCase());
				var secondString = removeTone(appData.lstChannels[i].name.toLowerCase());

				var firstIndexSecond = firstString.indexOf(secondString);
				var secondIndexFirst = secondString.indexOf(firstString);

		    	if( firstIndexSecond > -1)
		    	{
		    		var newObject = {groupName: "   Kết quả: " + $keyword, id:appData.lstChannels[i].id, name : appData.lstChannels[i].name, nameSearch : "<span class='searched'>" + appData.lstChannels[i].name + "</span>"};
			    	
			    	if(secondIndexFirst > -1)
		    		{
			    		lstSameChannels.push(newObject);
			    	}
			    	else 
			    	{
			    		lstSimilarChannels.push(newObject);
			    	}	
			    }else if(secondIndexFirst > -1)
			    	{			    	
			    		var newObject = {groupName: "Kết quả: " + $keyword, id:appData.lstChannels[i].id, name : appData.lstChannels[i].name, nameSearch:""};

			    		var oldName = newObject.name;
			    		newObject.nameSearch = oldName.substring(0, secondIndexFirst)+ "<span class='searched'>" + oldName.substring(secondIndexFirst, secondIndexFirst + firstString.length) + "</span>";
			    		if(secondIndexFirst + firstString.length < secondString.length) newObject.nameSearch +=  oldName.substring(secondIndexFirst + firstString.length);
			    		lstSimilarChannels.push(newObject);
			    	}
		    }
		    $scope.isDisplayGroupChannel = true;
		    $scope.lstChannels = lstSameChannels.concat(lstSimilarChannels);
		}
		else{
			$scope.isDisplayGroupChannel = false;
			$scope.lstChannels = appData.lstChannels;
		} 
		$ionicScrollDelegate.$getByHandle('ionScrollContentLeft').scrollTop();
		$scope.oldSearchValue = $keyword;
	}

	$scope.resetSearchChannel = function(){
		$scope.lstChannels = appData.lstChannels;
		$scope.oldSearchValue = "";
		$scope.isDisplayGroupChannel = false;
		$ionicScrollDelegate.$getByHandle('ionScrollContentLeft').scrollTop();
	}
	$scope.openShcedule("VTV1", "VTV1", true);
/*
	compareString = function(firstString, secondString)
	{
		if( > -1)
			if( > -1) 
				return 0;
			else return 1;
		else if(secondString.indexOf(firstString) > -1) return 2;
		return -1;
	}*/
})
.run(function($ionicPlatform) {
  $ionicPlatform.ready(function() {
    // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
    // for form inputs)
    if(window.cordova && window.cordova.plugins.Keyboard) {
      cordova.plugins.Keyboard.close();
    }
    if(window.StatusBar) {
      StatusBar.styleDefault();
    }

  });
})

	function removeTone(str){
		//code by Minit - www.canthoit.info - 13-05-2009
		str= str.toLowerCase();
		str= str.replace(/à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ/g,"a");
		str= str.replace(/è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ/g,"e");
		str= str.replace(/ì|í|ị|ỉ|ĩ/g,"i");
		str= str.replace(/ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ/g,"o");
		str= str.replace(/ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ/g,"u");
		str= str.replace(/ỳ|ý|ỵ|ỷ|ỹ/g,"y");
		str= str.replace(/đ/g,"d");
		return str;
	}
      function onLoad() {
      if(( /(ipad|iphone|ipod|android)/i.test(navigator.userAgent) )) {
        document.addEventListener('deviceready', initApp, false);
      } else {
        initApp();
        }
      }
      var ad_units = {
        android : {
          banner: 'ca-app-pub-4576847792571626/1841794794',
         }
      };
      var admobid = ad_units.android;
      function initApp() {
        if (! AdMob ) { alert( 'admob plugin not ready' ); return; }
        initAd();
        // display the banner at startup
        AdMob.createBanner( admobid.banner, function(){}, function(data){alert(JSON.stringify(data));} );
      }
      function initAd(){
        var defaultOptions = {
        // publisherId: admobid.banner,
        // interstitialAdId: admobid.interstitial,
         adSize: 'SMART_BANNER',
        // width: integer, // valid when set adSize 'CUSTOM'
        // height: integer, // valid when set adSize 'CUSTOM'
        // overlap: false, // set to true, if hope banner floating over web content, or positioin with XY
        position: AdMob.AD_POSITION.BOTTOM_CENTER,
        // x: integer, // valid when set position to 0 / POS_XY
        // y: integer, // valid when set position to 0 / POS_XY
        isTesting: true, // set to true, to receiving test ad for testing purpose
        // autoShow: true // auto show interstitial ad when loaded, set to false if prepare/show
        };
        AdMob.setOptions( defaultOptions );
      }