'use strict';

hthWebsiteApp.controller('ContentController',
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
          $scope.selectedIds = [];
          $scope.IsLoading = false;
          $scope.CategoryId = $routeParams["categoryId"];
          $scope.Categories = [{Id:null, Name:'None'}];
          $scope.SelectedCategory = $scope.Categories[0];
          $scope.Contents = [];
          $scope.SelectedContent = { Id: 0 };
          $scope.GetCategories = function () {
              var getCategoriesUrl = URL_SERVICE + '/api/AdministratorApi/GetCategories/';

              $scope.IsLoading = true;
              $http.get(getCategoriesUrl).then(
                  function (response) {
                      $scope.IsLoading = false;
                      var responseData = response.data;
                      if (responseData.IsSuccess) {
                          $scope.Categories = $scope.Categories.concat(responseData.Data);
                      } else {
                          $location.path('/login');
                      }
                  },
                  function (error) {
                      $scope.IsLoading = false;
                      alert(error);
                  });
          };
          $scope.GetContents = function () {
              var url = URL_SERVICE + '/api/AdministratorApi/GetContents/?';
              if ($scope.CategoryId)
              {
                  url += 'categoryId=' + $scope.CategoryId;
              }
              $scope.IsLoading = true;
              $http.get(url).then(
                  function (response) {
                      $scope.IsLoading = false;
                      var responseData = response.data;
                      if (responseData.IsSuccess) {
                          $scope.Contents = responseData.Data;
                      } else {
                          $location.path('/login');
                      }
                  },
                  function (error) {
                      $scope.IsLoading = false;
                      alert(error);
                  });
          };
          $scope.DeleteContent = function (item) {
              if (item != null && confirm("Do you want to delete " + item.Title + "?") == true) {
                  var url = URL_SERVICE + '/api/AdministratorApi/DeleteContent/?contentId=' + item.Id;

                  $scope.IsLoading = true;
                  $http.get(url).then(
                      function (response) {
                          $scope.IsLoading = false;
                          var responseData = response.data;
                          if (responseData.IsSuccess) {
                              if (responseData.Data.IsSuccess) {
                                  $scope.SelectedContent = { Id: 0 };
                                  $scope.GetContents();
                              } else {
                                  alert("Delete Content Error");
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
          $scope.CategorySelect = function (item) {
              $scope.SelectedCategory = item;
          }
          $scope.EditContent = function (item) {
              $scope.SelectedContent = item;
              $scope.SelectedContent.PublishedInDateTime = new Date($scope.SelectedContent.PublishedInDateTime);
              $scope.SelectedCategory = $scope.Categories[0];
              CKEDITOR.instances.Contenteditor.setData($scope.SelectedContent.Content);
              for (var i = 1; i < $scope.Categories.length; i++)
              {
                  if ($scope.SelectedContent.CategoryId == $scope.Categories[i].Id)
                  {
                      $scope.SelectedCategory = $scope.Categories[i];
                      break;
                  }
              }
              $('#CreateAndEditContentModal').modal('show');
          }

          $scope.UpdateSelections = function (checked, id) {
              if (checked) {
                  $scope.selectedIds.push(id);
              } else {
                  for (var i = $scope.selectedIds.length - 1; i >= 0; i--) {
                      if ($scope.selectedIds[i] === id) {
                          $scope.selectedIds.splice(i, 1);
                          break;
                      }
                  }
              }
          }

          $scope.GenarateZipContent = function (items) {
              if (items && items != null) {
                  var url = URL_SERVICE + '/api/AdministratorApi/GetContentsInString/?contentIds=' + items.join(',');

                  $scope.IsLoading = true;
                  $http.get(url).then(
                      function (response) {
                          $scope.IsLoading = false;
                          var responseData = response.data;
                          if (responseData.IsSuccess) {
                              $scope.GenaratedZipContents = responseData.Data;
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

          $scope.UpdateFromZipContent = function (data) {
              if (data && data != null && confirm("Do you want to update?") == true) {
                  var url = URL_SERVICE + '/api/AdministratorApi/SaveContentsFromString/';

                  $scope.IsLoading = true;
                  $http.post(url, { contents: data }).then(
                      function (response) {
                          $scope.IsLoading = false;
                          var responseData = response.data;
                          if (responseData.IsSuccess) {
                              alert(responseData.Data);
                              $scope.GetContents();
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
          
          $scope.NewContent = function () {
              var date = new Date();
              $scope.SelectedCategory = $scope.Categories[0];
              $scope.SelectedContent = { Id: 0, PublishedInDateTime: new Date(date.getFullYear(), date.getMonth(), date.getDate(), date.getHours(), date.getMinutes(), 0) };
              CKEDITOR.instances.Contenteditor.setData("");
              $('#CreateAndEditContentModal').modal('show');
          }
          $scope.getStringDateInCorrectFormat = function (date)
          {
              if (!date) date = new Date();
              var MM = date.getMonth() + 1; // getMonth() is zero-based
              var dd = date.getDate();
              var hh = date.getHours();
              var mm = date.getMinutes();
              return [date.getFullYear(),
                      (MM > 9 ? '' : '0') + MM,
                      (dd > 9 ? '' : '0') + dd
              ].join('-') + " " + [(hh > 9 ? '' : '0') + hh, (mm > 9 ? '' : '0') + mm, '00'].join(':');
          }
          $scope.SaveContent = function (valid) {
              if (valid && $scope.SelectedContent != null)
              {
                  var url = URL_SERVICE + '/api/AdministratorApi/SaveContent/';
                  $scope.SelectedContent.CategoryId = $scope.SelectedCategory.Id;
                  $scope.SelectedContent.ImageUrl = $("#txtSelectedFile").val();
                  $scope.SelectedContent.PublishedDate = $scope.getStringDateInCorrectFormat($scope.SelectedContent.PublishedInDateTime);
                  $scope.SelectedContent.Content = CKEDITOR.instances.Contenteditor.getData();
                  $scope.IsLoading = true;
                  $http.post(url, $scope.SelectedContent).then(
                      function (response) {
                          $scope.IsLoading = false;
                          var responseData = response.data;
                          if (responseData.IsSuccess) {
                              if (responseData.Data.IsSuccess) {
                                  $('#CreateAndEditContentModal').modal('hide');
                                  $scope.SelectedContent = { Id: 0 };
                                  $scope.GetContents();
                              } else {
                                  alert("Save Content Error");
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

          $scope.GetCategories();
          $scope.GetContents();
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