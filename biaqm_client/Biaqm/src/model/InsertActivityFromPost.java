package model;

import java.util.ArrayList;
import java.util.Arrays;

public class InsertActivityFromPost 
{

	private String ActivityType_id;
	private String CompanyID ;
	private String farm_id ;
	private String Remarks ;
	private String BlockID ;
	private String PlotID ;
	private String CropID ;
	private String VarietyID ;
	private String date ;

	private ArrayList<WorkerToInsert> workerToInsert ;
	private ArrayList<MachineToInsert> machineToInsert ;



	public InsertActivityFromPost(String activityType_id, String companyID,
			String farm_id, String remarks, String blockID, String plotID,
			String cropID, String varietyID, String date,
			ArrayList<WorkerToInsert> workerToInsert,
			ArrayList<MachineToInsert> machineToInsert) {
		super();
		ActivityType_id = activityType_id;
		CompanyID = companyID;
		this.farm_id = farm_id;
		Remarks = remarks;
		BlockID = blockID;
		PlotID = plotID;
		CropID = cropID;
		VarietyID = varietyID;
		this.date = date;
		this.workerToInsert = workerToInsert;
		this.machineToInsert = machineToInsert;
	}


	@Override
	public String toString() {
		return "InsertActivityFromPost [ActivityType_id=" + ActivityType_id
				+ ", CompanyID=" + CompanyID + ", farm_id=" + farm_id
				+ ", Remarks=" + Remarks + ", BlockID=" + BlockID + ", PlotID="
				+ PlotID + ", CropID=" + CropID + ", VarietyID=" + VarietyID
				+ ", workerToInsert=" + Arrays.toString(workerToInsert.toArray())
				+ ", machineToInsert=" + Arrays.toString(machineToInsert.toArray()) + "]";
	}

	
	

	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getActivityType_id() {
		return ActivityType_id;
	}
	public void setActivityType_id(String activityType_id) {
		ActivityType_id = activityType_id;
	}
	public String getCompanyID() {
		return CompanyID;
	}
	public void setCompanyID(String companyID) {
		CompanyID = companyID;
	}
	public String getFarm_id() {
		return farm_id;
	}
	public void setFarm_id(String farm_id) {
		this.farm_id = farm_id;
	}
	public String getRemarks() {
		return Remarks;
	}
	public void setRemarks(String remarks) {
		Remarks = remarks;
	}
	public String getBlockID() {
		return BlockID;
	}
	public void setBlockID(String blockID) {
		BlockID = blockID;
	}
	public String getPlotID() {
		return PlotID;
	}
	public void setPlotID(String plotID) {
		PlotID = plotID;
	}
	public String getCropID() {
		return CropID;
	}
	public void setCropID(String cropID) {
		CropID = cropID;
	}
	public String getVarietyID() {
		return VarietyID;
	}
	public void setVarietyID(String varietyID) {
		VarietyID = varietyID;
	}
	public ArrayList<WorkerToInsert> getWorkerToInsert() {
		return workerToInsert;
	}
	public void setWorkerToInsert(ArrayList<WorkerToInsert> workerToInsert) {
		this.workerToInsert = workerToInsert;
	}
	public ArrayList<MachineToInsert> getMachineToInsert() {
		return machineToInsert;
	}
	public void setMachineToInsert(ArrayList<MachineToInsert> machineToInsert) {
		this.machineToInsert = machineToInsert;
	}


}
