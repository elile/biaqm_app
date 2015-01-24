package model;

import java.math.BigDecimal;

public class ActivityType  extends BaseSpinnerModel  
{
	private int id ;
	private String name ;
	private long ActivityGroup;
	private BigDecimal ActualPrice;
	private int YieldDescription;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getActivityGroup() {
		return ActivityGroup;
	}
	public void setActivityGroup(long activityGroup) {
		ActivityGroup = activityGroup;
	}
	public BigDecimal getActualPrice() {
		return ActualPrice;
	}
	public void setActualPrice(BigDecimal actualPrice) {
		ActualPrice = actualPrice;
	}
	public int getYieldDescription() {
		return YieldDescription;
	}
	public void setYieldDescription(int yieldDescription) {
		YieldDescription = yieldDescription;
	}
	@Override
	public String toString() {
		return name;

	}
	public ActivityType(int id, String name, long activityGroup,
			BigDecimal actualPrice, int yieldDescription) {
		super();
		this.id = id;
		this.name = name;
		ActivityGroup = activityGroup;
		ActualPrice = actualPrice;
		YieldDescription = yieldDescription;
	}
	@Override
	public long getIdBase() {
		// TODO Auto-generated method stub
		return id;
	}
	@Override
	public String getSpinnerName() {
		// TODO Auto-generated method stub
		return name;
	}
	
	
	

}
