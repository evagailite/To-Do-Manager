package main;

import java.util.Scanner;

public class ToDoList {
    private final Scanner scanner = new Scanner(System.in);
    DataBaseToDo dataBaseToDo;

    public ToDoList(DataBaseToDo dataBaseToDo) {
        this.dataBaseToDo = dataBaseToDo;
    }

    public void starts() {
        welcomeMessage();
        assignItems();
    }

    private void welcomeMessage() {
        System.out.println("\nWelcome to the To Do Manager");
        System.out.println("====================================");
    }

    private static void printItemsOptions() {
        System.out.println("\n\t 1 - Add a new item");
        System.out.println("\t 2 - Edit an item");
        System.out.println("\t 3 - Delete an item");
        System.out.println("\t 4 - Display a list of items");
        System.out.println("\t 5 - Clear a list");
        System.out.println("\t 6 - Mark item as done");
        System.out.println("\t 7 - Exit from the list");
    }

    private void assignItems() {
        boolean quit = false;
        while (!quit) {
            int choice;
            printItemsOptions();
            System.out.println("Please enter a number from the menu to proceed:");
            choice = scanner.nextInt();
            input();
            switch (choice) {
                case 1:
                    createItem();
                    break;
                case 2:
                    edit();
                    break;
                case 3:
                    remove();
                    break;
                case 4:
                    displayItem();
                    break;
                case 5:
                    clearList();
                    break;
                case 6:
                    done();
                    break;
                case 7:
                    System.out.println("The application has stopped working!");
                    quit = true;
                    break;
                default:
                    System.out.println("Invalid input! Please choose action between numbers 1-8");
                    break;
            }
        }
    }

    private void clearList() {
        dataBaseToDo.cleanTable();
        dataBaseToDo.resetId();
        dataBaseToDo.reset();
    }

    public void createItem() {
        System.out.print("Please enter a new item: ");
        dataBaseToDo.searchItem(input());
    }

    public void edit() {
        displayItem();
        System.out.print("Please choose an id to modify: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Please modify an item: ");
        String editItem = input();
        dataBaseToDo.editItem(editItem, id);
    }

    public void remove() {
        displayItem();
        System.out.print("Please enter an id to remove an item: ");
        int id = scanner.nextInt();
        dataBaseToDo.removeItem(id);
        dataBaseToDo.resetId();
        dataBaseToDo.reset();

    }

    public void displayItem() {
        dataBaseToDo.printCount();
        dataBaseToDo.displayItems();
    }

    public void done() {
        displayItem();
        System.out.print("\nPlease enter an id to assign as done: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        dataBaseToDo.concat(dataBaseToDo.markAsDone(id), id);
    }

    public String input() {
        return scanner.nextLine();
    }
}
