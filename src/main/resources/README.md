menu table
```roomsql
CREATE TABLE IF NOT EXISTS menu(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50),
    description TEXT
)
```
ingredients table
```roomsql
CREATE TABLE IF NOT EXISTS ingredients(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT UNIQUE NOT NULL
)
```
order_status table
```roomsql
CREATE TABLE IF NOT EXISTS order_status(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
)
```
custom pizza table
```roomsql
CREATE TABLE IF NOT EXISTS custom_pizza(
    id SERIAL PRIMARY KEY,
    menu_id int NOT NULL,
    ingredient_id INT NOT NULL,
    CONSTRAINT menu_fk FOREIGN KEY (menu_id) REFERENCES menu(id) ON DELETE CASCADE,
    CONSTRAINT ingredient_fk FOREIGN KEY (ingredient_id) REFERENCES ingredients(id)
)
```

orders table
```roomsql
CREATE TABLE IF NOT EXISTS orders(
    id SERIAL PRIMARY KEY,
    status_id INT NOT NULL,
    CONSTRAINT order_status_fk FOREIGN KEY(status_id) REFERENCES order_status(id)
)
```
    
order_composition table
```roomsql
CREATE TABLE IF NOT EXISTS order_composition(
    order_id INT NOT NULL,
    menu_id INT NOT NULL,
    CONSTRAINT order_fk FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT menu_fk FOREIGN KEY (menu_id) REFERENCES menu(id)
)
```