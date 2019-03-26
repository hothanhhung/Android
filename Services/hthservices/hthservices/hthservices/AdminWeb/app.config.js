hthWebsiteApp.factory('httpRequestInterceptor', function () {
    return {
        request: function (config) {

            config.headers['token'] = window.localStorage.getItem("AuthInfo.Token");

            return config;
        }
    };
});

hthWebsiteApp.config(['$routeProvider', '$httpProvider', function ($stateProvider, $httpProvider) {

    //$httpProvider.defaults.headers.common['token'] = window.localStorage.getItem("AuthInfo.Token");

    $httpProvider.interceptors.push('httpRequestInterceptor');

    $stateProvider
      .when('/login', {
          templateUrl: "/adminweb/modules/login/login.tpl.html",
          controller: 'LoginController'
      })
     .when('/', {
         templateUrl: "/adminweb/modules/login/login.tpl.html",
         controller: 'LoginController'
     })
      .when('/home', {
          templateUrl: "/adminweb/modules/home/home.tpl.html",
          controller: 'HomeController'
      })
    .when('/category', {
        templateUrl: "/adminweb/modules/category/category.tpl.html",
        controller: 'CategoryController'
    })
    .when('/content/:categoryId?', {
        templateUrl: "/adminweb/modules/content/content.tpl.html",
        controller: 'ContentController'
    })
    .when('/comment/:contentId?', {
        templateUrl: "/adminweb/modules/comment/comment.tpl.html",
        controller: 'CommentController'
    })
    .when('/project', {
        templateUrl: "/adminweb/modules/project/project.tpl.html",
        controller: 'ProjectController'
    });
}]);