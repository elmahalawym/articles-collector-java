import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;

public class ArticleExtractorProject {

	private static String sportsFolderName = "sports";
	private static String politicsFolderName = "politics";
	private static String techFolderName = "tech";
	
	public static void main(String[] args) throws BoilerpipeProcessingException, IOException {
		 
		System.out.println(ArticleExtractor.INSTANCE.getText(
				new URL("http://nasemhoria.blogspot.com/2014/01/blog-post.html")));
		
		String[] sportsLinks = new String[] {
			"http://www.ar.beinsports.net/xml/rss/news",
			"http://zamalekfans.com/ar/feed/index.rss",
			"http://www.ahlynews.com/beta/ahly-news-rss-feed.html",
		//	"http://www.yallakora.com/arabic/rss.aspx?id=0",
			"http://maktoob.sports.yahoo.com/%D8%A3%D8%AE%D8%A8%D8%A7%D8%B1-%D8%A7%D9%84%D8%B1%D9%8A%D8%A7%D8%B6%D8%A9/?format=rss",
			"http://www.youm7.com/newreyadarssnew.asp",
			"http://arabia.eurosport.com/rss-flash.xml",
			"http://reyada.akhbarelyom.com/RSS/GetSectionNewsRSS?JournalID=5&SectionID=145",
			"http://www.egyptianpeople.com/rss/sports_rss.php"
		};
		
		String[] politicsLinks = new String[] {
			//"http://arabic.cnn.com/rss",
			"http://www.masrawy.com/News/rss/LocalPolitics.aspx",
			//"http://www.globalarabnetwork.com/politics/51-syria-politics?format=feed&type=rss",
			"http://www.aljazeera.net/AljazeeraRss/c992b9df-12d8-42ee-a0b0-f7fa7b2d6df8/3ea5221b-aab2-4774-9417-5416dac996db",
			//"http://www.shorouknews.com/egypt/Eg-Politics/rss"
			//"http://www.noonpost.net/rss.xml"
			//"http://www.misrnews.com/rss/politics",
			"http://egyptianpeople.com/rss/politics_rss.php",
			//"http://www.sasapost.com/feed/"
		};
		
		String[] techLinks = new String[] {
			"http://www.alarabiya.net/.mrss/ar/technology.xml",
			"http://www.bbc.co.uk/arabic/topics/technology/index.xml",
			"http://feeds.feedburner.com/itwadi/",

			
			"http://feeds.feedburner.com/tech-wd",
			//"http://arabtech6.spiru.la/?q=rss.xml"
			//"http://www.arabs-geeks.com/feeds/posts/default?alt=rss"
			"http://feeds.feedburner.com/aitnewscom",
			//"http://arabic.cnn.com/scitech/rss"
			//"http://www6.mashy.com/news/technology/titles.rss"
			"http://www.itp.net/arabic/rss/?tid=14",
			"http://www.itp.net/arabic/rss/?tid=13",
			"http://www.itp.net/arabic/rss/?tid=17",
			"http://www.itp.net/arabic/rss/?tid=12",
			"http://www.itp.net/arabic/rss/?tid=11",
			"http://www.itp.net/arabic/rss/?tid=9",
			"http://www.itp.net/arabic/rss/?tid=2",
			"http://www.itp.net/arabic/rss/?tid=330&cid=5",
			"http://www.itp.net/arabic/rss/?tid=18",
			"http://www.itp.net/arabic/rss/?tid=18",
			"http://www.itp.net/arabic/rss/?tid=8",
			"http://www.itp.net/arabic/rss/?tid=21",
			"http://www.itp.net/arabic/rss/?tid=16",
			"http://maktoob.news.yahoo.com/%D8%B9%D9%84%D9%88%D9%85-%D8%AA%D9%83%D9%86%D9%88%D9%84%D9%88%D8%AC%D9%8A%D8%A7/?format=rss",
			
		};
		
		int total = 0;
		int sportsCount = 0;
		int politicsCount = 0;
		int techCount = 0;

		
		ArrayList<String> linksVisited = getOldLinks();
		
		politicsCount = updatePoliticsDocs(politicsLinks, linksVisited);
		
		techCount = updateTechDocs(techLinks, linksVisited);
		
		sportsCount = UpdateSportsDocs(sportsLinks, linksVisited);
		
		
		
		total = sportsCount + politicsCount + techCount;
		
		System.out.println("sports  : " + sportsCount);
		System.out.println("politics: " + politicsCount);
		System.out.println("tech    : " + techCount);
		System.out.println("total   : " + total);

	}

