angular.module('samutammApp').controller('ProjectShowController', function (Project, $scope, $routeParams) {
    $scope.project = Project.get({id: $routeParams.id});
});
