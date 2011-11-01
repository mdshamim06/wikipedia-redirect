package edu.cmu.lti.wikipedia_redirect;
import java.io.File;
import java.util.Set;

/**
 * Showcase for what you can do with Wikipedia Redirect.
 * 
 * @author Hideki Shima
 */
public class Demo {
  
  public static void main(String[] args) throws Exception {
    // Initialization
    System.out.print("Deserializing Wikipedia Redirect ...");
    long t0 = System.nanoTime();
    WikipediaRedirect wr = IOUtil.loadWikipediaRedirect(new File(args[0]));
    long t1 = System.nanoTime();
    System.out.println(" Done in "+(double)(t1-t0)/(double)1000000000+" sec.\n");
    
    // Let's find a redirection given a source word.
    String[] srcTerms = {"オサマビンラディン", "オサマ・ビンラーディン",
            "東日本大地震","東日本太平洋沖地震" ,"慶大", "NACSIS", 
            "ダイアモンド", "アボガド", "バイオリン", "平成12年", "3.14"};
    StringBuilder sb = new StringBuilder();
    for ( String src : srcTerms ) {
      sb.append("\""+wr.get(src)+"\" was redirected from \""+src+"\"\n");
    }
    System.out.println(sb.toString());
    
    // Let's find which source words redirect to the given target word. 
    Set<String> keys = wr.getKeysByValue("東北地方太平洋沖地震");
    System.out.println(keys);
  }
  
}
