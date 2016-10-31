'use strict';

hthWebsiteApp.controller('CategoryController',
    ['$scope', '$rootScope', '$http', '$location', '$routeParams',
      function ($scope, $rootScope, $http, $location, $routeParams, $window) {
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

          $scope.IsLoading = false;
          $scope.Categories = [];
          $scope.SelectedCategory = { Id: 0 };

          $scope.GetCategories = function () {
              var getCategoriesUrl = URL_SERVICE + '/api/AdministratorApi/GetCategories/';

              $scope.IsLoading = true;
              $http.get(getCategoriesUrl).then(
                  function (response) {
                      $scope.IsLoading = false;
                      var responseData = response.data;
                      if (responseData.IsSuccess) {
                          $scope.Categories = responseData.Data;
                      } else {
                          $location.path('/login');
                      }
                  },
                  function (error) {
                      $scope.IsLoading = false;
                      alert(error);
                  });
          };
          $scope.DeleteCategory = function (item) {
              if (item != null && confirm("Do you want to delete " + item.Name + "?") == true) {
                  var getCategoriesUrl = URL_SERVICE + '/api/AdministratorApi/DeleteCategory/?categoryId=' + item.Id;

                  $scope.IsLoading = true;
                  $http.get(getCategoriesUrl).then(
                      function (response) {
                          $scope.IsLoading = false;
                          var responseData = response.data;
                          if (responseData.IsSuccess) {
                              if (responseData.Data.IsSuccess) {
                                  $scope.SelectedCategory = { Id: 0 };
                                  $scope.GetCategories();
                              } else {
                                  alert("Delete Category Error");
                              }
                          } else {
                              $location.path('/login');
                          }
                      },
                      function (error) {
                          $scope.IsLoading = false;
                          alert(error);
                      });
              }
          }
          $scope.EditCategory = function (item) {
              $scope.SelectedCategory = item;
          }
          $scope.NewCategory = function () {
              $scope.SelectedCategory = {Id: 0};
          }
          $scope.SaveCategory = function (valid) {
              if (valid && $scope.SelectedCategory != null)
              {
                  var getCategoriesUrl = URL_SERVICE + '/api/AdministratorApi/SaveCategory/';

                  $scope.IsLoading = true;
                  $http.put(getCategoriesUrl, $scope.SelectedCategory).then(
                      function (response) {
                          $scope.IsLoading = false;
                          var responseData = response.data;
                          if (responseData.IsSuccess) {
                              if (responseData.Data.IsSuccess) {
                                 // $scope.SelectedCategory = { Id: 0 };
                                 // $scope.GetCategories();
                              } else {
                                  alert("Save Category Error");
                              }
                          } else {
                              $location.path('/login');
                          }
                      },
                      function (error) {
                          $scope.IsLoading = false;
                          alert(error);
                      });
              }
          }
          $scope.GetCategories();
    }]);