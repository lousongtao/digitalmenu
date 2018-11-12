package com.shuishou.sysmgr.beans;

import java.util.Date;

public class IndentDetail {

	private int id;
	
	private Indent indent;
	
	private int dishId;
	
	private int amount;
	
	private double dishPrice;//����dish�۸�, ������amount
	
	private String dishFirstLanguageName;
	
	private String dishSecondLanguageName;
	
	private String additionalRequirements;
	
	private double weight;

	private Date time;
	
	public Indent getIndent() {
		return indent;
	}

	public void setIndent(Indent indent) {
		this.indent = indent;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public int getDishId() {
		return dishId;
	}

	public void setDishId(int dishId) {
		this.dishId = dishId;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public double getDishPrice() {
		return dishPrice;
	}

	public void setDishPrice(double dishPrice) {
		this.dishPrice = dishPrice;
	}

	
	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getDishFirstLanguageName() {
		return dishFirstLanguageName;
	}

	public void setDishFirstLanguageName(String dishFirstLanguageName) {
		this.dishFirstLanguageName = dishFirstLanguageName;
	}

	public String getDishSecondLanguageName() {
		return dishSecondLanguageName;
	}

	public void setDishSecondLanguageName(String dishSecondLanguageName) {
		this.dishSecondLanguageName = dishSecondLanguageName;
	}

	public String getAdditionalRequirements() {
		return additionalRequirements;
	}

	public void setAdditionalRequirements(String additionalRequirements) {
		this.additionalRequirements = additionalRequirements;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IndentDetail other = (IndentDetail) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OrderDetail [amount=" + amount + ", dishFirstLanguageName=" + dishFirstLanguageName + "]";
	}
}
