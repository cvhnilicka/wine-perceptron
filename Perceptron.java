/** This is code for a generic perceptron to use as a guideline */

import java.util.*;
import java.io.*;
import java.text.*;
import java.math.*;


class Perceptron {
	static int NUM_FEATURES = 0;
	static int NUM_TRAINING = 0;
	static int NUM_TUNE = 0;
	static int NUM_TEST = 0;
	static double LEARNING_RATE = 0.1;
	static int theta = 0;
	static int[][] training;
	static int[][] tuning;
	static int[][] testing;
	static int[] outputs;
	static int[] tun_outputs;
	static int[] test_outputs;
	static String class1, class2;

	public static void main(String[] args) {
		if (args.length < 3) {
			System.err.println("Usage: java perceptron <filename>");
			System.exit(1);
		}
		readFile(args[0], 0);
		readFile(args[1], 1);
		readFile(args[2], 2);

		// System.out.println("EVERYTHING IS READ IN");

		double[] weights = new double[NUM_FEATURES + 1];   // features plus bias
		double[] lastWeights = weights;
		double localError, globalError;

		for(int i = 0; i < NUM_FEATURES + 1; i++){
			weights[i] = 0 + Math.random() * (1 - 0);
		}

		int itr, output, p, outputcheck, gradient, lastGradient;

		itr = 0;
		gradient = 0;
		lastGradient = 0;
		float tunAccuracy = 0.000f;

		do {
			itr++;
			globalError = 0;
			//  iterate through all training (1 epoch)
			for( p = 0; p < NUM_TRAINING; p++) {
				output = calcOutput(theta, weights, training, p);

				localError = outputs[p] - output;
				for(int i = 0; i < NUM_FEATURES; i++){
					weights[i] += LEARNING_RATE * localError * training[p][i];
				}

				weights[NUM_FEATURES] += LEARNING_RATE * localError;
				globalError += (localError*localError);
			}
			if (itr % 4 == 0 && itr >= (NUM_TRAINING/3)) {
				
				int correct = 0;
				for (int i = 0; i < NUM_TUNE; i++) {
					outputcheck = calcOutput(theta, weights, tuning, i);
					// System.out.println("outputcheck == tun_outputs[i]" + outputcheck + "  " + tun_outputs[i]);
					if(outputcheck == tun_outputs[i]) {
						correct++;
					}
					// System.out.println("NUM CORRECT: " + correct);
				}
				tunAccuracy = (float)((correct* 100.0f)/NUM_TUNE);
				// System.out.println("tunAccuracy: " + tunAccuracy);
				if(tunAccuracy > 85) {
					gradient++;
					if(lastGradient < gradient) {
						lastWeights = weights;
					}
					lastGradient = gradient;
				} else {
					gradient = 0;
				}
			}

		} while(lastGradient < 3);

		// System.out.println("lastGradient < 3: " + (lastGradient < 3));

		System.out.println("ITR: " + itr + "\nGRADIENT: " + lastGradient + "\ntunAccuracy: " + tunAccuracy);

		weights = lastWeights;

		float accuracy;
		int correct = 0;
		for(int i =0; i < NUM_TEST; i++) {
			output = calcOutput(theta, weights, testing, i);
			if(output == test_outputs[i]) {
				correct++;
			}

		}
		accuracy = (float)((correct * 100.0f)/NUM_TEST);

		System.out.println("TESTING accuracy: " + accuracy);



	}

