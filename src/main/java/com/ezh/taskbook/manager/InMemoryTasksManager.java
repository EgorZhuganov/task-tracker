package com.ezh.taskbook.manager;

import com.ezh.taskbook.task.AbstractTask;
import com.ezh.taskbook.task.Epic;
import com.ezh.taskbook.task.SingleTask;
import com.ezh.taskbook.task.Subtask;

import java.util.*;

public class InMemoryTasksManager implements TaskManager {

    private Map<UUID, Epic> epicMap;
    private List<SingleTask> taskList;
    private HistoryManager inMemoryHistoryManager;

    public InMemoryTasksManager() {
        System.out.println("Начинаем строить грандиозный план!");
        this.epicMap = new HashMap<>();
        this.taskList = new ArrayList<>();
        this.inMemoryHistoryManager = new InMemoryHistoryManager();
    }

    @Override
    public List<Subtask> getListSubtasks() {
        List<Subtask> subtaskList = new ArrayList<>();
        getEpicMap().values().forEach(epic -> subtaskList.addAll(epic.getSubtaskList()));
        return subtaskList;
    }

    @Override
    public List<Epic> getListEpics() {
        return new ArrayList<>(getEpicMap().values());
    }

    @Override
    public List<SingleTask> getListSingleTasks() {
        return new ArrayList<>(getSingleTaskList());
    }

    @Override
    public List<Subtask> getListSubtasksByEpic(Epic epic) {
        List<Subtask> subtaskList = new ArrayList<>();
        for (Epic currentEpic : getEpicMap().values()) {
            if (currentEpic.getUuid().equals(epic.getUuid())) {
                subtaskList.addAll(currentEpic.getSubtaskList());
            }
        }
        return subtaskList;
    }

    @Override
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
        inMemoryHistoryManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskByUuid(UUID uuid) {
        List<Epic> epicList = new ArrayList<>(getEpicMap().values());
        Subtask subtask = null;
        for (Epic currentEpic : epicList) {
            List<Subtask> subtaskList = currentEpic.getSubtaskList();
            for (Subtask currentSubtask : subtaskList) {
                if (uuid.equals(currentSubtask.getUuid())) {
                    subtask = currentSubtask;
                    break;
                }
            }
        }
        if (subtask == null) {
            throw new RuntimeException("Cannot find this UUID in subtaskList. " +
                    "taskList might be empty or incorrect uuid");
        }
        inMemoryHistoryManager.add(subtask);
        return subtask;
    }

    @Override
    public SingleTask getSingleTaskByUuid(UUID uuid) {
        SingleTask task = null;
        for (SingleTask currentTask : getSingleTaskList()) {
            if (uuid.equals(currentTask.getUuid())) {
                task = currentTask;
                break;
            }
        }
        if (getSingleTaskList().isEmpty()) {
            throw new RuntimeException("Before use this method need to add one or more RealTask in taskList");
        } else if (task == null) {
            throw new RuntimeException("Cannot find this UUID in taskList");
        }
        inMemoryHistoryManager.add(task);
        return task;
    }

    @Override
    public void addEpic(Epic epic) {
        if (!getEpicMap().containsKey(epic.getUuid())) {
            getEpicMap().put(epic.getUuid(), epic);
        } else {
            throw new RuntimeException("Have not to put two same Epic in Map");
        }
    }

    @Override
    public void addEpicWithSubtask(Epic epic, Subtask... subtask) {
        if (!getEpicMap().containsKey(epic.getUuid())) {
            epic.getSubtaskList().addAll(Arrays.asList(subtask));
            getEpicMap().put(epic.getUuid(), epic);
        } else {
            throw new RuntimeException("Have not to put two same Epic in Map");
        }
    }

    @Override
    public void addSingleTask(SingleTask task) {
        UUID uuid = task.getUuid();
        for (SingleTask currentTask : getSingleTaskList()) {
            if (currentTask.getUuid().equals(uuid)) {
                throw new RuntimeException("Have not to add two same RealTask in taskList");
            }
        }
        getSingleTaskList().add(task);
    }

