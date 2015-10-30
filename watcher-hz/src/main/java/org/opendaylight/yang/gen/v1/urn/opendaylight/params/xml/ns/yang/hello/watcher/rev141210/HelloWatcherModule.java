package org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.watcher.rev141210;

import araobp.watcher.HelloWatcher;

public class HelloWatcherModule extends org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.watcher.rev141210.AbstractHelloWatcherModule {
    public HelloWatcherModule(org.opendaylight.controller.config.api.ModuleIdentifier identifier, org.opendaylight.controller.config.api.DependencyResolver dependencyResolver) {
        super(identifier, dependencyResolver);
    }

    public HelloWatcherModule(org.opendaylight.controller.config.api.ModuleIdentifier identifier, org.opendaylight.controller.config.api.DependencyResolver dependencyResolver, org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.watcher.rev141210.HelloWatcherModule oldModule, java.lang.AutoCloseable oldInstance) {
        super(identifier, dependencyResolver, oldModule, oldInstance);
    }

    @Override
    public void customValidation() {
        // add custom validation form module attributes here.
    }

    @Override
    public java.lang.AutoCloseable createInstance() {
      HelloWatcher watcher = new HelloWatcher();
      getBrokerDependency().registerProvider(watcher);
      return watcher;
    }

}
