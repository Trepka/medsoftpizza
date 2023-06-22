package com.medsoft.pizza.controller;

import com.medsoft.pizza.database.IngredientDao;
import com.medsoft.pizza.models.Ingredient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class IngredientController {
    private final IngredientDao ingredientDao;

    IngredientController(IngredientDao ingredientDao) {
        this.ingredientDao = ingredientDao;
    }

    @GetMapping("/ingredient")
    public Collection<Ingredient> all() {
        return ingredientDao.getAll();
    }
}