package com.ezh.taskbook.manager;

import com.ezh.taskbook.task.AbstractTask;
import com.ezh.taskbook.task.Epic;
import com.ezh.taskbook.task.SingleTask;
import com.ezh.taskbook.task.Subtask;

import java.util.*;

public class InMemoryTasksManager implements TaskManager {

    private Map<UUID, Epic> epicMap;
    private List<SingleTask> singleTaskList;
    private HistoryManager inMemoryHistoryManager;

    public InMemoryTasksManager() {
        System.out.println("Начинаем строить грандиозный план!");
        this.epicMap = new HashMap<>();
        this.singleTaskList = new ArrayList<>();
        this.inMemoryHistoryManager = new InMemoryHistoryManager();
    }

    @Override
    public List<Subtask> getListSubtasks() {
        List<Subtask> subtaskList = new ArrayList<>();
        epicMap.values().forEach(epic -> subtaskList.addAll(epic.getSubtaskList()));
        return subtaskList;
    }

    @Override
    public List<Epic> getListEpics() {
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public List<SingleTask> getListSingleTasks() {
        return new ArrayList<>(singleTaskList);
    }

    @Override
    public List<Subtask> getListSubtasksByEpic(Epic epic) {
        List<Subtask> subtaskList = new ArrayList<>();
        for (Epic currentEpic : epicMap.values()) {
            if (currentEpic.getUuid().equals(epic.getUuid())) {
                subtaskList.addAll(currentEpic.getSubtaskList());
            }
        }
        return subtaskList;
    }

    @Override
    public Epic getEpicByUuid(UUID uuid) {
        if (!epicMap.containsKey(uuid)) {
            throw new RuntimeException("Can not find Epic with this UUID");
        }
        inMemoryHistoryManager.add(epicMap.get(uuid));
        return epicMap.get(uuid);
    }

    @Override
    public Subtask getSubtaskByUuid(UUID uuid) {
        Subtask subtask = null;
        OUTER:
        for (Epic currentEpic : epicMap.values()) {
            List<Subtask> subtaskList = currentEpic.getSubtaskList();
            for (Subtask currentSubtask : subtaskList) {
                if (uuid.equals(currentSubtask.getUuid())) {
                    subtask = currentSubtask;
                    break OUTER;
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
        for (SingleTask currentTask : singleTaskList) {
            if (uuid.equals(currentTask.getUuid())) {
                task = currentTask;
                break;
            }
        }
        if (singleTaskList.isEmpty()) {
            throw new RuntimeException("Before use this method need to add one or more SingleTask");
        } else if (task == null) {
            throw new RuntimeException("Cannot find this UUID");
        }
        inMemoryHistoryManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicBySubtaskUuid(UUID subtaskUuid) {
        Epic epic = null;
        OUTER:
        for (Epic currentEpic : epicMap.values()) {
            for (Subtask subtask : currentEpic.getSubtaskList()) {
                if (subtask.getUuid().equals(subtaskUuid)) {
                    epic = currentEpic;
                    break OUTER;
                }
            }
        }
        if (epic == null) {
            throw new RuntimeException("This uuid must be subtask uuid and subtask must belong to Epic");
        }
        inMemoryHistoryManager.add(epic);
        return epic;
    }

    @Override
    public void addEpic(Epic epic) {
        if (!epicMap.containsKey(epic.getUuid())) {
            epicMap.put(epic.getUuid(), epic);
        } else {
            throw new RuntimeException("Have not to put two same Epic in Map");
        }
    }

    @Override
    public void addEpicWithSubtask(Epic epic, Subtask... subtask) {
        if (!epicMap.containsKey(epic.getUuid())) {
            epic.getSubtaskList().addAll(Arrays.asList(subtask));
            epicMap.put(epic.getUuid(), epic);
        } else {
            throw new RuntimeException("Have not to put two same Epic in Map");
        }
    }

    @Override
    public void addSingleTask(SingleTask task) {
        UUID uuid = task.getUuid();
        for (SingleTask currentTask : singleTaskList) {
            if (currentTask.getUuid().equals(uuid)) {
                throw new RuntimeException("Have not to add two same RealTask in taskList");
            }
        }
        singleTaskList.add(task);
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

    /*Before change RealTask you have to put in taskList one or more RealTask*/
    @Override
    public void changeSingleTaskByUuid(UUID uuid, SingleTask newTask) {
        if (singleTaskList.isEmpty()) {
            throw new RuntimeException("Need to add in taskList one or more RealTask, before use this method");
        }
        SingleTask oldTask = this.getSingleTaskByUuid(uuid);
        oldTask.setName(newTask.getName());
        oldTask.setDescription(newTask.getDescription());
        oldTask.setStatus(newTask.getStatus());
    }

    /*Before change subtask you have to put in Map Epic with old Subtask*/
    @Override
    public void changeEpicByUuid(UUID uuid, Epic newEpic) {
        if (epicMap.containsKey(uuid)) {
            Epic oldEpic = this.getEpicByUuid(uuid);
            oldEpic.setName(newEpic.getName());
            oldEpic.setDescription(newEpic.getDescription());
        } else if (epicMap.isEmpty()) {
            throw new RuntimeException("Need to add in epicMap one or more Epic, before use this method");
        } else {
            throw new RuntimeException("Cannot find this UUID in epicMap");
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
        removeTaskFromHistory(getSingleTaskByUuid(uuid));
        if (getSingleTaskList().isEmpty() || !getSingleTaskList().contains(getSingleTaskByUuid(uuid))) {
            throw new RuntimeException("Cannot find this uuid");
        }
        singleTaskList.removeIf(t -> t.getUuid().equals(uuid));
    }

    @Override
    public void removeSubtaskByUuid(UUID uuid) {
        removeTaskFromHistory(getSubtaskByUuid(uuid));
        boolean done = false;
        for (Epic epic : getEpicMap().values()) {
            boolean deleteSubtask = epic.getSubtaskList().
                    removeIf(currentSubtask -> currentSubtask.getUuid().equals(uuid));
            if (deleteSubtask) {
                done = true;
            }
        }
        if (!done) {
            throw new IllegalArgumentException("Cannot find this uuid.This method cannot remove epic or other task " +
                    "by uuid only one subtask");
        }
    }

    @Override
    public void removeEpicByUuid(UUID uuid) {
        removeTaskFromHistory(getEpicByUuid(uuid));
        boolean done = getEpicMap().values().removeIf(epic -> epic.getUuid().equals(uuid));
        if (!done) {
            throw new IllegalArgumentException("Cannot find Epic with this uuid. This method cannot remove task or " +
                    "subtask by uuid only one Epic with its subtask list");
        }
    }

    private void removeTaskFromHistory(AbstractTask task) {
        inMemoryHistoryManager.remove(task.getUuid());
    }

    @Override
    public List<AbstractTask> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }

}

