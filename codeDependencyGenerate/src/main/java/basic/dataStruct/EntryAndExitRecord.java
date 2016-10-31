package basic.dataStruct;

public class EntryAndExitRecord {
	private char callFlag;
	private String classSignature;
	private String methodName;
	private String threadID;
	
	public EntryAndExitRecord(char callFlag,String classSignature,String methodName,String threadID){
		this.callFlag = callFlag;
		this.classSignature = classSignature;
		this.methodName = methodName;
		this.threadID = threadID;
	}
	
	public char getCallFlag() {
		return callFlag;
	}

	public void setCallFlag(char callFlag) {
		this.callFlag = callFlag;
	}

	public String getClassSignature() {
		return classSignature;
	}

	public void setClassSignature(String classSignature) {
		this.classSignature = classSignature;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getThreadID() {
		return threadID;
	}

	public void setThreadID(String threadID) {
		this.threadID = threadID;
	}

}
