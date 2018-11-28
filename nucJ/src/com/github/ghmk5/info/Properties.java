package com.github.ghmk5.info;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;
import java.util.TreeSet;

/**
 * storeするときにソートして書き込むように拡張したProperties https://code.i-harness.com/ja/q/d417
 */
public class Properties extends java.util.Properties {

  @Override
  public Set<Object> keySet() {
    return Collections.unmodifiableSet(new TreeSet<Object>(super.keySet()));
  }

  @Override
  public synchronized Enumeration<Object> keys() {
    return Collections.enumeration(new TreeSet<Object>(super.keySet()));
  }

}
