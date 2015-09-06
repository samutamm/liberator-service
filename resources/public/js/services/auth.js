angular.module('samutammApp').factory('Auth', function($http, $location) {
  return function() {
    var username = prompt("Username: ");
    var password = prompt("Password: ");
    var authUrl = "/authenticate/" + username + "-" + password;
    $http.get(authUrl).
      then(function(response) {
        console.log(response.statusText);
      }, function(response) {
        alert("Wrong credentials!");
        $location.path("/projects");
      });
  }
});
