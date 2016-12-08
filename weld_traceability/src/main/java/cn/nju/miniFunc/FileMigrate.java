package cn.nju.miniFunc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileMigrate {
	private String targetReqPath = "z:/weld_traceability/cdi/requirement";
	private int count = 0;
	
	public void migrateFile(String curPath){
		File curDir = new File(curPath);
		if(curDir.isDirectory()){
			File[] subs = curDir.listFiles();
			for(File file:subs){
				migrateFile(file.getAbsolutePath());
			}
		}
		else{
			try {
				copyFile(curDir);
				count++;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void copyFile(File curFile) throws IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(curFile));
		BufferedWriter bw = new BufferedWriter(new FileWriter(targetReqPath+"\\"+
				curFile.getName()));
		
		String curLineContent = null;
		while((curLineContent=br.readLine())!=null){
			bw.write(curLineContent);
			bw.newLine();
		}
		bw.close();
		br.close();
	}

	public int getCount(){
		return count;
	}
	
	public static void main(String[] args){
		FileMigrate fileMigrate = new FileMigrate();
		String originalHomePath = "Z:\\weld_traceability\\cdi\\origin_requirements\\requirement";
		fileMigrate.migrateFile(originalHomePath);
		System.out.println(fileMigrate.getCount());
	}
}
