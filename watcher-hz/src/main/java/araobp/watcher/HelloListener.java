package araobp.watcher;

import java.io.UnsupportedEncodingException;
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
import com.hazelcast.internal.ascii.rest.RestValue;

/**
 * This class listens to greeting-registry entry events emitted from MD-SAL, 
 * then write the value to the map on MD-SAL.
 * 
 * @author arao
 *
 */
public class HelloListener implements DataChangeListener {

  private static final Logger LOG = LoggerFactory.getLogger(HelloListener.class);
  private IMap<String, RestValue> map;

  public HelloListener(IMap<String, RestValue> map) {
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
      RestValue v;
      try {
        v = new RestValue(gr.getGreeting().getBytes("US-ASCII"), "text/plain".getBytes("US-ASCII"));
        RestValue greeting = map.get(k);
        if (!v.equals(greeting)) {
          map.put(k, v);
        } else {
          LOG.info("Already exist: {} {}", k, v);
        }
      } catch (UnsupportedEncodingException e) {
        LOG.error("unable to create value", e);
      }
    }
  }
}
