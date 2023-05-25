package gyurix.soitrontask.db;

import gyurix.soitrontask.db.mapper.DTOMapper;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The Database class represents a database connection and provides methods for executing queries
 * and managing entity mappers.
 */
@SuppressWarnings("unchecked")
public final class Database {
    private final Connection connection;

    private final HashMap<Class<?>, DTOMapper<?>> mappers = new HashMap<>();

    /**
     * Constructs a Database object with the specified database URL.
     *
     * @param url The URL string representing the database connection.
     * @throws SQLException if a database access error occurs.
     */
    @SneakyThrows
    public Database(String url) {
        connection = DriverManager.getConnection(url);
    }

    /**
     * Retrieves the query results and maps them to objects using the result handler.
     *
     * @param resultHandler The result handler object for mapping query results.
     * @param statement     The prepared statement object.
     * @param <T>           The type of the result handler.
     * @return A list of objects representing the query results.
     * @throws SQLException if a database access error occurs.
     */
    private static <T> List<T> getQueryResults(DTOMapper<T> resultHandler, PreparedStatement statement) throws SQLException {
        if (resultHandler == null) {
            statement.executeUpdate();
            return null;
        }
        List<T> results = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            results.add(resultHandler.get(resultSet));
        }
        return results;
    }

    /**
     * Closes the database connection
     */
    public void closeConnection() throws SQLException {
        connection.close();
    }

    /**
     * Executes a raw SQL query and returns the results as a list of objects.
     *
     * @param query         The SQL query to be executed.
     * @param resultHandler The Class object representing the result handler for mapping query results.
     * @param params        The optional parameters to be passed to the query.
     * @param <T>           The type of the result handler.
     * @return A list of objects representing the query results, or null if the query is an update statement.
     * @throws SQLException if a database access error occurs.
     */
    public <T> List<T> executeRawQuery(String query, Class<T> resultHandler, Object... params) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            if (!populateQueryParams(statement, params)) {
                return null;
            }
            return (List<T>) getQueryResults(mappers.get(resultHandler), statement);
        }
    }

    /**
     * Populates the query parameters in the prepared statement.
     *
     * @param statement The prepared statement object.
     * @param params    The array of parameter objects.
     * @return True if the parameters are successfully populated, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    private boolean populateQueryParams(PreparedStatement statement, Object[] params) throws SQLException {
        int paramIdx = 1;
        for (Object param : params) {
            DTOMapper<Object> mapper = (DTOMapper<Object>) mappers.get(param.getClass());
            if (mapper == null) {
                System.err.println("Error, mapper for " + param.getClass().getName() + " was not found");
                return false;
            }
            paramIdx += mapper.add(statement, paramIdx, param);
        }
        return true;
    }

    /**
     * Registers a mapper for a specific entity class.
     *
     * @param cl     The Class object representing the entity class.
     * @param mapper The mapper object responsible for mapping the entity class.
     * @param <T>    The type of the entity class.
     */
    public <T> void registerMapper(Class<T> cl, DTOMapper<T> mapper) {
        mappers.put(cl, mapper);
    }
}
