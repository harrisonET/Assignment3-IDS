
public class ContinousEvents{
	private double min;
	private double max;
	private String eventName;
	private int weight;
	private char eventType;
	
	public ContinousEvents(String e, char eventType, double min, double max, int weight){
		this.eventName = e;
		this.weight = weight;
		this.eventType = eventType;
		this.min = min;
		this.max = max;
	}
	
	//accessor
	public String getEventName(){
		return eventName;
	}
	
	public int getWeight(){
		return weight;
	}
	
	public char getEventType(){
		return eventType;
	}
	
	//mutator
	public void setEventName(String n){
		this.eventName = n;
	}
	
	public void setWeight(int weight){
		this.weight = weight;
	}
	
	public void setEventType(char c){
		this.eventType = c;
	}
	
	
		//accessor
	public double getMin(){
		return min;
	}
	
	public double getMax(){
		return max;
	}
	
	//mutator
	public void setMin(double m){
		this.min = m;
	}
	
	public void setMax(double m){
		this.max = m;
	}
	public String toString() {
		String str;
		
		str = getEventName() + "|" +  "c|" + getMin() + "|"+ getMax() + "|" + getWeight();
		return str;
	}
}