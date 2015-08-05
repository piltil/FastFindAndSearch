package Kmeans;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;  
import java.util.Random;  

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

public class Kmeans {  
    private int k;//how many clusters
    private int iteration;// iteration  
    private int dataSetLength;
    private ArrayList<double[]> dataSet;
    private ArrayList<double[]> center; 
    //this definition of cluster fits other datasets but olivetti face
    private ArrayList<ArrayList<double[]>> cluster; 
    //for olivetti face
    
    private ArrayList<Double> offsetArray;
    private Random random;  
  
  
    public void setDataSet(ArrayList<double[]> dataSet) {  
        this.dataSet = dataSet;  
    } 
  
    public ArrayList<ArrayList<double[]>> getCluster() {  
        return cluster;  
    }   
   
    public Kmeans(int k) {  
        if (k <= 0) {  
            k = 1;  
        }  
        this.k = k;  
    }    
   
    private void init() {  
        iteration = 0;  
        random = new Random();        
        dataSetLength = dataSet.size();  
        if (k > dataSetLength) {  
            k = dataSetLength;  
        }  
        center = initCenters();  
        cluster = initCluster();  
        offsetArray = new ArrayList<Double>();  
    }  
  
    private ArrayList<double[]> initCenters() {  
        ArrayList<double[]> center = new ArrayList<double[]>();  
        int[] randoms = new int[k];  
        boolean flag;  
        int temp = random.nextInt(dataSetLength);  
        randoms[0] = temp;  
        for (int i = 1; i < k; i++) {  
            flag = true;  
            while (flag) {  
                temp = random.nextInt(dataSetLength);  
                int j = 0;                  
                while (j < i) {  
                    if (temp == randoms[j]) {  
                        break;  
                    }  
                    j++;  
                }  
                if (j == i) {  
                    flag = false;  
                }  
            }  
            randoms[i] = temp;  
        }  
   
        for (int i = 0; i < k; i++) {  
            center.add(dataSet.get(randoms[i]));
        } 
        
        return center;  
    }    
    
    private ArrayList<ArrayList<double[]>> initCluster() {  
        ArrayList<ArrayList<double[]>> cluster = new ArrayList<ArrayList<double[]>>();  
        for (int i = 0; i < k; i++) {  
            cluster.add(new ArrayList<double[]>());  
        }  
  
        return cluster;  
    }  
  
    // for 4 dimensions
    /*
    private double distance(double[] point1, double[] point2) {  
        double distance = 0;  
        double x = point1[0] - point2[0];  
        double y = point1[1] - point2[1]; 
       double z = point1[2] - point2[2];
        double k = point1[3] - point2[3];
        double s = x * x + y * y + z*z + k*k;  
        distance = (double) Math.sqrt(s);    
        return distance;  
    }  
  */
    // for 2 dimensions
    
    private double distance(double[] point1, double[] point2) {  
        double distance = 0;  
        double x = point1[0] - point2[0];  
        double y = point1[1] - point2[1];        
        double s = x * x + y * y; 
        distance = (double) Math.sqrt(s);    
        return distance;  
    }  
    
    private int minDistance(double[] distance) {  
        double minDistance = distance[0];  
        int minLocation = 0;  
        for (int i = 1; i < distance.length; i++) {  
            if (distance[i] < minDistance) {  
                minDistance = distance[i];  
                minLocation = i;  
            } else if (distance[i] == minDistance) //  
            {  
                if (random.nextInt(10) < 5) {  
                    minLocation = i;  
                }  
            }  
        }  
  
        return minLocation;  
    }  
  
    private void clusterSet() {  
        double[] distance = new double[k];  
        for (int i = 0; i < dataSetLength; i++) {  
        	
            for (int j = 0; j < k; j++) {  
                distance[j] = distance(dataSet.get(i), center.get(j));  
                //System.out.println("distance to "+j+":"+distance[j]);
            }  
            
            int minLocation = minDistance(distance);             
  
            cluster.get(minLocation).add(dataSet.get(i));// 
  
        }  
    }  
  
    // for 4 dimensions
    /*
    private double errorSquare(double[] element, double[] centroid) {  
        double x = element[0] - centroid[0];  
        double y = element[1] - centroid[1]; 
        double z = element[2] - centroid[2];  
        double k = element[3] - centroid[3];
  
        double errorSquare = x * x + y * y + z*z + k*k;  
  
        return errorSquare;  
    }  
  */
    // for 2 dimensions
    
