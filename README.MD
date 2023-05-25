# SoitronTask

**A multi-threaded, consumer-producer based command processing command line application**

This Java 19 command line application demonstrates the processing of commands from a FIFO (First-In-First-Out) queue
using the Producer-Consumer pattern.

## Supported commands:

- **Add (id, guid, name)**: Adds a user to the database
- **Help**: Lists the available commands
- **PrintAll**: Prints all users to the standard output
- **DeleteAll**: Deletes all users from the database

## Implementation Details

The program is implemented using Java 19.
It follows OOP (Object-Oriented Programming) principles and utilizes concurrent programming concepts.

The application consists of the following main parts:

- **CommandExecutor**: Represents the consumer that processes the commands from the queue.
- **Database**: Provides the database connection and executes SQL queries.
- **Producer**: Represents the producer that adds commands to the queue.
- **UserMapper**: Handles the mapping of user entities between the database and DTOs (Data Transfer Objects).
- **UserRepository**: Manages the storage and retrieval of user entities in the database.

## Automated tests

### ProducerConsumerIntegrationTest

- **testLogOutputMultiProducerSingleConsumer()**:
  This test case focuses on the log output of the producer-consumer system.
  It tests different configurations of input multipliers, consumer counts, and producer counts.
  The test simulates the interaction between producers and consumers and verifies the expected log output.

- **testNoErrorsMultiProducerMultiConsumerRandomUserId()**:
  This test case ensures that there are no errors in the producer-consumer system when using multiple producers,
  multiple consumers, and random user IDs.
  It tests various combinations of input multipliers, consumer counts,
  and producer counts to verify the system's robustness and error-free execution.

### UserMapperTest

- **testAdd()**:
  This test case validates the functionality of the add() method in the UserMapper class.
  It verifies that the method correctly adds a user to the database by mocking a PreparedStatement and asserting the expected
  values.

### UserRepositoryTest

- **testAddAndGetAll()**:
  This test case ensures the correctness of the add() and getAll() methods in the UserRepository class. It adds two users to the
  database and then retrieves all users.
  It asserts the size of the user list and checks if the expected users are present.

- **testDeleteAll():**
  This test case verifies the functionality of the deleteAll() method in the UserRepository class.
  It adds two users to the database, deletes all users, and then retrieves all users. It asserts that the user list is empty.


