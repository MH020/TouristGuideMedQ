package com.example.turistguidedel2.Model;

import java.util.ArrayList;
import java.util.List;

public class TouristAttraction {
    private int id;
    private String name;
    private String description;
    private String city;
    private int postcode;
    private String tags;

    //getters and setters
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

    public String getTags() {
        return tags != null ? tags : "";
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostcode(int postcode){
        this.postcode =postcode;
    }

    public int getPostcode(){
        return postcode;
    }

    //Tom constructor
    public TouristAttraction() {
    }


    //constructor
    public TouristAttraction(String name, String description, String city,int postcode, String tags) {
        this.name = name;
        this.description = description;
        this.city = city;
        this.postcode = postcode;
        this.tags = tags;
    }


}