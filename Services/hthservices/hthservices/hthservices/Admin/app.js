// Define the `phonecatApp` module
var hthServiceApp = angular.module('hthServiceApp', []);
var URL_SERVICE = "http://localhost:9511";



//hthServiceApp.controller('AdminController', function PhoneListController($scope, $http) {
//    $scope.AuthInfo = {
//        Token: "",
//        Message:""
//    }

//    $scope.Login = function(username, password)
//    {
//        var loginUrl = URL_SERVICE + '/api/Account/login/';

//        //var req = {
//        //    method: 'POST',
//        //    url: loginUrl,
//        //    data: { username: username, password: password }
//        //}

//        //$http(req).then(function (response) {
//        //            var responseData = response.data;
//        //            if (responseData.Data.IsSuccess) {
//        //                $scope.AuthInfo.Token = responseData.Data.Token;
//        //            } else {
//        //                $scope.AuthInfo.Message = responseData.Data.Message;
//        //            }
//        //        },
//        //        function (error) {
//        //            $scope.AuthInfo.Message = responseData.Data.Message;
//        //        });

//        $http.post(loginUrl, {username:username, password: password}, null).then(
//            function (response) {
//                var responseData = response.data;
//                if (responseData.Data.IsSuccess) {
//                    $scope.AuthInfo.Token = responseData.Data.Token;
//                } else {
//                    $scope.AuthInfo.Message = responseData.Data.Message;
//                }
//            },
//            function (error) {
//                $scope.AuthInfo.Message = responseData.Data.Message;
//            });
//    }
//});