package com.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import core.dag.Cloudlet;
import core.dag.DAG;
import core.vm.Vm;
import core.vm.VmComputeCost;

public class CPMHEFT {
	private double[][] ccr;
	private DAG dag;
	private VmComputeCost vcc;
	private double est[];
	private double lst[];
	private List<Integer>cpList;
	private double[]availableTime;
	private List<Vm> vmList;

	
	public void cpm(){
		
		computeEST();
		computeLST();

		for(int i = 0; i < dag.getCloudletList().size(); i++){
			int cloudletId = dag.getCloudletList().get(i).getCloudletId();
			System.out.println( (cloudletId + 1) + " " + est[cloudletId] + " " + lst[cloudletId]);
			if(Math.abs(est[cloudletId] - lst[cloudletId]) < 1.0E-6  )
				cpList.add(cloudletId);
		}
	}
	
	public List<Integer> getCP(){
		return cpList;
	}
	
	private void computeLST() {
//		System.out.println("computelst------------->");
		int cloudletNum = dag.getCloudletList().size();
		lst[cloudletNum - 1] = est[cloudletNum - 1];
		availableTime[dag.getCloudletList().get(cloudletNum - 1).getVmId()] = est[cloudletNum - 1];
		
		for(int i = cloudletNum - 2; i >=0; i--){
			Cloudlet cl = dag.getCloudletList().get(i);
			int cloudletId = cl.getCloudletId();
//			System.out.println("compute the cloudlet: " + (cloudletId+1));
			
			Iterator<Integer>sucIt = cl.getSucCloudletList().iterator();
			double min = Double.MAX_VALUE;
			
			while(sucIt.hasNext()){
				int sucId = sucIt.next();
				Cloudlet sucCl = dag.getCloudletById(sucId);
				double dependValueTemp = dag.getDependValue(cloudletId, sucId)*ccr[cl.getVmId()][sucCl.getVmId()];
				if( cl.getVmId() == sucCl.getVmId() ){
					dependValueTemp = 0;
				}
				
				double temp = lst[sucId] - dependValueTemp;
				min = (min > temp)?temp:min;
				min = (min > availableTime[cl.getVmId()])?availableTime[cl.getVmId()]:min;
//				System.out.println(min);
			}
			min -= vcc.getVmComputeCost(cloudletId, cl.getVmId());
			
			lst[cloudletId] = min;
			availableTime[cl.getVmId()] = lst[cloudletId];
		}
	}


	private void computeEST() {
		for(int i = 0; i < dag.getCloudletList().size(); i++){
			est[i] = dag.getCloudletById(i).getAst();
		}
	}

	public void initialize(double[][] ccr, DAG dag, VmComputeCost vcc, List<Vm> vmList){
		this.ccr = ccr;
		this.dag = dag;
		this.vcc = vcc;
		this.vmList = vmList;
		est = new double[dag.getCloudletList().size()];
		lst = new double[dag.getCloudletList().size()];
		cpList = new ArrayList<>();
		availableTime = new double[vmList.size()];
		
		for(int i = 0; i < vmList.size(); i++){
			availableTime[i] = Double.MAX_VALUE;
		}
	}
	
	public static void main(String[] args) {

	}
}
