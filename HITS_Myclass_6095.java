//Md Rakibul Hasan pp2_6095

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;


public class HITS_Myclass_6095 {

	static Integer iterations = -3; 
    static Integer initialValue = -1;

	static int n = 0;
	static int m = 0;
	static ArrayList<ArrayList<Integer>> adjacencyList;
	
	public static void main(String args[]) throws IOException {
		readInput(args);
		//printInput();
		hitss();
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

	static void printOutput(int iterationIndex, Boolean isConverged, ArrayList<Double> a, ArrayList<Double> h) {

		DecimalFormat format = new DecimalFormat("0.0000000");
		
		if(n > 10) {
			if(isConverged) {
				System.out.println("Iter : " + iterationIndex);
	        	for(int i = 0; i < n; i++) {
	               	System.out.println("A/H[" + i + "]=" + format.format(a.get(i)) + "/" +format.format(h.get(i)));
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
                System.out.print("A/H[" + i + "]=" + format.format(a.get(i)) + "/" +format.format(h.get(i)) + " ");
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

	static void hitss() {

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

		ArrayList<Double> h = new ArrayList<>();
		ArrayList<Double> a = new ArrayList<>();

		for(int i = 0; i < n; i++) {
			h.add(initValue);
			a.add(initValue);
		}

		double errorRate = Double.MAX_VALUE;
		int iterationCount = 0;

		Boolean isConverged = iterationCount >= iterationMaxLimit || errorRate <= errorMaxLimit;
		printOutput(iterationCount, isConverged, a, h);

		while(!isConverged) {
			
			iterationCount++;
			
			double errorA = -1.0;
			double errorH = -1.0;

			ArrayList<Double> hNew = new ArrayList<>();
			ArrayList<Double> aNew = new ArrayList<>();
			for(int i = 0; i < n; i++) {
				hNew.add(0.0);
				aNew.add(0.0);
			}

			for(int i = 0; i < adjacencyList.size(); i++) {
				int sz = adjacencyList.get(i).size();
				for(int j = 0; j < sz; j++) {
					int k = adjacencyList.get(i).get(j);
					aNew.set(k, (aNew.get(k) + h.get(i)));
				}
			}

			double aNewSum = 0.0;
			for(int i = 0; i < n; i++) {
				aNewSum += aNew.get(i) * aNew.get(i);
			}

			for(int i = 0; i < n; i++) {
				double scaledVal = aNew.get(i)/Math.sqrt(aNewSum);
				aNew.set(i, scaledVal);
			}

			errorA = getErrorRate(a, aNew);

			for(int i = 0; i < adjacencyList.size(); i++) {
				int sz = adjacencyList.get(i).size();
				for(int j = 0; j < sz; j++) {
					int k = adjacencyList.get(i).get(j);
					hNew.set(i, (hNew.get(i) + aNew.get(k)));
				}
			}

			double hNewSum = 0.0;
			for(int i = 0; i < n; i++) {
				hNewSum += hNew.get(i) * hNew.get(i);
			}

			for(int i = 0; i < n; i++) {
				double scaledVal = hNew.get(i)/Math.sqrt(hNewSum);
				hNew.set(i, scaledVal);
			}

			errorH = getErrorRate(h, hNew);

			errorRate = Math.max(errorA, errorH);

			isConverged = iterationCount >= iterationMaxLimit || errorRate <= errorMaxLimit;
			printOutput(iterationCount, isConverged, aNew, hNew);

			a.clear();
			a.addAll(aNew);

			h.clear();
			h.addAll(hNew);

		}

	}
}