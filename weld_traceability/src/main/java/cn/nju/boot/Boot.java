package cn.nju.boot;

import java.io.File;
import java.io.IOException;

import cn.nju.fileIO.ReadFileTXT;
import cn.nju.txtPreprocess.RemoveStopWords;
import cn.nju.txtPreprocess.Stemmer;

public class Boot {
	
	public static void main(String[] args) throws IOException{
		ReadFileTXT rft = new ReadFileTXT();
		String str = rft.readFile(new File(""));
		System.out.println(str);
		Stemmer stem = new Stemmer();
		str = stem.process(str);
		System.out.println(str);
		RemoveStopWords removeStopWords = new RemoveStopWords();
		System.out.println(removeStopWords.removeStopWords(str));
	}
}
