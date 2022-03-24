package com.ezh.taskbook.manager;

import org.junit.jupiter.api.BeforeEach;

class InMemoryTasksManagerTest extends TaskManagerTest<InMemoryTasksManager> {

    @BeforeEach
    void setup(){
        manager = new InMemoryTasksManager();
    }
}