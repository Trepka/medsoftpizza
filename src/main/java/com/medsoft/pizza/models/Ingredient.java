package com.medsoft.pizza.models;

public class Ingredient {
    private Integer id;
    private String name;
    private String description;

    public Ingredient(int id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId(){
        return this.id;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getDescription(){
        return this.description;
    }

    @Override
    public String toString() {
        return "Ingredient[" + "id = " + id + ", name = " + name + ", description = " + description + "]";
    }
}
