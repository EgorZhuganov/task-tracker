package com.ezh.taskbook.manager;

import com.ezh.taskbook.task.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InMemoryTasksManagerTestV01 {

    TaskManager manager = new Managers().getDefault();

    @Test
    void getEpicBySubtaskUuid() {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();
        Epic epic2 = new Epic();
        Subtask subtask4 = new Subtask();
        Subtask subtask5 = new Subtask();
        Subtask subtask6 = new Subtask();
        SingleTask singleTask = new SingleTask();
        manager.addEpicWithSubtask(epic2, subtask4, subtask5, subtask6);
        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);
        manager.getEpicBySubtaskUuid(subtask5.getUuid());

        Assertions.assertThrows(RuntimeException.class, () -> manager.getSubtaskByUuid(singleTask.getUuid()));
        Assertions.assertThrows(RuntimeException.class, () -> manager.getSubtaskByUuid(epic1.getUuid()));
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
        SingleTask singleTask = new SingleTask();

        manager.addEpicWithSubtask(epic2, subtask4, subtask5, subtask6);
        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);
        manager.getSubtaskByUuid(subtask1.getUuid());

        Assertions.assertThrows(RuntimeException.class, () -> manager.getSubtaskByUuid(singleTask.getUuid()));
        Assertions.assertThrows(RuntimeException.class, () -> manager.getSubtaskByUuid(epic1.getUuid()));
    }

    @Test
    void getListSubtasks() {
        TaskManager manager = new InMemoryTasksManager();

        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();
        Subtask subtask4 = new Subtask();
        Subtask subtask5 = new Subtask();
        Subtask subtask6 = new Subtask();

        manager.addEpicWithSubtask(epic2, subtask4, subtask5, subtask6);
        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);

        Assertions.assertEquals(6, manager.getListSubtasks().size());
        Assertions.assertEquals(0, manager.getHistory().size());
    }

    @Test
    void getListEpics() {
        TaskManager manager = new InMemoryTasksManager();

        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Epic epic3 = new Epic();
        Epic epic4 = new Epic();
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();
        Subtask subtask4 = new Subtask();
        Subtask subtask5 = new Subtask();
        Subtask subtask6 = new Subtask();

        Assertions.assertEquals(0, manager.getListEpics().size());

        manager.addEpic(epic3);
        manager.addEpic(epic4);

        Assertions.assertEquals(2, manager.getListEpics().size());

        manager.addEpicWithSubtask(epic2, subtask4, subtask5, subtask6);
        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);

        Assertions.assertEquals(4, manager.getListEpics().size());
    }

    @Test
    void getListSubtasksByEpic() {
        TaskManager manager = new InMemoryTasksManager();

        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();
        Subtask subtask4 = new Subtask();
        Subtask subtask5 = new Subtask();
        Subtask subtask6 = new Subtask();

        manager.addEpicWithSubtask(epic2, subtask4, subtask5, subtask6);
        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);

        Assertions.assertEquals(3, manager.getListSubtasksByEpic(epic2).size());

    }

    @Test
    void getEpicByUuid() {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        Subtask subtask3 = new Subtask(epic1);

        Epic epic2 = new Epic();
        Subtask subtask4 = new Subtask(epic2);
        Subtask subtask5 = new Subtask(epic2);
        Subtask subtask6 = new Subtask(epic2);

        SingleTask singleTask = new SingleTask();

        manager.addEpicWithSubtask(epic2, subtask4, subtask5, subtask6);
        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);

        Assertions.assertEquals(epic1.getUuid(), manager.getEpicByUuid(epic1.getUuid()).getUuid());
        Assertions.assertThrows(RuntimeException.class, () -> manager.getEpicByUuid(singleTask.getUuid()));
        Assertions.assertThrows(RuntimeException.class, () -> manager.getEpicByUuid(subtask1.getUuid()));
    }

    @Test
    void getSingleTaskByUuid() {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask();
        SingleTask singleTask = new SingleTask();

        manager.addSingleTask(singleTask);

        Assertions.assertEquals(singleTask.getUuid(), manager.getSingleTaskByUuid(singleTask.getUuid()).getUuid());
        Assertions.assertThrows(RuntimeException.class, () -> manager.getSingleTaskByUuid(epic1.getUuid()));
        Assertions.assertThrows(RuntimeException.class, () -> manager.getSingleTaskByUuid(subtask1.getUuid()));
    }

    @Test /*there is not two same Epic by one uuid in epicMap*/
    void addEpic() {
        Epic epic1 = new Epic();
        manager.addEpic(epic1);
        Assertions.assertThrows(RuntimeException.class, () -> manager.addEpic(epic1));
        Epic epic2 = new Epic();
        manager.addEpic(epic2);
        Assertions.assertEquals(2, manager.getListEpics().size());
    }

    /* unable to add Epic even if try to add with subtask which isn't include in this Epic
     * there is not two same Epic by one uuid in epicMap */
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

        manager.addEpicWithSubtask(epic2, subtask1, subtask5, subtask6);
        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);
        manager.addEpicWithSubtask(epic3, subtask7);
        Assertions.assertThrows(RuntimeException.class, () -> manager.addEpicWithSubtask(epic3, subtask7));
        Assertions.assertThrows(RuntimeException.class, () -> manager.addEpicWithSubtask(epic1, subtask2));
        Assertions.assertThrows(RuntimeException.class, () -> manager.addEpicWithSubtask(epic3, subtask4));

        Assertions.assertEquals(1, epic3.getSubtaskList().size());
    }

    @Test
    void addSingleTask() {

        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();

        manager.addSingleTask(singleTask1);
        Assertions.assertThrows(RuntimeException.class, () -> manager.addSingleTask(singleTask1));
        manager.addSingleTask(singleTask2);
    }

    @Test
    void addSubtaskInCreatedEpic() {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();

        manager.addEpic(epic1);
        manager.addSubtaskInAddedEpic(epic1, subtask1);
        Assertions.assertThrows(RuntimeException.class, () -> manager.addSubtaskInAddedEpic(epic1, subtask1));
        manager.addSubtaskInAddedEpic(epic1, subtask2);
        Assertions.assertEquals(2, epic1.getSubtaskList().size());
    }

    @Test
    void changeSingleTaskByUuid() {
        Subtask subtask1 = new Subtask();
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();

        singleTask2.setName("I'm real task");
        singleTask2.setDescription("About something");
        singleTask2.setStatus(StatusTask.DONE);

        Assertions.assertThrows(RuntimeException.class, () ->
                manager.changeSingleTaskByUuid(singleTask1.getUuid(), singleTask2));
        manager.addSingleTask(singleTask1);
        System.out.println(singleTask1.getName() + " " + singleTask1.getDescription() + " " + singleTask1.getStatus());

        Assertions.assertThrows(RuntimeException.class, () ->
                manager.changeSingleTaskByUuid(subtask1.getUuid(), singleTask2));
        manager.changeSingleTaskByUuid(singleTask1.getUuid(), singleTask2);
        System.out.println(singleTask1.getName() + " " + singleTask1.getDescription() + " " + singleTask1.getStatus());
    }

    @Test
    void changeEpicByUuid() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Epic epic4 = new Epic();
        Epic epic5 = new Epic();
        Subtask subtask1 = new Subtask();
        SingleTask singleTask1 = new SingleTask();

        Assertions.assertThrows(RuntimeException.class, () -> manager.changeEpicByUuid(epic1.getUuid(), epic2));
        manager.addEpic(epic4);

        epic5.setName("Find yourself");
        epic5.setDescription("Wake up at 8 am");
        Assertions.assertThrows(RuntimeException.class, () ->  manager.changeEpicByUuid(subtask1.getUuid(), epic5));
        Assertions.assertThrows(RuntimeException.class, () -> manager.changeEpicByUuid(singleTask1.getUuid(), epic4));
        manager.changeEpicByUuid(epic4.getUuid(), epic5);
        System.out.println(epic4.getName() + " " + epic4.getDescription() + " " + epic4.getStatus());
    }

    @Test
    void changeSubtaskByUuid() {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();

        Assertions.assertThrows(RuntimeException.class, () -> manager.
                changeSubtaskByUuid(subtask1.getUuid(), subtask2));
        manager.addEpic(epic1);
        Assertions.assertThrows(RuntimeException.class, () -> manager.
                changeSubtaskByUuid(subtask1.getUuid(), subtask2));
        manager.addSubtaskInAddedEpic(epic1, subtask1);
        subtask2.setStatus(StatusTask.IN_PROGRESS);
        subtask2.setName("My name is Subtask");
        subtask2.setDescription("I have to do something important");
        manager.changeSubtaskByUuid(subtask1.getUuid(), subtask2);
        System.out.println(subtask2.getName() + " " + subtask2.getDescription() + " " + subtask2.getStatus());
        Assertions.assertThrows(RuntimeException.class, () ->
                manager.changeSubtaskByUuid(subtask1.getUuid(), subtask1));
    }

    @Test
    void clearSingleTasks() {
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();

        manager.addSingleTask(singleTask1);
        manager.addSingleTask(singleTask2);
        Assertions.assertEquals(2, manager.getListSingleTasks().size());
        manager.clearSingleTasks();
        Assertions.assertEquals(0, manager.getListSingleTasks().size());
    }

    @Test
    void clearEpics() {

        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Epic epic3 = new Epic();
        Epic epic4 = new Epic();
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();

        manager.addEpic(epic1);
        manager.addEpicWithSubtask(epic2, subtask2, subtask3, subtask1);
        manager.addEpic(epic3);
        manager.addEpic(epic4);

        manager.getEpicByUuid(epic2.getUuid());
        manager.getEpicByUuid(epic1.getUuid());
        manager.getEpicByUuid(epic3.getUuid());
        manager.getSubtaskByUuid(subtask3.getUuid());

        Assertions.assertEquals(4, manager.getListEpics().size());
        Assertions.assertEquals(4, manager.getHistory().size());
        manager.clearEpics();
        Assertions.assertEquals(0, manager.getListEpics().size());
        Assertions.assertEquals(0, manager.getHistory().size());

    }

    @Test
    void clearSubtasksInAllEpic() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();
        Subtask subtask4 = new Subtask();
        Subtask subtask5 = new Subtask();

        Assertions.assertEquals(0, epic1.getSubtaskList().size());
        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);
        Assertions.assertEquals(3, epic1.getSubtaskList().size());

        Assertions.assertEquals(0, epic2.getSubtaskList().size());
        manager.addEpic(epic2);
        manager.addSubtaskInAddedEpic(epic2, subtask4);
        manager.addSubtaskInAddedEpic(epic2, subtask5);
        Assertions.assertEquals(2, epic2.getSubtaskList().size());

        manager.clearSubtasksInAllEpic();
        Assertions.assertEquals(0, epic1.getSubtaskList().size());
        Assertions.assertEquals(0, epic2.getSubtaskList().size());
    }

    @Test
    void clearSubtasksInEpic() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();

        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);
        manager.clearSubtasksInEpic(epic1);
        Assertions.assertEquals(0, epic1.getSubtaskList().size());
        Assertions.assertEquals(0, epic2.getSubtaskList().size());
    }

    @Test
    void removeSingleTaskByUuid() {
        Epic epic = new Epic();
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();
        Assertions.assertThrows(RuntimeException.class, () -> manager.removeSingleTaskByUuid(singleTask1.getUuid()));
        manager.addSingleTask(singleTask1);
        manager.addSingleTask(singleTask2);
        manager.removeSingleTaskByUuid(singleTask1.getUuid());
        Assertions.assertThrows(RuntimeException.class, () -> manager.removeSingleTaskByUuid(epic.getUuid()));
    }

    @Test
    void removeSubtaskByUuid() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();
        Subtask subtask5 = new Subtask();

        Assertions.assertThrows(RuntimeException.class, () -> manager.removeSubtaskByUuid(epic1.getUuid()));
        Assertions.assertThrows(RuntimeException.class, () -> manager.removeSubtaskByUuid(subtask1.getUuid()));
        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);

        manager.addEpicWithSubtask(epic2, subtask5);
        manager.removeSubtaskByUuid(subtask5.getUuid());
        manager.getEpicByUuid(epic2.getUuid());

        manager.removeSubtaskByUuid(subtask2.getUuid());
        System.out.println(manager.getListSubtasksByEpic(epic1));
    }

    @Test
    void removeEpicByUuid() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask5 = new Subtask();

        Assertions.assertThrows(RuntimeException.class, () -> manager.removeEpicByUuid(epic1.getUuid()));
        manager.addEpic(epic1);
        manager.addEpicWithSubtask(epic2, subtask1, subtask2, subtask5);
        Assertions.assertEquals(2, manager.getListEpics().size());

        manager.removeEpicByUuid(epic1.getUuid());
        Assertions.assertEquals(1, manager.getListEpics().size());
        Assertions.assertEquals(0, manager.getListSubtasksByEpic(epic1).size());
    }
}