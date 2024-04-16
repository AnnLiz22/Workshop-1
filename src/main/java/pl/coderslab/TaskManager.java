package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TaskManager {

    static final String FILE_NAME = "tasks.csv";
    static final String[] OPTIONS = {"add", "remove", "list", "exit"};
    static String[][] tasks;


    public static void main(String[] args) throws IOException {
        tasks = loadDataToTab(FILE_NAME);
        selectOption();
    }


    public static String selectOption() throws IOException {

        Scanner scan = new Scanner(System.in);
        System.out.println(ConsoleColors.BLUE + "Please select an option: ");
        System.out.println(ConsoleColors.RESET + Arrays.toString(OPTIONS));
        String option = scan.nextLine();

        switch (option) {
            case "add":
                addTask();
                break;
            case "remove":
                removeTask(tasks, getTheNumber());
                saveTabToFile(FILE_NAME, tasks);
                System.out.println("Value was successfully deleted.");
                ;
                break;
            case "list":
                list();
                break;
            case "exit":
                saveTabToFile(FILE_NAME, tasks);
                exit();
                break;

            default:
                System.out.println("Please select a correct option.");
        }
        return option;
    }

    public static void list() throws IOException {
        Path path = Paths.get(FILE_NAME);
        try {
            for (String line : Files.readAllLines(path)) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addTask() {

        StringBuilder newTask = new StringBuilder();
        Path pathAdd = Paths.get(FILE_NAME);

        Scanner scan = new Scanner(System.in);
        System.out.println("Please add task description: ");
        newTask.append(scan.nextLine()).append(", ");
        System.out.println("Please add task due date: ");
        newTask.append(scan.nextLine()).append(", ");
        System.out.println("Is your task important: true/ false");
        newTask.append(scan.nextBoolean()).append("\n");

        String task = newTask.toString();

        try {
            Files.writeString(pathAdd, task, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            System.out.println("Not possible to write.");
        }
    }

    private static void removeTask(String[][] tab, int index) {
        try {
            if (index < tab.length) {
                tasks = ArrayUtils.remove(tab, index);
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Element doesn't exist.");
        }
    }

    public static int getTheNumber() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please select number to remove.");

        String n = scanner.nextLine();
        while (!isNumberGreaterEqualZero(n)) {
            System.out.println("Incorrect argument passed. Please give number greater or equal 0");
            scanner.nextLine();
        }
        return Integer.parseInt(n);
    }

    public static boolean isNumberGreaterEqualZero(String input) {
        if (NumberUtils.isParsable(input)) {
            return Integer.parseInt(input) >= 0;
        }
        return false;
    }

    public static String[][] loadDataToTab(String fileName) {
        Path dir = Paths.get(fileName);
        if (!Files.exists(dir)) {
            System.out.println("File not exist.");
            System.exit(0);
        }

        String[][] tab = null;
        try {
            List<String> strings = Files.readAllLines(dir);
            tab = new String[strings.size()][strings.get(0).split(",").length];

            for (int i = 0; i < strings.size(); i++) {
                String[] split = strings.get(i).split(",");
                for (int j = 0; j < split.length; j++) {
                    tab[i][j] = split[j];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tab;
    }


    public static void saveTabToFile(String fileName, String[][] tab) {
        Path dir = Paths.get(fileName);

        String[] lines = new String[tasks.length];
        for (int i = 0; i < tab.length; i++) {
            lines[i] = String.join(",", tab[i]);
        }

        try {
            Files.write(dir, Arrays.asList(lines));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void exit() {
        System.out.println(ConsoleColors.RED + "Bye, bye.");
        System.exit(0);
    }

}