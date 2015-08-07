angular.module('samutammApp').factory('AWSCredentials', function($resource) {
  return $resource('/image-credentials');
});
