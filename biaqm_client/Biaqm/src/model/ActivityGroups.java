package model;

public class ActivityGroups
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
		return "ActivityGroups [ID=" + ID + ", GroupName=" + GroupName	+ "]";
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
	

}