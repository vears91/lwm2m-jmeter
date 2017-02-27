package com.lwm2mjmeter.jmeter.protocol.lwm2m;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import com.simplelwm2m.simplelwm2m.*;

public class NotifyObserversSampler extends LwM2mAbstractSampler implements TestBean {

    private static final Logger log = LoggingManager.getLoggerForClass();
    protected Random rng = new Random();

    private String objectId;
    private String instanceId;
    private String resourceId;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String oid) {
        objectId = oid;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public void send(JMeterContext context, SampleResult result) {
        JMeterVariables variables = context.getVariables();
        ConcurrentHashMap<String, MockLwM2mClient> clients = (ConcurrentHashMap<String, MockLwM2mClient>) variables.getObject("lwm2mClients");
        MockLwM2mClient client = clients.get(endpoint);
        if (client != null) {
            SimpleResource resource = (SimpleResource) client.getObject(objectId).getChild(instanceId).getChild(resourceId);
            result.sampleStart();
            if (resource != null) {
                resource.setResourceValue(Float.toString(rng.nextInt(100)));
                result.setSuccessful(true);
                log.debug("Sent observation for " + objectId + "/" + instanceId + "/" + resourceId);
            } else {
                result.setSuccessful(false);
                log.debug("Could not send observation for " + objectId + "/" + instanceId + "/" + resourceId);
            }
            result.sampleEnd();
        }
    }

}
