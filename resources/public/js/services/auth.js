angular.module('samutammApp').factory('Auth', function($http, $location) {
  return function(credentials) {
    var authUrl = "/authenticate/" + credentials.username + "-" + credentials.password;
    $http.get(authUrl).
      then(function(response) {
        console.log(response.statusText);
      }, function(response) {
        alert("Wrong credentials!");
        $location.path("/projects");
      });
  }
});
