package issueLinkRetrive.spider;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class IssueLinkRetrive {
	private String baseUrl = "https://github.com/weld/core/pull/";
	private int pullRequestAmount = 200;
	//deal with every web html
	public void retriveIssueLink(){
		int curPullRequestIndex = 1;
		while(curPullRequestIndex<=pullRequestAmount){
			try {
				System.out.println("------------------------"+curPullRequestIndex+"-------------------------------------");
				retriveIssueLinkFromAWebHtml(baseUrl+curPullRequestIndex+"/files");
			} catch (IOException e) {
				e.printStackTrace();
			}
			curPullRequestIndex++;
		}
	}
	
	//a specific web html
	public void retriveIssueLinkFromAWebHtml(String url) throws IOException{
		Connection conn = Jsoup.connect(url);
		conn.timeout(30000);
		Document doc = conn.get();
		String title = doc.title();
		List<String> issueIDList = new LinkedList<String>();
		retriveIssueID(issueIDList,title);
		if(issueIDList.isEmpty()){
			retriveFromConversation(issueIDList,url.substring(0, url.length()-6));
		}
		if(issueIDList.isEmpty()){
			return ;
		}
		else{
			for(String issueID:issueIDList){
				System.out.print(issueID+" ");
			}
			System.out.println();
		}
		//user-select-contain
		
		Elements filesFullName = doc.getElementsByClass("user-select-contain");
		for(int i = 0; i < filesFullName.size();i++){
			Element curElement = filesFullName.get(i);
			if(curElement.attr("class").equals("user-select-contain")){
				Element fileElement = curElement;
				//filter the file that is not java 
				String fileName = fileElement.attr("title");
				int suffixNamePos = fileName.lastIndexOf(".");
				if(fileName.substring(suffixNamePos+1).equals("java")){
					System.out.println(fileName);
				}
				
			}
		}
		
//		Elements descriptions = filterLevels.get(0).getElementsByClass("description");
//		for(int i = 0; i < descriptions.size();i++){
//			Element curDescription = descriptions.get(i);
//			System.out.println(curDescription.text());
//		}
		
		
	}
	
	private void retriveFromConversation(List<String> issueIDList, String url) throws IOException {
		Connection conn = Jsoup.connect(url);
		Document doc = conn.get();
		
		Elements conversation = doc.getElementsByClass("edit-comment-hide");
		if(conversation!=null){
			for(int i = 0; i < conversation.size();i++){
				retriveIssueID(issueIDList,conversation.get(i).text());
			}
		}
		
		Elements comment = doc.getElementsByClass("commit-message");
		if(comment!=null){
			for(int i = 0; i < comment.size();i++){
				retriveIssueID(issueIDList,comment.get(i).text());
			}
		}
		
		//discussion-item-ref-title
		Elements des = doc.getElementsByClass("discussion-item-ref-title");
		if(des!=null){
			for(int i = 0; i < des.size();i++){
				retriveIssueID(issueIDList,des.get(i).text());
			}
		}
		
	}
	

	private List<String> retriveIssueID(List<String> issueIDList,String title) {
		String[] strs = title.split(",|\\[|]|\\(|\\)| |;|\\.|-|/");//split by several separator
		for(int i = 0; i < strs.length;i++){
			strs[i] = strs[i].toUpperCase();
			if(strs[i].startsWith("WELD")){
				if(strs[i].equals("WELD") && i+1!=strs[i].length()&&isNumber(strs[i+1])){
					issueIDList.add(strs[i]+"-"+strs[i+1]);
				}
				else{
					String remain = strs[i].substring(4);
					if(isNumber(remain)){
						issueIDList.add("WELD-"+remain);
					}
				}
			}
		}
		return issueIDList;
	}

	private boolean isNumber(String curValue) {
		if(curValue==null||curValue.length()==0){
			return false;
		}
		char[] chs = curValue.toCharArray();
		for(int i = 0; i < chs.length;i++){
			if(chs[i]<'0'||chs[i]>'9'){
				return false; 
			}
		}
		return true;
	}

	public static void main(String[] args) throws IOException{
		IssueLinkRetrive issueLinkRetrive = new IssueLinkRetrive();
		String url = "https://github.com/weld/core/pull/182/files";
		//issueLinkRetrive.retriveIssueLinkFromAWebHtml(url);
		issueLinkRetrive.retriveIssueLink();;
	}
}
