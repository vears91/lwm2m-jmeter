package com.lwm2mjmeter.jmeter.protocol.lwm2m;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.testelement.ThreadListener;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import com.simplelwm2m.simplelwm2m.MockLwM2mClient;
import com.simplelwm2m.simplelwm2m.SimpleResource;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public abstract class LwM2mManager extends ConfigTestElement implements LoopIterationListener, TestBean, TestStateListener, ThreadListener {

    private static final Logger log = LoggingManager.getLoggerForClass();

    /**
    * LWM2M endpoint
    */
    protected String endpoint = "";

    /**
    * URI for the LWM2M server in the format coap://host:port
    */
    protected String serverURI;

    /**
    * Path to target during registration
    */
    protected String registrationPath;

    /**
    * Duration in seconds to set for the duration option at registration
    */
    protected long registrationDuration;

    /**
    * Send a registration request when starting the client
    */
    protected boolean registerAtStart;

    /**
    * Send a derregistration request when stopping the client
    */
    protected boolean derregisterAtStop;

    /**
    * Comma-separated resources, in the format /objectId/instanceId/resourceId
    */
    protected String resources;

    /**
    * Keeps track of the mock client used by each thread
    */
    protected static ConcurrentHashMap<String, MockLwM2mClient> clients;

    /**
    *
    * @return LWM2M server URI to target
    */
    public String getServerURI() {
        return serverURI;
    }

    /**
    * Set the URI of the LWM2M server
    * @param serverURI
    */
    public void setServerURI(String serverURI) {
        this.serverURI = serverURI;
    }

    /**
    * Get path to target during registration
    * @return Registration path
    */
    public String getRegistrationPath() {
        return registrationPath;
    }

    /**
    * Set path to target during registration
    * @param registrationPath
    */
    public void setRegistrationPath(String registrationPath) {
        this.registrationPath = registrationPath;
    }

    /**
    * Get duration option for registration
    * @return
    */
    public long getRegistrationDuration() {
        return registrationDuration;
    }

    /**
    * Set duration option for registration
    * @param registrationDuration
    */
    public void setRegistrationDuration(long registrationDuration) {
        this.registrationDuration = registrationDuration;
    }

    /**
    *
    * @return
    */
    public boolean isRegisterAtStart() {
        return registerAtStart;
    }

    /**
    * Determine if a registration should be sent when a mock client is started
    * @param registerAtStart
    */
    public void setRegisterAtStart(boolean registerAtStart) {
        this.registerAtStart = registerAtStart;
    }

    /**
    * Determine if a registration should be sent when a mock client is stopped
    * @return
    */
    public boolean isDerregisterAtStop() {
        return derregisterAtStop;
    }

    /**
    *
    * @param derregisterAtStop
    */
    public void setDerregisterAtStop(boolean derregisterAtStop) {
        this.derregisterAtStop = derregisterAtStop;
    }

    /**
    * Set available /object/instance/resources in the mock clients
    * @param resources
    */
    public void setResources(String resources) {
        this.resources = resources;
    }

    /**
    *
    * @return String with the /object/instance/resource in the mock clients
    */
    public String getResources() {
        return resources;
    }

    /**
    * Retrieves the mock client used by the calling thread
    * @return MockLwM2mClient for this thread
    */
    protected MockLwM2mClient getThreadClient() {
        int threadNumber = JMeterContextService.getContext().getThreadNum()+1;
        String machineName;
        try {
            machineName = InetAddress.getLocalHost().getHostName();
            endpoint = "jmeter-"+machineName+"-"+threadNumber;
        } catch (UnknownHostException ex) {
            log.error("An error occured trying to get the local host name");
            log.error(ex.getStackTrace().toString());
        }

        MockLwM2mClient device = clients.get(endpoint);
        if (device == null && !endpoint.equals("")) {
            try {
                device = new MockLwM2mClient(endpoint, serverURI);
                device.setRegistrationDuration(registrationDuration);
                device.setRegistrationPath(registrationPath);
                addResources(device, resources);
                device.start(registerAtStart);
                clients.put(endpoint, device);
            } catch (Exception ex) {
                log.error(ex.getMessage());
                log.error(ex.getStackTrace().toString());
            }
        }
        return device;
    }

    /**
    * Add the resources defined in the resources property to the mock LWM2M clients
    * @param device
    * @param resources
    */
    private void addResources(MockLwM2mClient device, String resources) {
        try {
            String r[] = resources.split(",");
            for (String resourceDefinition : r) {
                resourceDefinition = resourceDefinition.trim();
                String parts[] = resourceDefinition.split("/");
                String objId  = parts[1];
                String instId = parts[2];
                String resId = parts[3];
                SimpleResource resource = new SimpleResource(resId);

                SimpleResource obj;
                if (device.getObject(objId) == null)
                obj = new SimpleResource(objId);
                else
                obj = (SimpleResource)  device.getObject(objId);

                SimpleResource inst;
                if (obj.getChild(instId) == null)
                inst = new SimpleResource(instId);
                else
                inst = (SimpleResource) obj.getChild(instId);

                inst.add(resource);
                obj.add(inst);
                device.addObject(objId, obj);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Incorrect format for resources. Please use comma-separated"
            + "definitions with the format /objectId/instanceId/resourceId");
        }
    }

    /**
    * Initialize the clients for each thread during iteration start
    */
    @Override
    public void iterationStart(LoopIterationEvent loopIterationEvent) {
        if (clients == null) {
            clients = new ConcurrentHashMap<>();
        }
        JMeterVariables variables = JMeterContextService.getContext().getVariables();
        variables.putObject("lwm2mClients", clients);
        getThreadClient();
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public void threadStarted() {
        log.info("Thread started " + new Date());
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public void threadFinished() {
        log.info("finished " + endpoint);
        MockLwM2mClient client = clients.get(endpoint);
        if (client != null) {
            client.stop(derregisterAtStop);
            clients.remove(endpoint);
        }
        log.info("Thread ended " + new Date());
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public void testEnded() {
        log.info("Test ended " + new Date());
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public void testEnded(String host) {
        log.info("Test ended " + new Date());
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public void testStarted() {
        log.info("Test started " + new Date());
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public void testStarted(String host) {
        log.info("Test ended " + new Date());
    }

}
