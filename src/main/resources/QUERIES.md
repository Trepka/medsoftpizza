## Insert data to database tables
### menu table
```roomsql
INSERT INTO menu(name, description) VALUES ('Marinara', 'Features tomatoes, garlic, oregano, and extra virgin olive oil');
INSERT INTO menu(name, description) VALUES ('Chicago Pizza', 'Ground beef, sausage, pepperoni, onion, mushrooms, and green peppers, placed underneath the tomato sauce');
INSERT INTO menu(name, description) VALUES ('Sicilian Pizza', 'Sicilian pizza, also known as "sfincione," provides a thick cut of pizza with pillowy dough, a crunchy crust, and robust tomato sauce.');
INSERT INTO menu(name, description) VALUES ('California Pizza', 'Pizza with mustard, ricotta, pate, and red pepper, and by chance, served it to Wolfgang Puck');
INSERT INTO menu(name, description) VALUES ('St. Louis Pizza', 'This pizza features Provel processed cheese, which is a gooey combination of cheddar, Swiss, and provolone cheeses');
```
### ingredients table
```roomsql
INSERT INTO ingredients(name, description) VALUES ('Vegetables', 'carrot, tomatoes, mushrooms, onions, olives, beans, peppers, spinach');
INSERT INTO ingredients(name, description) VALUES ('Nuts', 'almonds, peanuts, pistachios, pine nuts, walnuts');
INSERT INTO ingredients(name, description) VALUES ('Herbs and Spices', 'basil, coriander, garlic, oregano, pepper, rosemary');
INSERT INTO ingredients(name, description) VALUES ('Sea Food', 'anchovies, lobster, shrimps, salmon, squid, tuna, oysters');
INSERT INTO ingredients(name, description) VALUES ('Meats', 'bacon, ham, salami, meatballs, duck, chicken, barbequed meat');
```
### order_status table
```roomsql
INSERT INTO order_status(name) VALUES ('Pending');
INSERT INTO order_status(name) VALUES ('Processing');
INSERT INTO order_status(name) VALUES ('Preparing');
INSERT INTO order_status(name) VALUES ('Ready');
```
### custom_pizza table
```roomsql
INSERT INTO menu(name) VALUES('custom_pizza');
INSERT INTO custom_pizza(menu_id, ingredient_id) VALUES (6, 1);
INSERT INTO custom_pizza(menu_id, ingredient_id) VALUES (6, 3);
INSERT INTO custom_pizza(menu_id, ingredient_id) VALUES (6, 5);
INSERT INTO menu(name) VALUES('custom_pizza');
INSERT INTO custom_pizza(menu_id, ingredient_id) VALUES (7, 1);
INSERT INTO custom_pizza(menu_id, ingredient_id) VALUES (7, 2);
INSERT INTO custom_pizza(menu_id, ingredient_id) VALUES (7, 4);
```
### orders
```roomsql
INSERT INTO orders (status_id) VALUES (1);
INSERT INTO orders (status_id) VALUES (2);
INSERT INTO orders (status_id) VALUES (3);
```
### order_composition
```roomsql
INSERT INTO order_composition(order_id, menu_id) VALUES (1, 4);
INSERT INTO order_composition(order_id, menu_id) VALUES (1, 5);
INSERT INTO order_composition(order_id, menu_id) VALUES (1, 6);
INSERT INTO order_composition(order_id, menu_id) VALUES (1, 7);
INSERT INTO order_composition(order_id, menu_id) VALUES (2, 1);
INSERT INTO order_composition(order_id, menu_id) VALUES (2, 2);
INSERT INTO order_composition(order_id, menu_id) VALUES (3, 2);
INSERT INTO order_composition(order_id, menu_id) VALUES (3, 3);
```