    private double error(double[] element, double[] centroid) {  
        double x = element[0] - centroid[0];  
        double y = element[1] - centroid[1];        
        double errorSquare = x * x + y * y;  
  
        return errorSquare;  
    }  
  
    private void countRule() {  
        double errorCluster = 0;  
        for (int i = 0; i < cluster.size(); i++) {  
            for (int j = 0; j < cluster.get(i).size(); j++) {  
                errorCluster += error(cluster.get(i).get(j), center.get(i));  
  
            }  
        }  
        offsetArray.add(errorCluster);  
    }  
    
  // for 4 dimensions
   /*
    private void setNewCenter() {  
        for (int i = 0; i < k; i++) {  
            int n = cluster.get(i).size();  
            if (n != 0) {  
                double[] newCenter = { 0, 0, 0, 0, 0};  
                for (int j = 0; j < n; j++) {  
                    newCenter[0] += cluster.get(i).get(j)[0];  
                    newCenter[1] += cluster.get(i).get(j)[1]; 
                    newCenter[2] += cluster.get(i).get(j)[2];  
                    newCenter[3] += cluster.get(i).get(j)[3]; 
                    newCenter[4] += cluster.get(i).get(j)[4];                 
                   }  
                 
                newCenter[0] = newCenter[0] / n;  
                newCenter[1] = newCenter[1] / n;  
                newCenter[2] = newCenter[2] / n;  
                newCenter[3] = newCenter[3] / n; 
                newCenter[4] = newCenter[4] / n;
                center.set(i, newCenter);  
            }  
        }  
    }    
   */
    // for 2 dimensions
    
    private void setNewCenter() {  
        for (int i = 0; i < k; i++) {  
            int n = cluster.get(i).size();  
            if (n != 0) {  
                double[] newCenter = { 0, 0, 0};  
                for (int j = 0; j < n; j++) {  
                    newCenter[0] += cluster.get(i).get(j)[0];  
                    newCenter[1] += cluster.get(i).get(j)[1]; 
                    newCenter[2] += cluster.get(i).get(j)[2];
                    
                }                 
                newCenter[0] = newCenter[0] / n;  
                newCenter[1] = newCenter[1] / n;  
                newCenter[2] = newCenter[2] / n;
                center.set(i, newCenter);  
            }  
        }  
    } 
    
    // for 4 dimensions
    /*
    public void printDataArray(ArrayList<double[]> dataArray,  
            String dataArrayName) {  
        for (int i = 0; i < dataArray.size(); i++) {  
            //System.out.println("print:" + dataArrayName + "[" + i + "]={"  
                    //+ dataArray.get(i)[0] + "," + dataArray.get(i)[1]+"," + dataArray.get(i)[2] + "," + dataArray.get(i)[3]+ "}");  
        	System.out.print((int)dataArray.get(i)[4]+", ");
        }  
        System.out.println("===================================");  
    }  
  */
    // for 2 dimensions
    
    public void printDataArray(ArrayList<double[]> dataArray,  
            String dataArrayName) {  
    	System.out.println();
        for (int i = 0; i < dataArray.size(); i++) { 
            
        	System.out.print((int)dataArray.get(i)[2]+", ");
        }  
        
    }  
    
    private void kmeans() {  
        init();  
        
        
        while (true) {  
            clusterSet();            
  
            countRule();    
            
            if (iteration != 0) {  
                if (offsetArray.get(iteration) - offsetArray.get(iteration - 1) == 0) {  
                    break;  
                }  
            }
            System.out.println("iteration:"+iteration);
            for(int i = 0; i < k; i++){
            	System.out.println("center:"+center.get(i)[0]+","+center.get(i)[1] + ","+ center.get(i)[2]);
            }
            /*
            for(int j = 0; j < cluster.size(); j++){
            	System.out.println("cluster:"+j);
            	for(int i = 0; i < cluster.get(j).size(); i++){
            		System.out.print(cluster.get(j).get(i)[0]+","+cluster.get(j).get(i)[1]+ "," + cluster.get(j).get(i)[2] + ";");
            	}
            	System.out.println();
            }
            */
            setNewCenter();              
            iteration++; 
            
            cluster.clear();  
            cluster = initCluster();  
        }   
       
    } 
    
