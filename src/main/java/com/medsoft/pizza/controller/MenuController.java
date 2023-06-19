package com.medsoft.pizza.controller;

import com.medsoft.pizza.database.MenuPositionDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.medsoft.pizza.models.MenuPosition;

import java.util.Collection;
import java.util.Optional;

@RestController
public class MenuController {
    private final MenuPositionDao menuPositionDao;

    MenuController(MenuPositionDao menuPositionDao){
        this.menuPositionDao = menuPositionDao;
    }

    @GetMapping("/menu_position/{id}")
    MenuPosition getById(@PathVariable int id){
        return menuPositionDao.get(id).orElseThrow(()-> new MenuNotFoundException(id));
    }

    @GetMapping("/menu_position")
    public ResponseEntity<Collection<MenuPosition>> all() {
        return new ResponseEntity<>(menuPositionDao.getAll(), HttpStatus.OK);
    }

    @PostMapping("/menu_position")
    Optional<Integer> newMenuPosition(@RequestBody MenuPosition newPosition){
        return menuPositionDao.save(newPosition);
    }

    @PutMapping("/menu_position/{id}")
    MenuPosition replaceMenuPosition(@RequestBody MenuPosition newPosition, @PathVariable int id){
        return menuPositionDao.get(id).map(pizza -> {
            pizza.setName(newPosition.getName());
            pizza.setDescription(newPosition.getDescription());
            menuPositionDao.update(pizza);
            return newPosition;
        }).orElseGet(()-> {
            menuPositionDao.update(newPosition);
            return newPosition;
        });
    }

    @DeleteMapping("/menu_position/{id}")
    void deleteMenuPosition(@PathVariable int id){
        MenuPosition pizza = menuPositionDao.get(id).orElseThrow(()-> new MenuNotFoundException(id));
        menuPositionDao.delete(pizza);
    }
}
