hthServiceApp.config(['$routeProvider', function ($stateProvider) {

    $stateProvider
      .when('/login', {
          templateUrl: "/admin/modules/login/login.tpl.html",
          controller: 'LoginController'
      })
     .when('/', {
         templateUrl: "/admin/modules/login/login.tpl.html",
         controller: 'LoginController'
     })
      .when('/home', {
          templateUrl: "/admin/modules/home/home.tpl.html",
          controller: 'HomeController'
      });
}]);