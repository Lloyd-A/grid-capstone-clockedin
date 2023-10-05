package com.clockedIn.userservice.DAO;


import com.clockedIn.userservice.User;
import com.clockedIn.userservice.UserRole;
import util.ConnectionCreationAndUsage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class UserDAO <T extends User>{

    public Optional<T> getUserById(UUID userId) {
        //setDBParameters();

        String getUserQuery = "SELECT * FROM users WHERE user_id = ?";
        Function<ResultSet, T> mapper = userMapper();

        T user = ConnectionCreationAndUsage.findOne(getUserQuery, mapper, userId);

        return Optional.ofNullable(user);
    }

    public Optional<T> getUserByUniversityId(Long universityId) {
        //setDBParameters();

        String getUserQuery = "SELECT * FROM users WHERE university_id = ?";
        Function<ResultSet, T> mapper = userMapper();

        T user = ConnectionCreationAndUsage.findOne(getUserQuery, mapper, universityId);

        return Optional.ofNullable(user);
    }

    public Iterable<T> getAllUsers() {
        //setDBParameters();
        String getAllUsersQuery = "SELECT * FROM users";
        Function<ResultSet, T> mapper = userMapper();

        List<T> users = ConnectionCreationAndUsage.findMany(getAllUsersQuery, mapper);

        return users;
    }

    public void createUser(User user) {
        //setDBParameters();
        String insertUserQuery = "INSERT INTO users VALUES (?,?,?,?,?,?,?)";
        ConnectionCreationAndUsage.execute(insertUserQuery, user.getUserID(),user.getUniversityID(), user.getFirstName(),
                user.getLastName(), user.getUserRole(), user.getPhoneNum(), user.getEmail());

    }

    public void updateUser(User user) {
        //setDBParameters();
        String updateUserQuery = "UPDATE users SET first_name=?, last_name=?, user_role=?, phone_num=?, email=? " +
                "WHERE user_id=?";
        ConnectionCreationAndUsage.execute(updateUserQuery, user.getFirstName(), user.getLastName(), user.getUserRole(),
                user.getPhoneNum(), user.getEmail(), user.getUserID());

    }

    public void deleteUser(UUID userId) {
        //setDBParameters();
        String deleteUserQuery = "DELETE FROM users WHERE user_id=?";
        ConnectionCreationAndUsage.execute(deleteUserQuery, userId);

    }

    /**
     * Provides a function that maps the data from a resultSet object to the constructor of that object and
     * instantiates it.
     * @return Function that maps data from ResultSet object to User constructor and creates a User object.
     * */
    protected Function<ResultSet, T> userMapper(){
        return resultSet -> {
            T mappedUser;
            try{
                mappedUser = (T) T.builder().userID((UUID)resultSet.getObject("user_id"))
                        .universityID(resultSet.getLong("university_id"))
                        .firstName(resultSet.getString("first_name"))
                        .lastName(resultSet.getString("last_name"))
                        .userRole((UserRole) resultSet.getObject("user_role"))
                        .phoneNum(resultSet.getString("phone_num"))
                        .email(resultSet.getString("email"))
                        .build();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return mappedUser;
        };
    }
    /*protected void setDBParameters(){
        ConnectionCreationAndUsage
                .setUpDatabaseConnnection("jdbc:postgresql://localhost:5432/postgres", "myuser", "mypassword");
    }*/
}
