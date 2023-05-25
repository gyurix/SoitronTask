package gyurix.soitrontask.db.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The User class represents a user entity in the system.
 * It encapsulates the properties of a user, such as ID, GUID, and name.
 */
@Data
@NoArgsConstructor
public class User {

    /**
     * The GUID (Globally Unique Identifier) of the user.
     */
    private String guid;

    /**
     * The ID of the user.
     */
    private int id;

    /**
     * The name of the user.
     */
    private String name;

    /**
     * Constructs a new User object with the specified ID, GUID, and name.
     *
     * @param id   The ID of the user.
     * @param guid The GUID of the user.
     * @param name The name of the user.
     */
    public User(int id, String guid, String name) {
        this.id = id;
        this.guid = guid;
        this.name = name;
    }

    /**
     * Constructs a new User object by parsing a string representation of user properties.
     * The string should be in the format: "id, guid, name".
     *
     * @param params The string representation of user properties.
     */
    public User(String params) {
        String[] args = params.split(", *");
        id = Integer.parseInt(args[0]);
        guid = args[1].replace("\"", "");
        name = args[2].replace("\"", "");
    }
}
