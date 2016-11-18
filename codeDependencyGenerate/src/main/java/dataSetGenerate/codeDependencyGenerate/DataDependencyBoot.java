package dataSetGenerate.codeDependencyGenerate;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DataDependencyBoot {
	public static void main(String[] args){
		String dbName = "jdbc:sqlite:D:\\sqliteOutput\\impl\\test3.db";
		DataDependencyCapturer dataDependencyCapturer = new DataDependencyCapturer(dbName);
		dataDependencyCapturer.dataDependencyCapturer();
		Map<String,Set<String>> filedToMethod_Impl = dataDependencyCapturer.getFiledToMethod();
		/*---------------------------------------------------------------------------*/
		
		String dbName2 = "jdbc:sqlite:D:\\sqliteOutput\\Weld Implementation (Core)\\test3.db";
		DataDependencyCapturer dataDependencyCapturer2 = new DataDependencyCapturer(dbName2);
		dataDependencyCapturer2.dataDependencyCapturer();
		Map<String,Set<String>> filedToMethod_Inject = dataDependencyCapturer2.getFiledToMethod();
	    /*----------------------------------------------------------------------------*/
		Iterator<String> ite = filedToMethod_Impl.keySet().iterator();
		while(ite.hasNext()){
			String curString = ite.next();
			if(filedToMethod_Inject.containsKey(curString)){
				System.out.println(curString);
			}
		}
		
	}
}
