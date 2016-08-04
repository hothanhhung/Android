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

    $scope.ScheduleRequestLogs = { FromDate: new Date(), ToDate: new Date(), IsLoading: false };
    $scope.ScheduleRequestLogs.FromDate = new Date($scope.ScheduleRequestLogs.ToDate.getFullYear(), $scope.ScheduleRequestLogs.ToDate.getMonth(), 1);
    $scope.GetReport = function () {
        var getReportUrl = URL_SERVICE + '/api/ReportApi/Dashboard/?token=' + $rootScope.AuthInfo.Token + "&fromDate=" + GetStringOfDate($scope.ScheduleRequestLogs.FromDate) + "&toDate=" + GetStringOfDate($scope.ScheduleRequestLogs.ToDate);
        
        $scope.ScheduleRequestLogs.IsLoading = true;
        $http.get(getReportUrl, null).then(
            function (response) {
                $scope.ScheduleRequestLogs.IsLoading = false;
                var responseData = response.data;

                if (responseData.IsSuccess) {
                    areaDashboard.setData(responseData.Data);
                } else {
                    $location.path('/login');
                }
            },
            function (error) {
                ctrl.ScheduleRequestLogs.IsLoading = false;
                alert(error);
            });
    };
        
    $scope.ToDateMaxDate = new Date();
    var areaDashboard = Morris.Line({
        element: 'area-dashboard',
        data: null,
        xkey: 'CurrentDate',
        ykeys: ['NumberOfRequest', 'NumberOfFailedRequest'],
        labels: ['Schedule Request Logs', 'Schedule Failed Request Logs'],
        pointSize: 2,
        hideHover: 'auto'
    });

    $scope.GetReport();

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
}]);
