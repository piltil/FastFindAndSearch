package cs298;

import javax.swing.*;
import calculateDistanceMatrix.*;
import mdsj.MDSJ;
import org.math.plot.*;
import java.util.ArrayList;
import java.io.*;

public class FFSP {
	double[][] distanceMatrix;
	int[] clusterArray;	
	int[] centers;
	double cutoffdc;
	public static double[] rhoArray;
	public static double[] deltaArray;
	//double[] sortedDeltaArray;
	int[] originalIndexOfDelta;
	double[] sortedRhoArray;
	int[] originalIndexOfRho;
	double rhomin;
	double deltamin;
	static int clusterNumbers;
	static ArrayList<ArrayList<Integer>> group;	
	ArrayList<Integer> halo;	
	int[] index;
	//this constructor for files which contains the distance between points
	/*
	 * The only input needed is a distance matrix file
	The format of this file should be: 
	Column 1: id of element i
	Column 2: id of element j
	Column 3: dist(i,j)
	 * */
	public FFSP(double[][] distanceMatrix, double cutoffdc, double rhomin, double deltamin){		
		clusterNumbers = 0;
		clusterArray = new int[distanceMatrix[0].length];	
		for(int i = 0; i < distanceMatrix[0].length; i++){
			clusterArray[i] = -2;
		}
		centers = new int[distanceMatrix[0].length];
		for(int i = 0; i < distanceMatrix[0].length; i++){
			centers[i] = -1;
		}
		this.distanceMatrix = distanceMatrix;
		this.cutoffdc = cutoffdc;
		this.rhomin = rhomin;
		this.deltamin = deltamin;
		rhoArray = new double[distanceMatrix[0].length];
		deltaArray = new double[distanceMatrix[0].length];
		group = new ArrayList<ArrayList<Integer>>();	
		halo = new ArrayList<Integer>();
		
		originalIndexOfDelta = new int[distanceMatrix[0].length];
		index = new int[distanceMatrix[0].length];
		for(int i = 0; i < index.length; i++){
			index[i] = i;
		}		
	}
	//this constructor for the file which contains the points
	public FFSP(String dir, double cutoffdc, double rhomin, double deltamin){
		CalculateDistanceMatrix cdm = new CalculateDistanceMatrix(dir);
		distanceMatrix = cdm.distanceMatrix();
		
		clusterNumbers = 0;
		clusterArray = new int[distanceMatrix[0].length];	
		for(int i = 0; i < distanceMatrix[0].length; i++){
			clusterArray[i] = -2;
		}
		centers = new int[distanceMatrix[0].length];
		for(int i = 0; i < distanceMatrix[0].length; i++){
			centers[i] = -1;
		}		
		this.cutoffdc = cutoffdc;
		this.rhomin = rhomin;
		this.deltamin = deltamin;
		rhoArray = new double[distanceMatrix[0].length];
		deltaArray = new double[distanceMatrix[0].length];
		group = new ArrayList<ArrayList<Integer>>();	
		halo = new ArrayList<Integer>();		
		originalIndexOfDelta = new int[distanceMatrix[0].length];
		index = new int[distanceMatrix[0].length];
		for(int i = 0; i < index.length; i++){
			index[i] = i;
		}		
		
	}
	//local density of point index: rho. here we use X(chi) function
	//% "Cut off" kernel
	/*
	public int localDensity(int index){
		int rho = 0; 
		for(int i = 0; i < distanceMatrix[0].length; i++){
			if(distanceMatrix[index][i] < cutoffdc && i != index) 
				rho++;
		}
		
		return rho;
	}
	*/
	// Gaussian kernel
	
	double localDensity(int index){
		double rho = 0; 
		for(int i = 0; i < distanceMatrix[0].length && i != index; i++){
			rho = rho + Math.exp(-(distanceMatrix[i][index]/cutoffdc)*(distanceMatrix[i][index]/cutoffdc));
		}
		return rho;
	}
	
