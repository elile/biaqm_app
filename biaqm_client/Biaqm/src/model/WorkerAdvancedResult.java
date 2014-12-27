package model;

import java.io.Serializable;

public class WorkerAdvancedResult implements Serializable 
{
	private Worker w;
	private int workerQuantity;
	private double totalTime;// need to split to extra hour...
	private Yield_Descriptions yd;
	private int numOfYd;
	private double costOfYd;
	private String remarks;
	
	private String StartWorkingHour;
	private String EndWorkingHour;
	
	public WorkerAdvancedResult(Worker w, int workerQuantity, double totalTime,
			Yield_Descriptions yd, int numOfYd, double costOfYd, String remarks) {
		super();
		this.w = w;
		this.workerQuantity = workerQuantity;
		this.totalTime = totalTime;
		this.yd = yd;
		this.numOfYd = numOfYd;
		this.costOfYd = costOfYd;
		this.remarks = remarks;
		StartWorkingHour = "0:0";
		EndWorkingHour= "0:0";
	}
	
	
	public String getStartWorkingHour() {
		return StartWorkingHour;
	}


	public void setStartWorkingHour(String startWorkingHour) {
		StartWorkingHour = startWorkingHour;
	}


	public String getEndWorkingHour() {
		return EndWorkingHour;
	}


	public void setEndWorkingHour(String endWorkingHour) {
		EndWorkingHour = endWorkingHour;
	}


	public Worker getW() {
		return w;
	}
	
	public void setW(Worker w) {
		this.w = w;
	}
	
	public int getWorkerQuantity() {
		return workerQuantity;
	}
	
	public void setWorkerQuantity(int workerQuantity) {
		this.workerQuantity = workerQuantity;
	}
	
	public double getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(double totalTime) {
		this.totalTime = totalTime;
	}
	public Yield_Descriptions getYd() {
		return yd;
	}
	public void setYd(Yield_Descriptions yd) {
		this.yd = yd;
	}
	public int getNumOfYd() {
		return numOfYd;
	}
	public void setNumOfYd(int numOfYd) {
		this.numOfYd = numOfYd;
	}
	public double getCostOfYd() {
		return costOfYd;
	}
	public void setCostOfYd(double costOfYd) {
		this.costOfYd = costOfYd;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	@Override
	public String toString() {
		return "WorkerAdvancedResult [w=" + w + ", workerQuantity="
				+ workerQuantity + ", totalTime=" + totalTime + ", yd=" + yd
				+ ", numOfYd=" + numOfYd + ", costOfYd=" + costOfYd
				+ ", remarks=" + remarks + "]";
	}
	
	
	

}
