# Wikipedia Redirect  #

Using this software, you can extract pairs of a title and a redirected title (e.g. "USA" -> "United States") in Wikipedia.

## Requirement ##
  * [JDK 6](http://www.oracle.com/technetwork/java/javase/downloads/index.html) or later.

  * Wikipedia Language: any Wikipedia-supported language. We tested with "en" (English; largest among all languages) and "ja" (Japanese) Wikipedia.

## Extracting redirect data from Wikipedia dump ##
1. Check out the code base and build it.
```
$ svn co http://wikipedia-redirect.googlecode.com/svn/trunk/edu.cmu.lti.wikipedia_redirect
$ cd edu.cmu.lti.wikipedia_redirect
$ javac src/edu/cmu/lti/wikipedia_redirect/*.java
```

2. Download the latest version of [wikipedia dump (pages-articles)](http://dumps.wikimedia.org/backup-index.html).
```
# Example in English Wikipedia
$ wget 'http://dumps.wikimedia.org/enwiki/latest/enwiki-latest-pages-articles.xml.bz2'
$ bunzip2 enwiki-latest-pages-articles.xml.bz2
```

```
# Example in Japanese Wikipedia
$ wget 'http://dumps.wikimedia.org/jawiki/latest/jawiki-latest-pages-articles.xml.bz2'
$ bunzip2 jawiki-latest-pages-articles.xml.bz2
```

3. Run [WikipediaRedirectExtractor](http://code.google.com/p/wikipedia-redirect/source/browse/trunk/edu.cmu.lti.wikipedia_redirect/src/edu/cmu/lti/wikipedia_redirect/WikipediaRedirectExtractor.java) to obtain wikipedia redirects.
```
# Example in English Wikipedia
$ java -cp src edu.cmu.lti.wikipedia_redirect.WikipediaRedirectExtractor ./enwiki-latest-pages-articles.xml
---- Wikipedia redirect extraction done ----
Discarded 285287 redirects to wikipedia meta articles.
Extracted 5912206 redirects.
Saved output: /home/hideki/edu.cmu.lti.wikipedia_redirect/target/enwiki-redirect.txt
Done in 549 sec.
```

```
# Example in Japanese Wikipedia
$ java -cp src edu.cmu.lti.wikipedia_redirect.WikipediaRedirectExtractor ./jawiki-latest-pages-articles.xml
---- Wikipedia redirect extraction done ----
Discarded 7971 redirects to wikipedia meta articles.
Extracted 537441 redirects.
Saved output: /home/hideki/edu.cmu.lti.wikipedia_redirect/target/jawiki-redirect.txt
Done in 49 sec.
```

4. Make sure the extracted redirects are stored in a tab-separated .txt file.
```
$ ls target -lh
-rw-r--r-- 1 hideki users 250M 2013-01-25 16:48 enwiki-redirect.txt
-rw-r--r-- 1 hideki users  25M 2013-01-25 16:25 jawiki-redirect.txt
```

## Running a demo ##

Here is how the extracted data can be used with the [Demo.java](https://code.google.com/p/wikipedia-redirect/source/browse/trunk/edu.cmu.lti.wikipedia_redirect/src/edu/cmu/lti/wikipedia_redirect/Demo.java).

```
$ cat src/edu/cmu/lti/wikipedia_redirect/Demo.java
```
```
package edu.cmu.lti.wikipedia_redirect;
import java.io.File;
import java.util.Set;

/**
 * Demo of what you can do with Wikipedia Redirect.
 * @author Hideki Shima
 */
public class Demo {
  private static String[] enSrcTerms = {"Bin Ladin", "William Henry Gates", 
    "JFK", "The Steel City", "The City of Bridges", "Da burgh", "Hoagie", 
    "Centre", "3.14"};
  private static String[] jaSrcTerms = {"オサマビンラディン", "オサマ・ビンラーディン",
          "東日本大地震","東日本太平洋沖地震" ,"NACSIS", 
          "ダイアモンド", "アボガド", "バイオリン", "平成12年", "3.14"};
  private static String enTarget = "Bayesian network";
  private static String jaTarget = "計算機科学";
  
  public static void main(String[] args) throws Exception {
    // Initialization
    System.out.print("Loading Wikipedia Redirect ...");
    long t0 = System.currentTimeMillis();
    File inputFile = new File(args[0]);
    WikipediaRedirect wr = IOUtil.loadWikipediaRedirect(inputFile);
    boolean useJapaneseExample = inputFile.getName().substring(0, 2).equals("ja");
    String[] srcTerms = useJapaneseExample ? jaSrcTerms : enSrcTerms;
    String target = useJapaneseExample ? jaTarget : enTarget;
    long t1 = System.currentTimeMillis();
    System.out.println(" done in "+(t1-t0)/1000D+" sec.\n");
    
    // Let's find a redirection given a source word.
    StringBuilder sb = new StringBuilder();
    for ( String src : srcTerms ) {
      sb.append("redirect(\""+src+"\") = \""+wr.get(src)+"\"\n");
    }
    long t2 = System.currentTimeMillis();
    System.out.println(sb.toString()+"Done in "+(t2-t1)/1000D+" sec.\n--\n");

    // Let's find which source words redirect to the given target word.
    Set<String> keys = wr.getKeysByValue(target);
    long t3 = System.currentTimeMillis();
    System.out.println("All of the following redirect to \""+target+"\":\n"+keys);
    System.out.println("Done in "+(t3-t2)/1000D+" sec.\n");
  } 
}
```

```
# Example in English Wikipedia
$ java -Xmx2g -cp src edu.cmu.lti.wikipedia_redirect.Demo ./target/enwiki-redirect.txt
Loading Wikipedia Redirect ... done in 54.482 sec.

redirect("Bin Ladin") = "Osama bin Laden"
redirect("William Henry Gates") = "Bill_Gates"
redirect("JFK") = "John F. Kennedy"
redirect("The Steel City") = "Pittsburgh"
redirect("The City of Bridges") = "Pittsburgh"
redirect("Da burgh") = "Pittsburgh"
redirect("Hoagie") = "Submarine sandwich"
redirect("Centre") = "Center"
redirect("3.14") = "Pi#Approximate value"
Done in 0.0030 sec.
--

All of the following redirect to "Bayesian network":
[Bayesian Graphical Model, Bayesian Network, Bayes net, Bayesian Networks, Bayesian belief net, Hierarchical Bayesian model, Bayesian belief network, Bayesian neural network, Hierarchical bayes model, Bayes networks, Hierarchical Bayes model, Bayesian networks, Bayesian model, Bayesian graphical model, Hierarchial bayes, Belief network, Hierarchical bayes, Bayes network, Inference network, Bayesian net, Learning bayesian network structure, Belief networks]
Done in 0.61 sec.
```

```
# Example in Japanese Wikipedia
$ java -cp src edu.cmu.lti.wikipedia_redirect.Demo ./target/jawiki-redirect.txt
Loading Wikipedia Redirect ... done in 2.126 sec.

redirect("オサマビンラディン") = "ウサーマ・ビン・ラーディン"
redirect("オサマ・ビンラーディン") = "ウサーマ・ビン・ラーディン"
redirect("東日本大地震") = "東北地方太平洋沖地震"
redirect("東日本太平洋沖地震") = "東北地方太平洋沖地震"
redirect("NACSIS") = "国立情報学研究所"
redirect("ダイアモンド") = "ダイヤモンド"
redirect("アボガド") = "アボカド"
redirect("バイオリン") = "ヴァイオリン"
redirect("平成12年") = "2000年"
redirect("3.14") = "円周率"
Done in 0.0030 sec.
--

All of the following redirect to "計算機科学":
[計算機学, コンピュータ・サイエンス, コンピュータサイエンス, コンピューター科学, コンピュータ科学, コンピュータサイエンス学科, 計算機科学科, コンピューターサイエンス]
Done in 0.057 sec.
```

Note: There is no guarantee that [redirections in Wikipedia](http://en.wikipedia.org/wiki/Wikipedia:Redirect) (a pair of source title and its redirected title) are not alternative forms of the same entity. Doing some reasonable filtering might help in terms of disk usage and speed (the definition of "noise" may depend on your application).

### Link ###
  * [Hyponymy extraction tool](http://alaginrc.nict.go.jp/hyponymy/index.html) developed by NICT can extract hypernyms/hyponyms from Wikipedia (Japanese only).