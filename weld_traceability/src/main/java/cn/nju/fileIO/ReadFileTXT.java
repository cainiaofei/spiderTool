package cn.nju.fileIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import cn.nju.txtPreprocess.CamelCase;

public class ReadFileTXT {
	public String readFile(File file) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(file));
		StringBuilder sb = new StringBuilder();
		String curLine = null;
		while((curLine=br.readLine())!=null){
			sb.append(curLine+" ");
		}
		br.close();
		return sb.toString();
	}
	
	public static void main(String[] args){
		ReadFileTXT rft = new ReadFileTXT();
//		try {
//			rft.readFile("");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
}
