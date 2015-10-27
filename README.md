#SDN controllers interworking as an OpenDaylight application

##Chapter 1

First, you need to learn how to write your application on OpenDaylight.

Read this and write a hello app: [HOW-TO-WRITE-YOUR-ODL-APP](./HOWTO.md)

##Chapter 2

MD-SAL is basically YANG datastore with DCN(pubsub) and RPC support.

If your SDN controller also follows this kind of data-driven architecture, it is possible to connect your SDN controller to MD-SAL via some kind of gateway:

```
[Your SDN controller]---pubsub(such as Redis)---[GW plugin]<-CRUD/DCN->[MD-SAL]

```

