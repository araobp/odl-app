package araobp.watcher;

import java.util.concurrent.ExecutionException;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.ReadTransaction;
import org.opendaylight.controller.md.sal.binding.api.WriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.ReadFailedException;
import org.opendaylight.controller.md.sal.common.api.data.TransactionCommitFailedException;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150904.GreetingRegistry;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150904.greeting.registry.GreetingRegistryEntry;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150904.greeting.registry.GreetingRegistryEntryBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150904.greeting.registry.GreetingRegistryEntryKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.CheckedFuture;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.map.listener.EntryAddedListener;

public class HazelcastListener implements EntryAddedListener<String, String> {

  private static final Logger LOG = LoggerFactory.getLogger(HazelcastListener.class);
  DataBroker db;

  public HazelcastListener(DataBroker db) {
    this.db = db;
  }

  @Override
  public void entryAdded(EntryEvent<String, String> entry) {
    String k = entry.getKey();
    String v = entry.getValue();
    LOG.info("entryAdded event received: {} {}", k, v);

    GreetingRegistryEntryKey key = new GreetingRegistryEntryKey(entry.getKey());
    InstanceIdentifier<GreetingRegistryEntry> iid = InstanceIdentifier
        .create(GreetingRegistry.class)
        .child(GreetingRegistryEntry.class, new GreetingRegistryEntryKey(key));
    ReadTransaction rt = db.newReadOnlyTransaction();
    CheckedFuture<Optional<GreetingRegistryEntry>, ReadFailedException> rtFuture =
        rt.read(LogicalDatastoreType.OPERATIONAL, iid);
    try {
      Optional<GreetingRegistryEntry> opt = rtFuture.get();
      String greeting = opt.get().getGreeting();
      if (!greeting.equals(v)) {
        GreetingRegistryEntryBuilder builder = new GreetingRegistryEntryBuilder();
        GreetingRegistryEntry newGreeting = builder.setName(key.toString())
            .setKey(key)
            .setGreeting(entry.getValue())
            .build();
        WriteTransaction wt = db.newWriteOnlyTransaction();
        wt.put(LogicalDatastoreType.OPERATIONAL, iid, newGreeting);
        CheckedFuture<Void, TransactionCommitFailedException> wtFuture = wt.submit();
      }
    } catch (InterruptedException | ExecutionException e) {
      LOG.error("get failed", e);
    }
  }
}
