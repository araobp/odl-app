## Hazelcast and MD-SAL

Hazelcast can be embedded in your Java application, thus it can run in Karaf container with OpenDaylight.

Just include Hazelcast artifact(com.hazelcast/hazelcast/3.5.3) in your pom.xml to install it, and also to use Hazelcast's APIs from your application.

I modify "HelloListener.java" a little bit to synchronizes Hazelcast with the data tree on MD-SAL:
```
[Hazelcast]---[GW]---[MD-SAL]
```

And don't forget to include Hazelcast artifact(bundle) in Karaf features:
```
<bundle>mvn:com.hazelcast/hazelcast/3.5.3</bundle>
```

## Starting the app again

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

Quite easy? Not at all...

I implemented a Hazelcast map entry listener and I realized that this architecture causes an inifite loop:
```
[Hazelcast]             [GW]              [MD-SAL]
     |                    |                   |
     |<--put()------------|<--onDataChanged()-|
     |---entryAdded()---->|---put()---------->|
     |<--put()------------|<--onDataChanged()-|
     |---entryUpdated()-->|---put()---------->|
     |        :           |        :          |
```

 So I improved the code to avoid the infinite loop like this:
 - Check if the key-value pair already exists on MD-SAL before put().
 - Check if the key-value pair already exists on Hazelcast before put().

And finally it worked!
```
~/odl-app/util$ ./md-sal-restconf-post.sh
* About to connect() to localhost port 8181 (#0)
*   Trying ::1...
* connected
* Connected to localhost (::1) port 8181 (#0)
* Server auth using Basic with user 'admin'
> POST /restconf/operations/hello:hello-world HTTP/1.1
> Authorization: Basic YWRtaW46YWRtaW4=
> User-Agent: curl/7.26.0
> Host: localhost:8181
> Accept: */*
> Content-Type: application/json
> Content-Length: 41
>
* upload completely sent off: 41 out of 41 bytes
* additional stuff not fine transfer.c:1037: 0 0
* additional stuff not fine transfer.c:1037: 0 0
* HTTP 1.1 or later with persistent connection, pipelining supported
< HTTP/1.1 200 OK
< Content-Type: application/yang.operation+json
< Transfer-Encoding: chunked
< Server: Jetty(8.1.15.v20140411)
<
* Connection #0 to host localhost left intact
{"output":{"greeting":"Hello OpenDaylight!"}}* Closing connection #0

~/odl-app/util$ ./md-sal-restconf-get.sh
* About to connect() to localhost port 8181 (#0)
*   Trying ::1...
* connected
* Connected to localhost (::1) port 8181 (#0)
* Server auth using Basic with user 'admin'
> GET /restconf/operational/hello:greeting-registry HTTP/1.1
> Authorization: Basic YWRtaW46YWRtaW4=
> User-Agent: curl/7.26.0
> Host: localhost:8181
> Accept: */*
> Content-Type: application/json
>
* additional stuff not fine transfer.c:1037: 0 0
* HTTP 1.1 or later with persistent connection, pipelining supported
< HTTP/1.1 200 OK
< Content-Type: application/yang.data+json
< Vary: Accept-Encoding, User-Agent
< Transfer-Encoding: chunked
< Server: Jetty(8.1.15.v20140411)
<
* Connection #0 to host localhost left intact
{"greeting-registry":{"greeting-registry-entry":[{"name":"OpenDaylight","greeting":"RestValue{contentType='text/plain', value=\"Hello OpenDaylight!\"}"}]}}* Closing connection #0

~/odl-app/util$ ./hz-rest-get.sh OpenDaylight
* About to connect() to localhost port 5701 (#0)
*   Trying ::1...
* connected
* Connected to localhost (::1) port 5701 (#0)
> GET /hazelcast/rest/maps/greeting-registry/OpenDaylight HTTP/1.1
> User-Agent: curl/7.26.0
> Host: localhost:5701
> Accept: */*
> Content-Type: text/plain
>
* additional stuff not fine transfer.c:1037: 0 0
* HTTP 1.1 or later with persistent connection, pipelining supported
< HTTP/1.1 200 OK
< Content-Type: text/plain
< Content-Length: 19
<
* Connection #0 to host localhost left intact
Hello OpenDaylight!* Closing connection #0

~/odl-app/util$ ./hz-rest-post.sh Hazelcast GutenTag
* About to connect() to localhost port 5701 (#0)
*   Trying ::1...
* connected
* Connected to localhost (::1) port 5701 (#0)
> POST /hazelcast/rest/maps/greeting-registry/Hazelcast HTTP/1.1
> User-Agent: curl/7.26.0
> Host: localhost:5701
> Accept: */*
> Content-Type: text/plain
> Content-Length: 8
>
* upload completely sent off: 8 out of 8 bytes
* HTTP 1.1 or later with persistent connection, pipelining supported
< HTTP/1.1 200 OK
< Content-Length: 0
<
* Connection #0 to host localhost left intact
* Closing connection #0

~/odl-app/util$ ./hz-rest-get.sh Hazelcast
* About to connect() to localhost port 5701 (#0)
*   Trying ::1...
* connected
* Connected to localhost (::1) port 5701 (#0)
> GET /hazelcast/rest/maps/greeting-registry/Hazelcast HTTP/1.1
> User-Agent: curl/7.26.0
> Host: localhost:5701
> Accept: */*
> Content-Type: text/plain
>
* HTTP 1.1 or later with persistent connection, pipelining supported
< HTTP/1.1 200 OK
< Content-Type: text/plain
< Content-Length: 53
<
* Connection #0 to host localhost left intact
RestValue{contentType='text/plain', value="GutenTag"}* Closing connection #0

~/odl-app/util$ ./md-sal-restconf-get.sh
* About to connect() to localhost port 8181 (#0)
*   Trying ::1...
* connected
* Connected to localhost (::1) port 8181 (#0)
* Server auth using Basic with user 'admin'
> GET /restconf/operational/hello:greeting-registry HTTP/1.1
> Authorization: Basic YWRtaW46YWRtaW4=
> User-Agent: curl/7.26.0
> Host: localhost:8181
> Accept: */*
> Content-Type: application/json
>
* additional stuff not fine transfer.c:1037: 0 0
* HTTP 1.1 or later with persistent connection, pipelining supported
< HTTP/1.1 200 OK
< Content-Type: application/yang.data+json
< Vary: Accept-Encoding, User-Agent
< Transfer-Encoding: chunked
< Server: Jetty(8.1.15.v20140411)
<
* Connection #0 to host localhost left intact
{"greeting-registry":{"greeting-registry-entry":[{"name":"Hazelcast","greeting":"RestValue{contentType='text/plain', value=\"GutenTag\"}"},{"name":"OpenDaylight","greeting":"RestValue{contentType='text/plain', value=\"Hello OpenDaylight!\"}"}]}}* Closing connection #0
```

BUT what about auto-conflict-detection/auto-conflict-resolution, transaction collisions between Hazelcast and MD-SAL, and transaction/rollback features??? MD-SAL does not have any support for those problems --- never ending probrems I have not been able to resolve for the past two or three years...


