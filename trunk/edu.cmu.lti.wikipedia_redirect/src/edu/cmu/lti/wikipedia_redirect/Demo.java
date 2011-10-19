package edu.cmu.lti.wikipedia_redirect;
import java.io.File;

public class Demo {
  public static void main(String[] args) throws Exception {
    WikipediaRedirect wr = IOUtil.loadWikipediaRedirect(new File(args[0]));
    String[] srcTerms = {"オサマビンラディン", "オサマ・ビンラーディン",
            "東日本大地震","東日本太平洋沖地震" ,"慶大", "NACSIS", 
            "ダイアモンド", "アボガド", "バイオリン", "平成12年", "3.14"};
    StringBuilder sb = new StringBuilder();
    for ( String src : srcTerms ) {
      sb.append("\""+wr.get(src)+"\" was redirected from \""+src+"\"\n");
    }
    System.out.println(sb.toString());
  }
}
