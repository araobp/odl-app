#Inter-working between SDN controllers as an OpenDaylight application

##Background and motivation

It is time for reality check on SDN. See this: ["SDN hits rock bottom and FCoE is obsolete, say Gartner mages"](http://www.theregister.co.uk/2015/07/29/sdn_enthusiasm_dives_says_gartner/)

From my experiences in the area of SDN, I conclude that SDN is just about software architecture and engineering optimized for a specific use case, and there is no one-fits-all SDN controller or platform. From now on, we had better focus on good-quality open source software components and integrating them into a purpose-built system on PaaS.

Let's think about using OpenDaylight as a device driver for your SDN controller or vise versa.

##Chapter 1: Learning OpenDaylight MD-SAL

First, you need to learn how to write your application on OpenDaylight.

Read this and write a hello app: [HOW-TO-WRITE-YOUR-ODL-APP](./doc/CHAPTER1.md)

##Chapter 2: Architecture discussion

Architecture discussion for inter-working with your SDN controller: [ARCHITECTURE](./doc/CHAPTER2.md)

##Chapter 3: Sample implementation

I use Hazelcast for this sample implementation: [SYNC-WITH-HAZELCAST](./doc/CHAPTER3.md)

##Homework

This is the end of DIY on MD-SAL, and it is time to review what you have done: [HOMEWORK](./doc/HOMEWORK.md)
