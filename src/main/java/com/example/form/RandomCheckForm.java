package com.example.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RandomCheckForm {

	@NotBlank(message="{NotBlank.form.email}")
	@Email(message="{Email.form.email}")
	private String mail;
	private String passCheck;
	
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getPassCheck() {
		return passCheck;
	}
	public void setPassCheck(String passCheck) {
		this.passCheck = passCheck;
	}
	@Override
	public String toString() {
		return "RandomCheckForm [mail=" + mail + ", passCheck=" + passCheck + "]";
	}
	
	
}
