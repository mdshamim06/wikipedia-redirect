package edu.cmu.lti.wikipedia_redirect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class WikipediaRedirect extends LinkedHashMap<String,String>
 implements Serializable {

  private static final long serialVersionUID = 20111008L;

  public WikipediaRedirect() {
    super();
  }

  public WikipediaRedirect( Map<String, String> map ) {
    super();
    ArrayList<Entry<String,String>> list = new ArrayList<Entry<String,String>>( map.entrySet() );
    Collections.sort(list, new EntryComparator());
    for ( Entry<String, String> entry : list ) {
      this.put( entry.getKey(), entry.getValue() );
    }
  }

  public static class EntryComparator 
    implements Comparator<Entry<String,String>> {
    @Override
    public int compare(Entry<String, String> e1, 
            Entry<String, String> e2) {
      return e1.getKey().compareTo(e2.getKey());
    }
  }
  
}
