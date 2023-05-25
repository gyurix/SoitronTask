package gyurix.soitrontask.db.repository;

import gyurix.soitrontask.db.Database;
import gyurix.soitrontask.db.mapper.DTOMapper;
import lombok.SneakyThrows;

import java.sql.SQLException;
import java.util.List;

/**
 * The Repository class serves as a base class for specific entity repositories.
 * It provides common functionality for interacting with the database, such as adding entities, deleting all entities,
 * retrieving all entities, and initializing the database table.
 *
 * @param <T> The type of entity handled by the repository.
 * @param <M> The type of the mapper used to map entities between the database and DTOs.
 */
public abstract class Repository<T, M extends DTOMapper<T>> {
    protected final Database database;

    /**
     * Constructs a Repository object with the specified database, entity type, and mapper.
     *
     * @param database   The Database object representing the database connection.
     * @param entityType The class object representing the type of entity handled by the repository.
     * @param mapper     The mapper used to map entities between the database and DTOs.
     * @throws SQLException if a database access error occurs.
     */
    @SneakyThrows
    protected Repository(Database database, Class<T> entityType, M mapper) {
        this.database = database;
        database.registerMapper(entityType, mapper);
        initTable();
    }

    /**
     * Adds an entity to the repository.
     *
     * @param entity The entity to be added.
     * @throws SQLException if a database access error occurs.
     */
    public abstract void add(T entity) throws SQLException;

    /**
     * Deletes all entities from the repository.
     *
     * @throws SQLException if a database access error occurs.
     */
    public abstract void deleteAll() throws SQLException;

    /**
     * Retrieves all entities from the repository.
     *
     * @return A list of all entities in the repository.
     * @throws SQLException if a database access error occurs.
     */
    public abstract List<T> getAll() throws SQLException;

    /**
     * Initializes the database table for the entity.
     * This method is called during repository initialization.
     *
     * @throws SQLException if a database access error occurs.
     */
    protected abstract void initTable() throws SQLException;
}
