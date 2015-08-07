angular.module('samutammApp').controller('ProjectsController', function (Project, $scope) {
    $scope.projects = Project.query();
    $scope.imageUrl = "https://s3.eu-central-1.amazonaws.com/samutamm-images/";
});
