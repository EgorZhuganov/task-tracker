package test.java.com.ezh.taskbook.manager;

import main.java.com.ezh.taskbook.manager.Manager;
import main.java.com.ezh.taskbook.task.Epic;
import main.java.com.ezh.taskbook.task.RealTask;
import main.java.com.ezh.taskbook.task.StatusTask;
import main.java.com.ezh.taskbook.task.Subtask;
import org.junit.jupiter.api.Assertions;
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

        Assertions.assertThrows(RuntimeException.class, () -> manager.getSubtaskByUuid(realTask.getUuid()));
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
        RealTask realTask = new RealTask();

        manager.addEpicWithSubtask(epic2, subtask4, subtask5, subtask6);
        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);
        manager.getSubtaskByUuid(subtask1.getUuid());

        Assertions.assertThrows(RuntimeException.class, () -> manager.getSubtaskByUuid(realTask.getUuid()));
        Assertions.assertThrows(RuntimeException.class, () -> manager.getSubtaskByUuid(epic1.getUuid()));
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
        Assertions.assertThrows(RuntimeException.class, () -> manager.getEpicByUuid(realTask.getUuid()));
        Assertions.assertThrows(RuntimeException.class, () -> manager.getEpicByUuid(subtask1.getUuid()));
    }

    @Test
    void getTaskByUuid() {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask();

        Epic epic2 = new Epic();
        Subtask subtask4 = new Subtask();
        Subtask subtask5 = new Subtask();
        Subtask subtask6 = new Subtask();

        RealTask realTask = new RealTask();

        manager.addEpicWithSubtask(epic2, subtask4, subtask5, subtask6);
        manager.addRealTask(realTask);

        System.out.println(manager.getTaskByUuid(realTask.getUuid())); //done
        Assertions.assertThrows(RuntimeException.class, () -> manager.getTaskByUuid(epic1.getUuid()));
        Assertions.assertThrows(RuntimeException.class, () -> manager.getTaskByUuid(subtask1.getUuid()));
    }

    @Test /*there is not two same Epic by one uuid in epicMap*/
    void addEpic() {
        Epic epic1 = new Epic();
        manager.addEpic(epic1);
        Assertions.assertThrows(RuntimeException.class, () -> manager.addEpic(epic1));
        Epic epic2 = new Epic();
        manager.addEpic(epic2);
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
    void addRealTask() {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask();
        RealTask realTask1 = new RealTask();
        RealTask realTask2 = new RealTask();

        manager.addRealTask(realTask1);
        Assertions.assertThrows(RuntimeException.class, () -> manager.addRealTask(realTask1));
        manager.addRealTask(realTask2);
    }

    @Test
    void addSubtaskInCreatedEpic() {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();

        manager.addEpic(epic1);
        manager.addSubtaskInCreatedEpic(epic1, subtask1);
        Assertions.assertThrows(RuntimeException.class, () -> manager.addSubtaskInCreatedEpic(epic1, subtask1));
        manager.addSubtaskInCreatedEpic(epic1, subtask2);
        Assertions.assertEquals(2, epic1.getSubtaskList().size());
    }

    @Test
    void changeTaskByUuid() {
        Subtask subtask1 = new Subtask();
        RealTask realTask1 = new RealTask();
        RealTask realTask2 = new RealTask();

        realTask2.setName("I'm real task");
        realTask2.setDescription("About something");
        realTask2.setStatus(StatusTask.DONE);

        Assertions.assertThrows(RuntimeException.class, () -> manager.changeTaskByUuid(realTask1.getUuid(), realTask2));
        manager.addRealTask(realTask1);
        System.out.println(realTask1.getName() + " " + realTask1.getDescription() + " " + realTask1.getStatus());

        Assertions.assertThrows(RuntimeException.class, () -> manager.changeTaskByUuid(subtask1.getUuid(), realTask2));
        manager.changeTaskByUuid(realTask1.getUuid(), realTask2);
        System.out.println(realTask1.getName() + " " + realTask1.getDescription() + " " + realTask1.getStatus());
    }

    @Test
    void changeEpicByUuid() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Epic epic4 = new Epic();
        Epic epic5 = new Epic();
        Subtask subtask1 = new Subtask();
        RealTask realTask1 = new RealTask();

        manager.changeEpicByUuid(epic1.getUuid(), epic2); //done exception was expected
        manager.addEpic(epic4);

        epic5.setStatus(StatusTask.DONE);
        epic5.setName("Find yourself");
        epic5.setDescription("Wake up at 8 am");
        manager.changeEpicByUuid(subtask1.getUuid(), epic5); //done exception was expected
        manager.changeEpicByUuid(realTask1.getUuid(), epic4);  //done exception was expected
        manager.changeEpicByUuid(epic4.getUuid(), epic5);
        System.out.println(epic4.getName() + " " + epic4.getDescription() + " " + epic4.getStatus());
    }

    @Test
    void changeSubtaskByUuid() {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();
        RealTask realTask1 = new RealTask();

        Assertions.assertThrows(RuntimeException.class, () -> manager.
                changeSubtaskByUuid(subtask1.getUuid(), subtask2));
        manager.addEpic(epic1);
        Assertions.assertThrows(RuntimeException.class, () -> manager.
                changeSubtaskByUuid(subtask1.getUuid(), subtask2));
        manager.addSubtaskInCreatedEpic(epic1, subtask1);
        subtask2.setStatus(StatusTask.IN_PROGRESS);
        subtask2.setName("My name is Subtask");
        subtask2.setDescription("I have to do something important");
        manager.changeSubtaskByUuid(subtask1.getUuid(), subtask2);
        System.out.println(subtask2.getName() + " " + subtask2.getDescription() + " " + subtask2.getStatus());
        Assertions.assertThrows(RuntimeException.class, () ->
                manager.changeSubtaskByUuid(subtask1.getUuid(), subtask1));
    }

    @Test
    void clearListTasks() {
        RealTask realTask1 = new RealTask();
        RealTask realTask2 = new RealTask();

        manager.addRealTask(realTask1);
        manager.addRealTask(realTask2);
        Assertions.assertEquals(2, manager.getListRealTask().size());
        manager.clearListTasks();
        Assertions.assertEquals(0, manager.getListRealTask().size());
    }

    @Test
    void clearListEpics() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Epic epic3 = new Epic();

        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addEpic(epic3);

        Assertions.assertEquals(3, manager.getListEpics().size());
        manager.clearListEpics();
        Assertions.assertEquals(0, manager.getListEpics().size());
    }

    @Test
    void clearListSubtasksInMap() {
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
        manager.addSubtaskInCreatedEpic(epic2, subtask4);
        manager.addSubtaskInCreatedEpic(epic2, subtask5);
        Assertions.assertEquals(2, epic2.getSubtaskList().size());

        manager.clearListSubtasksInMap();
        Assertions.assertEquals(0, epic1.getSubtaskList().size());
        Assertions.assertEquals(0, epic2.getSubtaskList().size());
    }

    @Test
    void clearListSubtasksInEpic() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();

        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);
        manager.clearListSubtasksInEpic(epic1);
        Assertions.assertEquals(0, epic1.getSubtaskList().size());
        Assertions.assertEquals(0, epic2.getSubtaskList().size());
    }

    @Test
    void clearTaskByUuid() {
        Epic epic = new Epic();
        RealTask realTask1 = new RealTask();
        RealTask realTask2 = new RealTask();
        Assertions.assertThrows(RuntimeException.class, () -> manager.clearTaskByUuid(realTask1.getUuid()));
        manager.addRealTask(realTask1);
        manager.addRealTask(realTask2);
        manager.clearTaskByUuid(realTask1.getUuid());
        Assertions.assertThrows(RuntimeException.class, () -> manager.clearTaskByUuid(epic.getUuid()));
    }

    @Test
    void clearSubtaskByUuid() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();
        Subtask subtask4 = new Subtask();
        Subtask subtask5 = new Subtask();

        Assertions.assertThrows(RuntimeException.class, () -> manager.clearSubtaskByUuid(epic1.getUuid()));
        Assertions.assertThrows(RuntimeException.class, () -> manager.clearSubtaskByUuid(subtask1.getUuid()));
        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);

        manager.addEpicWithSubtask(epic2, subtask5);
        manager.clearSubtaskByUuid(subtask5.getUuid());
        manager.getEpicByUuid(epic2.getUuid());

        manager.clearSubtaskByUuid(subtask2.getUuid());
        System.out.println(manager.getListSubtasksByEpic(epic1));
    }

    @Test
    void clearEpicByUuid() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask5 = new Subtask();

        Assertions.assertThrows(RuntimeException.class, () -> manager.clearEpicByUuid(epic1.getUuid()));
        manager.addEpic(epic1);
        manager.addEpicWithSubtask(epic2, subtask1, subtask2, subtask5);
        Assertions.assertEquals(2, manager.getListEpics().size());

        manager.clearEpicByUuid(epic1.getUuid());
        Assertions.assertEquals(1, manager.getListEpics().size());
        Assertions.assertEquals(0, manager.getListSubtasksByEpic(epic1).size());
    }
}