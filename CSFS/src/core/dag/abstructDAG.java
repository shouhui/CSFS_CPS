package core.dag;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public abstract class abstructDAG {
	/*
	 * The default constructor for DAGMontage
	 */
	private double aveCommunicateCost = 0;
	
	public abstract void generateRandomFile();
//	public abstract void writeFile(boolean[][]result);
	
	public double getAveCommunicateCost() {
		return aveCommunicateCost;
	}
	public void setAveCommunicateCost(double aveCommunicateCost) {
		this.aveCommunicateCost = aveCommunicateCost;
	}
	
	public void writeFile(boolean[][]result){
		try{
			String fileName = "dataout/DAGAll.txt";
			BufferedWriter output = new BufferedWriter(new FileWriter(fileName));
			double ave = 0;
			for(int i = 0;i <= result.length - 1;i++){
				for(int j = i + 1;j <= result.length - 1;j++){
					if(result[i][j]){
						int randomV = (int)(Math.random()*90)+10;
						ave += randomV;
						output.write(i + " " + j + " " + randomV);
						output.write('\n');
					}
				}
			}
			ave /= result.length;
			setAveCommunicateCost(ave);
			output.flush();
			output.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
