package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.jdbc.DataSourceProvider;
import guru.qa.niffler.data.sjdbc.CategoryEntityRowMapper;
import guru.qa.niffler.data.sjdbc.SpendEntityRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.*;
import java.util.List;
import java.util.UUID;

public class SpendRepositorySprintJdbc implements SpendRepository {

    private static final JdbcTemplate spendJdbcTemplate = new JdbcTemplate(
            DataSourceProvider.dataSource(DataBase.SPEND)
    );


    @Override
    public CategoryEntity findByUsernameAndCategory(String username, String category) {
        return null;
    }

    @Override
    public CategoryEntity findByUsernameAndCategoryId(String username, UUID categoryId) {
        return spendJdbcTemplate.queryForObject(
                "SELECT * FROM \"category\" WHERE username = ? AND id = ?",
                CategoryEntityRowMapper.instance,
                username,
                categoryId
        );
    }

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        KeyHolder kh = new GeneratedKeyHolder();
        spendJdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(
                            """ 
                                       INSERT INTO "category" (category, username)
                                       VALUES (?, ?)
                                       ON CONFLICT (category, username) DO UPDATE SET
                                       category = EXCLUDED.category,
                                       username = EXCLUDED.username
                                    """,
                            PreparedStatement.RETURN_GENERATED_KEYS);
                    ps.setString(1, category.getCategory());
                    ps.setString(2, category.getUsername());
                    return ps;
                }, kh
        );
        category.setId((UUID) kh.getKeys().get("id"));
        return category;
    }

    @Override
    public CategoryEntity editCategory(CategoryEntity category) {
        spendJdbcTemplate.update(
                "UPDATE category SET category = ?, username = ? WHERE id = ?",
                category.getCategory(),
                category.getUsername(),
                category.getId()
        );
            return category;
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        spendJdbcTemplate.update(
                "DELETE FROM category WHERE id = ?",
                category.getId()
        );
    }

    @Override
    public SpendEntity createSpend(SpendEntity spend) {
        KeyHolder kh = new GeneratedKeyHolder();
        spendJdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(
                            """ 
                                       INSERT INTO spend (username, spend_date, currency, amount, description, category_id) VALUES (?, ?, ?, ?, ?, ?)
                                    """,
                            PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, spend.getUsername());
            ps.setDate(2, new Date(spend.getSpendDate().getTime()) );
            ps.setString(3, spend.getCurrency().toString());
            ps.setDouble(4, spend.getAmount());
            ps.setString(5, spend.getDescription());
            ps.setObject(6, spend.getCategoryEntity().getId());
                    return ps;
                }, kh
        );
        spend.setId((UUID) kh.getKeys().get("id"));

        return spend;
    }

    @Override
    public SpendEntity editSpend(SpendEntity spend) {
        spendJdbcTemplate.update(
                "UPDATE \"spend\" SET username = ?, spend_date = ?, currency = ?, amount = ?, description = ?, category_id = ? WHERE id = ?",
                spend.getUsername(),
                new Date(spend.getSpendDate().getTime()),
                spend.getCurrency(),
                spend.getAmount(),
                spend.getDescription(),
                spend.getCategoryEntity().getId(),
                spend.getId()
        );
        return spend;
    }

    @Override
    public void removeSpend(SpendEntity spend) {
        spendJdbcTemplate.update(
                "DELETE FROM spend where id = ?",
                spend.getId()
        );
    }

    @Override
    public List<SpendEntity> findAllByUsername(String username) {
        List<SpendEntity> spendEntities = spendJdbcTemplate.query(
                """
                        SELECT * FROM spend WHERE username = ?
                        """,
                SpendEntityRowMapper.instance,
                username
        );
        return spendEntities;
    }
}
