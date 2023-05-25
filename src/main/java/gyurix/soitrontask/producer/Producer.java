package gyurix.soitrontask.producer;

import gyurix.soitrontask.QueueHolder;

import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Scanner;

/**
 * The Producer class represents a producer that reads user input commands and adds them to a queue.
 */
public class Producer extends QueueHolder {
    private final Scanner scanner;

    /**
     * Constructs a Producer object with the specified queue and scanner.
     *
     * @param queue   The queue to which the commands will be added.
     * @param scanner The scanner used for reading user input commands.
     */
    public Producer(Queue<String> queue, Scanner scanner) {
        super(queue);
        this.scanner = scanner;
    }

    /**
     * Runs the producer to continuously read user input commands and add them to the queue.
     * The method terminates when a NoSuchElementException occurs, indicating the end of input.
     * <p>
     * Note: Synchronization of both the queue and the scanner ensures that input commands are executed
     * in the correct order, even with multiple producers. By synchronizing the queue inside the scanner block,
     * thread safety and mutual exclusion are achieved, preventing issues related to command execution order and
     * preserving the desired program behavior.
     */
    public void run() {
        log("Started producer");
        try {
            while (true) {
                log("Enter the next command");
                String command;
                synchronized (scanner) {
                    command = scanner.nextLine();
                    synchronized (queue) {
                        queue.add(command);
                    }
                }
            }
        } catch (NoSuchElementException ignored) {
        }
    }
}