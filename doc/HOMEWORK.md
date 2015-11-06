##Homework

[1] You have to summarize in a report what YANG is about and why a lot of people try to use it as a common modeling language for networking.

[2] We have implemented the sample GW and we have got that model conversion is NOT easy. How are standardization organizations such as OpenConfig and IETF trying to solve this problem? 

[3] Why are conflict resolution and transaction/rollback necessary for your production network? What happens in case of network partition between the controller and your networking equipment? Does OpenDaylight provide any capabilities for conflict resolution and transaction/rollback?

[4] Is programming on MD-SAL easy or difficult? Why do you think so?

[5] Modify the GW implementation to run Hazelcast instance outside of Karaf container:
```
                   +----------------------------------------+
                   |                                        |
Hazelcast---socket---[hello-watcher]--MD-SAL--[hello-impl]  |
instance           |  GW                                    |
                   +----------------------------------------+
                    Karaf container

```
[6] Modify the GW implementation and use Redis or ActiveMQ instead of Hazelcast. Create a mini-application in golang or python that interacts with hello-impl via the message bus.
```
                   +----------------------------------------+
                   |                                        |
Redis-------socket---[hello-watcher]--MD-SAL--[hello-impl]  |
  |                |  GW                                    |
client             +----------------------------------------+
(golang)            Karaf container

```
You may also use part of the code here: https://wiki.opendaylight.org/view/Messaging4Transport:AMQP_Bindings_for_MD-SAL
