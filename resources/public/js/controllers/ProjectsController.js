angular.module('samutammApp').controller('ProjectsController', function ($scope, $http) {
    $scope.projects = [];

    $http.get('/projects').
      success(function(data, status, headers, config) {
        console.log("Status: " + status);
        console.log("data: " + data);
        $scope.projects = data;
      }).
      error(function(data, status, headers, config) {
        console.log("Error status: " + status);
      });
});
