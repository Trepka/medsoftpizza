package com.medsoft.pizza.database;

import com.medsoft.pizza.models.CustomPizza;
import com.medsoft.pizza.models.Ingredient;
import com.medsoft.pizza.models.MenuPosition;
import com.medsoft.pizza.models.Order;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class OrderDao implements Dao<Order, Integer>{
    private static final Logger LOGGER = Logger.getLogger(OrderDao.class.getName());

    @Override
    public Optional<Order> get(int id){
        return JdbcConnection.getConnection().flatMap(conn-> {
            Optional<Order> order = Optional.empty();
            String sql = "SELECT * FROM orders WHERE id = " + id;
            try(Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)){

                if (resultSet.next()) {
                    String orderStatus = this.orderStatus(id).get();
                    ArrayList<MenuPosition> orderComposition = this.getMenu(id);
                    ArrayList<CustomPizza> customPizzas = this.getOrderCustomPizzas(id);
                    order = Optional.of(new Order(id, orderStatus, orderComposition, customPizzas));

                    LOGGER.log(Level.INFO, "Found {0} in database", order);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, null, e);
            }
            return order;
        });

    }

    @Override
    public Collection<Order> getAll(){
        Collection<Order> orders = new ArrayList<>();
        String sql = "SELECT id FROM orders";

        JdbcConnection.getConnection().ifPresent(conn-> {
            try(Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)){

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String orderStatus = this.orderStatus(id).get();
                    ArrayList<MenuPosition> orderComposition = this.getMenu(id);
                    ArrayList<CustomPizza> customPizzas = this.getOrderCustomPizzas(id);
                    Order order = new Order(id, orderStatus, orderComposition, customPizzas);
                    orders.add(order);

                    LOGGER.log(Level.INFO, "Found {0} in database", order);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, null, e);
            }
        });
        return orders;
    }

    private Optional<String> orderStatus(int orderId) {
        return JdbcConnection.getConnection().flatMap(conn -> {
            String orderStatus = "";
            String sql = "SELECT name FROM order_status os " +
                    "LEFT JOIN orders o ON o.status_id = os.id " +
                    "WHERE o.id = " + orderId;

            try (Statement statement = conn.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    orderStatus += name;
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, null, e);
            }
            return Optional.of(orderStatus);
        });
    }

    private ArrayList<MenuPosition> getMenu(int id) {
        ArrayList<MenuPosition> menuPositions = new ArrayList<>();
        String sql = "SELECT m.id, m.name, m.description FROM menu m " +
                "LEFT JOIN order_composition oc ON oc.menu_id = m.id " +
                "LEFT JOIN orders o ON oc.order_id = o.id " +
                "WHERE o.id = "+ id + " AND m.name != 'custom_pizza'";

        JdbcConnection.getConnection().ifPresent(conn->{
            try(Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()){
                    int positionId = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    MenuPosition menuPosition = new MenuPosition(positionId, name, description);
                    menuPositions.add(menuPosition);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, null, e);
            }
        });
        return menuPositions;
    }

    private ArrayList<CustomPizza> getOrderCustomPizzas(int orderId){
        ArrayList<CustomPizza> customPizzas = new ArrayList<>();
        String sql = "SELECT m.id AS mid, i.id, i.name, i.description FROM ingredients i " +
                "LEFT JOIN custom_pizza cp ON cp.ingredient_id = i.id " +
                "LEFT JOIN menu m ON m.id = cp.menu_id " +
                "LEFT JOIN order_composition oc ON oc.menu_id = m.id " +
                "WHERE m.name = 'custom_pizza' AND oc.order_id = " + orderId;

        JdbcConnection.getConnection().ifPresent(conn->{
            try(Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {
                Map<Integer, ArrayList<Ingredient>> pizzasMap = new HashMap<>();
                while (resultSet.next()) {
                    int pizzaId = resultSet.getInt("mid");
                    int ingredientId = resultSet.getInt("id");
                    String ingredientName = resultSet.getString("name");
                    String ingredientDescription = resultSet.getString("description");
                    Ingredient ingredient = new Ingredient(ingredientId, ingredientName, ingredientDescription);
                    ArrayList<Ingredient> ingredients = pizzasMap.get(pizzaId);
                    if (ingredients != null){
                        ingredients.add(ingredient);
                    } else {
                        pizzasMap.put(pizzaId, new ArrayList<>(List.of(ingredient)));
                    }
                }
                for(Map.Entry<Integer, ArrayList<Ingredient>> pizzas: pizzasMap.entrySet()){
                    customPizzas.add(new CustomPizza(pizzas.getKey(), pizzas.getValue()));
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, null, e);
            }
        });
        return customPizzas;
    }

    @Override
    public Optional<Integer> save(@NotNull Order order){
        ArrayList<MenuPosition> orderMenu = order.getOrderComposition();
        ArrayList<CustomPizza> customPizzas = order.getCustomPizzas();
        ArrayList<Integer> orderMenuPositions = new ArrayList<>();
        if(customPizzas != null){
            for (CustomPizza cp: customPizzas) {
                orderMenuPositions.add(createCustomPizza(cp.getCustomPizzaIngredients()).get());
            }
        }
        for (MenuPosition mp: orderMenu) {
            orderMenuPositions.add(mp.getId());
        }
        return this.createOrderComposition(orderMenuPositions);
    }

    private Optional<Integer> createOrder(){
        String sql = "INSERT INTO orders(status_id) VALUES(?)";

        return JdbcConnection.getConnection().flatMap(conn-> {
            Optional<Integer> generatedId = Optional.empty();
            try(PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, 1);
                int numberOfInsertedRows = statement.executeUpdate();
                if(numberOfInsertedRows > 0) {
                    try (ResultSet resultSet = statement.getGeneratedKeys()){
                        if(resultSet.next()){
                            generatedId = Optional.of(resultSet.getInt(1));
                        }
                    }
                }
                LOGGER.log(Level.INFO, "{0} created successfully? {1} Id={2}",
                        new Object[]{"order", numberOfInsertedRows > 0, generatedId});
            } catch (SQLException e){
                LOGGER.log(Level.SEVERE, null, e);
            }

            return generatedId;
        });
    }

    private Optional<Integer> createOrderComposition(ArrayList<Integer> orderComposition){
        String sql = "INSERT INTO order_composition (order_id, menu_id) VALUES (?, ?)";
        Optional<Integer> orderId = this.createOrder();

        return JdbcConnection.getConnection().flatMap(conn -> {
            for (Integer positionId : orderComposition) {
                try (PreparedStatement statement = conn.prepareStatement(sql, Statement.NO_GENERATED_KEYS)) {
                    statement.setInt(1, orderId.get());
                    statement.setInt(2, positionId);
                    statement.executeUpdate();

                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, null, e);
                }
            }
            return orderId;
        });
    }

    public Optional<Integer> createMenuCustomPosition(){
        String sql = "INSERT INTO menu (name) VALUES ('custom_pizza')";

        return JdbcConnection.getConnection().flatMap(conn-> {
            Optional<Integer> generatedId = Optional.empty();
            try(PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                int numberOfInsertedRows = statement.executeUpdate();
                if(numberOfInsertedRows > 0) {
                    try (ResultSet resultSet = statement.getGeneratedKeys()){
                        if(resultSet.next()){
                            generatedId = Optional.of(resultSet.getInt(1));
                        }
                    }
                }

                LOGGER.log(Level.INFO, "{0} created successfully? {1} Id={2}",
                        new Object[]{"MenuCustomPosition", numberOfInsertedRows > 0, generatedId});
            } catch (SQLException e){
                LOGGER.log(Level.SEVERE, null, e);
            }

            return generatedId;
        });
    }

    private Optional<Integer> createCustomPizza(ArrayList<Ingredient> ingredients){
        Optional<Integer> customPizzaId = this.createMenuCustomPosition();
        String sql = "INSERT INTO custom_pizza (menu_id, ingredient_id) VALUES (?, ?)";

        return JdbcConnection.getConnection().flatMap(conn->{
            for (Ingredient ingredient: ingredients){
                try(PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                    statement.setInt(1, customPizzaId.get());
                    statement.setInt(2, ingredient.getId());
                    statement.executeUpdate();
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, null, e);
                }
            }
            return customPizzaId;
        });
    }

    @Override
    public void update(Order order){
        String message = "The position to be added should not be null";
        Order nonNullOrder = Objects.requireNonNull(order, message);
        String sql = "UPDATE orders SET status_id = (?) WHERE id = (?)" ;

        JdbcConnection.getConnection().ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, getOrderStatusId(nonNullOrder.getOrderStatus()));
                statement.setInt(2, nonNullOrder.getId());
                int numberOfUpdateRows = statement.executeUpdate();

                LOGGER.log(Level.INFO, "Was the order updated successfully? {0}",
                        numberOfUpdateRows > 0);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, null, e);
            }
        });
    }

    private int getOrderStatusId(String name){
        var wrapper = new Object(){ int id; };
        String sql = "SELECT id FROM order_status WHERE name = '" + name + "'";

        JdbcConnection.getConnection().ifPresent(conn -> {
            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)){
                if(resultSet.next()){
                    wrapper.id = resultSet.getInt("id");
                }

            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, null, e);
            }
        });
        return wrapper.id;
    }

    @Override
    public void delete(Order order){
        String message = "The order to be deleted should not be null";
        Order nonNullOrder = Objects.requireNonNull(order, message);
        String sql = "DELETE FROM orders WHERE id = ?";

        JdbcConnection.getConnection().ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, nonNullOrder.getId());
                int numberOfDeletedRows = statement.executeUpdate();

                LOGGER.log(Level.INFO, "Was the order deleted successfully? {0}",
                        numberOfDeletedRows > 0);
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }
}
