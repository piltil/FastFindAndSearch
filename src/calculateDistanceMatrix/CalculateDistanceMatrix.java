package calculateDistanceMatrix;
import java.lang.*;
import java.io.*;
import java.util.*;

import mdsj.MDSJ;

public class CalculateDistanceMatrix {
	ArrayList<ArrayList<Double>> arr;
	File file;

	public CalculateDistanceMatrix(String dir) {
		file = new File(dir);
		arr = new ArrayList<ArrayList<Double>>();
	}

	public double[][] distanceMatrix() {
		try {
			FileInputStream fis = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String current;
			while ((current = br.readLine()) != null) {
				if (current.length() == 0)
					continue;
				String[] split = current.trim().split("\\,");
				//String[] split = current.trim().split("\\s++");
				ArrayList<Double> temp = new ArrayList<Double>();
				temp.add(Double.parseDouble(split[0]));
				temp.add(Double.parseDouble(split[1]));	
				temp.add(Double.parseDouble(split[2]));
				temp.add(Double.parseDouble(split[3]));
				arr.add(temp);				
			}

			br.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		double[][] distanceMatrix = new double[arr.size()][arr.size()];

		for (int i = 0; i < arr.size(); i++) {
			for (int j = 0; j < arr.size(); j++) {
				double x = arr.get(i).get(0) - arr.get(j).get(0);				
				double y = arr.get(i).get(1) - arr.get(j).get(1);
				double z = arr.get(i).get(2) - arr.get(j).get(2);
				double k = arr.get(i).get(3) - arr.get(j).get(3);
				double distance = Math.sqrt(x * x + y * y + z * z + k * k);
				//double distance = Math.sqrt(x * x + y * y);
				distanceMatrix[i][j] = distance;
			}			
		}

		return distanceMatrix;
	}

	public static void main(String[] args) {
		CalculateDistanceMatrix iris = new CalculateDistanceMatrix("d:/cs298/aggregation_3a.txt");
		double[][] tempMatrix;
		tempMatrix = iris.distanceMatrix();
		double[][] positionMatrix=MDSJ.classicalScaling(tempMatrix); // apply MDS
		for(int i=0; i<positionMatrix[0].length; i++) {  // output all coordinates
			System.out.println("***********************");
			    System.out.println(positionMatrix[0][i]+" "+positionMatrix[1][i]);		
			} 
	}

}
