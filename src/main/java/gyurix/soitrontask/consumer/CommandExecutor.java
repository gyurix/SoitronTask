package gyurix.soitrontask.consumer;

import gyurix.soitrontask.db.entity.User;
import gyurix.soitrontask.db.repository.UserRepository;

import java.util.Queue;

/**
 * The CommandExecutor class represents a consumer responsible for executing commands
 * received from a shared queue and interacting with a UserRepository.
 * It processes commands related to adding users, deleting all users,
 * printing all users, and providing help information about available commands.
 * The CommandExecutor runs in a separate thread and continuously polls the command queue
 * for new commands to execute.
 */
public class CommandExecutor extends Consumer<UserRepository> implements Runnable {

    /**
     * Constructs a new CommandExecutor with the specified command queue and user repository.
     *
     * @param queue      The queue that stores the commands to be executed.
     * @param repository The user repository to interact with.
     */
    public CommandExecutor(Queue<String> queue, UserRepository repository) {
        super(queue, repository);
    }

    /**
     * Runs the CommandExecutor in a separate thread.
     * The CommandExecutor continuously polls the command queue for new commands to execute.
     * It processes each command, performs the corresponding operation in the user repository,
     * and logs the execution results.
     */
    public void run() {
        log("Started consumer");
        while (running) {
            String commandLine;
            String[] command;
            synchronized (queue) {
                if (queue.isEmpty()) {
                    continue;
                }
                commandLine = queue.poll();
            }
            command = commandLine.trim().split(" *\\(", 2);
            if (command.length == 2 && command[1].endsWith(")")) {
                command[1] = command[1].substring(0, command[1].length() - 1);
            }
            try {
                switch (command[0].toLowerCase()) {
                    case "add" -> {
                        User user = new User(command[1]);
                        repository.add(user);
                        log("Added user " + user);
                        continue;
                    }
                    case "deleteall" -> {
                        repository.deleteAll();
                        log("Deleted all users");
                        continue;
                    }
                    case "help" -> {
                        log("Available commands:");
                        log("- Add (id, guid, name): Adds a new user");
                        log("- DeleteAll: Deletes all the users");
                        log("- Help: Shows this help menu");
                        log("- PrintAll: Prints all the users");
                        continue;
                    }
                    case "printall" -> {
                        log("Users:");
                        for (User user : repository.getAll()) {
                            log(user);
                        }
                        continue;
                    }
                }
                logError("Command " + command[0] + " was not found, enter \"Help\" to list the available commands");
            } catch (Throwable e) {
                logError("Failed to execute command \"" + command[0] + " " + command[1] + "\", " +
                        "enter \"Help\" to list the available commands");
                logError(e.getClass().getSimpleName() + " - " + e.getMessage());
                for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                    logError(stackTraceElement);
                }
            }
        }
    }
}
