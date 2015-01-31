package model;

public class MachineToInsert 
{
	private long id ;
	private String totalTime ;
	private MachineAdvanceResult machineAdvanceResult;
	
	public MachineToInsert(long id, String totalTime) {
		super();
		this.id = id;
		this.totalTime = totalTime;
	}
	
	
	
	public MachineAdvanceResult getMachineAdvanceResult() {
		return machineAdvanceResult;
	}



	public void setMachineAdvanceResult(MachineAdvanceResult machineAdvanceResult) {
		this.machineAdvanceResult = machineAdvanceResult;
	}




	@Override
	public String toString() {
		return "MachineToInsert [id=" + id + ", totalTime=" + totalTime
				+ ", machineAdvanceResult=" + machineAdvanceResult + "]";
	}



	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}
	
	
}
