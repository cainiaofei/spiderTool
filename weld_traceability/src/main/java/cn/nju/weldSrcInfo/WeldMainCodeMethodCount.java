package cn.nju.weldSrcInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class WeldMainCodeMethodCount {

	private int methodsAmount = 0;
	
	/**
	 * read classFullName from txt and load and calculate methodAmount
	 * @throws IOException 
	*/
	public void calculateMethodAmount() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader("d://classFullName.txt"));
		String fullClassName = null;
		while((fullClassName=br.readLine())!=null){
			System.out.println(fullClassName);
			try {
				Class<?> curClass = Class.forName(fullClassName);
				methodsAmount += curClass.getDeclaredMethods().length;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		br.close();
	}
	
//	public static void test() throws ClassNotFoundException{
//		String str = "org.jboss.weld.examples.numberguess.Game";
//		Class<?> test = Class.forName(str);
//		System.out.println(test.getDeclaredMethods().length);
//	}
	
	public void showMethodsAmount(){
		System.out.println(methodsAmount);
	}
	
	public static void main(String[] args) throws ClassNotFoundException, IOException{
		System.out.println("ffddssf");
		//WeldMainCodeMethodCount.test();
		WeldMainCodeMethodCount methodAmountCount = new WeldMainCodeMethodCount();
		methodAmountCount.calculateMethodAmount();
		methodAmountCount.showMethodsAmount();
	}
}
