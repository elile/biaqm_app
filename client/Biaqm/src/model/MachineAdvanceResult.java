package model;

import java.io.Serializable;

public class MachineAdvanceResult implements Serializable
{
	private motoring_machinery m;
	private double totalTime;
	private Yield_Descriptions yd;
	private int numOfYd;
	private double costOfYd;
	private String remarks;
	
	
	
	
	public MachineAdvanceResult(motoring_machinery m, double totalTime,
			Yield_Descriptions yd, int numOfYd, double costOfYd, String remarks) {
		super();
		this.m = m;
		this.totalTime = totalTime;
		this.yd = yd;
		this.numOfYd = numOfYd;
		this.costOfYd = costOfYd;
		this.remarks = remarks;
	}
	
	public motoring_machinery getM() {
		return m;
	}
	public void setM(motoring_machinery m) {
		this.m = m;
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
		return "MachineAdvanceResult [m=" + m + ", totalTime=" + totalTime
				+ ", yd=" + yd + ", numOfYd=" + numOfYd + ", costOfYd="
				+ costOfYd + ", remarks=" + remarks + "]";
	}
	
	
}
