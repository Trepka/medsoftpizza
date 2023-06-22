package com.medsoft.pizza.controller;

import com.medsoft.pizza.database.MenuPositionDao;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.medsoft.pizza.models.MenuPosition;
import org.springframework.web.server.ResponseStatusException;

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
        return menuPositionDao.get(id).orElseThrow(()-> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        ));
    }

    @GetMapping("/menu_position")
    public Collection<MenuPosition> all() {
        return menuPositionDao.getAll();
    }

    @PostMapping("/menu_position")
    Optional<Integer> newMenuPosition(@RequestBody MenuPosition newPosition){
        return menuPositionDao.save(newPosition);
    }

    @PutMapping("/menu_position/{id}")
    MenuPosition replaceMenuPosition(@RequestBody MenuPosition newPosition, @PathVariable int id){
        return menuPositionDao.get(id).map(position -> {
            position.setName(newPosition.getName());
            position.setDescription(newPosition.getDescription());
            menuPositionDao.update(position);
            return newPosition;
        }).orElseGet(()-> {
            menuPositionDao.update(newPosition);
            return newPosition;
        });
    }

    @DeleteMapping("/menu_position/{id}")
    void deleteMenuPosition(@PathVariable int id){
        MenuPosition position = menuPositionDao.get(id).orElseThrow(()-> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        ));
        menuPositionDao.delete(position);
    }
}
