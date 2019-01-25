import java.util.ArrayList;

public class LogContinous {
		String eventName;
		ArrayList<Double>dataList;
		double mean;
		double stdDev;
		ArrayList<Double>anomaly;
		
		public LogContinous(String e) {
			eventName = e;
			dataList = new ArrayList<Double>();
			mean = 0;
			stdDev = 0;
			anomaly =  new ArrayList<Double>();
		}
		
		public LogContinous(String e, ArrayList<Double>d) {
			eventName = e;
			dataList = d;
			mean = 0;
			stdDev = 0;
			anomaly =  new ArrayList<Double>();
		}
		
		public void insertData(double d) {
			dataList.add(d);
		}

		
		public String getEventName() {
			return eventName;
		}

		public void setEventName(String eventName) {
			this.eventName = eventName;
		}

		public ArrayList<Double> getDataList() {
			return dataList;
		}

		public void setDataList(ArrayList<Double> dataList) {
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
			return "LogContinous [eventName=" + eventName + ", dataList=" + dataList 
					+  "Mean: " + mean + ", StdDev: " + stdDev + "\n" 
					+  "Anomaly: " + anomaly + "]";
		}
		
		
		
}
