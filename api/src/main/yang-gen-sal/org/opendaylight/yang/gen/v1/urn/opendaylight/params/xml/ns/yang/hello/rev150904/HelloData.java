package org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150904;
import org.opendaylight.yangtools.yang.binding.DataRoot;


/**
 * &lt;p&gt;This class represents the following YANG schema fragment defined in module &lt;b&gt;hello&lt;/b&gt;
 * &lt;br&gt;Source path: &lt;i&gt;META-INF/yang/hello.yang&lt;/i&gt;):
 * &lt;pre&gt;
 * module hello {
 *     yang-version 1;
 *     namespace "urn:opendaylight:params:xml:ns:yang:hello";
 *     prefix "hello";
 *
 *     revision 2015-09-04 {
 *         description "";
 *     }
 *
 *     container greeting-registry {
 *         list greeting-registry-entry {
 *             key "name"
 *             leaf name {
 *                 type string;
 *             }
 *             leaf greeting {
 *                 type string;
 *             }
 *         }
 *     }
 *
 *     rpc fetch-hello-world {
 *         input {
 *             leaf name {
 *                 type string;
 *             }
 *         }
 *         
 *         output {
 *             leaf greeting {
 *                 type string;
 *             }
 *         }
 *         status CURRENT;
 *     }
 *     rpc hello-world {
 *         input {
 *             leaf name {
 *                 type string;
 *             }
 *         }
 *         
 *         output {
 *             leaf greeting {
 *                 type string;
 *             }
 *         }
 *         status CURRENT;
 *     }
 * }
 * &lt;/pre&gt;
 *
 */
public interface HelloData
    extends
    DataRoot
{




    GreetingRegistry getGreetingRegistry();

}

