package com.util;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import core.dag.Cloudlet;
import core.dag.DAG;

public class CriticalPath {
	private double aveCcr;
	private DAG dag;
	private List<Double> aveComputeCost;
	private double est[];
	private double lst[];
	private List<Integer>cpList;

	
	public void cpm(){
		cpList = new ArrayList<>();
		computeEST();
		computeLST();

		for(int i = 0; i < dag.getCloudletList().size(); i++){
//			System.out.println(est[i] + " " + lst[i]);
			if(Math.abs(est[i] - lst[i]) < 1.0E-6  )
				cpList.add(i);
		}
	}
	
	public List<Integer> getCP(){
		return cpList;
	}
	
	private void computeLST() {
		int cloudletNum = dag.getCloudletList().size();
		lst[cloudletNum - 1] = est[cloudletNum - 1];
		
		for(int i = cloudletNum - 2; i >=0; i--){
			Cloudlet cl = dag.getCloudletById(i);
			Iterator<Integer>sucIt = cl.getSucCloudletList().iterator();
			double min = Double.MAX_VALUE;
			
			while(sucIt.hasNext()){
				int sucId = sucIt.next();
				double temp = lst[sucId] - dag.getDependValue(i, sucId)*aveCcr - aveComputeCost.get(i);
				min = (min > temp)?temp:min;
			}
			lst[i] = min;
		}
	}


	private void computeEST() {
		est[0] = 0;
		for(int i = 1; i < dag.getCloudletList().size(); i++){
			Cloudlet cl = dag.getCloudletById(i);
			Iterator<Integer> preIt = cl.getPreCloudletList().iterator();
			double max = 0;
			
			while(preIt.hasNext()){
				int preId = preIt.next();
				double temp = est[preId] + aveComputeCost.get(preId) + dag.getDependValue(preId, i)*aveCcr;
				max =  (max > temp)?max:temp;
			}
			est[i] = max;
		}
	}

	public void initialize(double aveCcr, DAG dag, List<Double> aveComputeCost){
		this.aveCcr = aveCcr;
		this.dag = dag;
		this.aveComputeCost = aveComputeCost;
		est = new double[dag.getCloudletList().size()];
		lst = new double[dag.getCloudletList().size()];
	}
	
	public static void main(String[] args) {

	}
}
