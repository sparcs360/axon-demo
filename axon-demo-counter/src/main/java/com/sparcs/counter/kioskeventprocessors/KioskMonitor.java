package com.sparcs.counter.kioskeventprocessors;

import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sparcs.kiosk.KioskEvent;

@Component
public class KioskMonitor {
    
    private static final Logger LOG = LoggerFactory.getLogger(KioskMonitor.class);

    @EventHandler
    void on(KioskEvent event) {

        LOG.trace("{}", event);
    }
}
