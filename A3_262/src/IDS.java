import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import javax.xml.crypto.Data;

public class IDS {
	public static ArrayList<DiscreteEvents> dEventsList = new ArrayList<DiscreteEvents>();
	public static ArrayList<ContinousEvents> cEventsList = new ArrayList<ContinousEvents>();
	public static ArrayList<Stats> statsList = new ArrayList<Stats>(); 
	public static ArrayList<LogDiscrete>discList = new ArrayList<LogDiscrete>();
	public static ArrayList<LogContinous>contList = new ArrayList<LogContinous>();
	
	//map events name to what type it is(C or D)
	private static Map<String,Character> nameToTypeMap = new HashMap<String,Character>();
	private int counter;

	public static void main(String[] args) throws FileNotFoundException {
		int Days = Integer.parseInt(args[2]);
		boolean repeat = true;
		Scanner sc;
		do {
			
			try {
				//IDS (Initial Input)
				readEvents(args[0]);
				readStats(args[1]);
			}
			catch(NumberFormatException e) {
				e.printStackTrace();
			}
			
			//IDS(Activity and Simulation Logs)
			//Doing training mode now
			simulation(Days,statsList);
			
			System.out.println("Finished Generating Event");
			System.out.println("Begin Analyzing...");
			
			Analysis(Days);
			AlertEngine(Days);
			
			System.out.println("Finished Training Data from " + args[1]);
			System.out.println("========================================");
			System.out.println("Start monitoring mode");
			System.out.print("Input new Stats file: ");
			
			sc = new Scanner(System.in);
			String fileName = sc.nextLine();
			
			System.out.print("Enter number of Days: ");
			Days = sc.nextInt();
			statsList.clear();
			discList.clear();
			contList.clear();
			try {
				//IDS (Initial Input)
				readStats(fileName);
			}
			catch(NumberFormatException e) {
				e.printStackTrace();
			}
	
			//IDS(Activity and Simulation Logs)
			//Doing training mode now
			simulation(Days,statsList);
			
			System.out.println("Finished Generating Event");
			System.out.println("Begin Analyzing...");
			
			Analysis(Days);
			AlertEngine(Days);
			
			String input;
			System.out.print("Do you want to repeat this simulation? (Y/N) = ");
			input = sc.next();
			
			repeat = false;
			
			if (input.equals("Y") || input.equals("y")) {
				repeat = true;
				dEventsList.clear();
				cEventsList.clear();
				statsList.clear();
				discList.clear();
				contList.clear();
			}else if (input.equals("N")) {
				repeat = false;
			}
		
		}while (repeat);
		
		System.out.println("Thank you using the IDS System");
		
		sc.close();
		
		System.exit(0);
	}
	
	public static void AlertEngine(int Days) {
		//Compute threshold = totalWeight / 2
		int totalWeight = 0;
		
		for(int i = 0; i < dEventsList.size();i++) {
			totalWeight += dEventsList.get(i).getWeight();
		}
		
		for(int i = 0; i < cEventsList.size();i++) {
			totalWeight += cEventsList.get(i).getWeight();
		}
		int threshold = totalWeight * 2;
		
		System.out.println("Threshold: " + threshold);
		
		for(int x = 0; x < discList.size();x++) {
			ArrayList<Integer>dataList = discList.get(x).getDataList();
			int weight = 0;
			System.out.print(discList.get(x).getEventName() + ": " );
			for(int i = 0; i < dEventsList.size();i++) {
				if (dEventsList.get(i).getEventName().equals(discList.get(x).getEventName())) {
					weight = dEventsList.get(i).getWeight();
				}
			}
			
			ArrayList<Double>anomalyList = new ArrayList<Double>();
			for(int i = 0; i < dataList.size();i++ ) {
				double anomaly = (Math.abs(discList.get(x).getMean() - 
						dataList.get(i))/discList.get(x).getStdDev())
						* weight;
	
				anomaly = Math.round(anomaly * 100.00) / 100.00;
				anomalyList.add(anomaly);
				System.out.print(anomaly + "|");				
			}
			System.out.println();
			
			discList.get(x).setAnomalyList(anomalyList);
			
		}
		
		for(int x = 0; x < contList.size();x++) {
			ArrayList<Double>dataList = contList.get(x).getDataList();
			int weight = 0;
			System.out.print(contList.get(x).getEventName() + ": " );
			for(int i = 0; i < cEventsList.size();i++) {
				if (cEventsList.get(i).getEventName().equals(contList.get(x).getEventName())) {
					weight = cEventsList.get(i).getWeight();
				}
			}
			ArrayList<Double>anomalyList = new ArrayList<Double>();
			for(int i = 0; i < dataList.size();i++ ) {
				double anomaly = (Math.abs(contList.get(x).getMean() - 
						dataList.get(i))/contList.get(x).getStdDev())
						* weight;
	
				anomaly = Math.round(anomaly * 100.00) / 100.00;
				anomalyList.add(anomaly);
				System.out.print(anomaly + "|");				
			}
			System.out.println();
			
			contList.get(x).setAnomalyList(anomalyList);
		}	
		System.out.println("==================================================");
		System.out.println("Anomaly Computed");
		int anomalyCounter = 0;
		int totalAnomalyCounter = 0;
		int weight = 0;
		for(int i = 1; i <= Days;i++) {
			double totalAnomaly = 0;
			for(int j = 0; j < discList.size();j++) {
				ArrayList<Double>anomalyList = discList.get(j).getAnomaly();
				totalAnomaly += anomalyList.get(i-1);
			}
			
			for(int k = 0; k < contList.size();k++) {
				ArrayList<Double>anomalyList = contList.get(k).getAnomaly();	
				totalAnomaly += anomalyList.get(i-1);				
			}
			
			totalAnomaly = Math.round(totalAnomaly * 100.0) / 100.0;
			System.out.print("Day " + i + ": " + totalAnomaly);
			
			if (totalAnomaly > threshold) {
				System.out.print("|Flaggged Anomaly");
				anomalyCounter++;
			}else {
				System.out.print("|Ok");
			}
			System.out.println();
			
		}
		System.out.println("Anomaly Counter Total: " + anomalyCounter);
	}
	
