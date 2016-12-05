package cn.nju.txtPreprocess;

/**
 * 
 */
public class CamelCase {

    public static String split(String input) {
        String words[] = input.split(" ");

        StringBuilder sb = new StringBuilder();

        for (String word : words) {
            for (String cc : splitCamelCase(word)) {
            	if(cc.length()==0){
            		continue;
            	}
            	else{
            		System.out.println(cc);
            		String regex = "(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])";
            		String[] strs =  cc.split(regex);
            		for(String str:strs){
            			//System.out.println(str);
            		}
            	}
                sb.append(cc);
                sb.append(" ");
            }
        }

        return sb.toString();
    }

    private static String[] splitCamelCase(String s) {
        //return s.split("(?<!^)(?=[A-Z])");
    	//return s.split("\\{|\\}|\\<|\\>|\\(|\\)|\\[|\\]\\.|,|\\*|\\+|-|\\=|\\$|;|.[a-z][A-Z][a-z].");
        //return s.split("/{|/}|/<|/>|/(|/)|/[|/]/.|,|/*|/+|/-|/=|/$");
    	String regex = "(\\.|\\{|\\}|\\[|\\]|\\(|\\)|\\<|\\>|\\?|\\;|,|:|/)|((?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z]))";
    	return s.split(regex);
    }
}
