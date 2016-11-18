package dataSetGenerate.codeDependencyGenerate;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import basic.dataStruct.EntryAndExitRecord;
import basic.dbProcess.DBOperate;

public class CallDependencyCapturer {
	
	private List<EntryAndExitRecord> methodRecordList = new LinkedList<EntryAndExitRecord>();
	private Map<String,Integer> methodToIndex = new HashMap<String,Integer>();
	private Map<Integer,String> indexToMethod = new HashMap<Integer,String>();
	private char[][] callDependencyMatrix;
	private DBOperate db = null;
	
	public CallDependencyCapturer(String dbName){
		db = new DBOperate(dbName);
	}
	
	private void readFromDB() throws SQLException{
		String sql = "select callFlag, classSignature, methodName, threadID from callGraph";
		db.callDependencyExecuteSelect(methodRecordList,sql);
	}
	
	public void buildMapBetweenMethodAndIndex(List<EntryAndExitRecord> methodRecordList){
		for(EntryAndExitRecord methodRecord:methodRecordList){
			String methodIdentifier = methodRecord.getClassSignature() + "/" + methodRecord.getMethodName();
			if(methodToIndex.containsKey(methodIdentifier)){
				continue;
			}
			else{
				int value = methodToIndex.size();
				methodToIndex.put(methodIdentifier, value);
				indexToMethod.put(value, methodIdentifier);
			}
		}
	}
	

	/*
	 * deal with call dependency
	 * traverse the map build call dependency 
	 * use matrix represent the call dependency
	 */
	public void callDependencyCapturer(){
		try {
			readFromDB();
		} catch (SQLException e) {
			System.out.println("read db exception");
			e.printStackTrace();
		}
		buildMapBetweenMethodAndIndex(methodRecordList);///methodToIndex indexToMethod
		
		callDependencyMatrix = new char[methodToIndex.size()][methodToIndex.size()];
		
		Map<String,List<EntryAndExitRecord>>threadIDToMethods = splitIntoSubListByThreadID(methodRecordList);
		
		Iterator<String> ite = threadIDToMethods.keySet().iterator();
		while(ite.hasNext()){
			String curThreadID = ite.next();
			List<EntryAndExitRecord> subRecordList = threadIDToMethods.get(curThreadID);
			buildCallDependency(curThreadID,subRecordList);
		}
		show();
	}
	
	private Map<String,List<EntryAndExitRecord>> splitIntoSubListByThreadID(List<EntryAndExitRecord> methodRecordList) {
		Map<String,List<EntryAndExitRecord>> threadIDToMethods = new HashMap<String,List<EntryAndExitRecord>>();
		for(EntryAndExitRecord record:methodRecordList){
			String curThreadID = record.getThreadID();
			if(threadIDToMethods.containsKey(curThreadID)){
				threadIDToMethods.get(curThreadID).add(record);
			}
			else{
				List<EntryAndExitRecord> subList = new LinkedList<EntryAndExitRecord>();
				subList.add(record);
				threadIDToMethods.put(curThreadID, subList);
			}
		}
		return threadIDToMethods;
	}

	

	private void buildCallDependency(String curThreadID, List<EntryAndExitRecord> subRecordList) {
		Stack<EntryAndExitRecord> methodStack = new Stack<EntryAndExitRecord>();
		int pos = 0;
		while(pos<subRecordList.size()){
			EntryAndExitRecord curMethodRecord = methodRecordList.get(pos);
			if(curMethodRecord.getCallFlag()=='E'){//method entry
				methodStack.push(curMethodRecord);
			}
			else{//method exit
				if(methodStack.isEmpty()){
					System.out.println("InConsistency"+curMethodRecord.getClassSignature()+
							curMethodRecord.getMethodName());
					System.exit(0);
				}
				else{
					EntryAndExitRecord pre = methodStack.pop();
					if(!isSameMethod(pre,curMethodRecord)){
						System.out.println("InConsistency"+curMethodRecord.getClassSignature()+
								curMethodRecord.getMethodName());
						System.exit(0);
					}
					else{
						if(!methodStack.isEmpty()){
							String curMethodFullName = curMethodRecord.getClassSignature()+"/"+curMethodRecord.getMethodName();
							EntryAndExitRecord parentRecord = methodStack.peek();
							String parent = parentRecord.getClassSignature()+"/"+parentRecord.getMethodName();
							int node1 = methodToIndex.get(curMethodFullName);
							int node2 = methodToIndex.get(parent);
							callDependencyMatrix[node1][node2] = 'X';
							callDependencyMatrix[node2][node1] = 'X';
						}
					}
				}
			}
			pos++;
		}
	}


	private boolean isSameMethod(EntryAndExitRecord pre, EntryAndExitRecord cur) {
		return (pre.getClassSignature().equals(cur.getClassSignature())&&pre.getMethodName().equals(cur.getMethodName()));
	}
	
	public void show(){
		System.out.println("---------------------------");
		for(int row = 0; row < callDependencyMatrix.length;row++){
			for(int col = row; col < callDependencyMatrix[0].length;col++){
				if(callDependencyMatrix[row][col]=='X'){
					System.out.println(indexToMethod.get(row)+"---------"+indexToMethod.get(col));
				}
			}
		}
	}
}
