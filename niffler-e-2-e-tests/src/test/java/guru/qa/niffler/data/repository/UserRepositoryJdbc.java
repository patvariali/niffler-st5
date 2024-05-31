package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.Authority;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.jdbc.DataSourceProvider;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryJdbc implements UserRepository {

    private static final DataSource authDataSource = DataSourceProvider.dataSource(DataBase.AUTH);
    private static final DataSource ubDataSource = DataSourceProvider.dataSource(DataBase.USERDATA);
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public UserAuthEntity createUserInAuth(UserAuthEntity user) {
        try (Connection conn = authDataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement userPs = conn.prepareStatement(
                    """
                            INSERT INTO "user" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) VALUES (?, ?, ?, ?, ?, ?)
                            """,
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
                 PreparedStatement authorityPs = conn.prepareStatement(
                         """
                                 INSERT INTO "authority" (user_id, authority) VALUES (?, ?)
                                 """

                 )) {
                userPs.setString(1, user.getUsername());
                userPs.setString(2, pe.encode(user.getPassword()));
                userPs.setBoolean(3, user.getEnabled());
                userPs.setBoolean(4, user.getAccountNonExpired());
                userPs.setBoolean(5, user.getAccountNonLocked());
                userPs.setBoolean(6, user.getCredentialsNonExpired());
                userPs.executeUpdate();

                UUID generatedUserId = null;
                try (ResultSet rs = userPs.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedUserId = UUID.fromString(rs.getString("id"));
                    } else {
                        throw new IllegalArgumentException("Can't access to user by ID");
                    }
                }
                user.setId(generatedUserId);

                for (Authority a : Authority.values()) {
                    authorityPs.setObject(1, generatedUserId);
                    authorityPs.setString(2, a.name());
                    authorityPs.addBatch();
                    authorityPs.clearParameters();
                }

                authorityPs.executeBatch();

                conn.commit();
                return user;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserAuthEntity updateUserInAuth(UserAuthEntity user) {
        try (Connection conn = authDataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement userPs = conn.prepareStatement(
                    """
                            UPDATE "user" SET username = ?, password = ?, enabled = ?, account_non_expired = ?, account_non_locked = ?, credentials_non_expired = ? WHERE id = ?
                            """,
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
                 PreparedStatement authorityPs = conn.prepareStatement(
                         """
                                 UPDATE "authority" SET user_id = ?, authority = ?
                                 """

                 )) {
                userPs.setString(1, user.getUsername());
                userPs.setString(2, pe.encode(user.getPassword()));
                userPs.setBoolean(3, user.getEnabled());
                userPs.setBoolean(4, user.getAccountNonExpired());
                userPs.setBoolean(5, user.getAccountNonLocked());
                userPs.setBoolean(6, user.getCredentialsNonExpired());
                userPs.setObject(7, user.getId());
                userPs.executeUpdate();

                for (Authority a : Authority.values()) {
                    authorityPs.setObject(1, user.getId());
                    authorityPs.setString(2, a.name());
                    authorityPs.addBatch();
                    authorityPs.clearParameters();
                }

                authorityPs.executeBatch();

                conn.commit();
                return user;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserEntity createUserInUserData(UserEntity user) {
        try (Connection conn = ubDataSource.getConnection();
             PreparedStatement userPs = conn.prepareStatement(
                     """
                             INSERT INTO "user" (username, currency, firstname, surname, photo, photo_small) VALUES (?, ?, ?, ?, ?, ?)
                             """,
                     PreparedStatement.RETURN_GENERATED_KEYS
             )) {
            userPs.setString(1, user.getUsername());
            userPs.setString(2, user.getCurrency().name());
            userPs.setString(3, user.getFirstname());
            userPs.setString(4, user.getSurname());
            userPs.setObject(5, user.getPhoto());
            userPs.setObject(6, user.getPhotoSmall());
            userPs.executeUpdate();

            UUID generatedUserId = null;
            try (ResultSet rs = userPs.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedUserId = (UUID) rs.getObject("id");
                } else {
                    throw new IllegalArgumentException("Can't access to user by ID");
                }
            }
            user.setId(generatedUserId);
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserEntity updateUserInUserdata(UserEntity user) {
        try (Connection conn = ubDataSource.getConnection();
             PreparedStatement userPs = conn.prepareStatement(
                     """
                             UPDATE "user" SET username = ?, currency = ?, firstname = ?, surname = ?, photo = ?, photo_small = ? WHERE id = ?
                             """
             )) {
            userPs.setString(1, user.getUsername());
            userPs.setString(2, user.getCurrency().name());
            userPs.setString(3, user.getFirstname());
            userPs.setString(4, user.getSurname());
            userPs.setObject(5, user.getPhoto());
            userPs.setObject(6, user.getPhotoSmall());
            userPs.setObject(7, user.getId());
            userPs.executeUpdate();

            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findUserInUserDataById(UUID id) {
        try (Connection conn = ubDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     """                 
                                 SELECT * FROM \"user\" WHERE id = ?
                             """
             )) {
            ps.setObject(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UserEntity userEntity = new UserEntity();
                    userEntity.setId(UUID.fromString(rs.getString("id")));
                    userEntity.setUsername(rs.getString("username"));
                    userEntity.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                    userEntity.setFirstname(rs.getString("firstname"));
                    userEntity.setSurname(rs.getString("surname"));
                    userEntity.setPhoto(rs.getBytes("photo"));
                    userEntity.setPhotoSmall(rs.getBytes("photo_small"));
                    return Optional.of(userEntity);


                } else {
                    throw new NoSuchElementException("Can't find category by username and category");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
