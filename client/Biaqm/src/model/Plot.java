package model;

import java.util.Date;

public class Plot  extends BaseSpinnerModel  {
	private int id ;
	private String name ;
	private long farm_id ;
	private int region_id ;
	private int block_id ;
	private Date start_date ;
	private Date end_date ;
	private int crop_id;
	
	
	
	public Plot(int id, String name, long farm_id, int region_id, int block_id,
			Date start_date, Date end_date, int crop_id) {
		super();
		this.id = id;
		this.name = name;
		this.farm_id = farm_id;
		this.region_id = region_id;
		this.block_id = block_id;
		this.start_date = start_date;
		this.end_date = end_date;
		this.crop_id = crop_id;
	}
	public int getCrop_id() {
		return crop_id;
	}
	public void setCrop_id(int crop_id) {
		this.crop_id = crop_id;
	}
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
	public long getFarm_id() {
		return farm_id;
	}
	public void setFarm_id(long farm_id) {
		this.farm_id = farm_id;
	}
	public int getRegion_id() {
		return region_id;
	}
	public void setRegion_id(int region_id) {
		this.region_id = region_id;
	}
	public int getBlock_id() {
		return block_id;
	}
	public void setBlock_id(int block_id) {
		this.block_id = block_id;
	}
	public Date getStart_date() {
		return start_date;
	}
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	@Override
	public String toString() {
		return name;

	}
	public Plot(int id, String name, long farm_id, int region_id, int block_id,
			Date start_date, Date end_date) {
		super();
		this.id = id;
		this.name = name;
		this.farm_id = farm_id;
		this.region_id = region_id;
		this.block_id = block_id;
		this.start_date = start_date;
		this.end_date = end_date;
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
