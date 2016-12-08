package cn.nju.indexer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cn.nju.fileIO.ReadFileTXT;
import cn.nju.txtPreprocess.CamelCase;
import cn.nju.txtPreprocess.RemoveStopWords;
import cn.nju.txtPreprocess.Stemmer;

public class WeightCalcu {
	private Map<String,Map<String,Double>> corpus = new HashMap<String,Map<String,Double>>();
	private Map<String,Set<String>> wordMapFile = new HashMap<String,Set<String>>();
	private ReadFileTXT readFile;
	private CamelCase camelCase;
	private RemoveStopWords removeStopWords;
	private Stemmer stem;
	
	public WeightCalcu(){
		readFile = new ReadFileTXT();
		camelCase = new CamelCase();
		removeStopWords = new RemoveStopWords();
		stem = new Stemmer();
	}
	
	public void process(String basePath) throws IOException{
		File dir = new File(basePath);
		File[] files = dir.listFiles();
		for(File file:files){
			buildTF(file);
		}
		calcuTFIDF();
	}

	private void buildTF(File file) throws IOException {
		String fullName = file.getName();
		//System.out.println(fullName);
		String fileName = fullName.substring(0,fullName.lastIndexOf('.'));
		Map<String,Double> tf = new HashMap<String,Double>();
		String codeMapTXT = preprocess(file);
		
		for(String word:codeMapTXT.split(" ")){//for
			if(tf.containsKey(word)){
				double curValue = tf.get(word);
				tf.put(word, curValue+1);
			}
			else{
				tf.put(word, 1.0);
			}
			
			if(wordMapFile.containsKey(word)){
				wordMapFile.get(word).add(fileName);
			}
			else{
				Set<String> mapFile = new HashSet<String>();
				mapFile.add(fileName);
				wordMapFile.put(word, mapFile);
			}
		}//for
		corpus.put(fileName, tf);
	}

	public void calcuTFIDF(){
		//corpus
		//traverse corpus
		Iterator<String> ite = corpus.keySet().iterator();
		while(ite.hasNext()){
			String fileName = ite.next();
			Map<String,Double> tfidf = corpus.get(fileName);
			Iterator<String> wordIte = tfidf.keySet().iterator();
			while(wordIte.hasNext()){
				String word = wordIte.next();
				double tf = tfidf.get(word);
				int n = corpus.size();
				double idf = Math.log(n*1.0/wordMapFile.get(word).size())/Math.log(2);
				tfidf.put(word, tf*idf);
			}
		}
	}
	
	///
	private String preprocess(File file) throws IOException {
		String fileTXT = readFile.readFile(file);
		fileTXT = camelCase.split(fileTXT);
		fileTXT = removeStopWords.removeStopWords(fileTXT);
		fileTXT = stem.process(fileTXT);
		fileTXT = removeStopWords.removeStopWords(fileTXT);
		return fileTXT;
	}
	
	public Map<String,Map<String,Double>> getCorpus(){
		return corpus;
	}
	
	public static void main(String[] args){
		
	}
}
