'use strict';

hthWebsiteApp.controller('ProjectController',
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
          $scope.Projects = [];
          $scope.SelectedProject = { Id: 0 };
          $scope.GetProjects = function () {
              var url = URL_SERVICE + '/api/AdministratorApi/GetProjects';

              $scope.IsLoading = true;
              $http.get(url).then(
                  function (response) {
                      $scope.IsLoading = false;
                      var responseData = response.data;
                      if (responseData.IsSuccess) {
                          $scope.Projects = responseData.Data;
                      } else {
                          $location.path('/login');
                      }
                  },
                  function (error) {
                      $scope.IsLoading = false;
                      alert(error);
                  });
          };
          $scope.DeleteProject = function (item) {
              if (item != null && confirm("Do you want to delete " + item.Title + "?") == true) {
                  var url = URL_SERVICE + '/api/AdministratorApi/DeleteProject/?contentId=' + item.Id;

                  $scope.IsLoading = true;
                  $http.get(url).then(
                      function (response) {
                          $scope.IsLoading = false;
                          var responseData = response.data;
                          if (responseData.IsSuccess) {
                              if (responseData.Data.IsSuccess) {
                                  $scope.SelectedProject = { Id: 0 };
                                  $scope.GetProjects();
                              } else {
                                  alert("Delete Project Error");
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
          $scope.EditProject = function (item) {
              $scope.SelectedProject = item;
              CKEDITOR.instances.Contenteditor.setData($scope.SelectedProject.Content);
              
              $('#CreateAndEditContentModal').modal('show');
          }
          $scope.NewProject = function () {
              $scope.SelectedProject = { Id: 0 };
              CKEDITOR.instances.Contenteditor.setData("");
              $('#CreateAndEditContentModal').modal('show');
          }
          $scope.SaveProject = function (valid) {
              if (valid && $scope.SelectedProject != null)
              {
                  var url = URL_SERVICE + '/api/AdministratorApi/SaveProject/';
                  $scope.SelectedProject.Content = CKEDITOR.instances.Contenteditor.getData();
                  $scope.IsLoading = true;
                  $http.post(url, $scope.SelectedProject).then(
                      function (response) {
                          $scope.IsLoading = false;
                          var responseData = response.data;
                          if (responseData.IsSuccess) {
                              if (responseData.Data.IsSuccess) {
                                  $('#CreateAndEditContentModal').modal('hide');
                                  $scope.SelectedProject = { Id: 0 };
                                  $scope.GetProjects();
                              } else {
                                  alert("Save Project Error");
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

          $scope.openBrowserFileForImageUrl = function() {
              $('#browserFileForImageUrl').dialog({ modal: true, width: 875, height: 600 });
          }
          $scope.closeBrowserFileForImageUrl = function () {
              $('#browserFileForImageUrl').dialog('close');
          }

          $scope.GetProjects();
          var roxyFileman = URL_SERVICE + '/fileman/index.html';
          if (CKEDITOR.instances.Contenteditor !== undefined) {
              CKEDITOR.instances.Contenteditor.destroy();
          }
              CKEDITOR.replace('Contenteditor',
                  {
                      filebrowserImageUploadUrl: URL_SERVICE+'/CKEditorUpload.ashx',
                      filebrowserBrowseUrl:roxyFileman,
                      filebrowserImageBrowseUrl:roxyFileman+'?type=image',
                      removeDialogTabs: 'link:upload;image:upload'
                  }
                  /*{
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