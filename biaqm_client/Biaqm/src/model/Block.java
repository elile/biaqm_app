package model;

import java.util.Date;

public class Block  extends BaseSpinnerModel  {
	
	public int ID ;
    public Date InsertDate ;
    public int InsertUser ;
    public Date LastUpdate ;
    public int LastUpdateUser ;
    public String NameEng ;
    public String NameHe ;
    public int RegionID ;
    public long FarmID ;
    public String Remarks ;
    
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public Date getInsertDate() {
		return InsertDate;
	}
	public void setInsertDate(Date insertDate) {
		InsertDate = insertDate;
	}
	public int getInsertUser() {
		return InsertUser;
	}
	public void setInsertUser(int insertUser) {
		InsertUser = insertUser;
	}
	public Date getLastUpdate() {
		return LastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		LastUpdate = lastUpdate;
	}
	public int getLastUpdateUser() {
		return LastUpdateUser;
	}
	public void setLastUpdateUser(int lastUpdateUser) {
		LastUpdateUser = lastUpdateUser;
	}
	public String getNameEng() {
		return NameEng;
	}
	public void setNameEng(String nameEng) {
		NameEng = nameEng;
	}
	public String getNameHe() {
		return NameHe;
	}
	public void setNameHe(String nameHe) {
		NameHe = nameHe;
	}
	public int getRegionID() {
		return RegionID;
	}
	public void setRegionID(int regionID) {
		RegionID = regionID;
	}
	public long getFarmID() {
		return FarmID;
	}
	public void setFarmID(long farmID) {
		FarmID = farmID;
	}
	public String getRemarks() {
		return Remarks;
	}
	public void setRemarks(String remarks) {
		Remarks = remarks;
	}
	@Override
	public String toString() {
		return NameHe;

	}
	public Block(int iD, Date insertDate, int insertUser, Date lastUpdate,
			int lastUpdateUser, String nameEng, String nameHe, int regionID,
			long farmID, String remarks) {
		super();
		ID = iD;
		InsertDate = insertDate;
		InsertUser = insertUser;
		LastUpdate = lastUpdate;
		LastUpdateUser = lastUpdateUser;
		NameEng = nameEng;
		NameHe = nameHe;
		RegionID = regionID;
		FarmID = farmID;
		Remarks = remarks;
	}
	@Override
	public long getIdBase() {
		// TODO Auto-generated method stub
		return ID;
	}
	@Override
	public String getSpinnerName() {
		// TODO Auto-generated method stub
		return NameHe;

	}
    
    

    
    
}
