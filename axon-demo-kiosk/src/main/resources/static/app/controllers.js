'use strict';

angular.module('appKiosk')
.controller('KioskCtrl', function ($scope, SocketService, KioskService) {
	
    SocketService.connect().then(function () {
    	KioskService
    		.subscribeToBalanceUpdates()
    		.then(function () {}, function () {}, updateBalance);
    	KioskService.requestBalanceUpdate();
    });

    $scope.depositCash = function (amount) {
    	KioskService.depositCash(amount);
    };
    
    function updateBalance(data) {
        $scope.balance = data / 100;
    };
})
.controller('BuildSlipCtrl', function ($scope, SocketService, BuildSlipService) {
	
    SocketService.connect().then(function () {
    	getEvents();
    	BuildSlipService.subscribeToSlipUpdates()
    		.then(function () {}, function () {}, updateSlip);
    	BuildSlipService.requestSlipUpdate();
    });
    
	$scope.slip = {
			
		"selections": []
	};
	
	$scope.clearSlip = function() {
		
		BuildSlipService.clearSlip();
	}
	
    $scope.containsSelection = function(selectionId) {
		return $.grep($scope.slip.selections, (s) => s.selectionId == selectionId).length > 0;
	}

    $scope.prettyPrintSelection = function (selection) {
    	return selection.name + ' @ ' + selection.numerator + '/' + selection.denominator;
    };
    
	$scope.getSelectionButtonClass = function(selectionId) {
    	return ($scope.containsSelection(selectionId)) ? 'btn-primary' : 'btn-default';
    };
    
    $scope.toggleSelection = function (selectionId) {
    	
    	if ($scope.containsSelection(selectionId)) {
        	BuildSlipService.removeSelection(selectionId);
    	} else {
        	BuildSlipService.addSelection(selectionId);
    	}
    };
    
	function getEvents() {
		
	    $scope.events = [
	    	{
	    		id: "E1",
	    		name: "Barnsley vs Sheffield",
	    		selections: [
	    			{
	            		selectionId: "S1.1",
	            		name: "Barnsley",
	            		numerator: 7,
	            		denominator: 6
	    			},
	    			{
	            		selectionId: "S1.2",
	            		name: "Draw",
	            		numerator: 2,
	            		denominator: 1
	    			},
	    			{
	            		selectionId: "S1.3",
	            		name: "Sheffield",
	            		numerator: 3,
	            		denominator: 1
	    			}
	    		]
	    	},
	    	{
	    		name: "Leeds vs Manchester",
	    		selections: [
	    			{
	            		selectionId: "S2.1",
	            		name: "Leeds",
	            		numerator: 3,
	            		denominator: 2
	    			},
	    			{
	            		selectionId: "S2.2",
	            		name: "Draw",
	            		numerator: 11,
	            		denominator: 10
	    			},
	    			{
	            		selectionId: "S2.3",
	            		name: "Manchester",
	            		numerator: 2,
	            		denominator: 1
	    			}
	    		]
	    	}
	    ];
	}
	
    function updateSlip(data) {
    	$scope.slip.selections = data.selections;
    };
});
