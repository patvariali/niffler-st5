package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.jdbc.DataSourceProvider;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;
import java.util.UUID;

public class SpendRepositoryJdbc implements SpendRepository{
    private static final DataSource spendDataSource = DataSourceProvider
            .dataSource(DataBase.SPEND);

    @Override
    public CategoryEntity findByUsernameAndCategory(String username, String category) {
        try (Connection conn = spendDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM \"category\" WHERE username = ? AND category = ?"
        )){
            ps.setString(1, username);
            ps.setString(2, category);


            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    CategoryEntity categoryEntity = new CategoryEntity();
                    categoryEntity.setUsername(rs.getString("username"));
                    categoryEntity.setCategory(rs.getString("category"));
                    categoryEntity.setId(UUID.fromString(rs.getString("id")));
                    return categoryEntity;
                } else {
                    throw new NoSuchElementException("Can't find category by username and category");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        try (Connection conn = spendDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO \"category\" (category, username) " +
                             "VALUES (?, ?) " +
                             "ON CONFLICT (category, username) DO UPDATE SET " +
                             "category = EXCLUDED.category, " +
                             "username = EXCLUDED.username",
                     PreparedStatement.RETURN_GENERATED_KEYS
             )) {
            ps.setString(1, category.getCategory());
            ps.setString(2, category.getUsername());
            ps.executeUpdate();

            UUID generatedId = null;
            try (ResultSet resultSet = ps.getGeneratedKeys()){
                if (resultSet.next()) {
                    generatedId = UUID.fromString(resultSet.getString("id"));
                } else {
                    throw new IllegalStateException();
                }
            }
            category.setId(generatedId);
            return category;

        }catch (SQLException e) {
             throw new RuntimeException(e);
        }
    }

    @Override
    public CategoryEntity editCategory(CategoryEntity category) {
        try (Connection conn = spendDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE \"category\" SET category = ?, username = ? WHERE id = ?"
             )) {
            ps.setString(1, category.getCategory());
            ps.setString(2, category.getUsername());
            ps.setObject(3, category.getId());
            ps.executeUpdate();

            return category;

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        try (Connection conn = spendDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM \"category\" WHERE id = ?"
             )) {
            ps.setObject(1, category.getId());
            ps.executeUpdate();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SpendEntity createSpend(SpendEntity spend) {
        try(Connection conn = spendDataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO \"spend\" (username, spend_date, currency, amount, description, category_id) VALUES (?, ?, ?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            )) {
            ps.setString(1, spend.getUsername());
            ps.setDate(2, new Date(spend.getSpendDate().getTime()) );
            ps.setString(3, spend.getCurrency().toString());
            ps.setDouble(4, spend.getAmount());
            ps.setString(5, spend.getDescription());
            ps.setObject(6, spend.getCategoryEntity().getId());

            ps.executeUpdate();

            UUID createdSpendId = null;
            try (ResultSet rs = ps.getGeneratedKeys()){
                if (rs.next()) {
                    createdSpendId = UUID.fromString(rs.getString("id"));
                }
            }
            spend.setId(createdSpendId);
            return spend;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SpendEntity editSpend(SpendEntity spend) {
        try (Connection conn = spendDataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(
                "UPDATE \"spend\" SET username = ?, spend_date = ?, currency = ?, amount = ?, description = ?, category_id = ? WHERE id = ?"
        )) {
            ps.setString(1, spend.getUsername());
            ps.setDate(2, Date.valueOf(String.valueOf(spend.getSpendDate())));
            ps.setObject(3, spend.getCurrency());
            ps.setDouble(4, spend.getAmount());
            ps.setString(5, spend.getDescription());
            ps.setObject(6, spend.getCategoryEntity().getId());
            ps.setObject(7, spend.getId());

            ps.executeUpdate();

            return spend;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeSpend(SpendEntity spend) {
        try (Connection conn = spendDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM \"spend\" where id = ?"
             )) {

            ps.setObject(1, spend.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

