package com;

import java.util.*;

public class Manager {

    static Map<UUID, Epic> epicMap;
    static List<RealTask> taskList;

    public Manager() {
        System.out.println("Начинаем строить грандиозный план!");
        Manager.epicMap = new HashMap<>();
        Manager.taskList = new ArrayList<>();
    }

    //TODO п.2.1 получение списка всех сабтасков +
    public List<Subtask> getListSubtasks() {
        List<Subtask> subtaskList = new ArrayList<>();
        epicMap.values().forEach(epic -> subtaskList.addAll(epic.getSubtaskList()));
        return subtaskList;
    }

    //TODO п.2.2 получение списка всех эпиков +
    public List<Epic> getListEpics() {
        return new ArrayList<>(epicMap.values());
    }

    //TODO п.2.3 получение всех подзадач конкретного эпика +
    public List<Subtask> getListSubtasksByEpic(Epic epic) {
        List<Subtask> subtaskList = new ArrayList<>();
        for (Epic currentEpic : epicMap.values()) {
            if (currentEpic.getUuid().equals(epic.getUuid())) {
                subtaskList.addAll(currentEpic.getSubtaskList());
            }
        }
        return subtaskList;
    }

    //TODO п.2.4 получение задачи любого типа по идентификатору разделен на 3 метода
    public Epic getEpicByUuid(UUID uuid) {
        if (!epicMap.containsKey(uuid)) {
            try {
                throw new Exception("This method accept only Epic key");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        List<Epic> epicList = new ArrayList<>(epicMap.values());
        Epic epic = new Epic();
        for (Epic currentEpic : epicList) {
            if (uuid.equals(currentEpic.getUuid())) {
                epic = currentEpic;
            }
        }
        return epic;
    }

    //TODO п.2.4 получение сабтаски по индентификатору
    public Subtask getSubtaskByUuid(UUID uuid) {
        List<Epic> epicList = new ArrayList<>(epicMap.values());
        Subtask subtask = new Subtask();
        int uuidFound = 0;
        for (Epic currentEpic : epicList) {
            List<Subtask> subtaskList = currentEpic.getSubtaskList();
            for (Subtask currentSubtask : subtaskList) {
                if (uuid.equals(currentSubtask.getUuid())) {
                    subtask = currentSubtask;
                    uuidFound++;
                    break;
                }
            }
        }
        if (uuidFound == 0) {
            try {
                throw new Exception("This method accept only Subtask key");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return subtask;
    }

    //TODO п.2.4 получение задачи типа Таск по идентификатору
    public RealTask getTaskByUuid(UUID uuid) {
        RealTask task = new RealTask();
        int uuidFound = 0;
        for (RealTask currentTask : taskList) {
            if (uuid.equals(currentTask.getUuid())) {
                task = currentTask;
                uuidFound++;
                break;
            }
        }
        if (uuidFound == 0) {
            try {
                throw new Exception("This method accept only RealTask key");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return task;
    }

    //TODO п.2.5 Добавление нового Эпика в мапу. Добавление новой задачи, эпика и подзадачи разделен на 4. +
    public void addEpic(Epic epic) {
        if (!epicMap.containsKey(epic.getUuid())) {
            this.changeEpicStatus(epic);
            Manager.epicMap.put(epic.getUuid(), epic);
        } else {
            try {
                throw new Exception("Have not to put two same Epic in Map");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //TODO п.2.5 Добавление эпика сразу с сабтасками  +
    public void addEpicWithSubtask(Epic epic, Subtask... subtask) {
        if (!epicMap.containsKey(epic.getUuid())) {
            epic.getSubtaskList().addAll(Arrays.asList(subtask));
            this.changeEpicStatus(epic);
            Manager.epicMap.put(epic.getUuid(), epic);
        } else {
            try {
                throw new Exception("Have not to put two same Epic in Map");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //TODO п.2.5 Добавление нового таска в таск лист.  +
    public void addTask(RealTask task) {
        UUID uuid = task.getUuid();
        for (RealTask currentTask : taskList) {
            if (currentTask.getUuid().equals(uuid)) {
                try {
                    throw new Exception("Have not to add two same RealTask in taskList");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        Manager.taskList.add(task);
    }

    //TODO п.2.5 Добавление нового сабтастка в существующий эпик.  +
    public void addSubtaskInCreatedEpic(Epic epic, Subtask subtask) {
        epic.getSubtaskList().add(subtask);
        this.changeEpicStatus(epic);
    }

    //TODO нет такого пунтка
    public Epic findEpicBySubtaskUuid(UUID subtaskUuid) {
        Epic epic = new Epic();
        int uuidFound = 0;
        for (Epic currentEpic : epicMap.values()) {
            for (Subtask subtask : currentEpic.getSubtaskList()) {
                if (subtask.getUuid().equals(subtaskUuid)) {
                    epic = currentEpic;
                    uuidFound++;
                }
            }
        }
        if (uuidFound == 0) {
            try {
                throw new Exception("This method find only Epic by subtask uuid");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return epic;
    }

    //TODO п.2.6 Обновление задачи любого типа по идентификатору. Новая версия объекта передаётся в виде параметра разделен на 3 +
    /*Before change RealTask you have to put in taskList one or more RealTask*/
    public void changeTaskByUuid(UUID uuid, RealTask newTask) {
        if (taskList.isEmpty()) {
            try {
                throw new Exception("Need to add in taskList one or more RealTask, before use this method");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        RealTask oldTask = this.getTaskByUuid(uuid);
        oldTask.setName(newTask.getName());
        oldTask.setDescription(newTask.getDescription());
        oldTask.setStatus(newTask.getStatus());
    }

    //TODO п.2.6 Обновление задачи любого типа по идентификатору. Новая версия объекта передаётся в виде параметра. +
    /*Before change subtask you have to put in Map Epic with old Subtask*/
    public void changeEpicByUuid(UUID uuid, Epic newEpic) {
        if (epicMap.containsKey(uuid)) {
            Epic oldEpic = this.getEpicByUuid(uuid);
            oldEpic.setName(newEpic.getName());
            oldEpic.setDescription(newEpic.getDescription());
            oldEpic.setStatus(newEpic.getStatus());
            changeEpicStatus(oldEpic);
        } else if (epicMap.isEmpty()) {
            try {
                throw new Exception("Need to add in epicMap one or more Epic, before use this method");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                throw new Exception("This is Epic have not found in Map");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //TODO п.2.6 Обновление задачи любого типа по идентификатору. Новая версия объекта передаётся в виде параметра. +
    /*Before change subtask you have to put in Map Epic with Subtask*/
    public void changeSubtaskByUuid(UUID uuid, Subtask newSubtask) {
        Subtask oldSubtask = getSubtaskByUuid(uuid);
        oldSubtask.setName(newSubtask.getName());
        oldSubtask.setDescription(newSubtask.getDescription());
        oldSubtask.setStatus(newSubtask.getStatus());
        changeEpicStatus(findEpicBySubtaskUuid(uuid));
    }

    //TODO п.2.7 Удаление ранее добавленных тасков — всех и по идентификатору. разделен на 5 +
    public void clearListTasks() { //чистим лист тасков
        taskList.clear();
    }

    //TODO п.2.7 Удаление ранее добавленных эпиков с сабтасками, т.к. сабтаск без эпика не живет. +
    public void clearListEpics() {
        epicMap.values().forEach(epic -> epic.getSubtaskList().clear());
        epicMap.values().clear();
    }

    //TODO п.2.7 Удаление ранее добавленных сабтасков, т.к. эпик живет без сабтаска. +
    public void clearListSubtasks() {
        epicMap.values().forEach(epic -> epic.getSubtaskList().clear());
    }

    //TODO п.2.7 Удаление ранее добавленных задач по идентификатору. +
    public void clearTaskByUuid(UUID uuid) {
        taskList.removeIf(t -> t.getUuid().equals(uuid));
    }

    //TODO п.2.7 Удаление ранее добавленных сабтасков по идентификатору.+
    public void clearSubtaskByUuid(UUID uuid) {
        boolean done = false;
        for (Epic epic : epicMap.values()) {
            boolean deleteSubtask = epic.getSubtaskList().
                    removeIf(currentSubtask -> currentSubtask.getUuid().equals(uuid));
            if (deleteSubtask) {
                done = true;
            }
        }
        if (!done) {
            throw new IllegalArgumentException("This method cannot clear epic or other task by uuid only one subtask");
        }
    }

    //TODO п.2.7 Удаление ранее добавленных эпиков вместе с сабтасками, т.к. они сами по себе не живут по идентификатору.+
    public void clearEpicByUuid(UUID uuid) {
        boolean done = epicMap.values().removeIf(epic -> epic.getUuid().equals(uuid));
        if (!done) {
            throw new IllegalArgumentException("Cannot find Epic with this uuid. This method cannot clear task or " +
                    "subtask by uuid only one Epic with its subtask list");
        }
    }

    //TODO п.3.1 Управление стутусами задачи.
    public void changeEpicStatus(Epic epic) {
        if (epic.getSubtaskList().isEmpty()) {
            epic.setStatus(StatusTask.NEW);
        } else if (!epic.getSubtaskList().isEmpty()) {
            int counterNew = 0;
            int counterDone = 0;
            List<Subtask> subtaskList = epic.getSubtaskList();
            for (Subtask currentSubtask : subtaskList) {
                if (currentSubtask.getStatus().equals(StatusTask.NEW)) {
                    counterNew++;
                } else if (currentSubtask.getStatus().equals(StatusTask.DONE)) {
                    counterDone++;
                }
            }
            if (counterDone == subtaskList.size()) {
                epic.setStatus(StatusTask.DONE);
            } else if (counterNew == subtaskList.size()) {
                epic.setStatus(StatusTask.NEW);
            } else {
                epic.setStatus(StatusTask.IN_PROGRESS);
            }
        }
    }
}

