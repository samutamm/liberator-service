angular.module('samutammApp').controller('ProjectsController', function (Tag, Project, $scope) {
    $scope.search = {};
    $scope.projects = Project.query();
    $scope.imageUrl = function(project) {
        return "https://s3.eu-central-1.amazonaws.com/samutamm-images/" + project.image;
    };
    $scope.allTags = Tag.query();
    $scope.tagAdded = function(newTag) {
      $scope.search.tags = newTag.text;
    }
});
