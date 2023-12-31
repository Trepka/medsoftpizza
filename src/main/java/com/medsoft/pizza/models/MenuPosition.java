package com.medsoft.pizza.models;

public class MenuPosition {
    private Integer id;
    private String name;
    private String description;
    public MenuPosition(){}

    public MenuPosition(int id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id = id;
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
    public String toString(){
        return "Position[" + "id=" + id + ", name=" + name + ", description=" + description + "]";
    }
}
