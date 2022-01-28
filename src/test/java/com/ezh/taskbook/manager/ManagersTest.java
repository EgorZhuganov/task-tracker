package com.ezh.taskbook.manager;

import com.ezh.taskbook.task.Epic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ManagersTest {

    @Test
    void getDefault() {
        TaskManager manager = new Managers().getDefault();
        Epic epic = new Epic();
        manager.addEpic(epic);
        manager.getEpicMap();
        System.out.println(manager.getEpicByUuid(epic.getUuid()));
        Assertions.assertEquals(1,manager.getEpicMap().size());
    }
}