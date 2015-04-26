package Team13;

import java.util.Date;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

 
public class MySqlConnection {
 
	Connection connection = null;
  public MySqlConnection() {
 
	System.out.println("-------- MySQL JDBC Connection Testing ------------");
 
	try {
		Class.forName("com.mysql.jdbc.Driver");
	} catch (ClassNotFoundException e) {
		//System.out.println("MYSQL driver error");
		System.out.println("MySQL JDBC Driver not Registered!");
		e.printStackTrace();
		
	}
 
	
	
 
	try {
		connection = DriverManager
		.getConnection("jdbc:mysql://cmpe285.crf2mftam4cg.us-east-1.rds.amazonaws.com:3306/cmpe285","root", "kalyan123");
 
	} catch (SQLException e) {
		System.out.println("Connection Failed! Check output console");
		e.printStackTrace();
	
	}
 
	if (connection != null) {
		System.out.println("Database connected now!");
	} else {
		System.out.println("Failed to make connection!");
	}
//return connection;
  }
  
  public void insert(String HostName,java.util.Date date_time,Integer MetricID,String GroupType,Integer MetricValue,String DataType) throws Exception{
	  String query = " insert into cmpe283_logs (HostName, date_time, MetricID, GroupType, MetricValue, DataType)"
		        + " values (?, ?, ?, ?, ?, ?)";
	  System.out.println("Inserted>>> host: "+HostName+" date: "+date_time+" MetricID: "+MetricID+" GroupType: "+GroupType+" MetricValue: "+MetricValue+" DataType: "+DataType);
		
		      // create the mysql insert preparedstatement
		      PreparedStatement preparedStmt = connection.prepareStatement(query);
		      preparedStmt.setString(1, HostName); //HostName
		      preparedStmt.setTimestamp(2, new java.sql.Timestamp(date_time.getTime())); //date
		      preparedStmt.setInt(3, MetricID); //MetricID
		      preparedStmt.setString(4, GroupType); //GroupType
		      preparedStmt.setInt(5, MetricValue); //MetricValue
		      preparedStmt.setString(6, DataType); //DataType
		      // execute the preparedstatement
		      preparedStmt.execute();
		       
		      //connection.close();
  }
  public void close() throws SQLException{
	  connection.close();
  }
  
}