package com.shuishou.sysmgr.beans;


import com.fasterxml.jackson.annotation.JsonIgnore;

public class Category2Printer {
	private long id;
	
	private Category2 category2;
	
	private Printer printer;
	
	private int printStyle;

    private String printTemplateFile;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Category2 getCategory2() {
		return category2;
	}

	public void setCategory2(Category2 category2) {
		this.category2 = category2;
	}

	public Printer getPrinter() {
		return printer;
	}

	public void setPrinter(Printer printer) {
		this.printer = printer;
	}

	public int getPrintStyle() {
		return printStyle;
	}

	public void setPrintStyle(int printStyle) {
		this.printStyle = printStyle;
	}

    public String getPrintTemplateFile() {
        return printTemplateFile;
    }

    public void setPrintTemplateFile(String printTemplateFile) {
        this.printTemplateFile = printTemplateFile;
    }

    @Override
	public String toString() {
		return "Category2Printer [category2=" + category2 + ", printer=" + printer + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		Category2Printer other = (Category2Printer) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}
