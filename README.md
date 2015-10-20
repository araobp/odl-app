#How to write your OpenDaylight application

##[Step 1] Maven repo setting for your $HOME/.m2
https://wiki.opendaylight.org/view/GettingStarted:Development_Environment_Setup
```
cp -n ~/.m2/settings.xml{,.orig} ; \
wget -q -O - https://raw.githubusercontent.com/opendaylight/odlparent/master/settings.xml > ~/.m2/settings.xml
```

##[Step 2] ODL project skeleton generation by making use of maven "startup project archtype"
OpenDaylight project provides "startup project archetype": https://wiki.opendaylight.org/view/OpenDaylight_Controller:MD-SAL:Startup_Project_Archetype

I just used the archetype to generate a maven project skeleton and added some codes.

##[Step 3] Confirm the directory structure that the archtype has generated

```
.
├── api   ==> YANG data model
├── artifacts
├── features  ==> Karaf features
├── impl  ==> Implementation of this appl.
├── watcher ==> Yet another bundle.
├── karaf  ==> Karaf itself
├── pom.xml
```

Note: I added "watcher" sub-module to the basic skeleton, to see if a new bundle can be added to the maven artifact.

##[Step 4] Define YANG model

This is the heart of the model-driven architecture:
[hello.yang](./api/src/main/yang/hello.yang)

Then,
```
$ cd api
$ mvn clean install -DskipTests=true
```
That will generate Java binding (api bundle artifact) from the YANG model.

You include the artifact in your pom.xml of your project to use the APIs.

##[Step 5] Write your codes

- src/main/yang ==> YANG model to manage your bundle via ODL Config Subsystem
- src/main/config ==> The data to be sent to ODL via Config Subsystem (NETCONF) to configure your bundle at start up
- src/main/java ==> The bundle you create

For example,
- [YANG model for "hello-watcher" bundle](~/watcher/src/main/yang/hello-watcher.yang)
- [XML config for "hello-watcher" bundle](~/watcher/src/main/config/default-config.xml)
- [Java codes for "hello-watcher" bundle](~/watcher/src/main/java/araobp/watcher)
- [Java codes generated by maven to wire MD-SAL with the code above](~/watcher/src/main/java/org)

##[Step 6] Edit features
Since I added a new bundle "hello-watcher", I had to modify the following files to add the bundle to the feature "hello":
- [pom.xml](./features/pom.xml)
- [features.xml](./features/src/main/features/features.xml)
```
    <bundle>mvn:araobp/hello-impl/${project.version}</bundle>
    <configfile finalname="${configfile.directory}/20-hello-impl.xml">mvn:araobp/hello-impl/${project.version}/xml/config</configfile>
    <bundle>mvn:araobp/hello-watcher/${project.version}</bundle>
    <configfile finalname="${configfile.directory}/21-hello-watcher.xml">mvn:araobp/hello-watcher/${project.version}/xml/config</configfile>
```
Especially, "20-hello-impl.xml" and "21-hello-watcher.xml" are very important to pass ODL DataBroker reference to the bundles at start up. ODL's Config Subsystem just reads these XML files at start up, and send them to ODL via NETCONF.

Those XML files are copied to the following directory:
```
~/odl-app/karaf/target/assembly/etc/opendaylight/karaf$ ls
00-netty.xml   04-xsql.xml        08-authn-config.xml    20-hello-impl.xml
01-md-sal.xml  05-clustering.xml  10-rest-connector.xml  21-hello-watcher.xml
```
##[Step 7] Build the app
```
$ cd <root>
$ mvn clean install -DskipTests=true
```
##[Step 8] Start Karaf

```
$ cd $HOME/odl-app/karaf/target/assembly/bin
$ ./karaf
```

##[Step 9] Check if the bundles you have created has started in Karaf container
```
opendaylight-user@root>la | grep hello
169 | Resolved |  80 | 1.0.0.SNAPSHOT                            | hello-api                           
170 | Resolved |  80 | 1.0.0.SNAPSHOT                            | hello-impl                          
171 | Resolved |  80 | 1.0.0.SNAPSHOT                            | hello-watcher

opendaylight-user@root>log:tail
                              :
2015-10-20 21:50:24,812 | INFO  | er [_value=8185] | WebSocketServer                  | 234 - org.opendaylight.controller.sal-rest-connector - 1.2.0.SNAPSHOT | Web socket server started at port 8185.
2015-10-20 21:50:25,034 | INFO  | config-pusher    | HelloProvider                    | 170 - araobp.hello-impl - 1.0.0.SNAPSHOT | HelloProvider Session Initiated
2015-10-20 21:50:25,045 | INFO  | config-pusher    | HelloProvider                    | 170 - araobp.hello-impl - 1.0.0.SNAPSHOT | Preparing to initialize the greeting registry
2015-10-20 21:50:25,633 | INFO  | CommitFutures-0  | HelloProvider                    | 170 - araobp.hello-impl - 1.0.0.SNAPSHOT | Success! null
2015-10-20 21:50:25,860 | INFO  | config-pusher    | HelloWatcher                     | 171 - araobp.hello-watcher - 1.0.0.SNAPSHOT | Watcher Session Initiated
```
It's working! Yeah!

##[Step 10] Check if the RPC you have made is working properly

Open [RESTCONF API doc exploler](http://localhost:8181/apidoc/explorer/index.html), find "hello", copy and paste the following input data into the GUI input parameter form:

```
{"hello:input": { "name":"OpenDaylihgt"}}
```
