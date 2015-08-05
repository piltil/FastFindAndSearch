package clusterValidation;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ClusterValidation {
	// the input string array is {m1j, m2j, ..., mlj}
	// the sum is sum(m1j + m2j +...+mlj)

	double[] p;
	double[] e;
	ArrayList<int[]> clusterMatrix;
	double[] purity;
	double[] entropy;
	double overallEntropy;
	double overallPurity;
	int[] rowsum;

	public ClusterValidation(String file) {
		clusterMatrix = new ArrayList<int[]>();
		readFile(file);
		//readFile();
		p = new double[clusterMatrix.get(0).length];
		e = new double[clusterMatrix.get(0).length];
		
		purity = new double[clusterMatrix.get(0).length];
		entropy = new double[clusterMatrix.get(0).length];
		rowsum = new int[clusterMatrix.get(0).length];
	}

	public void readFile(String file) {

		try {
			FileInputStream fis = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String current;
			while ((current = br.readLine()) != null) {
				if (current.length() == 0)
					continue;
				String[] split = current.trim().split("\\s++");
				int[] temp = new int[split.length];
				for (int i = 0; i < split.length; i++) {
					temp[i] = Integer.parseInt(split[i]);
					//System.out.println(temp[i]);
				}
				//System.out.println("*******"+clusterMatrix.size());
				clusterMatrix.add(temp);
			
			}

		} catch (Exception e) {
			System.out.println(e);
		}
        //System.out.println(clusterMatrix.get(0));
	}
	
/*	
public void readFile(){
int[] temp1 = {2, 153};
int[] temp2 = {85, 0};
clusterMatrix.add(temp1);
clusterMatrix.add(temp2);
	
}
*/
	void execute() {

		
		//double maxP = 0;
		for (int i = 0; i < clusterMatrix.size(); i++) {
			int tempsum = 0;
			for (int j = 0; j < clusterMatrix.get(i).length; j++) {
				// System.out.println(args[i]);
				tempsum += clusterMatrix.get(i)[j];
			}
			rowsum[i] = tempsum;
			//System.out.println("sum:" + rowsum);

			for (int j = 0; j < clusterMatrix.get(i).length; j++) {
				p[j] = (double) (clusterMatrix.get(i)[j]) / rowsum[i];
				//System.out.println("p[j]:" + p[j]);

			}
             double maxP = 0;
			for (int j = 0; j < clusterMatrix.get(0).length; j++) {
				if(p[j] != 0)
					//p[j]*Math.log10(p[j]) / Math.log10(2) = 0;
					entropy[i] += 0 - p[j] * Math.log10(p[j]) / Math.log10(2);
				if (p[j] > maxP) {
					maxP = p[j];
				}
			}

			purity[i] = maxP;
			
		}

	}

	void overall() {
		int totalSum = 0;
		for(int i = 0; i < clusterMatrix.size(); i++){
			totalSum += rowsum[i];						
		}
		
		for(int j = 0; j < clusterMatrix.size(); j++){			
			overallEntropy = overallEntropy + (double) rowsum[j]/totalSum *entropy[j];
			overallPurity = overallPurity + ((double)rowsum[j]/totalSum)*purity[j];
		}
		

	}
	
	void print(){
		//System.out.println("overallPurity"+overallPurity);
		for(int i = 0; i < purity.length; i++){
			System.out.println("purity:"+purity[i]+","+"entropy:" +entropy[i]);
		}
		System.out.println("overallpurity"+":"+overallPurity+","+"overallentropy"+":"+overallEntropy);
	}

	public static void main(String[] args) {
		ClusterValidation cv = new ClusterValidation("d:/cs298/validation/olivetti_Kmeans.txt");
		cv.execute();
		cv.overall();
		cv.print();
		

	}

}
