package org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.watcher.rev141210.modules.module.configuration;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.watcher.rev141210.modules.module.configuration.hello.watcher.Broker;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.controller.config.rev130405.modules.module.Configuration;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.binding.Augmentable;


/**
 * &lt;p&gt;This class represents the following YANG schema fragment defined in module &lt;b&gt;hello-watcher&lt;/b&gt;
 * &lt;br&gt;(Source path: &lt;i&gt;META-INF/yang/hello-impl.yang&lt;/i&gt;):
 * &lt;pre&gt;
 * case hello-watcher {
 *     container broker {
 *         leaf type {
 *             type leafref;
 *         }
 *         leaf name {
 *             type leafref;
 *         }
 *         uses service-ref {
 *             refine (urn:opendaylight:params:xml:ns:yang:hello:watcher?revision=2014-12-10)type {
 *                 leaf type {
 *                     type leafref;
 *                 }
 *             }
 *         }
 *     }
 * }
 * &lt;/pre&gt;
 * The schema path to identify an instance is
 * &lt;i&gt;hello-watcher/modules/module/configuration/(urn:opendaylight:params:xml:ns:yang:hello:watcher?revision=2014-12-10)hello-watcher&lt;/i&gt;
 *
 */
public interface HelloWatcher
    extends
    DataObject,
    Augmentable<org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.watcher.rev141210.modules.module.configuration.HelloWatcher>,
    Configuration
{



    public static final QName QNAME = org.opendaylight.yangtools.yang.common.QName.cachedReference(org.opendaylight.yangtools.yang.common.QName.create("urn:opendaylight:params:xml:ns:yang:hello:watcher","2014-12-10","hello-watcher"));

    Broker getBroker();

}

