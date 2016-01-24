package core.dag;


/*
 * DAGMontage is a class used for generating Montage Directed Acyclic graphs (DAGs)
 */
public class DAGMontage extends abstructDAG{
	

	public DAGMontage(){
		
	}
	/*
	 * Generate a Montage DAG, and saves the results to a file DAGMontage.txt
	 */
	public void generateRandomFile(){
		int nodeCount = 34;
		int random1 = 6;	//The number of the first layer
		int random2 = 15 - random1;	//The number of the second layer
		boolean[][]result = new boolean[nodeCount][nodeCount];
		int layerEndLine[] = { 0,random1,(random1 + random2),( random1 + random2*2 ),( random1 + random2*2 + 1 ),( random1 + random2*2 + 2),( random1*2 + random2*2 + 2),( random1*2 + random2*2 + 3) };
//		for(int a:layerEndLine){
//			System.out.println(a);
//		}
		//	The zero layer
		for(int i = layerEndLine[0] + 1;i <= layerEndLine[1];i++){
			result[0][i] = true;
		}
		//	The first layer
		for(int i = layerEndLine[0] + 1;i <= layerEndLine[1];i++){
			result[i][i + layerEndLine[1] - layerEndLine[0]] = true;
			result[i][i + layerEndLine[1] - layerEndLine[0] + 3] = true;
			result[i][i + layerEndLine[5] - layerEndLine[0]] = true;
		}
		//	The second layer
		for(int i = layerEndLine[1] + 1;i <= layerEndLine[2];i++){
			result[i][i + layerEndLine[2] - layerEndLine[1]] = true;
		}
		//	The third layer
		for(int i = layerEndLine[2] + 1;i <= layerEndLine[3];i++){
			result[i][layerEndLine[4]] = true;
		}
		//	The fourth layer
		result[layerEndLine[4]][layerEndLine[5]] = true;
		//	The fifth layer
		for(int i = layerEndLine[5] + 1;i <= layerEndLine[6];i++){
			result[layerEndLine[5]][i] = true;
		}
		//	The sixth layer
		for(int i = layerEndLine[5] + 1;i <= layerEndLine[6];i++){
			result[i][layerEndLine[7]] = true;
		}
		
		//	Save the results to the file DAGMontage.txt
		writeFile(result);
		
//		for(int i = 0;i <= nodeCount - 1;i++){
////			System.out.print(i + " ");
//			for(int j = 0;j <= nodeCount - 1;j++){
//				if(result[i][j]){
//					System.out.print("T ");
//				}else{
//					System.out.print("F ");
//				}
////				System.out.print(result[i][j] + " ");
//			}
//			System.out.println();
//		}
	}
	

	

}
