'use strict';


hthServiceApp.controller('DashboardController',
    ['$scope', '$rootScope', '$http', '$location', function($scope, $rootScope, $http, $location) {
    if (typeof ($rootScope.AuthInfo) === 'undefined' || typeof ($rootScope.AuthInfo.Token) === 'undefined' || $rootScope.AuthInfo.Token == '') {
        var token = window.localStorage.getItem("AuthInfo.Token");
        if (token && token != "") {
            $rootScope.AuthInfo = {
                Token: token,
                Message: ""
            }
        } else {
            $location.path('/login');
            return;
        }
    };

    $scope.ScheduleRequestLogs = { FromDate: new Date(), ToDate: new Date(), IsLoading: false, TotalRequest: 0, TotalFailedRequest: 0 };
    $scope.ScheduleRequestLogs.TopRequests = [];
    $scope.ScheduleRequestLogs.TopFailedRequests = [];
    $scope.ScheduleRequestLogs.IsShowChart = false;

    $scope.RequestInfo = [];
    if (typeof ($rootScope.FromDate) != 'undefined') {
        $scope.ScheduleRequestLogs.FromDate = $rootScope.FromDate;
    } 

    if (typeof ($rootScope.ToDate) != 'undefined') {
        $scope.ScheduleRequestLogs.ToDate = $rootScope.ToDate;
    }
    
    if ($scope.ScheduleRequestLogs.FromDate.setHours(0, 0, 0, 0, 0) != $scope.ScheduleRequestLogs.ToDate.setHours(0, 0, 0, 0, 0)) {
        $scope.ScheduleRequestLogs.IsShowChart = true;
    }
    //$scope.ScheduleRequestLogs.FromDate = new Date($scope.ScheduleRequestLogs.ToDate.getFullYear(), $scope.ScheduleRequestLogs.ToDate.getMonth(), 1);
    $scope.GetReport = function () {
        var getReportUrl = URL_SERVICE + '/api/ReportApi/Dashboard/?token=' + $rootScope.AuthInfo.Token + "&fromDate=" + GetStringOfDate($scope.ScheduleRequestLogs.FromDate) + "&toDate=" + GetStringOfDate($scope.ScheduleRequestLogs.ToDate);
        
        $scope.ScheduleRequestLogs.IsLoading = true;
        $http.get(getReportUrl, null).then(
            function (response) {
                $scope.ScheduleRequestLogs.IsLoading = false;
                var responseData = response.data;

                if (responseData.IsSuccess) {
                    $scope.ScheduleRequestLogs.TotalRequest = 0;
                    $scope.ScheduleRequestLogs.TotalFailedRequest = 0;
                    areaDashboard.setData(responseData.Data, $scope.ScheduleRequestLogs.IsShowChart);
                    for (var i = 0; i < responseData.Data.length; i++) {
                        $scope.ScheduleRequestLogs.TotalRequest += responseData.Data[i].NumberOfRequest;
                        $scope.ScheduleRequestLogs.TotalFailedRequest += responseData.Data[i].NumberOfFailedRequest;
                    }
                } else {
                    $location.path('/login');
                }
            },
            function (error) {
                ctrl.ScheduleRequestLogs.IsLoading = false;
                alert(error);
            });
        GetTop();
        GetTop(true);
        GetRequestInfoStatistic("GetAds");
        GetRequestInfoStatistic("UserClickAd");
    };
        
    var areaDashboard = Morris.Line({
        shown: false,
        element: 'area-dashboard',
        data: null,
        xkey: 'CurrentDate',
        ykeys: ['NumberOfRequest', 'NumberOfFailedRequest'],
        labels: ['Schedule Request Logs', 'Schedule Failed Request Logs'],
        pointSize: 2,
        hideHover: 'auto'
    });

    $scope.GetReport();
    $scope.UpdateFilter = function (fromDate, toDate) {
        $scope.ScheduleRequestLogs.FromDate = fromDate;
        $scope.ScheduleRequestLogs.ToDate = toDate;
        $scope.GetReport();
        //save to root
        $rootScope.FromDate = fromDate;
        $rootScope.ToDate = toDate;
    }

    $scope.ShowChart = function () {
        if ($scope.ScheduleRequestLogs.IsShowChart) {
            setTimeout(function () {
                if ($('#area-dashboard-parent').hasClass("ng-hide")) {
                    $scope.ShowChart();
                }
                else {
                    areaDashboard.redraw();
                }
            }, 1000);
        }
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
    $scope.openRequestInfo = function (type) {
        window.open(URL_SERVICE + "/api/ReportApi/RequestInfo/?token=" + $rootScope.AuthInfo.Token + "&type="+type+"&order=Id&desc=true&date=" + GetStringOfDate(new Date()), "_blank");
    };

    $scope.getTotalOfUserClickAds = function (index) {
        var total = 0;
        if (typeof ($scope.RequestInfo['UserClickAd']) == 'undefined' || $scope.RequestInfo['UserClickAd'] == null)
        {
            return total;
        }
        for (var i = 0; i < $scope.RequestInfo['UserClickAd'].length; i++) {
            total += $scope.RequestInfo['UserClickAd'][i].TotalClickOnItems[index];
        }
        return total;
    }

    function GetStringOfDate(date)
    {
        if (typeof (date) != 'object')
        {
            return "";
        }
        var m = date.getMonth() + 1;
        var d = date.getDate();
        return date.getFullYear() + "-" + (m < 10 ? "0" : "") + m + "-" + (d < 10 ? "0" : "") + d;
    }

    function GetTop(isFailedRequest)
    {
        var getReportUrl = URL_SERVICE + '/api/ReportApi/GroupRequest/?token=' + $rootScope.AuthInfo.Token;
        var headers = {
            'Content-Type': 'application/json'
        };
        var filter = {
            NoChannelKey: false,
            NoCurrentDate: true,
            NoDateOn: true,
            NoDeviceId: true,
            NoOpenKey: true,
            NoAppVersion: true,
            Page: 1,
            Size: 10,
            Desc: null,
            OrderField: 'NumberOfRequests',
            FromDate: GetStringOfDate($scope.ScheduleRequestLogs.FromDate),
            ToDate: GetStringOfDate($scope.ScheduleRequestLogs.ToDate)
        };
        if (isFailedRequest == true) {
            getReportUrl = URL_SERVICE + '/api/ReportApi/GroupFailedRequest/?token=' + $rootScope.AuthInfo.Token;
        }
        $scope.ScheduleRequestLogs.IsLoading = true;
        $http.post(getReportUrl, filter, { headers: headers }).then(
            function (response) {
                $scope.ScheduleRequestLogs.IsLoading = false;
                var responseData = response.data;
                if (responseData.IsSuccess) {
                    if (isFailedRequest) {
                        $scope.ScheduleRequestLogs.TopFailedRequests = responseData.Data.ScheduleLogs;
                    } else {
                        $scope.ScheduleRequestLogs.TopRequests = responseData.Data.ScheduleLogs;
                    }
                } else {
                    $location.path('/login');
                }
            },
            function (error) {
                $scope.ScheduleRequestLogs.IsLoading = false;
                alert(error);
            });
    }

    function GetRequestInfoStatistic(type) {
        var getReportUrl = URL_SERVICE + '/api/ReportApi/GetRequestInfoStatistic/?token=' + $rootScope.AuthInfo.Token + "&type=" + type + "&fromDate=" + GetStringOfDate($scope.ScheduleRequestLogs.FromDate) + "&toDate=" + GetStringOfDate($scope.ScheduleRequestLogs.ToDate);

        $scope.ScheduleRequestLogs.IsLoading = true;
        $http.get(getReportUrl).then(
            function (response) {
                $scope.ScheduleRequestLogs.IsLoading = false;
                var responseData = response.data;
                if (responseData.IsSuccess) {
                    $scope.RequestInfo[type] = responseData.Data;
                } else {
                    $location.path('/login');
                }
            },
            function (error) {
                $scope.ScheduleRequestLogs.IsLoading = false;
                alert(error);
            });
    }


}]);
