package com.medsoft.pizza.controller;

public class MenuNotFoundException extends RuntimeException{
    MenuNotFoundException(int id) {
        super("Could not find employee " + id);
    }
}
