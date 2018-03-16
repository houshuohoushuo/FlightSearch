package com.example.monkey.myapplication.backend;

import java.io.Serializable;

public class User implements Serializable {
	private String lastName;
	private String firstName;
	private String email;
	private String Address;
	private String cardNum;
	private String expiryDate;

	public User(String lastName, String firstName, String email, String Address, String cardNum, String expiryDate) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.Address = Address;
		this.cardNum = cardNum;
		this.expiryDate = expiryDate;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String toString() {
		return lastName + ',' + firstName + ',' + email + ',' + Address + ',' + cardNum + ',' + expiryDate;
	}
}