package com.shuishou.sysmgr.beans;

import com.google.gson.annotations.SerializedName;
import com.shuishou.sysmgr.ConstantValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/22.
 */

public class Dish implements Serializable{
	@SerializedName(value = "id", alternate={"objectid"})
    private int id;

    private String firstLanguageName;

    private String secondLanguageName;

    private int sequence;

    private Category2 category2;

    private double price;

    private String pictureName;

    private boolean isNew = false;

    private boolean isSpecial = false;

    private boolean isSoldOut;

    private int hotLevel;

    private String abbreviation;

    private int chooseMode = ConstantValue.DISH_CHOOSEMODE_DEFAULT;
	
	private DishChoosePopinfo choosePopInfo;
	
	private boolean autoMergeWhileChoose = true;

	private int purchaseType = ConstantValue.DISH_PURCHASETYPE_UNIT;
	
	private boolean allowFlavor = true;
	
	private double originPrice;
	private boolean isPromotion = false;
	private String description_1stlang;
	private String description_2ndlang;
	
	private ArrayList<DishConfigGroup> configGroups = new ArrayList<>();
	
	public String getDescription_1stlang() {
		return description_1stlang;
	}

	public void setDescription_1stlang(String description_1stlang) {
		this.description_1stlang = description_1stlang;
	}

	public String getDescription_2ndlang() {
		return description_2ndlang;
	}

	public void setDescription_2ndlang(String description_2ndlang) {
		this.description_2ndlang = description_2ndlang;
	}

	public double getOriginPrice() {
		return originPrice;
	}

	public void setOriginPrice(double originPrice) {
		this.originPrice = originPrice;
	}

	public boolean isPromotion() {
		return isPromotion;
	}

	public void setPromotion(boolean isPromotion) {
		this.isPromotion = isPromotion;
	}

	public int getPurchaseType() {
		return purchaseType;
	}

	public void setPurchaseType(int purchaseType) {
		this.purchaseType = purchaseType;
	}

	public boolean isAutoMergeWhileChoose() {
		return autoMergeWhileChoose;
	}

	public void setAutoMergeWhileChoose(boolean autoMergeWhileChoose) {
		this.autoMergeWhileChoose = autoMergeWhileChoose;
	}
	
	public Dish(){

    }

	
	public ArrayList<DishConfigGroup> getConfigGroups() {
		return configGroups;
	}

	public void setConfigGroups(ArrayList<DishConfigGroup> configGroups) {
		this.configGroups = configGroups;
	}


    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public int getHotLevel() {
        return hotLevel;
    }

    public void setHotLevel(int hotLevel) {
        this.hotLevel = hotLevel;
    }

    public boolean isSoldOut() {
        return isSoldOut;
    }

    public void setSoldOut(boolean soldOut) {
        isSoldOut = soldOut;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    public void setSpecial(boolean isSpecial) {
        this.isSpecial = isSpecial;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getFirstLanguageName() {
		return firstLanguageName;
	}

	public void setFirstLanguageName(String firstLanguageName) {
		this.firstLanguageName = firstLanguageName;
	}

	public String getSecondLanguageName() {
		return secondLanguageName;
	}

	public void setSecondLanguageName(String secondLanguageName) {
		this.secondLanguageName = secondLanguageName;
	}

	public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public Category2 getCategory2() {
        return category2;
    }

    public void setCategory2(Category2 category2) {
        this.category2 = category2;
    }

    
    public int getChooseMode() {
		return chooseMode;
	}

	public void setChooseMode(int chooseMode) {
		this.chooseMode = chooseMode;
	}

	public DishChoosePopinfo getChoosePopInfo() {
		return choosePopInfo;
	}

	public void setChoosePopInfo(DishChoosePopinfo choosePopInfo) {
		this.choosePopInfo = choosePopInfo;
	}

	public boolean isAllowFlavor() {
		return allowFlavor;
	}

	public void setAllowFlavor(boolean allowFlavor) {
		this.allowFlavor = allowFlavor;
	}

	@Override
    public String toString() {
        return firstLanguageName;
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
        Dish other = (Dish) obj;
        if (id != other.id)
            return false;
        return true;
    }
}
