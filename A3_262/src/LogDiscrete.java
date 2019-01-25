import java.util.ArrayList;

public class LogDiscrete {
	String eventName;
	ArrayList<Integer>dataList;
	double mean;
	double stdDev;
	ArrayList<Double>anomaly;
	
	public LogDiscrete(String e) {
		eventName = e;
		dataList = new ArrayList<Integer>();
	}
	
	public LogDiscrete(String e, ArrayList<Integer>d) {
		eventName = e;
		dataList = d;
	}
	
	public void insertData(int d) {
		dataList.add(d);
	}
	
	
	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public ArrayList<Integer> getDataList() {
		return dataList;
	}

	public void setDataList(ArrayList<Integer> dataList) {
		this.dataList = dataList;
	}
	
	public void setMean(double m) {
		mean = m;
	}
	
	public void setStdDev(double s) {
		stdDev =  s;
	}
	
	public double getMean() {
		return mean;
	}
	
	public double getStdDev() {
		return stdDev;
	}
	
	public void insertAnomalyData(double e) {
		anomaly.add(e);
	}
	
	public void setAnomalyList(ArrayList<Double>l) {
		anomaly = l;
	}
	
	public ArrayList<Double>getAnomaly(){
		return anomaly;
	}
	
	@Override
	public String toString() {
		return "LogDiscrete [eventName=" + eventName + ", dataList=" + dataList 
				+  "Mean: " + mean + ", StdDev: " + stdDev + "\n" 
				+  "Anomaly: " + anomaly + "]";
	}
	
}
