package cn.nju.weldSrcInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * requirements:calculate the code lines of weld code not containing the test code
 * 
 * */

public class WeldMainCodeLinesCount {
	private int codeLines;
	private final String MAIN = "main";
	private int javaFilesCount = 0;
	
	//find main directory
	public void findTargetDir(File curBaseFile) throws IOException{
		if(curBaseFile.isDirectory()){
			if(curBaseFile.getName().equals(MAIN)){
				countCodeLinesOfThisDir(curBaseFile);
			}
			else{
				File[] subFiles = curBaseFile.listFiles();
				for(File subFile:subFiles){
					findTargetDir(subFile);
				}
			}
		}
	}
	
	private void countCodeLinesOfThisDir(File curBaseFile) throws IOException {
		if(curBaseFile.isDirectory()){
			File[] subFiles = curBaseFile.listFiles();
			for(File subFile:subFiles){
				countCodeLinesOfThisDir(subFile);
			}
		}
		else{
			String fileName = curBaseFile.getName();
			int pos = fileName.indexOf('.');
			if(pos==-1){
				return ;
			}
			else{
				String extensionName = fileName.substring(pos+1);
				if(extensionName.equals("java")){
					javaFilesCount++;
					codeLines += calcuCodeLinesOfThisFile(curBaseFile);
				}
			}
		}
	}

	private int calcuCodeLinesOfThisFile(File curFile) throws IOException {
		int codeLineCounts = 0;
		BufferedReader br = new BufferedReader(new FileReader(curFile));
		String lineContent = null;
		while((lineContent=br.readLine())!=null){
			if(lineContent.length()>0){
				codeLineCounts++;
			}
		}
		br.close();
		return codeLineCounts;
	}

	public void showWeldInfo(){
		System.out.println("the code lines of weld are " + codeLines);
		System.out.println("the amount of java file are " + javaFilesCount);
	}
	
	public static void main(String[] args){
		String rootPath = "F:\\weld";
		WeldMainCodeLinesCount weldMainCodeLinesCount = new WeldMainCodeLinesCount();
		try {
			weldMainCodeLinesCount.process(rootPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		weldMainCodeLinesCount.showWeldInfo();
	}

	private void process(String rootPath) throws IOException {
		this.findTargetDir(new File(rootPath));
	}
	
	class HD{
		public void hhd(){
			
		}
		public void hdjsd(){
			
		}
	}
}
