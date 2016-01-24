package core.scheduler;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.util.CPMHEFT;
import com.util.CriticalPath;

import core.algorithm.GTI;
import core.algorithm.HEFT;
import core.algorithm.PSO;
import core.dag.Cloudlet;
import core.dag.DAG;
import core.vm.Vm;
import core.vm.VmComputeCost;

/**
 * 
 * @author shouhui
 *
 */
public class Broker {
	//	Print the debug or not
	boolean debug = false;
	private DAG dag;
	private List<Vm>vmList;
	private VmComputeCost vcc;
	private double[][]ccr;
	private double B = 0.5;
	
	/*	The temporary actual start time and actual finish time of all the cloudlets*/
	private List<Double[]>exeTimeAfterHEFT = new ArrayList<>();
	
	public Broker() {}
	
	/**
	 * Start the algorithms.
	 */
	public void start() {
		//	cmp
		List<Double>aveComputeCost = new ArrayList<Double>();
		for(int i = 0; i < vcc.getAveVmComputeCostMap().size(); i++){
			aveComputeCost.add(vcc.getVmAveComputeCost(i));
		}
		CriticalPath cmp = new CriticalPath();
		cmp.initialize(getAveCcr(), dag, aveComputeCost);
		cmp.cpm();
		for(int i:cmp.getCP()){
			System.out.println(i);
		}
		
		//	Executing HEFT
		double deadline = startHEFTWithMaxF();
		backupExeTime();
		System.out.println("The deadline:" + deadline);
		
//		for(Cloudlet cl:dag.getCloudletList()){
//			System.out.println("getscaledtime test:" +  cl.getCloudletId() + " " + vcc.getScaledTime(cl, 1, vmList.get(cl.getVmId())));
//			System.out.println(cl.getBeta() + " " + vcc.getVmComputeCostMap().get(cl.getCloudletId())[cl.getVmId()]);
//		}
		
		for (Cloudlet cl : dag.getCloudletList()) {
			int clId = cl.getCloudletId();
			System.out.println("cloudletId:" + (clId + 1) + "    vmId:"
					+ (cl.getVmId() + 1) + "    ast:" + cl.getAst()
					+ "    aft:" + cl.getAft() );
		}
		
		//	CMP after the HEFT scheduling
		CPMHEFT cmpHEFT = new CPMHEFT();
		cmpHEFT.initialize(ccr, dag, vcc, vmList);
		cmpHEFT.cpm();
		for(int i:cmpHEFT.getCP()){
			System.out.println(i);
		}


		
		GTI gti = new GTI(deadline, dag, vmList, vcc, ccr);
		gti.start();
		double basePrice = gti.getBaseE();
		double csfsPrice = gti.getPriceAfterG();
		double gtiPrice = gti.getCurE();
		
		System.out.println("basePrice: "+ basePrice + " after group: " + gti.getPriceAfterG() + " gtiPrice: " + gtiPrice);
		
//		restoreExeTimeAndLevel();
		
		PSO pso = new PSO(deadline, dag, vmList, vcc, ccr);
		pso.start();
		double psoPrice = pso.getPsoPrice();
		
		writeFile(basePrice, csfsPrice, gtiPrice, psoPrice);
		
		
	}
	
	public void writeFile(double a,double b,double c, double d){
		try{
			String fileName = "dataout/result.txt";
//			BufferedWriter output = new BufferedWriter(new FileWriter(fileName));
			BufferedWriter output = new BufferedWriter(new FileWriter(fileName,true));
		
			output.write(a + " " + b + " " + c + " " + d);
			output.write('\n');
			
			output.flush();
			output.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private void restoreExeTimeAndLevel() {
		for(int i = 0; i < dag.getCloudletList().size(); i++){
			Cloudlet cl = dag.getCloudletById(i);
			cl.setAst(exeTimeAfterHEFT.get(i)[0]);
			cl.setAst(exeTimeAfterHEFT.get(i)[1]);
			cl.setLevel(0);
		}
		
	}

	private void backupExeTime() {
		for(int i = 0; i < dag.getCloudletList().size(); i++){
			Cloudlet cl = dag.getCloudletById(i);
			exeTimeAfterHEFT.add(new Double[]{cl.getAst(), cl.getAft()});
		}
	}

	/**
	 * @return	deadline.
	 */
	private double startHEFTWithMaxF() {
		HEFT heft = new HEFT(0, dag, vmList, vcc, ccr);
		List<Double[]> computeCostSend = new ArrayList<Double[]>();
		
		//	Set the temporary cost of all the cloudlets for vms*/
		for(int i = 0;i < dag.getCloudletList().size();i++){
			Double[]costTemp = new Double[vmList.size()];
			for(Vm vm:vmList){
				int vmId = vm.getVmId();
				costTemp[vmId] = vcc.getVmComputeCost(i, vmId);
			}
			computeCostSend.add(i,costTemp);
		}
		heft.initialize(computeCostSend);
		heft.start();
		//	Set actual start time and actual finish time
		for(Cloudlet cl:dag.getCloudletList()){
			int cloudletId = cl.getCloudletId();
			cl.setAst(heft.getExeTimeTemp(cloudletId)[0]);
			cl.setAft(heft.getExeTimeTemp(cloudletId)[1]);
		}
		
		List<Double> computeCostTemp = new ArrayList<>();
		//	Set the temporary cost of all the cloudlets executing in minimize frequency*/
		for(int i = 0;i < dag.getCloudletList().size();i++){
			Vm vm = vmList.get(dag.getCloudletById(i).getVmId());
			computeCostTemp.add(vcc.getScaledTime(dag.getCloudletById(i), vm.getMaxfLevel(), vm));
		}
		double makespanMinF = heft.reAssignCloudlet(dag.getCloudletList(), computeCostTemp );
		double makespanMaxF = dag.getCloudletList().get(dag.getCloudletList().size() - 1).getAft();
		return makespanMaxF + B*( makespanMinF - makespanMaxF ) ;
	}

	public double getAveCcr(){
		double sumCcr = 0;
		for(int i = 0;i < vmList.size();i++){
			for(int j = i+1;j < vmList.size();j++){
				sumCcr += ccr[i][j];
			}
		}
		return sumCcr/( vmList.size()*(vmList.size() - 1)/2 );
	}
	
	public DAG getDag() {
		return dag;
	}
	public void setDag(DAG dag) {
		this.dag = dag;
	}
	public List<Vm> getVmList() {
		return vmList;
	}
	public void setVmList(List<Vm> vmList) {
		this.vmList = vmList;
	}
	public VmComputeCost getVcc() {
		return vcc;
	}
	public void setVcc(VmComputeCost vcc) {
		this.vcc = vcc;
	}
	public double[][] getCcr() {
		return ccr;
	}
	public void setCcr(double[][] ccr) {
		this.ccr = ccr;
	}
	
	

}
