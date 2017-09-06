'use strict';

angular.module('appCounter')
.service('SocketService', function ($stomp, $q) {

    var isConnected = false;

    return {
        connect: function () {
            return $q(function (resolve, reject) {
                if (!isConnected) {
                	console.log('Attempting to connect to /counter-websocket');
                    $stomp.connect('/counter-websocket')
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
.service('CounterService', function ($stomp, $q) {

    return {

        getKioskStatus: function () {
            return $q(function (resolve, reject) {
                $stomp.subscribe('/counter/commands/kiosks/get-status', function (data) {
                    resolve(data);
                });
            });
        },
        subscribeToKioskUpdates: function () {
            var deferred = $q.defer();
        	console.log('subscribeToKioskUpdates() -> SUBSCRIBE /topic/kiosks/events');
            $stomp.subscribe('/topic/kiosks/events', function (data) {
            	console.log('subscribeToKioskUpdates() <- ' + JSON.stringify(data));
                deferred.notify(data);
            });
            return deferred.promise;
        }    
    };
})
;
