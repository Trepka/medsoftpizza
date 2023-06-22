package com.medsoft.pizza.database;

import com.medsoft.pizza.models.Ingredient;
import com.medsoft.pizza.models.MenuPosition;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
@Repository
public class MenuPositionDao implements Dao<MenuPosition, Integer>{
    private static final Logger LOGGER = Logger.getLogger(MenuPositionDao.class.getName());

    @Override
    public Optional<MenuPosition> get(int id){
        return JdbcConnection.getConnection().flatMap(conn -> {
            Optional<MenuPosition> position = Optional.empty();
            String sql = "SELECT * from menu WHERE id = " + id;

            try (Statement statement = conn.createStatement();
                 ResultSet resultSet= statement.executeQuery(sql)){

                if (resultSet.next()){
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");

                    position = Optional.of(new MenuPosition(id, name, description));

                    LOGGER.log(Level.INFO, "Found {0} in database", position.get());
                }
            } catch (SQLException e){
                LOGGER.log(Level.SEVERE, null, e);
            }

            return position;
        });
    }

    @Override
    public Collection<MenuPosition> getAll(){
        Collection<MenuPosition> menuPositions = new ArrayList<>();
        String sql = "SELECT * FROM menu WHERE name != 'custom_pizza'";

        JdbcConnection.getConnection().ifPresent(conn-> {
            try(Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)){

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");

                    MenuPosition menuPosition = new MenuPosition(id, name, description);

                    menuPositions.add(menuPosition);

                    LOGGER.log(Level.INFO, "Found {0} in database", menuPosition);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, null, e);
            }
        });
        return menuPositions;
    }

    @Override
    public Optional<Integer> save(MenuPosition position){
        String message = "The position to be added should not be null";
        MenuPosition nonNullPosition = Objects.requireNonNull(position, message);
        String sql = "INSERT INTO menu(name, description) VALUES(?, ?)";

        return JdbcConnection.getConnection().flatMap(conn-> {
            Optional<Integer> generatedId = Optional.empty();

            try(PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, nonNullPosition.getName());
                statement.setString(2, nonNullPosition.getDescription());

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

    @Override
    public void update(MenuPosition position){
        String message = "The position to be added should not be null";
        MenuPosition nonNullPosition = Objects.requireNonNull(position, message);
        String sql = "UPDATE menu SET name = ?, description = ? WHERE id = ?";

        JdbcConnection.getConnection().ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, nonNullPosition.getName());
                statement.setString(2, nonNullPosition.getDescription());
                statement.setInt(3, nonNullPosition.getId());

                int numberOfUpdateRows = statement.executeUpdate();

                LOGGER.log(Level.INFO, "Was the MenuPosition updated successfully? {0}",
                        numberOfUpdateRows > 0);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, null, e);
            }
        });
    }

    @Override
    public void delete(MenuPosition position) {
        String message = "The position to be deleted should not be null";
        MenuPosition nonNullPosition = Objects.requireNonNull(position, message);
        String sql = "DELETE FROM menu WHERE id = ?";

        JdbcConnection.getConnection().ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setInt(1, nonNullPosition.getId());

                int numberOfDeletedRows = statement.executeUpdate();

                LOGGER.log(Level.INFO, "Was the MenuPosition deleted successfully? {0}",
                        numberOfDeletedRows > 0);

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }
}
