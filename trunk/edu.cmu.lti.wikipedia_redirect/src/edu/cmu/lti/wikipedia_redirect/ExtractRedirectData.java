package edu.cmu.lti.wikipedia_redirect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ExtractRedirectData {

  private static String titlePattern    = "    <title>";
  private static String redirectPattern = "    <redirect";
  private static String textPattern     = "      <text xml";
  
  public void run(String filepath) throws Exception {
    long t0 = System.nanoTime();
    File f = new File(filepath);
    if (!f.exists()) {
      System.err.println("ERROR: File not found at "+f.getAbsolutePath());
      return;
    }
    FileInputStream fis = new FileInputStream(f);
    Map<String,String> redirectData = new HashMap<String,String>();
    InputStreamReader isr = new InputStreamReader(fis, "utf-8");
    BufferedReader br = new BufferedReader(isr);
    String title = null;
    String text = null;
    String line = null;
    boolean isRedirect = false;
    while ((line=br.readLine())!=null) {
      if (line.startsWith(titlePattern)) {
        title = line;
        isRedirect = false;  
      }
      if (line.startsWith(redirectPattern)) {
        isRedirect = true;
      }
      if (isRedirect && line.startsWith(textPattern)) {
        text = line;
        title = cleanupTitle(title);
        text = cleanupRedirect(text);
        redirectData.put(title, text);
      }
    }
    br.close();
    isr.close();
    fis.close();
    long t1 = System.nanoTime();
    IOUtil.save(redirectData);
    System.out.println("Found "+redirectData.size()+" redirects.");
    System.out.println("Done in "+((double)(t1-t0)/(double)1000000000)+" [sec]");
  }
  
  private String cleanupTitle( String title ) {
    int end = title.indexOf("</title>");
    return title.substring(titlePattern.length(), end);
  }

  private String cleanupRedirect( String text ) {
    int start = text.indexOf("[[")+2;
    int end = text.indexOf("]]");
    return text.substring(start, end);
  }
  
  public static void main(String[] args) throws Exception {
    new ExtractRedirectData().run(args[0]);
  }
}
