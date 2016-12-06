package cn.nju.fileIO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import cn.nju.txtPreprocess.CamelCase;

public class ReadFileTXT {
	private static final String path = "C:/Users/zhangsan/Desktop/weld_traceability/methods/";
	public String readFile() throws IOException{
		String file_path = path + "org.jboss.weld.bootstrap.AbstractBeanDeployer.deploySpecialized.txt";
		BufferedReader br = new BufferedReader(new FileReader(file_path));
		StringBuilder sb = new StringBuilder();
		String curLine = null;
		while((curLine=br.readLine())!=null){
			sb.append(curLine+" ");
		}
		br.close();
		String str = CamelCase.split(sb.toString());
		return str;
	}
	
	public static void main(String[] args){
		ReadFileTXT rft = new ReadFileTXT();
		try {
			rft.readFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
