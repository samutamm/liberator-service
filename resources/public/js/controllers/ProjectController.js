angular.module('samutammApp').controller('ProjectController', function ($scope) {
    $scope.moi = "Heippa!";

    $scope.greeting = function() {
      console.log($scope.moi);
    }
});
