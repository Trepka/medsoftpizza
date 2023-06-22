package com.medsoft.pizza.database;

import com.medsoft.pizza.models.Ingredient;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class IngredientDao implements Dao<Ingredient, Integer> {
    private static final Logger LOGGER = Logger.getLogger(MenuPositionDao.class.getName());

    public Collection<Ingredient> getAll(){
        Collection<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT * FROM ingredients";

        JdbcConnection.getConnection().ifPresent(conn-> {
            try(Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)){

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");

                    Ingredient ingredient = new Ingredient(id, name, description);

                    ingredients.add(ingredient);

                    LOGGER.log(Level.INFO, "Found {0} in database", ingredient);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, null, e);
            }
        });
        return ingredients;
    }

    @Override
    public Optional<Ingredient> get(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Integer> save(Ingredient ingredient){
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(Ingredient ingredient) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Ingredient ingredient) {
        throw new UnsupportedOperationException();
    }
}
