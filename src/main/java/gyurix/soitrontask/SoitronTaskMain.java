package gyurix.soitrontask;

import gyurix.soitrontask.consumer.CommandExecutor;
import gyurix.soitrontask.db.Database;
import gyurix.soitrontask.db.repository.UserRepository;
import gyurix.soitrontask.producer.Producer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * The main class of the Soitron Task application.
 * It sets up the necessary components and starts the producer and consumer threads.
 */
public class SoitronTaskMain {
    /**
     * The entry point of the Soitron Task application.
     * It initializes the command queue, the database, and the user repository.
     * Then, it creates instances of the producer and consumer.
     * Finally, it starts the producer and consumer threads.
     *
     * @param args The command-line arguments (not used).
     */
    public static void main(String[] args) {
        Queue<String> commandQueue = new LinkedList<>();
        Database database = new Database("jdbc:h2:mem:mydatabase");
        UserRepository userRepository = new UserRepository(database);

        Producer producer = new Producer(commandQueue, new Scanner(System.in));
        CommandExecutor commandExecutor = new CommandExecutor(commandQueue, userRepository);

        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(commandExecutor);

        producerThread.start();
        consumerThread.start();
    }
}
