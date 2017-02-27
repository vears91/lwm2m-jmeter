package com.lwm2mjmeter.jmeter.protocol.lwm2m;

import java.beans.PropertyDescriptor;
import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.property.BooleanProperty;
import org.apache.poi.hsmf.datatypes.PropertyValue.BooleanPropertyValue;

public class NotifyObserversSamplerBeanInfo extends BeanInfoSupport {

    public NotifyObserversSamplerBeanInfo() {
        super(NotifyObserversSampler.class);

        createPropertyGroup("Sensor",
        new String[]{"objectId", "instanceId", "resourceId"});

        PropertyDescriptor p = property("objectId");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, Integer.valueOf(3303));

        p = property("instanceId");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, Integer.valueOf(0));

        p = property("resourceId");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, Integer.valueOf(5700));

    }

}
