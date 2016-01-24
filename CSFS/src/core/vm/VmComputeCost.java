package core.vm;
import java.util.Map;

import core.dag.Cloudlet;

public class VmComputeCost {

	/*	���������ڸ���vm��ִ�д���*/
	private Map<Integer,double[]>vmComputeCostMap;
	
	/*	���������ڸ���vm��ƽ��ִ�д���*/
	private Map<Integer,Double>vmAveComputeCostMap;
	
	/*	����vmComputeCostMap*/
	public void setVmComputeCostMap(Map<Integer, double[]> cch){
		this.vmComputeCostMap = cch;
	}
	
	public Map<Integer, double[]> getVmComputeCostMap(){
		return vmComputeCostMap;
	}
	
	/*	���computeCost*/
	public double getVmComputeCost(int cloudletId,int vmId){
		return vmComputeCostMap.get(cloudletId)[vmId];
	}
	
	/*	����vmAveComputeCostMap*/
	public void setAveVmComputeCostMap(Map<Integer, Double> acch){
		this.vmAveComputeCostMap = acch;
	}
	
	public Map<Integer, Double>getAveVmComputeCostMap(){
		return vmAveComputeCostMap;
	}
	
	/*	��ȡvmAveComputeCost*/
	public double getVmAveComputeCost(int cloudletId){
		return vmAveComputeCostMap.get(cloudletId);
	}
	
	public double getScaledTime(Cloudlet cl, int level, Vm vm){
		double maxTime = vmComputeCostMap.get(cl.getCloudletId())[cl.getVmId()];
		return ( cl.getBeta()*( vm.getfByLevel(0)/vm.getfByLevel(level) - 1 ) + 1 )*maxTime;
	}
	
	
}
