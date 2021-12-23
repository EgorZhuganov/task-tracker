package com;

import java.util.*;
import java.util.stream.Collectors;

public class Manager {

    Map<UUID, Epic> epicMap;
    List<Task> taskList;

    public Manager() {
        System.out.println("Начинаем строить грандиозный план!");
        this.epicMap = new HashMap<>();
        this.taskList = new ArrayList<>();
    }

    //TODO п.2.1 получение списка всех сабтасков +
    public List<Subtask> getListSubtasks() {
        List<Subtask> subtaskList = new ArrayList<>();
        epicMap.values().forEach(epic -> subtaskList.addAll(epic.getSubtaskList()));
        return subtaskList;
    }

    //TODO п.2.2 получение списка всех эпиков
    public List<Epic> getListEpics() {
        return new ArrayList<>(epicMap.values());
    }

    //TODO п.2.3 получение всех подзадач конкретного эпика
    public List<Subtask> getListSubtasksByEpicUuid(UUID epicUuid) {
        List<Epic> epicList = new ArrayList<>();
        epicMap.values().addAll(epicList);
        List<Subtask> subtaskList = new ArrayList<>();
        for (Epic currentEpic : epicList) {
            if (currentEpic.getUuid().equals(epicUuid)) {
                subtaskList = currentEpic.getSubtaskList();
            }
        }
        return subtaskList;
    }

    //TODO п.2.3 получение всех подзадач конкретного эпика
    public List<Subtask> getListSubtasksByEpic(Epic epic) {
        List<Subtask> subtaskList = new ArrayList<>();
        epicMap.values().forEach(ep -> ep.getSubtaskList().stream().
                filter(epi -> epi.getUuid().equals(epic.getUuid())).
                collect(Collectors.toList()).addAll(subtaskList));
        return subtaskList;
    }

    //TODO п.2.4 получение задачи типа Эпик или Сабтаск по идентификатору разделен на 2 метода
    public Task getEpicOrSubtaskByUuid(UUID uuid) { //не нравится что возвращает Таск, а не эпик или сабтаск отдельно
        List<Epic> epicList = new ArrayList<>();
        epicMap.values().addAll(epicList);
        Task task = new Task(); //или создать Epic и второй Subtask? в зависимости от резльтата писать, либо разбить на два метода
        //Epic epic = new Epic();
        //Subtask subtask =  new Subtask();
        for (Epic currentEpic : epicList) {
            if (uuid.equals(currentEpic.getUuid())) {
                task = currentEpic;
                break;
            } else {
                List<Subtask> subtaskList = currentEpic.getSubtaskList();
                for (Subtask subtask : subtaskList) {
                    if (uuid.equals(subtask.getUuid())) {
                        task = subtask;
                        break;
                    }
                }
            }
        }
        return task;
    }

    //TODO п.2.4 получение задачи типа Таск по идентификатору
    public Task getTaskByUuid(UUID uuid) {
        Task task = new Task();
        for (Task currentTask : taskList) {
            if (uuid.equals(currentTask.getUuid())) {
                task = currentTask;
                break;
            }
        }
        return task;
    }

    //TODO п.2.5 Добавление нового Эпика в мапу. Добавление новой задачи, эпика и подзадачи разделен на 4.
    public void addEpic(Epic epic) {
        this.epicMap.put(epic.getUuid(), epic);
    }

    //TODO п.2.5 Добавление нового сабтаска для эпика. Добавление новой задачи, эпика и подзадачи
    public void addEpicWithSubtask(Epic epic, Subtask... subtask) {
        epic.getSubtaskList().addAll(Arrays.asList(subtask));
        this.epicMap.put(epic.getUuid(), epic);
    }

    //TODO п.2.5 Добавление нового таска в таск лист. Добавление новой задачи, эпика и подзадачи
    public void addTask(Task... task) {
        this.taskList.addAll(Arrays.asList(task));
    }

    //TODO п.2.5 Добавление нового сабтастка в существующий эпик. Добавление новой задачи, эпика и подзадачи
    public void addSubtaskInCreatedEpic(Epic epic, Subtask subtask) {
        epic.getSubtaskList().add(subtask);
    }

