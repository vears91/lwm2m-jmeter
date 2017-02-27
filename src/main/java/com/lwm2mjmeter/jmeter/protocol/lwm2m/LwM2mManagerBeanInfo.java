package com.lwm2mjmeter.jmeter.protocol.lwm2m;

import java.beans.PropertyDescriptor;
import org.apache.jmeter.testbeans.BeanInfoSupport;

public class LwM2mManagerBeanInfo extends BeanInfoSupport {

    public LwM2mManagerBeanInfo() {
        super(LwM2mManager.class);

        createPropertyGroup("ConnectionInfo",
        new String[]{"serverURI", "registrationPath", "registrationDuration", "registerAtStart", "derregisterAtStop"});

        PropertyDescriptor p = property("serverURI");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "coap://localhost:5683");

        p = property("registrationPath");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "rd");

        p = property("registrationDuration");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, Long.valueOf(86400));

        p = property("registerAtStart");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, Boolean.TRUE);

        p = property("derregisterAtStop");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, Boolean.TRUE);

        createPropertyGroup("Device",
        new String[]{"resources"});

        p = property("resources");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "/3303/0/5700, /3303/0/5604");
    }

}
