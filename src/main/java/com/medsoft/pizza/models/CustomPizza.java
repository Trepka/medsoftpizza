package com.medsoft.pizza.models;

import java.util.ArrayList;

public class CustomPizza {
    private Integer id;
    private ArrayList<Ingredient> customPizzaIngredients;
    public CustomPizza(){}
    public CustomPizza(Integer id, ArrayList<Ingredient> customPizzaIngredients){
        this.id = id;
        this.customPizzaIngredients = customPizzaIngredients;
    }
    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id = id;
    }

    public ArrayList<Ingredient> getCustomPizzaIngredients(){
        return this.customPizzaIngredients;
    }
    public void setOrderComposition(ArrayList<Ingredient> customPizzaIngredients){
        this.customPizzaIngredients = customPizzaIngredients;
    }

    private String printPizzaIngredients(ArrayList<Ingredient> customPizzaIngredients){
        StringBuilder sb = new StringBuilder();
        for (Ingredient mp: customPizzaIngredients) {
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
        return "Custom pizza: " + printPizzaIngredients(this.customPizzaIngredients);
    }
}
