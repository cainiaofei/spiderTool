package cn.nju.weldSrcInfo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * requirements:calculate the code lines of weld code not containing the test code
 * */

public class WeldMainCodeLinesCount {
	private int codeLines;
	private final String MAIN = "main";
	private int javaFilesCount = 0;
	private BufferedWriter bw;
	
	public WeldMainCodeLinesCount(){
		try {
			bw = new BufferedWriter(new FileWriter("d://classFullName.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//find main directory
	public void findTargetDir(File curBaseFile) throws IOException{
		if(curBaseFile.isDirectory()){
			if(curBaseFile.getName().equals(MAIN)){
				countCodeLinesOfThisDir(curBaseFile);
			}
			else if(curBaseFile.getName().equals("tests")){
				;
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
					///write the full name into txt
					if(!fileName.equals("package-info.java")){
						String absolutePath = curBaseFile.getAbsolutePath();
						int fullNameStartIndex = absolutePath.lastIndexOf("org");
						int fullNameEndIndex = absolutePath.lastIndexOf('.');
						bw.write(retrieveFullName(absolutePath,fullNameStartIndex,fullNameEndIndex));
						bw.newLine();
					}
				}
			}
		}
	}

	private String retrieveFullName(String absolutePath, int fullNameStartIndex,int fullNameEndIndex) {
		StringBuilder sb = new StringBuilder();
		char[] chs = absolutePath.toCharArray();
		for(int i = fullNameStartIndex; i < fullNameEndIndex;i++){
			if(chs[i]=='/'||chs[i]=='\\'){
				sb.append(".");
			}
			else{
				sb.append(chs[i]);
			}
		}
		return sb.toString();
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
		weldMainCodeLinesCount.endProcess();
	}

	private void process(String rootPath) throws IOException {
		this.findTargetDir(new File(rootPath));
	}
	
	public void endProcess(){
		try {
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
