package cn.nju.WeldMethodsExtract;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class WeldMethodExtract {
	private final String MAIN = "main";
	private final String BASE_PATH = "z:/weld_traceability/methods";
	private int methodCounts = 0;
	private int classCounts = 0;
	
	public void extractMethodOfThisClass(String filePath) throws ParseException, IOException{
        classCounts++;
		FileInputStream in = new FileInputStream(filePath);
        File file = new File(filePath);
        CompilationUnit cu;
        cu = JavaParser.parse(in);
        String fileName = file.getName();
        MethodVisitor visitor = new MethodVisitor(cu.getPackage().getPackageName()+"." + 
        		fileName.substring(0,fileName.lastIndexOf(".")));
        visitor.visit(cu, null);//
        in.close();
	}
	
	private void extractMethod(File curBaseFile) throws IOException {
		if(curBaseFile.isDirectory()){
			File[] subFiles = curBaseFile.listFiles();
			for(File subFile:subFiles){
				extractMethod(subFile);
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
					if(!fileName.equals("package-info.java")){
						try {
							extractMethodOfThisClass(curBaseFile.getAbsolutePath());
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	//find main directory
	public void findTargetDir(File curBaseFile) throws IOException{
		if(curBaseFile.isDirectory()){
			if(curBaseFile.getName().equals(MAIN)){
				extractMethod(curBaseFile);
			}
//			else if(curBaseFile.getName().equals("tests")){
//				;
//			}
			else{
				File[] subFiles = curBaseFile.listFiles();
				for(File subFile:subFiles){
					findTargetDir(subFile);
				}
			}
		}
	}
	
	public void process(String homePath){
		try {
			findTargetDir(new File(homePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("the amount of method is: " + methodCounts);
		System.out.println("the amount of class is: " + classCounts);
	}
	
	public static void main(String[] args){
		WeldMethodExtract methodExtract = new WeldMethodExtract();
		String weldHomePath = "Z:/weld_traceability/weld";
		methodExtract.process(weldHomePath);
	}
	
	 /**
     * Simple visitor implementation for visiting MethodDeclaration nodes. 
     */
    private class MethodVisitor extends VoidVisitorAdapter {
    	private String classFullName;
    	//key:methodFullName value:index avoid method name duplicate
        Map<String,Integer> map = new HashMap<String,Integer>();
        
    	public MethodVisitor(String classFullName){
    		this.classFullName = classFullName;
    	}
    	
        @Override
        public void visit(MethodDeclaration n, Object arg) {
        	methodCounts++;
        	String methodFullName = classFullName + "." + n.getName();
        	System.out.println(classFullName);
//        	if(classFullName.equals("org.jboss.weld.environment.deployment.discovery.FileSystemBeanArchiveHandler")){
//        		System.out.println("");
//        	}
        	if(map.containsKey(methodFullName)){
        		int newValue = map.get(methodFullName) + 1;
        		map.put(methodFullName, newValue);
        		methodFullName = methodFullName + map.get(methodFullName);
        	}
        	else{
        		map.put(methodFullName, 0);
        	}
        	
        	BufferedWriter bw = null;
        	try {
				bw = new BufferedWriter(new FileWriter(BASE_PATH+"/"+methodFullName+".txt"));
			} catch (IOException e) {
				e.printStackTrace();
			}
        	try {
        		Comment comment = n.getComment();
        		if(comment!=null){
        			bw.write(comment.toString());
        		}
        		
				bw.write(n.getDeclarationAsString());
				bw.newLine();
				BlockStmt body = n.getBody();
				if(body!=null){
					bw.write(body.toString());
				}
	        	bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }
}
