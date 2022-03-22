package com.ezh.taskbook.manager;

import org.junit.jupiter.api.BeforeEach;

class InMemoryTasksManagerTest_v2 extends TaskManagerTest<InMemoryTasksManager> {

    @BeforeEach
    void setup(){
        manager = new InMemoryTasksManager();
    }
}