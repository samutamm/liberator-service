angular.module('samutammApp').controller('ProjectShowController', function (Project, $scope, $routeParams) {
    Project.get({id: $routeParams.id}).$promise.then(function(response) {
      $scope.imageUrl = "https://s3.eu-central-1.amazonaws.com/samutamm-images/" + response.image;
      $scope.project = response;
      debugger;
    });
});
