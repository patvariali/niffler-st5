package guru.qa.niffler.data.sjdbc;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.repository.SpendRepositorySprintJdbc;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;


public class SpendEntityRowMapper implements RowMapper<SpendEntity> {
    private SpendEntityRowMapper() {}
    public static final SpendRepositorySprintJdbc spendRepositoryJdbc = new SpendRepositorySprintJdbc();
    public static final SpendEntityRowMapper instance = new SpendEntityRowMapper();

    @Override
    public SpendEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        CategoryEntity categoryEntity = spendRepositoryJdbc.findByUsernameAndCategoryId(
                rs.getString("username"),
                (UUID) rs.getObject("category_id")
        );

        SpendEntity spendEntity = new SpendEntity();
        spendEntity.setId((UUID) rs.getObject("id"));
        spendEntity.setUsername(rs.getString("username"));
        spendEntity.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
        spendEntity.setSpendDate(new java.util.Date(((Date)rs.getObject("spend_date")).getTime()));
        spendEntity.setAmount(rs.getDouble("amount"));
        spendEntity.setDescription(rs.getString("description"));
        spendEntity.setCategoryEntity(categoryEntity);

        return spendEntity;
    }
}
