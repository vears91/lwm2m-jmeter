# Example with Leshan server demo

The test plan in this example registers 10 clients to a Leshan server demo
instance running on localhost. The observe relationship is established by
issuing HTTP requests to Leshan's API (inside a only-once controller at the
beginning of the test).

The request is sent to 
`/api/clients/jmeter-${__machineName}-${__threadNum}/3303/0/5700/observe`
where `${__machineName}` and `${__threadNum}` are JMeter variables, since this
is the format used by the plugin to name the endpoints of the virtual clients.

The thread group runs 100 times, sending an observation of a temperature sensor
every second.

# Running the example

Make sure you have copied the jar with dependencies of this plugin to JMeter's
lib/ext directory.

Then run `jmeter -n -t leshan.jmx`

