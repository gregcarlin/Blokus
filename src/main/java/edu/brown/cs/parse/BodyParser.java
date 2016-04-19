package edu.brown.cs.parse;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
  * Parses http bodies into maps.
  * Works all the time, 95% of the time.
  */
public class BodyParser extends AbstractMap<String, String> {
  private final Map<String, String> map;

  /**
    * Parses a body string into a map.
    * @param rawBody the raw body string
    */
  public BodyParser(String rawBody) {
    map = new HashMap<>();
    String[] items = rawBody.split("&");
    for (String item : items) {
      String[] split = item.split("=");
      assert split.length == 2;
      map.put(split[0], split[1]);
    }
  }

  @Override
  public Set<Map.Entry<String, String>> entrySet() {
    return map.entrySet();
  }

  /**
    * Returns the value of a key parsed as an integer.
    * @param key the key to find
    * @return the int value associated with the key, 0 if not found
    */
  public int getInt(String key) {
    return map.containsKey(key) ? Integer.parseInt(map.get(key)) : 0;
  }
}