	public static void simulation(int Days, ArrayList<Stats> statList){
		
		//ArrayList<LogDiscrete>discList = new ArrayList<LogDiscrete>();
		//ArrayList<LogContinous>contList = new ArrayList<LogContinous>();
		
		LogDiscrete logDisc = null;
		LogContinous logCont = null;
		
		for(int j = 0; j < statsList.size();j++) {
				if (nameToTypeMap.get(statsList.get(j).getName()) == 'D') {
					logDisc = new LogDiscrete(statList.get(j).getName());
				}
				else {
					logCont = new LogContinous(statList.get(j).getName());
				}	
			
				for(int i=0;i< Days;i++) {
				//System.out.println("Day " + (i + 1));
				
				//System.out.println(statsList.get(j).getName());
								
				Random rand = new Random();
				int n = rand.nextInt(2);
				switch(n) {
				case 0 : n = -1; break;
				case 1: n = 1; break;
				}

				double mean = statsList.get(j).getMean();
				double stdDev = statsList.get(j).getStdDev();
				int highest =  (int) (mean + stdDev);
				
				int smallest =(int) Math.round(mean - stdDev);
				
				if (nameToTypeMap.get(statsList.get(j).getName()) == 'D'  ) {	
					int result = rand.nextInt( (highest+1) - smallest) + highest;
					//System.out.println("Result for D: " + result);
					logDisc.insertData(result);
				}
				// if Continous events
				else {
					double result = smallest + (highest - smallest) * rand.nextDouble();;
					result = Math.round(result * 100.00) / 100.00;
					//System.out.println("Result C: " + result);
					logCont.insertData(result);
				}
				
			}//for(int j = 0; j < statsList.size();j++) {
				
				if (nameToTypeMap.get(statsList.get(j).getName()) == 'D') {
					discList.add(logDisc);
				}else {
					contList.add(logCont);
				}	
		}		
		
		//print the log to textFile
		try {
			PrintWriter outFile = new PrintWriter(new File("log.txt"));
			
			for(int i = 0; i < discList.size();i++) {
				outFile.print(discList.get(i).getEventName());
				ArrayList<Integer>data =discList.get(i).getDataList();
				for(int j = 0; j < data.size();j++ ) {
					outFile.print("|" + data.get(j));
				}
				outFile.println();
			}
			
			for(int i = 0; i < contList.size();i++) {
				outFile.print(contList.get(i).getEventName());
				ArrayList<Double>data =contList.get(i).getDataList();
				for(int j = 0; j < data.size();j++ ) {
					outFile.print("|" + data.get(j));
				}
				outFile.println();
			}
			outFile.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Analysis
	public static void Analysis(int Days) {
		double meanC = 0;
		double meanD = 0;
		double meanAnalysis = 0;
		double meanStd = 0;
		
		//Mean
		for(int j=0;j<discList.size();j++) {
			meanD = 0;
			ArrayList<Integer> data = discList.get(j).getDataList();
			for(int k=0;k<data.size();k++) {
				meanD += data.get(k);
			}
			meanD = meanD/Days;
			meanD = Math.round(meanD * 100.00) / 100.00;
			double stdDev = 0;
			for(int k=0;k<data.size();k++) {
				stdDev += Math.pow(data.get(k) - meanD, 2);
			}
			
			stdDev = Math.sqrt(stdDev/Days);
			
			 stdDev = Math.round(stdDev * 100.00) / 100.00;
			
			System.out.println("Event Name: " + discList.get(j).getEventName());
			System.out.println("Mean D: "+meanD);
			System.out.println("Std D: " + stdDev);
			System.out.println();
			
			discList.get(j).setMean(meanD);
			discList.get(j).setStdDev(stdDev);
		}
		
		for(int i=0;i<contList.size();i++) {
			meanC =0;
			ArrayList<Double> data = contList.get(i).getDataList();
			for(int k=0;k<data.size();k++) {
				meanC += data.get(k);
			}
			meanC = meanC/Days;
			meanC = Math.round(meanC * 100.00) / 100.00;
			
			double stdDev = 0;
			for(int k=0;k<data.size();k++) {
				stdDev += Math.pow(data.get(k) - meanC, 2);
			}
			
			stdDev = Math.sqrt(stdDev/Days);
			
			 stdDev = Math.round(stdDev * 100.00) / 100.00;
			
			System.out.println("Event Name: " + contList.get(i).getEventName());			
			System.out.println("Mean C: "+meanC);
			System.out.println("Std C: " + stdDev);
			System.out.println();
			
			contList.get(i).setMean(meanC);
			contList.get(i).setStdDev(stdDev);
		}
		
		//print the log to textFile
		try {
			PrintWriter outFile = new PrintWriter(new File("analysis.txt"));
			
			for(int i = 0; i < discList.size();i++) {
				outFile.print(discList.get(i).getEventName());
				ArrayList<Integer>data =discList.get(i).getDataList();
				for(int j = 0; j < data.size();j++ ) {
					outFile.print("|" + data.get(j));
				}
				
				outFile.print( "||" + discList.get(i).getMean());
				outFile.print( "|" + discList.get(i).getStdDev());
				
				outFile.println();
			}
			
			for(int i = 0; i < contList.size();i++) {
				outFile.print(contList.get(i).getEventName());
				ArrayList<Double>data =contList.get(i).getDataList();
				for(int j = 0; j < data.size();j++ ) {
					outFile.print("|" + data.get(j));
				}
				outFile.print( "||" + contList.get(i).getMean());
				outFile.print( "|" + contList.get(i).getStdDev());
				outFile.println();
			}
			outFile.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//read in Events.txt
	public static void readEvents(String fileName) throws FileNotFoundException{
		Scanner scan = new Scanner(new File(fileName));
		String dataCount, data;
		
		dataCount = scan.nextLine();
		int count = Integer.parseInt(dataCount);
		int i = 0;
		while(i<count) {		
			data = scan.nextLine();
			String [] split = data.split("\\:");
			
			if(split[3].isEmpty()) {
				split[3] = "-1";
			}
			
			if(split[1].charAt(0) == 'C') {
				ContinousEvents eventsInfo = new ContinousEvents(split[0], split[1].charAt(0), Double.parseDouble(split[2]),
						Double.parseDouble(split[3]), Integer.parseInt(split[4]));
					cEventsList.add(eventsInfo);
					nameToTypeMap.put(split[0],split[1].charAt(0));
			}
			else if(split[1].charAt(0) == 'D') {
				DiscreteEvents eventsInfo = new DiscreteEvents(split[0], split[1].charAt(0), Integer.parseInt(split[2]),
						Integer.parseInt(split[3]), Integer.parseInt(split[4]));
					dEventsList.add(eventsInfo);
					nameToTypeMap.put(split[0], split[1].charAt(0));
			}
			i++;
		}
		scan.close();
	}
	
	//read in Stats.txt
	public static void readStats(String fileName){
		
		try {
			Scanner scan = new Scanner(new File(fileName));
			String data, dataCount;
			
			dataCount = scan.nextLine();
			int count = Integer.parseInt(dataCount);
			int i = 0;
			while(i < count) {
				data = scan.nextLine();
				String [] split = data.split("\\:");
				Stats statsInfo = new Stats(split[0], Double.parseDouble(split[1]), Double.parseDouble(split[2]));
				
				if (nameToTypeMap.containsKey(statsInfo.getName()))
					statsList.add(statsInfo);
				else {
					System.out.println("No events name found!! for " + statsInfo.getName());
				}
				
				
				i++;
			}
			scan.close();
		}catch(FileNotFoundException e) {
			System.out.println("File not found");
			System.exit(0);
		}
	}
}
