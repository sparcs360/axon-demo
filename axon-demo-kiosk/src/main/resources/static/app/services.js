'use strict';

angular.module('appKiosk')
.service('SocketService', function ($stomp, $q) {

    var isConnected = false;

    return {
        connect: function () {
            return $q(function (resolve, reject) {
                if (!isConnected) {
                	console.log('Attempting to connect to /kiosk-websocket');
                    $stomp.connect('/kiosk-websocket')
                        .then(function (frame) {
                        	console.log('Connected');
                            isConnected = true;
                            resolve();
                        })
                        .catch(function (reason) {
                        	console.log('Failed because [' + reason + ']');
                            reject(reason);
                        });
                }
                else {
                    resolve();
                }
            });
        }
    };
})
.service('KioskService', function ($stomp, $q) {

    return {
    	requestBalanceUpdate: function () {
        	console.log('requestBalanceUpdate() -> SEND /kiosk/commands/account/balance/get');
        	var response = $stomp.send('/kiosk/commands/account/balance/get');
        	console.log('requestBalanceUpdate() <- ' + JSON.stringify(response));
        	return response;
        },
        depositCash: function (amount) {
        	console.log('depositCash(amount=' + amount + ') -> SEND /kiosk/commands/account/deposit/cash');
        	$stomp.send('/kiosk/commands/account/deposit/cash', {'kioskId': '000001-01', 'amount': amount});
        },
        subscribeToBalanceUpdates: function () {
            var deferred = $q.defer();
        	console.log('subscribeToBalanceUpdates() -> SUBSCRIBE /topic/account/balance');
            $stomp.subscribe('/topic/account/balance', function (data) {
            	console.log('subscribeToBalanceUpdates() <- ' + JSON.stringify(data));
                deferred.notify(data);
            });
            return deferred.promise;
        }
    };
})
.service('BuildSlipService', function ($stomp, $q) {

	return {
		addSelection: function(id) {
        	console.log('addSelection(id=' + id + ') -> SEND /kiosk/commands/slip/selection/add');
        	$stomp.send('/kiosk/commands/slip/selection/add', {'kioskId': '000001-01', "selectionId":id});
		},
		removeSelection: function(id) {
        	console.log('removeSelection(id=' + id + ') -> SEND /kiosk/commands/slip/selection/remove');
        	$stomp.send('/kiosk/commands/slip/selection/remove', {'kioskId': '000001-01', "selectionId":id});
		},
        subscribeToSlipUpdates: function () {
            var deferred = $q.defer();
        	console.log('subscribeToSlipUpdates() -> SUBSCRIBE /topic/slip');
            $stomp.subscribe('/topic/slip', function (data) {
            	console.log('subscribeToSlipUpdates() <- ' + JSON.stringify(data));
                deferred.notify(data);
            });
            return deferred.promise;
        }
	}
	
});