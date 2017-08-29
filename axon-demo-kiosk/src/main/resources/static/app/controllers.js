'use strict';

angular.module('appKiosk')
.controller('KioskCtrl', function ($scope, SocketService, KioskService) {
	
    function updateBalance(data) {
    	console.log('updateBalance(data=' + angular.toJson(data) + ')');
        $scope.balance = data / 100;
    };

    $scope.depositCash = function (amount) {
    	KioskService.depositCash(amount);
    };
    
    SocketService.connect().then(function () {
    	KioskService
    		.subscribeToBalanceUpdates()
    		.then(function () {}, function () {}, updateBalance);
    	KioskService.requestBalanceUpdate();
    });

})
.controller('BuildSlipCtrl', function ($scope, SocketService, BuildSlipService) {
	
	function getEvents() {
		
	    $scope.events = [
	    	{
	    		id: "E1",
	    		name: "Barnsley vs Sheffield",
	    		selections: [
	    			{
	            		id: "S1.1",
	            		name: "Barnsley",
	            		den: 7,
	            		num: 6
	    			},
	    			{
	            		id: "S1.2",
	            		name: "Draw",
	            		den: 2,
	            		num: 1
	    			},
	    			{
	            		id: "S1.3",
	            		name: "Sheffield",
	            		den: 3,
	            		num: 1
	    			}
	    		]
	    	},
	    	{
	    		name: "Leeds vs Manchester",
	    		selections: [
	    			{
	            		id: "S2.1",
	            		name: "Leeds",
	            		den: 3,
	            		num: 2
	    			},
	    			{
	            		id: "S2.2",
	            		name: "Draw",
	            		den: 11,
	            		num: 10
	    			},
	    			{
	            		id: "S2.3",
	            		name: "Manchester",
	            		den: 2,
	            		num: 1
	    			}
	    		]
	    	}
	    ];
	}
	
	function clearSlip() {
		
		$scope.slip = {
			
			selections: []
		};
	}
	
    function updateSlip(data) {
    	console.log('updateSlip(data=' + angular.toJson(data) + ')');
    };

    $scope.prettyPrintSelection = function (selection) {
    	return selection.name + ' @ ' + selection.den + '/' + selection.num;
    };
    
    $scope.getSelectionButtonClass = function(selectionId) {
    	
    	return $scope.slip.selections.includes(selectionId) ? 'btn-primary' : 'btn-default';
    };
    
    $scope.toggleSelection = function (id) {
    	
    	if ($scope.slip.selections.includes(id)) {
        	BuildSlipService.removeSelection(id);
    		var i = $scope.slip.selections.indexOf(id);
    		$scope.slip.selections.splice(i,1);
    	} else {
        	BuildSlipService.addSelection(id);
        	$scope.slip.selections.push(id);
    	}
    };
    
    SocketService.connect().then(function () {
    	getEvents();
    	clearSlip();
    	BuildSlipService.subscribeToSlipUpdates()
    		.then(function () {}, function () {}, updateSlip);
    });
});
