package Team13;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.vmware.vim25.PerfCounterInfo;
import com.vmware.vim25.PerfEntityMetric;
import com.vmware.vim25.PerfEntityMetricBase;
import com.vmware.vim25.PerfMetricId;
import com.vmware.vim25.PerfMetricIntSeries;
import com.vmware.vim25.PerfMetricSeries;
import com.vmware.vim25.PerfProviderSummary;
import com.vmware.vim25.PerfQuerySpec;
import com.vmware.vim25.PerfSampleInfo;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.PerformanceManager;
import com.vmware.vim25.mo.VirtualMachine;

public class PerformanceManagerMonitor {
	File logFile;
	PrintStream console = System.out;
	FileOutputStream outFileStream;
	PrintStream psWriteFile;
	VMURLS vmurl = new VMURLS();
	//VmFunctions vmfunc = new VmFunctions();

	public PerformanceManagerMonitor() throws Exception {
		
		try {
			logFile = new File("c:\\Users\\Sagar\\Desktop\\project2.log");
			//this.performanceManager();
			
			
			if(!logFile.exists())
			{
			logFile.createNewFile();
			}
			outFileStream = new FileOutputStream(logFile,true);
			psWriteFile = new PrintStream(outFileStream);
			System.setOut(psWriteFile);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			System.out.println("Error in getting performance details");
//			e.printStackTrace();
			PerformanceManagerMonitor pm = new PerformanceManagerMonitor();
		}
		try {
			while(true){
			this.realTimeMonitor();
			Thread.sleep(20000); // Wait 20 seconds and collect again
			// Call a function, which collects mongo stats of 5 minutes
			// , averages them and puts in mysql
			
			}
		} catch (Exception e) {
			//System.out.println("Error in getting real time details");
			e.printStackTrace();
			Thread.sleep(20000);
			PerformanceManagerMonitor pm = new PerformanceManagerMonitor();
		}

	}

	// COLLECT PERFORMANCE DESCIPTIONS AND COUNTERS
//	public void performanceManager() throws Exception {
		//PerformanceManager perfMgr = vmurl.si_team().getPerformanceManager();
		// Counter and Interval description... Not needed for now.
		// System.out.println("***Print All Descriptions:");
		// PerformanceDescription pd = perfMgr.getDescription();
		// vmfunc.printPerfDescription(pd);
		// System.out.println("====================================");
		// System.out.println("\n***Print All Historical Intervals:");
		// PerfInterval[] pis = perfMgr.getHistoricalInterval();
		// vmfunc.printPerfIntervals(pis);
		//
		// System.out.println("\n***Print All Perf Counters:");
		// PerfCounterInfo[] pcis = perfMgr.getPerfCounter();
		// vmfunc.printPerfCounters(pcis);

//	}

	public void realTimeMonitor() throws Exception {

		for (int i = 0; i < vmurl.mes_team().length; i++) {
			HostSystem hosts = (HostSystem) vmurl.mes_team()[i];
			//realTimeMetrics(hosts);
			//VirtualMachine[] vm = hosts.getVms();
			realTimeMetrics(hosts);
			//for (int j = 0; j < vm.length; j++) {
				// CHeck if VM is powered On or Off. If Powered On, then take
				// the metrics.
				//System.out.println(vm[j]+">>"+vm[j].getName()+">>");
				//realTimeMetrics(vm[j]);
			//}
		}

	}

	public void realTimeMetrics(ManagedEntity mes) throws Exception {
		PerformanceManager perfMgr = vmurl.si_team().getPerformanceManager();
		PerfProviderSummary summary = perfMgr.queryPerfProviderSummary(mes);
		int perfInterval = summary.getRefreshRate();
		PerfMetricId[] queryAvailablePerfMetric = perfMgr
				.queryAvailablePerfMetric(mes, null, null, perfInterval);
		int ids[] = new int[534];
		for (int q = 1; q < 534; q++) {
			ids[q] = q;
		}

		ArrayList<PerfMetricId> list = new ArrayList<PerfMetricId>();

		for (int i = 0; i < ids.length; i++) {
			// System.out.println("list ids:::"+ids[i]);
			for (int i2 = 0; i2 < queryAvailablePerfMetric.length; i2++) {

				PerfMetricId perfMetricId = queryAvailablePerfMetric[i2];
				if (ids[i] == perfMetricId.getCounterId()) {
					// Printing available counters of a given MES
					// System.out.println("perfMetricId::::	"+perfMetricId.getCounterId());
					list.add(perfMetricId);
					break;
				}
			}
		}
		PerfMetricId[] pmis2 = list.toArray(new PerfMetricId[list.size()]);
		PerfQuerySpec qSpec2 = new PerfQuerySpec();
		qSpec2.setEntity(mes.getMOR());
		qSpec2.setMetricId(pmis2);
		qSpec2.setMaxSample(new Integer(1));
		qSpec2.intervalId = perfInterval;

		PerfEntityMetricBase[] pembs = perfMgr
				.queryPerf(new PerfQuerySpec[] { qSpec2 });
		PerfCounterInfo[] pcis3 = perfMgr.getPerfCounter();

		for (int i = 0; pembs != null && i < pembs.length; i++) {
			PerfEntityMetricBase val = pembs[i];
			PerfEntityMetric pem = (PerfEntityMetric) val;
			PerfMetricSeries[] vals = pem.getValue();
			PerfSampleInfo[] infos = pem.getSampleInfo();
			//System.out.println();
			for (int j = 0; vals != null && j < vals.length; ++j) {
				PerfMetricIntSeries val1 = (PerfMetricIntSeries) vals[j];
				long[] metric_value = val1.getValue();
				String mes_name = mes.getName().toString();
				String group_info_summary = pcis3[j].groupInfo.summary;
				int counter_id = val1.id.counterId;
				for (int k = 0; k < metric_value.length; k++) {
					
					System.out.println(mes_name + " " + counter_id + " "
							+ group_info_summary + " "
							+ infos[k].getTimestamp().getTime() + " "
							+ metric_value[k] + " "
							+ pcis3[j].getUnitInfo().getLabel());
				}
			}
		}
	}
	
	
	
	
	
	
	
	
}
