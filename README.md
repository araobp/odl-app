#OpenDaylight HelloWorld app

OpenDaylight project provides "startup project archetype": https://wiki.opendaylight.org/view/OpenDaylight_Controller:MD-SAL:Startup_Project_Archetype

I just used the archetype to generate a maven project skeleton and added some codes.

##Directory structure

```
.
├── api   ==> YANG data model
├── artifacts
├── features  ==> Karaf features
├── impl  ==> Implementation of this appl.
├── karaf  ==> Karaf itself
├── pom.xml
```

##Building the app
``
$ mvn clean install -DskipTests
```
Then start karaf/target/assembly/bin/karaf

```
opendaylight-user@root>la | grep hello
169 | Active   |  80 | 1.0.0.SNAPSHOT                            | hello-api                                                                
170 | Active   |  80 | 1.0.0.SNAPSHOT                            | hello-impl                                          
```

##

Open [RESTCONF API doc exploler](http://localhost:8181/apidoc/explorer/index.html), find "hello", copy and paste the following input data into the GUI input parameter form:

```
{"hello:input": { "name":"OpenDaylihgt"}}
```
