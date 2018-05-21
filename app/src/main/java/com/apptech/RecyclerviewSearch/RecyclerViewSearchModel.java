package com.apptech.RecyclerviewSearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nirob on 12/16/17.
 */

public class RecyclerViewSearchModel {

    private int id;

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("image")
    @Expose
    private String image;

    private String status;

    public RecyclerViewSearchModel() {
    }

    public RecyclerViewSearchModel(int id, String name, String phone, String image, String status) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.image = image;
        this.status = status;
    }

    public RecyclerViewSearchModel(String name, String phone, String image, String status) {
        this.name = name;
        this.phone = phone;
        this.image = image;
        this.status = status;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
