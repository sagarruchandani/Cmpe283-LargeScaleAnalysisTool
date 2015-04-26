package Team13;

public class Analysis {
	

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		 
		
		//PerformanceManagerMonitor pm = new PerformanceManagerMonitor();
		//CopyOfPerformanceManagerMonitor x= new CopyOfPerformanceManagerMonitor();
		//System.out.println("HEY MAIN");
		
		//MongoToMysqlThread m2m = new MongoToMysqlThread("VM1");
		//MongoToMysqlThread VM2 = new MongoToMysqlThread("VM2");
		MongoToMysqlThread VM3 = new MongoToMysqlThread("VM3");
		MongoToMysqlThread VM4 = new MongoToMysqlThread("VM4");
//		MongoToMysqlThread host1 = new MongoToMysqlThread("130.65.133.11");
//		MongoToMysqlThread host2 = new MongoToMysqlThread("130.65.133.12");
//		MongoToMysqlThread host3 = new MongoToMysqlThread("130.65.133.13");
//		
		
		
//		m2m.start();
//		VM2.start();
		VM3.start();
		VM4.start();
//		host1.start();
//		host2.start();
//		host3.start();
//		
		
		
		
		//System.out.println("HEY2");
		
		
	}

	
}
