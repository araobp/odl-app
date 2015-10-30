curl --basic --user admin:admin -v -X POST -H "Content-Type: application/json" http://localhost:8181/restconf/operations/hello:hello-world -d '{"hello:input": { "name":"OpenDaylight"}}'
