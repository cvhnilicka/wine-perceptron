/** This is code for a generic perceptron to use as a guideline */

import java.util.*;
import java.io.*;
import java.text.*;
import java.math.*;


class Perceptron {
	static int NUM_FEATURES = 0;
	static int NUM_INSTANCES = 0;
	static double LEARNING_RATE = 0.1;
	static int theta = 0;
	static int[][] features;
	static int[][] testing;
	static int[] outputs;
	static String class1, class2;

	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println("Usage: java perceptron <filename>");
			System.exit(1);
		}
		readFile(args[0]);

		// System.out.println("HERE LAST");

		double[] weights = new double[NUM_FEATURES + 1];   // features plus bias
		double localError, globalError;

		for(int i = 0; i < NUM_FEATURES + 1; i++){
			weights[i] = randomNumberFormatted(0,1);
		}

		int itr, output, p;

		itr = 0;

		do {
			itr++;
			globalError = 0;
			for( p = 0; p < NUM_INSTANCES; p++) {
				output = calcOutput(theta, weights, features, p);

				localError = outputs[p] - output;

				for(int i = 0; i < NUM_FEATURES; i++){
					weights[i] += LEARNING_RATE * localError * features[i][p];
				}
				weights[NUM_FEATURES] += LEARNING_RATE * localError;

				globalError += (localError*localError);
			}
		} while(globalError != 0);

		readFile(args[1]);
		itr = 0;
		do {
			itr++;
			globalError = 0;
			for( p = 0; p < NUM_INSTANCES; p++) {
				output = calcOutput(theta, weights, features, p);

				localError = outputs[p] - output;

				globalError += (localError*localError);
			}
		} while(globalError != 0 && itr<=1000);

		System.out.println("RMSE = " + globalError/NUM_INSTANCES);



	}
	
	public static void readFile(String filename) {
		Scanner in;

		try {
			in = new Scanner(new File(filename));
			int linecount = 0;
			Boolean skip = false;
			int i;
			int entries = 0;
			while(in.hasNext()){
				String line = in.nextLine().trim();


				// skip the blank lines
				if(line.length() == 0 || (line.charAt(0) == '/' && line.charAt(1) == '/')) {
					continue;
				}

				if(skip == false) {
					NUM_FEATURES = Integer.parseInt(line);
					System.out.println("Number of features: " + NUM_FEATURES);
					features = featureTable(NUM_FEATURES);
					skip = true;
				} else {
					String[] split = line.split(" ");
					if (linecount == 21) {
						class1 = split[0];
					}
					if (linecount == 22){
						class2 = split[0];
					}
					if(linecount == 23){
						System.out.println(split[0]);
						NUM_INSTANCES = Integer.parseInt(split[0]);
						for(int k = 0; k < NUM_FEATURES; k++){
							features[k] = new int[NUM_INSTANCES];
						}
						outputs = new int[NUM_INSTANCES];
					} else if (linecount > 23) {
						// System.out.println("split length: " + split.length);
						if (split[1].equals(class1)) {
							System.out.println(class1);
							for(i = 2; i < 21; i++){
								if(split[i].equals("F")) {
									System.out.print(" F ");
									features[i-2][entries] = 0;
									
								} else {
									features[i-2][entries] = 1;
									System.out.print("  T  ");
								}
							}
							outputs[entries] = 0;
							System.out.println();
						} else {
							System.out.println(class2);
							for(i = 2; i < 21; i++){
								if(split[i].equals("F")) {
									System.out.print(" F ");
									features[i-2][entries] = 0;
									
								} else {
									features[i-2][entries] = 1;
									System.out.print("  T  ");
								}
							}
							outputs[entries] = 1;
							System.out.println();
						}

					}
					

				}




				linecount++;
				
				


			}

		}
		catch(FileNotFoundException e) {
			System.err.println("Could not find file.");
			System.exit(1);
		}


	}

	private static int calcOutput(int theta, double[] weights, int[][] inputs, int entry){
		double sum = 0;

		for(int i = 0; i < NUM_FEATURES; i++){
			sum += weights[i] * inputs[i][entry];
		}
		sum += weights[NUM_FEATURES];
		return (sum>=theta) ? 1 : 0;
	}

	private static double randomNumberFormatted(int min, int max) {
		DecimalFormat df = new DecimalFormat("#.###");
		double decimal = min + Math.random() * (max - min);
		String formattedDecimal = df.format(decimal);
		double ret = Double.parseDouble(formattedDecimal);
		return ret;
	}

	private static int[][] featureTable(int numfeat) {
		int[][] temp = new int[numfeat][];
		return temp;
	}

}