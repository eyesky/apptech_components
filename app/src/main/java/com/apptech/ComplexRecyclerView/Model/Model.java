package com.apptech.ComplexRecyclerView.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nirob on 3/29/17.
 */

public class Model {

    private int id;
    private String status;

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("releaseYear")
    @Expose
    private String releaseYear;
    @SerializedName("genre")
    @Expose
    private List<String> genre = null;

    public Model(int id, String title, String image, String rating, String releaseYear) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.rating = rating;
        this.releaseYear = releaseYear;
    }

    public Model(int id, String status, String title, String image, String rating, String releaseYear) {
        this.id = id;
        this.status = status;
        this.title = title;
        this.image = image;
        this.rating = rating;
        this.releaseYear = releaseYear;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

}