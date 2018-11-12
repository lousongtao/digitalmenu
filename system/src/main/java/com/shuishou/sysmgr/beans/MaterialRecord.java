package com.shuishou.sysmgr.beans;

import java.io.Serializable;
import java.util.Date;

public class MaterialRecord implements Serializable {

	private int id;
	
	/**
	 * 操作数量, 可以为负
	 */
	private double amount;
	
	/**
	 * 每次操作后的剩余数量
	 */
	private double leftAmount;
	
	private String operator;
	
	/**
	 * 操作类型
	 */
	private int type;
	
	private Date date;
	
	private int indentDetailId;
	
	
	
	public int getIndentDetailId() {
		return indentDetailId;
	}

	public void setIndentDetailId(int indentDetailId) {
		this.indentDetailId = indentDetailId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getLeftAmount() {
		return leftAmount;
	}

	public void setLeftAmount(double leftAmount) {
		this.leftAmount = leftAmount;
	}


	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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
		MaterialRecord other = (MaterialRecord) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Material";
	}

	
	
}
