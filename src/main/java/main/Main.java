package main;

public class Main {
    public static void main(String[] args) {

        DataBaseToDo dataBaseToDo = new DataBaseToDo();
        if (!dataBaseToDo.open()) {
            System.out.println("Something went wrong. Can't open database.");
            return;
        }

        ToDoList toDoList = new ToDoList(dataBaseToDo);
        toDoList.starts();

        dataBaseToDo.close();
    }
}
