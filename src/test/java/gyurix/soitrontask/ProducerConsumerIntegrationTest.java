package gyurix.soitrontask;

import gyurix.soitrontask.consumer.CommandExecutor;
import gyurix.soitrontask.db.Database;
import gyurix.soitrontask.db.repository.UserRepository;
import gyurix.soitrontask.producer.Producer;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

/**
 * This test class validates the functionality of the producer-consumer system
 * by simulating the interaction between producers and consumers and verifying the expected output.
 * <p>
 * It tests various configurations of input multipliers, consumer counts, and producer counts
 * to ensure the system handles concurrent execution correctly.
 */
public class ProducerConsumerIntegrationTest {
    /**
     * Queue to hold commands.
     */
    private static final Queue<String> commandQueue = new LinkedList<>();

    /**
     * Database instance.
     */
    private static final Database database = new Database("jdbc:h2:mem:mydatabase");

    /**
     * Random number generator.
     */
    private static final Random random = new Random();

    /**
     * Thread pool for executing tasks.
     */
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(20);

    /**
     * User repository for interacting with the database.
     */
    private static final UserRepository userRepository = new UserRepository(database);

    /**
     * Gets the test parameters and logs them.
     *
     * @param inputMultiplier The input multiplier.
     * @param consumerCount   The number of consumers.
     * @param producerCount   The number of producers.
     * @return The formatted test parameters.
     */
    private static String getAndLogTestParams(int inputMultiplier, int consumerCount, int producerCount) {
        String params = inputMultiplier + "x input, " + producerCount + " producers, " + consumerCount + " consumers";
        System.out.println("Testing with " + params + " ...");
        return params;
    }

    /**
     * Simulates the execution of producers and consumers and collects the sorted logs.
     *
     * @param consumerCount The number of consumers.
     * @param producerCount The number of producers.
     * @param input         The input commands for producers.
     * @param errorLogs     Flag indicating whether to collect error logs or normal logs.
     * @return The sorted log lines.
     * @throws InterruptedException If the thread sleep is interrupted.
     */
    private static List<String> simulateAndCollectSortedLogs(
            int consumerCount,
            int producerCount,
            Scanner input,
            boolean errorLogs) throws InterruptedException {
        List<CommandExecutor> consumers = new ArrayList<>();
        List<Producer> producers = new ArrayList<>();

        // Create and start consumers
        for (int i = 0; i < consumerCount; i++) {
            consumers.add(new CommandExecutor(commandQueue, userRepository));
        }

        // Create and start producers
        for (int i = 0; i < producerCount; i++) {
            producers.add(new Producer(commandQueue, input));
        }

        // Submit consumers and producers to the thread pool for execution
        for (CommandExecutor consumer : consumers) {
            threadPool.submit(consumer);
        }

        for (Producer producer : producers) {
            threadPool.submit(producer);
        }

        // Wait for a short duration to allow execution
        Thread.sleep(50);

        // Stop consumers
        for (CommandExecutor consumer : consumers) {
            consumer.stop();
        }

        // Collect logs from consumers
        List<String> logLines = new ArrayList<>();
        for (CommandExecutor consumer : consumers) {
            logLines.addAll(errorLogs ? consumer.getErrorLog() : consumer.getLog());
        }

        // Sort the log lines
        Collections.sort(logLines);
        return logLines;
    }

    /**
     * Get the expected output log lines sorted based on the number of consumers and input multiplier.
     *
     * @param consumers       The number of consumers.
     * @param inputMultiplier The input multiplier.
     * @return The expected output log lines.
     */
    private List<String> getExpectedOutputSorted(int consumers, int inputMultiplier) {
        List<String> expectedOutput = new ArrayList<>();
        for (int i = 0; i < inputMultiplier; ++i) {
            expectedOutput.add("Added user User(guid=a1, id=1, name=Robert)");
            expectedOutput.add("Added user User(guid=a2, id=2, name=Martin)");
            expectedOutput.add("Deleted all users");
            expectedOutput.add("User(guid=a1, id=1, name=Robert)");
            expectedOutput.add("User(guid=a2, id=2, name=Martin)");
            expectedOutput.add("Users:");
            expectedOutput.add("Users:");
        }
        for (int i = 0; i < consumers; ++i) {
            expectedOutput.add("Started consumer");
        }
        Collections.sort(expectedOutput);
        return expectedOutput;
    }

    /**
     * Creates a {@link Scanner} with input commands.
     *
     * @param inputMultiplier The input multiplier.
     * @return The {@link Scanner} object with input commands.
     */
    private Scanner getInput(int inputMultiplier) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < inputMultiplier; ++i) {
            sb.append("Add (1, \"a1\", \"Robert\")\n");
            sb.append("Add (2, \"a2\", \"Martin\")\n");
            sb.append("PrintAll\n");
            sb.append("DeleteAll\n");
            sb.append("PrintAll\n");
        }
        return new Scanner(new ByteArrayInputStream(sb.toString().getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * Creates a {@link Scanner} with input commands containing random IDs.
     *
     * @param inputMultiplier The input multiplier.
     * @return The {@link Scanner} object with input commands.
     */
    private Scanner getInputWithRandomId(int inputMultiplier) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < inputMultiplier; ++i) {
            sb.append("Add (").append(random.nextInt(1000000000)).append(", \"a1\", \"Robert\")\n");
            sb.append("Add (").append(random.nextInt(1000000000)).append(", \"a2\", \"Martin\")\n");
            sb.append("PrintAll\n");
            sb.append("DeleteAll\n");
            sb.append("PrintAll\n");
        }
        return new Scanner(new ByteArrayInputStream(sb.toString().getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * Test case for log output with multiple producers and a single consumer.
     *
     * @throws InterruptedException If the thread sleep is interrupted.
     */
    @Test
    public void testLogOutputMultiProducerSingleConsumer() throws InterruptedException {
        int consumerCount = 1;
        for (int inputMultiplier : new int[]{1, 5, 25, 100}) {
            List<String> expectedOutput = getExpectedOutputSorted(consumerCount, inputMultiplier);
            for (int producerCount : new int[]{1, 2, 3, 5, 10}) {
                String params = getAndLogTestParams(inputMultiplier, consumerCount, producerCount);
                Scanner input = getInput(inputMultiplier);
                List<String> logLines = simulateAndCollectSortedLogs(consumerCount, producerCount, input, false);
                assertEquals(expectedOutput, logLines);
                System.out.println("Passed test with " + params);
            }
        }
    }

    /**
     * Test case for no errors with multiple producers, multiple consumers, and random user IDs.
     *
     * @throws InterruptedException If the thread sleep is interrupted.
     */
    @Test
    public void testNoErrorsMultiProducerMultiConsumerRandomUserId() throws InterruptedException {
        for (int inputMultiplier : new int[]{1, 5, 25, 100}) {
            for (int consumerCount : new int[]{1, 2, 3, 5, 10}) {
                for (int producerCount : new int[]{1, 2, 3, 5, 10}) {
                    String params = getAndLogTestParams(inputMultiplier, consumerCount, producerCount);
                    Scanner input = getInputWithRandomId(inputMultiplier);
                    List<String> logLines = simulateAndCollectSortedLogs(consumerCount, producerCount, input, true);
                    assertEquals(List.of(), logLines);
                    System.out.println("Passed test with " + params);
                }
            }
        }
    }
}
