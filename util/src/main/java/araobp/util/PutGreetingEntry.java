package araobp.util;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class PutGreetingEntry {

  private HazelcastInstance hz;
  private static IMap<String, String> map;

  public PutGreetingEntry() {
    Config config = new Config();
    hz = Hazelcast.newHazelcastInstance(config);
    map = hz.getMap("greeting-registry");
  }

  public static void main(String[] args) {
    new PutGreetingEntry();
    String k = args[0];
    String v = args[1];
    map.put(k, v);
  }
}
