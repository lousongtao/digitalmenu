package com.shuishou.sysmgr.beans;

import java.io.Serializable;

public class DishMaterialConsume implements Serializable{

	private int id;
	
	private Dish dish;
	
	private Material material;
	
	/**
	 * 一道菜消耗的量
	 */
	private double amount;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Dish getDish() {
		return dish;
	}

	public void setDish(Dish dish) {
		this.dish = dish;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "DishMaterialConsume [id=" + id + ", dish=" + dish + ", material=" + material + ", amount=" + amount
				+ "]";
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
		DishMaterialConsume other = (DishMaterialConsume) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}
