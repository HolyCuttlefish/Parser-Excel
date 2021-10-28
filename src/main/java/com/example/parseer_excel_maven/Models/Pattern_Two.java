package com.example.parseer_excel_maven.Models;

public class Pattern_Two extends Pattern_One {
    protected Long Value;

    public Pattern_Two(){}

    public Pattern_Two(int id, String name, String description, long value){
        this.Id = id;
        this.Name = name;
        this.Description = description;
        this.Value = value;
    }

    public long getValue(){ return this.Value; }

    public void setValue(long value){ this.Value = value; }
}
