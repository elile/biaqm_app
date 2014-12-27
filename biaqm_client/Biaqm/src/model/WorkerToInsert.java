package model;

public class WorkerToInsert
{
	private long id ;
	private String totalTime ;
	
	private WorkerAdvancedResult workerAdvancedResult;


	public WorkerToInsert(long id, String totalTime)
	{
		super();
		this.id = id;
		this.totalTime = totalTime;
		workerAdvancedResult = null;
	}



	public WorkerAdvancedResult getWorkerAdvance() {
		return workerAdvancedResult;
	}



	public void setWorkerAdvance(WorkerAdvancedResult workerAdvance) {
		this.workerAdvancedResult = workerAdvance;
	}



	@Override
	public String toString() {
		return "WorkerToInsert [id=" + id + ", totalTime=" + totalTime + "]";
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
