package com.medsoft.pizza.models;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Order {
    private Integer id;
    private String orderStatus;
    private ArrayList<MenuPosition> orderComposition;
    private ArrayList<CustomPizza> customPizzas;
    public Order(){}
    public Order(Integer id, String orderStatus, ArrayList<MenuPosition> orderComposition, ArrayList<CustomPizza> customPizzas){
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderComposition = orderComposition;
        this.customPizzas = customPizzas;
    }
    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getOrderStatus(){
        return this.orderStatus;
    }
    public void setOrderStatus(String orderStatus){
        this.orderStatus = orderStatus;
    }
    public ArrayList<MenuPosition> getOrderComposition(){
        return this.orderComposition;
    }
    public void setOrderComposition(ArrayList<MenuPosition> orderComposition){
        this.orderComposition = orderComposition;
    }
    public ArrayList<CustomPizza> getCustomPizzas(){
        return this.customPizzas;
    }
    public void setCustomPizzas(ArrayList<CustomPizza> customPizzas){
        this.customPizzas = customPizzas;
    }
    private String printOrderComposition(ArrayList<MenuPosition> orderComposition){
        StringBuilder sb = new StringBuilder();
        for (MenuPosition mp: orderComposition) {
            sb.append(mp.getName());
            sb.append(", ");
        }
        if(!sb.isEmpty()){
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }
    @Override
    public String toString(){
        return "Order: " + printOrderComposition(this.orderComposition) + ". Order status: " + orderStatus + ".";
    }
}
