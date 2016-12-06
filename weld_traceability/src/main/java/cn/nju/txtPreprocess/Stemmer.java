package cn.nju.txtPreprocess;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

/**
 * 2016/12/6
 * provide processSuffix API to others. 
 * use process() method to manipulate processSuffix method 
 */

public class Stemmer {
	private Set<Character> vowels = new HashSet<Character>();
	private Set<String> dicts = new HashSet<String>();
	public Stemmer(){
		initVowels();
	}
	
	/**
	 * input the origin txt and return processed txt
	 * @param origin txt
	 * @return after stemmer txt
	 */
	public String process(String txt){
		StringBuilder res = new StringBuilder();
		String[] strs = txt.split(" ");
 		for(String str:strs){
 			StringBuilder sb = new StringBuilder(str);
 			processSuffix(sb);
 			res.append(sb.toString()+" ");
 		}
 		return res.toString();
	}
	
	
	public static void main(String[] args) throws IOException{
		Stemmer stem = new Stemmer();
		stem.processWordsFromSpecificFile("the_hobbit.txt");
		stem.exportTXT();
	}
	
	public void exportTXT() throws IOException{
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("result.txt")));
		for(String str:dicts){
			out.print(str+" ");
		}
		out.close(); 
	}
	
	public void initVowels(){
		vowels.add('a');
		vowels.add('e');
		vowels.add('i');
		vowels.add('o');
		vowels.add('u');
	}
	/*
	 *the readWordsFromSpecificFile method of Stemmer
	 *this method is called when we want to read a external file
	 *@param fileName
	 *the name of file , we should make sure the file and .class were placed on the same directory.
	 *@throws FileNotFoundException
	 *if the file can't be found
	 */
	public void processWordsFromSpecificFile(String fileName) throws IOException{
		Set<String> sets = new HashSet<String>();
		FileInputStream in = new FileInputStream(fileName);
		char end = (char)-1;
		while(true){
			StringBuilder sb = new StringBuilder();
			char ch;
			while(Character.isLetter(ch=(char)in.read())){
				sb.append(Character.toLowerCase(ch));
			}
			sets.add(sb.toString());
			processSuffix(sb);
			dicts.add(sb.toString());
			if(ch==end){
				break; 
			}
		}
		System.out.println(dicts.size()+": "+sets.size());
		in.close();
	}

	/*
	 *the processSuffix method of Stemmer
	 *this method is called when we want to process the suffix of word , eg.strip the suffix of word 
	 *@param word
	 *the word(term) we want to process.
	 */
	public void processSuffix(StringBuilder word) {
		if(word.length()==0){
			return;
		}
		else{
			step1(word);
			step2(word);
			step3(word);
			step4(word);
			step5(word);
		}
	}
	
	private void step5(StringBuilder word) {
		int len = word.length();
		if(len==0){
			return ;
		}
		//step 5a
		if(word.charAt(len-1)=='e' && m(word,0,len-1)>1){
			word.deleteCharAt(len-1);
			len--;
		}
		else if(word.charAt(len-1)=='e' && m(word,0,len)==1 && !endWithcvc(word,0,len-1)){
			word.deleteCharAt(len-1);
			len--;
		}
		//step 5b
		if(endWith(word,"ll") && m(word,0,len)>1){
			word.deleteCharAt(len-1);
		}
		else{}
	}

	private void step4(StringBuilder word) {
		int len = word.length();
		if(len==0){
			return ;
		}
		if(endWith(word,"ement")&&m(word,0,len-5)>1){
			word.delete(len-5, len);
		}
		else if(endWith(word,"ance")||endWith(word,"ence")||endWith(word,"able")
				||endWith(word,"ible")||endWith(word,"ment")){
			if(m(word,0,len-4)>1){
				word.delete(len-4, len);
			}
		}
		else if(endWith(word,"ant")||endWith(word,"ent")||endWith(word,"ism")||
				endWith(word,"ate")||endWith(word,"iti")||endWith(word,"ous")||
				endWith(word,"ive")||endWith(word,"ize")){
			if(m(word,0,len-3)>1){
				word.delete(len-3, len);
			}
		}
		else if(endWith(word,"al")||endWith(word,"er")||endWith(word,"ic")||endWith(word,"ou")){
			if(m(word,0,len-2)>1){
				word.delete(len-2, len);
			}
		}
		else if(endWith(word,"ion")&&m(word,0,len-3)>1&&(word.charAt(len-4)=='s'||word.charAt(len-4)=='t')){
			word.delete(len-3, len);
		}
	}

	private void step3(StringBuilder word) {
		int len = word.length();
		if(len==0){
			return ;
		}
		if(endWith(word,"icate")||endWith(word,"alize")||endWith(word,"iciti")){
			if(m(word,0,len-5)>0){
				word.delete(len-3, len);
			}
		}
		else if(endWith(word,"ative")&&m(word,0,len-5)>0){
			word.delete(len-5, len);
		}
		else if(endWith(word,"ical")&&m(word,0,len-4)>0){
			word.delete(len-2, len);
		}
		else if(endWith(word,"ful")&&m(word,0,len-3)>0){
			word.delete(len-3, len);
		}
		else if(endWith(word,"ness")&&m(word,0,len-4)>0){
			word.delete(len-4, len);
		}
		else{}
	}

	/*
	 * step2() based on the paper 
	 */
	private void step2(StringBuilder word) {
		int len = word.length();
		if(len==0){
			return ;
		}
		if(endWith(word,"tional")){
			if(len>=7&&word.charAt(len-7)=='a'&&m(word,0,len-7)>0){//delete [ , )
				word.delete(len-5,len);
				word.append('e');
			}
			else{
				if(m(word,0,len-6)>0){
					word.delete(len-2, len);
				}
			}
		}
		else if((endWith(word,"enci")||endWith(word,"anci")||endWith(word,"abli"))&&
				m(word,0,len-4)>0){
			word.deleteCharAt(len-1);
			word.append('e');
		}
		else if(endWith(word,"izer")&&m(word,0,len-4)>0){
			word.deleteCharAt(len-1);
		}
		else if((endWith(word,"alli")&&m(word,0,len-4)>0)||(endWith(word,"entli")&&m(word,0,len-5)>0)||
				(endWith(word,"eli")&&m(word,0,len-3)>0)||(endWith(word,"ousli")&&m(word,0,len-5)>0)){
			word.delete(len-2,len);
		}
		else if(endWith(word,"ation")){
			if(word.length()>=7&&word.charAt(len-7)=='i'&&word.charAt(len-6)=='z'&&m(word,0,len-7)>0){
				word.delete(len-5, len);
				word.append('e');
			}
			else{
				if(m(word,0,len-5)>0){
					word.delete(len-3,len);
					word.append('e');
				}
			}
		}
		else if(endWith(word,"ator")&&m(word,0,len-4)>0){
			word.delete(len-2, len);
			word.append('e');
		}
		else if(endWith(word,"alism")&&m(word,0,len-5)>0){
			word.delete(len-3, len);
		}
		else if(endWith(word,"iveness")||endWith(word,"fulness")||endWith(word,"ousness")){
			if(m(word,0,len-6)>0){
				word.delete(len-4, len);
			}
		}
		else if(endWith(word,"aliti")&&m(word,0,len-5)>0){
			word.delete(len-3, len);
		}
		else if(endWith(word,"iviti")&&m(word,0,len-5)>0){
			word.delete(len-3, len);
			word.append('e');
		}
		else if(endWith(word,"biliti")&&m(word,0,len-6)>0){
			word.delete(len-5, len);
			word.append("le");
		}
		else{}
	}

	/*
	 * step1 method of Stemmer
	 * the poster stemmer algorithm is divided into five steps. based on the paper.
	 * @param word
	 * the term that we want to process
	 */
	private void step1(StringBuilder word) {
		//step 1a
		int len = word.length();
		if(word.charAt(len-1)=='s'){
			if(endWith(word,"sses")||endWith(word,"ies")){
				word.delete(len-2,len);
				len = len - 2;
			}
			else if((word.length()==1||word.charAt(len-2)!='s')){
				word.deleteCharAt(len-1);
				len = len - 1;
			}
		}
		
		//step 1b
		if(m(word,0,len-3)>0&&endWith(word,"eed")){
			word.deleteCharAt(len-1);
			len = len - 1;
		}
		else if((containVowel(word,0,word.length()-2)&&endWith(word,"ed"))||
				(containVowel(word,0,word.length()-3)&&(endWith(word,"ing")))){
			if(word.charAt(word.length()-1)=='d'){
				word.delete(len-2, len);
				len = len -2;
			}
			else{
				word.delete(len-3, len);
				len = len -3;
			}
			
			if(endWith(word,"at")||endWith(word,"bl")||endWith(word,"iz")){
				word.append('e');
				len = len + 1;
			}
			else if(len>1&&(word.charAt(len-1)==word.charAt(len-2))){
				word.deleteCharAt(word.length()-1);
				len = len - 1;
			}
			else if(m(word,0,len)==1&&endWithcvc(word,0,len)){
				word.append('e');
				len = len + 1;
			}
		}
		
		//step 1c
		if(containVowel(word,0,len-1)&&endWith(word,"y")){
			word.deleteCharAt(len-1);
			word.append('i');
		}
	}
	
	/*
	 * map the *o condition 
	 * @param (,]
	 */
	private boolean endWithcvc(StringBuilder word,int start,int end) {
		if((end-start)<3){
			return false;
		}
		else {
			char[] chs = word.substring(end-3).toCharArray();
			if(!vowels.contains(chs[0])&&(chs[0]!='w'&&chs[0]!='x'&&chs[0]!='y')){
				if(vowels.contains(chs[1])&&!vowels.contains(chs[2])){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 *based on the paper. [C](VC){m}[V]
	 *@param word the specific term
	 *[ , )
	 */
	
	public int m(StringBuilder word,int start,int end){
		int count = 0;
		char preCharacterType = ' ';
		for(int i = start; i < end;i++){
			char currentCh = word.charAt(i);
			if(vowels.contains(currentCh)&&preCharacterType!='V'){
				preCharacterType = 'V';
			}
			else if(!vowels.contains(currentCh)&&preCharacterType=='V'&&
					(currentCh != 'y' || vowels.contains(word.charAt(i-1)))){
				count++;
				preCharacterType = 'C';
			}
		}
		return count;
	}

	/*
	 * the containVowel method of Stemmer
	 * judge whether the interval of the word contains a vowel
	 * @param word the term
	 * @param start end the interval [ , )
	 * @return whether the interval of the word contains a vowel
	 */
	private boolean containVowel(StringBuilder word, int start, int end) {
		for(int i = start;i < end;i++){
			if(vowels.contains(word.charAt(i))){
				return true;
			}
		}
		return false;
	}

	/*
	 * this method is called when we want to judge whether the word end with this tails.
	 * @param word the target terms
	 * @param tails the target tails
	 * @return true or false
	 */
	private boolean endWith(StringBuilder word, String tails) {
		if(word.length()<tails.length()){
			return false;
		}
		else{
			return word.substring(word.length()-tails.length()).equals(tails);
		}
	}
}
