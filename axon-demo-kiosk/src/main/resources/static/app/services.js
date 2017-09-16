'use strict';

angular.module('appKiosk')
.service('SocketService', function ($stomp, $q) {

	var state = 'idle';
	var subscriberMap;
	var failureReason;

    return {
        connect: function (requester) {
            return $q(function (resolve, reject) {
            	
            	console.log('SocketService[' + state + ']: Connect request from [' + requester + ']');

            	if (state === 'connected') {

                	console.log('SocketService[' + state + ']: Resolving [' + requester + ']');
            		resolve();

            	} else if (state === 'idle') {

                	state = 'connecting';
                	console.log('SocketService[' + state + ']: Attempting to connect to /kiosk-websocket');
            		subscriberMap = new Map([[requester, {'resolve': resolve, 'reject': reject}]]);

                    $stomp.connect('/kiosk-websocket')
                        .then(function (frame) {
                        	state = 'connected';
                        	console.log('SocketService[' + state + ']: Connected, frame=' + frame);
                    		for (let [pendingRequester, pendingPromise] of subscriberMap) {
                            	console.log('SocketService[' + state + ']: Resolving [' + pendingRequester + ']');
                            	pendingPromise.resolve();
                    		}
                    		subscriberMap = undefined;
                        })
                        .catch(function (reason) {
                        	state = "failed";
                        	console.log('SocketService[' + state + ']: Connection failed because [' + reason + ']');
                        	failureReason = reason;
                    		for (let [pendingRequester, pendingPromise] of subscriberMap) {
                            	console.log('SocketService[' + state + ']: Rejecting [' + pendingRequester + ']');
                            	pendingPromise.reject(failureReason);
                    		}
                    		subscriberMap = undefined;
                        });

            	} else if (state === 'connecting') {

            		console.log('SocketService[' + state + ']: Adding [' + requester + '] to subscriber list');
            		subscriberMap.set(requester, {'resolve': resolve, 'reject': reject});

            	} else if (state === 'failed') {

                	console.log('SocketService[' + state + ']: Rejecting [' + requester + ']');
            		reject(failureReason);

            	} else {

            		console.error('SocketService[' + state + ']: UNKNOWN state');
            	}
            });
        }
    };
})
.service('KioskService', function ($stomp, $q, $http) {

    return {
    	getKioskInfo: function () {
        	console.log('getKioskInfo() -> GET /info');
    		return $q(function(resolve, reject) {
        	    $http.get('/info')
        	    	 .then((response) => resolve(response.data), (response) => reject(response));
    		});
    	},
        depositCash: function (amount) {
        	var destinationBase = '/kiosk/commands/send/';
        	var commandName = 'executive.account.CDepositCash';
        	var destination = destinationBase + commandName;
        	console.log('depositCash(amount=' + amount + ') -> SEND ' + destination);
        	$stomp.send(destination, {'amount': amount});
        },
    	requestBalanceUpdate: function () {
        	console.log('requestBalanceUpdate() -> SEND /kiosk/commands/account/balance/get');
        	$stomp.send('/kiosk/commands/account/balance/get');
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
		clearSlip: function() {
        	var destinationBase = '/kiosk/commands/send/';
        	var commandName = 'executive.slipbuild.CClearPotentialSlip';
        	var destination = destinationBase + commandName;
        	console.log('clearSlip() -> SEND ' + destination);
        	$stomp.send(destination, {});
		},
		addSelection: function(selectionId) {
        	var destinationBase = '/kiosk/commands/send/';
        	var commandName = 'executive.slipbuild.CAddSelection';
        	var destination = destinationBase + commandName;
        	console.log('addSelection(selectionId=' + selectionId + ') -> SEND ' + destination);
        	$stomp.send(destination, {"selectionId":selectionId});
		},
		removeSelection: function(selectionId) {
        	var destinationBase = '/kiosk/commands/send/';
        	var commandName = 'executive.slipbuild.CRemoveSelection';
        	var destination = destinationBase + commandName;
        	console.log('removeSelection(selectionId=' + selectionId + ') -> SEND ' + destination);
        	$stomp.send(destination, {"selectionId":selectionId});
		},
    	requestSlipUpdate: function () {
        	console.log('requestSlipUpdate() -> SEND /kiosk/commands/slip/get');
        	$stomp.send('/kiosk/commands/slip/get');
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
