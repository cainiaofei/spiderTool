package cn.nju.txtPreprocess;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class RemoveStopWords {
	private Set<String> stopWords = new HashSet<String>();
	private final String STOPWORDS_PATH = "stopwords_en.txt";
	
	//build StopWords when new a object
	public RemoveStopWords(){
		try {
			buildStopWords();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void buildStopWords() throws IOException{
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(STOPWORDS_PATH));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String word = null;
		while((word=br.readLine())!=null){
			stopWords.add(word.trim());
		}
		br.close();
	}
	
	//filter txt through remove stop words
	public String removeStopWords(String txt){
		StringBuilder sb = new StringBuilder();
		String[] strs = txt.split(" ");
		for(String str:strs){
			if(!stopWords.contains(str)){
				sb.append(str+" ");
			}
		}
		return sb.toString();
	}
	
	//show how to use this class
	public static void main(String[] args){
		RemoveStopWords ssw = new RemoveStopWords();
		String str = "window the is maven decorator m l e use";
		System.out.println(ssw.removeStopWords(str));
	}
}
