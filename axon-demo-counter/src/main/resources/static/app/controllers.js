'use strict';

angular.module('appCounter')
.controller('KiosksCtrl', function ($scope, SocketService, CounterService) {
	
	$scope.kiosks;
	
    SocketService.connect().then(function () {
    	CounterService
    		.getKioskStatus()
    		.then(updateKioskStatus);
    	CounterService
    		.subscribeToKioskUpdates()
    		.then(function () {}, function () {}, updateKioskStatus);
    });
    
    function updateKioskStatus(data) {
    	console.log('updateKioskStatus(data=' + angular.toJson(data) + ')');
    	$scope.kiosks = data;
    };

    $scope.addCreditToKiosk = function (kioskId, amount) {
    	var data = {"kioskId": kioskId, "amount": amount};
    	console.log('addCreditToKiosk(data=' + angular.toJson(data) + ')');
    };
    
    $scope.removeCreditFromKiosk = function (kioskId, amount) {
    	var data = {"kioskId": kioskId, "amount": amount};
    	console.log('removeCreditFromKiosk(data=' + angular.toJson(data) + ')');
    };
    
    $scope.resetKiosk = function (kioskId, reason) {
    	var data = {"kioskId": kioskId, "reason": reason};
    	console.log('resetKiosk(data=' + angular.toJson(data) + ')');
    };
})
;