    public void plot(){
    	ArrayList<ArrayList<double[]>> cluster=getCluster();  
        
        for(int i=0;i<cluster.size();i++)  
        {  
            printDataArray(cluster.get(i), "cluster["+i+"]");  
        }  
        Plot2DPanel plot = new Plot2DPanel();
        double[] z;
		double[] y;
		for(int i = 0; i < cluster.size(); i++){
			z = new double[cluster.get(i).size()];
			y = new double[cluster.get(i).size()];
			for(int j = 0; j < cluster.get(i).size(); j++){
				z[j] = cluster.get(i).get(j)[0];
				y[j] = cluster.get(i).get(j)[1];
			}
			plot.addScatterPlot("clustering", z, y);
		}    

	    JFrame frame = new JFrame("a plot panel");
	    frame.setSize(600, 600);
	    frame.setContentPane(plot);
	    frame.setVisible(true);
          
    }
  
    
    public void execute() {  
        double startTime = System.currentTimeMillis();  
        System.out.println("Kmeans starts");  
        kmeans();  
        double endTime = System.currentTimeMillis();  
        System.out.println("kmeans running time=" + (endTime - startTime)  
                + "ms");  
        System.out.println("Kmeans ends");  
        
    } 
    /*
    public static void main(String[] args){
    	  
            Kmeans kmeans=new Kmeans(3);  
            ArrayList<double[]> dataSet=new ArrayList<double[]>();
            ReadMatrix rm = new ReadMatrix("d:\\cs298\\positionMatrix_iris.txt");
            dataSet = rm.getDataSet();
            
           //rm.distanceMatrix();
           // System.out.println("distance matrix:");
          // rm.printDistanceMatrix();        
           
            kmeans.setDataSet(dataSet);  
            kmeans.execute();
            kmeans.plot();
            
    }
*/
    
}  

class ReadMatrix{
	 ArrayList<double[]> dataSet=new ArrayList<double[]>(); 
	 public ReadMatrix(String dir){
		 convert(dir);
	 }
	 void convert(String dir){
		 try{
			 File file = new File(dir);
			 FileInputStream fis = new FileInputStream(file);
			 DataInputStream dis = new DataInputStream(fis);
			 InputStreamReader isr = new InputStreamReader(dis);
			 BufferedReader br = new BufferedReader(isr);
			 String current;
			 int count = 0;
			 while((current = br.readLine()) != null){
				 if(current.length() == 0)
					 continue;
				 //if the data is seperated by ",", use the below split way
				String[] split = current.trim().split("\\,");
				 //if the data is separated by space, ust the below split way
				 //String[] split = current.trim().split("\\s++");
				 //for 4 dimensions				
				 //dataSet.add(new double[]{Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), (double)count});				 
				dataSet.add(new double[]{Double.parseDouble(split[0]), Double.parseDouble(split[1]), (double)count});	
				 count++;
			 }
		 } catch(Exception e){
			 System.out.println(e);			 
		 }
		 
	 }
	 
	 public ArrayList<double[]> getDataSet(){
		 return dataSet;
	 }
	 //for 4 dimensions
	 /*
	 double[][] distanceMatrix(){
		 double[][] distanceMatrix = new double[dataSet.size()][dataSet.size()];
		 for(int i = 0; i < dataSet.size(); i++){
			 for(int j = 0; j < dataSet.size(); j++){
				 double x = dataSet.get(i)[0] - dataSet.get(j)[0];
				 double y = dataSet.get(i)[1] - dataSet.get(j)[1];
				 double z = dataSet.get(i)[2] - dataSet.get(j)[2];
				 double k = dataSet.get(i)[3] - dataSet.get(j)[3];
				// System.out.println(k);
				 distanceMatrix[i][j] = x*x + y*y + z*z + k*k;
				 distanceMatrix[j][i] = x*x + y*y + z*z + k*k;
			 }
		 }
		 
		 return distanceMatrix;
	 }
	 */
	 
	 // for 2 dimensions
	 
	 double[][] distanceMatrix(){
		 double[][] distanceMatrix = new double[dataSet.size()][dataSet.size()];
		 for(int i = 0; i < dataSet.size(); i++){
			 for(int j = 0; j < dataSet.size(); j++){
				 double x = dataSet.get(i)[0] - dataSet.get(j)[0];
				 double y = dataSet.get(i)[1] - dataSet.get(j)[1];
				 
				 distanceMatrix[i][j] = x*x + y*y;
				 distanceMatrix[j][i] = x*x + y*y;
			 }
		 }
		 
		 return distanceMatrix;
	 }
	 
	 void printDistanceMatrix(){
		 for(int i = 0; i < distanceMatrix().length; i++){
			 for(int j = 0; j < distanceMatrix().length; j++){
				 System.out.print(distanceMatrix()[i][j]+" ");
			 }
			 System.out.println();
		 }
	 }
	 
}