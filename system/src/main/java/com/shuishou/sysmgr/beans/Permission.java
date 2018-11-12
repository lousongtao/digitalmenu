package com.shuishou.sysmgr.beans;

public class Permission {
	private int id;
	
	private String name;
	
	private int sequence;
	
	private String description;
	
	

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

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
	
	
	public String toString(){
		return name;
	}
	
	public boolean equals(Object o){
		if (o instanceof Permission){
			return name.equals(((Permission)o).getName());
		}
		return false;
	}
	
	public int hashCode(){
		return super.hashCode();
	}
}
