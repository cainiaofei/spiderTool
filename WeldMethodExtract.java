package cn.nju.WeldMethodsExtract;

import java.io.FileInputStream;
import java.io.IOException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class WeldMethodExtract {
	public static void demo() throws ParseException, IOException{
        FileInputStream in = new FileInputStream("Test.java");
        CompilationUnit cu;
        cu = JavaParser.parse(in);
        in.close();
        MethodVisitor visitor = new MethodVisitor();
        visitor.visit(cu, null);
	}
	
	public static void main(String[] args){
		try {
			WeldMethodExtract.demo();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	 /**
     * Simple visitor implementation for visiting MethodDeclaration nodes. 
     */
    private static class MethodVisitor extends VoidVisitorAdapter {

        @Override
        public void visit(MethodDeclaration n, Object arg) {
            // here you can access the attributes of the method.
            // this method will be called for all methods in this 
            // CompilationUnit, including inner class methods
        	System.out.println(n.getDeclarationAsString());
            System.out.println(n.getBody());
            System.out.println("-------------------------------------");
            //super.visit(n, arg);
        }
    }
}
