package issueRetrieve.spider;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class IssueRetrive {
	
	public void retriveIssueInfo(String issueUrl) throws IOException{
		
		Connection conn = Jsoup.connect(issueUrl);
		Document doc = conn.get();
		System.out.println(doc.title());
		
		///issueDetails
		Element issueDetail = doc.getElementById("issuedetails");
		Elements issueDetails = issueDetail.getElementsByTag("li");
		System.out.println("-----------------------------------------");
		for(int i = 0; i < issueDetails.size();i++){
			Element curElement = issueDetails.get(i);
			String text = curElement.text();
			System.out.println(text);
		}
		
		System.out.println("-------------------------");
		//people details
		Element peopleDetail = doc.getElementById("peopledetails");
		Elements peopleDetails = peopleDetail.getElementsByTag("li").get(0).getElementsByTag("dl");
		for(int i = 0; i < peopleDetails.size();i++){
			Element curElement = peopleDetails.get(i);
			String text = curElement.text();
			System.out.println(text);
		}
		
		System.out.println("------------------------");
		//date details
		Element dateDetail = doc.getElementById("datesmodule");
		Elements dateDetails = dateDetail.getElementsByClass("mod-content").get(0).getElementsByTag("ul").
				get(0).getElementsByTag("li").get(0).getElementsByTag("dl");
		for(int i = 0; i < dateDetails.size();i++){
			Element curElement = dateDetails.get(i);
			String text = curElement.text();
			System.out.println(text);
		}
		
		System.out.println("------------------------");
		//description , maybe there are no description, and we will deal with this case later.
		Element descriptionDetail = doc.getElementById("descriptionmodule");
		if(descriptionDetail==null){
			System.out.println("description: null");
		}
		else{
			Elements descriptionDetails = descriptionDetail.getElementsByClass("mod-content");
			for(int i = 0; i < descriptionDetails.size();i++){
				Element curElement = descriptionDetails.get(i);
				String text = curElement.text();
				System.out.println(text);
			}
		}
		
	}
	
	
	public static void main(String[] args) throws IOException{
		String url = "https://issues.jboss.org/browse/WELD-47";
		IssueRetrive retrive = new IssueRetrive();
		retrive.retriveIssueInfo(url);
		///
		
	}
}
