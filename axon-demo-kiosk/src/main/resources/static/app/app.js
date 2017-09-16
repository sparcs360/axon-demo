'use strict';

angular.module('appKiosk', [
    'ui.bootstrap',
    'ngStomp'
])
.run(function ($stomp) { // instance-injector

    $stomp.setDebug(function (message) {
        console.log('$stomp: ' + message);
    });
});
