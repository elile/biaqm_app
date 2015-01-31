package model;

public class Crop  extends BaseSpinnerModel  
{
	private int id;
	private String name;
	
	
	
	public Crop(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	@Override
	public String toString() {
		return name;
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
