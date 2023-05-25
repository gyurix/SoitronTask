package gyurix.soitrontask.db.mapper;

import gyurix.soitrontask.db.entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The UserMapper class is responsible for mapping User entities between the database and DTOs (Data Transfer Objects).
 * It implements the DTOMapper interface for adding User objects to prepared statements and extracting User objects from result
 * sets.
 */
public class UserMapper implements DTOMapper<User> {

    /**
     * Adds a User object to a prepared statement at the specified index.
     *
     * @param statement The prepared statement to add the User object to.
     * @param index     The index at which to add the User object.
     * @param user      The User object to be added.
     * @return The updated index value after adding the User object.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public int add(PreparedStatement statement, int index, User user) throws SQLException {
        statement.setInt(index, user.getId());
        statement.setString(index + 1, user.getGuid());
        statement.setString(index + 2, user.getName());
        return 3;
    }

    /**
     * Extracts a User object from a result set.
     *
     * @param resultSet The result set containing the data to extract the User object from.
     * @return The User object extracted from the result set.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public User get(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt(1));
        user.setGuid(resultSet.getString(2));
        user.setName(resultSet.getString(3));
        return user;
    }
}
