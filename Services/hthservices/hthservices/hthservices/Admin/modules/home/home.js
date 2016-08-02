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
    }]);