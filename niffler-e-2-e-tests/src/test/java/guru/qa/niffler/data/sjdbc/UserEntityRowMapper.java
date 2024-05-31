package guru.qa.niffler.data.sjdbc;

import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class UserEntityRowMapper implements RowMapper<UserEntity> {
    private UserEntityRowMapper() {}
    public static final UserEntityRowMapper instance = new UserEntityRowMapper();

    @Override
    public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(rs.getString("username"));
        userEntity.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
        return userEntity;
    }
}
