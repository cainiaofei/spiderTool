package cn.nju.txtPreprocess;


/*
 * date:2016/12/5 
 */
public class CamelCase {
	
    /**
     * receive a String txt and split camel
     * @param fileTXT code txt
     * @return after camel split 
	*/
    public static String split(String fileTXT) {
    	StringBuilder sb = new StringBuilder();
    	for(String word:splitCamelCase(fileTXT)){
    		if(word.length()==0){
    			continue;
    		}
    		sb.append(word.toLowerCase()+" ");
    	}
        return sb.toString();
    }

    private static String[] splitCamelCase(String s) {
    	String regex = "(\\.|\\{|\\}|\\[|\\]|\\(|\\)|\\<|\\>|\\?|\\;|,|:|/|\\s+)|((?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z]))";
    	return s.split(regex);
    }
}
