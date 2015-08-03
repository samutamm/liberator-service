angular.module('samutammApp').config(function ($routeProvider) {
    $routeProvider
            .when('/', {
                redirectTo: '/projects'
            })
            .when('/projects', {
              templateUrl: "templates/home.html",
              controller: "ProjectsController"
            })
            .when('/projects/new', {
              templateUrl: "templates/new.html",
              controller: "ProjectCreateController"
            })
            .when('/projects/:id', {
              templateUrl: "templates/show.html",
              controller: "ProjectShowController"
            })
});
