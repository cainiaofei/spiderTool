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
		///
		separate();
		Element ele = doc.getElementById("issuedetails");
		Elements issueDetails = ele.getElementsByTag("li");
		
		//type
		Element one = issueDetails.get(0);
		String oneTemp = one.getElementsByTag("div").get(0).getElementById("type-val").text();
		System.out.println("Type: "+oneTemp);
		
		//status
		Element two = issueDetails.get(1);
		String twoTemp = two.getElementsByTag("div").get(0).getElementById("status-val").text();
		System.out.println("status: "+twoTemp);
		
		//priority
		Element three = issueDetails.get(2);
		String threeTemp = three.getElementsByTag("div").get(0).getElementById("priority-val").text();
		System.out.println("priority: "+threeTemp);
		
		//Resolution
		Element four = issueDetails.get(3);
		String fourTemp = four.getElementsByTag("div").get(0).getElementById("resolution-val").text();
		System.out.println("resolution: "+fourTemp);
		
		//Affects Version/s
		Element five = issueDetails.get(4);
		String fiveTemp = five.getElementsByTag("div").get(0).getElementById("versions-val").text();
		System.out.println("Affects Version/s: "+fiveTemp);
		
		//Fix Version/s
		Element six = issueDetails.get(5);
		String sixTemp = six.getElementsByTag("div").get(0).getElementById("fixfor-val").text();
		System.out.println("Fix Version/s: "+sixTemp);
		
		//Component/s
		Element seven = issueDetails.get(6);
		String sevenTemp = seven.getElementsByTag("div").get(0).getElementById("components-val").text();
		System.out.println("Component/s: "+sevenTemp);
		
		//Labels 这个比较特殊  不能直接用id
		Element eight = issueDetails.get(7);
		String eightTemp = eight.getElementsByTag("div").get(0).text();
		System.out.println("Component/s: "+eightTemp);
		
		System.out.println("-----------------------------------------");
		for(int i = 0; i < issueDetails.size();i++){
			Element curElement = issueDetails.get(i);
			String str = curElement.text();
			System.out.println(str);
		}
	}
	
	public void separate(){
		System.out.println("-------------------------------------------");
	}
	
	public static void main(String[] args) throws IOException{
		String url = "https://issues.jboss.org/browse/WELD-46";
		IssueRetrive retrive = new IssueRetrive();
		retrive.retriveIssueInfo(url);
		///
		
	}
}
