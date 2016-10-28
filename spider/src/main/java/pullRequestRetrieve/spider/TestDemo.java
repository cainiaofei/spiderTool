package pullRequestRetrieve.spider;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class TestDemo {
	
	public static void spider(String addr){
		try {
			URL url = new URL(addr);
			URLConnection conn = url.openConnection();//build connection
			InputStream ins = conn.getInputStream();//input source
			BufferedReader br = new BufferedReader(new InputStreamReader(ins));//read from ins
			StringBuffer sb = new StringBuffer();
			String lines;
			while((lines=br.readLine()) != null){
				sb.append(lines+"\n");
			}
			System.out.println(1+sb.toString());
			
			br.close();
			ins.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		TestDemo.spider("https://github.com");//
	}
}
