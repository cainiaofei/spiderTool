package basic.dataStruct;

import java.util.HashSet;
import java.util.Set;

public class InfoBetweenTwoMethod {
	private Set<String> dependencyType;
	
	public InfoBetweenTwoMethod(){
		dependencyType = new HashSet<String>();
	}

	public Set<String> getDependencyType() {
		return dependencyType;
	}
	
	public void setDependencyType(Set<String> dependencyType) {
		this.dependencyType = dependencyType;
	}
}
