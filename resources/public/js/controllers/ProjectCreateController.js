angular.module('samutammApp').controller('ProjectCreateController', function (Project, $scope, $location) {
    $scope.isSubmitting = false;

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
      $scope.isSubmitting = true;
      var project = createProjectObject($scope.project);
      project.$save().then(function(response) {
        console.log("Uusi id: " + response.id);
        $location.path("/projects");
      }).finally(function() {
        $scope.isSubmitting = false;
      });
    }

    $scope.dateOptions = {
      formatYear: 'yy',
      startingDay: 1
    }

    function createProjectObject(project) {
      var projectToSend = new Project();
      projectToSend.projectstart = createDateJson(project.projectstart);
      projectToSend.projectend = createDateJson(project.projectend);
      projectToSend.tags = project.tags.map(function(tag) {
         return tag.text
      }).join(";");
      projectToSend.projectname = project.projectname;
      projectToSend.description = project.description;
      return projectToSend;
    }

    function createDateJson(date) {
       return {
         year: date.getFullYear(),
         month: date.getMonth(),
         day: date.getDate()
      };
    }
});
