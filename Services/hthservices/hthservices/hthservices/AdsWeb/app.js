// Define the `phonecatApp` module
var hthAdsApp = angular.module('hthAdsApp', [
  'ngMaterial'
]);
var URL_SERVICE = "";



hthAdsApp.controller('AdsController', function PhoneListController($scope, $http) {
    $scope.RequestInfo={
        Country:"EN",
        OS:"android",
        Device:"",
        Open:"",
        Version:"",
        Package:""
    }
    $scope.Ads = {};
    $scope.Message = "";

    $scope.GetAds = function()
    {
        var getAdsUrl = URL_SERVICE + '/api/ads/getads/?country=' + $scope.RequestInfo.Country + '&os=' + $scope.RequestInfo.OS + '&device=' + $scope.RequestInfo.Device + '&open=' + $scope.RequestInfo.Open + '&version=' + $scope.RequestInfo.Version + '&package=' + $scope.RequestInfo.Package;
        $scope.Message = "Loading...";
        $http.get(getAdsUrl, null).then(
            function (response) {
                var responseData = response.data;
                if (responseData.IsSuccess) {
                    $scope.Ads = responseData.Data;
                    $scope.Message = "";
                } else {
                    $scope.Message = "Error when loading ads";
                }
            },
            function (error) {
                $scope.Message = "Error when connect to sever";
            });
    }

    $scope.UserClickAd = function(link)
    {
        var getAdsUrl = URL_SERVICE + '/api/ads/userclickad/?country=' + $scope.RequestInfo.Country + '&os=' + $scope.RequestInfo.OS + '&info=' + escape(link) + '&device=' + $scope.RequestInfo.Device + '&open=' + $scope.RequestInfo.Open + '&version=' + $scope.RequestInfo.Version + '&package=' + $scope.RequestInfo.Package;
        $http.get(getAdsUrl, null).then(
            function (response) {               
            },
            function (error) {
            });
        window.open(link, "_blank");
    }

    $scope.GetAds();
});