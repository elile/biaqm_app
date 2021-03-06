package model;

import java.io.Serializable;

public class Yield_Descriptions extends BaseSpinnerModel  implements Serializable 
{
	private int ID;
	private String Name;
	private int CorrelatedField;
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}


	public int getCorrelatedField() {
		return CorrelatedField;
	}
	public void setCorrelatedField(int correlatedField) {
		CorrelatedField = correlatedField;
	}
	public Yield_Descriptions(int iD, String name, 	int correlatedField) {
		super();
		ID = iD;
		Name = name;
		CorrelatedField = correlatedField;
	}
	
	@Override
	public String toString() {
		return Name;
	}
	
	
	@Override
	public long getIdBase() {
		// TODO Auto-generated method stub
		return ID;
	}
	@Override
	public String getSpinnerName() {
		// TODO Auto-generated method stub
		return Name;
	}


}
