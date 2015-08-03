angular.module('samutammApp').controller('ProjectCreateController', function (Project, $scope) {
    $scope.addProject = function() {
      console.log($scope.project);
    }
});
