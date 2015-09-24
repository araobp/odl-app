package araobp;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.ReadOnlyTransaction;
import org.opendaylight.controller.md.sal.binding.api.ReadTransaction;
import org.opendaylight.controller.md.sal.binding.api.WriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.ReadFailedException;
import org.opendaylight.controller.md.sal.common.api.data.TransactionCommitFailedException;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150904.FetchHelloWorldInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150904.FetchHelloWorldOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150904.FetchHelloWorldOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150904.GreetingRegistry;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150904.GreetingRegistryBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150904.HelloService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150904.HelloWorldInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150904.HelloWorldOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150904.HelloWorldOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150904.greeting.registry.GreetingRegistryEntry;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150904.greeting.registry.GreetingRegistryEntryBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150904.greeting.registry.GreetingRegistryEntryKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.CheckedFuture;
import com.google.common.util.concurrent.Futures;

public class HelloWorldImpl implements HelloService {

  private static final Logger LOG = LoggerFactory.getLogger(HelloProvider.class);
  private final DataBroker db;

  public HelloWorldImpl(DataBroker db) {
    this.db = db;
    initializeDataTree(db);
  }

  @Override
  public Future<RpcResult<HelloWorldOutput>> helloWorld(HelloWorldInput input) {
    HelloWorldOutputBuilder helloBuilder = new HelloWorldOutputBuilder();
    helloBuilder.setGreeting("Hello " + input.getName() + "!");
    HelloWorldOutput output = helloBuilder.build();
    writeToGreetingRegistry(input, output);
    return RpcResultBuilder.success(helloBuilder.build()).buildFuture();
  }

  @Override
  public Future<RpcResult<FetchHelloWorldOutput>> fetchHelloWorld(FetchHelloWorldInput input) {
    ReadTransaction transaction = db.newReadOnlyTransaction();
    InstanceIdentifier<GreetingRegistryEntry> iid = toInstanceIdentifier(input);
    GreetingRegistryEntry entry = null;
    try {
      entry = transaction.read(LogicalDatastoreType.OPERATIONAL, iid).get().get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    FetchHelloWorldOutputBuilder builder = new FetchHelloWorldOutputBuilder();
    builder.setGreeting(entry.getGreeting());
    return RpcResultBuilder.success(builder.build()).buildFuture();
  }

  private void initializeDataTree(DataBroker db) {
    LOG.info("Preparing to initialize the greeting registry");
    WriteTransaction transaction = db.newWriteOnlyTransaction();
    InstanceIdentifier<GreetingRegistry> iid = InstanceIdentifier.create(GreetingRegistry.class);
    GreetingRegistry greetingRegistry = new GreetingRegistryBuilder().build();
    transaction.put(LogicalDatastoreType.OPERATIONAL, iid, greetingRegistry);
    CheckedFuture<Void, TransactionCommitFailedException> future = transaction.submit();
    Futures.addCallback(future,
        new LoggingFuturesCallBack<>("Failed to create greeting registry", LOG));
  }

  private void writeToGreetingRegistry(HelloWorldInput input, HelloWorldOutput output) {
    WriteTransaction transaction = db.newWriteOnlyTransaction();
    InstanceIdentifier<GreetingRegistryEntry> iid = toInstanceIdentifier(input);
    GreetingRegistryEntry greeting = new GreetingRegistryEntryBuilder()
        .setGreeting(output.getGreeting())
        .setName(input.getName())
        .build();
    transaction.put(LogicalDatastoreType.OPERATIONAL, iid, greeting);
    CheckedFuture<Void, TransactionCommitFailedException> future = transaction.submit();
    Futures.addCallback(future, new LoggingFuturesCallBack<Void>(
        "Failed to write greeting to greeting registry", LOG));
  }

  private InstanceIdentifier<GreetingRegistryEntry> toInstanceIdentifier(HelloWorldInput input) {
    InstanceIdentifier<GreetingRegistryEntry> iid =
        InstanceIdentifier.create(GreetingRegistry.class)
            .child(GreetingRegistryEntry.class, new GreetingRegistryEntryKey(input.getName()));
    return iid;
  }

  private InstanceIdentifier<GreetingRegistryEntry>
      toInstanceIdentifier(FetchHelloWorldInput input) {
    InstanceIdentifier<GreetingRegistryEntry> iid =
        InstanceIdentifier.create(GreetingRegistry.class)
            .child(GreetingRegistryEntry.class, new GreetingRegistryEntryKey(input.getName()));
    return iid;
  }

  private String readFromGreetingRegistry(HelloWorldInput input) {
    String result = "Hello " + input.getName();
    ReadOnlyTransaction transaction = db.newReadOnlyTransaction();
    InstanceIdentifier<GreetingRegistryEntry> iid = toInstanceIdentifier(input);
    CheckedFuture<Optional<GreetingRegistryEntry>, ReadFailedException> future =
        transaction.read(LogicalDatastoreType.CONFIGURATION, iid);
    Optional<GreetingRegistryEntry> optional = Optional.absent();
    try {
      optional = future.checkedGet();
    } catch (ReadFailedException e) {
      LOG.warn("Reading greeting failed:", e);
    }
    if (optional.isPresent()) {
      result = optional.get().getGreeting();
    }
    return result;
  }

}
