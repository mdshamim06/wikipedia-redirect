/*
 * Copyright 2011 Carnegie Mellon University
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package edu.cmu.lti.wikipedia_redirect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 
 * @author Hideki Shima
 *
 */
public class ExtractRedirectData {

  private static String titlePattern    = "    <title>";
  private static String redirectPattern = "    <redirect";
  private static String textPattern     = "      <text xml";
  private static Pattern pRedirect = Pattern.compile(
          "#[ ]?[^ ]+[ ]?\\[\\[(.+?)\\]\\]", Pattern.CASE_INSENSITIVE);
  
  public void run(String filepath) throws Exception {
    int invalidCount = 0;
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
    boolean inText = false;
    while ((line=br.readLine())!=null) {
      if (line.startsWith(titlePattern)) {
        title = line;
        text = null;
        isRedirect = false;  
      }
      if (line.startsWith(redirectPattern)) {
        isRedirect = true;
      }
      if (isRedirect && (line.startsWith(textPattern) || inText)) {
        Matcher m = pRedirect.matcher(line); // wait heavy regex until here.
//        int start = line.indexOf("[[");
//        int end = line.indexOf("]]");
//        if (start!=-1 && end!=-1) { // make sure current text contains [[...]]
        if (m.find()) { // make sure current text contains [[...]]
          text  = line;
          try {
            title = cleanupTitle(title);
//            String redirectedTitle  = cleanupRedirect(text, start+2, end);
            String redirectedTitle = m.group(1);
            if ( isValidAlias(title, redirectedTitle) ) {
              redirectData.put(title, redirectedTitle);
            } else {
              invalidCount++;
            }
          } catch ( StringIndexOutOfBoundsException e ) {
            System.out.println("ERROR: cannot extract redirection from title = "+title+", text = "+text);
            e.printStackTrace();
          }
        } else { // Very rare case, so not using StringBuilder for concatenation 
          inText = true;
        }
      }
    }
    br.close();
    isr.close();
    fis.close();
    System.out.println("---- Wikipedia redirect extraction done ----");
    long t1 = System.nanoTime();
    IOUtil.save(redirectData);
    System.out.println("Discarded "+invalidCount+" redirects.");
    System.out.println("Acquired "+redirectData.size()+" redirects.");
    System.out.println("Done in "+((double)(t1-t0)/(double)1000000000)+" [sec]");
  }
  
  private String cleanupTitle( String title ) {
    int end = title.indexOf("</title>");
    return end!=-1?title.substring(titlePattern.length(), end):title;
  }

//  private String cleanupRedirect( String text, int start, int end ) {
//    return text.substring(start, end);
//  }
  
  /**
   * Identifies if the redirection is valid.
   * Currently, we only check if the redirection is related to
   * a special Wikipedia page or not.
   * 
   * TODO: write more rules. 
   *  
   * @param title source title
   * @param redirectedTitle target title
   * @return validity
   */
  private boolean isValidAlias( String title, String redirectedTitle ) {
    if ( title.indexOf("Wikipedia:")!=-1 ) {
      return false;
    }
    return true;
  }
  
  public static void main(String[] args) throws Exception {
    new ExtractRedirectData().run(args[0]);
  }
}
