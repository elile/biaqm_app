package model;

public class Trailing_machine {
	
	public int id ;
    public String name ;
    public double hour_price ;
    
	@Override
	public String toString() 
	{
		return "motoring_machinery [id=" + id + ", name=" + name
				+ ", hour_price=" + hour_price + "]";
	}
	public Trailing_machine(int id, String name, double hour_price) 
	{
		super();
		this.id = id;
		this.name = name;
		this.hour_price = hour_price;
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
	public double getHour_price() {
		return hour_price;
	}
	public void setHour_price(double hour_price) {
		this.hour_price = hour_price;
	} 
    
    

}