	private static int updateTechDocs(String[] techLinks,
			ArrayList<String> linksVisited) throws IOException,
			BoilerpipeProcessingException, MalformedURLException {
		int techCount;
		// tech
		techCount = 0;
		for(String s : techLinks) {
			
			ArrayList<String> links = ExtractLinks(s);
			
			for(String link : links) {
				if(linksVisited.contains(link)) {
					System.out.println("link already found *********");	
					continue;
				}
				String doc;
				try {
					doc = ExtractArticle(link);
				}
				catch(Exception ex) {
					continue;
				}
				addDocument(techFolderName, doc);
				addLink(link);
				
				String sub;
				if(doc.length() >= 50)
					sub = doc.substring(0, 50);
				else
					sub = doc;
				
				System.out.println("document added to tech : " + sub);
				System.out.println("source: " + s);
				System.out.println("--------------------");
				
				techCount++;
			}
		}
		return techCount;
	}

	private static int updatePoliticsDocs(String[] politicsLinks,
			ArrayList<String> linksVisited) throws IOException,
			BoilerpipeProcessingException, MalformedURLException {
		int politicsCount;
		// politics
		politicsCount = 0;
		for(String s : politicsLinks) {
			
			ArrayList<String> links = ExtractLinks(s);
			
			for(String link : links) {
				if(linksVisited.contains(link)) {
					System.out.println("link already found *********");
					continue;
				}
				String doc;
				try {
					doc = ExtractArticle(link);
				}
				catch(Exception ex) {
					continue;
				}
				
				addDocument(politicsFolderName, doc);
				addLink(link);
				
				String sub;
				if(doc.length() >= 50)
					sub = doc.substring(0, 50);
				else
					sub = doc;
				
				System.out.println("document added to politics : " + sub);
				System.out.println("source: " + s);
				System.out.println("--------------------");
				
				politicsCount++;
				
			}
		}
		return politicsCount;
	}

	private static int UpdateSportsDocs(String[] sportsLinks, ArrayList<String> linksVisited) throws IOException,
			BoilerpipeProcessingException, MalformedURLException {
		// sports
		int sportsCount = 0;
		for(String s : sportsLinks) {
			
			ArrayList<String> links = ExtractLinks(s);
			
			for(String link : links) {
				if(linksVisited.contains(link)){
					System.out.println("link already found *********");
					continue;
				}
				
				String doc;
				try {
					doc = ExtractArticle(link);
				}
				catch(Exception ex) {
					continue;
				}
				
				addDocument(sportsFolderName, doc);
				addLink(link);
				
				String sub;
				if(doc.length() >= 50)
					sub = doc.substring(0, 50);
				else
					sub = doc;
				
				System.out.println("document added to sports : " + sub);
				System.out.println("source: " + s);
				System.out.println("--------------------");
				
				sportsCount++;
			}
		}
		return sportsCount;
	}
	
	static void addDocument(String folderName, String doc) throws NumberFormatException, IOException {
		
		BufferedReader reader = new BufferedReader(new FileReader(folderName + "\\n.txt"));
		
		int nDocs = Integer.parseInt(reader.readLine());
		reader.close();
		
		nDocs++;
		
		PrintWriter pw = new PrintWriter(folderName + "\\" + nDocs + ".txt");
		pw.print(doc);
		pw.close();
		
		PrintWriter pw2 = new PrintWriter(folderName + "\\n.txt");
		pw2.println(nDocs);
		pw2.close();
		
		
	}
	
	static void addLink(String link) throws IOException {
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("VisitedLinks.txt", true)));
		
		pw.println(link);
		pw.close();
	}
	
	static ArrayList<String> getOldLinks() throws IOException {
		
		ArrayList<String> links = new ArrayList<>();
		
		String sCurrentLine;
		BufferedReader br = new BufferedReader(new FileReader("VisitedLinks.txt"));

		while ((sCurrentLine = br.readLine()) != null) {
			links.add(sCurrentLine.trim());
		}
		
		br.close();
		
		return links;
	}
	
	static ArrayList<String> ExtractLinks(String rssUrl) throws IOException {
		
		try {
		Document doc = Jsoup.connect(rssUrl).get();
		ArrayList<String> res = new ArrayList<String>();
		
		for( Element item : doc.select("item") ) {
		    final String link = item.select("link").first().nextSibling().toString();
		    res.add(link);
		}
		
		for (Element item : doc.select("entry")) {
			final String link = item.select("link").first().nextSibling().toString();
		    res.add(link);
		}
		
		return res;
		}
		catch(Exception ex) { return new ArrayList<String>(); }
	}
	
	
	static String ExtractArticle(String Url) throws BoilerpipeProcessingException, MalformedURLException {
		URL url = new URL(Url);
		return ArticleExtractor.INSTANCE.getText(url);
	}
	
	
	static ArrayList<Integer> getIDs(String xmlStr) {
		ArrayList<Integer> res = new ArrayList<>();
		Document doc = Jsoup.parseBodyFragment(xmlStr);
		for(Element element : doc.select("ID")) {
			res.add(Integer.parseInt(element.val()));
		}
		return res;
	}

}
