// Define the `phonecatApp` module
var hthAdsApp = angular.module('hthAdsApp', [
  'ngMaterial'
]);
hthAdsApp.config(['$locationProvider', function ($locationProvider) {
    $locationProvider.html5Mode(true);
}]);
var URL_SERVICE = "";



hthAdsApp.controller('AdsController', function PhoneListController($scope, $http, $location) {
    var search = $location.search();
    var req = {};
    jQuery.each(search, function (key, value) {
        req[key.toLowerCase()] = value;
    });
    $scope.RequestInfo = {
        Country: req.country ? req.country : 'VN',
        OS: req.os ? req.os : 'android',
        Device: req.device ? req.device : '',
        Open: req.open ? req.open : '',
        Version: req.version ? req.version : '',
        Package: req.package ? req.package : ''
    }
    $location.search();
    $scope.Ads = {};
    $scope.Message = "";
    $scope.IsLoading = false;

    $scope.GetAds = function()
    {
        var getAdsUrl = URL_SERVICE + '/api/ads/getads/?country=' + $scope.RequestInfo.Country + '&os=' + $scope.RequestInfo.OS + '&device=' + $scope.RequestInfo.Device + '&open=' + $scope.RequestInfo.Open + '&version=' + $scope.RequestInfo.Version + '&package=' + $scope.RequestInfo.Package;
        $scope.Message = "";
        $scope.IsLoading = true;

        $http.get(getAdsUrl, null).then(
            function (response) {
                var responseData = response.data;
                if (responseData.IsSuccess) {
                    $scope.Ads = responseData.Data;
                    $scope.Message = "";
                } else {
                    $scope.Message = "Error when loading ads";
                }
                    $scope.IsLoading = false;
            },
            function (error) {
                $scope.IsLoading = false;
                $scope.Message = "Error when connect to sever";
            });
    }

    $scope.UserClickAd = function(link)
    {
        if (typeof (link) != 'undefined' && link != null && link!='') {
            var getAdsUrl = URL_SERVICE + '/api/ads/userclickad/?country=' + $scope.RequestInfo.Country + '&os=' + $scope.RequestInfo.OS + '&info=' + escape(link) + '&device=' + $scope.RequestInfo.Device + '&open=' + $scope.RequestInfo.Open + '&version=' + $scope.RequestInfo.Version + '&package=' + $scope.RequestInfo.Package;
            $http.get(getAdsUrl, null).then(
                function (response) {               
                },
                function (error) {
                });
            window.open(link, "_blank");
        }
    }

    $scope.GetAds();
});