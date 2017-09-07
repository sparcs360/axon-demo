'use strict';

angular.module('appCounter')
.controller('KiosksCtrl', function ($scope, $uibModal, SocketService, CounterService) {
	
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

    $scope.addCreditToKiosk = function (kioskId) {
    	console.log('addCreditToKiosk(kioskId=' + kioskId + ')');
        $uibModal.open({
            controller: 'SendCommandFromModalCtrl',
            templateUrl: '/app/modals/addCreditToKioskModal.html',
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            resolve: {
            	commandName: () => 'executive.account.CAddCredit',
            	kioskId: () => kioskId
            }
        });
    };
    
    $scope.removeCreditFromKiosk = function (kioskId) {
    	console.log('removeCreditFromKiosk(kioskId=' + kioskId + ')');
        $uibModal.open({
            controller: 'SendCommandFromModalCtrl',
            templateUrl: '/app/modals/removeCreditFromKioskModal.html',
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            resolve: {
            	commandName: () => 'executive.account.CRemoveCredit',
            	kioskId: () => kioskId
            }
        });
    };
    
    $scope.resetKiosk = function (kioskId, reason) {
    	console.log('resetKiosk(kioskId=' + kioskId + ')');
        $uibModal.open({
            controller: 'SendCommandFromModalCtrl',
            templateUrl: '/app/modals/resetKioskModal.html',
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            resolve: {
            	commandName: () => 'executive.CResetKiosk',
            	kioskId: () => kioskId
            }
        });
    };
})
.controller('SendCommandFromModalCtrl', function ($scope, $uibModalInstance, CounterService, kioskId, commandName) {

    $scope.commandPayload = {
    	"kioskId": kioskId
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    };
    $scope.submit = function () {
    	CounterService.sendCommand(commandName, $scope.commandPayload);
        $uibModalInstance.close();
    };
})
;
