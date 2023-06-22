package com.medsoft.pizza.controller;

import com.medsoft.pizza.models.Order;
import com.medsoft.pizza.database.OrderDao;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Optional;

@RestController
public class OrderController {
    private final OrderDao orderDao;

    OrderController(OrderDao orderDao){
        this.orderDao = orderDao;
    }

    @GetMapping("/orders/{id}")
    Order getById(@PathVariable int id){
        return orderDao.get(id).orElseThrow(()-> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        ));
    }

    @GetMapping("/orders")
    public Collection<Order> all() {
        return orderDao.getAll();
    }

    @PostMapping("/orders")
    Optional<Integer> newOrder(@RequestBody Order newOrder){
        return orderDao.save(newOrder);
    }

    @PutMapping("/orders/{id}")
    Order replaceMenuPosition(@RequestBody Order newOrder, @PathVariable int id){
        return orderDao.get(id).map(order -> {
            order.setOrderStatus(newOrder.getOrderStatus());
            order.setOrderComposition(newOrder.getOrderComposition());
            orderDao.update(order);
            return newOrder;
        }).orElseGet(()-> {
            orderDao.update(newOrder);
            return newOrder;
        });
    }

    @DeleteMapping("/orders/{id}")
    void deleteOrder(@PathVariable int id){
        Order order = orderDao.get(id).orElseThrow(()-> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        ));
        orderDao.delete(order);
    }
}
