package gyurix.soitrontask.db.mapper;

import gyurix.soitrontask.db.entity.User;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test for UserMapper class.
 */
public class UserMapperTest {

    /**
     * Test the add() method of UserMapper.
     *
     * @throws SQLException if a database access error occurs.
     */
    @Test
    public void testAdd() throws SQLException {
        UserMapper userMapper = new UserMapper();
        User user = new User(1, "a1", "Robert");

        PreparedStatement statement = mock(PreparedStatement.class);
        int index = 1;

        assertEquals(3, userMapper.add(statement, index, user));
        verify(statement).setInt(1, 1);
        verify(statement).setString(2, "a1");
        verify(statement).setString(3, "Robert");
    }

    /**
     * Test the get() method of UserMapper.
     *
     * @throws SQLException if a database access error occurs.
     */
    @Test
    public void testGet() throws SQLException {
        UserMapper userMapper = new UserMapper();
        User expectedUser = new User(1, "a1", "Robert");

        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getInt(1)).thenReturn(expectedUser.getId());
        when(resultSet.getString(2)).thenReturn(expectedUser.getGuid());
        when(resultSet.getString(3)).thenReturn(expectedUser.getName());

        User actualUser = userMapper.get(resultSet);

        assertEquals(expectedUser, actualUser);
    }
}
