package cn.nju.similarity;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import cn.nju.indexer.WeightCalcu;

public class SimilarityCalcu {
	private WeightCalcu weightReq;
	private WeightCalcu weightCode;
	
	public SimilarityCalcu(String reqPath, String methodsPath) throws IOException{
		weightReq = new WeightCalcu();
		weightCode = new WeightCalcu();
		weightReq.process(reqPath);
		weightCode.process(methodsPath);
	}
	
	public void similarityCalcu(String outputPath) throws IOException{
		double max_similarity = 0;
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath));
		Map<String,Map<String,Double>> reqCorpus = weightReq.getCorpus();
		Iterator<String> reqIte = reqCorpus.keySet().iterator();
		while(reqIte.hasNext()){
			String curReqFileName = reqIte.next();
			Map<String,Double> curReqFileCorpus = reqCorpus.get(curReqFileName);
			////correspond code corpus
			Map<String,Map<String,Double>> codeCorpus = weightCode.getCorpus();
			Iterator<String> codeIte = codeCorpus.keySet().iterator();
			while(codeIte.hasNext()){
				String curCodeFileName = codeIte.next();
				double similarity = similarityBetween(curReqFileCorpus,codeCorpus.get(curCodeFileName));
				if(similarity>1){
					System.out.println("--");
				}
				max_similarity = Math.max(max_similarity, similarity);
				String curRes = curReqFileName+"---------->"+curCodeFileName+" : "+similarity;
				bw.write(curRes);
				bw.newLine();
				//System.out.println(curRes);
			}
		}
		System.out.println("max similarity: "+max_similarity);
		bw.close();
	}
	
	private double similarityBetween(Map<String, Double> reqCorpus, Map<String, Double> codeCorpus) {
		double sqrtSquareSumReq = Math.sqrt(squareSum(reqCorpus));
		double sqrtSquareSumCode = Math.sqrt(squareSum(codeCorpus));
		double similarity;
		
		double productSum = 0;
		Iterator<String> reqIte = reqCorpus.keySet().iterator();
		while(reqIte.hasNext()){
			String curWord = reqIte.next();
			if(codeCorpus.containsKey(curWord)){
				productSum += codeCorpus.get(curWord);
			}
		}
		
		similarity = productSum / (sqrtSquareSumReq*sqrtSquareSumCode);
		
		return similarity;
	}

	///calculate squareSum
	private double squareSum(Map<String, Double> map) {
		double squareSum = 0;
		Iterator<String> ite = map.keySet().iterator();
		while(ite.hasNext()){
			String word = ite.next();
			squareSum += (map.get(word)*map.get(word));
		}
		if(squareSum==0){
			System.out.println("0");
		}
		return squareSum;
	}

	public static void main(String[] args){
		
	}
	
}
