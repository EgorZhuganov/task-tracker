package com.ezh.taskbook.manager;

import com.ezh.taskbook.exception.TaskNotFoundException;
import com.ezh.taskbook.kvserver.KVServer;
import com.ezh.taskbook.task.Epic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class ManagersTest {

    @Test
    void getDefault() throws TaskNotFoundException, IOException {
        KVServer server = new KVServer();
        server.start();
        TaskManager manager = new Managers().getDefault(); //если дефолтное значение это HttpTaskManager
        Epic epic = new Epic();
        manager.addEpic(epic);
        manager.getListEpics();
        System.out.println(manager.getEpicByUuid(epic.getUuid()));
        Assertions.assertEquals(1, manager.getListEpics().size());
        server.stop();
    }
}