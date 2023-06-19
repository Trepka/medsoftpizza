package com.medsoft.pizza;

import com.medsoft.pizza.database.MenuPositionDao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PizzaApplication {

	public static void main(String[] args) {

		SpringApplication.run(PizzaApplication.class, args);

	}

	@Bean
	public MenuPositionDao pizzaMapper(){
		return new MenuPositionDao();
	}
}
