package dataSetGenerate.codeDependencyGenerate;

import java.util.HashMap;
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
		Map<String,Set<String>> impWithoutHashCode = new HashMap<String,Set<String>>();
		
		Iterator<String> ite = filedToMethod_Impl.keySet().iterator();
		while(ite.hasNext()){
			String curString = ite.next();
			impWithoutHashCode.put(curString.split(":")[0],filedToMethod_Impl.get(curString));
		}
		
		/////
		ite = filedToMethod_Inject.keySet().iterator();
		while(ite.hasNext()){
			String curString = ite.next();
			if(impWithoutHashCode.containsKey(curString.split(":")[0])){
				String filedName = curString.split(":")[0];
				System.out.println("-----------------"+filedName+"-----------------\n");
				System.out.println("*****************impl*********************");
				print(impWithoutHashCode.get(filedName));
				System.out.println("******************inject******************");
				print(filedToMethod_Inject.get(curString));
				System.out.println("_______________________________________________________________________\n");
			}
		}
		
		//System.out.println("---------end----------");
		
	}

	private static void print(Set<String> set) {
		Iterator<String> ite = set.iterator();
		while(ite.hasNext()){
			System.out.println(ite.next());
		}
	}
}
