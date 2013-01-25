package edu.cmu.lti.wikipedia_redirect;
import java.io.File;
import java.util.Set;

/**
 * Demo of what you can do with Wikipedia Redirect.
 * @author Hideki Shima
 */
public class Demo {
  private static String[] jaSrcTerms = {"オサマビンラディン", "オサマ・ビンラーディン",
          "東日本大地震","東日本太平洋沖地震" ,"慶大", "NACSIS", 
          "ダイアモンド", "アボガド", "バイオリン", "平成12年", "3.14"};
  private static String[] enSrcTerms = {"Bin Ladin", "William Henry Gates", 
    "JFK", "The Steel City", "The City of Bridges", "Da burgh", "Hoagie", 
    "Centre", "3.14"};
  private static String jaTarget = "東北地方太平洋沖地震";
  private static String enTarget = "Hurricane Sandy";
  
  public static void main(String[] args) throws Exception {
    // Initialization
    System.out.print("Loading Wikipedia Redirect ...");
    long t0 = System.currentTimeMillis();
    File inputFile = new File(args[0]);
    WikipediaRedirect wr = IOUtil.loadWikipediaRedirect(inputFile);
    boolean isJapaneseExample = inputFile.getName().substring(0, 2).equals("ja");
    String[] srcTerms = isJapaneseExample ? jaSrcTerms : enSrcTerms;
    String target = isJapaneseExample ? jaTarget : enTarget;
    long t1 = System.currentTimeMillis();
    System.out.println(" done in "+(t1-t0)/1000+" sec.\n");
    
    // Let's find a redirection given a source word.
    StringBuilder sb = new StringBuilder();
    for ( String src : srcTerms ) {
      sb.append("\""+wr.get(src)+"\" was redirected from \""+src+"\"\n");
    }
    System.out.println(sb.toString()+"--\n");

    // Let's find which source words redirect to the given target word.
    Set<String> keys = wr.getKeysByValue(target);
    System.out.println("All of the following redirect to \""+target+"\":\n"+keys);
    long t2 = System.currentTimeMillis();
    System.out.println("Redirection lookup done in "+(t2-t1)/1000+" sec.\n");
  }
  
}
