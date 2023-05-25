package gyurix.soitrontask.db.repository;

import gyurix.soitrontask.db.Database;
import gyurix.soitrontask.db.entity.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for UserRepository class.
 */
public class UserRepositoryTest {
    private Database database;

    private UserRepository userRepository;

    /**
     * Set up the test environment before each test case.
     *
     * @throws SQLException if a database access error occurs.
     */
    @Before
    public void setup() throws SQLException {
        database = new Database("jdbc:h2:mem:testdb");
        userRepository = new UserRepository(database);
    }

    /**
     * Clean up the test environment after each test case.
     *
     * @throws SQLException if a database access error occurs.
     */
    @After
    public void tearDown() throws SQLException {
        database.closeConnection(); // Clean up the database connection after each test
    }

    /**
     * Test the add() and getAll() methods of UserRepository.
     *
     * @throws SQLException if a database access error occurs.
     */
    @Test
    public void testAddAndGetAll() throws SQLException {
        User user1 = new User(1, "a1", "Robert");
        User user2 = new User(2, "a2", "Martin");

        userRepository.add(user1);
        userRepository.add(user2);

        List<User> users = userRepository.getAll();

        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    /**
     * Test the deleteAll() method of UserRepository.
     *
     * @throws SQLException if a database access error occurs.
     */
    @Test
    public void testDeleteAll() throws SQLException {
        User user1 = new User(1, "a1", "Robert");
        User user2 = new User(2, "a2", "Martin");

        userRepository.add(user1);
        userRepository.add(user2);

        userRepository.deleteAll();

        List<User> users = userRepository.getAll();
        assertTrue(users.isEmpty());
    }
}
