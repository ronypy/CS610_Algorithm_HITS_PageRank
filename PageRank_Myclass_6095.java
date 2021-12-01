//Md Rakibul Hasan pp2_6095

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;


public class PageRank_Myclass_6095 {

	static Integer iterations = -3; 
    static Integer initialValue = -1;

	static int n = 0;
	static int m = 0;
	static ArrayList<ArrayList<Integer>> adjacencyList;
	
	public static void main(String args[]) throws IOException {
		readInput(args);
		//printInput();
		pageRank();
	}

    static void readInput(String sysArgs[]) {
		iterations = Integer.parseInt(sysArgs[0]);
		initialValue = Integer.parseInt(sysArgs[1]);
		String fileName = sysArgs[2];
		try {
			parseInputFile(fileName);
		}
		catch(FileNotFoundException e) {
			System.out.print("FileNotFoundException occurred");
		}
	}

	static void parseInputFile(String fileName) throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(fileName));
		n = scanner.nextInt();
		m = scanner.nextInt();

		adjacencyList = new ArrayList<>();

		for(int i=0;i<n;i++) {
			adjacencyList.add(new ArrayList<Integer>());
		}

		while(scanner.hasNextInt()) {
			int u = scanner.nextInt();
			int v = scanner.nextInt();
			adjacencyList.get(u).add(v);
		}

		scanner.close();

	}

	static void printInput() {
		System.out.println("n: " + n + " m: " + m);
		
		for(int i=0;i<n;i++) {
			System.out.print("Index " + i + ":");
			for(int j=0;j<adjacencyList.get(i).size();j++) {
				System.out.print(" " + adjacencyList.get(i).get(j));
			}
			System.out.println();
		}
	}

	static void printOutput(int iterationIndex, Boolean isConverged, ArrayList<Double> ranks) {

		DecimalFormat format = new DecimalFormat("0.0000000");
		
		if(n > 10) {
			if(isConverged) {
				System.out.println("Iter : " + iterationIndex);
				for(int i = 0; i < n; i++) {
                	System.out.println("P[" + i + "]=" + format.format(ranks.get(i)));
            	}
            }
		}
		else {
			if(iterationIndex == 0) {
				System.out.print("Base : " + iterationIndex + " :");
			}
			else {
				System.out.print("Iter : " + iterationIndex + " :");
			}

			for(int i = 0; i < n; i++) {
                System.out.print("P[" + i + "]=" + format.format(ranks.get(i)) + " ");
            }

            System.out.println();
		}
	}

	static double getErrorRate(ArrayList<Double> prev , ArrayList<Double> current) {
        double errorMax = -1.0;
        for (int i = 0; i < n; i++) { 
            double error = Math.abs(current.get(i) - prev.get(i));
            if(error > errorMax) {
            	errorMax = error;
            }
        }
        return errorMax;
    }

	static void pageRank() {

		double d = 0.85;

		double initValue = initialValue;

		if(n > 10 || initialValue == -1) {
			initValue = 1.0 / n;
		}
		else if(initialValue == -2) {
			initValue = 1.0 / Math.sqrt(n);
		}

		double errorMaxLimit = -1;
		if(n > 10 || iterations == 0) {
			errorMaxLimit = Math.pow(10, -5);
		}
		else if(iterations < 0) {
			errorMaxLimit = Math.pow(10, iterations);
		}

		int iterationMaxLimit = iterations;
		if(n > 10 || iterations <= 0) {
			iterationMaxLimit = Integer.MAX_VALUE;
		}

		ArrayList<Double> prevRanks = new ArrayList<>();

		for(int i = 0; i < n; i++) {
			prevRanks.add(initValue);
		}

		double errorRate = Double.MAX_VALUE;
		int iterationCount = 0;

		Boolean isConverged = iterationCount >= iterationMaxLimit || errorRate <= errorMaxLimit;
		printOutput(iterationCount, isConverged, prevRanks);

		double baseValue = (1-d)/n;
		while(!isConverged) {
			
			iterationCount++;
			
			ArrayList<Double> currentRanks = new ArrayList<>();
			for(int i = 0; i < n; i++) {
				currentRanks.add(0.0);
			}

			for(int i = 0; i < adjacencyList.size(); i++) {
				int sz = adjacencyList.get(i).size();
				for(int j = 0; j < sz; j++) {
					int k = adjacencyList.get(i).get(j);
					currentRanks.set(k, (currentRanks.get(k) + prevRanks.get(i)/sz));
				}
			}

			for(int i = 0; i < n; i++) {
				currentRanks.set(i, (baseValue + d * currentRanks.get(i)));
			}

			errorRate = getErrorRate(prevRanks, currentRanks);

			isConverged = iterationCount >= iterationMaxLimit || errorRate <= errorMaxLimit;
			printOutput(iterationCount, isConverged, currentRanks);

			
			prevRanks.clear();
			prevRanks.addAll(currentRanks);

		}

	}

}