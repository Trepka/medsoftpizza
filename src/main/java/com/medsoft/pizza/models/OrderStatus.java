package com.medsoft.pizza.models;

public class OrderStatus {
    private Integer id;
    private String name;
    public OrderStatus(int id, String name){
        this.id = id;
        this.name = name;
    }
    public int getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return "Status[" + "id = " + id + ", name = " + name + "]";
    }
}
