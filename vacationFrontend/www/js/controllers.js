angular.module('starter.controllers', [])

.factory('apiendpoint', function() {
    var service={};
    service.url = "http://localhost:8080";
    return service;
})

.factory('locations', function(){
  var service = {}; var locations;
  service.locations = locations;
  service.setLocations = function(data){
    locations = data;
  }
  service.getLocations = function(){
    return locations;
  }
  return service;
})

.controller('SearchCtrl', function($scope, $state, $http, apiendpoint, locations) {
  $scope.population = "";
  $scope.country = "";
  $scope.month = 0;
  // $scope.distance = "12h";
  // $scope.transportation = "car";
  $scope.specification = "";
  $scope.temperature = "";

  // $scope.continent = "TMP_ALL";
  // $scope.fromwhere = "Anywhere";
  $scope.countries = [];
  $scope.country = "";
  $http.get("data/countries.json")
    .success(function(json) {
      $scope.countries = json;
  });

  $scope.results = [];

  $scope.search = function(month,country,population,specification,temperature) {
    var parameter = {
      // "distance":distance,
      // "transportation":transportation
    };
    if(month != 0) parameter.month = month;
    if(specification != "" && specification != undefined) parameter.specification = specification;
    if(temperature != "" && temperature != undefined) parameter.temperature = temperature;
    if(population != "" && population != undefined) parameter.population = population;
    if(country != "" && country != undefined) parameter.country = country;

    console.log(parameter);

    // parameter = {"month":"8","distance":"12h","transportation":"car","specification":"beach","temperature":"18"};

    $http.post(apiendpoint.url + '/destination', parameter).success(function(response) {
      $scope.results = [];
      for(var city in response) $scope.results = $scope.results.concat([{name:city,lat:response[city][0].lat,long:response[city][1].long}]);
      // console.log($scope.results);
      locations.setLocations($scope.results);
      // console.log("read from factory");
      // console.log(locations.getLocations());
      $scope.openMap()
    });
    // $http.get(apiendpoint.url + '/destination').success(function(response) {
    //         console.log(response.data);
    //         console.log(response.status);
    // });
    // $scope.results = [{name:"Berlin",lat:52.521918,long:13.413215,score:"99%"},{name:"London",lat:51.5073509,long:-0.1277583,score:"80%"}];

  }

  $scope.openMap = function(){
    $state.go('app.map');
  }

})

.controller('MapCtrl', function($scope, $state, $stateParams, locations) {
  // var options = {timeout: 10000, enableHighAccuracy: true};

  // $cordovaGeolocation.getCurrentPosition(options).then(function(position){

    // var latLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);

    // console.log("stateParams: " + $stateParams.name + " " + $stateParams.lat + " "  + $stateParams.long);

    var mapOptions = {
      center: {lat: Number($stateParams.lat), lng: Number($stateParams.long)},
      // center: latLng,
      zoom: 1,
      // mapTypeId: google.maps.MapTypeId.ROADMAP
    };

    $scope.map = new google.maps.Map(document.getElementById("map"), mapOptions);

    var locations = locations.getLocations();
    console.log(locations);

    var marker, i;

    for (i = 0; i < locations.length; i++) {
      marker = new google.maps.Marker({
        position: new google.maps.LatLng(locations[i]["lat"], locations[i]["long"]),
        map: $scope.map
      });


      google.maps.event.addListener(marker, 'click', (function(marker, i) {
        return function() {
          var infowindow = new google.maps.InfoWindow();
          infowindow.setContent(locations[i]["name"]);
          infowindow.open($scope.map, marker);
        }
      })(marker, i));
    }


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
