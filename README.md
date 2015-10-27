#Inter-working between SDN controllers as an OpenDaylight application

##Chapter 1

First, you need to learn how to write your application on OpenDaylight.

Read this and write a hello app: [HOW-TO-WRITE-YOUR-ODL-APP](./HOWTO.md)

##Chapter 2

MD-SAL is basically YANG datastore with DCN(pubsub) and RPC support.

If your SDN controller also follows this kind of data-driven architecture, it is possible to connect your SDN controller to MD-SAL via some kind of gateway:

```
[Your SDN controller]---pubsub(such as Redis)---[GW plugin]<-CRUD/DCN->[MD-SAL]

```

###Data-driven architecture
Usually, SDN controllers internally have datastore supporting pubsub:
- A combination of ZooKeeper and Cassandra
- A combination of Redis and another datastore
- Hazelcast
- MD-SAL
- And many others...

You may also add MongoDB and etcd to the list.

Plugins attached to datastore communicate with each other indirectly via the datastore's pubsub feature.

###The GW's role
- The construct is sort of clustering (ACT-ACT).
- OpenDaylight's CRUD/DCN is seen as MESSAGE on Redis.
- The GW subscribes channels on MD-SAL and Redis for example.
- The GW works as a pubsub relay and data model translator: translation between your data model schema (e.g., defined in your Java classes) and YANG Java binding generated by ODL YANG Tools.
- The data on Your SDN controller's datastore are synchronized with MD-SAL datastore via the GW.
- Users may use both your SDN controller's N.B. API and OpenDaylight's RESTCONF API.

##Coodination of SDN controllers
If everythings work in a same container (such as Karaf container), things are easy. If not, you may need to use something like ZooKeeper or etcd (or MD-SAL?) for coordinating SDN controllers.

Recommendation:

1. Run you SDN controller as a Karaf feature (a combination of OSGi bundles) and OpenDaylight in a same Karaf container (i.e., on a same JVM).
2. Use "embedded" pubsub server or develop a pubsub capability on your own, and avoid using an external pubsub server (such as Redis).

##HA(High-availability)

- A cluster of MD-SAL (three nodes)
- Instances of your SDN controller are attached to the cluster
- All the instances share the same view of data
```
                                MD-SAL clustering (RAFT-based)
[Instance A of your SDN controller]---[MD-SAL]--[MD-SAL]---[Instance B of your SDN controller]
                                            |    |
                                           [MD-SAL]
                                              |
                              [Instance C of your SDN controller]
```
