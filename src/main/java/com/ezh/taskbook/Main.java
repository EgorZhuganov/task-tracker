package com.ezh.taskbook;

import com.ezh.taskbook.manager.Managers;
import com.ezh.taskbook.manager.TaskManager;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = new Managers().getDefault();

    }
}
