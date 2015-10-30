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
import com.hazelcast.internal.ascii.rest.RestValue;

import org.opendaylight.controller.sal.binding.api.BindingAwareProvider;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150904.GreetingRegistry;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150904.greeting.registry.GreetingRegistryEntry;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

/**
 * This class gets a reference to MD-SAL's data broker at start up,
 * then create and run two instances: HelloListener and HazelcastListner.
 * 
 * @author arao
 */
public class HelloWatcher implements BindingAwareProvider, AutoCloseable {
  
  private static final Logger LOG = LoggerFactory.getLogger(HelloWatcher.class);

  @Override
  public void close() throws Exception {
    LOG.info("Watcher Closed");
    
  }

  @Override
  public void onSessionInitiated(ProviderContext session) {
    LOG.info("Watcher Session Initiated");

    // Obtains a reference to MD-SAL's data broker.
    DataBroker db = session.getSALService(DataBroker.class);
    
    // Starts a Hazelcast instance.
    Config config = new Config();
    HazelcastInstance hz = Hazelcast.newHazelcastInstance(config);
    
    // Creates a map on the Hazelcast instance.
    IMap<String, RestValue> map = hz.getMap("greeting-registry");
    
    // Starts HazelcastListener
    HelloListener listener = new HelloListener(map);
    HazelcastListener hzListener = new HazelcastListener(db);
    map.addEntryListener(hzListener, true);
    
    // Starts HelloListener
    InstanceIdentifier<GreetingRegistryEntry> iid =
        InstanceIdentifier.create(GreetingRegistry.class)
        .child(GreetingRegistryEntry.class);
    db.registerDataChangeListener(LogicalDatastoreType.OPERATIONAL,
        iid, listener, DataChangeScope.BASE);
  }
  
}
