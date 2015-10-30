package araobp.watcher;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.common.api.data.AsyncDataBroker.DataChangeScope;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import org.opendaylight.controller.sal.binding.api.BindingAwareProvider;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150904.GreetingRegistry;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150904.greeting.registry.GreetingRegistryEntry;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

public class HelloWatcher implements BindingAwareProvider, AutoCloseable {
  
  private static final Logger LOG = LoggerFactory.getLogger(HelloWatcher.class);

  @Override
  public void close() throws Exception {
    LOG.info("Watcher Closed");
    
  }

  @Override
  public void onSessionInitiated(ProviderContext session) {
    LOG.info("Watcher Session Initiated");
    DataBroker db = session.getSALService(DataBroker.class);
    Config config = new Config();
    HazelcastInstance hz = Hazelcast.newHazelcastInstance(config);
    IMap<String, String> map = hz.getMap("greeting-registry");
    HelloListener listener = new HelloListener(map);
    HazelcastListener hzListener = new HazelcastListener(db);
    map.addEntryListener(hzListener, true);
    InstanceIdentifier<GreetingRegistryEntry> iid =
        InstanceIdentifier.create(GreetingRegistry.class)
        .child(GreetingRegistryEntry.class);
    db.registerDataChangeListener(LogicalDatastoreType.OPERATIONAL,
        iid, listener, DataChangeScope.BASE);
  }
  
}
