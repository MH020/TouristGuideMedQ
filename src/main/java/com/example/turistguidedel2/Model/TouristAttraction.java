package com.example.turistguidedel2.Model;

import java.util.ArrayList;
import java.util.List;

public class TouristAttraction {
    private int id;
    private String name;
    private String description;
    private String city;
    private String tags ;

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
        return tags;
    }
    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCity(){
        return city;
    }
    public void setCity(String city){
        this.city = city;
    }

    //Tom constructor
    public TouristAttraction() {
    }


    //constructor
    public TouristAttraction(String name, String description, String city,  String tags) {
        this.name = name;
        this.description = description;
        this.city = city;
        this.tags = tags;
    }


}