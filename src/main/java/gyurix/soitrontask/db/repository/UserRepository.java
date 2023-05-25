package gyurix.soitrontask.db.repository;

import gyurix.soitrontask.db.Database;
import gyurix.soitrontask.db.entity.User;
import gyurix.soitrontask.db.mapper.UserMapper;

import java.sql.SQLException;
import java.util.List;

/**
 * The UserRepository class is a specific repository for managing User entities.
 * It extends the base Repository class and provides methods for adding users, deleting all users,
 * retrieving all users, and initializing the User table in the database.
 */
public class UserRepository extends Repository<User, UserMapper> {
    /**
     * Constructs a UserRepository object with the specified database.
     * It sets up the User repository by providing the User class and a UserMapper instance to the base Repository class.
     *
     * @param database The Database object representing the database connection.
     */
    public UserRepository(Database database) {
        super(database, User.class, new UserMapper());
    }

    /**
     * Adds a user to the repository.
     *
     * @param user The User object representing the user to be added.
     * @throws SQLException if a database access error occurs.
     */
    public synchronized void add(User user) throws SQLException {
        database.executeRawQuery("INSERT INTO SUSERS (ID, GUID, NAME) VALUES (?, ?, ?)", null, user);
    }

    /**
     * Deletes all users from the repository.
     *
     * @throws SQLException if a database access error occurs.
     */
    public synchronized void deleteAll() throws SQLException {
        database.executeRawQuery("DELETE FROM SUSERS", null);
    }

    /**
     * Retrieves all users from the repository.
     *
     * @return A list of all User objects in the repository.
     * @throws SQLException if a database access error occurs.
     */
    public synchronized List<User> getAll() throws SQLException {
        return database.executeRawQuery("SELECT * FROM SUSERS", User.class);
    }

    /**
     * Initializes the User table in the database if it doesn't exist.
     * This method is called during repository initialization.
     *
     * @throws SQLException if a database access error occurs.
     */
    protected synchronized void initTable() throws SQLException {
        database.executeRawQuery("CREATE TABLE IF NOT EXISTS SUSERS (" +
                "ID INT PRIMARY KEY, " +
                "GUID VARCHAR(50)," +
                "NAME VARCHAR(50))", null);

    }
}
