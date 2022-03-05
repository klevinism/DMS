package com.visionous.dms.pojo;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class IaoAccount {

    @NotEmpty(message = "{alert.fieldEmpty}")
	private String name;
	
    @NotEmpty(message = "{alert.fieldEmpty}")
	private String surname;
	
	private String email;
	
	private long phone;
	
    @NotEmpty(message = "{alert.fieldEmpty}")
	private String password;
	
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern="dd-MM-yyyy")
	private Date birthday;
	
    @NotEmpty(message = "{alert.fieldEmpty}")
	private String gender;
	
	private Boolean terms = false;
	
	public IaoAccount() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getPhone() {
		return phone;
	}

	public void setPhone(Long phone) {
		this.phone = phone;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Boolean getTerms() {
		return terms;
	}

	public void setTerms(Boolean terms) {
		this.terms = terms;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
