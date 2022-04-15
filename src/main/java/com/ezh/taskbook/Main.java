package com.ezh.taskbook;

import com.ezh.taskbook.kvserver.KVServer;
import com.ezh.taskbook.manager.Managers;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        KVServer kvServer = new KVServer(8078);
        kvServer.start();

        Managers managers = new Managers();
        managers.getDefault();

        kvServer.stop();
    }
}
