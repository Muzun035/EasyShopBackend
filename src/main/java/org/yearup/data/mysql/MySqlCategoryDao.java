package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories()
    {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT category_id, name, description FROM categories";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int categoryId = resultSet.getInt("category_id");
                String categoryName = resultSet.getString("name");
                String description = resultSet.getString("description");
                Category category = new Category(categoryId, categoryName, description);
                categories.add(category);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving categories", e);
        }
        return categories;
    }

    @Override
    public Category getById(int categoryId)
    {
        String sql = "SELECT category_id, name, description FROM categories WHERE category_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, categoryId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String categoryName = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    return new Category(categoryId, categoryName, description);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieved the category by ID", e);
        }
        // get category by id
        return null;
    }

    @Override
    public Category create(Category category)
    {
        // create a new category
        String sql = "INSERT INTO categories(name, description) VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        return getById(generatedId);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error created the category", e);
        }
        return null;
    }

    @Override
    public void update(int categoryId, Category category)
    {
        // update category
    }

    @Override
    public void delete(int categoryId)
    {
        // delete category
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
