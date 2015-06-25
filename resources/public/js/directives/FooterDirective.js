angular.module("samutammApp").directive('footerDirective', function() {
  return {
    replace: true,
    restrict: "E",
    templateUrl: "templates/footer.html",
    controller: function($scope) {
      $scope.year = (function() {
        return new Date().getFullYear();
      })();
    }
  };
});
