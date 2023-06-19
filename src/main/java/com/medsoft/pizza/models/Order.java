package com.medsoft.pizza.models;

import java.util.ArrayList;

public class Order {
    private Integer id;
    private String orderStatus;
    private ArrayList<MenuPosition> orderComposition;
    public Order(Integer id, String orderStatus, ArrayList<MenuPosition> orderComposition){
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderComposition = orderComposition;
    }
    public int getId(){
        return this.id;
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

    @Override
    public String toString(){
        return "Order: " + ", order status: " + orderStatus;
    }
}
