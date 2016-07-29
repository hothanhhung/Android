'use strict';

hthServiceApp.controller('LoginController',
    ['$scope', '$rootScope', '$http', '$location',
    function ($scope, $rootScope, $http, $location) {
        $rootScope.AuthInfo = {
            Token: "",
            Message:""
        }
        $scope.Login = function (username, password) {
            var loginUrl = URL_SERVICE + '/api/Account/login/';
            $http.post(loginUrl, { username: username, password: password }, null).then(
                function (response) {
                    var responseData = response.data;
                    if (responseData.Data.IsSuccess) {
                        window.localStorage.setItem("AuthInfo.Token", responseData.Data.Token);
                        $rootScope.AuthInfo.Token = responseData.Data.Token;
                        $location.path('/home');
                    } else {
                        $rootScope.AuthInfo.Message = responseData.Data.Message;
                    }
                },
                function (error) {
                    $rootScope.AuthInfo.Message = responseData.Data.Message;
                });
        };
    }]);