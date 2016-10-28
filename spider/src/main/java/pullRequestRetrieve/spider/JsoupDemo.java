package pullRequestRetrieve.spider;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupDemo {
	public static void main(String[] args) throws IOException{
		Document doc = Jsoup.connect("https://github.com/weld/core/pull/409").get(); 
		String title = doc.title(); 
		String str1 = "div.comment-body markdown-body markdown-format js-comment-body";
		String str2 = "div.edit-comment-hide";
		Elements elements = doc.select(str2);
		Element eles = elements.get(0);
		Elements eee = eles.getElementsByClass("comment-body markdown-body markdown-format js-comment-body");
		Elements tit = eee.get(0).getElementsByTag("p");
		title = tit.get(0).ownText();
		System.out.println(title);
	}
}
