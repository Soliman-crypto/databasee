package java.repositories.impl;

import models.User;
import repositories.UserRepository;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements UserRepository {

    private final static String SQL_SELECT_ALL =
            "select * from user";


    private final static String SQL_INSERT =
            "insert into accounts(uname, password, email, phone, age) values (?, ?, ?, ?, ?)";


    private static final String SQL_SELECT_BY_AGE = "SELECT * FROM user WHERE age = ?";

    private final DataSource dataSource;

    public UserRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(User model) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, model.getUname());
                statement.setString(2, model.getPassword());
                statement.setString(3, model.getEmail());
                statement.setInt(4, model.getPhone());
                statement.setInt(5, model.getAge());


                int affectedRows = statement.executeUpdate();

                if (affectedRows != 1) {
                    throw new SQLException("Cannot insert user");
                }

                try (ResultSet generatedIds = statement.getGeneratedKeys()){
                    if (generatedIds.next()) {
                        model.setId(generatedIds.getInt("id"));
                    } else {
                        throw new SQLException("Cannot retrieve id");
                    }
                }

            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users= new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            try (ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL)) {
                while (resultSet.next()) {
                    User user = User.builder()
                            .id(resultSet.getInt("id"))
                            .uname(resultSet.getString("first_name"))
                            .password(resultSet.getString("last_name"))
                            .email(resultSet.getString("email"))
                            .phone(resultSet.getInt("phone"))
                            .age(resultSet.getInt("age"))
                            .build();

                    users.add(user);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return users;
    }

    public List<User> findAllByAge(int age) {
        List<User> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_AGE)) {

            statement.setInt(1, age);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    User user = User.builder()
                            .id(resultSet.getInt("id"))
                            .uname(resultSet.getString("first_name"))
                            .password(resultSet.getString("last_name"))
                            .email(resultSet.getString("email"))
                            .phone(resultSet.getInt("phone"))
                            .age(resultSet.getInt("age"))
                            .build();

                    users.add(user);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return users;
    }

    @Override
    public Optional<User> findByEmail(User email) {
        return Optional.empty();
    }
}
