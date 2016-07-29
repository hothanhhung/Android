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
          
          $scope.ScheduleRequestLogs = { ScheduleLogs: [], Total: 0, NumberOfPages: 0 };
          $scope.ScheduleFailedRequestLogs = { ScheduleLogs: [], Total: 0, NumberOfPages: 0 };

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
              $http.post(getReportUrl, $scope.ScheduleRequestLogs.Filter, { headers: headers }).then(
                  function (response) {
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
                      alert(error);
                  });
          };

          $scope.GetFailedReport = function () {
              var getReportUrl = URL_SERVICE + '/api/ReportApi/GroupFailedRequest/?token=' + $rootScope.AuthInfo.Token;
              var headers = {
                  'Content-Type': 'application/json'
              };
              $http.post(getReportUrl, $scope.ScheduleFailedRequestLogs.Filter, { headers: headers }).then(
                  function (response) {
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

          $scope.GetReport();
          $scope.GetFailedReport();
    }]);