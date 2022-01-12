package main.java.com.ezh.taskbook;

import main.java.com.ezh.taskbook.manager.Managers;
import main.java.com.ezh.taskbook.manager.TaskManager;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = new Managers().getDefault();

    }
}
