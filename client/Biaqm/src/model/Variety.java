package model;

public class Variety  extends BaseSpinnerModel  
{
	
	private int id;
	private String name;
	private int crop_id;
	
	
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
	public int getCrop_id() {
		return crop_id;
	}
	public void setCrop_id(int crop_id) {
		this.crop_id = crop_id;
	}
	@Override
	public String toString() {
		return name;
	}
	public Variety(int id, String name, int crop_id) {
		super();
		this.id = id;
		this.name = name;
		this.crop_id = crop_id;
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
