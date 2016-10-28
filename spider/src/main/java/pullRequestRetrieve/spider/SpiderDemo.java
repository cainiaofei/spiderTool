package pullRequestRetrieve.spider;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class SpiderDemo {

private static void spider(String url){
		try {
			URL urls=new URL(url); 
			URLConnection urlconnection = urls.openConnection(); //build connection 
			InputStream ins = urlconnection.getInputStream(); // read html
			BufferedReader buff = new BufferedReader(new InputStreamReader(ins, "utf-8")); // 以字符流读入
			StringBuffer sb = new StringBuffer(); // 把内容提取出来
			String lines="";
			while((lines=buff.readLine())!=null){
				sb.append(lines+"\n");
			}
			OutputStreamWriter ops = new OutputStreamWriter(new FileOutputStream("Z:/qiubai.html",true),"utf-8"); // 写入到本地的W盘中，命名为qiubai.html
			ops.write(sb.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	public static void main(String[] args){
		SpiderDemo.spider("https://github.com/");
	}
}
