angular.module('samutammApp').controller('ProjectCreateController', function (Project, $scope) {
    $scope.today = function() {
      var today = new Date();
      var dd = today.getDate();
      var mm = today.getMonth()+1; //January is 0!
      var yyyy = today.getFullYear();
      if(dd<10){
          dd='0'+dd
      }
      if(mm<10){
          mm='0'+mm
      }
      var today = dd+'/'+mm+'/'+yyyy;
      $scope.project = {};
      $scope.project.projectstart = new Date(today);
      $scope.project.projectend = new Date(today);
    };
    $scope.today()

    $scope.addProject = function() {
      console.log($scope.project);
    }

    $scope.dateOptions = {
      formatYear: 'yy',
      startingDay: 1
    }
});
