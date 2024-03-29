﻿'use strict';

hthServiceApp.controller('HomeController',
    ['$scope', '$rootScope', '$http', '$location', '$routeParams',
      function ($scope, $rootScope, $http, $location, $routeParams, $window) {
          if (typeof ($rootScope.AuthInfo) === 'undefined' || typeof ($rootScope.AuthInfo.Token) === 'undefined' || $rootScope.AuthInfo.Token == '') {
              var token = window.localStorage.getItem("AuthInfo.Token");
              if (token && token != "") {
                      $rootScope.AuthInfo = {
                          Token: token,
                          Message:""
                      }
              } else {
                  $location.path('/login');
                  return;
              }
          };

          if (typeof ($rootScope.FromDate) == 'undefined')
          {
            $scope.FromDate = new Date();
          } else {
              $scope.FromDate = $rootScope.FromDate;
          }

          if (typeof ($rootScope.ToDate) == 'undefined') {
              $scope.ToDate = new Date();
          } else {
              $scope.ToDate = $rootScope.ToDate;
          }

          if ($rootScope.AuthInfo.Role == '1') {
              $scope.IsShowAction = true;
          } else {
              $scope.IsShowAction = false;
          }
          $scope.IsTop = false;

          if (typeof ($routeParams.top) != 'undefined')
          {
              $scope.IsTop = true;
          }

          $scope.Logout = function () {
              var logoutUrl = URL_SERVICE + '/api/Account/Logout/?token=' + $rootScope.AuthInfo.Token;
              $http.get(logoutUrl, null, null).then(
                  function (response) {
                      var responseData = response.data;
                      if (responseData.Data.IsSuccess) {
                          window.localStorage.setItem("AuthInfo.Token", "");
                          $rootScope.AuthInfo.Token = "";
                          $location.path('/login');
                      } else {
                          alert(responseData.Data.Message);
                      }
                  },
                  function (error) {
                      alert(responseData.Data.Message);
                  });
          };

          $scope.TriggerRefeshForFailedRequest = false;
          $scope.TriggerRefeshForRequest = false;
          $scope.UpdateFilter = function (fromDate, toDate) {
              $scope.FromDate = fromDate;
              $scope.ToDate = toDate;
              $scope.TriggerRefeshForFailedRequest = !$scope.TriggerRefeshForFailedRequest;
              $scope.TriggerRefeshForRequest = !$scope.TriggerRefeshForRequest;

              //save to root
              $rootScope.FromDate = fromDate;
              $rootScope.ToDate = toDate;
          }
          $scope.openRequestInfo = function (type) {
              window.open(URL_SERVICE + "/api/ReportApi/RequestInfo/?token=" + $rootScope.AuthInfo.Token + "&type="+type+"&order=Id&desc=true&date=" + GetStringOfDate(new Date()), "_blank");
          };
          function GetStringOfDate(date) {
              if (typeof (date) != 'object') {
                  return "";
              }
              var m = date.getMonth() + 1;
              var d = date.getDate();
              return date.getFullYear() + "-" + (m < 10 ? "0" : "") + m + "-" + (d < 10 ? "0" : "") + d;
          }
    }]);