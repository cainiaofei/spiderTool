package pullRequestRetrieve.spider;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupDemo {
	private final String div = "div";
	private String baseUrl = "https://github.com/weld/core/pull/";
	private String outerClassName = "edit-comment-hide";
	private String innerClassName = "comment-body markdown-body markdown-format js-comment-body";
	
	public void dataCapture() throws IOException{
		int pullRequestIndex = 1;
		int pullRequestAmount = 1490;//////
		while(pullRequestIndex<pullRequestAmount){
			Connection conn = Jsoup.connect(baseUrl+pullRequestIndex);
			Document doc = conn.get();//get the document represent html
			Elements elements = doc.select(div+"."+outerClassName);
			for(int i = 0; i < elements.size();i++){
				Element curElement = elements.get(i);
				Elements targetElement = curElement.getElementsByClass(innerClassName);
				if(targetElement.size()!=1){
					System.out.println("-----there should exist more than one target element-----");
				}
				else{
					Element target = targetElement.get(0);
					Elements textElements = target.getElementsByTag("p");
					if(textElements.size()!=1){
						System.out.println(baseUrl+pullRequestIndex+"--more than one p--");
					}
					else{
						Element textElement = textElements.get(0);
						String text = textElement.ownText();
						System.out.println(text);
					}
				}
			}
			pullRequestIndex++;
		}
	}
	
	public static void main(String[] args) throws IOException{
		JsoupDemo jd = new JsoupDemo();
		jd.dataCapture();
	}
}
