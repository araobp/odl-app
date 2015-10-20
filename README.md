#OpenDaylight HelloWorld app

##Maven repo setting
https://wiki.opendaylight.org/view/GettingStarted:Development_Environment_Setup
```
cp -n ~/.m2/settings.xml{,.orig} ; \
wget -q -O - https://raw.githubusercontent.com/opendaylight/odlparent/master/settings.xml > ~/.m2/settings.xml
```

##ODL project skeleton generated by maven "startup project archtype"
OpenDaylight project provides "startup project archetype": https://wiki.opendaylight.org/view/OpenDaylight_Controller:MD-SAL:Startup_Project_Archetype

I just used the archetype to generate a maven project skeleton and added some codes.

##Directory structure

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

##Building the app
```
$ mvn clean install -DskipTests=true
```
Then start karaf/target/assembly/bin/karaf

```
opendaylight-user@root>la | grep hello
opendaylight-user@root>la | grep hello
169 | Resolved |  80 | 1.0.0.SNAPSHOT                            | hello-api                           
170 | Resolved |  80 | 1.0.0.SNAPSHOT                            | hello-impl                          
171 | Resolved |  80 | 1.0.0.SNAPSHOT                            | hello-watcher
```

##

Open [RESTCONF API doc exploler](http://localhost:8181/apidoc/explorer/index.html), find "hello", copy and paste the following input data into the GUI input parameter form:

```
{"hello:input": { "name":"OpenDaylihgt"}}
```
