// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the title of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
angular.module('starter', ['ngRoute', 'ionic', 'ngSanitize', 'starter.appData', 'starter.appDataProcess'])
.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/stories/:categoryId', {
        templateUrl: 'stories.html',
        controller: 'storiesController'
      }).
      when('/categories', {
        templateUrl: 'categories.html',
        controller: 'categoriesController'
      }).
      otherwise({
        redirectTo: '/categories'
      });
  }])
.controller("categoriesController", function ($scope, $routeParams, $location, $ionicPlatform,  $ionicScrollDelegate, appData){
	$scope.lstCategories = appData.lstCategories;
	$scope.go = function ( path ) {
		
		$location.path( path );
	};
})
.controller("storiesController", function ($scope, $routeParams, $location, $http, $ionicPlatform, $ionicSideMenuDelegate, $ionicPopup, $ionicScrollDelegate, appData, appDataProcess, $ionicModal, $sce){
	
	var lstCategories = appData.lstCategories;
	$scope.category = {};
	for(var i =0 ; i< lstCategories.length; i++)
		if(lstCategories[i].id == $routeParams.categoryId)
		{
			$scope.category = lstCategories[i];
			break;
		} 
	var listStoriesInCategory;

	var lastId = appDataProcess.getLastId($scope.category.id)

	
	$.getJSON($scope.category.link, function(jsondata) {
    	
		listStoriesInCategory = jsondata;// appData.getStories("./truyenviet/kho-tang-truyen-co-tich-viet-nam/kho-tang-truyen-co-tich-viet-nam.json");
		$scope.predicate="";
		$scope.lstStories = jsondata;

		var selectedStory = 0;

		if(lastId != "")
			for(var i =0 ; i < listStoriesInCategory.length; i++)
			{
				if(listStoriesInCategory[i].id==lastId)
				{
					selectedStory = i;
				}
			}
		if(listStoriesInCategory) $scope.openStory(listStoriesInCategory[selectedStory].id, listStoriesInCategory[selectedStory].title, listStoriesInCategory[selectedStory].link, true);
		
	}) .done(function() {
		console.log( "second success" );
		})
		.fail(function(a) {
		console.log( "error"+a );
		})
		.always(function() {
		console.log( "complete" );
		});

	$scope.lstFavoriteStories = appDataProcess.all();	
	
	var today = new Date();
	if($scope.lstFavoriteStories==null) $scope.lstFavoriteStories = [];
	$scope.lstFavoriteStories = $scope.lstFavoriteStories.filter(function(e){return e}); 
	$scope.dataSchedule = {id:0, title:"", link: "",data: ""};
	$scope.dataSchedule.timeForSearch = today.getFullYear() +"-"+(today.getMonth()+1)+"-"+today.getDate();//'2013-11-26';

	$scope.goBack = function () {
	  $location.path("#/categories");
	};

	//$scope.dataSchedule.data = $sce.trustAsHtml("<strong>a<\/strong> B\u1e3n");
	$scope.openStory =  function (id, title, link, isOpenMenuLeft, isNotFavoriteStory){
		$scope.dataSchedule.id = id;
		$scope.dataSchedule.title = title;
		$scope.dataSchedule.link = link;

		if (typeof isNotFavoriteStory === 'undefined') 
		{
			isNotFavoriteStory = true;
		}

		if (typeof isOpenMenuLeft === 'undefined') 
		{
			$scope.openStoryGetData(false, false, isNotFavoriteStory);
		}
		else if(isOpenMenuLeft)
			$scope.openStoryGetData(true, false, isNotFavoriteStory);
		else $scope.openStoryGetData(false, true, isNotFavoriteStory);
	};

	$scope.nextStory = null;
	$scope.backStory = null;

	$scope.openStoryGetData =  function (isOpenMenuLeft, isOpenMenuRight, isNotFavoriteStory){
		//$scope.$apply();
		//var linkreqest = 'http://www.mytvnet.vn/module/ajax/ajax_get_schedule.php?channelId=' + $scope.dataSchedule.id + '&dateSchedule=' + $scope.getCorrectFormatDate($scope.dataSchedule.timeForSearch,'/');
		//var linkreqest = "http://localhost:8100/modal.html";
		
		$scope.nextStory = null;
		$scope.backStory = null;
		if (typeof isNotFavoriteStory === 'undefined') 
		{
			isNotFavoriteStory = true;
		}
		
		if(listStoriesInCategory!=null && isNotFavoriteStory)
		{
			for(var i =0; i<listStoriesInCategory.length; i++)
		  	{
		  		if(listStoriesInCategory[i] == null) continue;
		  		if(listStoriesInCategory[i].id == $scope.dataSchedule.id)
		  		{
		  			if(i==0) $scope.backStory = null;
		  			else $scope.backStory = listStoriesInCategory[i - 1];
		  			if(i > listStoriesInCategory.length - 2) $scope.nextStory = null;
		  			else $scope.nextStory = listStoriesInCategory[i + 1];
		  		}
		  	}
		}

		$scope.dataSchedule.data = "Đang tải dữ liệu...";
		var url = 'truyenviet/' + $scope.dataSchedule.link;
		$.ajax({
			url: url,
			type: 'GET',
			timeout: 10000,
			error: function(){
				$ionicPopup.alert({
					 title: 'Rất tiếc!',
					 template: "Lỗi khi yêu cầu dữ liệu."
				   });
				$scope.dataSchedule.data = "Lỗi khi yêu cầu dữ liệu.";
				if(isOpenMenuLeft) $ionicSideMenuDelegate.toggleLeft(true);
				else if(isOpenMenuRight) $ionicSideMenuDelegate.toggleRight(true);
				$ionicScrollDelegate.$getByHandle('ionScrollContentStory').scrollTop();
			},
			success: function(data){
				if(data != ''){
					$scope.dataSchedule.data = $.trim(data).split("\n").join("<br/>");;//data.replace(/\t\n/g,"<br/>");//$('#schedule_content').html(jsondata);
					appDataProcess.saveLastId($scope.category.id, $scope.dataSchedule.id);
				}
				else{
					$scope.dataSchedule.data = $('<div/>').html('<font style="width: 100%; padding: 15px; text-align: center; float: left; color: #E67D00">Chưa có lịch phát sóng</font>').html();
				}
				if(isOpenMenuLeft) $ionicSideMenuDelegate.toggleLeft(true);
				$ionicScrollDelegate.$getByHandle('ionScrollContentStory').scrollTop();
			}
		});
	};

	$scope.getCorrectFormatDate =  function ($stringDate, $sign){
		var res = $stringDate.split("-"); 
		return "" + (res[2].length < 2 ? "0" + res[2] : res[2]) + $sign + (res[1].length < 2 ? "0" + res[1] : res[1]) + $sign + res[0];
	};
    
    $scope.saveFavoriteStory = function(story) {
    	if(story==null) return;

    	$scope.lstFavoriteStories.push({id:story.id, title:story.title, link:story.link});
	    appDataProcess.save($scope.lstFavoriteStories);
	  };

	  $scope.removeFavoriteStory = function(story) {

    	for(var i =0; i<$scope.lstFavoriteStories.length; i++)
    	{
    		if($scope.lstFavoriteStories[i] == null) continue;
	  		if($scope.lstFavoriteStories[i].id == story.id)
	  		{ 
	  			$scope.lstFavoriteStories.splice(i, 1);
    		    appDataProcess.save($scope.lstFavoriteStories);
    		    break;
			}
		}
	  };

	  $scope.wasFavoriteStory = function(storyId) {
	  	for(var i =0; i<$scope.lstFavoriteStories.length; i++)
	  	{
	  		if($scope.lstFavoriteStories[i] == null) continue;
	  		if($scope.lstFavoriteStories[i].id == storyId) return true;
	  	}
    	return false;
	  };

	$scope.toggleLeft = function() {
	    $ionicSideMenuDelegate.toggleLeft();
	  };

	 $scope.toggleRight = function() {
	    $ionicSideMenuDelegate.toggleRight();
	  };

	$scope.isSearchStories = function($data) {
		if($data == null) return false;
		if(typeof($data.titleSearch) == 'undefined' || $data.titleSearch=='') return false;
		return true;
	};
	$scope.keyUpOnSearchStories = function(event, keyword) {
		if(event.keyCode == 13) $scope.searchStories(keyword);
	};

	$scope.oldSearchValue = "";
	$scope.searchStories = function($keyword) {

		if(window.cordova && window.cordova.plugins.Keyboard) {
		  cordova.plugins.Keyboard.close();
		}
		
		if($keyword == $scope.oldSearchValue) return;
	  	if ($keyword != "")
		{
		    var lstSimilarChannels = new Array();
		    var lstSameChannels = new Array();
		    var lstSimilarChannels = new Array();
		    for(var i =0; i< listStoriesInCategory.length; i++)
		    {
				var firstString = removeTone($keyword.toLowerCase());
				var secondString = removeTone(listStoriesInCategory[i].title.toLowerCase());

				var firstIndexSecond = firstString.indexOf(secondString);
				var secondIndexFirst = secondString.indexOf(firstString);

		    	if( firstIndexSecond > -1)
		    	{
		    		var newObject = {id:listStoriesInCategory[i].id, title : listStoriesInCategory[i].title, link : listStoriesInCategory[i].link, titleSearch : "<span class='searched'>" + listStoriesInCategory[i].title + "</span>"};
			    	
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
			    		var newObject = {id:listStoriesInCategory[i].id, title : listStoriesInCategory[i].title, link : listStoriesInCategory[i].link, titleSearch:""};

			    		var oldtitle = newObject.title;
			    		newObject.titleSearch = oldtitle.substring(0, secondIndexFirst)+ "<span class='searched'>" + oldtitle.substring(secondIndexFirst, secondIndexFirst + firstString.length) + "</span>";
			    		if(secondIndexFirst + firstString.length < secondString.length) newObject.titleSearch +=  oldtitle.substring(secondIndexFirst + firstString.length);
			    		lstSimilarChannels.push(newObject);
			    	}
		    }
		    $scope.lstStories = lstSameChannels.concat(lstSimilarChannels);
		}
		else $scope.lstStories = listStoriesInCategory;
		$scope.oldSearchValue = $keyword;

		$ionicScrollDelegate.$getByHandle('ionScrollListStories').scrollTop();
	}
})
.run(function($ionicPlatform) {
  $ionicPlatform.ready(function() {
    // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
    // for form inputs)
    if(window.cordova && window.cordova.plugins.Keyboard) {
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
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
          banner: 'ca-app-pub-4576847792571626/2668419599',
          interstitial:'ca-app-pub-4576847792571626/2852383194'
         }
      };
      var admobid = ad_units.android;
      function initApp() {
        if (! AdMob ) { alert( 'admob plugin not ready' ); return; }
        initAd();
        // display the banner at startup
        AdMob.createBanner( admobid.banner, function(){}, function(data){alert(JSON.stringify(data));} );
		//if((new Date()).getSeconds()%2 == 1)
		{
			AdMob.prepareInterstitial( {adId:admobid.interstitial, autoShow:false} );
		}
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
        isTesting: false, // set to true, to receiving test ad for testing purpose
        // autoShow: true // auto show interstitial ad when loaded, set to false if prepare/show
        };
        AdMob.setOptions( defaultOptions );
      }
	  
	  function showInterstitial()
	  {
		if(AdMob){
		//	AdMob.isInterstitialReady(function(ready){ 
			//	if(ready)
			{
					AdMob.showInterstitial();
				}
		//	});
		}
		
	  }