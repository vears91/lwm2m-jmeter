package com.lwm2mjmeter.jmeter.protocol.lwm2m;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public abstract class LwM2mAbstractSampler extends AbstractSampler {

    private static final Logger log = LoggingManager.getLoggerForClass();

    protected String endpoint = "";

    @Override
    public SampleResult sample(Entry entry) {
        if (endpoint.equals("")) {
            setEndpoint();
        }
        SampleResult result = new SampleResult();
        send(JMeterContextService.getContext(), result);
        return result;
    }

    public abstract void send(JMeterContext context, SampleResult result);

    private void setEndpoint() {
        int threadNumber = JMeterContextService.getContext().getThreadNum()+1;
        String machineName;
        try {
            machineName = InetAddress.getLocalHost().getHostName();
            endpoint = "jmeter-"+machineName+"-"+threadNumber;
        } catch (UnknownHostException ex) {
            log.error("An error occured trying to get the local host name");
            log.error(ex.getStackTrace().toString());
        }
    }

}