    //TODO п.2.6 Обновление задачи любого типа по идентификатору. Новая версия объекта передаётся в виде параметра разделен на 2
    public void changeTaskByUuid(UUID uuid, Task newTask) {
        Task oldTask = this.getTaskByUuid(uuid);
        oldTask.setName(newTask.getName());
        oldTask.setDescription(newTask.getDescription());
        oldTask.setStatus(newTask.getStatus());
    }

    //TODO п.2.6 Обновление задачи любого типа по идентификатору. Новая версия объекта передаётся в виде параметра.
    public void changeEpicOrSubtask(UUID uuid, Epic newTask) {
        Task oldTask = this.getEpicOrSubtaskByUuid(uuid); //Тут тоже не нравится что работаем через Таску
        oldTask.setName(newTask.getName());
        oldTask.setDescription(newTask.getDescription());
        oldTask.setStatus(newTask.getStatus());
    }

    //TODO п.2.7 Удаление ранее добавленных тасков — всех и по идентификатору. разделен на 5
    public void clearListTasks() { //чистим лист тасков
        taskList.clear();
    }

    //TODO п.2.7 Удаление ранее добавленных эпиков с сабтасками, т.к. сабтаск без эпика не живет.
    public void clearListEpics() {
        epicMap.values().forEach(epic -> epic.getSubtaskList().clear());
        epicMap.values().clear();
    }

    //TODO п.2.7 Удаление ранее добавленных сабтасков, т.к. эпика живет без сабтаска.
    public void clearListSubtasks() {
        epicMap.values().forEach(epic -> epic.getSubtaskList().clear());
    }

    //TODO п.2.7 Удаление ранее добавленных задач по идентификатору.
    public void clearTaskByUuid(UUID uuid) {
        taskList.removeIf(t -> t.getUuid().equals(uuid));
    }

    //TODO п.2.7 Удаление ранее добавленных сабтасков по идентификатору.
    public void clearSubtaskByUuid(UUID uuid) {
        if (getEpicOrSubtaskByUuid(uuid).getClass().getName().equals("Subtask")) {
            epicMap.values().forEach(epic -> epic.getSubtaskList().
                    removeIf(subtask -> subtask.getUuid().equals(uuid)));
        } else {
            throw new IllegalArgumentException("This method cannot clear epic or other task by uuid only one subtask");
        }

    }

    //TODO п.2.7 Удаление ранее добавленных эпиков вместе с сабтасками, т.к. они сами по себе не живут по идентификатору.
    public void clearEpicByUuid(UUID uuid) {
        if (getEpicOrSubtaskByUuid(uuid).getClass().getName().equals("Epic")) {
            epicMap.values().forEach(e -> e.getSubtaskList().removeIf(epic -> epic.getUuid().equals(uuid)));
            epicMap.values().removeIf(epic -> epic.getUuid().equals(uuid));
        } else {
            throw new IllegalArgumentException("This method cannot clear task or subtask by uuid only one Epic with" +
                    "its subtask list");
        }
    }

    //TODO п.3.1 Управление стутусами задачи.

    //Получили статус лист для эпика
    public List<StatusTask> getListSubtaskStatus(Epic epic) {
        List<StatusTask> statusTaskList = new ArrayList<>();
        if (!getListSubtasks().isEmpty()) {
            epic.getSubtaskList().forEach(subtask -> statusTaskList.add(subtask.getStatus()));
        } else {
            return null;
        }
        return statusTaskList;
    }

    //TODO п.3.2. Управление статусами для эпиков
    public void changeEpicStatus(Epic epic) {
        if (getListSubtaskStatus(epic).isEmpty() &&
                getListSubtaskStatus(epic) != null ||
                getListSubtaskStatus(epic).contains(StatusTask.NEW)) {
            epic.setStatus(StatusTask.NEW);
        } else if (!getListSubtaskStatus(epic).isEmpty() &&
                getListSubtaskStatus(epic) != null &&
                !getListSubtaskStatus(epic).contains(StatusTask.NEW) &&
                !getListSubtaskStatus(epic).contains(StatusTask.IN_PROGRESS)) {
            epic.setStatus(StatusTask.DONE);
        } else {
            epic.setStatus(StatusTask.IN_PROGRESS);
        }
    }
}
