package com;

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
        Subtask st7 = new Subtask();
        Subtask st8 = new Subtask();

        RealTask t1 = new RealTask();
        RealTask t2 = new RealTask();
        RealTask t3 = new RealTask();
        RealTask t4 = new RealTask();
        RealTask t5 = new RealTask();

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

        manager.addTask(t1); //добавление тасков в тасклист +
        manager.addTask(t2);
        manager.addTask(t1);
        System.out.println(Manager.taskList);

        manager.addEpicWithSubtask(epic1, st1, st2, st3); //добавление эпиков с сабтаской +
        manager.addEpicWithSubtask(epic2, st4, st5, st6);
        System.out.println(Manager.epicMap);

        //manager.addTask(t1, t2, t3, t4); // стоит ли таскам иметь возможно дважны добавлять в лист?
        System.out.println(Manager.taskList);

        System.out.println(manager.getListEpics()); //получение списка всех эпиков +
        System.out.println(manager.getListSubtasks()); //получение списка всех сабтасков +
        System.out.println(manager.getListSubtasksByEpic(epic2));//получение списка подзадач конкретного эпика +
        System.out.println(manager.getEpicByUuid(epic1.getUuid())); //получение задачи эпик по идентификатору +
        System.out.println(manager.getTaskByUuid(t1.getUuid())); //получение таски по идентификатору +
        System.out.println(manager.getSubtaskByUuid(st1.getUuid())); //получение субтаски по идентификатору +

        manager.addEpic(epic3); //добавление нового эпика в мапу +
        System.out.println(Manager.epicMap);
        manager.addSubtaskInCreatedEpic(epic3, st7, st8); //добавление нового сабтаска в эпик +

        Subtask subtask = new Subtask();
        subtask.setName("YA subtask");
        manager.changeSubtaskByUuid(st1.getUuid(), subtask);
        System.out.println(st1.getName());
        System.out.println(subtask.getName());

        manager.clearSubtaskByUuid(st1.getUuid());
        manager.clearEpicByUuid(epic1.getUuid());

        Epic epic4 = new Epic();
        System.out.println(epic4.getStatus());
        manager.changeEpicStatus(epic4);
        Subtask st9 = new Subtask();
        Subtask st10 = new Subtask();
        Subtask st11 = new Subtask();
        st11.setStatus(StatusTask.DONE);
        manager.addSubtaskInCreatedEpic(epic4, st9, st10, st11);
        manager.changeEpicStatus(epic4);
        System.out.println(epic4.getStatus());
        manager.addEpic(epic4);
       // manager.addEpic(epic4);

    }
}