	public static void readFile(String filename, int set) {
		Scanner in;

		try {
			in = new Scanner(new File(filename));
			int linecount = 0;
			Boolean skip = false;
			int entries = 0;
			while(in.hasNext()) {
				String line = in.nextLine().trim();

				if(line.length() == 0 || (line.charAt(0) == '/' && line.charAt(1) == '/')) {
					continue;
				}

				if(skip == false) {
					NUM_FEATURES = Integer.parseInt(line);
					skip = true;
				} else {
					
					String[] split = line.split(" ");
					if (linecount == 21) {
						class1 = split[0];
					}
					if (linecount == 22) {
						class2 = split[0];
					}
					if(linecount == 23) {
						if(set == 0) {
							NUM_TRAINING = Integer.parseInt(line);
							outputs = new int[NUM_TRAINING];
						} else if (set == 1) {
							NUM_TUNE = Integer.parseInt(line);
						} else if (set == 2) {
							NUM_TEST = Integer.parseInt(line);
						}
						if(set == 0) {
							training = tableInit(NUM_TRAINING);
						} else if (set == 1) {
							tuning = tableInit(NUM_TUNE);
							tun_outputs = new int[NUM_TUNE];
						} else if (set == 2) {
							testing = tableInit(NUM_TEST);
							test_outputs = new int[NUM_TEST];
						}
						
					} else if (linecount > 23) {
						// System.out.println("ENTRY: " + entries + "\nLINECOUNT: " + linecount + "SET: " + set);
						// System.out.println("LINE: " + line);
						if(split[1].equals(class1)) {
							// System.out.println("MADE IT HERE");
							for (int i = 2; i < 22; i++){
								if(split[i].equals("F")) {
									// System.out.print(" F ");
									if(set == 0){
										// System.out.println(0);
										training[entries][i-2] = 0;	
									} else if (set == 1) {
										tuning[entries][i-2] = 0;
										tun_outputs[entries] = 0;
									} else if (set == 2) {
										testing[entries][i-2] = 0;
										test_outputs[entries] = 0;
									}
								} else {
									// System.out.println("NOW HERE");
									if(set == 0){
										// System.out.println("AHHHHHHHHHHH");
										training[entries][i-2] = 1;	
									} else if (set == 1) {
										tuning[entries][i-2] = 1;
										tun_outputs[entries] = 1;
									} else if (set == 2) {
										testing[entries][i-2] = 1;
										test_outputs[entries] = 1;
									}
									// System.out.print("  T  ");
								}
							}
							// System.out.println("SHIT");
							outputs[entries] = 0;
							// System.out.println();
						} else {
							// System.out.println(7);
							for(int i = 2; i < 22; i++){
								// System.out.println("I-2: " + (i-2));
								if(split[i].equals("F")) {
									// System.out.print(" F ");
									if(set == 0){
										// System.out.println("BBBBBBBBBBBB");
										training[entries][i-2] = 0;	
									} else if (set == 1) {
										tuning[entries][i-2] = 0;
										tun_outputs[entries] = 0;
									} else if (set == 2) {
										testing[entries][i-2] = 0;
										test_outputs[entries] = 0;
									}
								} else {
									if(set == 0){
										// System.out.println(3);
										training[entries][i-2] = 1;	
									} else if (set == 1) {
										tuning[entries][i-2] = 1;
										tun_outputs[entries] = 1;
									} else if (set == 2) {
										testing[entries][i-2] = 1;
										test_outputs[entries] = 1;
									}
									// System.out.print("  T  ");
								}
							}
							// System.out.println("FUCK");
							outputs[entries] = 1;
							// System.out.println();
						} 
						entries++;
					}
					
				}
				
				linecount++;
			}	
		} catch(FileNotFoundException e) {
			System.err.println("Could not find file.");
			System.exit(1);
		}

	}

	public static int[][] tableInit(int numElements) {
		int[][] temp = new int[numElements][NUM_FEATURES];
		return temp;
	}

	private static int calcOutput(int theta, double[] weights, int[][] inputs, int entry){
		double sum = 0;

		for(int i = 0; i < NUM_FEATURES; i++){
			sum += weights[i] * inputs[entry][i];
		}
		sum += weights[NUM_FEATURES];
		return (sum>=theta) ? 1 : 0;
	}


	private static int[][] featureTable(int numinst) {
		int[][] temp = new int[numinst][];
		return temp;
	}

}