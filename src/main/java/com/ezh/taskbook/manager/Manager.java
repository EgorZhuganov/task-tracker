package main.java.com.ezh.taskbook.manager;

import main.java.com.ezh.taskbook.task.Epic;
import main.java.com.ezh.taskbook.task.RealTask;
import main.java.com.ezh.taskbook.task.StatusTask;
import main.java.com.ezh.taskbook.task.Subtask;

import java.util.*;

public class Manager {

    private Map<UUID, Epic> epicMap;
    private List<RealTask> taskList;

    public Manager() {
        System.out.println("Начинаем строить грандиозный план!");
        this.epicMap = new HashMap<>();
        this.taskList = new ArrayList<>();
    }

    public List<Subtask> getListSubtasks() {
        List<Subtask> subtaskList = new ArrayList<>();
        getEpicMap().values().forEach(epic -> subtaskList.addAll(epic.getSubtaskList()));
        return subtaskList;
    }

    public List<Epic> getListEpics() {
        return new ArrayList<>(getEpicMap().values());
    }

    public List<RealTask> getListRealTask() {
        return new ArrayList<>(getTaskList());
    }

    public List<Subtask> getListSubtasksByEpic(Epic epic) {
        List<Subtask> subtaskList = new ArrayList<>();
        for (Epic currentEpic : getEpicMap().values()) {
            if (currentEpic.getUuid().equals(epic.getUuid())) {
                subtaskList.addAll(currentEpic.getSubtaskList());
            }
        }
        return subtaskList;
    }

    public Epic getEpicByUuid(UUID uuid) {
        if (!getEpicMap().containsKey(uuid)) {
            throw new RuntimeException("This method accept only Epic key");
        }
        List<Epic> epicList = new ArrayList<>(getEpicMap().values());
        Epic epic = new Epic();
        for (Epic currentEpic : epicList) {
            if (uuid.equals(currentEpic.getUuid())) {
                epic = currentEpic;
            }
        }
        return epic;
    }

    public Subtask getSubtaskByUuid(UUID uuid) {
        List<Epic> epicList = new ArrayList<>(getEpicMap().values());
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
        for (RealTask currentTask : getTaskList()) {
            if (uuid.equals(currentTask.getUuid())) {
                task = currentTask;
                uuidFound++;
                break;
            }
        }
        if (getTaskList().isEmpty()) {
            throw new RuntimeException("Before use this method need to add one or more RealTask in taskList");
        } else if (uuidFound == 0) {
            throw new RuntimeException("Cannot find this UUID in taskList");
        }
        return task;
    }

    public void addEpic(Epic epic) {
        if (!getEpicMap().containsKey(epic.getUuid())) {
            this.changeEpicStatus(epic);
            getEpicMap().put(epic.getUuid(), epic);
        } else {
            throw new RuntimeException("Have not to put two same Epic in Map");
        }
    }

    public void addEpicWithSubtask(Epic epic, Subtask... subtask) {
        if (!getEpicMap().containsKey(epic.getUuid())) {
            epic.getSubtaskList().addAll(Arrays.asList(subtask));
            this.changeEpicStatus(epic);
            getEpicMap().put(epic.getUuid(), epic);
        } else {
            throw new RuntimeException("Have not to put two same Epic in Map");
        }
    }

    public void addRealTask(RealTask task) {
        UUID uuid = task.getUuid();
        for (RealTask currentTask : getTaskList()) {
            if (currentTask.getUuid().equals(uuid)) {
                throw new RuntimeException("Have not to add two same RealTask in taskList");
            }
        }
        getTaskList().add(task);
    }

    public void addSubtaskInCreatedEpic(Epic epic, Subtask subtask) {

        for (Subtask currentSubtask : epic.getSubtaskList()) {
            if (currentSubtask.getUuid().equals(subtask.getUuid())) {
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
        for (Epic currentEpic : getEpicMap().values()) {
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
        if (getTaskList().isEmpty()) {
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
        if (getEpicMap().containsKey(uuid)) {
            Epic oldEpic = this.getEpicByUuid(uuid);
            oldEpic.setName(newEpic.getName());
            oldEpic.setDescription(newEpic.getDescription());
            changeEpicStatus(oldEpic);
        } else if (getEpicMap().isEmpty()) {
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
        if (uuid.equals(newSubtask.getUuid())) {
            throw new RuntimeException("Trying changing two subtasks with the same uuid");
        }
        Subtask oldSubtask = getSubtaskByUuid(uuid);
        oldSubtask.setName(newSubtask.getName());
        oldSubtask.setDescription(newSubtask.getDescription());
        oldSubtask.setStatus(newSubtask.getStatus());
        changeEpicStatus(findEpicBySubtaskUuid(uuid));
    }

    public void clearListTasks() {
        getTaskList().clear();
    }

    public void clearListEpics() {
        getEpicMap().values().forEach(epic -> epic.getSubtaskList().clear());
        getEpicMap().values().clear();
    }

    public void clearListSubtasksInMap() {
        getEpicMap().values().forEach(epic -> epic.getSubtaskList().clear());
    }

    public void clearListSubtasksInEpic(Epic epic) {
        epic.getSubtaskList().clear();
    }

    public void clearTaskByUuid(UUID uuid) {
        if (getTaskList().isEmpty() || !getTaskList().contains(getTaskByUuid(uuid))) {
            throw new RuntimeException("Cannot find this uuid");
        }
        getTaskList().removeIf(t -> t.getUuid().equals(uuid));
    }

    public void clearSubtaskByUuid(UUID uuid) {
        boolean done = false;
        for (Epic epic : getEpicMap().values()) {
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
        boolean done = getEpicMap().values().removeIf(epic -> epic.getUuid().equals(uuid));
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

    public Map<UUID, Epic> getEpicMap() {
        return epicMap;
    }

    public void setEpicMap(Map<UUID, Epic> epicMap) {
        this.epicMap = epicMap;
    }

    public List<RealTask> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<RealTask> taskList) {
        this.taskList = taskList;
    }
}

