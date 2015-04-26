package Team13;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintStream;

import com.vmware.vim25.ElementDescription;
import com.vmware.vim25.PerfCounterInfo;
import com.vmware.vim25.PerfEntityMetric;
import com.vmware.vim25.PerfEntityMetricBase;
import com.vmware.vim25.PerfEntityMetricCSV;
import com.vmware.vim25.PerfInterval;
import com.vmware.vim25.PerfMetricId;
import com.vmware.vim25.PerfMetricIntSeries;
import com.vmware.vim25.PerfMetricSeries;
import com.vmware.vim25.PerfMetricSeriesCSV;
import com.vmware.vim25.PerfQuerySpec;
import com.vmware.vim25.PerfSampleInfo;
import com.vmware.vim25.PerformanceDescription;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.ManagedEntity;

public class VmFunctions {
	
	//CReate or Check for the existing new file 
	File logFile;
	PrintStream console = System.out;
	FileOutputStream outFileStream;
	PrintStream psWriteFile;
	VMURLS vmurl = new VMURLS();
	public void getAllHost(VMURLS vmurl) throws Exception
	{
		for(int i=0;i<vmurl.mes_admin().length;i++){
			HostSystem host=(HostSystem) vmurl.mes_admin()[i];
			
			System.out.println("Name::"+ host.getName());
			
			
		}
		
	}

	public VmFunctions()
	{
		
	//	inBufferReader = new BufferedReader(new InputStreamReader(System.in));
		try{
		logFile = new File("C:/Users/kalyan/Documents/edu/283/project2.log");
		
		if(!logFile.exists())
		{
		logFile.createNewFile();
		}
		outFileStream = new FileOutputStream(logFile);
		psWriteFile = new PrintStream(outFileStream);
		System.setOut(psWriteFile);
		}
		catch(Exception e)
		{
			System.out.println("Error in opening file");
			
		}
	}

	  public void printPerfIntervals(PerfInterval[] pis)
	  {
	    for(int i=0; pis!=null && i<pis.length; i++)
	    {
	      System.out.println("\nPerfInterval # " + i);
	      StringBuffer sb = new StringBuffer();
	      sb.append("Name:" + pis[i].getName());
	      sb.append("\nKey:" + pis[i].getKey());
	      sb.append("\nLevel:"+ pis[i].getLevel());
	      sb.append("\nSamplingPeriod:" + pis[i].getSamplingPeriod());
	      sb.append("\nLength:" + pis[i].getLength());
	      sb.append("\nEnabled:" + pis[i].isEnabled());
	      System.out.println(sb);
	    }
	  }
	 public void printPerfDescription(PerformanceDescription pd)
	  {
		 ElementDescription[] eds = pd.getCounterType();
		    printElementDescriptions(eds);
		   
		    ElementDescription[] statsTypes = pd.getStatsType();
		    printElementDescriptions(statsTypes);

	  }
	  
	  public void printElementDescriptions(ElementDescription[] eds)
	  {
	    for(int i=0; eds!=null && i<eds.length; i++)
	    {
	      printElementDescription(eds[i]);
	    }
	  }


	  public void printPerfCounters(PerfCounterInfo[] pcis)
	  {
	    for(int i=0; pcis!=null && i<pcis.length; i++)
	    {
	    	String perfCounterString = pcis[i].getNameInfo().getLabel() + " (" + pcis[i].getGroupInfo().getKey() + ") in "
					 + pcis[i].getUnitInfo().getLabel() + " (" + pcis[i].getStatsType().toString() + ") " 
	    			 +" Group Info: "+pcis[i].groupInfo.getSummary()+" Name Info: "+pcis[i].getNameInfo().getSummary();
			System.out.println(pcis[i].getKey() + " : " + perfCounterString);
	    }
	  }

	  


	  static void printElementDescription(ElementDescription ed)
	  {
	    System.out.println("\nKey:" + ed.getKey());
	    System.out.println("Label:" + ed.getLabel());
	    System.out.println("Summary:" + ed.getSummary());
	  }




	  public void printPerfMetric(PerfEntityMetric pem)
	  {
	    PerfMetricSeries[] vals = pem.getValue();
	    PerfSampleInfo[]  infos = pem.getSampleInfo();
	    
	    System.out.println("Sampling Times and Intervales:");
	    for(int i=0; infos!=null && i <infos.length; i++)
	    {
	      System.out.println("Sample time: " 
	          + infos[i].getTimestamp().getTime());
	      System.out.println("Sample interval (sec):" 
	          + infos[i].getInterval());
	    }
	    System.out.println("Sample values:");
	    for(int j=0; vals!=null && j<vals.length; ++j)
	    {
	      System.out.println("Perf counter ID:" 
	          + vals[j].getId().getCounterId());
	      System.out.println("Device instance ID:" 
	          + vals[j].getId().getInstance());
	      
	      if(vals[j] instanceof PerfMetricIntSeries)
	      {
	        PerfMetricIntSeries val = (PerfMetricIntSeries) vals[j];
	        long[] longs = val.getValue();
	        for(int k=0; k<longs.length; k++) 
	        {
	          System.out.print(longs[k] + " ");
	        }
	        System.out.println("Total:"+longs.length);
	      }
	      else if(vals[j] instanceof PerfMetricSeriesCSV)
	      { // it is not likely coming here...
	        PerfMetricSeriesCSV val = (PerfMetricSeriesCSV) vals[j];
	        System.out.println("CSV value:" + val.getValue());
	      }
	    }
	  }
	    
	 public void printPerfMetricCSV(PerfEntityMetricCSV pems)
	  {
	    System.out.println("SampleInfoCSV:" 
	        + pems.getSampleInfoCSV());
	    PerfMetricSeriesCSV[] csvs = pems.getValue();
	    for(int i=0; i<csvs.length; i++)
	    {
	      System.out.println("PerfCounterId:" 
	          + csvs[i].getId().getCounterId());
	      System.out.println("CSV sample values:" 
	          + csvs[i].getValue());
	    }
	  }



	 public void displayValues(PerfEntityMetricBase[] values)
	  {
	    for(int i=0; i<values.length; ++i) 
	    {
	      String entityDesc = values[i].getEntity().getType() 
	          + ":" + values[i].getEntity().get_value();
	      System.out.println("Entity:" + entityDesc);
	      if(values[i] instanceof PerfEntityMetric)
	      {
	        printPerfMetric((PerfEntityMetric)values[i]);
	      }
	      else if(values[i] instanceof PerfEntityMetricCSV)
	      {
	        printPerfMetricCSV((PerfEntityMetricCSV)values[i]);
	      }
	      else
	      {
	        System.out.println("UnExpected sub-type of " +
	        		"PerfEntityMetricBase.");
	      }
	    }
	  }

	 public PerfQuerySpec createPerfQuerySpec(ManagedEntity me, 
		      PerfMetricId[] metricIds, int maxSample, int interval)
		  {
		    PerfQuerySpec qSpec = new PerfQuerySpec();
		    qSpec.setEntity(me.getMOR());
		    // set the maximum of metrics to be return
		    // only appropriate in real-time performance collecting
		    qSpec.setMaxSample(new Integer(maxSample));
//		    qSpec.setMetricId(metricIds);
		    // optionally you can set format as "normal"
		    qSpec.setFormat("csv");
		    // set the interval to the refresh rate for the entity
		    qSpec.setIntervalId(new Integer(interval));
		 
		    return qSpec;
		  }
	 
	

	
}