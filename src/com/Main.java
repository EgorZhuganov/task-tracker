package com;

import java.util.UUID;

public class Main {

    public static void main(String[] args) {

        Manager manager = new Manager();

        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Epic epic3 = new Epic();

        Subtask st1 = new Subtask();
        Subtask st2 = new Subtask();
        Subtask st3 = new Subtask();
        Subtask st4 = new Subtask();
        Subtask st5 = new Subtask();
        Subtask st6 = new Subtask();

        Task t1 = new Task();
        Task t2 = new Task();
        Task t3 = new Task();
        Task t4 = new Task();
        Task t5 = new Task();

        System.out.println(epic1.getUuid());
        System.out.println(epic2.getUuid());
        System.out.println(epic3.getUuid());
        System.out.println(st1.getUuid());
        System.out.println(st2.getUuid());
        System.out.println(st3.getUuid());
        System.out.println(st4.getUuid());
        System.out.println(st5.getUuid());
        System.out.println(st6.getUuid());
        System.out.println(t1.getUuid());
        System.out.println(t2.getUuid());
        System.out.println(t3.getUuid());
        System.out.println(t4.getUuid());
        System.out.println(t5.getUuid());

        manager.addTask(t1, t2, t3); //добавление тасков в тасклист
        System.out.println(manager.taskList);

        manager.addEpicWithSubtask(epic1, st1, st2, st3); //добавление эпиков с сабтаской
        manager.addEpicWithSubtask(epic2, st4, st5, st6);
        System.out.println(manager.epicMap);

        System.out.println(manager.getListSubtasks());

//        manager.getListSubtasksByEpic(epic1);
//        System.out.println(manager.getListSubtasksByEpic(epic1));



    }
}