	//rhoArray and deltaArray	
		void calculateRhoArrays(){
			for(int i = 0; i < distanceMatrix[0].length; i++){
				rhoArray[i] = localDensity(i);
				//System.out.println("rho:"+rhoArray[i]);
			}
		}
		
		//sort Array（ascend） and remember the original index
		
		void sortRhoArray(){
			sortedRhoArray=rhoArray;
			double temp;
			int temp2;
			for(int i = 0; i <sortedRhoArray.length-1; i++){
				for(int j = i+1; j < sortedRhoArray.length; j++){
					if(sortedRhoArray[i] > sortedRhoArray[j]){
						temp = sortedRhoArray[i];
						sortedRhoArray[i] = sortedRhoArray[j];
						sortedRhoArray[j] = temp;
						
						temp2 = index[i];
						
						index[i] = index[j];
						//System.out.println("*************");
						//System.out.print("i"+"j"+i+","+j);
						index[j] = temp2;
					}
					
				}
			}
			
		}
		
		//calculate neighbor
		int neighbor(int i){
			int neighborOfi = Integer.MAX_VALUE;
			double temp = Double.MAX_VALUE;			
			for(int j = 0; j < distanceMatrix[0].length; j++){
				if(index[j] == i){					
					for(int k = j+1; k < distanceMatrix[0].length; k++){
						if(distanceMatrix[index[k]][i] < temp){
							temp = distanceMatrix[index[k]][i];
							neighborOfi = index[k];
						}
					}
					break;
				}	
				
			}
			
			if(neighborOfi == Integer.MAX_VALUE)
				neighborOfi = i;				
			return neighborOfi;				
		}		
		
	//distance delta from higher points of higher density point
	public double distanceFromHigherDensity(int point){
		double delta;
		double tempMaxValue = Double.MIN_VALUE;
		for(int i = 0; i < distanceMatrix[0].length; i++){
			for(int j = i+1; j < distanceMatrix[0].length; j++){
				if(distanceMatrix[i][j] > tempMaxValue){
					tempMaxValue = distanceMatrix[i][j];
				}				
			}
		}
		
		delta = tempMaxValue;
		
		for(int i = 0; i < distanceMatrix[0].length; i++){			
			if(index[i] == point){
				for(int j = i+1; j < distanceMatrix[0].length; j++){
					if(distanceMatrix[point][index[j]] < delta){
						delta = distanceMatrix[point][index[j]];						
					}					
				}
				break;
			}
		}
		deltaArray[point] = delta;	    
		return delta;		
			
	}
	//calculate the deltaArray
	void calculateDeltaArrays(){
		for(int i = 0; i < distanceMatrix[0].length; i++){
			deltaArray[i] = distanceFromHigherDensity(i);
			//System.out.println("rho:"+rhoArray[i]);
		}
	}
	
	// sort delta array
	/*
	void sortDeltaArray(){		
		double temp;		
		for(int i = 0; i <sortedRhoArray.length-1; i++){
			for(int j = i+1; j < sortedRhoArray.length; j++){
				if(deltaArray[i] > deltaArray[j]){
					temp = deltaArray[i];
					deltaArray[i] = deltaArray[j];
					deltaArray[j] = temp;					
					
				}
				
			}
		}
		
	}
	
	*/
	//assign points: points with bigger delta and rho are centers
	//all datasets except olivetti face sets
	
	void cluster(){					
		
		for(int i = 0; i < distanceMatrix[0].length; i++){
			if(localDensity(i) >= rhomin && deltaArray[i] >= deltamin){
				System.out.println("the center point:"+i);
				System.out.println("rho"+localDensity(i));
				System.out.println("delta"+deltaArray[i]);				
				clusterArray[i] = clusterNumbers; // clusterNumbers: the NO. of cluster. clusterCenters[i]: point i is the center if clusterCenters[i] != -2
				centers[clusterNumbers] = i;
				clusterNumbers++;
								
				
			}			
		}
		
		for(int j = distanceMatrix[0].length-1; j >= 0; j--){
			if(clusterArray[index[j]] == -2){
				clusterArray[index[j]] = clusterArray[neighbor(index[j])];
				
			}
		}
		//initialize group
		for(int i = 0; i < clusterNumbers; i++){
			group.add(new ArrayList<Integer>());
		}
		if(clusterNumbers != 0){
			for(int k = 0; k < distanceMatrix[0].length; k++){
				for(int i = 0; i < clusterNumbers; i++){
					if(clusterArray[k] == i){
						group.get(i).add(k);					
					}			
					
				}
			}
			
		}
		
	}
	
