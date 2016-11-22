package dataSetGenerate.codeDependencyGenerate;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import basic.dataStruct.FiledVisitRecord;
import basic.dataStruct.InfoBetweenTwoMethod;
import basic.dbProcess.DBOperate;

public class DataDependencyCapturer {
	//former:filed latter:methodFullName
	private Map<String,Set<String>> filedToMethod = new HashMap<String,Set<String>>();
	private List<FiledVisitRecord> methodRecordList = new LinkedList<FiledVisitRecord>();
	private int[][] dataDependencyMatrix;
	private Map<String,Integer> methodNameToIndex = new HashMap<String,Integer>();
	private Map<Integer,String> indexToMethodName = new HashMap<Integer,String>();
	private DBOperate db = null;
	
	public Map<String,Set<String>> getFiledToMethod(){
		return filedToMethod;
	}
	
	public DataDependencyCapturer(String dbPath){
		db = new DBOperate(dbPath);
	}
	
	public void dataDependencyCapturer(){
		String sql = "select fSignature, fHashcode, McSignature, methodName from parameterPass";
		try {
			db.dataDependencyExecuteSelect(methodRecordList, sql);
		} catch (SQLException e) {
			System.out.println("error in method dataDependencyCapturer");
			e.printStackTrace();
		}
		
		process(methodNameToIndex,indexToMethodName);
		
		for(int i = 0; i < methodRecordList.size();i++){
			FiledVisitRecord curRecord = methodRecordList.get(i);
			String filedInfo = curRecord.getfSignature()+':'+curRecord.getfHashcode();
			String methodInfo = curRecord.getMcSignature()+"/"+curRecord.getMethodName();
			if(filedToMethod.containsKey(filedInfo)){
				filedToMethod.get(filedInfo).add(methodInfo);
			}
			else{
				Set<String> valueSet = new HashSet<String>();
				valueSet.add(methodInfo);
				filedToMethod.put(filedInfo, valueSet);
			}
		}
		
		generateDataDependency();
	}

	private void generateDataDependency() {
		dataDependencyMatrix = new int[methodNameToIndex.size()][methodNameToIndex.size()];
		
		InfoBetweenTwoMethod[][] ibm = new InfoBetweenTwoMethod[methodNameToIndex.size()][methodNameToIndex.size()];
		initIBM(ibm);
		
		Iterator<String> ite = filedToMethod.keySet().iterator();
		while(ite.hasNext()){
			String fileInfo = ite.next();
			String type = fileInfo.split(":")[0];
			buildDependencyBetweenTwoMethod(ibm,type,filedToMethod.get(fileInfo));
		}
		
		for(int i = 0; i < dataDependencyMatrix.length-1;i++){
			for(int j = i+1; j < dataDependencyMatrix.length;j++){
				int typeAmount = ibm[i][j].getDependencyType().size();
				dataDependencyMatrix[i][j] = typeAmount;
				dataDependencyMatrix[j][i] = typeAmount;
			}
		}
		
		//show
		//show();
	}

	private void initIBM(InfoBetweenTwoMethod[][] ibm) {
		for(int row = 0; row < ibm.length;row++){
			for(int col = 0; col < ibm[0].length;col++){
				ibm[row][col] = new InfoBetweenTwoMethod();
			}
		}
	}

	private void show() {
		for(int i = 0; i < dataDependencyMatrix.length-1;i++){
			for(int j = i+1; j < dataDependencyMatrix.length;j++){
				String oneMethodName = indexToMethodName.get(i);
				String anotherMethodName = indexToMethodName.get(j);
				if(dataDependencyMatrix[i][j]>0){
					System.out.println(oneMethodName+"-------"+anotherMethodName+" "+dataDependencyMatrix[i][j]);
				}
			}
		}
	}

	private void buildDependencyBetweenTwoMethod(InfoBetweenTwoMethod[][] ibm, String type, Set<String> methodNameSet) {
		String[] methodName = new String[methodNameSet.size()];
		Iterator<String> ite = methodNameSet.iterator();
		int index = 0;
		while(ite.hasNext()){
			methodName[index] = ite.next();
		    index++;
		}
		
		for(int i = 0; i < methodName.length-1;i++){
			for(int j = i+1; j < methodName.length;j++){
				int row = methodNameToIndex.get(methodName[i]);
				int col = methodNameToIndex.get(methodName[j]);
				ibm[row][col].getDependencyType().add(type);
				ibm[col][row].getDependencyType().add(type);
			}
		}
	}

	private void process(Map<String, Integer> methodNameToIndex, Map<Integer, String> indexToMethodName) {
		int number = 0;
		for(int i = 0; i < methodRecordList.size();i++){
			FiledVisitRecord curRecord = methodRecordList.get(i);
			String methodName = curRecord.getMcSignature() + "/" + curRecord.getMethodName();
			if(methodNameToIndex.containsKey(methodName)){
				continue;
			}
			else{
				methodNameToIndex.put(methodName, number);
				indexToMethodName.put(number, methodName);
				number++;
			}
		}
	}
}
