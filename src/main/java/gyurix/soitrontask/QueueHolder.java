package gyurix.soitrontask;

import lombok.Getter;

import java.util.LinkedList;
import java.util.Queue;

/**
 * The QueueHolder class represents a base class for components that hold a queue and provide logging functionality.
 * It is designed to be extended by classes that require a queue and logging capabilities.
 */
public abstract class QueueHolder implements Runnable {
    protected final Queue<String> queue;

    @Getter
    private final Queue<String> errorLog = new LinkedList<>();

    @Getter
    private final Queue<String> log = new LinkedList<>();

    /**
     * Constructs a QueueHolder object with the specified queue.
     *
     * @param queue The queue to be held by the QueueHolder.
     */
    protected QueueHolder(Queue<String> queue) {
        this.queue = queue;
    }

    /**
     * Logs a message to the console and adds it to the log queue.
     *
     * @param msg The message to be logged.
     */
    public void log(Object msg) {
        System.out.println("[" + Thread.currentThread().getName() + "] " + msg);
        log.add(msg.toString());
    }

    /**
     * Logs an error message to the error console and adds it to the error log queue.
     *
     * @param msg The error message to be logged.
     */
    public void logError(Object msg) {
        System.err.println("[" + Thread.currentThread().getName() + "] " + msg);
        errorLog.add(msg.toString());
    }
}