	//just for olivetti face data sets
/*	
void cluster(){					
		
		for(int i = 0; i < distanceMatrix[0].length; i++){
			if(localDensity(i) >= rhomin && deltaArray[i] >= deltamin){
				System.out.println("the center point:"+i);
				System.out.println("rho"+localDensity(i));
				System.out.println("delta"+deltaArray[i]);				
				clusterArray[i] = clusterNumbers; // clusterNumbers: the NO. of cluster. clusterCenters[i]: point i is the center if clusterCenters[i] != -2
				centers[clusterNumbers] = i;
				clusterNumbers++;
								
				
			}			
		}
		
		for(int j = distanceMatrix[0].length-1; j >= 0; j--){
			if(clusterArray[index[j]] == -2 && distanceMatrix[index[j]][neighbor(index[j])]<cutoffdc){
				clusterArray[index[j]] = clusterArray[neighbor(index[j])];
				
			}
		}
		//initialize group
		for(int i = 0; i < clusterNumbers; i++){
			group.add(new ArrayList<Integer>());
		}
		if(clusterNumbers != 0){
			for(int k = 0; k < distanceMatrix[0].length; k++){
				for(int i = 0; i < clusterNumbers; i++){
					if(clusterArray[k] == i){
						group.get(i).add(k);					
					}
				
					
				}
			}
			
		}
		
	}
	*/
	//halo for any dataset but olivetti face data set

