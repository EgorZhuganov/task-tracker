package test.java.com.ezh.taskbook.manager;

import main.java.com.ezh.taskbook.manager.Manager;
import main.java.com.ezh.taskbook.task.Epic;
import main.java.com.ezh.taskbook.task.RealTask;
import main.java.com.ezh.taskbook.task.StatusTask;
import main.java.com.ezh.taskbook.task.Subtask;
import org.junit.jupiter.api.Test;

/*Код закомментировал потому что из-за него падают тесты, то есть то, что мне нужно сейчас и будет нужно в будущем для
* проверки. Пока не знаю, как правильно задать в тестировании, что упавший тест - это норма и ожидаемое поведение, т.е.
* что мне необходимо и не нужно подсвечивать упавший тест знаком (!).*/

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
//        System.out.println(manager.getEpicByUuid(realTask.getUuid())); //done an exception was expected
//        System.out.println(manager.getEpicByUuid(subtask1.getUuid())); //done an exception was expected
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
//        manager.addEpic(epic1); //done an exception was expected, there is not two same Epic by one uuid in epicMap
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
//        manager.addEpicWithSubtask(epic3, subtask7); //done an exception was expected, there is not two same Epic by one uuid in epicMap
//        manager.addEpicWithSubtask(epic1, subtask2); //done an exception was expected
//        manager.addEpicWithSubtask(epic3, subtask4); //done unable to add Epic even if it has subtask, which absent in Epic

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
//        manager.addRealTask(realTask1); //done an exception was expected
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
        Epic epic2 = new Epic();
        Epic epic4 = new Epic();
        Epic epic5 = new Epic();
        Epic epic6 = new Epic();
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();
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

//        manager.changeSubtaskByUuid(subtask1.getUuid(), subtask2); //done exception was expected
        manager.addEpic(epic1);
//        manager.changeSubtaskByUuid(subtask1.getUuid(), subtask2); //done exception was expected
        manager.addSubtaskInCreatedEpic(epic1, subtask1);
        subtask2.setStatus(StatusTask.IN_PROGRESS);
        subtask2.setName("My name is Subtask");
        subtask2.setDescription("I have to do something important");
        manager.changeSubtaskByUuid(subtask1.getUuid(), subtask2);
        System.out.println(subtask2.getName() + " " + subtask2.getDescription() + " " + subtask2.getStatus());
//        manager.changeSubtaskByUuid(subtask1.getUuid(), subtask1); //done exception was expected
    }

    @Test
    void clearListTasks() {
        RealTask realTask1 = new RealTask();
        RealTask realTask2 = new RealTask();

        System.out.println(manager.getListRealTask().size());
        manager.addRealTask(realTask1);
        manager.addRealTask(realTask2);
        System.out.println(manager.getListRealTask().size()); //2
        manager.clearListTasks();
        System.out.println(manager.getListRealTask().size()); //0
    }

    @Test
    void clearListEpics() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Epic epic3 = new Epic();

        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addEpic(epic3);

        System.out.println(manager.getListEpics().size());
        manager.clearListEpics();
        System.out.println(manager.getListEpics().size());
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
        Subtask subtask6 = new Subtask();

        System.out.println(epic1.getSubtaskList().size());
        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);
        System.out.println(epic1.getSubtaskList().size());

        System.out.println(epic2.getSubtaskList().size());
        manager.addEpic(epic2);
        manager.addSubtaskInCreatedEpic(epic2, subtask4);
        manager.addSubtaskInCreatedEpic(epic2, subtask5);
        System.out.println(epic2.getSubtaskList().size());

        manager.clearListSubtasksInMap();
        System.out.println(epic1.getSubtaskList().size());
        System.out.println(epic2.getSubtaskList().size());
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
        System.out.println(epic1.getSubtaskList().size());
        System.out.println(epic2.getSubtaskList().size());
    }

    @Test
    void clearTaskByUuid() {
        Epic epic = new Epic();
        RealTask realTask1 = new RealTask();
        RealTask realTask2 = new RealTask();
//        manager.clearTaskByUuid(realTask1.getUuid()); // done exception was expected
        manager.addRealTask(realTask1);
        manager.addRealTask(realTask2);
        manager.clearTaskByUuid(realTask1.getUuid());
//        manager.clearTaskByUuid(epic.getUuid()); // done done exception was expected
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

//        manager.clearSubtaskByUuid(epic1.getUuid()); // done done exception was expected
//        manager.clearSubtaskByUuid(subtask1.getUuid()); // done exception was expected
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
        Subtask subtask3 = new Subtask();
        Subtask subtask4 = new Subtask();
        Subtask subtask5 = new Subtask();

//        manager.clearEpicByUuid(epic1.getUuid()); // done exception was expected
        manager.addEpic(epic1);
        manager.addEpicWithSubtask(epic2, subtask1, subtask2, subtask5);
        System.out.println(manager.getListEpics().size()); //done 2

        manager.clearEpicByUuid(epic1.getUuid());
        System.out.println(manager.getListEpics().size()); //done 1
        System.out.println(manager.getListSubtasksByEpic(epic1).size()); //done 0
    }

    @Test
    void changeEpicStatus() {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();
        Subtask subtask4 = new Subtask();
        Subtask subtask5 = new Subtask();

        manager.addEpic(epic1);
        System.out.println(epic1.getStatus()); //done NEW without subtasks

        manager.addSubtaskInCreatedEpic(epic1, subtask1);
        System.out.println(epic1.getStatus()); //done NEW with subtask status NEW

        subtask2.setStatus(StatusTask.DONE);
        manager.changeSubtaskByUuid(subtask1.getUuid(), subtask2);
        System.out.println(epic1.getStatus()); //done DONE with subtask changed from NEW to DONE

        manager.addSubtaskInCreatedEpic(epic1, subtask3);
        System.out.println(epic1.getStatus()); //done IN_PROGRESS if add new subtask with status NEW

        subtask4.setStatus(StatusTask.DONE);
        manager.changeSubtaskByUuid(subtask3.getUuid(), subtask4);
        System.out.println(epic1.getStatus()); //done DONE if change status subtask, status Epic also change
    }
}