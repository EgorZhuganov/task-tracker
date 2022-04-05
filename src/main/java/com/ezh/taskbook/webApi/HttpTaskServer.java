package com.ezh.taskbook.webApi;

import com.ezh.taskbook.manager.FileBackedTasksManager;
import com.ezh.taskbook.manager.TaskManager;
import com.ezh.taskbook.webApi.hendler.AllTasksHandler;
import com.ezh.taskbook.webApi.hendler.HistoryHandler;
import com.ezh.taskbook.webApi.hendler.ListSingleTaskHandler;
import com.ezh.taskbook.webApi.hendler.SingleTaskHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;

public class HttpTaskServer { //Server

    static final int PORT = 8080;
    HttpServer server;
    TaskManager manager;

    public HttpTaskServer(TaskManager taskManager) {
        this.manager = taskManager;
        try {
            server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        } catch (IOException e) {
            System.out.println("Error when creating a server with a port number" + PORT);
            e.printStackTrace();
        }
        createAllContext();
    }

    public static void main(String[] args) {
        HttpTaskServer server = new HttpTaskServer(FileBackedTasksManager.loadFromFile(new File("test.txt")));
        server.start();
    }

    public void start() {
        server.start();
        System.out.println("Server starts");
    }

    private void createAllContext() {

        server.createContext("/tasks/", new AllTasksHandler(manager));
        server.createContext("/tasks/history", new HistoryHandler(manager));
        server.createContext("/tasks/single-task", new ListSingleTaskHandler(manager));
        server.createContext("/tasks/single-task/", new SingleTaskHandler(manager));

    }


    //GET /tasks/single-task/                    = getListSingleTasks()
    //GET /tasks/single-task/?id=                = getSingleTaskByUuid
    //POST /tasks/single-task/ Body: {task: }    = addSingleTask // addSubtaskInAddedEpic(Epic epic, Subtask subtask);
    //PUT  /tasks/single-task/?d= Body: {task: } = changeSingleTaskByUuid
    //DELETE /tasks/single-task/?d=              = removeSingleTaskByUuid
    //DELETE /tasks/single-task/                 = clearSingleTasks
    //_________________________________ для 3-ех видов таски 6*3 = 18 методов
    //плюс еще эти методы:
    //GET /tasks/subtask/epic/?id=              = getListSubtasksByEpicId  -> можно отнести к сабтаскам
    //GET /tasks/history                        = getHistory()          +
    //GET /tasks/                               = getPrioritizedTasks() +
    //_________________________________ для 3-ех видов таски 18+3 = 21 метод
    //DELETE /tasks/subtask/epic/?id=           = clearSubtasksInEpicByEpicUuid -> можно отнести к сабтаскам
    //POST   /tasks/epic-and-subtask Body: {task: } = addEpicWithSubtask(Epic epic, Subtask... subtask) (что передавать в тело???)
    //GET    /tasks/epic/subtask/?id=           = getEpicBySubtaskUuid -> отнести к Epic
    //_________________________________ все 24-е метода

    //методы Post - создание новой таски код 201 - Created - созданы один или несколько новых тасок
    //Get - получение таски тасок - 200 - ОК - запрос успешно обработан и результат возвращен в теле ответа
    //Put - обновление - 204 - No content
    //Delete - удалить - 204 - No content - запрос успешно бработан и нет никаких данных для возврата. Тело ответа проверять не нужно оно будет пустым

    //Коды клиентских ошибок
    //400 - Bad Request - сервер не понимает запрос или пытается его обработаь но не может выполнить из-за того, что какой-то аспект не верене
    //401 - Unauthorized - Для выполнения запроса нужна аутентификация но вместе с запросом не были переданы авторизацонные данные
    //404 - Not Found - Сервер не может найти запрашиваемый ресурс

    //Коды серверных ошибок
    //501  - Not Implemented Серверу неизвестен HTTP-метод, использованный в запросе, поэтому запрос невозможно обработать.
    //503 - Service Unavailable	Сервер не может сейчас обработать запрос, поскольку сильно загружен, отключён или перезагружается.
}