	void halo(){
		double[] boarderRho = new double[clusterNumbers];
		
	   System.out.println("first halo size:"+halo.size());
		for(int i = 0; i < distanceMatrix[0].length-1; i++){
			for(int j = i + 1; j < distanceMatrix[0].length; j++){
				if(clusterArray[i] != clusterArray[j] && distanceMatrix[i][j] < cutoffdc){
					if(boarderRho[clusterArray[i]] < localDensity(i)){
						//System.out.println("******cluster "+clusterArray[i]+":"+i+":"+localDensity(i));
						boarderRho[clusterArray[i]] = localDensity(i);
					}
					if(boarderRho[clusterArray[j]] < localDensity(j)){
						//System.out.println("******cluster "+clusterArray[j]+":"+j+":"+localDensity(j));
						boarderRho[clusterArray[j]] = localDensity(j);
					}
					
				}
			}
		}
		
		for(int i = 0; i <clusterNumbers; i++){
			System.out.println("cluster boardRho"+i+":"+boarderRho[i]);
		}
		
		for(int j = 0; j < group.size(); j++){
			System.out.println("group "+j);
			for(int k = 0; k < group.get(j).size();k++){
				System.out.print(localDensity(group.get(j).get(k))+"  ");
			}
			System.out.println();
			
		}
		
		
		System.out.println("first group size:"+group.size());
		for(int i = 0; i < group.size(); i++){			
			for(int j = 0; j < group.get(i).size(); j++){
				
				if(localDensity(group.get(i).get(j)) < boarderRho[i]){
					halo.add(j);
					//halo.get(i).add(j);
				}					
				
			}		
		
	   }
	}
	
	
// for olivetti face set
/*
void halo(){
	double[] boarderRho = new double[clusterNumbers];
	
   System.out.println("first halo size:"+halo.size());
	for(int i = 0; i < distanceMatrix[0].length-1; i++){
		for(int j = i + 1; j < distanceMatrix[0].length; j++){
			if(clusterArray[i] != clusterArray[j] && distanceMatrix[i][j] < cutoffdc){
				if(clusterArray[i] != -2 && boarderRho[clusterArray[i]] < localDensity(i)){
					//System.out.println("******cluster "+clusterArray[i]+":"+i+":"+localDensity(i));
					boarderRho[clusterArray[i]] = localDensity(i);
				}
				if(clusterArray[j] != -2 && boarderRho[clusterArray[j]] < localDensity(j)){
					//System.out.println("******cluster "+clusterArray[j]+":"+j+":"+localDensity(j));
					boarderRho[clusterArray[j]] = localDensity(j);
				}
				
			}
		}
	}
	
	for(int i = 0; i <clusterNumbers; i++){
		System.out.println("cluster boardRho"+i+":"+boarderRho[i]);
	}
	
	for(int j = 0; j < group.size(); j++){
		System.out.println("group "+j);
		for(int k = 0; k < group.get(j).size();k++){
			System.out.print(localDensity(group.get(j).get(k))+"  ");
		}
		System.out.println();
		
	}
	
	
	System.out.println("first group size:"+group.size());
	for(int i = 0; i < group.size(); i++){			
		for(int j = 0; j < group.get(i).size(); j++){
			
			if(localDensity(group.get(i).get(j)) < boarderRho[i]){
				halo.add(j);
				//halo.get(i).add(j);
			}					
			
		}		
	
   }
}

*/
	void print(){
		
		System.out.println("distance matrix is:");
		for(int i = 0; i < distanceMatrix.length; i++){
			for(int j = i+1; j < distanceMatrix.length; j++){
				System.out.print(distanceMatrix[i][j]+" ");
			}
			System.out.println();
		}
		
		System.out.println("index of sorted array is:");
		for(int i = 0; i < distanceMatrix[0].length; i++){
			System.out.print(index[i]);			
		}
		
		System.out.println();
		System.out.println("delta is:");
		for(int i = 0; i < distanceMatrix[0].length; i++)
			System.out.print(deltaArray[i]+",");
		
		System.out.println();
		System.out.println("rho is:");		
		for(int i = 0; i < distanceMatrix[0].length; i++)
			System.out.print(localDensity(i)+",");
		
		System.out.println();
		
		System.out.println("neighbors:");
		
		for(int i = 0; i < distanceMatrix[0].length; i++)
			System.out.print(i+":"+neighbor(i)+",");
			
		
		System.out.println();
		System.out.println("clusterNumbers:"+clusterNumbers);
		System.out.println();
		
        System.out.println("clusters:");
		
		for(int i = 0; i < distanceMatrix[0].length; i++)
			System.out.print(i+":"+clusterArray[i]+",");
		
		System.out.println();	
		
        System.out.println("centers:");
		
		for(int i = 0; i < distanceMatrix[0].length; i++){
			if(centers[i] != -1){
				System.out.print(i+":"+centers[i]+",");				
			}		
			
		}		
			
		
		System.out.println("groups are following:");
		for(int i = 0; i < group.size(); i++){
			System.out.println("the cluster "+i+":");
			for(int j = 0; j < group.get(i).size(); j++){				
				System.out.print(group.get(i).get(j)+",");				
			}
			
			System.out.println();
				
		}
	
	}
	
