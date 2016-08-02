'use strict';


function GridReportController($scope, $rootScope, $http, $location) {
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

    ctrl.TablePaging = function (page) {
        if (page) {
            ctrl.ScheduleRequestLogs.Filter.Page = page;
            ctrl.GetReport();
        }
    };

    ctrl.ApplyColums = function (channelKey, currentDate, dateOn) {
        ctrl.ScheduleRequestLogs.Filter.NoChannelKey = !channelKey;
        ctrl.ScheduleRequestLogs.Filter.NoCurrentDate = !currentDate;
        ctrl.ScheduleRequestLogs.Filter.NoDateOn = !dateOn;
        ctrl.ScheduleRequestLogs.Filter.Page = 1;
        ctrl.GetReport();

    }

    ctrl.OrderColum = function (columName) {
        if (ctrl.ScheduleRequestLogs.Filter.Desc == null) {
            ctrl.ScheduleRequestLogs.Filter.Desc = true;
        } else {
            ctrl.ScheduleRequestLogs.Filter.Desc = !ctrl.ScheduleRequestLogs.Filter.Desc;
        }
        ctrl.ScheduleRequestLogs.Filter.OrderField = columName;
        ctrl.GetReport();
    }

    ctrl.GetReport();
};

hthServiceApp.component('gridReport', {
    templateUrl: '/admin/modules/grid-report/grid-report.tpl.html',
    controller: GridReportController,
    bindings: {
        isFailedRequest: '@'
    },
    bindToController: {
        isFailedRequest: '@'
    }
});