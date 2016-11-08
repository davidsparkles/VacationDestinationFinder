angular.module('starter.controllers', [])

.factory('apiendpoint', function() {
    var service={};
    service.url = "http://example.com";
    // service.backend = "http://smartbackend.cloudf.de"
    return service;
})

.controller('SearchCtrl', function($scope, $state, $http, apiendpoint) {
  $scope.temperature = "TMP_ALL";
  $scope.continent = "TMP_ALL";
  $scope.fromwhere = "Anywhere";

  $scope.resultText = "-";

  $scope.search = function(temperature,continent,fromwhere) {
    console.log(temperature + ', ' + continent + ', ' + fromwhere);
    var parameter = {
      temperature : temperature,
      continent : continent,
      fromwhere : fromwhere
    };

    // $http.post(apiendpoint.url + '/api/', parameter).success(function(response) {
    //         console.log('HTTP-Post response: ' + response);
    // });



      if($scope.resultText == "not yet implemented"){
        $scope.resultText = "stop pressing me";
      }else if($scope.resultText == "stop pressing me"){
        $scope.resultText = "dude...";
      } else{
        $scope.resultText = "not yet implemented";
      }
  }

  $scope.openMap = function(lat, lng){
    $state.go('app.map');
  }

})

.controller('MapCtrl', function($scope, $state) {
  // var options = {timeout: 10000, enableHighAccuracy: true};

  // $cordovaGeolocation.getCurrentPosition(options).then(function(position){

    // var latLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);

    var mapOptions = {
      center: {lat: 49.4836, lng: 8.4630},
      // center: latLng,
      zoom: 13,
      // mapTypeId: google.maps.MapTypeId.ROADMAP
    };

    $scope.map = new google.maps.Map(document.getElementById("map"), mapOptions);

  // }, function(error){
  //   console.log("Could not get location");
  // });
})

.controller('AppCtrl', function($scope, $ionicModal, $timeout) {

  // With the new view caching in Ionic, Controllers are only called
  // when they are recreated or on app start, instead of every page change.
  // To listen for when this page is active (for example, to refresh data),
  // listen for the $ionicView.enter event:
  //$scope.$on('$ionicView.enter', function(e) {
  //});

  // Form data for the login modal
  $scope.loginData = {};

  // Create the login modal that we will use later
  $ionicModal.fromTemplateUrl('templates/login.html', {
    scope: $scope
  }).then(function(modal) {
    $scope.modal = modal;
  });

  // Triggered in the login modal to close it
  $scope.closeLogin = function() {
    $scope.modal.hide();
  };

  // Open the login modal
  $scope.login = function() {
    $scope.modal.show();
  };

  // Perform the login action when the user submits the login form
  $scope.doLogin = function() {
    console.log('Doing login', $scope.loginData);

    // Simulate a login delay. Remove this and replace with your login
    // code if using a login system
    $timeout(function() {
      $scope.closeLogin();
    }, 1000);
  };
})


;
