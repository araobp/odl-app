#Inter-working between SDN controllers as an OpenDaylight application

##Chapter 1: Learning OpenDaylight MD-SAL

First, you need to learn how to write your application on OpenDaylight.

Read this and write a hello app: [HOW-TO-WRITE-YOUR-ODL-APP](./CHAPTER1.md)

##Chapter 2: Architecture discussion

Architecture discussion for inter-working with your SDN controller: [ARCHITECTURE](./CHAPTER2.md)

##Chapter 3: Sample implementation

I am going to use Redis, ZooKeeper, Cassandra, Hazelcast, etcd or MongoDB as a datastore for your SDN controller in this sample implementation.

```
[datastore]---[GW]---[MD-SAL]
```
