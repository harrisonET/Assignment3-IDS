
public class DiscreteEvents{
	private int min;
	private int max;
	private String eventName;
	private int weight;
	private char eventType;
	
	public DiscreteEvents(String e, char eventType, int min, int max, int weight){
		this.eventName = e;
		this.weight = weight;
		this.eventType = eventType;
		this.min = min;
		this.max = max;
	}
	
	//accessor
	public int getMin(){
		return min;
	}
	
	public int getMax(){
		return max;
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
	
	//mutator
	public void setMin(int m){
		this.min = m;
	}
	
	public void setMax(int m){
		this.max = m;
	}
	
	public String toString() {
		String str;
		
		str = getEventName() + "|" + "d|" + getMin() + "|"+ getMax() + "|" + getWeight();
		return str;
	}
}