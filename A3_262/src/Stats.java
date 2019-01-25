
public class Stats {
	private String name;
	private double mean;
	private double stdDev;
	
	public Stats(String name, double mean, double stdDev) {
		this.name = name;
		this.mean = mean;
		this.stdDev = stdDev;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}

	public double getStdDev() {
		return stdDev;
	}

	public void setStdDev(double stdDev) {
		this.stdDev = stdDev;
	}
	
	public String toString() {
		String str;
		
		str = getName() + "|" + getMean() + "|"+ getStdDev() ;
		return str;
	}
	
}
