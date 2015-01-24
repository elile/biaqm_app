package model;

import java.math.BigDecimal;

public class Farm extends BaseSpinnerModel  
{
	private long id ;
	private String InsertDate ;
	private int InsertUser ;
	private String LastUpdate ;
	private int LastUpdateUser ;
	private String name ;
	private String name_eng ;
	private int AreaUOM ;
	private int LengthUOM ;
	private BigDecimal UnitsConversion ;
	private String contact_name ;
	private String contact_phone ;
	private String contact_fax ;
	private String contact_email ;
	private String tax_number ;
	private long company_id ;
	
	
	
	
	
	public Farm(long id, String insertDate, int insertUser, String lastUpdate,
			int lastUpdateUser, String name, String name_eng, int areaUOM,
			int lengthUOM, BigDecimal unitsConversion, String contact_name,
			String contact_phone, String contact_fax, String contact_email,
			String tax_number, long company_id) {
		this.id = id;
		InsertDate = insertDate;
		InsertUser = insertUser;
		LastUpdate = lastUpdate;
		LastUpdateUser = lastUpdateUser;
		this.name = name;
		this.name_eng = name_eng;
		AreaUOM = areaUOM;
		LengthUOM = lengthUOM;
		UnitsConversion = unitsConversion;
		this.contact_name = contact_name;
		this.contact_phone = contact_phone;
		this.contact_fax = contact_fax;
		this.contact_email = contact_email;
		this.tax_number = tax_number;
		this.company_id = company_id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getInsertDate() {
		return InsertDate;
	}

	public void setInsertDate(String insertDate) {
		InsertDate = insertDate;
	}

	public int getInsertUser() {
		return InsertUser;
	}

	public void setInsertUser(int insertUser) {
		InsertUser = insertUser;
	}

	public String getLastUpdate() {
		return LastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		LastUpdate = lastUpdate;
	}

	public int getLastUpdateUser() {
		return LastUpdateUser;
	}

	public void setLastUpdateUser(int lastUpdateUser) {
		LastUpdateUser = lastUpdateUser;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName_eng() {
		return name_eng;
	}

	public void setName_eng(String name_eng) {
		this.name_eng = name_eng;
	}

	public int getAreaUOM() {
		return AreaUOM;
	}

	public void setAreaUOM(int areaUOM) {
		AreaUOM = areaUOM;
	}

	public int getLengthUOM() {
		return LengthUOM;
	}

	public void setLengthUOM(int lengthUOM) {
		LengthUOM = lengthUOM;
	}

	public BigDecimal getUnitsConversion() {
		return UnitsConversion;
	}

	public void setUnitsConversion(BigDecimal unitsConversion) {
		UnitsConversion = unitsConversion;
	}

	public String getContact_name() {
		return contact_name;
	}

	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}

	public String getContact_phone() {
		return contact_phone;
	}

	public void setContact_phone(String contact_phone) {
		this.contact_phone = contact_phone;
	}

	public String getContact_fax() {
		return contact_fax;
	}

	public void setContact_fax(String contact_fax) {
		this.contact_fax = contact_fax;
	}

	public String getContact_email() {
		return contact_email;
	}

	public void setContact_email(String contact_email) {
		this.contact_email = contact_email;
	}

	public String getTax_number() {
		return tax_number;
	}

	public void setTax_number(String tax_number) {
		this.tax_number = tax_number;
	}

	public long getCompany_id() {
		return company_id;
	}

	public void setCompany_id(long company_id) {
		this.company_id = company_id;
	}

	@Override
	public long getIdBase() 
	{
		return id;
	}
	
	@Override
	public String getSpinnerName() 
	{
		return name;
	}

	@Override
	public String toString() {
		return name;
	}



}
