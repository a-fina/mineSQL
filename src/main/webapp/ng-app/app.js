'use strict';

// Declare app level module which depends on views, and components
angular.module('myApp', [
  'ngRoute',
  'myApp.view1',
  'myApp.view2',
  'myApp.version',
  'ui.bootstrap',
  'ngResource'
]).
config(['$routeProvider', function($routeProvider) {
  $routeProvider.otherwise({redirectTo: '/view1'});
}])

.controller('WelcomeController', function($scope) {
    // TODO: login / signin
    $scope.greeting = 'back';
})
// Web Services
.factory('MorraFactory', function ($resource) {
    return $resource('/rest/morra/choice/computer', {}, {
        query: {
            method: 'GET',
            params: {},
            isArray: false
        }
    })
});
