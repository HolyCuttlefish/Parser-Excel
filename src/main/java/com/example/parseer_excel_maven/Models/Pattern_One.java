package com.example.parseer_excel_maven.Models;

public class Pattern_One {
    protected int Id;
    protected String Name;
    protected String Description;

    public Pattern_One(){}

    public Pattern_One(int id, String name, String description){
        this.Id = id;
        this.Name = name;
        this.Description = description;
    }

    public int getId() { return this.Id; }
    public String getName() { return this.Name; }
    public String getDescription() { return this.Description; }

    public void getId(int id) { this.Id = id; }
    public void getName(String name) { this.Name = name; }
    public void getDescription(String description) { this.Description = description; }
}
