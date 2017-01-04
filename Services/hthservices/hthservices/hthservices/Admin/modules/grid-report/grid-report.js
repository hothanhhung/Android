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
    if (typeof (ctrl.fromDate) === 'undefined') {
        ctrl.fromDate = new Date();
    }
    if (typeof (ctrl.toDate) === 'undefined') {
        ctrl.toDate = new Date();
    }
    if (typeof (ctrl.isShowAction) === 'undefined') {
        ctrl.isShowAction = true;
    }

    var initByTop = false;
    if (ctrl.isTop)
    {
        initByTop = true;
    }

    ctrl.ScheduleRequestLogs = { ScheduleLogs: [], Total: 0, NumberOfPages: 0, IsLoading: false };
    ctrl.ScheduleRequestLogs.Filter = {
        NoChannelKey: false,
        NoCurrentDate: initByTop,
        NoDateOn: initByTop,
        NoDeviceId: initByTop,
        NoOpenKey: initByTop,
        NoAppVersion: initByTop,
        Page: 1,
        Size: 30,
        Desc: true,
        OrderField: initByTop?'NumberOfRequests':'CurrentDate',
        FromDate: '',
        ToDate: '',
        Conditions: []
    };

    ctrl.AddCondition = function(key, value)
    {
        ctrl.ScheduleRequestLogs.Filter.Conditions.push({ Key: key, Value: value });
        ctrl.GetReport();
    }

    ctrl.RemoveCondition = function (condition) {
        ctrl.ScheduleRequestLogs.Filter.Conditions.pop(condition);
        ctrl.GetReport();
    }

    ctrl.GetReport = function () {
        var getReportUrl = URL_SERVICE + '/api/ReportApi/GroupRequest/?token=' + $rootScope.AuthInfo.Token;
        var headers = {
            'Content-Type': 'application/json'
        };
        if (ctrl.isFailedRequest=="true") {
            getReportUrl = URL_SERVICE + '/api/ReportApi/GroupFailedRequest/?token=' + $rootScope.AuthInfo.Token;
        }
        ctrl.ScheduleRequestLogs.Filter.FromDate = GetStringOfDate(ctrl.fromDate);
        ctrl.ScheduleRequestLogs.Filter.ToDate = GetStringOfDate(ctrl.toDate);
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

    ctrl.ApplyColums = function (channelKey, currentDate, dateOn, deviceId, openKey, appVersion) {
        ctrl.ScheduleRequestLogs.Filter.NoChannelKey = !channelKey;
        ctrl.ScheduleRequestLogs.Filter.NoCurrentDate = !currentDate;
        ctrl.ScheduleRequestLogs.Filter.NoDateOn = !dateOn;
        ctrl.ScheduleRequestLogs.Filter.NoDeviceId = !deviceId;
        ctrl.ScheduleRequestLogs.Filter.NoOpenKey = !openKey;
        ctrl.ScheduleRequestLogs.Filter.NoAppVersion = !appVersion;
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

    ctrl.Delete = function (scheduleLog) {
        if (confirm("Do you want to delete?")) {
            var deleteReportUrl = URL_SERVICE + '/api/ReportApi/Delete/?token=' + $rootScope.AuthInfo.Token + '&isFailed=' + ctrl.isFailedRequest;
            var headers = {
                'Content-Type': 'application/json'
            };
            ctrl.ScheduleRequestLogs.IsLoading = true;
            $http.post(deleteReportUrl, scheduleLog, { headers: headers }).then(
                function (response) {
                    ctrl.ScheduleRequestLogs.IsLoading = false;
                    var responseData = response.data;
                    if (responseData.IsSuccess) {
                        alert(responseData.Data.Message);
                        ctrl.GetReport();

                    } else {
                        $location.path('/login');
                    }
                },
                function (error) {
                    ctrl.ScheduleRequestLogs.IsLoading = false;
                    alert(error);
                });
        }
    }

    //ctrl.GetReport();

    $scope.$watch('$ctrl.triggerRefesh', function (newValue, oldValue, scope) {
        //Do anything with $scope.letters
        if (typeof (newValue) != "undefined") {
            ctrl.GetReport();
        }
    });

    function GetStringOfDate(date) {
        if (typeof (date) != 'object') {
            return "";
        }
        var m = date.getMonth() + 1;
        var d = date.getDate();
        return date.getFullYear() + "-" + (m < 10 ? "0" : "") + m + "-" + (d < 10 ? "0" : "") + d;
    }
};

hthServiceApp.component('gridReport', {
    templateUrl: '/admin/modules/grid-report/grid-report.tpl.html',
    controller: GridReportController,
    bindings: {
        isShowAction:'<',
        isFailedRequest: '@',
        fromDate: '=?',
        toDate: '=?',
        triggerRefesh: '=',
        isTop: '<'
    },
    bindToController: {
        isShowAction: '<',
        isFailedRequest: '@',
        fromDate: '=?',
        toDate: '=?',
        triggerRefesh: '=',
        isTop:'<'
    }
});