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
    var ctrl = this;
    if (typeof(ctrl.isFailedRequest) === 'undefined') {
        ctrl.isFailedRequest = false;
    }
    ctrl.ScheduleRequestLogs = { ScheduleLogs: [], Total: 0, NumberOfPages: 0, IsLoading: false };
    ctrl.ScheduleRequestLogs.Filter = {
        NoChannelKey: false,
        NoCurrentDate: false,
        NoDateOn: false,
        Page: 1,
        Size: 10,
        Desc: null,
        OrderField: '',
    };

    ctrl.GetReport = function () {
        var getReportUrl = URL_SERVICE + '/api/ReportApi/GroupRequest/?token=' + $rootScope.AuthInfo.Token;
        var headers = {
            'Content-Type': 'application/json'
        };
        if (ctrl.isFailedRequest=="true") {
            getReportUrl = URL_SERVICE + '/api/ReportApi/GroupFailedRequest/?token=' + $rootScope.AuthInfo.Token;
        }
        ctrl.ScheduleRequestLogs.IsLoading = true;
        $http.post(getReportUrl, ctrl.ScheduleRequestLogs.Filter, { headers: headers }).then(
            function (response) {
                ctrl.ScheduleRequestLogs.IsLoading = false;
                var responseData = response.data;
                if (responseData.IsSuccess) {
                    ctrl.ScheduleRequestLogs.ScheduleLogs = responseData.Data.ScheduleLogs;
                    ctrl.ScheduleRequestLogs.Total = responseData.Data.Total;
                    ctrl.ScheduleRequestLogs.NumberOfPages = responseData.Data.NumberOfPages;
                } else {
                    $location.path('/login');
                }
            },
            function (error) {
                ctrl.ScheduleRequestLogs.IsLoading = false;
                alert(error);
            });
    };

    Morris.Area({
        element: 'area-dashboard',
        data: [
          { y: '2006', a: 100, b: 90 },
          { y: '2007', a: 75, b: 65 },
          { y: '2008', a: 50, b: 40 },
          { y: '2009', a: 75, b: 65 },
          { y: '2010', a: 50, b: 40 },
          { y: '2011', a: 75, b: 65 },
          { y: '2012', a: 100, b: 90 }
        ],
        xkey: 'y',
        ykeys: ['a', 'b'],
        labels: ['Schedule Failed Request Logs', 'Schedule Request Logs']
    });

    //ctrl.GetReport();
}]);
