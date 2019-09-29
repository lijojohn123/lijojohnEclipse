package com.ust.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.ust.model.User;
import com.ust.model.VendorContactPerson;


public class UstDao {
	JdbcTemplate template;

	public void setTemplate(JdbcTemplate template) {
		this.template = template;
	}
	
	//--------------------UST LOGIN---------------------//
	
	public User selectRole(String username, String password) {
		String sql = "select user_id from login where username='"
				+ username
				+ "' and password='" + password + "'";
		return template.queryForObject(sql, new Object[] {},
				new BeanPropertyRowMapper<User>(User.class));
	}
	
	//------get all vendors
	
	public List<VendorContactPerson> getVendor() {
		return template
				.query("select vc.vendor_id,vc.vendor_name,vc.address,vc.location,vc.service,vc.pincode,cd.name,cd.dept,cd.email,cd.phone from vendor vc join contactPersonDetails cd on vc.vendor_id=cd.vendor_id where vc.isActive=0",
						new RowMapper<VendorContactPerson>() {
							public VendorContactPerson mapRow(ResultSet rs, int row)
									throws SQLException {
								VendorContactPerson vc = new VendorContactPerson();
								vc.setVendor_id(rs.getInt(1));
								vc.setVendor_name(rs.getString(2));
								vc.setAddress(rs.getString(3));
								vc.setLocation(rs.getString(4));
								vc.setService(rs.getString(5));
								vc.setPincode(rs.getInt(6));
								vc.setName(rs.getString(7));
								vc.setDept(rs.getString(8));
								vc.setEmail(rs.getString(9));
								vc.setPhone(rs.getString(10));
								return vc;
							}
						});
	}
	
	//------get vendor by name
	
		public List<VendorContactPerson> getVendorByName(String vendor_name) {
			return template
					.query("select vendor_id,vendor_name,address,location,service,pincode from vendor where isActive=0 and vendor_name='"+ vendor_name + "'",
							new RowMapper<VendorContactPerson>() {
								public VendorContactPerson mapRow(ResultSet rs, int row)
										throws SQLException {
									VendorContactPerson vc = new VendorContactPerson();
									vc.setVendor_id(rs.getInt(1));
									vc.setVendor_name(rs.getString(2));
									vc.setAddress(rs.getString(3));
									vc.setLocation(rs.getString(4));
									vc.setService(rs.getString(5));
									vc.setPincode(rs.getInt(6));
									return vc;
								}
							});
		}
		
		//------------get vendor details by id
	
		
		public VendorContactPerson getVendorById(int vendor_id) {
			String sql = "select vc.vendor_id,vc.vendor_name,vc.address,vc.location,vc.service,vc.pincode,cd.name,cd.dept,cd.email,cd.phone,cd.cont_id from vendor vc join contactPersonDetails cd on vc.vendor_id=cd.vendor_id where vc.isActive=0 and vc.vendor_id="+ vendor_id +"";
			return template.queryForObject(sql,
					new BeanPropertyRowMapper<VendorContactPerson>(VendorContactPerson.class));
		}
	
	//---------Add vendors
	
	public int saveVendor(VendorContactPerson vc) {

		String sql1 = "insert into vendor(vendor_name,address,location,service,pincode,isActive) values "
				+ "('"
				+ vc.getVendor_name()
				+ "','"
				+ vc.getAddress()
				+ "','"
				+ vc.getLocation()
				+ "','"
				+ vc.getService()
				+ "','"
				+ vc.getPincode()
				+ "',"
				+ 0
				+ ")";

		 template.update(sql1);
		 
		 Integer maxId = getSequence();
		 String sql2="insert into contactPersonDetails(vendor_id,name,dept,email,phone) values ("
				 + maxId
					+ ",'"
					+ vc.getName()
					+ "','"
					+ vc.getDept()
					+ "','"
					+ vc.getEmail()
					+ "','" + vc.getPhone() + "')";
		 return template.update(sql2);

				 
		 
	}
	
	//---------- to get vendor id
	private Integer getSequence() {
		Integer seq;
		String sql = "select MAX(vendor_id)from vendor";
		seq = template.queryForObject(sql, new Object[] {}, Integer.class);
		return seq;
	}
	
	//-------------- update vendor
	public int updateVendor(int vendor_id, VendorContactPerson vc) {

		String sql = "update vendor set vendor_name='" + vc.getVendor_name()
				+ "' ,address='" + vc.getAddress() + "' ,location='"
				+ vc.getLocation() + "',service='" + vc.getService() + "',pincode='" + vc.getPincode() + "',isActive=" + vc.getIsActive() + " "
				+ "where vendor_id =" + vendor_id;
		template.update(sql);

		// Integer maxId = getSequence();

		String sql1 = "update contactPersonDetails set vendor_id=" + vendor_id + ",name='"
				+ vc.getName() + "',dept='"
				+ vc.getDept() + "',email='"
				+ vc.getEmail() + "',phone='" + vc.getPhone() + "'where cont_id = " + vc.getCont_id();

		return template.update(sql1);
	}
	
	//-------------- get contactdetails by id

	public List<VendorContactPerson> getContactDetailsByVId(int vid) {
	return template.query("select cont_id,name,dept,vendor_id,email,phone from contactPersonDetails where vendor_id="+vid+"", new RowMapper<VendorContactPerson>() {
		public VendorContactPerson mapRow(ResultSet rs, int row)
				throws SQLException {
			VendorContactPerson vc = new VendorContactPerson();
			vc.setCont_id(rs.getInt(1));
			vc.setName(rs.getString(2));
			vc.setDept(rs.getString(3));
			vc.setVendor_id(rs.getInt(4));
			vc.setEmail(rs.getString(5));
			vc.setPhone(rs.getString(6));
			return vc;
		}
	});
	}
	//--------------- to disable a vendor
	public int disableVendor(int vId) {

	String sql = "update vendor set isActive='1' where vendor_id=" + vId
	+ "";

	return template.update(sql);
	}
	
}
