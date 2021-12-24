package com;

import org.junit.jupiter.api.Test;

class ManagerTest {
    Manager manager = new Manager();

    @Test
    void findEpicBySubtaskUuid() {

        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();
        Epic epic2 = new Epic();
        Subtask subtask4 = new Subtask();
        Subtask subtask5 = new Subtask();
        Subtask subtask6 = new Subtask();
        RealTask realTask = new RealTask();
        manager.addEpicWithSubtask(epic2, subtask4, subtask5, subtask6);
        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);
        manager.findEpicBySubtaskUuid(subtask5.getUuid());
//        manager.findEpicBySubtaskUuid(realTask.getUuid());  //done an exception was expected
//        manager.findEpicBySubtaskUuid(epic1.getUuid()); //done an exception was expected
    }

    @Test
    void getSubtaskByUuid() {

        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();
        Epic epic2 = new Epic();
        Subtask subtask4 = new Subtask();
        Subtask subtask5 = new Subtask();
        Subtask subtask6 = new Subtask();
        RealTask realTask = new RealTask();
        manager.addEpicWithSubtask(epic2, subtask4, subtask5, subtask6);
        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);
        manager.getSubtaskByUuid(subtask1.getUuid());
//        manager.getSubtaskByUuid(realTask.getUuid()); //done an exception was expected
//        manager.getSubtaskByUuid(epic1.getUuid()); //done an exception was expected
    }

    @Test
    void getListSubtasks() {

        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();

        Epic epic2 = new Epic();
        Subtask subtask4 = new Subtask();
        Subtask subtask5 = new Subtask();
        Subtask subtask6 = new Subtask();
        manager.addEpicWithSubtask(epic2, subtask4, subtask5, subtask6);
        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);

        System.out.println(manager.getListSubtasks());
    }

    @Test
    void getListEpics() {

        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();

        Epic epic2 = new Epic();
        Subtask subtask4 = new Subtask();
        Subtask subtask5 = new Subtask();
        Subtask subtask6 = new Subtask();

        Epic epic3 = new Epic();
        Epic epic4 = new Epic();

        manager.getListEpics();

        manager.addEpic(epic3);
        manager.addEpic(epic4);

        manager.getListEpics();
        manager.addEpicWithSubtask(epic2, subtask4, subtask5, subtask6);
        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);

        manager.getListEpics();
    }

    @Test
    void getListSubtasksByEpic() {

        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();

        Epic epic2 = new Epic();
        Subtask subtask4 = new Subtask();
        Subtask subtask5 = new Subtask();
        Subtask subtask6 = new Subtask();
        manager.addEpicWithSubtask(epic2, subtask4, subtask5, subtask6);
        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);

        System.out.println(manager.getListSubtasksByEpic(epic2));
    }

    @Test
    void getEpicByUuid() {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();

        Epic epic2 = new Epic();
        Subtask subtask4 = new Subtask();
        Subtask subtask5 = new Subtask();
        Subtask subtask6 = new Subtask();

        RealTask realTask = new RealTask();

        manager.addEpicWithSubtask(epic2, subtask4, subtask5, subtask6);
        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);

        System.out.println(manager.getEpicByUuid(epic1.getUuid())); //done
        System.out.println(manager.getEpicByUuid(realTask.getUuid())); //done an exception was expected
        System.out.println(manager.getEpicByUuid(subtask1.getUuid())); //done an exception was expected
    }

    @Test
    void getTaskByUuid() {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();

        Epic epic2 = new Epic();
        Subtask subtask4 = new Subtask();
        Subtask subtask5 = new Subtask();
        Subtask subtask6 = new Subtask();

        RealTask realTask = new RealTask();

        manager.addEpicWithSubtask(epic2, subtask4, subtask5, subtask6);
        manager.addRealTask(realTask);

        System.out.println(manager.getTaskByUuid(realTask.getUuid())); //done
//        System.out.println(manager.getTaskByUuid(epic1.getUuid())); //done an exception was expected
//        System.out.println(manager.getTaskByUuid(subtask1.getUuid())); //done an exception was expected

    }

    @Test
    void addEpic() {

        Epic epic1 = new Epic();
        manager.addEpic(epic1);
        manager.addEpic(epic1); //done an exception was expected, there is not two same Epic by one uuid in epicMap
        Epic epic2 = new Epic();
        manager.addEpic(epic2);

    }

    @Test
    void addEpicWithSubtask() {

        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();

        Epic epic2 = new Epic();
        Subtask subtask4 = new Subtask();
        Subtask subtask5 = new Subtask();
        Subtask subtask6 = new Subtask();

        Epic epic3 = new Epic();
        Subtask subtask7 = new Subtask();
        Subtask subtask8 = new Subtask();
        Subtask subtask9 = new Subtask();

        manager.addEpicWithSubtask(epic2, subtask1, subtask5, subtask6);
        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);
        manager.addEpicWithSubtask(epic3, subtask7);
        manager.addEpicWithSubtask(epic3, subtask7); //done an exception was expected, there is not two same Epic by one uuid in epicMap
        manager.addEpicWithSubtask(epic1, subtask2); //done an exception was expected
        manager.addEpicWithSubtask(epic3, subtask4); //done unable to add Epic even if it has subtask, which absent in Epic

        System.out.println(epic3.getSubtaskList().size()); //1
    }

    @Test
    void addRealTask() {

        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();

        RealTask realTask1 = new RealTask();
        RealTask realTask2 = new RealTask();

        manager.addRealTask(realTask1);
        manager.addRealTask(realTask1); //done an exception was expected
        manager.addRealTask(realTask2);

    }

    @Test
    void addSubtaskInCreatedEpic() {

        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();

        manager.addEpic(epic1);
        manager.addSubtaskInCreatedEpic(epic1, subtask1);
//        manager.addSubtaskInCreatedEpic(epic1, subtask1); //done an exception was expected
        manager.addSubtaskInCreatedEpic(epic1, subtask2);
        System.out.println(epic1.getSubtaskList().size()); //2

    }

    @Test
    void changeTaskByUuid() {

        Subtask subtask1 = new Subtask();
        RealTask realTask1 = new RealTask();
        RealTask realTask2 = new RealTask();

        realTask2.setName("I'm real task");
        realTask2.setDescription("About something");
        realTask2.setStatus(StatusTask.DONE);

//        manager.changeTaskByUuid(realTask1.getUuid(), realTask1); //done 2 exception was expected
        manager.addRealTask(realTask1);
        System.out.println(realTask1.getName() + " " + realTask1.getDescription() + " " + realTask1.getStatus());

//        manager.changeTaskByUuid(subtask1.getUuid(), realTask2); //find by uuid subtask - done, exception was expected
        manager.changeTaskByUuid(realTask1.getUuid(), realTask2);
        System.out.println(realTask1.getName() + " " + realTask1.getDescription() + " " + realTask1.getStatus());

    }

    @Test
    void changeEpicByUuid() {

        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();



    }

    @Test
    void changeSubtaskByUuid() {

        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();

    }

    @Test
    void clearListTasks() {
    }

    @Test
    void clearListEpics() {
    }

    @Test
    void clearListSubtasks() {
    }

    @Test
    void clearTaskByUuid() {
    }

    @Test
    void clearSubtaskByUuid() {
    }

    @Test
    void clearEpicByUuid() {
    }

    @Test
    void changeEpicStatus() {
    }
}