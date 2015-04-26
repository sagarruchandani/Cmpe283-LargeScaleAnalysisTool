package Team13;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoToMysqlThread extends Thread{
	private String host_name;
	public MongoToMysqlThread(String h){
		host_name=h;
	}
	
	public void run()
	{
	//public static void main(String[] args) throws Exception{
while(true){
	try {
		 
		System.out.println("HEY");
		MongoClientURI uri  = new MongoClientURI("mongodb://cmpe283:cmpe283@ds053370.mongolab.com:53370/mydb");
		MongoClient mongo = new MongoClient(uri);
		MySqlConnection mysql = new MySqlConnection();
		DB db = mongo.getDB(uri.getDatabase());
		DBCollection logstash = db.getCollection("testData");
		
        
		System.out.println("HEY");
		////List distinct_host = logstash.distinct("HostName");
		
		//Iterator<String> iterator = distinct_host.iterator();
		////for(Iterator<String> iterator = distinct_host.iterator();iterator.hasNext(); ){
			//List distinct_metric = logstash.find(iterator.next());
			
			//String host_name = iterator.next();
			System.out.println("HEY, "+host_name);
			BasicDBObject distinctQuery = new BasicDBObject();
			distinctQuery.put("HostName", host_name);
			List distinct_metrics = logstash.distinct("MetricID",distinctQuery);
			for(Iterator<String> iterator2 = distinct_metrics.iterator();iterator2.hasNext(); ){
				//System.out.println("inside iterator");
				String met_id = iterator2.next();
				BasicDBObject searchQuery = new BasicDBObject();
				searchQuery.put("HostName", host_name);
				searchQuery.put("MetricID", met_id);
				//searchQuery.put("Time_Stamp",date_minus_30);//new BasicDBObject("$lt",.append("$lt", current_date));
				//System.out.println(date_minus_30.toString());
				DBCursor cursor = logstash.find(searchQuery);
				
				
				BasicDBObject obj1 = (BasicDBObject) cursor.next();
				String HostName = (String) obj1.getString("HostName");
				String date_string1 = (String) obj1.get("TimeStamp");
				
			    java.util.Date time1= new SimpleDateFormat("E MMM d k:m:s z yyyy",Locale.ENGLISH).parse(date_string1);
			    
			    
			    Integer MetricValue1 = Integer.parseInt((obj1.get("MetricValue")).toString());
			    int counter=1;
			    while (cursor.hasNext()) {
					//System.out.println(cursor.next());
			    	BasicDBObject obj = (BasicDBObject) cursor.next();
				    
					String date_string = (String) obj.get("TimeStamp");
				    java.util.Date date= new SimpleDateFormat("E MMM d k:m:s z yyyy",Locale.ENGLISH).parse(date_string);
				    MetricValue1 += Integer.parseInt((obj.get("MetricValue")).toString());
				    counter++;
				    //System.out.println(counter+"- of metric: "+met_id+" at time: "+date);
				    if(date.getTime()-time1.getTime()>=300000){
				    	Integer MetricID = Integer.parseInt((obj.get("MetricID")).toString());
					    String GroupType = (String) obj.getString("GroupType");
					    String DataType = (String) obj.getString("DataType");
					    //System.out.println("inside if condition...............................");
					    Integer MetricValue = MetricValue1/counter; // Average
					    counter=1;
					    time1=date;
					    mysql.insert(HostName, date, MetricID, GroupType, MetricValue, DataType);
						   
				    }
				    BasicDBObject document = new BasicDBObject();
					document.put("HostName", host_name);
					document.put("MetricID", met_id);
					document.put("TimeStamp", date_string);
					logstash.remove(document);
					//System.out.println("Deleted>> "+host_name+", Its metric: "+met_id+" at time: "+date);
				    
				}
			    
					
				    
				    
				    
				     //System.out.println("host: "+HostName+" date: "+date+" MetricID: "+MetricID+" GroupType: "+GroupType+" MetricValue: "+MetricValue+" DataType: "+DataType);
					
				
			} 
			//for loop iterator}
		mysql.close();
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		continue;
	}
	try {
		System.out.println("Sleeping. Shhh Time for "+host_name+"..........");
		Thread.sleep(1800000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		continue;
	}
	}// end of while

} 
}