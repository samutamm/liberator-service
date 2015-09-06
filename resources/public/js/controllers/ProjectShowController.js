angular.module('samutammApp').controller('ProjectShowController', function ($location, Auth, Project, $scope, $routeParams) {
    Project.get({id: $routeParams.id}).$promise.then(function(response) {
      $scope.imageUrl = "https://s3.eu-central-1.amazonaws.com/samutamm-images/" + response.image;
      $scope.project = response;
      $scope.github = JSON.parse(response.links).github;
      $scope.tags = response.tags.split(";");
    });

    $scope.deleteProject = function() {
      new Auth();
      Project.delete({id: $routeParams.id}).$promise.then(function(response) {
        $location.path("/projects");
      });
    }
});
