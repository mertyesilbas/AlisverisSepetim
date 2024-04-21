package com.example.alisveris_sepetim.models;

import androidx.annotation.NonNull;

import java.util.List;

public class SectionItem {
    private String sectionName;
    private List<ShoppingItem> sectionItems;


    public SectionItem(String sectionName, List<ShoppingItem> sectionItems) {
        this.sectionName = sectionName;
        this.sectionItems = sectionItems;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public List<ShoppingItem> getSectionItems() {
        return sectionItems;
    }

    public void setSectionItems(List<ShoppingItem> sectionItems) {
        this.sectionItems = sectionItems;
    }

    @NonNull
    @Override
    public String toString() {
        return "ItemSection{" +
                "sectionName='" + sectionName + '\'' +
                ", sectionItems=" + sectionItems +
                '}';
    }
}
