angular.module('samutammApp').controller('ProjectsController', function (Project, $scope) {
    $scope.projects = Project.query();
});
