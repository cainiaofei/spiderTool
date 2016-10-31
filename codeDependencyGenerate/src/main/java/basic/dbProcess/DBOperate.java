package basic.dbProcess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import basic.dataStruct.EntryAndExitRecord;

public class DBOperate {
	private Connection conn = null;
	
	public DBOperate(String db){
		try {
			buildConnection(db);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void buildConnection(String db) throws SQLException{
		String sqliteDriver = "org.sqlite.JDBC";
		try {
			Class.forName(sqliteDriver);
			conn = DriverManager.getConnection(db);//connect .db file
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void callDependencyExecuteSelect(List<EntryAndExitRecord> recordList,String sql) throws SQLException{
		Statement stat = conn.createStatement();
		ResultSet resultSet = stat.executeQuery(sql);
		while(resultSet.next()){
			char callFlag = resultSet.getString(1).charAt(0);
			String classSignature = resultSet.getString(2);
			classSignature = classSignature.substring(0, classSignature.length()-1);//remove semicolon
			String methodName = resultSet.getString(3);
			String threadID = resultSet.getString(4);
			recordList.add(new EntryAndExitRecord(callFlag,classSignature,methodName,threadID));
			//System.out.println(callFlag+"  "+classSignature+"  "+methodName+"  "+threadID);
		}
		stat.close();
		conn.close();
	}
}
