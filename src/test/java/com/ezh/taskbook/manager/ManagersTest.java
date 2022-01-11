package test.java.com.ezh.taskbook.manager;

import main.java.com.ezh.taskbook.manager.Managers;
import main.java.com.ezh.taskbook.manager.TaskManager;
import main.java.com.ezh.taskbook.task.Epic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void getDefault() {
        Managers manager = new Managers();
        TaskManager t = manager.getDefault();
        Epic epic = new Epic();
        t.addEpic(epic);
        t.getEpicMap();
        System.out.println(t.getEpicByUuid(epic.getUuid()));
        Assertions.assertEquals(1,t.getEpicMap().size());
    }
}