package model;

import java.io.Serializable;

public class Worker  extends BaseSpinnerModel   implements Serializable 
{
	private int id ;
	private String name ;
	private int PaymentTypeID ;
	private float hour_cost ;
	private float standard_hours ;
	private float first_overtime ;
	private float second_overtime ;
	private float first_overtime_hours ;
	private int worker_type_id ;
	private int farm_id ;
	private int company_id ;
	private String payment_type ;


	public Worker(int id, String name, int paymentTypeID, float hour_cost,
			float standard_hours, float first_overtime, float second_overtime,
			float first_overtime_hours, int worker_type_id, int farm_id,
			int company_id, String payment_type) {
		super();
		this.id = id;
		this.name = name;
		PaymentTypeID = paymentTypeID;
		this.hour_cost = hour_cost;
		this.standard_hours = standard_hours;
		this.first_overtime = first_overtime;
		this.second_overtime = second_overtime;
		this.first_overtime_hours = first_overtime_hours;
		this.worker_type_id = worker_type_id;
		this.farm_id = farm_id;
		this.company_id = company_id;
		this.payment_type = payment_type;
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
	public int getPaymentTypeID() {
		return PaymentTypeID;
	}
	public void setPaymentTypeID(int paymentTypeID) {
		PaymentTypeID = paymentTypeID;
	}
	public float getHour_cost() {
		return hour_cost;
	}
	public void setHour_cost(float hour_cost) {
		this.hour_cost = hour_cost;
	}
	public float getStandard_hours() {
		return standard_hours;
	}
	public void setStandard_hours(float standard_hours) {
		this.standard_hours = standard_hours;
	}
	public float getFirst_overtime() {
		return first_overtime;
	}
	public void setFirst_overtime(float first_overtime) {
		this.first_overtime = first_overtime;
	}
	public float getSecond_overtime() {
		return second_overtime;
	}
	public void setSecond_overtime(float second_overtime) {
		this.second_overtime = second_overtime;
	}
	public float getFirst_overtime_hours() {
		return first_overtime_hours;
	}
	public void setFirst_overtime_hours(float first_overtime_hours) {
		this.first_overtime_hours = first_overtime_hours;
	}
	public int getWorker_type_id() {
		return worker_type_id;
	}
	public void setWorker_type_id(int worker_type_id) {
		this.worker_type_id = worker_type_id;
	}
	public int getFarm_id() {
		return farm_id;
	}
	public void setFarm_id(int farm_id) {
		this.farm_id = farm_id;
	}
	public int getCompany_id() {
		return company_id;
	}
	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}
	public String getPayment_type() {
		return payment_type;
	}
	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}

	@Override
	public long getIdBase() 
	{
		// TODO Auto-generated method stub
		return id;
		
	}

	@Override
	public String getSpinnerName() {
		// TODO Auto-generated method stub
		return name;
	}



}
