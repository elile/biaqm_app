package model;

public class Variety 
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
		return "Variety [id=" + id + ", name=" + name + ", crop_id=" + crop_id
				+ "]";
	}
	public Variety(int id, String name, int crop_id) {
		super();
		this.id = id;
		this.name = name;
		this.crop_id = crop_id;
	}
	
	
	

}
