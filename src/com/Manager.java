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

    public List<Subtask> getListSubtasks() {
        List<Subtask> subtaskList = new ArrayList<>();
        epicMap.values().forEach(epic -> subtaskList.addAll(epic.getSubtaskList()));
        return subtaskList;
    }

    public List<Epic> getListEpics() {
        return new ArrayList<>(epicMap.values());
    }

    public List<RealTask> getListRealTask () { return  new ArrayList<>(taskList); }

    public List<Subtask> getListSubtasksByEpic(Epic epic) {
        List<Subtask> subtaskList = new ArrayList<>();
        for (Epic currentEpic : epicMap.values()) {
            if (currentEpic.getUuid().equals(epic.getUuid())) {
                subtaskList.addAll(currentEpic.getSubtaskList());
            }
        }
        return subtaskList;
    }

    public Epic getEpicByUuid(UUID uuid) {
        if (!epicMap.containsKey(uuid)) {
            throw new RuntimeException("This method accept only Epic key");
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
            throw new RuntimeException("Cannot find this UUID in subtaskList. " +
                    "taskList might be empty or incorrect uuid");
        }
        return subtask;
    }

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
        if (taskList.isEmpty()) {
            throw new RuntimeException("Before use this method need to add one or more RealTask in taskList");
        } else if (uuidFound == 0) {
            throw new RuntimeException("Cannot find this UUID in taskList");
        }
        return task;
    }

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

    public void addRealTask(RealTask task) {
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

    public void addSubtaskInCreatedEpic(Epic epic, Subtask subtask) {

        for (Subtask currentSubtask : epic.getSubtaskList()) {
            if(currentSubtask.getUuid().equals(subtask.getUuid())) {
                throw new RuntimeException("Have not to add two same Subtask in one Epic");
            }
        }
        epic.getSubtaskList().add(subtask);
        this.changeEpicStatus(epic);
    }

    //TODO необходим для 210 строки, подумать
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
            throw new RuntimeException("This method find only Epic by subtask uuid");
        }
        return epic;
    }

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

    /*Before change subtask you have to put in Map Epic with old Subtask*/
    public void changeEpicByUuid(UUID uuid, Epic newEpic) {
        if (epicMap.containsKey(uuid)) {
            Epic oldEpic = this.getEpicByUuid(uuid);
            oldEpic.setName(newEpic.getName());
            oldEpic.setDescription(newEpic.getDescription());
            changeEpicStatus(oldEpic);
        } else if (epicMap.isEmpty()) {
            try {
                throw new Exception("Need to add in epicMap one or more Epic, before use this method");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                throw new Exception("Cannot find this UUID in epicMap");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*Before change subtask you have to put in Map Epic with Subtask*/
    public void changeSubtaskByUuid(UUID uuid, Subtask newSubtask) {
        if (uuid.equals(newSubtask.getUuid())){
            throw new RuntimeException("Trying changing two subtasks with the same uuid");
        }
        Subtask oldSubtask = getSubtaskByUuid(uuid);
        oldSubtask.setName(newSubtask.getName());
        oldSubtask.setDescription(newSubtask.getDescription());
        oldSubtask.setStatus(newSubtask.getStatus());
        changeEpicStatus(findEpicBySubtaskUuid(uuid));
    }

    public void clearListTasks() {
        taskList.clear();
    }

    public void clearListEpics() {
        epicMap.values().forEach(epic -> epic.getSubtaskList().clear());
        epicMap.values().clear();
    }

    public void clearListSubtasksInMap() {
        epicMap.values().forEach(epic -> epic.getSubtaskList().clear());
    }

    public void clearListSubtasksInEpic(Epic epic) {
        epic.getSubtaskList().clear();
    }

    public void clearTaskByUuid(UUID uuid) {
        if (taskList.isEmpty() || !taskList.contains(getTaskByUuid(uuid))){
            throw new RuntimeException("Cannot find this uuid");
        }
        taskList.removeIf(t -> t.getUuid().equals(uuid));
    }

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
            throw new IllegalArgumentException("Cannot find this uuid.This method cannot clear epic or other task by " +
                    "uuid only one subtask");
        }
    }

    public void clearEpicByUuid(UUID uuid) {
        boolean done = epicMap.values().removeIf(epic -> epic.getUuid().equals(uuid));
        if (!done) {
            throw new IllegalArgumentException("Cannot find Epic with this uuid. This method cannot clear task or " +
                    "subtask by uuid only one Epic with its subtask list");
        }
    }

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

