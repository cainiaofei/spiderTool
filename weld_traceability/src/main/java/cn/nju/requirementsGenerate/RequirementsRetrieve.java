package cn.nju.requirementsGenerate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

class DirInfo{
	String path;
	int level;
	public DirInfo(String path,int level){
		this.path = path;
		this.level = level;
	}
}

public class RequirementsRetrieve {
	private int levelThreeCount = 0;
	private int levelTwoCount = 0;
	private int levelOneCount = 0;
	public void readPDF() throws IOException{
		PDDocument document = null;
	    document = PDDocument.load(new File("d:\\cdi.pdf"));
	    System.out.println(document.getPages());
	    if (!document.isEncrypted()) {
	        PDFTextStripperByArea stripper = new PDFTextStripperByArea();
	        stripper.setSortByPosition(true);
	        PDFTextStripper Tstripper = new PDFTextStripper();
	        String st = Tstripper.getText(document);
	        String[] strs = st.split("\\n");
	        writeTXT(strs);
	    }
	    document.close();
	}
	
	public void writeTXT(String[] strs) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter("d://cdi.txt"));
		String url = "http://docs.jboss.org/cdi/spec/1.2/cdi­spec.html";
		for(String str:strs){
        	if(str.startsWith("2016/11/24")||str.startsWith(url)||str.startsWith("JAVA")
        			||str.startsWith("XML")){
        		continue;
        	}
        	else{
        		bw.write(str);
        	}
        }
		bw.close();
	}
	
	public void divideIntoRequirement() throws IOException{
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("D:\\cdi.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String curLineContent = null;
		String basePath = "d://cdi-requirement";
		Stack<DirInfo> stack = new Stack<DirInfo>();
		stack.add(new DirInfo(basePath,0));
		
		BufferedWriter curBW = null;
		while((curLineContent=br.readLine())!=null){
			if(curLineContent.length()==0){
				continue;
			}
			curLineContent.trim();
			char firstCH = curLineContent.charAt(0);
			if(firstCH>'0'&&firstCH<='9'){
				if(curBW!=null){
					curBW.close();
				}
				
				String name = curLineContent.split(" ")[0];
				int levels = name.split("\\.").length;
				if(levels==3){
					levelThreeCount++;
					if(curLineContent.endsWith("?")){
						curLineContent = curLineContent.substring(0,curLineContent.length()-1);
					}
					curBW = new BufferedWriter(new FileWriter(stack.peek().path+"/"+curLineContent+".txt"));
				}
				else{
					if(levels==1){
						levelOneCount++;
					}
					else{
						levelTwoCount++;
					}
					
					while(levels<stack.peek().level){
						stack.pop();
					}
					if(levels==stack.peek().level){
						stack.pop();
						String newPath = stack.peek().path+"/"+curLineContent;
						File newDir = new File(newPath);
						if(!newDir.mkdir()){
							System.out.println("make directory fail!!!");
							System.exit(0);
						}
						else{
							stack.push(new DirInfo(newPath,levels));
							curBW = new BufferedWriter(new FileWriter(stack.peek().path+"/"+"text.txt"));
						}
					}
					else{
						String newPath = stack.peek().path+"/"+curLineContent;
						File newDir = new File(newPath);
						if(!newDir.mkdir()){
							System.out.println("make directory fail!!!");
							System.exit(0);
						}
						else{
							stack.push(new DirInfo(newPath,levels));
							curBW = new BufferedWriter(new FileWriter(stack.peek().path+"/"+"text.txt"));
						}
					}
				}
			}
			else{
				curBW.write(curLineContent);
				curBW.newLine();
			}
		}//while
		curBW.close();
		br.close();
	}
	
	public void show(){
		System.out.println("levelOneCount: "+levelOneCount);
		System.out.println("levelTwoCount: "+levelTwoCount);
		System.out.println("levelThreeCount: "+levelThreeCount);
	}
	
	public static void main(String[] args){
		
		RequirementsRetrieve rr = new RequirementsRetrieve();
		try {
			//rr.readPDF();
			rr.divideIntoRequirement();
		} catch (IOException e) {
			e.printStackTrace();
		}
		rr.show();
	}
}
