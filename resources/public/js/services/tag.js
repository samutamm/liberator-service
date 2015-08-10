angular.module('samutammApp').factory('Tag', function($resource) {
  return $resource('/tags/');
});
