package cn.nju.requirementsGenerate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class RequirementsExtract {
	private final String BASE_PATH = "d://cdi_requirement";
	
	public void extractRequirements(String path) throws IOException{
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		BufferedWriter bw = null;
		String curLine = null;
		while((curLine=br.readLine())!=null){
			if(curLine.length()>0){
				curLine.trim();
				char head = curLine.charAt(0);
				if(head>'0'&&head<='9'){
					if(bw!=null){
						bw.close();
					}
					bw = new BufferedWriter(new FileWriter(BASE_PATH+"/"+curLine.split(" ")[0]+".txt"));
				}
				bw.write(curLine);
				bw.newLine();
			}
			else{
				continue;
			}
		}
		br.close();
	}
	
	public static void main(String[] args){
		RequirementsExtract requirementsExtract = new RequirementsExtract();
		String weldHomePath = "D:/cdi.txt";
		try {
			requirementsExtract.extractRequirements(weldHomePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
