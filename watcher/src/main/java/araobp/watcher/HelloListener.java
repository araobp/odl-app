package araobp.watcher;

import java.util.Collection;
import java.util.Map;

import org.opendaylight.controller.md.sal.binding.api.DataChangeListener;
import org.opendaylight.controller.md.sal.common.api.data.AsyncDataChangeEvent;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150904.greeting.registry.GreetingRegistryEntry;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.core.IMap;

public class HelloListener implements DataChangeListener {

  private static final Logger LOG = LoggerFactory.getLogger(HelloListener.class);
  private IMap<String, String> map;
  
  public HelloListener(IMap<String, String> map) {
    this.map = map;
  }
  
  @Override
  public void onDataChanged(AsyncDataChangeEvent<InstanceIdentifier<?>, DataObject> dcn) {
    Map<InstanceIdentifier<?>, DataObject> data = dcn.getCreatedData();
    Collection<DataObject> values = data.values();
    for (DataObject value : values) {
      GreetingRegistryEntry gr = (GreetingRegistryEntry) value;
      LOG.info(gr.toString());
      String k = gr.getName();
      String v = gr.getGreeting();
      String greeting = map.get(k);
      if (!v.equals(greeting)) {
        map.put(k, v);
      } else {
        LOG.info("Already exist: {} {}", k, v);
      }
    }
  }
}
