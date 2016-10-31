package dataSetGenerate.codeDependencyGenerate;

import java.sql.SQLException;
import java.util.Arrays;
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
	private Map<String, HashMap<String, Integer>> threadIDToMethods = new HashMap<String,HashMap<String,Integer>>();
	private char[][] callDependencyMatrix;
	//threadID, the method correspond List
	//private Map<Integer,List<String>> threadIDToMethods = new HashMap<Integer,List<String>>();
	private DBOperate db = null;
	
	public CallDependencyCapturer(String dbName){
		db = new DBOperate(dbName);
	}
	
	private void readFromDB() throws SQLException{
		String sql = "select callFlag, classSignature, methodName, threadID from callGraph";
		db.callDependencyExecuteSelect(methodRecordList,sql);
	}
	
	private void buildMethodToIndex(){
		Map<String,Integer> threadToIndex = new HashMap<String,Integer>();//threadID and current index
		for(int i = 0; i < methodRecordList.size();i++){
			EntryAndExitRecord curRecord = methodRecordList.get(i);
			String curThreadID = curRecord.getThreadID();
			if(threadToIndex.containsKey(curThreadID)){
				String curMethodFullName = curRecord.getClassSignature()+'/'+curRecord.getMethodName();
				Map<String,Integer> curValueMap = threadIDToMethods.get(curThreadID);
				if(curValueMap.containsKey(curMethodFullName)){
					;
				}
				else{
					int curIndex = threadToIndex.get(curThreadID);
					curValueMap.put(curMethodFullName, curIndex);
					threadToIndex.put(curThreadID, curIndex+1);
				}
			}
			else{
				threadToIndex.put(curThreadID,0);
				String curMethodFullName = curRecord.getClassSignature()+'/'+curRecord.getMethodName();
				HashMap<String,Integer> newValueMap = new HashMap<String,Integer>();
				newValueMap.put(curMethodFullName, 0);
				threadIDToMethods.put(curThreadID,newValueMap);
				threadToIndex.put(curThreadID, 1);//0+1
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
		buildMethodToIndex();
		Iterator<String> ite = threadIDToMethods.keySet().iterator();
		while(ite.hasNext()){
			String curThreadID = ite.next();
			HashMap<String,Integer> methodToIndex = threadIDToMethods.get(curThreadID);
			buildCallDependency(curThreadID,methodToIndex);
			Map<Integer,String> reverseMethodToIndex = new HashMap<Integer,String>();
			initReverseMethodToIndexWithMethodToIndex(reverseMethodToIndex,methodToIndex);
			this.show(reverseMethodToIndex);
		}
	}
	
	private void initReverseMethodToIndexWithMethodToIndex(Map<Integer, String> reverseMethodToIndex,
			HashMap<String, Integer> methodToIndex) {
		Iterator<String> ite = methodToIndex.keySet().iterator();
		while(ite.hasNext()){
			String methodName = ite.next();
			reverseMethodToIndex.put(methodToIndex.get(methodName), methodName);
		}
	}

	private void buildCallDependency(String curThreadID, HashMap<String,Integer> methodToIndex) {
		Stack<EntryAndExitRecord> methodStack = new Stack<EntryAndExitRecord>();
		callDependencyMatrix = new char[methodToIndex.size()][methodToIndex.size()];
		initCallDependencyMatrixWithBlank(callDependencyMatrix);
		int pos = 0;
		while(pos<methodRecordList.size()){
			EntryAndExitRecord curMethodRecord = methodRecordList.get(pos);
			if(!curMethodRecord.getThreadID().equals(curThreadID)){
				pos++;
				continue;
			}
			else{
				if(curMethodRecord.getCallFlag()=='E'){//method entry
					methodStack.push(curMethodRecord);
				}
				else{//method exit
					if(methodStack.isEmpty()){
						System.out.println("InConsistency"+curMethodRecord.getClassSignature()+
								curMethodRecord.getMethodName());
					}
					else{
						EntryAndExitRecord pre = methodStack.pop();
						if(!isSameMethod(pre,curMethodRecord)){
							System.out.println("InConsistency"+curMethodRecord.getClassSignature()+
									curMethodRecord.getMethodName());
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
			}
			pos++;
		}
	}

	private void initCallDependencyMatrixWithBlank(char[][] callDependencyMatrix) {
		for(int row = 0; row < callDependencyMatrix.length;row++){
			Arrays.fill(callDependencyMatrix[row], ' ');
		}
	}

	private boolean isSameMethod(EntryAndExitRecord pre, EntryAndExitRecord cur) {
		return (pre.getClassSignature().equals(cur.getClassSignature())&&pre.getMethodName().equals(cur.getMethodName()));
	}
	
	public void show(Map<Integer,String> indexToMethodFullName){
		System.out.println("---------------------------");
		for(int row = 0; row < callDependencyMatrix.length;row++){
			for(int col = row; col < callDependencyMatrix[0].length;col++){
				if(callDependencyMatrix[row][col]=='X'){
					System.out.println(indexToMethodFullName.get(row)+"---------"+indexToMethodFullName.get(col));
				}
			}
		}
	}
}
