'use strict';

angular.module('myApp.view1', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/view1', {
    templateUrl: 'view1/view1.html',
    controller: 'View1Ctrl'
  });
}])

.controller('View1Ctrl', ['$scope',function($scope) {
    // TODO: move to server side MorraChinese.java
    $scope.choiceList = [
        {'name': 'scissor', 'win': 'paper'},
        {'name': 'paper', 'win': 'rock'},
        {'name': 'rock', 'win': 'scissor'}
    ];
    $scope.name = "World";
}])

.controller('PlayCtrl',['$scope', 'MorraFactory', function ($scope, MorraFactory) {
  $scope.singleModel = 1;

  $scope.playerChoise   = 'scissor';
  $scope.computerChoice = '';

  
  $scope.checkModel = {
    scissor: false,
    paper: true,
    rock: false
  };

  $scope.alerts = [];
  
  $scope.totPlay = 0;
  $scope.totWin = 0;
  $scope.totLoose = 0;

  function randomFromTo(from, to){
       return Math.floor(Math.random() * (to - from + 1) + from);
  }
    
  $scope.feelLucky = function() {
    $scope.alerts.push({ type: 'danger', msg: $scope.playerChoise+' Sorry! You loose!!' });
    $scope.totPlay++;
    $scope.totLoose++;
  };

  $scope.playMorra= function() {

    // Leggo la scelta del computer viw web service
    MorraFactory.get({}, function (MorraFactory) {
        //$scope.computerChoice = $scope.choiceList[rand];
        $scope.computerChoice = MorraFactory.type;

        if ( $scope.computerChoice === $scope.playerChoise){
            // Computer Win
            $scope.alerts.push({ type: 'danger', msg: $scope.playerChoise+' Sorry! You loose!!' });
            $scope.totPlay++;
            $scope.totLoose++;
        }else{
            // Human Win
            $scope.alerts.push({ type: 'success', msg: $scope.playerChoise+' Well done! You win!!' });
            $scope.totPlay++;
            $scope.totWin++;
        }
    })
  };

  $scope.closeAlert = function(index) {
    $scope.alerts.splice(index, 1);
  };
}]);