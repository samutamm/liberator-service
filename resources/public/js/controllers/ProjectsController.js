angular.module('samutammApp').controller('ProjectsController', function (Project, $scope, $http) {
    $scope.projects = Project.query();
});
