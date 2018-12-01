package com.github.ghmk5.info;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;
import java.util.TreeSet;

/**
 * 私家拡張版Properties
 * 
 * storeするときにソートして書き込むように改変 https://code.i-harness.com/ja/q/d417
 * 
 * booleanとして取り出すメソッドgetPropertiesAsBooleanを追加
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

  /**
   * DialogConverterSettings.setPropsSelected(および元になったAozoraEpub3Applet.setPropsSelected)が
   * Propertiesにbooleanを記録したときにできるエントリをbooleanとして取り出す
   */
  public Boolean getPropertiesAsBoolean(String key) throws IllegalStateException {
    Boolean boolean1 = null;
    String storedValue = this.getProperty(key);
    if (storedValue.equals("1")) { // storedValueがnullならここでぬるぽが出るはずなので、改めて書く必要はない
      boolean1 = true;
    } else if (storedValue.equals("")) {
      boolean1 = false;
    } else {
      throw new IllegalStateException("value stored with key \"" + key + "\" is not a boolean.");
    }
    return boolean1;
  }

}
