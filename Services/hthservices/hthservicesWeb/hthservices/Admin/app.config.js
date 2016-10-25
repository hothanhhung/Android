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
     .when('/dashboard', {
         templateUrl: "/admin/modules/dashboard/dashboard.tpl.html",
         controller: 'DashboardController'
     })
      .when('/home', {
          templateUrl: "/admin/modules/home/home.tpl.html",
          controller: 'HomeController'
      });
}]);