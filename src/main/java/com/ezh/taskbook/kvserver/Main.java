package com.ezh.taskbook.kvserver;

import com.ezh.taskbook.exception.TaskNotFoundException;
import com.ezh.taskbook.exception.TasksIntersectionException;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, TasksIntersectionException, TaskNotFoundException {

        new KVServer().start(); //port 8078

    }
}
