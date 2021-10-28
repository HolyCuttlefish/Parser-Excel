package com.example.parseer_excel_maven.Models;

public class Pattern_Three extends Pattern_Two {
    protected boolean Availability;

    public Pattern_Three(){}

    public Pattern_Three (int id, String name, String description, long value, boolean availability){
        this.Id = id;
        this.Name = name;
        this.Description = description;
        this.Value = value;
        this.Availability = availability;
    }

    public boolean getAvailability(){ return this.Availability; }

    public void setAvailability(boolean availability){ this.Availability = availability; }
}