	//centers decision graph	
	public void plot(){	
		
		double[][] positionMatrix=MDSJ.classicalScaling(distanceMatrix); // apply MDS		
		//decision graph
		try{
			File file = new File("d:/cs298/positionMatrix_iris.txt");
			if(!file.exists()){
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			for(int i = 0; i < distanceMatrix.length; i++){
				bw.write(positionMatrix[0][i]+","+positionMatrix[1][i]);
				bw.newLine();				
			}
			bw.close();
		} catch(Exception e){
			System.out.println(e);
		}
		
		
		double[] x = new double[deltaArray.length];
	    double[] y = deltaArray;
	    for(int i = 0; i < deltaArray.length; i++){
	    	x[i] = localDensity(i);
	    }
	    Plot2DPanel plot1 = new Plot2DPanel(); 
	    plot1.addScatterPlot("example_distance", x, y);	    
	    JFrame frame = new JFrame("decision graph");
	    frame.setSize(600, 600);
	    frame.setContentPane(plot1);
	    frame.setVisible(true);
	    // clusters graph	
	    Plot2DPanel plot2 = new Plot2DPanel(); 	
	    for(int i = 0; i < clusterNumbers; i++){
	    	double[] k = new double[group.get(i).size()];
	    	double[] z = new double[group.get(i).size()];	    	
	    	for(int j = 0; j < group.get(i).size(); j++){
	    		k[j] = positionMatrix[0][group.get(i).get(j)];
	    		z[j] = positionMatrix[1][group.get(i).get(j)];	
	    		
	    	}    	
	    	
	    	plot2.addScatterPlot("clustering", k, z);
	    	
	    }
	    
	    	double[] haloK = new double[halo.size()];
	    	double[] haloZ = new double[halo.size()];
	    	for(int j = 0; j < halo.size(); j++){
	    		haloK[j] = positionMatrix[0][halo.get(j)];
	    		haloZ[j] = positionMatrix[1][halo.get(j)];		    		
	    	}
	    	System.out.println("halo:********************");
	    	System.out.println("halo size:"+halo.size());
	    	for(int i = 0; i < halo.size(); i++){
	    		System.out.println(halo.get(i));
	    	}
	    	
	    JFrame frame2 = new JFrame("clustering");
	    frame2.setSize(600, 600);
	    frame2.setContentPane(plot2);
	    frame2.setVisible(true);
	    /*
	    double[] ga = new double[100];
	    double[] n = new double[100];
	    Plot2DPanel plot3 = new Plot2DPanel();	    
	    for(int i = 0; i < 100; i++){
	    	ga[i] = localDensity(i)*distanceFromHigherDensity(i);
	    	n[i] = i;
	    }
	    plot3.addScatterPlot("ga", n, ga);
	    JFrame frame3 = new JFrame("ga");
	    frame3.setSize(600, 600);
	    frame3.setContentPane(plot3);
	    frame3.setVisible(true);
	    */
			
	    }
	
	//border region
	
	
	public static void main(String[] arg){
		
		long start = System.currentTimeMillis();
		//DistanceMatrix dm = new DistanceMatrix("d:/cs298/100olivetti.txt",100);
		//dm.convertFile();
		//double[][] temp = dm.getDistanceMatrix();
		
	//FFSP ffsp = new FFSP(dm.getDistanceMatrix(),0.07, 0.6, 0.2);
		
		FFSP ffsp = new FFSP("d:/cs298/iris.txt", 1.1, 30, 0.93);
		ffsp.calculateRhoArrays();
		
		ffsp.sortRhoArray();
		
		ffsp. calculateDeltaArrays();		
		ffsp.cluster();
		
		ffsp.halo();
		
		ffsp.print();
		
		ffsp.plot();
		
		long end = System.currentTimeMillis();
		long runningTime = end - start;
		System.out.println("running time:"+runningTime);
		
	
	}
    
}

class DistanceMatrix{
	int numberOfElements;
	static double[][] distanceMatrix;
	String dir;
	public DistanceMatrix(String dir, int numberOfElements){
		this.numberOfElements = numberOfElements;
		distanceMatrix = new double[numberOfElements][numberOfElements];
		this.dir = dir;
			}
	
	void convertFile(){
		File file = new File(dir);
		try{
			FileInputStream fis = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String current;
			while((current =br.readLine()) !=null){	
				if (current.length()==0)
					continue;
				String[] split= current.trim().split("\\s++");				
				//System.out.println(split[0]+","+split[1]+","+split[2]);
				distanceMatrix[Integer.parseInt(split[1])-1][Integer.parseInt(split[0])-1]=Double.parseDouble(split[2]);
				distanceMatrix[Integer.parseInt(split[0])-1][Integer.parseInt(split[1])-1] = Double.parseDouble(split[2]);					
							
			}
			
			br.close();
						
		} catch(Exception e){
			System.out.println(e);
			
		}
		
	}
	
	double[][] getDistanceMatrix(){
		return distanceMatrix;
	}
	
}