    @Override
    public void addSubtaskInCreatedEpic(Epic epic, Subtask subtask) {
        for (Subtask currentSubtask : epic.getSubtaskList()) {
            if (currentSubtask.getUuid().equals(subtask.getUuid())) {
                throw new RuntimeException("Have not to add two same Subtask in one Epic");
            }
        }
        epic.getSubtaskList().add(subtask);
    }

    @Override
    public Epic getEpicBySubtaskUuid(UUID subtaskUuid) {
        Epic epic = null;
        for (Epic currentEpic : getEpicMap().values()) {
            for (Subtask subtask : currentEpic.getSubtaskList()) {
                if (subtask.getUuid().equals(subtaskUuid)) {
                    epic = currentEpic;
                    break;
                }
            }
        }
        if (epic == null) {
            throw new RuntimeException("This method find only Epic by subtask uuid");
        }
        return epic;
    }

    /*Before change RealTask you have to put in taskList one or more RealTask*/
    @Override
    public void changeSingleTaskByUuid(UUID uuid, SingleTask newTask) {
        if (getSingleTaskList().isEmpty()) {
            try {
                throw new Exception("Need to add in taskList one or more RealTask, before use this method");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SingleTask oldTask = this.getSingleTaskByUuid(uuid);
        oldTask.setName(newTask.getName());
        oldTask.setDescription(newTask.getDescription());
        oldTask.setStatus(newTask.getStatus());
    }

    /*Before change subtask you have to put in Map Epic with old Subtask*/
    @Override
    public void changeEpicByUuid(UUID uuid, Epic newEpic) {
        if (getEpicMap().containsKey(uuid)) {
            Epic oldEpic = this.getEpicByUuid(uuid);
            oldEpic.setName(newEpic.getName());
            oldEpic.setDescription(newEpic.getDescription());
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
    @Override
    public void changeSubtaskByUuid(UUID uuid, Subtask newSubtask) {
        if (uuid.equals(newSubtask.getUuid())) {
            throw new RuntimeException("Trying changing two subtasks with the same uuid");
        }
        Subtask oldSubtask = getSubtaskByUuid(uuid);
        oldSubtask.setName(newSubtask.getName());
        oldSubtask.setDescription(newSubtask.getDescription());
        oldSubtask.setStatus(newSubtask.getStatus());
    }

    @Override
    public void clearListSingleTasks() {
        getSingleTaskList().clear();
    }

    @Override
    public void clearListEpics() {
        getEpicMap().values().forEach(epic -> epic.getSubtaskList().clear());
        getEpicMap().values().clear();
    }

    @Override
    public void clearListSubtasksInMap() {
        getEpicMap().values().forEach(epic -> epic.getSubtaskList().clear());
    }

    @Override
    public void clearListSubtasksInEpic(Epic epic) {
        epic.getSubtaskList().clear();
    }

    @Override
    public void removeSingleTaskByUuid(UUID uuid) {
        if (getSingleTaskList().isEmpty() || !getSingleTaskList().contains(getSingleTaskByUuid(uuid))) {
            throw new RuntimeException("Cannot find this uuid");
        }
        getSingleTaskList().removeIf(t -> t.getUuid().equals(uuid));
    }

    @Override
    public void removeSubtaskByUuid(UUID uuid) {
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

    @Override
    public void removeEpicByUuid(UUID uuid) {
        boolean done = getEpicMap().values().removeIf(epic -> epic.getUuid().equals(uuid));
        if (!done) {
            throw new IllegalArgumentException("Cannot find Epic with this uuid. This method cannot clear task or " +
                    "subtask by uuid only one Epic with its subtask list");
        }
    }

    @Override
    public List<AbstractTask> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }

    @Override
    public void removeTaskFromHistory(UUID id) {
        inMemoryHistoryManager.remove(id);
    }

    @Override
    public Map<UUID, Epic> getEpicMap() {
        return epicMap;
    }

    @Override
    public List<SingleTask> getSingleTaskList() {
        return taskList;
    }


}

