package issueLinkRetrive.spider;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class IssueLinkRetrive {
	
	//a specific web html
	public void retriveIssueLinkFromAWebHtml(String url) throws IOException{
		Connection conn = Jsoup.connect(url);
		conn.timeout(30000);
		Document doc = conn.get();
		String title = doc.title();
		System.out.println(title);
		Elements filterLevels = doc.getElementsByClass("diffbar-item toc-select select-menu js-menu-container js-select-menu");
		if(filterLevels==null){
			System.out.println("error the first level ");
		}
		
		Elements descriptions = filterLevels.get(0).getElementsByClass("description");
		for(int i = 0; i < descriptions.size();i++){
			Element curDescription = descriptions.get(i);
			System.out.println(curDescription.text());
		}
		
		
	}
	
	public static void main(String[] args){
		IssueLinkRetrive issueLinkRetrive = new IssueLinkRetrive();
		String url = "https://github.com/weld/core/pull/1458/files";
		try {
			issueLinkRetrive.retriveIssueLinkFromAWebHtml(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
