package issueRetrieve.spider;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class IssueRetrive {
	private List<String> textList = new LinkedList<String>();
	private String baseUrl = "https://issues.jboss.org/browse/WELD-";
	private int pageAmounts = 2252;
	private Set<String> set = new HashSet<String>();//in case  duplicate
	
	public void issueInfoProcess(){
		int pageIndex = 1;
		
		while(pageIndex<=pageAmounts){
			try {
				String curUrl = baseUrl+pageIndex;
				retriveIssueInfo(curUrl);
				System.out.println("-------------------"+curUrl+"---------------------\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			pageIndex++;
		}
	}
	
	public void retriveIssueInfo(String issueUrl) throws IOException{
		
		Connection conn = Jsoup.connect(issueUrl);
		conn.timeout(300000);
		StringBuilder sb = new StringBuilder();
		//title
		Document doc = conn.get();
		String title = doc.title();
		String anchor = findAnchor(title);
		if(anchor==null||!anchor.trim().startsWith("WELD-") || set.contains(anchor)){
			return ;
		}
		else{
			set.add(anchor);
		}
		String summary = findSummary(title);
		sb.append(anchor);
		sb.append(";"+filter(summary));
		///issueDetails
		Element issueDetail = doc.getElementById("issuedetails");
		if(issueDetail==null){
			return ;
		}
		Elements issueDetails = issueDetail.getElementsByTag("li");
		for(int i = 0; i < issueDetails.size()&&i<8;i++){
			Element curElement = issueDetails.get(i);
			String text = curElement.text();
			//System.out.println(text);
			text = filter(text);
			text = text.substring(text.indexOf(":")+1, text.length());
			sb.append(";"+text);
		}
		
		//people details
		Element peopleDetail = doc.getElementById("peopledetails");
		Elements peopleDetails = peopleDetail.getElementsByTag("li").get(0).getElementsByTag("dl");
		for(int i = 0; i < peopleDetails.size();i++){
			Element curElement = peopleDetails.get(i);
			String text = curElement.text();
			//System.out.println(text);
			text = filter(text);
			text = text.substring(text.indexOf(":")+1, text.length());
			sb.append(";"+text);
		}
		
		//date details
		Element dateDetail = doc.getElementById("datesmodule");
		Elements dateDetails = dateDetail.getElementsByClass("mod-content").get(0).getElementsByTag("ul").
				get(0).getElementsByTag("li").get(0).getElementsByTag("dl");
		for(int i = 0; i < dateDetails.size();i++){
			Element curElement = dateDetails.get(i);
			String text = curElement.text();
			text = filter(text);
			text = text.substring(text.indexOf(":")+1, text.length());
			sb.append(";"+text);
		}
		
		//description , maybe there are no description, and we will deal with this case later.
		Element descriptionDetail = doc.getElementById("descriptionmodule");
		if(descriptionDetail==null){
			sb.append(";null");
		}
		else{
			Elements descriptionDetails = descriptionDetail.getElementsByClass("mod-content");
			for(int i = 0; i < descriptionDetails.size();i++){
				Element curElement = descriptionDetails.get(i);
				String text = curElement.text();
				sb.append(";"+filter(text));
			}
		}
		textList.add(sb.toString());
	}
	
	//summary retrieve
	private String findSummary(String title) {
		int start = title.indexOf(']');
		if(start==-1){
			return null;
		}
		int end = title.lastIndexOf('-');
		return title.substring(start+1,end);
	}

	//[WELD-50] If a Web Bean component is declared in web-beans.xml ------> WELD-50
	private String findAnchor(String title) {
		int start = title.indexOf('[');
		if(start==-1){
			return null;
		}
		int end = title.indexOf(']');
		return title.substring(start+1, end);
	}

	/**
	 * 写文件的操作 对textList进行持久化操作 
	 * @throws IOException 
	 **/
	public void writeFile() throws IOException{
		String filePath = "Z:/issue.csv";
		FileWriter fw = new FileWriter(filePath);   
	    BufferedWriter bw = new BufferedWriter(fw); 
	    //写头
	    String head = "anchor;summary;type;status;priority;resolution;affects version/s;fix version/s;component/s;labels;assignee;"
	    		+ "reporter;created;updated;resolved;description";
	    bw.write(head);
	    bw.newLine();
	    for(String text:textList){
	    	bw.write(text);
	    	bw.newLine();
	    } 
	    bw.close();
	}
	
	
	private String filter(String text) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < text.length();i++){
			char curCh = text.charAt(i);
			if(curCh!='\r' && curCh!='\t' && curCh!=',' && curCh!='\n'){
				sb.append(curCh);
			}
			else{
				sb.append(" ");
			}
		}
		return sb.toString();
	}

	public void demo() throws IOException{
//		String filePath = "Z:/issue.csv";
//		FileWriter fw = new FileWriter(filePath);   
//	    BufferedWriter bw = new BufferedWriter(fw); 
//	    //写头
//	    String head = "d";
//	    bw.close();
		String url = "https://issues.jboss.org/browse/WELD-70";
		IssueRetrive retrive = new IssueRetrive();
		retrive.retriveIssueInfo(url);
	}
	public static void main(String[] args) throws IOException{
		IssueRetrive retrive = new IssueRetrive();
		retrive.issueInfoProcess();
		retrive.writeFile();
		
	}
}
