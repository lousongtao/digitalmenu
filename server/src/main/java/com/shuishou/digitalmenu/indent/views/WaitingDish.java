package com.shuishou.digitalmenu.indent.views;

import java.util.ArrayList;
import java.util.List;

public class WaitingDish {
    private String dishName;
    private List<WaitingIndentDetail> indentDetails = new ArrayList<>();

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public List<WaitingIndentDetail> getIndentDetails() {
        return indentDetails;
    }

    public void setIndentDetails(List<WaitingIndentDetail> indentDetails) {
        this.indentDetails = indentDetails;
    }

    @Override
    public String toString() {
        return "WaitingDish{" +
                "dishName='" + dishName + '\'' +
                '}';
    }
}
