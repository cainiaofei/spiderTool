package cn.nju.boot;

import java.io.IOException;

import cn.nju.similarity.SimilarityCalcu;

public class Entrance {
	private String reqPath = "Z:\\weld_traceability\\cdi\\requirement";
	private String methodsPath = "Z:\\weld_traceability\\methods";
	private String similarityTargetPath = "z:\\weld_traceability\\similarityRes\\method_req_similarity.txt";
	
	private SimilarityCalcu similarityCalcu;
	
	public Entrance() throws IOException{
		similarityCalcu = new SimilarityCalcu(reqPath,methodsPath);
	}
	
	public void go() throws IOException{
		similarityCalcu.similarityCalcu(similarityTargetPath);
	}
	
	
	public static void main(String[] args) throws IOException{
		Entrance entrance = new Entrance();
		entrance.go();
	}
}
