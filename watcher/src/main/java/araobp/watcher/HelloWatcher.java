package araobp.watcher;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.opendaylight.controller.sal.binding.api.BindingAwareProvider;

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
  }
  
}
