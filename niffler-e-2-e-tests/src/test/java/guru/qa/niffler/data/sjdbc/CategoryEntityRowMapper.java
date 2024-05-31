package guru.qa.niffler.data.sjdbc;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;


public class CategoryEntityRowMapper implements RowMapper<CategoryEntity> {
    private CategoryEntityRowMapper() {}
    public static final CategoryEntityRowMapper instance = new CategoryEntityRowMapper();

    @Override
    public CategoryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setUsername(rs.getString("username"));
        categoryEntity.setCategory(rs.getString("category"));
        categoryEntity.setId(UUID.fromString(rs.getString("id")));

        return categoryEntity;
    }
}
