package cn.nju.txtPreprocess;

public class Demo {
	public static void main(String[] args){
		Stemmer stem = new Stemmer();
		StringBuilder str = new StringBuilder("baffle");
		System.out.println(stem.m(str,0,str.length()));
	}
}
