package com.smartvillageoi.clientapp.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Product implements Serializable {

String id;
    String name;
    String slug;
    String category_id;
    String indicator;
    String manufacturer;
    String made_in;
    String return_status;
    String cancelable_status;
    String till_status;
    String image;
    String size_chart;
    String description;
    String status;
    String ratings;
    String number_of_ratings;
    String tax_percentage;
    Double latitude;
    Double longitude;
    String distance;
    boolean is_favorite;
    ArrayList<Variants> variants;
    ArrayList<String> other_images;

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

    public String getSlug() {
        return slug;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getIndicator() {
        return indicator;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getMade_in() {
        return made_in;
    }

    public String getReturn_status() {
        return return_status;
    }

    public String getCancelable_status() {
        return cancelable_status;
    }

    public String getTill_status() {
        return till_status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSize_chart() {
        return size_chart;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRatings() {
        return ratings;
    }

    public String getNumber_of_ratings() {
        return number_of_ratings;
    }

    public String getTax_percentage() {
        return tax_percentage;
    }

    public boolean getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(boolean is_favorite) {
        this.is_favorite = is_favorite;
    }

    public ArrayList<Variants> getVariants() {
        return variants;
    }

    public ArrayList<String> getOther_images() {
        return other_images;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}