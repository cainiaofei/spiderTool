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
		List<String> issueIDList = retriveIssueID(title);
		if(issueIDList.isEmpty()){
			return ;
		}
		else{
			for(String issueID:issueIDList){
				System.out.print(issueID+" ");
			}
			System.out.println();
		}
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
	
	private List<String> retriveIssueID(String title) {
		List<String> issueIDList = new LinkedList<String>();
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
		String url = "https://github.com/weld/core/pull/78/files";
		//issueLinkRetrive.retriveIssueLinkFromAWebHtml(url);
		issueLinkRetrive.retriveIssueLink();;
	}
}
