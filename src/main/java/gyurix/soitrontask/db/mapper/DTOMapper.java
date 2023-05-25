package gyurix.soitrontask.db.mapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The DTOMapper interface defines the contract for mapping between database entities and DTOs (Data Transfer Objects).
 * It provides methods for adding DTO objects to a prepared statement and extracting DTO objects from a result set.
 *
 * @param <T> The type of the DTO object.
 */
public interface DTOMapper<T> {

    /**
     * Adds a DTO object to a prepared statement at the specified index.
     *
     * @param statement The prepared statement to add the DTO object to.
     * @param index     The index at which to add the DTO object.
     * @param object    The DTO object to be added.
     * @return The updated index value after adding the DTO object.
     * @throws SQLException if a database access error occurs.
     */
    int add(PreparedStatement statement, int index, T object) throws SQLException;

    /**
     * Extracts a DTO object from a result set.
     *
     * @param resultSet The result set containing the data to extract the DTO object from.
     * @return The DTO object extracted from the result set.
     * @throws SQLException if a database access error occurs.
     */
    T get(ResultSet resultSet) throws SQLException;
}
