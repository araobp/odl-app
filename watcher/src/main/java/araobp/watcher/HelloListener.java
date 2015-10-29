package araobp.watcher;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.opendaylight.controller.md.sal.binding.api.DataChangeListener;
import org.opendaylight.controller.md.sal.common.api.data.AsyncDataChangeEvent;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150904.greeting.registry.GreetingRegistryEntry;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class HelloListener implements DataChangeListener {

  private static final Logger LOG = LoggerFactory.getLogger(HelloListener.class);
  
  private ConcurrentMap<String, String> map;
  
  public HelloListener() {
    super();
    Config config = new Config();
    HazelcastInstance h = Hazelcast.newHazelcastInstance(config);
    map = h.getMap("greeting-registry");
  }
  
  @Override
  public void onDataChanged(AsyncDataChangeEvent<InstanceIdentifier<?>, DataObject> dcn) {
    Map<InstanceIdentifier<?>, DataObject> data = dcn.getCreatedData();
    Collection<DataObject> values = data.values();
    for (DataObject value : values) {
      GreetingRegistryEntry gr = (GreetingRegistryEntry) value;
      LOG.info(gr.toString());
      toHazelcast(gr);
    }
  }

  private void toHazelcast(GreetingRegistryEntry gr) {
    map.put(gr.getName(), gr.getGreeting());
  }

}
