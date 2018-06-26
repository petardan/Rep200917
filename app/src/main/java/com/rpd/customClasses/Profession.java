package com.rpd.customClasses;

import java.io.Serializable;

/**
 * Created by Petar on 9/21/2017.
 */

public class Profession implements Serializable{

    String id;
    String categoryId;
    String name;
    String description;

    public Profession() {
    }

    public Profession(String id, String categoryId, String name, String description) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return Integer.valueOf(id);
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCategoryId() {
        return Integer.valueOf(categoryId);

    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
