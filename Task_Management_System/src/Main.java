import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Task class to represent individual tasks
class Task {
    private String title;
    private String description;
    private String assignedUser;
    private boolean completed;

    public Task(String title, String description, String assignedUser) {
        this.title = title;
        this.description = description;
        this.assignedUser = assignedUser;
        this.completed = false;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAssignedUser() {
        return assignedUser;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void markCompleted() {
        this.completed = true;
    }
}

// TaskManager class to manage tasks
class TaskManager implements TaskManagerMBean {
    private List<Task> tasks;

    public TaskManager() {
        this.tasks = new ArrayList<>();
    }

    public void createTask(String title, String description, String assignedUser) {
        Task task = new Task(title, description, assignedUser);
        tasks.add(task);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void markTaskAsCompleted(int taskId) {
        if (taskId >= 0 && taskId < tasks.size()) {
            tasks.get(taskId).markCompleted();
            System.out.println("Task marked as completed.");
        } else {
            System.out.println("Invalid task ID.");
        }
    }

    @Override
    public int getTaskCount() {
        return tasks.size();
    }

    @Override
    public int getCompletedTaskCount() {
        int count = 0;
        for (Task task : tasks) {
            if (task.isCompleted()) {
                count++;
            }
        }
        return count;
    }
}

public class Main {
    public static void main(String[] args) throws Exception {
        TaskManager taskManager = new TaskManager();

        // Registering the MBean
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("com.example:type=TaskManager");
        mbs.registerMBean(taskManager, name);

        // Scanner for user input
        Scanner scanner = new Scanner(System.in);

        // Create tasks by user input
        System.out.println("Enter task details or type 'exit' to finish:");
        while (true) {
            System.out.print("Title: ");
            String title = scanner.nextLine();
            if (title.equalsIgnoreCase("exit")) {
                break;
            }
            System.out.print("Description: ");
            String description = scanner.nextLine();
            System.out.print("Assigned User: ");
            String assignedUser = scanner.nextLine();
            taskManager.createTask(title, description, assignedUser);
            System.out.println("Task created successfully.");
        }

        // Display tasks
        List<Task> tasks = taskManager.getTasks();
        System.out.println("\nTasks:");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            System.out.println((i + 1) + ". Title: " + task.getTitle() + ", Assigned to: " + task.getAssignedUser() + ", Completed: " + task.isCompleted());
        }
    }
}
