package com.ezh.taskbook.kvserver;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        KVServer kvServer = new KVServer();
        kvServer.start(); //port 8078
        kvServer.stop();
    }
}
