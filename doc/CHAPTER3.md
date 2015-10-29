##Hazelcast and MD-SAL

Hazelcast can be embedded in your Java application, thus it can run in Karaf container with OpenDaylight.

Just include Hazelcast artifact(com.hazelcast/hazelcast/3.5.3) in your pom.xml to install it and to use Hazelcast's APIs from your application.

I modify "HelloListener.java" a little bit to synchronizes Hazelcast with the data tree on MD-SAL:
```
[Hazelcast]---[GW]---[MD-SAL]
```

(The old one was renamed as "HelloListener.java_wo_hazelcast")

And don't forget to include Hazelcast artifact(bundle) in Karaf features:
```
<bundle>mvn:com.hazelcast/hazelcast/3.5.3</bundle>
```

##Starting the app again

Start the sample application, then confirm that Hazelcast has started in Karaf container by checking the Karaf log:
```
2015-10-30 02:56:16,378 | INFO  | config-pusher    | MulticastJoiner                  | 171 - com.hazelcast - 3.5.3 | [192.168.1.110]:5701 [dev] [3.5.3]


Members [1] {
        Member [192.168.1.110]:5701 this
}
```

Also confirm that hello-watcher bundle has received DCN:
```
2015-10-30 02:57:22,250 | INFO  | lt-dispatcher-17 | HelloListener                    | 172 - araobp.hello-watcher - 1.0.0.SNAPSHOT | GreetingRegistryEntry{getGreeting=Hello OpenDaylight!, getName=OpenDaylight, augmentations={}}
2015-10-30 02:57:22,252 | INFO  | CommitFutures-1  | HelloProvider                    | 170 - araobp.hello-impl - 1.0.0.SNAPSHOT | Success! null
```

Then input the parameter via the RPC as I did in CHAPTER 1, then try the following URL to check that the same data (greeting registry entry) is on Hazelcast:
```
http://<ip address>:5701/hazelcast/rest/maps/greeting-registry/OpenDaylight
```

Note that you have to use IP address of the host on which Karaf container is running.


Quite easy!

