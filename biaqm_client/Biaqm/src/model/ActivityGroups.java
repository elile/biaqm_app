package model;

public class ActivityGroups  extends BaseSpinnerModel  
{
	private long ID ;
	private String GroupName ;
	
	public ActivityGroups(long iD, String groupName)
	{
		ID = iD;
		GroupName = groupName;
	}
	
	@Override
	public String toString() 
	{
		return GroupName;
	}
	
	public long getID()
	{
		return ID;
	}
	
	public void setID(long iD) 
	{
		ID = iD;
	}
	
	public String getGroupName() 
	{
		return GroupName;
	}
	
	public void setGroupName(String groupName) 
	{
		GroupName = groupName;
	}

	@Override
	public long getIdBase() {
		// TODO Auto-generated method stub
		return ID;
	}

	@Override
	public String getSpinnerName() {
		// TODO Auto-generated method stub
		return GroupName;
	}
	

}