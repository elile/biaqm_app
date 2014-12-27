package model;

import java.util.Date;

public class User 
{
	private long id ;
	private String first_name ;
	private String last_name ;
	private String phone_number ;
	private String mobile_phone ;
	private String fax_number ;
	private String skype_name ;
	private String country ;
	private String address ;
	private String email ;
	private String login ;
	private String password ;
	private String syte_lang ;
	private int company_id ;
	private int role ;
	private int farm_id ;
	private short enabled;
	private String User_RegDateTime ;



	@Override
	public String toString() {
		return "User [id=" + id + ", first_name=" + first_name + ", last_name="
				+ last_name + ", phone_number=" + phone_number
				+ ", mobile_phone=" + mobile_phone + ", fax_number="
				+ fax_number + ", skype_name=" + skype_name + ", country="
				+ country + ", address=" + address + ", email=" + email
				+ ", login=" + login + ", password=" + password
				+ ", syte_lang=" + syte_lang + ", company_id=" + company_id
				+ ", role=" + role + ", farm_id=" + farm_id + ", enabled="
				+ enabled + ", User_RegDateTime=" + User_RegDateTime + "]";
	}
	public User(long id, String first_name, String last_name,
			String phone_number, String mobile_phone, String fax_number,
			String skype_name, String country, String address, String email,
			String login, String password, String syte_lang, int company_id,
			int role, int farm_id, short enabled, String user_RegDateTime) 
	{
		
		this.login = login;
		this.password = password;

		this.id = id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.phone_number = phone_number;
		this.mobile_phone = mobile_phone;
		this.fax_number = fax_number;
		this.skype_name = skype_name;
		this.country = country;
		this.address = address;
		this.email = email;
		this.syte_lang = syte_lang;
		this.company_id = company_id;
		this.role = role;
		this.farm_id = farm_id;
		this.enabled = enabled;
		User_RegDateTime = user_RegDateTime;
	}
	public User(String login, String password) {

		this.login = login;
		this.password = password;
		
		this.id = 0;
		this.first_name = "";
		this.last_name = "";
		this.phone_number = "";
		this.mobile_phone = "";
		this.fax_number = "";
		this.skype_name = "";
		this.country = "";
		this.address = "";
		this.email = "";
		this.syte_lang = "";
		this.company_id = 0;
		this.role = 0;
		this.farm_id = 0;
		this.enabled = 0;
		User_RegDateTime = null;
	}
	
	public User() {
		this.login = "";
		this.password = "";
		
		this.id = 0;
		this.first_name = "";
		this.last_name = "";
		this.phone_number = "";
		this.mobile_phone = "";
		this.fax_number = "";
		this.skype_name = "";
		this.country = "";
		this.address = "";
		this.email = "";
		this.syte_lang = "";
		this.company_id = 0;
		this.role = 0;
		this.farm_id = 0;
		this.enabled = 0;
		User_RegDateTime = null;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public String getMobile_phone() {
		return mobile_phone;
	}
	public void setMobile_phone(String mobile_phone) {
		this.mobile_phone = mobile_phone;
	}
	public String getFax_number() {
		return fax_number;
	}
	public void setFax_number(String fax_number) {
		this.fax_number = fax_number;
	}
	public String getSkype_name() {
		return skype_name;
	}
	public void setSkype_name(String skype_name) {
		this.skype_name = skype_name;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSyte_lang() {
		return syte_lang;
	}
	public void setSyte_lang(String syte_lang) {
		this.syte_lang = syte_lang;
	}
	public int getCompany_id() {
		return company_id;
	}
	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public int getFarm_id() {
		return farm_id;
	}
	public void setFarm_id(int farm_id) {
		this.farm_id = farm_id;
	}
	public short getEnabled() {
		return enabled;
	}
	public void setEnabled(short enabled) {
		this.enabled = enabled;
	}
	public String getUser_RegDateTime() {
		return User_RegDateTime;
	}
	public void setUser_RegDateTime(String user_RegDateTime) {
		User_RegDateTime = user_RegDateTime;
	}

}
