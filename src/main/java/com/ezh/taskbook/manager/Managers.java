package com.ezh.taskbook.manager;

import java.net.URI;

public class Managers {

    public TaskManager getDefault() {
        return new HttpTaskManager(URI.create("http://localhost:8078/"),"8080");
    }
}
