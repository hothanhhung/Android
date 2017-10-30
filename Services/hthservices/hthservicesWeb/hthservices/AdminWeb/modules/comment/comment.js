'use strict';

hthWebsiteApp.controller('CommentController',
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
          $scope.ContentId = $routeParams["contentId"];
          $scope.Comments = [];
          $scope.SelectedComment = { Id: 0 };
          
          $scope.GetComments = function () {
              var url = URL_SERVICE + '/api/AdministratorApi/GetComments/?';
              if ($scope.ContentId) {
                  url += 'contentId=' + $scope.ContentId;
              }
              $scope.IsLoading = true;
              $http.get(url).then(
                  function (response) {
                      $scope.IsLoading = false;
                      var responseData = response.data;
                      if (responseData.IsSuccess) {
                          $scope.Comments = responseData.Data;
                      } else {
                          $location.path('/login');
                      }
                  },
                  function (error) {
                      $scope.IsLoading = false;
                      alert(error);
                  });
          };
          $scope.DeleteComment = function (item) {
              if (item != null && confirm("Do you want to delete " + item.Name + "?") == true) {
                  var url = URL_SERVICE + '/api/AdministratorApi/DeleteComment/?commentId=' + item.Id;

                  $scope.IsLoading = true;
                  $http.get(url).then(
                      function (response) {
                          $scope.IsLoading = false;
                          var responseData = response.data;
                          if (responseData.IsSuccess) {
                              if (responseData.Data.IsSuccess) {
                                  $scope.SelectedContent = { Id: 0 };
                                  $scope.GetComments();
                              } else {
                                  alert("Delete Comment Error");
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
          $scope.EditComment = function (item) {
              $scope.SelectedComment = item;
              CKEDITOR.instances.Message.setData($scope.SelectedComment.Message);
              $('#CreateAndEditCommentModal').modal('show');
          }
          $scope.NewComment = function () {
              $scope.SelectedComment = { Id: 0 };
              $('#CreateAndEditCommentModal').modal('show');
          }
          $scope.SaveComment = function (valid) {
              if (valid && $scope.SelectedComment != null)
              {
                  var url = URL_SERVICE + '/api/AdministratorApi/SaveComment/';
                  $scope.SelectedComment.Message = CKEDITOR.instances.Message.getData();
                  $scope.IsLoading = true;
                  $http.post(url, $scope.SelectedComment).then(
                      function (response) {
                          $scope.IsLoading = false;
                          var responseData = response.data;
                          if (responseData.IsSuccess) {
                              if (responseData.Data.IsSuccess) {
                                  $scope.SelectedComment = { Id: 0 };
                                  $scope.GetComments();
                              } else {
                                  alert("Save Comment Error");
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
          $scope.GetComments();
          CKEDITOR.replace('commenteditor'/*, {
              toolbar: [
                          ['Cut', 'Copy', 'Paste', 'PasteText', 'PasteFromWord', '-', 'Undo', 'Redo'],
                          ['Link', 'Unlink', 'Anchor'],
                          ['Image', 'Table', 'Smiley', 'SpecialChar'],
                          ['TextColor', 'BGColor'],
                          '/',
                          ['Bold', 'Italic', 'Underline', 'Strike', 'Subscript', 'Superscript', '-', 'RemoveFormat'],
                          ['Styles', 'Format', 'Font', 'FontSize']

              ],
          }*/);
    }]);