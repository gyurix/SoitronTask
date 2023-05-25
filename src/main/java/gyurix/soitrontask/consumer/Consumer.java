package gyurix.soitrontask.consumer;

import gyurix.soitrontask.QueueHolder;

import java.util.Queue;

/**
 * The Consumer class represents an abstract consumer that processes items from a shared queue.
 * It provides common functionality and state for concrete consumer implementations.
 *
 * @param <R> The type of the repository associated with the consumer.
 */
public abstract class Consumer<R> extends QueueHolder {

    /**
     * The repository associated with the consumer.
     */
    protected R repository;

    /**
     * A flag indicating whether the consumer is running or not.
     */
    protected boolean running;

    /**
     * Constructs a new Consumer with the specified queue and repository.
     *
     * @param queue      The queue that stores the items to be processed by the consumer.
     * @param repository The repository associated with the consumer.
     */
    protected Consumer(Queue<String> queue, R repository) {
        super(queue);
        this.repository = repository;
        running = true;
    }

    /**
     * Stops the consumer.
     * This method sets the running flag to false, indicating that the consumer should stop processing items.
     */
    public void stop() {
        running = false;
    }
}
