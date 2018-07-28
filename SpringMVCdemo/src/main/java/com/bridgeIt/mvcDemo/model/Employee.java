package com.bridgeIt.mvcDemo.model;

public class Employee {

	private int id;
	private String name;
	private String mailId;
	
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
	public String getMailId() {
		return mailId;
	}
	public void setMailId(String mailId) {
		this.mailId = mailId;
	}
	
	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", mailId=" + mailId + "]";
	}
}
