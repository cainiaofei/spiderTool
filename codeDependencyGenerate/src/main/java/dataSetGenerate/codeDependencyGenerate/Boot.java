package dataSetGenerate.codeDependencyGenerate;

import java.sql.SQLException;


public class Boot {
	public static void main(String[] args) throws SQLException{
		String dbName = "jdbc:sqlite:F:\\CallDependencyTest\\CallGraph.db";
		CallDependencyCapturer callDependencyCapturer = new CallDependencyCapturer(dbName);
		callDependencyCapturer.callDependencyCapturer();
		System.out.println("--------------------------------------------");
	}
}
