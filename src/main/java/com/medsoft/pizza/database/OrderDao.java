package com.medsoft.pizza.database;

import com.medsoft.pizza.models.MenuPosition;
import com.medsoft.pizza.models.Order;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderDao implements Dao<Order, Integer>{
    private static final Logger LOGGER = Logger.getLogger(OrderDao.class.getName());
    private final Optional<Connection> connection;
    public OrderDao(){
        this.connection = JdbcConnection.getConnection();
    }

    @Override
    public Optional<Order> get(int id){
        return connection.flatMap(conn -> {
            Optional<Order> order = Optional.empty();
            String sql = "SELECT name FROM order_status os\n" +
                    "LEFT JOIN orders o ON o.status_id = os.id\n" +
                    "WHERE o.id = " + id;

            try (Statement statement = conn.createStatement();
                 ResultSet resultSet= statement.executeQuery(sql)){

                if (resultSet.next()){
                    String orderStatus = resultSet.getString("status");
                    ArrayList<MenuPosition> orderComposition = this.getOrderComposition(id);

                    order = Optional.of(new Order(id, orderStatus, orderComposition));

                    LOGGER.log(Level.INFO, "Found {0} in database", order.get());

                }
            } catch (SQLException e){
                LOGGER.log(Level.SEVERE, null, e);
            }

            return order;
        });
    }
    @Override
    public Collection<Order> getAll(){
        Collection<Order> orders = new ArrayList<>();
        String sql = "SELECT id FROM orders";

        connection.ifPresent(conn-> {
            try(Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)){

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String orderStatus = this.orderStatus(id);
                    ArrayList<MenuPosition> orderComposition = this.getOrderComposition(id);

                    Order order = new Order(id, orderStatus, orderComposition);

                    orders.add(order);

                    LOGGER.log(Level.INFO, "Found {0} in database", order);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, null, e);
            }
        });
        return orders;
    }
    @Override
    public Optional<Integer> save(Order order){
        String message = "The pizza to be added should not be null";
        Order nonNullOrder = Objects.requireNonNull(order, message);
        String sql = "INSERT INTO " + "menu(name, description) " + "VALUES(?, ?)";

        return connection.flatMap(conn-> {
            Optional<Integer> generatedId = Optional.empty();

            try(PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, nonNullOrder.getName());
                statement.setString(2, nonNullOrder.getDescription());

                int numberOfInsertedRows = statement.executeUpdate();

                if(numberOfInsertedRows > 0) {
                    try (ResultSet resultSet = statement.getGeneratedKeys()){
                        if(resultSet.next()){
                            generatedId = Optional.of(resultSet.getInt(1));
                        }
                    }
                }

                LOGGER.log(Level.INFO, "{0} created successfully? {1}",
                        new Object[]{nonNullPosition, numberOfInsertedRows > 0});
            } catch (SQLException e){
                LOGGER.log(Level.SEVERE, null, e);
            }

            return generatedId;
        });
    }
    private @NotNull ArrayList<MenuPosition> getOrderComposition(int orderId){
        ArrayList<MenuPosition> orderComposition = new ArrayList<>();
        String sql = "SELECT * FROM menu m\n" +
                "LEFT JOIN order_composition oc ON oc.menu_id = m.id\n" +
                "WHERE oc.order_id = " + orderId;

        connection.ifPresent(conn-> {
            try(Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)){

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");

                    MenuPosition menuPosition = new MenuPosition(id, name, description);

                    orderComposition.add(menuPosition);

                    LOGGER.log(Level.INFO, "Found {0} in database", menuPosition);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, null, e);
            }
        });
        return orderComposition;
    }

    private @NotNull String orderStatus(int orderId) {
        var wrapper = new Object(){ String orderStatus = ""; };
        String sql = "SELECT name FROM order_status os\n" +
                "LEFT JOIN orders o ON o.status_id = os.id\n" +
                "WHERE o.id = " + orderId;
        connection.ifPresent(conn -> {
            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    wrapper.orderStatus += name;

                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, null, e);
            }

        });
        return wrapper.orderStatus;
    }
}
