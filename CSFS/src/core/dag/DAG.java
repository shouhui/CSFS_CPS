package core.dag;
import java.util.List;
import java.util.Map;

/*	DAG��*/
public class DAG {

	/*	�����б�*/
	private List<Cloudlet>cloudletList;
	
	/*	����䴫��������Map��*/
	private Map<String,Double>cloudletDependValueMap;

	/*	���캯��*/
	public DAG(){
		
	}
	
//	/*	�ύcloudletList��DAG*/
//	public void submitCloudletList(List cl){
//		this.cloudletList = cl;
//	}
//	
//	/*	�ύ�����������ϵMap��*/
//	public void submitCloudletDependMap(Map cd){
//		this.cloudletDependMap = cd;
//	}
//	
//	/*	�ύ�����������ϵֵMap��*/
//	public void submitCloudletDependValueMap(Map cdv){
//		this.cloudletDependValueMap = cdv;
//	}
//	
	/*	�ж�cloudlet���Ƿ���������ϵ*/
	public boolean isDepend(String src,String des){
		if(cloudletDependValueMap.containsKey(src+" "+des)){
			return true;
		}
		else return false;
	}
	
	/*	��ȡcloudlet������ֵ*/
	public double getDependValue(int src,int des){
		return cloudletDependValueMap.get(String.valueOf(src)+" "+String.valueOf(des));
	}
	
	/*	����cloudletList*/
	public void setCloudletList(List<Cloudlet> cl){
		this.cloudletList = cl;
	}
	/*	���cloudletList*/
	public List<Cloudlet> getCloudletList(){
		return cloudletList;		
	}
	
	/*	����cloudlet������ֵ*/
	public void setCloudletDependValueMap(Map<String, Double> cdv){
		this.cloudletDependValueMap = cdv;
	}
	/*	���cloudlet������ֵ*/
	public Map<String, Double> getCloudletDependValueMap(){
		return cloudletDependValueMap;
	}

	/*	getCloudletById()*/
	public Cloudlet getCloudletById(int cloudletId){
		for(Cloudlet cl:cloudletList){
			if(cl.getCloudletId() == cloudletId)
				return cl;
		}
		return null;
	}
}
