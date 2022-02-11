package com.visionous.dms.pojo;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class IaoAccount extends Account {

	private String name;
	
	private String surname;
	
	private String email;
	
	private long phone;
	
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern="dd-MM-yyyy")
	private Date birthday;
	
	private String gender;
	
	private Boolean terms = false;
	
	public IaoAccount() {
		
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getSurname() {
		return surname;
	}

	@Override
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public Long getPhone() {
		return phone;
	}

	public void setPhone(Long phoneNr) {
		this.phone = phoneNr;
	}

	@Override
	public Date getBirthday() {
		return birthday;
	}

	@Override
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Override
	public String getGender() {
		return gender;
	}

	@Override
	public void setGender(String gender) {
		this.gender = gender;
	}

	public Boolean getTerms() {
		return terms;
	}

	public void setTerms(Boolean terms) {
		this.terms = terms;
	}
	
	
	
}
