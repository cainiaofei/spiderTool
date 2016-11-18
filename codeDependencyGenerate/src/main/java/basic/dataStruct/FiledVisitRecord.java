package basic.dataStruct;

public class FiledVisitRecord {
	private String fSignature;
	private String fHashcode;
	
	private String McSignature;
	private String methodName;
	
	public FiledVisitRecord(String fSignature,String fHashcode,String McSignature,String methodName){
		this.fSignature = fSignature;
		this.fHashcode = fHashcode;
		this.McSignature = McSignature;
		this.methodName = methodName;
	}
	
	public String getfSignature() {
		return fSignature;
	}

	public void setfSignature(String fSignature) {
		this.fSignature = fSignature;
	}

	public String getfHashcode() {
		return fHashcode;
	}

	public void setfHashcode(String fHashcode) {
		this.fHashcode = fHashcode;
	}

	public String getMcSignature() {
		return McSignature;
	}

	public void setMcSignature(String mcSignature) {
		McSignature = mcSignature;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
}
