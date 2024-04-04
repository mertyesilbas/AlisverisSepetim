package com.example.alisveris_sepetim;

public class ShoppingItem {
    private String id;
    private String name;
    private boolean isChecked;

    public ShoppingItem() {
    }

    public ShoppingItem(String id, String name, boolean isChecked) {
        this.id = id;
        this.name = name;
        this.isChecked = isChecked;
    }

    // Getters and setters for each field

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    // Other getters and setters for name and isChecked
}