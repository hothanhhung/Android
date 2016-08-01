'use strict';

hthServiceApp.controller('HomeController',
    ['$scope', '$rootScope', '$http', '$location',
      function ($scope, $rootScope, $http, $location) {
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
          
          $scope.ScheduleRequestLogs = { ScheduleLogs: [], Total: 0, NumberOfPages: 0, IsLoading: false };
          $scope.ScheduleFailedRequestLogs = { ScheduleLogs: [], Total: 0, NumberOfPages: 0, IsLoading: false };

          $scope.ScheduleRequestLogs.Filter = {
              NoChannelKey: false,
              NoCurrentDate: false,
              NoDateOn: false,
              Page: 1,
              Size: 10,
              Desc: null,
              OrderField: '',
          };
          $scope.ScheduleFailedRequestLogs.Filter = {
              NoChannelKey: false,
              NoCurrentDate: false,
              NoDateOn: false,
              Page: 1,
              Size: 10,
              Desc: null,
              OrderField: '',
          }


          $scope.GetReport = function () {
              var getReportUrl = URL_SERVICE + '/api/ReportApi/GroupRequest/?token=' + $rootScope.AuthInfo.Token;
              var headers = {
                  'Content-Type': 'application/json'
              };
              $scope.ScheduleRequestLogs.IsLoading = true;
              $http.post(getReportUrl, $scope.ScheduleRequestLogs.Filter, { headers: headers }).then(
                  function (response) {
                      $scope.ScheduleRequestLogs.IsLoading = false;
                      var responseData = response.data;
                      if (responseData.IsSuccess) {
                          $scope.ScheduleRequestLogs.ScheduleLogs = responseData.Data.ScheduleLogs;
                          $scope.ScheduleRequestLogs.Total = responseData.Data.Total;
                          $scope.ScheduleRequestLogs.NumberOfPages = responseData.Data.NumberOfPages;
                      } else {
                          $location.path('/login');
                      }
                  },
                  function (error) {
                      $scope.ScheduleRequestLogs.IsLoading = false;
                      alert(error);
                  });
          };

          $scope.GetFailedReport = function () {
              var getReportUrl = URL_SERVICE + '/api/ReportApi/GroupFailedRequest/?token=' + $rootScope.AuthInfo.Token;
              var headers = {
                  'Content-Type': 'application/json'
              };
              $scope.ScheduleFailedRequestLogs.IsLoading = true;
              $http.post(getReportUrl, $scope.ScheduleFailedRequestLogs.Filter, { headers: headers }).then(
                  function (response) {
                      $scope.ScheduleFailedRequestLogs.IsLoading = false;
                      var responseData = response.data;
                      if (responseData.IsSuccess) {
                          $scope.ScheduleFailedRequestLogs.ScheduleLogs = responseData.Data.ScheduleLogs;
                          $scope.ScheduleFailedRequestLogs.Total = responseData.Data.Total;
                          $scope.ScheduleFailedRequestLogs.NumberOfPages = responseData.Data.NumberOfPages;
                      } else {
                          $location.path('/login');
                      }
                  },
                  function (error) {
                      $scope.ScheduleFailedRequestLogs.IsLoading = false;
                      alert(error);
                  });
          };

          $scope.TablePaging = function (page, isFailedRequest) {
              if (page) {
                  if (isFailedRequest) {
                      $scope.ScheduleFailedRequestLogs.Filter.Page = page;
                      $scope.GetFailedReport();
                  } else {
                      $scope.ScheduleRequestLogs.Filter.Page = page;
                      $scope.GetReport();
                  }
              }
          };

          $scope.ApplyColums = function (channelKey, currentDate, dateOn, isFailedRequest) {
              if (isFailedRequest) {
                  $scope.ScheduleFailedRequestLogs.Filter.NoChannelKey = !channelKey;
                  $scope.ScheduleFailedRequestLogs.Filter.NoCurrentDate = !currentDate;
                  $scope.ScheduleFailedRequestLogs.Filter.NoDateOn = !dateOn;
                  $scope.ScheduleFailedRequestLogs.Filter.Page = 1;
                  $scope.GetFailedReport();
              } else {
                  $scope.ScheduleRequestLogs.Filter.NoChannelKey = !channelKey;
                  $scope.ScheduleRequestLogs.Filter.NoCurrentDate = !currentDate;
                  $scope.ScheduleRequestLogs.Filter.NoDateOn = !dateOn;
                  $scope.ScheduleRequestLogs.Filter.Page = 1;
                  $scope.GetReport();
              }
          }

          $scope.OrderColum = function (columName, isFailedRequest) {
              if (isFailedRequest) {
                  if ($scope.ScheduleFailedRequestLogs.Filter.Desc == null)
                  {
                      $scope.ScheduleFailedRequestLogs.Filter.Desc = true;
                  } else {
                      $scope.ScheduleFailedRequestLogs.Filter.Desc = !$scope.ScheduleFailedRequestLogs.Filter.Desc;
                  }
                  $scope.ScheduleFailedRequestLogs.Filter.OrderField = columName;
                  $scope.GetFailedReport();
              } else {
                  if ($scope.ScheduleRequestLogs.Filter.Desc == null) {
                      $scope.ScheduleRequestLogs.Filter.Desc = true;
                  } else {
                      $scope.ScheduleRequestLogs.Filter.Desc = !$scope.ScheduleRequestLogs.Filter.Desc;
                  }
                  $scope.ScheduleRequestLogs.Filter.OrderField = columName;
                  $scope.GetReport();
              }
          }

          $scope.GetReport();
          $scope.GetFailedReport();
    }]);