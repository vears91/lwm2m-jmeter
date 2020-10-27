# Please note that this project is not currently being maintained! It is kept for reference, but it is likely to have issues with current versions of JMeter and other libraries

# lwm2m-jmeter

This project is a plugin for [Apache JMeter](http://jmeter.apache.org/) that implements samplers and config elements for the [LightweightM2m protocol](http://openmobilealliance.org/iot/lightweight-m2m-lwm2m/). It allows JMeter threads to act as virtual LWM2M clients that can register, derregister and push observations to a LWM2M server.

This plugin can be used with JMeter to test non-functional and functional aspects of LWM2M servers. It uses lightweight mock LWM2M [clients](https://github.com/vears91/simplelwm2m) based on [Californium](https://github.com/eclipse/californium) to interact with the server. It has been tested with up to 2000 simultaneous clients generating load in a single commodity machine (a laptop with 8 GB RAM and an Intel Core i7-4710HQ CPU @ 2.50GHz processor), and more than 10,000 using JMeter's distributed mode in Amazon Web Services.

If you want to generate load without using JMeter or test simple use cases, you can use the [mock client](https://github.com/vears91/simplelwm2m) that is used in this plugin and write a scenario yourself. If you need a full-fledged LWM2M implementation, take a look at [Eclipse Leshan](https://github.com/eclipse/leshan).

# Installation

As with all JMeter plugins, you should copy the jar file to the "lib/ext" directory in JMeter's installation.

1. Clone this project with `git clone https://github.com/vears91/lwm2m-jmeter.git`
2. Go the project directory and build it or install it with maven `mvn install`.
3. After a successful build, go to the "target" directory and copy the generated jar-with-dependencies to JMeter's lib/ext directory `cp ./target/lwm2m-jmeter-version-jar-with-dependencies.jar /path/jmeter/installation/lib/ext`

When you open JMeter, you should be able to add the test elements of the plugin, like LWM2mConfig elements and NotifyObserverSampler.

# Usage

You can check out some simple test plans in the doc/examples/ directory of this repository. The basic steps to get started are the following:

1. Open JMeter 
2. Add a thread group
3. Right click the thread group and choose Add/Config Element/LwM2mConfig
4. Define the configuration of LWM2M clients in the LwM2mConfig element you added in the previous step. Here you will define the URI of the server to target, the available resources in each virtual client and registration options.
5. Add samplers from the samplers submenu when right clicking a thread group and going to the Add/Sampler submenu.

