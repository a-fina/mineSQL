'use strict';

angular.module('myApp.view1', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/view1', {
    templateUrl: 'view1/view1.html',
    controller: 'View1Ctrl'
  });
}])

.controller('View1Ctrl', ['$scope',function($scope) {

    $scope.choiceList= [
        {'name': 'Scissor', 'win': 'Paper'},
        {'name': 'Paper', 'win': 'Rock'},
        {'name': 'Rock', 'win': 'Scissor'}
    ];
    $scope.name = "World";
}])

.controller('PlayCtrl', function ($scope) {
  $scope.singleModel = 1;

  $scope.playerChoise   = 'Scissor';
  $scope.computerChoice = '';

  
  $scope.checkModel = {
    Scissor: false,
    Paper: true,
    Rock: false
  };

  $scope.alerts = [];
  
  $scope.totPlay = 0;
  $scope.totWin = 0;
  $scope.totLoose = 0;


  $scope.feelLucky = function() {
    $scope.alerts.push({ type: 'danger', msg: '<b>'+$scope.playerChoise+'</b> Sorry! You loose!!' });
    $scope.totPlay++;
    $scope.totLoose++;
  };

  $scope.playMorra= function() {
    // Random choise for computer
    $scope.computerChoice = $scope.choiceList.pop(Math.floor((Math.random() * 2) + 1)); 
    //console.log("-->" + $scope.computerChoice.win);

    if ( $scope.computerChoice.win === $scope.playerChoise){
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
  };

  $scope.closeAlert = function(index) {
    $scope.alerts.splice(index, 1);
  };
});