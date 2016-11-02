package com.temp;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Demo {
	
	public void func() throws IOException{
		String url = "https://github.com/weld/core/pull/63";
		Connection conn = Jsoup.connect(url);
		Document doc = conn.get();
		Elements conversation = doc.getElementsByClass("edit-comment-hide");
		for(int i = 0; i < conversation.size();i++){
			System.out.println(conversation.get(i).text());
		}
		Elements comment = doc.getElementsByClass("commit-message");
		for(int i = 0; i < comment.size();i++){
			System.out.println(comment.get(i).text());
		}
	}
	
	public static void main(String[] args) throws IOException{
		Demo demo = new Demo();
		demo.func();
	}
}
